package com.lotlyz.lotday.service.impl;

import com.lotlyz.lotday.mapper.DiarybookMapper;
import com.lotlyz.lotday.model.Diarybook;
import com.lotlyz.lotday.service.DiaryBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (^_^)
 *日记本服务实现
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:14
 */
@Service
public class DiaryBookServiceImpl implements DiaryBookService {
    @Autowired
    DiarybookMapper diarybookMapper;

    @Override
    public List<Diarybook> selectByPrimaryKey(Integer diarybookId) {
        return diarybookMapper.selectByPrimaryKey(diarybookId);
    }

    @Override
    public List<Diarybook> selectByDiarybookUserId(Integer diarybookUserId) {
        return diarybookMapper.selectByDiarybookUserId(diarybookUserId);
    }

    @Override
    public List<Diarybook> selectByDiarybookName(String diarybookName) {
        return diarybookMapper.selectByDiarybookName(diarybookName);
    }

    @Override
    public int insertSelective(Diarybook row) {
        return diarybookMapper.insertSelective(row);
    }

    @Override
    public int deleteByPrimaryKey(Integer diarybookId) {
        return diarybookMapper.deleteByPrimaryKey(diarybookId);
    }

    @Override
    public int updateByPrimaryKeySelective(Diarybook row) {
        return diarybookMapper.updateByPrimaryKeySelective(row);
    }
}
