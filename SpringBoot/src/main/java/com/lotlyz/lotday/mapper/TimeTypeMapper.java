package com.lotlyz.lotday.mapper;

import com.lotlyz.lotday.model.TimeType;
import org.apache.ibatis.annotations.Mapper;

import java.lang.reflect.Type;
import java.util.List;
/**
 * (^_^)
 *时间标签模块
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
@Mapper
public interface TimeTypeMapper {

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


    int insert(TimeType row);


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

    int updateByPrimaryKey(TimeType row);
}