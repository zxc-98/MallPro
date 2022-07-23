package com.zxcPro.dao;

import com.zxcPro.entity.OrderItem;
import com.zxcPro.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper extends GeneralDAO<OrderItem> {

    List<OrderItem> listOrderItemsByOrderId(int orderId);
}