package com.example.demo.mapper;

import com.example.demo.entity.UmsMemberSign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* UmsMemberSignMapper
* Created by YuanJW on 2023-01-05 15:08:10
*/
@Mapper
public interface UmsMemberSignMapper extends BaseMapper<UmsMemberSign> {
    /**
    * 新增UmsMemberSign对象
    * @param umsMemberSign
    * @return id
    */
    Long saveUmsMemberSign(UmsMemberSign umsMemberSign);
    /**
    * 根据id查询UmsMemberSign对象
    * @param id
    * @return UmsMemberSign
    */
    UmsMemberSign getUmsMemberSignById(Long id);
    /**
    * 根据搜索条件获取UmsMemberSign列表
    * @param umsMemberSign
    * @return
    */
    List<UmsMemberSign> getUmsMemberSigns(UmsMemberSign umsMemberSign);
    /**
    * 修改UmsMemberSign对象
    * @param umsMemberSign
    * @return
    */
    int updateUmsMemberSign(UmsMemberSign umsMemberSign);
    /**
    * 批量删除UmsMemberSign
    * @param ids
    * @return
    */
    int deleteUmsMemberSigns(List<Long> ids);
    /**
    * 统计UmsMemberSign
    * @param umsMemberSign
    * @return
    */
    int countUmsMemberSign(UmsMemberSign umsMemberSign);
}