package com.miaosha.demo.controller;

import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname BaseController
 * @Description 公共的功能设计到基类中
 *
 * @Date 2019/10/21 19:56
 * @Author gt
 */
public class  BaseController {
    public final static String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";


    // 定义exceptionhandler解决为被controller层吸收的异常. （符合spring钩子类定义的设计思想）
    // 业务逻辑的错误, 并不是服务器端不能处理
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request, Exception ex) {
        /*
        version-1, 返回很多无用的信息,不优雅
        final CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");
        commonReturnType.setData(ex);
        return commonReturnType;

         */

       /*
       version-2

       BusinessException businessException = (BusinessException)ex;

        final CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");

        final Map<String, Object> responseData = new HashMap<>();
        responseData.put("errCode", businessException.getErrCode());
        responseData.put("errMsg", businessException.getErrMsg());

        commonReturnType.setData(responseData);

        return commonReturnType;*/

        // version-3 : 重构一下代码,美化
        final Map<String, Object> responseData = new HashMap<>();

        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException)ex;

            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UNKNOW_ERROT.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOW_ERROT.getErrMsg());
        }

        return CommonReturnType.create(responseData, "fail");
    }

}
