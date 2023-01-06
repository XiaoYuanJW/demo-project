package com.example.demo.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.demo.dto.QueueEnum;
import com.example.demo.dto.SignStatisticsDto;
import com.example.demo.entity.UmsMemberSign;
import com.example.demo.entity.UmsMemberSignLog;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UmsMemberSignLogMapper;
import com.example.demo.mapper.UmsMemberSignMapper;
import com.example.demo.service.UmsMemberSignService;
import com.example.demo.util.MemberHolder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 会员签到操作接口实现类
 * Created by YuanJW on 2023/1/5.
 */
@Service
public class UmsMemberSignServiceImpl implements UmsMemberSignService {
    @Resource
    private UmsMemberSignMapper umsMemberSignMapper;
    @Resource
    private UmsMemberSignLogMapper umsMemberSignLogMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.key.sign}")
    private String signKey;
    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public List<UmsMemberSign> page(UmsMemberSign umsMemberSign) {
        return umsMemberSignMapper.getUmsMemberSigns(umsMemberSign);
    }

    @Override
    public UmsMemberSign detail(Long id) {
        return umsMemberSignMapper.selectById(id);
    }

    @Override
    public boolean sign(Long signId) {
        // 校验签到活动是否存在
        UmsMemberSign umsMemberSign = umsMemberSignMapper.selectById(signId);
        if (ObjectUtil.isNull(umsMemberSign)){
            throw new ServiceException("该签到活动不存在");
        }
        Long memberId = MemberHolder.get().getId();
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy/MM"));
        int day = now.getDayOfMonth() - 1;
        String key = signKey + ":" + signId + ":" + memberId + ":" + time;
        Boolean sign = redisTemplate.opsForValue()
                .getBit(key, day);
        // 是否已经签过到校验
        if (BooleanUtil.isTrue(sign)) {
            throw new ServiceException("您已经签过到了，请不要重复签到");
        }
        // 存储签到信息至redis的bitmap结构中
        redisTemplate.opsForValue()
                .setBit(key, day, true);
        // 发送rabbitMQ消息：新增签到记录到MySQL
        UmsMemberSignLog umsMemberSignLog = UmsMemberSignLog.builder()
                .memberId(memberId)
                .signId(signId)
                .backupStatus(1).signTime(new Date()).build();
        amqpTemplate.convertAndSend(
                QueueEnum.QUEUE_SIGN_LOG.getExchange(),
                QueueEnum.QUEUE_SIGN_LOG.getRouteKey(),
                umsMemberSignLog
        );
        return true;
    }

    @Override
    public SignStatisticsDto statistics(Long signId) {
        // 校验签到活动是否存在
        UmsMemberSign umsMemberSign = umsMemberSignMapper.selectById(signId);
        if (ObjectUtil.isNull(umsMemberSign)){
            throw new ServiceException("该签到活动不存在");
        }
        Long memberId = MemberHolder.get().getId();
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy/MM"));
        int day = now.getDayOfMonth() - 1;
        String key = signKey + ":" + signId + ":" + memberId + ":" + time;
        // 获取redis的bitmap结构中的签到信息
        List<Long> result = redisTemplate.opsForValue()
                .bitField(
                        key,
                        BitFieldSubCommands.create()
                                .get(BitFieldSubCommands.BitFieldType.unsigned(day))
                                .valueAt(0)
                );
        // 校验签到结果是否为空
        if (result == null || result.isEmpty()) {
            return new SignStatisticsDto()
                    .setCount(0)
                    .setResult(null);
        }
        Long num = result.get(0);
        if (num == null && num == 0) {
            return new SignStatisticsDto()
                    .setCount(0)
                    .setResult(null);
        }
        int count = 0;
        // 循环遍历
        while (true) {
            // 数字与1做与运算,获取最后一个bit位
            if ((num & 1) == 0) {
                break;
            } else {
                count++;
            }
            // 数字右移进入下一位
            num >>>= 1;
        }
        return new SignStatisticsDto()
                .setCount(count)
                .setResult(NumberUtil.getBinaryStr(result.get(0)));
    }

    @RabbitListener(queues = "demo.sign.log")
    public void handleSignLog(UmsMemberSignLog umsMemberSignLog) {
        umsMemberSignLogMapper.insert(umsMemberSignLog);
    }
}
