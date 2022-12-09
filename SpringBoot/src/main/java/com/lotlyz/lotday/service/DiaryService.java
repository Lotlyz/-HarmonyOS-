package com.lotlyz.lotday.service;

import com.lotlyz.lotday.model.Diary;

import java.util.List;

/**
 * (^_^)
 * 日记服务
 * @Author: Liyezhi
 * @Date: 2022/5/19 14:25
 */

public interface DiaryService {

    Diary selectByPrimaryKey(Integer diaryId);

    /**
     * 根据diaryUserId查询日记信息
     * @param diaryUserId
     * @return
     */
    List<Diary> selectByDiaryUserId(Integer diaryUserId);

    /**
     * 根据diaryUserId和diaryDiarybookId查询日记信息
     * @param diaryUserId
     * @param diaryDiarybookId
     * @return
     */
    List<Diary> selectByDiaryUserIdAndDiaryDiarybookId(Integer diaryUserId,Integer diaryDiarybookId);


    List<Diary> selectByDiaryTitleAndDiaryContent(String diaryTitle,String diaryContent);

    int insertSelective(Diary row);

    int deleteByPrimaryKey(Integer diaryId);

    int updateByPrimaryKeySelective(Diary row);
}
