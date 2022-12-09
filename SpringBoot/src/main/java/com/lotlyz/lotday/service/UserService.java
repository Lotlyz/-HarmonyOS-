package com.lotlyz.lotday.service;

import com.lotlyz.lotday.model.User;

import java.util.List;

/**
 * (^_^)
 *用户服务
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
public interface UserService {
    /**
     * 登录
     * @param userPhone
     * @param userPassword
     * @return
     */
    List<User> login(String userPhone, String userPassword);


    /**
     * 根据userID查询
     * @param userId
     * @return
     */
    List<User> selectByPrimaryKey(Integer userId);


    /**
     * 插入
     * @param row
     * @return
     */
    int insertSelective(User row);


    /**
     * 删除
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Integer userId);


    /**
     * 更新
     * @param row
     * @return
     */
    int updateByPrimaryKeySelective(User row);

}
