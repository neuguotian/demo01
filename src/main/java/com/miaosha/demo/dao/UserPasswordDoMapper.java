package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.UserPasswordDo;

public interface UserPasswordDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPasswordDo record);

    int insertSelective(UserPasswordDo record);

    UserPasswordDo selectByPrimaryKey(Integer id);

    UserPasswordDo selectByUserId (Integer userId);

    int updateByPrimaryKeySelective(UserPasswordDo record);

    int updateByPrimaryKey(UserPasswordDo record);


}