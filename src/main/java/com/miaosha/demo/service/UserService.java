package com.miaosha.demo.service;

import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.service.model.UserModel;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2019/10/21 10:52
 * @Author gt
 */
public interface UserService {
    // get user by id
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /*

    telephone是用手机号,  encrptpassword加密后的密码
   */
    UserModel validateLogin(String telephone, String encrptpassword) throws BusinessException;
}
