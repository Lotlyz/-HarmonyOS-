package com.lotlyz.lotday.controller;

import com.lotlyz.lotday.model.Time;
import com.lotlyz.lotday.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (^_^)
 *时间模块控制器
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:02
 */
@RestController
@RequestMapping(value = "/Time")
public class TimeCotroller {

    @Autowired
    TimeService timeService;

    @RequestMapping(value = "/selectByTimeUserId")
    public List<Time> selectByTimeUserId(Integer timeUserId){
        List<Time> list = timeService.selectByTimeUserId(timeUserId);
        return list;
    }
    @RequestMapping(value = "/delete")
    public List<Time> deleteByPrimaryKey(Integer timeId){
        timeService.deleteByPrimaryKey(timeId);
        List<Time> list = timeService.selectByPrimaryKey(timeId);
        return list;

    }
}
