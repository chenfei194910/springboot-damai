package com.damai.datasource.database.dynamic;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.damai.datasource.database.configdto.DbConfigItemDTO;

/**
 * 数据库配置映射
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring")
public class DataSourceConfiguration {
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

	// 默认数据源类型
	private static final String DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";

	/**
	 * 默认主库名称
	 */
	private static final String DEFAULT = "master";
	
	/**
	 * 其他业务数据库名称（这个库可能是其他的业务库）
	 */
	private static final String OTHER = "other";

	/**
	 * 数据源类型
	 */
	private Class<? extends DataSource> type;

	/**
	 * 数据源配置信息
	 */
	private Map<String, DbConfigItemDTO> datasource;

	public Class<? extends DataSource> getType() {
		return type;
	}

	public void setType(Class<? extends DataSource> type) {
		this.type = type;
	}

	public Map<String, DbConfigItemDTO> getDatasource() {
		return datasource;
	}

	public void setDatasource(Map<String, DbConfigItemDTO> datasource) {
		this.datasource = datasource;
	}

	/**
	 * 生成主数据源
	 * 
	 * @return 主数据源
	 *
	 */
	@SuppressWarnings("unchecked")
	@Bean(name = "masterDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSource masterDataSource() {
		logger.info("-------------------- masterDataSource init ---------------------");
		if (type == null) {
			try {
				type = (Class<? extends DataSource>) Class.forName(DATASOURCE_TYPE_DEFAULT);
				return DataSourceBuilder.create().type(type).build();
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage());
				return null;
			}
		}

		return DataSourceBuilder.create().type(type).build();
	}

	/**
	 * 生成从数据源
	 * 
	 * @return 从数据源
	 *
	 */
	@SuppressWarnings("unchecked")
	@Bean(name = "slaveDataSources")
	public Map<String, DataSource> slaveDataSources() {
		logger.info("-------------------- slaveDataSources init ---------------------");
		Map<String, DataSource> dataSorucesMap = new HashMap<>();
		if (datasource == null || datasource.size() <= 0) {
			logger.warn("-------------------- 未配置从库数据源 ---------------------");
			return dataSorucesMap;
		}
		try {
			type = (Class<? extends DataSource>) Class.forName(DATASOURCE_TYPE_DEFAULT);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			return null;
		}
		for (String key : datasource.keySet()) {
			// 排除主数据源和其他业务数据库配置
			if (key.equals(DEFAULT) || key.equals(OTHER)) {
				continue;
			}
			DbConfigItemDTO temp = datasource.get(key);
			DataSource dataSource = buildDataSource(temp);
			dataSorucesMap.put(key, dataSource);
		}
		return dataSorucesMap;
	}

	/*** 
	 * 生成数据源
	 * 
	 * @param dbConfigItemDTO
	 * @return 数据源
	 *
	 */
	private DataSource buildDataSource(DbConfigItemDTO dbConfigItemDTO) {
		String driverClassName = dbConfigItemDTO.getDriverClassName();
		String url = dbConfigItemDTO.getJdbcUrl();
		String username = dbConfigItemDTO.getUsername();
		String password = dbConfigItemDTO.getPassword();
		DataSourceBuilder<?> factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
				.username(username).password(password).type(type);
		return factory.build();
	}
}
