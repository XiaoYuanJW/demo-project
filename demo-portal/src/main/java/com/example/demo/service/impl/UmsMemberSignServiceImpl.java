package com.example.demo.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.example.demo.dto.QueueEnum;
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
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_SIGN_LOG.getExchange(),
                QueueEnum.QUEUE_SIGN_LOG.getRouteKey(), umsMemberSignLog);
        return true;
    }

    @RabbitListener(queues = "demo.sign.log")
    public void handle(UmsMemberSignLog umsMemberSignLog) {
        umsMemberSignLogMapper.insert(umsMemberSignLog);
    }
}
