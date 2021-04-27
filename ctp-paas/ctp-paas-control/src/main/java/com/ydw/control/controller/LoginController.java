package com.ydw.control.controller;

import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1518:03
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController{

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResultInfo login(@RequestBody HashMap<String, String> params){
        String name = params.get("name");
        String password = params.get("password");
        return userService.login(name, password);
    }

    @PostMapping("/logout")
    public ResultInfo logout(){
        String token = getToken();
        return userService.logout(token);
    }
}
