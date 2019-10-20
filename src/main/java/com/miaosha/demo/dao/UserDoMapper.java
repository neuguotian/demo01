package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.UserDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKey(UserDo record);
}