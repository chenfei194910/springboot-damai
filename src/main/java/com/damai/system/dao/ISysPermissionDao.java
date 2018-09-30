package com.damai.system.dao;

import com.damai.system.entity.SysPermission;

//@MapperScan
public interface ISysPermissionDao {
    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);
}