package com.zxcPro.Controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.zxcPro.service.OrderService;
import com.zxcPro.webSocket.WebSocketServer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
@CrossOrigin
public class payController {

    @Autowired
    private OrderService orderService;


    /**
     * 成功之后，微信平台会请求这个接口，将支付状态的数据传过来
     * @param request httpRequest
     * @return
     * @throws Exception
     */
    @RequestMapping("/callback")
    public String paySuccessful(HttpServletRequest request) throws Exception {
        //1.接收微信平台的数据
        ServletInputStream is = request.getInputStream();//使用request的输入流接收
        StringBuilder sb = new StringBuilder();

        int len = 0;
        byte[] bs = new byte[1024];
        while ((len = is.read(bs)) != -1) {
            sb.append(new String(bs),0, len);
        }
        String s = sb.toString();//data
        Map<String, String> map = WXPayUtil.xmlToMap(s);

        if (!map.isEmpty() && "success".equalsIgnoreCase(map.get("result_code"))) {
            String orderId = map.get("out_trade_no");
            int i = orderService.updateOrderStatus(orderId, "2");

            WebSocketServer.sendMsg(orderId, "1");
            if (i > 0) {
                //响应支付平台
                HashMap<String,String> resp = new HashMap<>();
                resp.put("return_code", "success");
                resp.put("return_msg","OK");
                resp.put("appid",map.get("appid"));
                resp.put("result_code", "success");
                return WXPayUtil.mapToXml(resp);
            }
        }

        return null;
    }
}
