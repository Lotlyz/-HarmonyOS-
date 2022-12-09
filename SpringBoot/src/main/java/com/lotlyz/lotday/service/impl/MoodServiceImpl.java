package com.lotlyz.lotday.service.impl;

import com.lotlyz.lotday.mapper.MoodMapper;
import com.lotlyz.lotday.model.Mood;
import com.lotlyz.lotday.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (^_^)
 *Mood动态服务实现
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:14
 */
@Service
public class MoodServiceImpl implements MoodService {
    @Autowired
    MoodMapper moodMapper;

    @Override
    public List<Mood> selectAll() {
        return moodMapper.selectAll();
    }

    @Override
    public List<Mood> selectByMoodUserId(Integer moodUserId) {
        return moodMapper.selectByMoodUserId(moodUserId);
    }

    @Override
    public List<Mood> selectByMoodUserIdAndMoodMood(Integer moodUserId, String moodMood) {
        return moodMapper.selectByMoodUserIdAndMoodMood(moodUserId,moodMood);
    }

    @Override
    public List<Mood> selectByPrimaryKey(Integer moodId) {
        return moodMapper.selectByPrimaryKey(moodId);
    }

    @Override
    public int insertSelective(Mood row) {
        return moodMapper.insertSelective(row);
    }

    @Override
    public int deleteByPrimaryKey(Integer moodId) {
        return moodMapper.deleteByPrimaryKey(moodId);
    }

    @Override
    public int updateByPrimaryKeySelective(Mood row) {
        return moodMapper.updateByPrimaryKeySelective(row);
    }
}
