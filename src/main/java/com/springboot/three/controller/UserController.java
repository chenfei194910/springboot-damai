package com.springboot.three.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.springboot.three.config.dbconfig.MybatisConfiguration;
import com.springboot.three.domain.User;
import com.springboot.three.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return "hello";
	}

	/**
	 * 测试插入
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public String add() {
		User u = new User();
		int i = (int) (Math.random() * 900 + 10000);
		String myStr = Integer.toString(i);
		u.setId(myStr);
		log.info("add --- id:" + myStr);
		u.setUserName(myStr + 1);
		userService.insertUser(u);

		log.info(u.getId() + "    " + u.getUserName());

		return u.getId() + "    " + u.getUserName();
	}

	/**
	 * 测试读
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/get/{id}")
	@ResponseBody
	public String findById(@PathVariable("id") String id) {
		User u = userService.findById(id);
		return u.getId() + "    " + u.getUserName();
	}

	/**
	 * 测试写然后读
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping("/addAndRead")
	@ResponseBody
	public String addAndRead(String id, String userName) {
		User u = new User();
		int i = (int) (Math.random() * 900 + 10000);
		String myStr = Integer.toString(i);
		u.setId(myStr);
		u.setUserName(myStr + 1);
		userService.wirteAndRead(u);
		return u.getId() + "    " + u.getUserName();
	}

	/**
	 * 测试读然后写
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping("/readAndAdd")
	@ResponseBody
	public String readAndWrite(String id, String userName) {
		User u = new User();
		int i = (int) (Math.random() * 900 + 10000);
		String myStr = Integer.toString(i);
		u.setId(myStr);
		u.setUserName(myStr + 1);
		userService.readAndWirte(u);
		return u.getId() + "    " + u.getUserName();
	}

	/**
	 * 测试分页插件
	 * 
	 * @return
	 */
	@RequestMapping("/queryPage")
	@ResponseBody
	public String queryPage() {
		PageInfo<User> page = userService.queryPage("a", 1, 2);
		StringBuilder sb = new StringBuilder();
		sb.append("<br/>总页数=" + page.getPages());
		sb.append("<br/>总记录数=" + page.getTotal());
		for (User u : page.getList()) {
			sb.append("<br/>" + u.getId() + "      " + u.getUserName());
		}
		System.out.println("分页查询....\n" + sb.toString());
		return sb.toString();
	}

	/**
	 * 测试controller一个方法里面多个查询是否查询的是同一个库
	 * 
	 * @return
	 */
	@RequestMapping("/queryUser")
	@ResponseBody
	public String queryUser() {

		User user = userService.findById("1");
		log.info("findById --- id:" + user.getId());

		PageInfo<User> page = userService.queryPage("a", 1, 2);
		StringBuilder sb = new StringBuilder();
		sb.append("<br/>总页数=" + page.getPages());
		sb.append("<br/>总记录数=" + page.getTotal());
		for (User u : page.getList()) {
			sb.append("<br/>" + u.getId() + "      " + u.getUserName());
		}
		System.out.println("分页查询....\n" + sb.toString());
		return sb.toString();
	}

	/**
	 * 测试Service一个方法里面多个查询是否查询的是同一个库
	 * 
	 * @return
	 */
	@RequestMapping("/queryOneAndPage")
	@ResponseBody
	public String queryOneAndPage() {
		PageInfo<User> page = userService.queryOneAndPage("a", 1, 2);
		StringBuilder sb = new StringBuilder();
		sb.append("<br/>总页数=" + page.getPages());
		sb.append("<br/>总记录数=" + page.getTotal());
		for (User u : page.getList()) {
			sb.append("<br/>" + u.getId() + "      " + u.getUserName());
		}
		System.out.println("分页查询....\n" + sb.toString());
		return sb.toString();
	}
}
