package com.miaosha.demo.service.impl;

import com.miaosha.demo.dao.UserDoMapper;
import com.miaosha.demo.dao.UserPasswordDoMapper;
import com.miaosha.demo.dataobject.UserDo;
import com.miaosha.demo.dataobject.UserPasswordDo;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.service.UserService;
import com.miaosha.demo.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 引入了apache common lang的依赖
        if (StringUtils.isEmpty(userModel.getName()) || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelephone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 持久化用户信息
        final UserDo userDo = convertFromUserModel(userModel);
        // userDoMapper.insert 会用null覆盖, userDoMapper.insertSelective遇见null会跳过该字段,使用默认值
        userDoMapper.insertSelective(userDo);

        userModel.setId(userDo.getId());

        final UserPasswordDo userPasswordDo = convertPasswordFromUserModel(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDo);
    }

    // 实现UserModel --> UserDo
    private UserDo convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        final UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel, userDo);

        return userDo;
    }

    private UserPasswordDo convertPasswordFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        final UserPasswordDo userPasswordDo = new UserPasswordDo();

        userPasswordDo.setUserId(userModel.getId());
        userPasswordDo.setEncrptPassword(userModel.getEncrptPassword());

        return userPasswordDo;
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
