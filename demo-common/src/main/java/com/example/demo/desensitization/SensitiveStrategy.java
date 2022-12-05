package com.example.demo.desensitization;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.function.Function;

/**
 * 敏感字段枚举
 * Created by YuanJW on 2022/12/5.
 */
public enum SensitiveStrategy {
    /**
     * 中文名
     */
    CHINESE_NAME(s -> DesensitizedUtil.chineseName(s)),
    /**
     * 身份证号
     */
    ID_CARD(s -> DesensitizedUtil.idCardNum(s, 4, 3)),
    /**
     * 座机号
     */
    FIXED_PHONE(s -> DesensitizedUtil.fixedPhone(s)),
    /**
     * 手机号
     */
    MOBILE_PHONE(s -> DesensitizedUtil.mobilePhone(s)),
    /**
     * 地址
     */
    ADDRESS(s -> DesensitizedUtil.address(s, 6)),
    /**
     * 电子邮件
     */
    EMAIL(s -> DesensitizedUtil.email(s)),
    /**
     * 银行卡
     */
    BANK_CARD(s -> DesensitizedUtil.bankCard(s));

    private final Function<String, String> desensitizer;

    SensitiveStrategy(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
