package com.damai.datasource.database.dynamic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * 动态数据源
 *
 */
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

	/**
	 * 原子计数器，用于平均分配请求任务至各从库中
	 */
	private AtomicLong slaveCount = new AtomicLong();
	private int slaveSize = 0;

	@Resource(name = "masterDataSource")
	private DataSource masterDataSource;

	@Resource(name = "slaveDataSources")
	private Map<String, DataSource> slaveDataSources;

	private Map<Object, Object> dataSources = new HashMap<Object, Object>();

	private static final String DEFAULT = "master";
	private static final String SLAVE = "slave";

	private static final ThreadLocal<LinkedList<String>> datasourceHolder = new ThreadLocal<LinkedList<String>>() {

		@Override
		protected LinkedList<String> initialValue() {
			return new LinkedList<String>();
		}

	};

	/**
	 * 初始化
	 */
	@Override
	public void afterPropertiesSet() {
		if (null == masterDataSource) {
			throw new IllegalArgumentException("Property 'master' is required");
		}
		dataSources.put(DEFAULT, masterDataSource);
		if (null != slaveDataSources && slaveDataSources.size() > 0) {
			for (String key : slaveDataSources.keySet()) {
				dataSources.put(key, slaveDataSources.get(key));
			}
			slaveSize = slaveDataSources.size();
		}
		this.setDefaultTargetDataSource(masterDataSource);
		this.setTargetDataSources(dataSources);
		super.afterPropertiesSet();
	}

	/**
	 * 选择使用主库，并把选择放到当前ThreadLocal的栈顶
	 */
	public static void useMaster() {
		if (logger.isDebugEnabled()) {
			logger.debug("use datasource :" + datasourceHolder.get());
		}
		LinkedList<String> masterHolder = datasourceHolder.get();
		masterHolder.offerFirst(DEFAULT);
	}

	/**
	 * 选择使用从库，并把选择放到当前ThreadLocal的栈顶
	 */
	public static void useSlave() {
		if (logger.isDebugEnabled()) {
			logger.debug("use datasource :" + datasourceHolder.get());
		}
		LinkedList<String> slaveHolder = datasourceHolder.get();
		slaveHolder.offerFirst(SLAVE);
	}

	/**
	 * 重置当前栈
	 */
	public static void reset() {
		LinkedList<String> currentHolder = datasourceHolder.get();
		if (logger.isDebugEnabled()) {
			logger.debug("reset datasource {}", currentHolder);
		}
		if (currentHolder.size() > 0) {
			currentHolder.poll();
		}
	}

	/**
	 * 如果是选择使用从库，且从库的数量大于1，则通过取模来控制从库的负载, 计算结果返回AbstractRoutingDataSource
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		LinkedList<String> holder = datasourceHolder.get();
		String key = holder.peekFirst() == null ? DEFAULT : holder.peekFirst();
		if (logger.isDebugEnabled()) {
			logger.debug("currenty datasource :" + key);
		}
		if (null != key) {
			if (DEFAULT.equals(key)) {
				return key;
			} else if (SLAVE.equals(key)) {
				// 如果从库数据源数量大于1，则开启自动负载均衡
				if (slaveSize > 1) {
					// 如果原子计数器最大值超出Long最大值，则重新设置为0,继续执行
					if (slaveCount.get() >= Long.MAX_VALUE) {
						slaveCount.set(0);
					}

					long c = slaveCount.incrementAndGet();
					c = c % slaveSize;
					int i = 0;
					for (String keyValue : slaveDataSources.keySet()) {
						if (i == c)
							return keyValue;
						i++;
					}
				} else if (slaveSize == 1) {
					for (String keyValue : slaveDataSources.keySet()) {
						return keyValue;
					}
				} else {
					// 如果未配置从库，则使用主库
					logger.warn("未配置从库，切换至主库");
					return DEFAULT;
				}
			}
			return null;
		} else {
			return null;
		}
	}
}