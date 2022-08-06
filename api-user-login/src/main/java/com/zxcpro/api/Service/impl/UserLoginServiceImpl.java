package com.zxcpro.api.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zxcPro.util.MD5Utils;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import com.zxcpro.api.Service.UserLoginService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Override
    public ResultVO checkLogin(String name, String pwd) {
        //用户查询

        //用户校验
        return null;
    }
}
