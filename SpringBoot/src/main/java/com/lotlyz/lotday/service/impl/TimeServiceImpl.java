package com.lotlyz.lotday.service.impl;

import com.lotlyz.lotday.mapper.TimeMapper;
import com.lotlyz.lotday.model.Time;
import com.lotlyz.lotday.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (^_^)
 *时间服务实现
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:15
 */
@Service
public class TimeServiceImpl implements TimeService {
    @Autowired
    TimeMapper timeMapper;

    @Override
    public List<Time> selectByPrimaryKey(Integer timeId) {
        return timeMapper.selectByPrimaryKey(timeId);
    }

    @Override
    public List<Time> selectByTimeUserId(Integer timeUserId) {
        return timeMapper.selectByTimeUserId(timeUserId);
    }

    @Override
    public List<Time> selectByTimeTimelineAndTimeType(String timeTimeline, String timeType) {
        return timeMapper.selectByTimeTimelineAndTimeType(timeTimeline,timeType);
    }

    @Override
    public List<Time> selectByTimeIdAndTimeUserId(Integer timeId, Integer timeUserId) {
        return timeMapper.selectByTimeIdAndTimeUserId(timeId,timeUserId);
    }

    @Override
    public int insertSelective(Time row) {
        return timeMapper.insertSelective(row);
    }

    @Override
    public int deleteByPrimaryKey(Integer timeId) {
        return timeMapper.deleteByPrimaryKey(timeId);
    }

    @Override
    public int updateByPrimaryKeySelective(Time row) {
        return timeMapper.updateByPrimaryKeySelective(row);
    }
}
