package com.damai.system.service;

import com.damai.system.entity.SysUserRole;

public interface ISysUserRoleService {

//    PageInfo<SysUserRole> query(Map<String, Object> condition, int pageNo,
//            int pageSize);

    SysUserRole findById(String id);

    SysUserRole save(SysUserRole entity);

    SysUserRole update(SysUserRole entity);

    void delete(Long id);

	void insert();
	
}
