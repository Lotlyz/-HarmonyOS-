package com.lotlyz.lotday.controller;

import com.lotlyz.lotday.mapper.TimeTypeMapper;
import com.lotlyz.lotday.model.TimeType;
import com.lotlyz.lotday.model.User;
import com.lotlyz.lotday.service.TimeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (^_^)
 *时间标签模块控制器
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:03
 */
@RestController
@RequestMapping(value = "/TimeType")
public class TimeTypeCotroller {

    @Autowired
    TimeTypeService timeTypeService;


    /**
     * 查询全部
     * @return
     */
    @RequestMapping(value = "/timeTypeAll")
    public @ResponseBody
    List<TimeType> allList(){
        List<TimeType> allList = timeTypeService.selectAll();
        return allList;
    }

    /**
     * 添加
     * @param row
     * @return
     */
    @RequestMapping(value = "/add")
    public @ResponseBody
    List<TimeType> addList(TimeType row){
        timeTypeService.insertSelective(row);
        List<TimeType> addList = timeTypeService.selectByTimeTypeName(row.getTimetypeName());
        return addList;
    }

    /**
     * 删除
     * @param timetypeId
     * @return
     */
    @RequestMapping(value = "/delete")
    public @ResponseBody
    List<TimeType> delete(Integer timetypeId){
        timeTypeService.deleteByPrimaryKey(timetypeId);
        List<TimeType> deleteList = timeTypeService.selectByPrimaryKey(timetypeId);
        return deleteList;
    }

    /**
     * 修改
     * @param row
     * @return
     */
    @RequestMapping(value = "/update")
    public @ResponseBody
    List<TimeType> update(TimeType row){
        timeTypeService.updateByPrimaryKeySelective(row);
        List<TimeType> updateList = timeTypeService.selectByPrimaryKey(row.getTimetypeId());
        return updateList;
    }


}
