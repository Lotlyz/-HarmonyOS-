package com.lotlyz.lotday.service;

import com.lotlyz.lotday.model.Time;

import java.util.List;

/**
 * (^_^)
 *时间服务
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:04
 */
public interface TimeService {

    List<Time> selectByPrimaryKey(Integer timeId);


    List<Time> selectByTimeUserId(Integer timeUserId);

    List<Time> selectByTimeTimelineAndTimeType(String timeTimeline, String timeType);

    List<Time> selectByTimeIdAndTimeUserId(Integer timeId,Integer timeUserId);

    /**
     * 插入
     * @param row
     * @return
     */
    int insertSelective(Time row);

    int deleteByPrimaryKey(Integer timeId);

    int updateByPrimaryKeySelective(Time row);
}
