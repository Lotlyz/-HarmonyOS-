package com.lotlyz.lotday.controller;

import com.lotlyz.lotday.model.Diarybook;
import com.lotlyz.lotday.service.DiaryBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (^_^)
 *日记本模块控制器
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:02
 */
@RestController
@RequestMapping(value = "/DiaryBook")
public class DiaryBookController {
    @Autowired
    DiaryBookService diaryBookService;

    @RequestMapping(value = "/selectByDiarybookUserId")
    public @ResponseBody
    List<Diarybook> list(Integer diarybookUserId){
        return diaryBookService.selectByDiarybookUserId(diarybookUserId);
    }
}
