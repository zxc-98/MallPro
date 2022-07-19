package com.zxcPro.dao;

import com.zxcPro.entity.ShoppingCart;
import com.zxcPro.entity.ShoppingCartVO;
import com.zxcPro.general.GeneralDAO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartMapper extends GeneralDAO<ShoppingCart> {

    List<ShoppingCartVO> selectShoppingCartByUserId(int UserId);

    int updateCartNumByCartId(int CartNum, int CartId);

    List<ShoppingCartVO> selectShoppingCartByCid(List<Integer> cids);
}