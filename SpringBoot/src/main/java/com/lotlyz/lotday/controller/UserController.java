package com.lotlyz.lotday.controller;

import com.lotlyz.lotday.model.User;
import com.lotlyz.lotday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ^_^
 * 用户模块控制器
 * @Author: Liyezhi
 * @Date: 2022/5/19 17:11
 */

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 登录，返回查询结果集
     * @param userPhone
     * @param userPassword
     * @return
     */
    @RequestMapping(value = "/login")
    public @ResponseBody
    List<User> loginList(String userPhone, String userPassword){
        List<User> userList = userService.login(userPhone,userPassword);
        return userList;
    }

    /**
     * 插入新数据(注册)，返回结果集
     * @param row
     * @return
     */
    @RequestMapping(value = "/userAdd")
    public @ResponseBody
    List<User> addList(User row){
        userService.insertSelective(row);
        List<User> addList = userService.login(row.getUserPhone(), row.getUserPassword());
        return addList;
    }

    /**
     * 根据userId删除user数据，返回查询结果集
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userDelete")
    public @ResponseBody
    List<User> deleteList(Integer userId){
        userService.deleteByPrimaryKey(userId);
        List<User> deleteList = userService.selectByPrimaryKey(userId);
        return deleteList;
    }

    /**
     * 更新数据，返回结果集
     * @param row
     * @return
     */
    @RequestMapping(value = "/userUpdate")
    public @ResponseBody
    List<User> updateList(User row){
        userService.updateByPrimaryKeySelective(row);
        List<User> updateList = userService.selectByPrimaryKey(row.getUserId());
        return updateList;
    }
}