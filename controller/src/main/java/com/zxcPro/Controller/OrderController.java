package com.zxcPro.Controller;

import com.github.wxpay.sdk.WXPay;
import com.zxcPro.config.MyPayConfig;
import com.zxcPro.entity.Orders;
import com.zxcPro.service.OrderService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin
@Api(value = "订单相关接口" , tags = "订单接口管理")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/add")
    public ResultVO addOrder(String cids,@RequestBody Orders order) {
        ResultVO resultVO = null;
        try {
            Map<String, String> orderInfo = orderService.addOrder(cids, order);
            String orderId = orderInfo.get("orderId");
            if (orderId != null) {
                //当前订单信息
                Map<String, String> data = new HashMap<>();
                data.put("body", orderInfo.get("productNames"));
                data.put("out_trade_no", orderId);//当前订单的编号作为支付的交易号
                data.put("fee_type", "CNY");
                data.put("trade_type","Native");//交易类型
                data.put("total_fee", order.getActualAmount()*100 + "");//total amount money
                data.put("notify_url", "/pay/callback");

                WXPay wxPay = new WXPay(new MyPayConfig());
                Map<String, String> resp = wxPay.unifiedOrder(data);

                //String code_url = resp.get("code_url");
                orderInfo.put("payUrl", "https://www.baidu.com/");
                //微信支付的短链接因为没有商家号码，所以做不了
                resultVO = new ResultVO(ResStatus.OK,"提交订单",orderInfo);
            }
            else {
                resultVO = new ResultVO(ResStatus.NO,"提交订单失败",null);
            }
        }
        catch (SQLException q) {
            resultVO = new ResultVO(ResStatus.NO,"提交订单失败",null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultVO;
    }

    @GetMapping("/status/{oid}")
    public ResultVO getOrderStatus(@PathVariable("oid") String orderId,@RequestHeader("token")String token){
        ResultVO resultVO = orderService.getOrderById(orderId);
        return resultVO;
    }

    @GetMapping("/list")
    @ApiOperation("订单查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "userId", value = "用户ID",required = true),
            @ApiImplicitParam(dataType = "string",name = "status", value = "订单状态",required = false),
            @ApiImplicitParam(dataType = "int",name = "pageNum", value = "页码",required = true),
            @ApiImplicitParam(dataType = "int",name = "limit", value = "每页条数",required = true)
    })
    public ResultVO list(@RequestHeader("token")String token,
                         String userId,String status,int pageNum,int limit){
        ResultVO resultVO = orderService.listOrders(userId, status, pageNum, limit);
        return resultVO;
    }

}
