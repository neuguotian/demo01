package com.miaosha.demo.response;

import lombok.Data;

/**
 * @Classname CommonReturnType
 * @Description TODO
 * @Date 2019/10/21 12:26
 * @Author gt
 */
@Data
public class CommonReturnType {
    // 表明对应请求的处理结果 "success" 或 "fail"
    private String status;

    // 若status=success, 则data内返回前端需要的json数据格式
    // 若status=fail, 则data内使用通用的错误码格式
    private Object data;


    // 定义一个通用的创建方法, 默认status = "success"
    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    private static CommonReturnType create(Object result, String status) {
        final CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setData(result);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }
}
