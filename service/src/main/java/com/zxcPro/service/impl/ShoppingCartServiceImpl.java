package com.zxcPro.service.impl;

import com.zxcPro.dao.ShoppingCartMapper;
import com.zxcPro.entity.ShoppingCart;
import com.zxcPro.service.ShoppingCartService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public ResultVO addShoppingCart(ShoppingCart cart) {
        int insert = shoppingCartMapper.insert(cart);
        if (insert > 0) {
            return new ResultVO(ResStatus.OK, "success", null);
        }
        else {
            return new ResultVO(ResStatus.NO, "error", null);
        }
    }
}
