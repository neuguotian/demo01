package com.miaosha.demo.controller;

import com.miaosha.demo.controller.viewobject.UserVO;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.response.CommonReturnType;
import com.miaosha.demo.service.UserService;
import com.miaosha.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2019/10/21 10:42
 * @Author gt
 */
@RestController("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        // 调用service服务获取对应的id的用户对象并返回给前端
        final UserModel userModel = userService.getUserById(id);
        // 把整个领域模型返给前端, 很不专业
        // return userModel;

        // 若获取的对应的用户信息不存在
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }


        // 将核心领域模型对象转化为可供UI使用的viewobject
        final UserVO userVO = convertFromUserModel(userModel);

        // 返回给前端一个通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }


        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
