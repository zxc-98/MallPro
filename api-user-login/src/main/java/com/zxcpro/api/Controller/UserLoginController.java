package com.zxcpro.api.Controller;


import com.zxcPro.vo.ResultVO;
import com.zxcpro.api.Service.UserLoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@ResponseBody
@RequestMapping("/user")
@CrossOrigin
public class UserLoginController {

    @Resource
    UserLoginService userLoginService;

    @GetMapping(path = "/login")
    public ResultVO login(@RequestParam("username") String name,
                          @RequestParam(value = "password") String pwd) {

        return userLoginService.checkLogin(name, pwd);
    }
}
