package com.lotlyz.lotday.mapper;

import com.lotlyz.lotday.model.Diary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (^_^)
 *日记模块
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
@Mapper
public interface DiaryMapper {

    Diary selectByPrimaryKey(Integer diaryId);

    List<Diary> selectByDiaryUserId(Integer diaryUserId);
    List<Diary> selectByDiaryUserIdAndDiaryDiarybookId(Integer diaryUserId,Integer diaryDiarybookId);
    List<Diary> selectByDiaryTitleAndDiaryContent(String diaryTitle,String diaryContent);

    int insert(Diary row);

    int insertSelective(Diary row);

    int deleteByPrimaryKey(Integer diaryId);

    int updateByPrimaryKeySelective(Diary row);

    int updateByPrimaryKey(Diary row);
}