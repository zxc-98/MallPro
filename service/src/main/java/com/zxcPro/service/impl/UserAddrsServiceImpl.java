package com.zxcPro.service.impl;

import com.zxcPro.dao.UserAddrMapper;
import com.zxcPro.entity.UserAddr;
import com.zxcPro.service.UserAddrService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserAddrsServiceImpl implements UserAddrService {

    @Autowired
    private UserAddrMapper userAddrMapper;

    @Override
    public ResultVO listAddrsByUid(int uid) {
        Example example = new Example(UserAddr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", uid);
        criteria.andEqualTo("status", 1);
        List<UserAddr> userAddrs = userAddrMapper.selectByExample(example);

        return new ResultVO(ResStatus.OK, "success", userAddrs);
    }
}
