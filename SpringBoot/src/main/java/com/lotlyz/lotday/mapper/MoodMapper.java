package com.lotlyz.lotday.mapper;

import com.lotlyz.lotday.model.Mood;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * (^_^)
 *Mood动态模块
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
@Mapper
public interface MoodMapper {

    /**
     * 查询全部
     * @return
     */
    List<Mood> selectAll();

    /**
     * 查询用户的Mood
     * @param moodUserId
     * @return
     */
    List<Mood> selectByMoodUserId(Integer moodUserId);

    /**
     * 插入时查询Mood的userid 和 mood内容
     * @param moodUserId
     * @param moodMood
     * @return
     */
    List<Mood> selectByMoodUserIdAndMoodMood(Integer moodUserId,String moodMood);

    /**
     * 根据主键moodId查询
     * @param moodId
     * @return
     */
    List<Mood> selectByPrimaryKey(Integer moodId);

    int insert(Mood row);

    /**
     * 插入
     * @param row
     * @return
     */
    int insertSelective(Mood row);

    /**
     * 删除
     * @param moodId
     * @return
     */
    int deleteByPrimaryKey(Integer moodId);

    /**
     * 更新
     * @param row
     * @return
     */
    int updateByPrimaryKeySelective(Mood row);

    int updateByPrimaryKey(Mood row);
}