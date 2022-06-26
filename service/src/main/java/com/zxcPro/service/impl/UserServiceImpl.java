package com.zxcPro.service.impl;

import com.zxcPro.dao.UsersMapper;
import com.zxcPro.entity.Users;
import com.zxcPro.service.UserService;
import com.zxcPro.util.Base64Utils;
import com.zxcPro.util.MD5Utils;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UsersMapper usersMapper;

    @Override
    public ResultVO checkLogin(String name, String pwd) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", name);

        List<Users> users = usersMapper.selectByExample(example);

        if (users.size() == 0) {
            return new ResultVO(ResStatus.NO, "登录失败，用户不存在！！", null);
        }
        else {
            String md5Pwd = MD5Utils.md5(pwd);
            Users user = users.get(0);
            if (md5Pwd.equals(user.getPassword())) {
                Map<String, Object> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");

                //登录验证成功，返回生成的令牌token（token是按照一定规则生成的字符串）
                //String token = Base64Utils.encode(name + "zxc666");
                JwtBuilder builder = Jwts.builder();
                String token = builder.setSubject(name)//主题：token中存在的数据
                        .setIssuedAt(new Date()) //设置token生成时间
                        .setId(user.getUserId() + "")//设置用户id为 token id
                        .setClaims(map) //map中存放用户的角色权限信息
                        .setExpiration(new Date(System.currentTimeMillis() + 24*60*60* 1000)) //设置token的过期时间
                        .signWith(SignatureAlgorithm.HS256, "zxc666") //设置加密方式和加密密码
                        .compact();

                return new ResultVO(ResStatus.OK, token, user);
            }
            else {
                return new ResultVO(ResStatus.NO, "登录失败，密码错误!",null);
            }
        }
    }

    @Override
    @Transactional
    public ResultVO userRegister(String name, String pwd) {
        synchronized (this) {
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username", name);

            List<Users> users = usersMapper.selectByExample(example);

            if (users.size() == 0) {
                String md5Pwd = MD5Utils.md5(pwd);
                Users user = new Users();
                user.setUsername(name);
                user.setPassword(md5Pwd);
                user.setUserImg("img/default.png");
                user.setUserRegtime(new Date());
                user.setUserModtime(new Date());

                int res = usersMapper.insert(user);
                if (res > 0) {
                    return new ResultVO(ResStatus.OK, "注册成功", null);
                }
                else {
                    return new ResultVO(ResStatus.NO, "注册失败", null);
                }
            }
            else {
                return new ResultVO(ResStatus.NO, "用户名已被注册", null);
            }
        }
    }
}
