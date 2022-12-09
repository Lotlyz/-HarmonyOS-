package com.lotlyz.lotday.service.impl;

import com.lotlyz.lotday.mapper.TimeTypeMapper;
import com.lotlyz.lotday.model.TimeType;
import com.lotlyz.lotday.service.TimeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (^_^)
 *时间标签服务实现
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:15
 */
@Service
public class TimeTypeServiceImpl implements TimeTypeService {

    @Autowired
    TimeTypeMapper timeTypeMapper;

    /**
     * 查询全部
     * @return
     */
    @Override
    public List<TimeType> selectAll() {
        return timeTypeMapper.selectAll();
    }
    /**
     * 根据timetypeId查询
     * @param timetypeId
     * @return
     */
    @Override
    public List<TimeType> selectByPrimaryKey(Integer timetypeId) {
        return timeTypeMapper.selectByPrimaryKey(timetypeId);
    }

    /**
     * 根据timetypeName查询
     * @param timetypeName
     * @return
     */
    @Override
    public List<TimeType> selectByTimeTypeName(String timetypeName) {
        return timeTypeMapper.selectByTimeTypeName(timetypeName);
    }

    /**
     * 插入
     * @param row
     * @return
     */
    @Override
    public int insertSelective(TimeType row) {
        return timeTypeMapper.insertSelective(row);
    }

    /**
     * 删除
     * @param timetypeId
     * @return
     */
    @Override
    public int deleteByPrimaryKey(Integer timetypeId) {
        return timeTypeMapper.deleteByPrimaryKey(timetypeId);
    }

    /**
     * 更新
     * @param row
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(TimeType row) {
        return timeTypeMapper.updateByPrimaryKeySelective(row);
    }
}
