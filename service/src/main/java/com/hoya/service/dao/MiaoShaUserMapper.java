package com.hoya.service.dao;

import com.hoya.service.model.MiaoShaUser;

public interface MiaoShaUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MiaoShaUser record);

    int insertSelective(MiaoShaUser record);

    MiaoShaUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaUser record);

    int updateByPrimaryKey(MiaoShaUser record);

    MiaoShaUser getByMobile(String mobile);

}