package com.zy.common.extend;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CloseableHttpClientFactoryBean implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {

	private CloseableHttpClient closeableHttpClient;

	private int maxTotal = 200;

	private int defaultMaxPerRoute = 100;

	private int connectTimeout = 6000;

	private int socketTimeout = 6000;

	@Override
	public void destroy() throws Exception {
		closeableHttpClient.close();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(maxTotal);
		cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
		closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE).setConnectionManager(cm).build();
	}

	@Override
	public CloseableHttpClient getObject() throws Exception {
		return closeableHttpClient;
	}

	@Override
	public Class<?> getObjectType() {
		return CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

}
