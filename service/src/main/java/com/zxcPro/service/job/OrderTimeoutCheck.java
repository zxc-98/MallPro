package com.zxcPro.service.job;

import com.github.wxpay.sdk.WXPay;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zxcPro.dao.OrderItemMapper;
import com.zxcPro.dao.OrdersMapper;
import com.zxcPro.dao.ProductSkuMapper;
import com.zxcPro.entity.OrderItem;
import com.zxcPro.entity.Orders;
import com.zxcPro.entity.ProductSku;
import com.zxcPro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.annotation.Order;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//检查超时任务  检查并关闭订单
@Component
public class OrderTimeoutCheck {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderService orderService;

    private WXPay wxPay = new WXPay(new MyPayConfig());

    //https://cron.qqe2.com/ 表达式生成的网址
    @Scheduled(cron = "0/5 * * * * ? ")
    public void checkAndCloseOrder() {
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status" , 1);

        Date time = new Date(System.currentTimeMillis() - 30*60*1000);
        criteria.andLessThan("createTime", time);

        List<Orders> orders = ordersMapper.selectByExample(example);
        orders.forEach(order -> {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("out_trade_no", order.getOrderId());
                Map<String, String> resp = wxPay.orderQuery(params);
                if ("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))) {
                    //当前订单已支付，则需要修改订单状态为“代发货/已支付” status2
                    Orders updateOrder = new Orders();
                    updateOrder.setOrderId(order.getOrderId());
                    updateOrder.setStatus("2");//已支付
                    ordersMapper.updateByPrimaryKeySelective(updateOrder);
                }
                else {
                    //向位置支付平台发送请求，关闭当前订单的支付链接
                    wxPay.closeOrder(params);
                    //关闭订单
                    orderService.closeOrder(order.getOrderId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

}
