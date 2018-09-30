package com.damai.datasource.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.damai.datasource.database.dynamic.DataSourceChange;
import com.damai.datasource.database.dynamic.DynamicDataSource;


/**
 * 数据库访问切点类 <br/>
 * 根据调用服务端的方法名称来进行拦截，用于切换主库从库
 * 
 */

@Aspect
@Component
public class DatabaseAspect {

    /**
     *   日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseAspect.class);

    /**
     * 从库方法名称匹配规则,使用小写
     */
    static List<String> slaveMethodName = new ArrayList<String>() {
        
        private static final long serialVersionUID = 1L;

        {
            add("query");
            add("find");
            add("select");
            add("get");
        }
    };

    /**
     * service层切点
     */
    @Pointcut("execution(* com.damai..*.service..*.*(..))")
    public void serviceAspect() {
    }

    /**
     * 配置service前置通知,使用在方法aspect()上注册的切入点
     * 
     * @param joinPoint
     *            切点
     */
    @Before("serviceAspect()")
    public void before(JoinPoint joinPoint) {
        try {
            // System.out.println("开始service前置通知");

            // 如果方法加了事务注解，直接使用主库
            if (getAnnotation(joinPoint, Transactional.class) != null) {
                // System.out.println("存在事务注解");
                // 主库
                DynamicDataSource.useMaster();
            } else {
                // 获取强制指定访问的数据库注解
                Object object = getAnnotation(joinPoint, DataSourceChange.class);
                if (object != null) {
                    //
                    DataSourceChange dataSourceChange = (DataSourceChange) object;
                    if (dataSourceChange.slave() == true) {
                        // 从库
                        DynamicDataSource.useSlave();
                    } else {
                        // 主库
                        DynamicDataSource.useMaster();
                    }
                } else {

                    // 根据方法名称来判断使用主库还是从库
                    boolean flg = checkSwitchSlave(joinPoint.getSignature().getName());
                    if (flg) {
                        // 从库
                        DynamicDataSource.useSlave();
                    } else {
                        // 主库
                        DynamicDataSource.useMaster();
                    }

                }
            }
        } catch (Throwable e) {
            logger.error("before " + joinPoint + "\t with exception : " + e.getMessage());
        }
    }

    /**
     * 检查是否切换到从库 <br/>
     * query、find、select、get 以上使用从库; <br/>
     * insert、update、delete、save 使用主库； <br/>
     * 以上规则之外的方法使用主库
     * 
     * @param name
     * @return
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

    /**
     * 获取当前切点存在的注解
     * 
     * @param joinPoint
     *            你切点
     * @param annotationClass
     *            注解类
     * @return
     * @throws Exception
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

}
