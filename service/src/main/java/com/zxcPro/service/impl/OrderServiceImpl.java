package com.zxcPro.service.impl;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zxcPro.dao.OrderItemMapper;
import com.zxcPro.dao.OrdersMapper;
import com.zxcPro.dao.ProductSkuMapper;
import com.zxcPro.dao.ShoppingCartMapper;
import com.zxcPro.entity.*;
import com.zxcPro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
