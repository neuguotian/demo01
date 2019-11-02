package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.SequenceDo;
import com.miaosha.demo.validator.ValidationResult;

public interface SequenceDoMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDo record);

    int insertSelective(SequenceDo record);

    SequenceDo selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(SequenceDo record);

    int updateByPrimaryKey(SequenceDo record);

    SequenceDo getSequenceByName(String name);

}