package com.zxcPro.service;

import com.zxcPro.entity.Orders;

import java.util.Map;

public interface OrderService {

    Map<String,String> addOrder(String cids, Orders orders);

    int updateOrderStatus(String orderId, String status);

}
