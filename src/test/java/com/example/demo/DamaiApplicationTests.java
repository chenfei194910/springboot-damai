package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.Application;
import com.damai.system.dao.ISysUserDao;
import com.damai.system.entity.SysUser;


@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DamaiApplicationTests {

	@Autowired
	private ISysUserDao iSysUserDao;

	/**
	 * 新增用户
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddUser() throws Exception {
		SysUser user = new SysUser();
		user.setId(1L);
		user.setUserName("xiaowang");
		user.setRelName("小王");
		iSysUserDao.insert(user);
	}

	/**
	 * 修改用户信息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdUser() throws Exception {
		SysUser user = new SysUser();
		user.setId(1L);
		user.setUserName("xiaowang");
		user.setRelName("小王");
		iSysUserDao.insert(user);
	}
	

	/**
	 * 查询用户
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueryUser() throws Exception {
		SysUser user = iSysUserDao.selectByPrimaryKey("1");
		System.out.println(user.getUserName());
	}

	
	/**
	 * 删除用户
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDelUser() throws Exception {
		iSysUserDao.deleteByPrimaryKey("1");
	}
	
}
