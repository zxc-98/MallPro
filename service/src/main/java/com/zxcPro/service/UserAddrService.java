package com.zxcPro.service;

import com.zxcPro.entity.UserAddr;
import com.zxcPro.vo.ResultVO;

import java.util.List;

public interface UserAddrService {
    ResultVO listAddrsByUid(int uid);
}
