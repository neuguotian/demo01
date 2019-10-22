package com.miaosha.demo.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ValidationResult
 * @Description TODO
 * @Date 2019/10/22 14:21
 * @Author gt
 */
@Data
public class ValidationResult {
    // 校验结果是否有错
    private boolean hasErrors = false;

    // 存放错误信息的Map
    private Map<String, String> errorMsgMap = new HashMap<>();


    // 实现通用的通过格式化信字符串信息获取结果的msg方法
    public String getErrMsg() {
        return StringUtils.join(errorMsgMap.values().toArray()
                , ",");
    }
}
