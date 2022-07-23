package com.zxcPro.service;

import com.zxcPro.entity.Orders;
import com.zxcPro.vo.ResultVO;

import java.util.Map;

public interface OrderService {

    Map<String,String> addOrder(String cids, Orders orders);

    int updateOrderStatus(String orderId, String status);

    ResultVO getOrderById(String orderId);

    ResultVO listOrders(String userId, String status, int pageNum, int limit);

    void closeOrder(String orderId);
}
