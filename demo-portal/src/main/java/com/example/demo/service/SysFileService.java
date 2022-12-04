package com.example.demo.service;

import com.example.demo.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* SysFileService-系统文件接口
* Created by YuanJW on 2022-11-18 16:50:21
*/
public interface SysFileService {
    /**
    * 新增SysFile对象
    * @param sysFile
    * @return
    */
    int insertSysFile(SysFile sysFile);
    /**
    * 获取文件信息列表
    * @param sysFile
    * @return
    */
    List<SysFile> getSysFiles(SysFile sysFile);
    /**
    * 根据id获取SysFile对象
    * @param id
    * @return
    */
    SysFile getSysFileById(Long id);
    /**
    * 修改SysFile对象
    * @param sysFile
    * @return
    */
    int updateSysFile(SysFile sysFile);
    /**
    * 批量删除SysFile
    * @param ids
    * @return
    */
    int deleteSysFileByIds(List<Long> ids);

    /**
     * 上传文件
     * @param file
     * @return
     */
    SysFile uploadFile(MultipartFile file);

    /**
     * 获取文件信息列表
     * @param files
     * @return
     */
    List<SysFile> getFileList(String files);

    /**
     * 下载文件
     * @param id
     * @param response
     */
    void download(Long id, HttpServletResponse response);

    /**
     * 删除文件
     * @param id
     */
    void remove(Long id);

    /**
     * 批量删除文件
     * @param files
     */
    void removeBatch(String files);

    /**
     * 预览文件
     * @param id
     * @param response
     */
    void preview(Long id, HttpServletResponse response);
}