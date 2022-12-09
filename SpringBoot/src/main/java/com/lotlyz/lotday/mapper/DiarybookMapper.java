package com.lotlyz.lotday.mapper;

import com.lotlyz.lotday.model.Diarybook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (^_^)
 *日记本模块
 * @Author: Liyezhi
 * @Date: 2022/5/30 17:05
 */
@Mapper
public interface DiarybookMapper {

    List<Diarybook> selectByPrimaryKey(Integer diarybookId);
    List<Diarybook> selectByDiarybookUserId(Integer diarybookUserId);
    List<Diarybook> selectByDiarybookName(String diarybookName);


    int insert(Diarybook row);

    int insertSelective(Diarybook row);

    int deleteByPrimaryKey(Integer diarybookId);

    int updateByPrimaryKeySelective(Diarybook row);

    int updateByPrimaryKey(Diarybook row);
}