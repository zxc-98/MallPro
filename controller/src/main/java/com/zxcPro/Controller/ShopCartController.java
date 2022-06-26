package com.zxcPro.Controller;

import com.zxcPro.util.Base64Utils;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopcart")
@CrossOrigin
@Api(value = "提供购物车相关接口" , tags = "购物车管理")
public class ShopCartController {

    @GetMapping("/list")
    @ApiOperation("获取购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "token", value = "授权令牌", required = true),
    })
    public ResultVO listCarts(String token) {

        if (token == null) {
            return new ResultVO(ResStatus.NO, "请先登录", null);
        }

        //String decode = Base64Utils.decode(token);
        JwtParser parser = Jwts.parser();
        parser.setSigningKey("zxc666");//密钥一致
        try {
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);

            Claims body = claimsJws.getBody();//data
            String subject = body.getSubject();//token set subject
            String value1 = body.get("key1", String.class);// get map value
            return new ResultVO(ResStatus.OK, "sucess", null);
        }
        catch (Exception e) {
            return new ResultVO(ResStatus.NO, "登录过期，请重新登录！", null);
        }

    }
}
