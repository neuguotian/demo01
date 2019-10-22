package com.miaosha.demo.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * @Classname ValidatorImpl
 * @Description TODO
 * @Date 2019/10/22 14:25
 * @Author gt
 */
@Component
public class ValidatorImpl implements InitializingBean {

    // 包装javax.validation.Validator, java工具
    private Validator validator;

    // 实现校验方法并返回校验结果
    public ValidationResult validate(Object bean) {
        final ValidationResult result = new ValidationResult();
        final Set<ConstraintViolation<Object>> constraintViolationSet = this.validator.validate(bean);
        if (constraintViolationSet.size() > 0) {
            // 有错误
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation ->
            {
                final String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();

                result.getErrorMsgMap().put(propertyName, errMsg);
            });
        }
        return result;
    }

    // ValidatorImpl bean初始化完成之后会回调此函数
    @Override
    public void afterPropertiesSet() throws Exception {
        // 将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
