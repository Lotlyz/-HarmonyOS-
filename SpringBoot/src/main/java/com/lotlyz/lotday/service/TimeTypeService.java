package com.lotlyz.lotday.service;

import com.lotlyz.lotday.model.TimeType;

import java.util.List;

/**
 * (^_^)
 *时间分类标签服务
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
public interface TimeTypeService {


    /**
     * 查询全部
     * @return
     */
    List<TimeType> selectAll();


    /**
     * 根据timeTypeID查询
     * @param timetypeId
     * @return
     */
    List<TimeType> selectByPrimaryKey(Integer timetypeId);


    /**
     * 根据timetypeName查询
     * @param timetypeName
     * @return
     */
    List<TimeType> selectByTimeTypeName(String timetypeName);


    /**
     * 插入
     * @param row
     * @return
     */
    int insertSelective(TimeType row);


    /**
     * 删除
     * @param timetypeId
     * @return
     */
    int deleteByPrimaryKey(Integer timetypeId);


    /**
     * 更新
     * @param row
     * @return
     */
    int updateByPrimaryKeySelective(TimeType row);

}
