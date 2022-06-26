package com.zxcPro.service;

import com.zxcPro.vo.ResultVO;

public interface UserService {

    ResultVO checkLogin(String name, String pwd);

    //用户注册
    ResultVO userRegister(String name, String pwd);
}
