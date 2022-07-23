package com.zxcPro.dao;

import com.zxcPro.entity.Orders;
import com.zxcPro.entity.OrdersVO;
import com.zxcPro.general.GeneralDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersMapper extends GeneralDAO<Orders> {
    List<OrdersVO> selectOrders(@Param("userId") String userId,
                                @Param("status") String status,
                                @Param("start") int start,
                                @Param("limit") int limit);
}