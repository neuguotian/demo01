package com.miaosha.demo.service;

import com.miaosha.demo.error.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname UserServiceTest
 * @Description TODO
 * @Date 2019/10/22 13:17
 * @Author gt
 */
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @Test
    public void testValidateLogin() throws BusinessException {
        userService.validateLogin("11", "ZRK9Q9nKpuAsmQsKgmUtyg==");
        userService.validateLogin("11", "ZK9Q9nKpuAsmQsKgmUtyg==");
    }
}
