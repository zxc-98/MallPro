package com.zxcpro.api.Service;

import com.zxcPro.vo.ResultVO;


public interface UserLoginService {

    ResultVO checkLogin(String name, String pwd);
}
