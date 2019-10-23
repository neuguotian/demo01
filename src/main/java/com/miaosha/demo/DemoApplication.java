package com.miaosha.demo;

import com.miaosha.demo.dao.UserDoMapper;
import com.miaosha.demo.dataobject.UserDo;
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
    /**
     CREATE TABLE user_info (
     id int(11) NOT NULL AUTO_INCREMENT,
     name varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
     gender tinyint(255) NOT NULL,
     age int(11) NOT NULL,
     telephone varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
     register_mode varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
     third_party_id varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
     PRIMARY KEY (id)
     ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
     CREATE TABLE user_password (
     id int(11) NOT NULL,
     encrpt_password varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
     user_id int(11) NOT NULL,
     PRIMARY KEY (id)
     ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
     */
//    @Autowired
//    private UserDoMapper userDoMapper;
//
//    @RequestMapping("/")
//    public String hello() {
//        UserDo userDo = userDoMapper.selectByPrimaryKey(2);
//        if (userDo == null) {
//            return "userDo == null";
//        }
//
//        return userDo.getName();
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
