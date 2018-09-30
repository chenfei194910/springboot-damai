package com.damai.system.service;

import java.util.List;

import com.damai.system.entity.SysUser;
import com.damai.system.entity.SysUserRole;
//import com.github.pagehelper.PageInfo;

public interface ISysUserService {

//    PageInfo<SysUser> query(Map<String, Object> condition, int pageNo,
//            int pageSize);

    SysUser findById(Long id);

    SysUser save(SysUser entity);

    SysUser update(SysUser entity);

    void delete(Long id);

    
	void insert();
	
	
	List<SysUserRole> selectUserRoles();

//	SysUserRole selectUserRole();
}
