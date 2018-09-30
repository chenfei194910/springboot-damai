package com.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.damai.system.entity.SysUser;

@RestController
public class SysUserController {
	
	@GetMapping("/getPage")
	public String getPage() {
		return "hello";
	}
	
	@GetMapping("/getString")
	public String getString() {
		return "Hello Felix";
	}
	
	@GetMapping("/queryUserById")
	public SysUser querySysUserById() {
		SysUser user = new SysUser();
		user.setId(1L);
		user.setUserName("Felix");
		user.setRelName("小明");
		return user;
	}
	
	@GetMapping("/queryUserList")
	public List<SysUser> queryUserList(){
		List<SysUser> list = new ArrayList<>();

		SysUser user1 = new SysUser();
		user1.setId(1L);
		user1.setUserName("Felix");
		user1.setRelName("小明");

		SysUser user2 = new SysUser();
		user2.setId(2L);
		user2.setUserName("haha");
		user2.setRelName("小张");

		list.add(user1);
		list.add(user2);
		return list;
	}
}
