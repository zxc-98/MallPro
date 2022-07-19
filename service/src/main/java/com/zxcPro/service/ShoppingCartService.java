package com.zxcPro.service;

import com.zxcPro.entity.ShoppingCart;
import com.zxcPro.vo.ResultVO;

public interface ShoppingCartService {

    ResultVO addShoppingCart(ShoppingCart cart);

    ResultVO listShoppingCartByUserId(int UserId);

    ResultVO updateCartNum(int cartNum, int cartId);

    ResultVO selectShoppingCartByCids(String cids);
}
