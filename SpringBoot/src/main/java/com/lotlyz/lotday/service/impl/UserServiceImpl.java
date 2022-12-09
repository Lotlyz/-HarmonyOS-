package com.lotlyz.lotday.service.impl;

import com.lotlyz.lotday.mapper.UserMapper;
import com.lotlyz.lotday.model.User;
import com.lotlyz.lotday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (^_^)
 *用户服务实现
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:15
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired UserMapper userMapper;

    @Override
    /**
     * 登录
     * @param userPhone
     * @param userPassword
     * @return
     */
    public List<User> login(String userPhone, String userPassword) {
        return userMapper.login(userPhone,userPassword);
    }
    @Override
    /**
     * 根据userID查询
     * @param userId
     * @return
     */
    public List<User> selectByPrimaryKey(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
    @Override
    /**
     * 插入
     * @param row
     * @return
     */
    public int insertSelective(User row) {
        return userMapper.insertSelective(row);
    }

    @Override
    /**
     * 删除
     * @param userId
     * @return
     */
    public int deleteByPrimaryKey(Integer userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    /**
     * 更新
     * @param row
     * @return
     */
    public int updateByPrimaryKeySelective(User row) {
        return userMapper.updateByPrimaryKeySelective(row);
    }
}
