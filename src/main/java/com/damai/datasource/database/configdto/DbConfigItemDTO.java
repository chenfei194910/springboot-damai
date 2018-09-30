package com.damai.datasource.database.configdto;

import java.io.Serializable;

/**
 * 数据库连接配置属性类
 * 该类中的每个属性和application.yml中的属性名称一致
 *  
 * @author felix.chen
 * @date 2017年6月26日 上午10:52:45 
 *
 */
public class DbConfigItemDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 连接数据库的jdbcUrl，不同数据库不一样
	 */
    private String jdbcUrl;
    
    /**
     * 
     * 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
     */
    private String driverClassName;
    
    /**
     * 
     * 连接数据库的用户名
     */
    private String username;
    
    /**
     * 
     * 连接数据库的密码
     */
    private String password;
    
    /**
     * 
     * 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有： 
     * 监控统计用的filter:stat 
     * 日志用的filter:log4j 
     * 防御sql注入的filter:wall
     */
    private String filters;
    
    /**
     * 
     * 最大连接池数量
     */
    private Integer maxActive;
    
    /**
     * 
     * 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
     */
    private Integer initialSize;
    
    /**
     * 
     * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，
     * 缺省启用公平锁，并发效率会有所下降， 
     * 如果需要可以通过配置useUnfairLock属性为true使用非公平锁。 
     */
    private Integer maxWait;
    
    /**
     * 
     * 最小连接池数量
     */
    private Integer minIdle;
    
    /**
     * 
     * 有两个含义：
     * 1) Destroy线程会检测连接的间隔时间 
     * 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     */
    private Integer timeBetweenEvictionRunsMillis;
    
    /**
     * 
     * 连接在池中最小生存的时间，单位是毫秒
     */
    private Integer minEvictableIdleTimeMillis;
    
    /**
     * 
     * 检测连接是否有效的超时时间,单位：秒。
     * 底层调用jdbc Statement对象的void setQueryTimeout(int seconds)方法
     */
    private Integer validationQueryTimeout;
    
    /**
     * 
     * 用来检测连接是否有效的sql，要求是一个查询语句
     */
    private String validationQuery;
    
    /**
     * 
     * 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，
     * 执行validationQuery检测连接是否有效。
     */
    private Boolean testWhileIdle;
    
    /**
     * 
     * 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
     */
    private Boolean testOnBorrow;
    
    /**
     * 
     * 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     */
    private Boolean testOnReturn;
    
    /**
     * 
     * 是否缓存preparedStatement，也就是PSCache。
     * PSCache对支持游标的数据库性能提升巨大，比如说oracle。 
     * 在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。
     *  
     */
    private Boolean poolPreparedStatements;
    
    /**
     * 
     * 	要启用PSCache，必须配置大于0，当大于0时,poolPreparedStatements自动触发修改为true。
     * 
     */
    private Integer maxOpenPreparedStatements;// 默认值20
    
    private Boolean verifyServerCertificate;
    
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	public Integer getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}
	public Integer getInitialSize() {
		return initialSize;
	}
	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}
	public Integer getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	public Integer getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}
	public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}
	public Integer getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}
	public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}
	public Integer getValidationQueryTimeout() {
		return validationQueryTimeout;
	}
	public void setValidationQueryTimeout(Integer validationQueryTimeout) {
		this.validationQueryTimeout = validationQueryTimeout;
	}
	public String getValidationQuery() {
		return validationQuery;
	}
	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}
	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public Boolean getTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	public Boolean getPoolPreparedStatements() {
		return poolPreparedStatements;
	}
	public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}
	public Integer getMaxOpenPreparedStatements() {
		return maxOpenPreparedStatements;
	}
	public void setMaxOpenPreparedStatements(Integer maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}
	public Boolean getVerifyServerCertificate() {
		return verifyServerCertificate;
	}
	public void setVerifyServerCertificate(Boolean verifyServerCertificate) {
		this.verifyServerCertificate = verifyServerCertificate;
	}
    
    
}
