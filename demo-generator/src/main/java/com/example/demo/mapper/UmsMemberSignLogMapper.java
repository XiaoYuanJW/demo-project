package com.example.demo.mapper;

import com.example.demo.entity.UmsMemberSign;
import com.example.demo.entity.UmsMemberSignLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* UmsMemberSignLogMapper
* Created by YuanJW on 2023-01-05 15:08:10
*/
@Mapper
public interface UmsMemberSignLogMapper extends BaseMapper<UmsMemberSignLog> {
    /**
    * 新增UmsMemberSignLog对象
    * @param umsMemberSign
    * @return id
    */
    Long saveUmsMemberSignLog(UmsMemberSign umsMemberSign);
    /**
    * 根据id查询UmsMemberSignLog对象
    * @param id
    * @return UmsMemberSignLog
    */
    UmsMemberSignLog getUmsMemberSignLogById(Long id);
    /**
    * 根据搜索条件获取UmsMemberSignLog列表
    * @param umsMemberSign
    * @return
    */
    List<UmsMemberSignLog> getUmsMemberSignLogs(UmsMemberSignLog umsMemberSign);
    /**
    * 修改UmsMemberSignLog对象
    * @param umsMemberSign
    * @return
    */
    int updateUmsMemberSignLog(UmsMemberSignLog umsMemberSign);
    /**
    * 批量删除UmsMemberSignLog
    * @param ids
    * @return
    */
    int deleteUmsMemberSignLogs(List<Long> ids);
    /**
    * 统计UmsMemberSignLog
    * @param umsMemberSign
    * @return
    */
    int countUmsMemberSignLog(UmsMemberSignLog umsMemberSign);
}