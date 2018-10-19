package com.springboot.three.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.three.annotation.ReadDataSource;
import com.springboot.three.annotation.WriteDataSource;
import com.springboot.three.config.dbconfig.DataSourceContextHolder;
import com.springboot.three.dao.UserMapper;
import com.springboot.three.domain.User;
import com.springboot.three.util.SpringContextUtil;
/**
 * 如果需要事务，自行在方法上添加@Transactional
 * 如果方法有内部有数据库操作，则必须指定@WriteDataSource还是@ReadDataSource
 * 
 * 注：AOP ，内部方法之间互相调用时，如果是this.xxx()这形式，不会触发AOP拦截，可能会
 * 导致无法决定数据库是走写库还是读库
 * 方法：
 * 为了触发AOP的拦截，调用内部方法时，需要特殊处理下，看方法getService()
 * 
 * @author Jfei
 *
 */
@Service
//@Transactional
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@WriteDataSource
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void insertUser(User u){
		userMapper.insert(u);
	
		//如果类上面没有@Transactional,方法上也没有，哪怕throw new RuntimeException,数据库也会成功插入数据
//		throw new RuntimeException("测试插入事务");
	}
	/**
	 * 写事务里面调用读
	 * @param u
	 */
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void wirteAndRead(User u){
		userMapper.insert(u);//这里走写库，那后面的读也都要走写库
		//这是刚刚插入的
		User uu = userMapper.findById(u.getId());
		System.out.println("==读写混合测试中的读(刚刚插入的)====id="+u.getId()+",  user_name=" + uu.getUserName());
		//为了测试,3个库中id=1的user_name是不一样的
		User uuu = userMapper.findById("1");
		System.out.println("==读写混合测试中的读====id=1,  user_name=" + uuu.getUserName());
		throw new RuntimeException("测试 写事务里面调用读事务");
	}
	
	@Transactional
	public void readAndWirte(User u){
		//为了测试,3个库中id=1的user_name是不一样的
		User uu = userMapper.findById("1");
		System.out.println("==读写混合测试中的读====id=1,user_name=" + uu.getUserName());
		userMapper.insert(u);
		
		log.info("u :" + u.getId()+"--------------"+u.getUserName());
		throw new RuntimeException("测试 写事务里面调用读事务");
	}
	
	@ReadDataSource
	public User findById(String id){
		User u = userMapper.findById(id);
		return u;
	}
	
	
	//@ReadDataSource
	public PageInfo<User> queryPage(String userName,int pageNum,int pageSize){
		Page<User> page = PageHelper.startPage(pageNum, pageSize);
		//PageHelper会自动拦截到下面这查询sql
		userMapper.query(userName);
		return page.toPageInfo();
	}
	
}
