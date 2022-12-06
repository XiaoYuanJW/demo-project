package com.example.demo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.demo.entity.SysFile;
import com.example.demo.mapper.SysFileMapper;
import com.example.demo.service.SysFileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* SysFileServiceImpl--系统文件接口实现类
* Created by YuanJW on 2022-11-18 16:50:21
*/
@Slf4j
@Service
public class SysFileServiceImpl implements SysFileService {
    @Resource
    private SysFileMapper sysFileMapper;
    @Value("${minio.endpoint}")
    private String ENDPOINT;
    @Value("${minio.bucket}")
    private String BUCKET_NAME;
    @Value("${minio.accessKey}")
    private String ACCESS_KEY;
    @Value("${minio.secretKey}")
    private String SECRET_KEY;

    @Override
    public int insertSysFile(SysFile sysFile){
        int count = sysFileMapper.insert(sysFile);
        return count;
    }

    @Override
    public SysFile getSysFileById(Long id){
        return sysFileMapper.selectById(id);
    }

    @Override
    public List<SysFile> getSysFiles(SysFile sysFile){
        List<SysFile> sysFiles = sysFileMapper.getSysFiles(sysFile);
        return sysFiles;
    }

    @Override
    public int updateSysFile(SysFile sysFile){
        int count = sysFileMapper.updateById(sysFile);
        return count;
    }

    @Override
    public int deleteSysFileByIds(List<Long> ids){
        return sysFileMapper.deleteBatchIds(ids);
    }

    @Override
    public SysFile uploadFile(MultipartFile file) {
        // 创建MinIO的Java客户端
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!isExist) {
                // 创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
                // 设置只读权限
                SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build();
                minioClient.setBucketPolicy(setBucketPolicyArgs);
            } else {
                log.info("存储桶已经存在");
            }
            // 雪花算法生成文件的唯一id
            Long id = IdWorker.getId();
            // 获取文件原始名称
            String filename = file.getOriginalFilename();
            // 获取文件后缀
            String suffix = StrUtil.isNotEmpty(filename) ? StrUtil.subAfter(filename, '.', true) : null;
            // 设置存储对象名称
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String objectName = sdf.format(new Date()) + "/" + id + '.' + suffix;
            // 上传文件至存储桶中
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
            minioClient.putObject(putObjectArgs);
            log.info("文件上传成功：{}", objectName);
            // 存储文件信息
            SysFile sysFile = SysFile.builder()
                    .id(id)
                    .location(2)
                    .bucket(BUCKET_NAME)
                    .originName(filename)
                    .suffix(suffix)
                    .size(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024)).setScale(0, BigDecimal.ROUND_HALF_UP))
                    .objectName(objectName)
                    .path(BUCKET_NAME + '/' + objectName)
                    .gmtCreate(new Date())
                    .build();
            sysFileMapper.insert(sysFile);
            return sysFileMapper.selectById(id);
        } catch (Exception e) {
            log.info("文件上传失败：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<SysFile> getFileList(String files) {
        List<SysFile> fileList = Arrays.stream(files.split(","))
                .map(file -> sysFileMapper.selectById(Convert.toLong(file)))
                .distinct()
                .collect(Collectors.toList());
        return fileList;
    }

    @Override
    public void download(Long id, HttpServletResponse response) {
        SysFile sysFile = sysFileMapper.selectById(id);
        if (ObjectUtil.isNull(sysFile)) {
            throw new RuntimeException("文件不存在");
        }
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            // 获取文件字节码
            byte[] fileBytes = IoUtil.readBytes(minioClient.getObject(GetObjectArgs.builder().bucket(BUCKET_NAME).object(sysFile.getObjectName()).build()));
            // 设置response
            response.reset();
            response.setHeader("Content-Disposition" , "attachment; filename=\"" + URLUtil.encode(sysFile.getOriginName()) + "\"");
            response.addHeader("Content-Length" , "" + fileBytes.length);
            response.setHeader("Access-Control-Allow-Origin" , "*");
            response.setHeader("Access-Control-Expose-Headers" , "Content-Disposition");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            // 获取outputStream
            ServletOutputStream outputStream = response.getOutputStream();
            IoUtil.write(response.getOutputStream(), true, fileBytes);
        } catch (Exception e) {
            log.info("文件下载失败：{}", e.getMessage());
            throw new RuntimeException("文件下载失败");
        }
    }

    @Override
    public void preview(Long id, HttpServletResponse response) {
        SysFile sysFile = sysFileMapper.selectById(id);
        if (ObjectUtil.isNull(sysFile)) {
            throw new RuntimeException("文件不存在");
        }
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            // 获取文件字节码
            byte[] fileBytes = IoUtil.readBytes(minioClient.getObject(GetObjectArgs.builder().bucket(BUCKET_NAME).object(sysFile.getObjectName()).build()));
            // 设置contentType
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            // 获取outputStream
            ServletOutputStream outputStream = response.getOutputStream();
            IoUtil.write(outputStream, true, fileBytes);
        } catch (Exception e) {
            log.info("文件下载失败：{}", e.getMessage());
            throw new RuntimeException("文件下载失败");
        }
    }

    @Override
    public void remove(Long id) {
        SysFile sysFile = sysFileMapper.selectById(id);
        if (ObjectUtil.isNull(sysFile)) {
            throw new RuntimeException("文件不存在");
        }
        sysFileMapper.deleteById(id);
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(BUCKET_NAME).object(sysFile.getObjectName()).build());
        } catch (Exception e) {
            log.info("文件删除失败：{}", e.getMessage());
            throw new RuntimeException("文件删除失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeBatch(String files) {
        List<String> ids = Arrays.stream(files.split(","))
                                .distinct()
                                .collect(Collectors.toList());
        List<SysFile> sysFiles = sysFileMapper.selectBatchIds(ids);
        if (CollUtil.isEmpty(sysFiles)) {
            throw new RuntimeException("文件不存在");
        }
        sysFileMapper.deleteBatchIds(ids);
        MinioClient minioClient = null;
        try {
            minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            for (SysFile sysFile : sysFiles) {
                try {
                    minioClient.removeObject(RemoveObjectArgs.builder().bucket(BUCKET_NAME).object(sysFile.getObjectName()).build());
                } catch (Exception e) {
                    log.info("文件删除失败：{}", e.getMessage());
                    throw new RuntimeException("文件删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}