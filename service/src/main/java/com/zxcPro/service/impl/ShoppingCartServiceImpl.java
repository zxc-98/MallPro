package com.zxcPro.service.impl;

import com.zxcPro.dao.ShoppingCartMapper;
import com.zxcPro.entity.ShoppingCart;
import com.zxcPro.entity.ShoppingCartVO;
import com.zxcPro.service.ShoppingCartService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public ResultVO addShoppingCart(ShoppingCart cart) {
        cart.setCartTime(sdf.format(new Date()));
        int insert = shoppingCartMapper.insert(cart);
        if (insert > 0) {
            return new ResultVO(ResStatus.OK, "success", null);
        }
        else {
            return new ResultVO(ResStatus.NO, "error", null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ResultVO listShoppingCartByUserId(int userId) {
        List<ShoppingCartVO> list = shoppingCartMapper.selectShoppingCartByUserId(userId);

        ResultVO resultVO = new ResultVO(ResStatus.OK, "success", list);
        return resultVO;
    }

    @Override
    public ResultVO updateCartNum(int cartNum, int cartId) {
        int i = shoppingCartMapper.updateCartNumByCartId(cartNum, cartId);
        if (i > 0) {
            return new ResultVO(ResStatus.OK, "success", null);
        }
        else {
            return new ResultVO(ResStatus.NO, "fail", null);
        }
    }

    @Override
    public ResultVO selectShoppingCartByCids(String cids) {
        String[] strings = cids.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : strings) {
            list.add(Integer.parseInt(s));
        }
        List<ShoppingCartVO> shoppingCarts = shoppingCartMapper.selectShoppingCartByCid(list);
        return new ResultVO(ResStatus.OK,"success",shoppingCarts);
    }
}
