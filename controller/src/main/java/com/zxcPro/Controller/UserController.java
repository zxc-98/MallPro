package com.zxcPro.Controller;

import com.zxcPro.entity.Users;
import com.zxcPro.service.UserService;
import com.zxcPro.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@ResponseBody
@RequestMapping("/user")
@Api(value = "提供用户的登录和注册接口", tags = "用户管理")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "username", value = "用户登录用户名", required = true),
            @ApiImplicitParam(dataType = "string", name = "password", value = "用户登录密码", required = true)
    })
    @RequestMapping(path = "/login" , method = RequestMethod.GET)
    public ResultVO login(@RequestParam("username") String name,
                          @RequestParam(value = "password") String pwd) {
        ResultVO resultVO = userService.checkLogin(name, pwd);

        return resultVO;
    }


    @ApiOperation("用户注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "username", value = "用户注册用户名", required = true),
            @ApiImplicitParam(dataType = "string", name = "password", value = "用户注册密码", required = true)
    })
    @PostMapping("/register")
    public ResultVO register(@RequestBody Users user) {
        return userService.userRegister(user.getUsername(), user.getPassword());
    }
}
