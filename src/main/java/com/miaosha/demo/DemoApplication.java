package com.miaosha.demo;

import com.miaosha.demo.dao.UserDoMapper;
import com.miaosha.demo.dataobject.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.miaosha"})
@RestController
@MapperScan("com.miaosha.demo.dao")
public class DemoApplication {
    @Autowired
    private UserDoMapper userDoMapper;

    @RequestMapping("/")
    public String hello() {
        UserDo userDo = userDoMapper.selectByPrimaryKey(2);
        if (userDo == null) {
            return "userDo == null";
        }

        return userDo.getName();

    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
