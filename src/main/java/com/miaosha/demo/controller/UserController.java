package com.miaosha.demo.controller;

import com.miaosha.demo.controller.viewobject.UserVO;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.response.CommonReturnType;
import com.miaosha.demo.service.UserService;
import com.miaosha.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2019/10/21 10:42
 * @Author gt
 */
@RestController("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    // 通过一个bean的方式注入进来, 底层用ThreadLocal的方式支持多用户的并发访问, session之间不冲突
    @Autowired
    private HttpServletRequest httpServletRequest;

    // 用户获取otp短信接口
    @RequestMapping("/getotp")
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone) {
        // 按照一定的规则生成otp验证码
        final Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 将OTP验证码将手机号关联 (redis天然做K-V关联) 使用HttpSession方式绑定
        final HttpSession session = httpServletRequest.getSession();
        session.setAttribute(telephone, otpCode);


        // 将OTP验证码通过短信通道发送给用户 --- 省略, 正常是打日志(otpCode属于敏感信息)
        System.out.println("telephone = " + telephone + " & optCode = " + otpCode);

        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        // 调用service服务获取对应的id的用户对象并返回给前端
        final UserModel userModel = userService.getUserById(id);
        // 把整个领域模型返给前端, 很不专业
        // return userModel;

        // 若获取的对应的用户信息不存在
        if (userModel == null) {
            userModel.setEncrptPassword("2312");
            //throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
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
