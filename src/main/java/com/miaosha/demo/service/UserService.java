package com.miaosha.demo.service;

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
}
