package com.miaosha.demo.service.impl;

import com.miaosha.demo.dao.UserDoMapper;
import com.miaosha.demo.dao.UserPasswordDoMapper;
import com.miaosha.demo.dataobject.UserDo;
import com.miaosha.demo.dataobject.UserPasswordDo;
import com.miaosha.demo.service.UserService;
import com.miaosha.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2019/10/21 10:53
 * @Author gt
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Override
    public UserModel getUserById(Integer id) {
        // ORM直接映射不能直接返回. 调用userDoMapper 获取对应用户的dataObject
        final UserDo userDo = userDoMapper.selectByPrimaryKey(id);
        if (userDo == null) {
            return null;
        }

        // 通过userId 获取用户加密密码信息
        final UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());

        return convertFromDataObject(userDo, userPasswordDo);

    }

    private UserModel convertFromDataObject(UserDo userDo, UserPasswordDo userPasswordDo) {
        if (userDo == null) {
            return null;
        }
        final UserModel userModel = new UserModel();
        // ------?
        BeanUtils.copyProperties(userDo, userModel);

        if (userPasswordDo != null) {
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }

        return userModel;
    }



}
