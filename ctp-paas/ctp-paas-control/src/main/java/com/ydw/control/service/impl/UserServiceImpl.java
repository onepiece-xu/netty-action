package com.ydw.control.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.control.model.db.User;
import com.ydw.control.dao.UserMapper;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulh
 * @since 2021-04-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public ResultInfo login(String name, String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("name", name);
        qw.eq("password", password);
        User user = getOne(qw);
        if (user == null){
            return ResultInfo.fail("用户名或密码错误！");
        }else{
            String token = DigestUtils.md5Hex(user.getId()+System.currentTimeMillis());
            user.setToken(token);
            updateById(user);
            return ResultInfo.success("登录成功！",token);
        }
    }

    @Override
    public ResultInfo logout(String token) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("token", token);
        User one = getOne(qw);
        if (one != null){
            one.setToken(null);
            updateById(one);
        }
        return ResultInfo.success();
    }
}
