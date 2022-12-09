package com.lotlyz.lotday.service;

import com.lotlyz.lotday.model.Diarybook;

import java.util.List;

/**
 * (^_^)
 *日记本服务
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:04
 */
public interface DiaryBookService {

    List<Diarybook> selectByPrimaryKey(Integer diarybookId);
    List<Diarybook> selectByDiarybookUserId(Integer diarybookUserId);
    List<Diarybook> selectByDiarybookName(String diarybookName);

    int insertSelective(Diarybook row);

    int deleteByPrimaryKey(Integer diarybookId);

    int updateByPrimaryKeySelective(Diarybook row);
}
