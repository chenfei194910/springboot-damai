package com.springboot.three.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.three.config.dbconfig.DataSourceContextHolder;
import com.springboot.three.config.dbconfig.DataSourceType;


/**
 * 在service层觉得数据源
 * 
 * 必须在事务AOP之前执行，所以实现Ordered,order的值越小，越先执行
 * 如果一旦开始切换到写库，则之后的读都会走写库
 * 
 *
 */
@Aspect
@EnableAspectJAutoProxy(exposeProxy=true,proxyTargetClass=true)
@Component
public class DataSourceAopInService implements PriorityOrdered{

private static Logger log = LoggerFactory.getLogger(DataSourceAopInService.class);
	
/*	@Before("execution(* com.fei.springboot.service..*.find*(..)) "
			+ " or execution(* com.fei.springboot.service..*.get*(..)) "
			+ " or execution(* com.fei.springboot.service..*.query*(..))")
    public void setReadDataSourceType() {
		//如果已经开启写事务了，那之后的所有读都从写库读
		if(!DataSourceType.write.getType().equals(DataSourceContextHolder.getReadOrWrite())){
			DataSourceContextHolder.setRead();
		}
        
    }

    @Before("execution(* com.fei.springboot.service..*.insert*(..)) "
    		+ " or execution(* com.fei.springboot.service..*.update*(..))"
    		+ " or execution(* com.fei.springboot.service..*.add*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.setWrite();
    }*/
    

	@Before("execution(* com.springboot.three.service..*.*(..)) "
			+ " or @annotation(com.springboot.three.annotation.ReadDataSource) "
			+ " or @annotation(com.springboot.three.annotation.WriteDataSource) ")
	public void setReadDataSourceType(JoinPoint joinPoint) {
        try {
        	//如果已经开启写事务了，那之后的所有读都从写库读
    	if (getAnnotation(joinPoint, Transactional.class) != null) {
    		DataSourceContextHolder.setWrite();
    	}else if(DataSourceType.read.getType().equals(DataSourceContextHolder.getReadOrWrite())){ 
    		DataSourceContextHolder.setRead();
        }else if(DataSourceType.write.getType().equals(DataSourceContextHolder.getReadOrWrite())){
    		DataSourceContextHolder.setWrite();
    	}else {
            // 根据方法名称来判断使用主库还是从库
            boolean flg = checkSwitchSlave(joinPoint.getSignature().getName());
            if (flg) {
                // 从库
            	DataSourceContextHolder.setRead();
            } else {
                // 主库
            	DataSourceContextHolder.setWrite();
            }
    	}
    } catch (Throwable e) {
    	log.error("before " + joinPoint + "\t with exception : " + e.getMessage());
    }
	    
	}
	
//	@Before("execution(* com.springboot.three.service..*.*(..)) "
//			+ " or @annotation(com.springboot.three.annotation.WriteDataSource) ")
//	public void setWriteDataSourceType(JoinPoint joinPoint) {
//        try {
//                // 获取强制指定访问的数据库注解
//        	if (getAnnotation(joinPoint, Transactional.class) != null) {
//        		DataSourceContextHolder.setWrite();
//        	}else if(!DataSourceType.write.getType().equals(DataSourceContextHolder.getReadOrWrite())){
//        		DataSourceContextHolder.setRead();
//        	}else {
//                // 根据方法名称来判断使用主库还是从库
//                boolean flg = checkSwitchSlave(joinPoint.getSignature().getName());
//                if (flg) {
//                    // 从库
//                	DataSourceContextHolder.setRead();
//                } else {
//                    // 主库
//                	DataSourceContextHolder.setWrite();
//                }
//        	}
//        } catch (Throwable e) {
//        	log.error("before " + joinPoint + "\t with exception : " + e.getMessage());
//        }
//	}
    
	@Override
	public int getOrder() {
		/**
		 * 值越小，越优先执行
		 * 要优于事务的执行
		 * 在启动类中加上了@EnableTransactionManagement(order = 10) 
		 */
		return 1;
	}
	
    /**
     * 从库方法名称匹配规则,使用小写
     */
    static List<String> slaveMethodName = new ArrayList<String>() {
		private static final long serialVersionUID = -2390412163778027958L;

		{
            add("query");
            add("find");
            add("select");
            add("get");
        }
    };
    
    /**
     * 获取当前切点存在的注解
     * 
     * @param joinPoint
     *            你切点
     * @param annotationClass
     *            注解类
     * @return
     * @throws Exception
     * @author Ansel.Song 
     * @date 2017年6月26日 下午5:01:55 
     */
    private Object getAnnotation(JoinPoint joinPoint, Class<? extends Annotation> annotationClass) throws Exception {
        Object rtn = null;
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 获取当前方法对象
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                methodSignature.getMethod().getParameterTypes());
        // 判断是否存在注解
        if (realMethod.isAnnotationPresent(annotationClass)) {
            rtn = realMethod.getAnnotation(annotationClass);
        }
        return rtn;
    }
    
    /**
     * 检查是否切换到从库 <br/>
     * query、find、select、get 以上使用从库; <br/>
     * insert、update、delete、save 使用主库； <br/>
     * 以上规则之外的方法使用主库
     * 
     * @param name
     * @return
     * @author Ansel.Song 
     * @date 2017年6月21日 下午6:14:01 
     */
    private boolean checkSwitchSlave(String name) {
        boolean rtn = false;
        // 转成小写，以便忽略大小写
        name = name.toLowerCase();
        // 循环
        for (String str : slaveMethodName) {
            // 前匹配
            if (name.startsWith(str)) {
                rtn = true;
                break;
            }
        }
        return rtn;
    }

}
