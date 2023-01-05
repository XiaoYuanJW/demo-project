package com.example.demo.mapper;

import com.example.demo.entity.UmsFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* UmsFollowMapper
* Created by YuanJW on 2023-01-02 16:26:33
*/
@Mapper
public interface UmsFollowMapper extends BaseMapper<UmsFollow> {
    /**
    * 新增UmsFollow对象
    * @param umsFollow
    * @return id
    */
    Long saveUmsFollow(UmsFollow umsFollow);
    /**
    * 根据id查询UmsFollow对象
    * @param id
    * @return UmsFollow
    */
    UmsFollow getUmsFollowById(Long id);
    /**
    * 根据搜索条件获取UmsFollow列表
    * @param umsFollow
    * @return
    */
    List<UmsFollow> getUmsFollows(UmsFollow umsFollow);
    /**
    * 修改UmsFollow对象
    * @param umsFollow
    * @return
    */
    int updateUmsFollow(UmsFollow umsFollow);
    /**
    * 批量删除UmsFollow
    * @param ids
    * @return
    */
    int deleteUmsFollows(List<Long> ids);
    /**
    * 统计UmsFollow
    * @param umsFollow
    * @return
    */
    int countUmsFollow(UmsFollow umsFollow);
}