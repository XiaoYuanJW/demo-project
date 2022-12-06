package com.example.demo.validator;

import com.example.demo.validator.constraints.Flag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Flag自定义约束验证器
 * Created by YuanJW on 2022/12/6.
 */
public class FlagconstraintValidator implements ConstraintValidator<Flag, Integer> {
    private String[] values;

    @Override
    public void initialize(Flag constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer flag, ConstraintValidatorContext constraintValidatorContext) {
        List<String> list = Arrays.asList(values);
        return list.contains(String.valueOf(flag));
    }
}
