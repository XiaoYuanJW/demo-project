package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SysFilesysFileMapper
* Created by YuanJW on 2022-11-18 16:50:21
*/
@Mapper
public interface SysFileMapper extends BaseMapper<SysFile> {
    /**
    * 新增文件对象
    * @param sysFile
    * @return id
    */
    int saveSysFile(SysFile sysFile);
    /**
    * 根据id查询文件对象
    * @param id
    * @return SysFile
    */
    SysFile getSysFileById(Long id);
    /**
    * 获取文件信息列表
    * @param sysFile
    * @return
    */
    List<SysFile> getSysFiles(SysFile sysFile);
    /**
    * 修改文件对象
    * @param sysFile
    * @return
    */
    int updateSysFile(SysFile sysFile);
    /**
    * 批量删除文件
    * @param ids
    * @return
    */
    int deleteSysFiles(List<Long> ids);
    /**
    * 统计文件数量
    * @param sysFile
    * @return
    */
    int countSysFile(SysFile sysFile);
}