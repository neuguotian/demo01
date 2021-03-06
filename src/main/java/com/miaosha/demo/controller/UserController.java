package com.miaosha.demo.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaosha.demo.controller.viewobject.UserVO;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.response.CommonReturnType;
import com.miaosha.demo.service.UserService;
import com.miaosha.demo.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    // 通过一个bean的方式注入进来, 底层用ThreadLocal的方式支持多用户的并发访问, session之间不冲突
    @Autowired
    private HttpServletRequest httpServletRequest;


    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)
                || org.apache.commons.lang3.StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 用户登录服务, 用来校验用户登录是否合法
        final UserModel userModel = userService.validateLogin(telephone, encodeByMd5(password));

        // 将登录凭证加入到用户登录成功的session中
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);
    }


    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    // 用户注册接口
    public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") String gender,
                                     @RequestParam(name = "age") String age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {


        // 验证手机号和otpCode 符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }


        // 用户注册流程
        final UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(Byte.valueOf(gender));
        userModel.setAge(Integer.valueOf(age));
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.encodeByMd5(password));


        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    public String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        final BASE64Encoder base64Encoder = new BASE64Encoder();

        // 加密字符串
        final String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));

        return newstr;
    }


    // 用户获取otp短信接口
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone) {
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
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用service服务获取对应的id的用户对象并返回给前端
        final UserModel userModel = userService.getUserById(id);
        // 把整个领域模型返给前端, 很不专业
        // return userModel;

        // 若获取的对应的用户信息不存在
        if (userModel == null) {
            //userModel.setEncrptPassword("2312");
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
