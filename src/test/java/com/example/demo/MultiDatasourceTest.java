package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.Application;
import com.damai.system.dao.ISysUserDao;
import com.damai.system.entity.SysUser;
import com.damai.system.service.ISysUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MultiDatasourceTest {

	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 查询用户
	 * @throws Exception
	 */
	@Test
	public void testQueryUser() throws Exception {
		SysUser masterUser = sysUserService.findById(1L);
		System.out.println("masterUser==>"+masterUser.getId());
	}

}
