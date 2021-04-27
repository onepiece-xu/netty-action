package com.ydw.control.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.control.model.db.User;
import com.ydw.control.dao.UserMapper;
import com.ydw.control.model.vo.AuthException;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.control.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> getClusterIds(String token) {
        User one = getUserByToken(token);
        String clusterIds = one.getClusterIds();
        List<String> list = new ArrayList<>();
        if (StringUtil.isNotBlank(clusterIds)){
            list = Arrays.stream(clusterIds.split(",")).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public User getUserByToken(String token) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("token", token);
        User user = getOne(qw);
        if (user == null){
            throw new AuthException();
        }
        return user;
    }
}
