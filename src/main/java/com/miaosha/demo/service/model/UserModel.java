package com.miaosha.demo.service.model;

import lombok.Data;

/**
 * @Classname UserModel
 * @Description TODO
 * @Date 2019/10/21 11:02
 * @Author gt
 */

@Data
public class UserModel {
    private Integer id;

    private String name;

    private Byte gender;

    private Integer age;

    private String telephone;

    private String registerMode;

    private String thirdPartyId;


    private String encrptPassword;


}
