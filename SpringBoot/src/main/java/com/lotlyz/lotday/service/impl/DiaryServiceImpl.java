package com.lotlyz.lotday.service.impl;

import com.lotlyz.lotday.mapper.DiaryMapper;
import com.lotlyz.lotday.model.Diary;
import com.lotlyz.lotday.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (^_^)
 * 日记服务实现
 * @Author: Liyezhi
 * @Date: 2022/5/19 14:26
 */

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;


    @Override
    public Diary selectByPrimaryKey(Integer diaryId) {
        return diaryMapper.selectByPrimaryKey(diaryId);
    }

    @Override
    public List<Diary> selectByDiaryUserId(Integer diaryUserId) {
        return diaryMapper.selectByDiaryUserId(diaryUserId);
    }

    @Override
    public List<Diary> selectByDiaryUserIdAndDiaryDiarybookId(Integer diaryUserId, Integer diaryDiarybookId) {
        return diaryMapper.selectByDiaryUserIdAndDiaryDiarybookId(diaryUserId,diaryDiarybookId);
    }

    @Override
    public List<Diary> selectByDiaryTitleAndDiaryContent(String diaryTitle, String diaryContent) {
        return diaryMapper.selectByDiaryTitleAndDiaryContent(diaryTitle,diaryContent);
    }

    @Override
    public int insertSelective(Diary row) {
        return diaryMapper.insertSelective(row);
    }

    @Override
    public int deleteByPrimaryKey(Integer diaryId) {
        return diaryMapper.deleteByPrimaryKey(diaryId);
    }

    @Override
    public int updateByPrimaryKeySelective(Diary row) {
        return diaryMapper.updateByPrimaryKeySelective(row);
    }
}
