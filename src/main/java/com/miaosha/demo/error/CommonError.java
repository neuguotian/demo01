package com.miaosha.demo.error;

/**
 * @Classname CommonError
 * @Description TODO
 * @Date 2019/10/21 12:45
 * @Author gt
 */
public interface CommonError {
    int getErrCode();
    String getErrMsg();
    CommonError setErrMsg(String errMsg);

}
