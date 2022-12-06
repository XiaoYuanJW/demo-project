package com.example.demo.validator;

import cn.hutool.core.lang.Validator;
import com.example.demo.validator.constraints.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Phone自定义约束验证器
 * Created by YuanJW on 2022/12/2.
 */
public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return Validator.isMobile(phone);
    }
}
