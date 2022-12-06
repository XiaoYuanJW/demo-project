package com.example.demo.validator.constraints;

import com.example.demo.validator.FlagconstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验状态注解
 * Created by YuanJW on 2022/12/6.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = FlagconstraintValidator.class)
public @interface Flag {
    String[] value() default {};

    String message() default "状态校验异常";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
