package com.ydw.control.service;

import com.ydw.control.model.db.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.control.model.vo.ResultInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulh
 * @since 2021-04-15
 */
public interface IUserService extends IService<User> {

    ResultInfo login(String name, String password);

    ResultInfo logout(String token);
}
