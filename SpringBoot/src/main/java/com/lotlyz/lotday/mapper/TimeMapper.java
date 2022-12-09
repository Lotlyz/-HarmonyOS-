package com.lotlyz.lotday.mapper;

import com.lotlyz.lotday.model.Time;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (^_^)
 *时间模块
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
@Mapper
public interface TimeMapper {
    /**
     * 根据timeId查询
     * @param timeId
     * @return
     */
    List<Time> selectByPrimaryKey(Integer timeId);

    List<Time> selectByTimeUserId(Integer timeUserId);

    List<Time> selectByTimeTimelineAndTimeType(String timeTimeline,String timeType);

    List<Time> selectByTimeIdAndTimeUserId(Integer timeId,Integer timeUserId);


    int insert(Time row);

    /**
     * 插入
     * @param row
     * @return
     */
    int insertSelective(Time row);




    int deleteByPrimaryKey(Integer timeId);


    int updateByPrimaryKeySelective(Time row);

    int updateByPrimaryKey(Time row);
}