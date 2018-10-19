package com.springboot.three.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.springboot.three.domain.User;

@Mapper
public interface UserMapper {

	@Insert("insert sys_user(id,username) values(#{id},#{userName})")
	void insert(User u);
	
	@Select("select id,username from sys_user where id=#{id} ")
	User findById(@Param("id")String id);
	
	//注：方法名和要UserMapper.xml中的id一致
	List<User> query(@Param("userName")String userName);
	
}
