package com.zxcPro.service.impl;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zxcPro.dao.OrderItemMapper;
import com.zxcPro.dao.OrdersMapper;
import com.zxcPro.dao.ProductSkuMapper;
import com.zxcPro.dao.ShoppingCartMapper;
import com.zxcPro.entity.*;
import com.zxcPro.service.OrderService;
import com.zxcPro.util.PageHelper;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public Map<String, String> addOrder(String cids, Orders order) {
        String[] split = cids.split(",");
        List<Integer> list = new ArrayList<>();//存入传入的cid集合
        for (String s : split) {
            int cid = Integer.parseInt(s);
            list.add(cid);
        }

        List<ShoppingCartVO> shoppingCarts = shoppingCartMapper.selectShoppingCartByCid(list);
        boolean f = true;//库存不足警告
        StringBuilder itemsString = new StringBuilder();//订单中订单项的名称

        for (ShoppingCartVO shoppingCart : shoppingCarts) {
            if (shoppingCart.getSkuStock() < Integer.parseInt(shoppingCart.getCartNum())) {
                f = false;
            }
            itemsString.append(shoppingCart.getProductName()).append(",");
        }

        if (f && shoppingCarts.size() > 0) {
            order.setUntitled(itemsString.toString());//设置产品名称
            order.setCreateTime(new Date());
            order.setStatus("1");

            String orderId = UUID.randomUUID().toString().replace("-", "");//将-号去除
            order.setOrderId(orderId);
            ordersMapper.insert(order);

            for (ShoppingCartVO sc : shoppingCarts) {
                // 订单快照
                int buyCounts = Integer.parseInt(sc.getCartNum());
                String itemId = String.valueOf(System.currentTimeMillis() + new Random().nextInt(89999) + 10000);

                OrderItem orderItem = new OrderItem(itemId, orderId, sc.getProductId(),sc.getProductName(),
                        sc.getProductImg(),sc.getSkuId(), sc.getSkuName(), new BigDecimal(sc.getSellPrice()),
                        buyCounts,new BigDecimal(sc.getSellPrice() * buyCounts), new Date(), new Date(), 0);

                orderItemMapper.insert(orderItem);

                //扣减库存，更新
                int curStock = sc.getSkuStock() - Integer.parseInt(sc.getCartNum());
                String skuId = sc.getSkuId();
                ProductSku productSku = new ProductSku();
                productSku.setStock(curStock);
                productSku.setSkuId(skuId);

                productSkuMapper.updateByPrimaryKeySelective(productSku);
            }

            //将购物车记录删除
            for (Integer l : list) {
                shoppingCartMapper.deleteByPrimaryKey(l);
            }

            Map<String, String> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("productNames", itemsString.toString());

            return map;
        }
        else {
            //库存不足
            return null;
        }

    }

    @Override
    public int updateOrderStatus(String orderId, String status) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setStatus(status);
        int i = ordersMapper.updateByPrimaryKeySelective(orders);
        return i;
    }

    @Override
    public ResultVO getOrderById(String orderId) {
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        return new ResultVO(ResStatus.OK,"sucesss",order.getStatus());
    }

    @Override
    public ResultVO listOrders(String userId, String status, int pageNum, int limit) {
            //1.分页查询
            int start = (pageNum-1)*limit;
            List<OrdersVO> ordersVOS = ordersMapper.selectOrders(userId, status, start, limit);

            //2.查询总记录数
            Example example = new Example(Orders.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("userId",userId);
            if(status != null && !"".equals(status)){
                criteria.andLike("status",status);
            }
            int count = ordersMapper.selectCountByExample(example);

            //3.计算总页数
            int pageCount = count%limit==0?count/limit:count/limit+1;

            //4.封装数据
            PageHelper<OrdersVO> pageHelper = new PageHelper<>(count, pageCount, ordersVOS);;
            return new ResultVO(ResStatus.OK,"success",pageHelper);
        }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void closeOrder(String orderId) {
        //修改当前订单 status=6 close_type=1超时未支付
        //还原库存 先根据当前订单编号 查询商品快照（skuid buy_count） 修改product_sku
        Orders cancelOrder = new Orders();
        cancelOrder.setOrderId(orderId);
        cancelOrder.setStatus("6");//订单关闭
        cancelOrder.setCloseType(1);//超时未支付
        ordersMapper.updateByPrimaryKeySelective(cancelOrder);


        Example exampleItem = new Example(OrderItem.class);
        Example.Criteria itemCriteria = exampleItem.createCriteria();
        itemCriteria.andEqualTo("orderId", orderId);
        orderItemMapper.selectByExample(exampleItem).forEach(item ->{
            ProductSku productSku = productSkuMapper.selectByPrimaryKey(item.getSkuId());
            productSku.setStock(productSku.getStock() + item.getBuyCounts());
            productSkuMapper.updateByPrimaryKey(productSku);
        });
    }
}
