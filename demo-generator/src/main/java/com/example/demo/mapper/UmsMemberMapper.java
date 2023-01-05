package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.UmsMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* UmsMemberMapper
* Created by YuanJW on 2022-12-02 15:52:06
*/
@Mapper
public interface UmsMemberMapper extends BaseMapper<UmsMember> {
    /**
    * 新增UmsMember对象
    * @param umsMember
    * @return id
    */
    Long saveUmsMember(UmsMember umsMember);
    /**
    * 根据id查询UmsMember对象
    * @param id
    * @return UmsMember
    */
    UmsMember getUmsMemberById(Long id);
    /**
    * 根据搜索条件获取UmsMember列表
    * @param umsMember
    * @return
    */
    List<UmsMember> getUmsMembers(UmsMember umsMember);
    /**
    * 修改UmsMember对象
    * @param umsMember
    * @return
    */
    int updateUmsMember(UmsMember umsMember);
    /**
    * 批量删除UmsMember
    * @param ids
    * @return
    */
    int deleteUmsMembers(List<Long> ids);
    /**
    * 统计UmsMember
    * @param umsMember
    * @return
    */
    int countUmsMember(UmsMember umsMember);
}