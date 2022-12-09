package com.lotlyz.lotday.controller;

import com.lotlyz.lotday.mapper.DiaryMapper;
import com.lotlyz.lotday.model.Diary;
import com.lotlyz.lotday.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (^_^)
 * 日记模块控制器
 * @Author: Liyezhi
 * @Date: 2022/5/19 14:23
 */

//@Controller
@RestController //相当于控制层类上加@Controller+方法上加@ResponseBody
//意味着当前控制层类中所有方法返还的都是JSON对象
@RequestMapping(value = "/Diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @RequestMapping(value = "/selectByDiaryUserId")
    public @ResponseBody
    List<Diary> diaryList(Integer diaryUserId){
        List<Diary> diaryList = diaryService.selectByDiaryUserId(diaryUserId);
        return diaryList;
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public List<Diary> deletelist(Integer diaryId){
        diaryService.deleteByPrimaryKey(diaryId);
        return diaryService.selectByDiaryUserId(diaryId);
    }
}
