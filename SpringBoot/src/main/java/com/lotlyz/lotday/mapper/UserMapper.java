package com.lotlyz.lotday.mapper;

import com.lotlyz.lotday.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (^_^)
 *用户模块
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
@Mapper
public interface UserMapper {
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


    int insert(User row);

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

    int updateByPrimaryKey(User row);
}