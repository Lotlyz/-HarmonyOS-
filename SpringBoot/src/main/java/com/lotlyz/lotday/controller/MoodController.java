package com.lotlyz.lotday.controller;

import com.lotlyz.lotday.mapper.MoodMapper;
import com.lotlyz.lotday.model.Mood;
import com.lotlyz.lotday.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (^_^)
 *Mood动态模块控制器
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:01
 */

@RestController
@RequestMapping(value = "/Mood")
public class MoodController {

    @Autowired
    MoodService moodService;

    @Autowired
    MoodMapper moodMapper;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(value = "/moodAll")
    public @ResponseBody
    List<Mood> selectAll(){
        List<Mood> allList = moodService.selectAll();
        return allList;
    }

    /**
     * 根据moodUserId查询所有
     * @return
     */
    @RequestMapping(value = "/moodUser")
    public @ResponseBody
    List<Mood> selectByMoodUserId(Integer moodUserId){
        List<Mood> userList = moodService.selectByMoodUserId(moodUserId);
        return userList;
    }


    /**
     * 插入新数据，返回结果集
     * @param row
     * @return
     */
    @RequestMapping(value = "/moodAdd")
    public @ResponseBody
    List<Mood> addList(Mood row){
        moodService.insertSelective(row);
        List<Mood> addList = moodService.selectByMoodUserIdAndMoodMood(row.getMoodUserId(), row.getMoodMood());
        return addList;
    }

    /**
     * 根据moodId删除mood数据，返回查询结果集
     * @param moodId
     * @return
     */
    @RequestMapping(value = "/moodDelete")
    public @ResponseBody
    List<Mood> deleteList(Integer moodId){
        moodService.deleteByPrimaryKey(moodId);
        List<Mood> deleteList = moodService.selectByPrimaryKey(moodId);
        return deleteList;
    }

    /**
     * 更新数据，返回结果集
     * @param row
     * @return
     */
    @RequestMapping(value = "/moodUpdate")
    public @ResponseBody
    List<Mood> updateList(Mood row){
        moodService.updateByPrimaryKeySelective(row);
        List<Mood> updateList = moodService.selectByPrimaryKey(row.getMoodId());
        return updateList;
    }

}
