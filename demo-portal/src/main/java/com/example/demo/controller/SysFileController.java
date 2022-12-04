package com.example.demo.controller;

import com.example.demo.api.CommonPage;
import com.example.demo.api.CommonResult;
import com.example.demo.entity.SysFile;
import com.example.demo.service.SysFileService;
import com.example.demo.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(tags = "SysFileController", description = "申报信息管理")
@RequestMapping("/sysFile")
public class SysFileController {
    @Resource
    private SysFileService sysFileService;

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    public CommonResult upload(@RequestPart("file") MultipartFile file) {
        SysFile sysFile = sysFileService.uploadFile(file);
        return CommonResult.success(sysFile);
    }

    @ApiOperation(value = "下载文件")
    @GetMapping(value = "/download", produces = "application/json;charset=UTF-8")
    public void download(Long id, HttpServletResponse response) {
        sysFileService.download(id, response);
    }

    @ApiOperation(value = "预览文件")
    @GetMapping(value = "/preview", produces = "application/json;charset=UTF-8")
    public void preview(Long id, HttpServletResponse response) {
        sysFileService.preview(id, response);
    }

    @ApiOperation(value = "删除文件")
    @PostMapping(value = "/remove", produces = "application/json;charset=UTF-8")
    public CommonResult remove(Long id) {
        sysFileService.remove(id);
        return CommonResult.success(null);
    }

    @ApiOperation("分页获取文件信息列表")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public CommonResult<CommonPage<SysFile>> listSysFiles(SysFile sysFile) {
        PageUtils.startPage();
        List<SysFile> list = sysFileService.getSysFiles(sysFile);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("修改文件信息")
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public CommonResult updateSysFile (@RequestBody SysFile sysFile) {
        int count = sysFileService.updateSysFile(sysFile);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据id查询文件信息")
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult<SysFile> getSysFile(@PathVariable Long id) {
        SysFile SysFile = sysFileService.getSysFileById(id);
        return CommonResult.success(SysFile);
    }
}