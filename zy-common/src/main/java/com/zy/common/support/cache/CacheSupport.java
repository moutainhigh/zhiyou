package com.zy.common.support.cache;


public interface CacheSupport {
	
	public static int DEFAULT_EXPIRE = 3600;
	
	public <T> T get(String cacheName, String key) ;
	
	public void set(String cacheName, String key, Object value); // 默认3600秒
	
	public void set(String cacheName, String key, Object value, int expire); // 过期时间秒
	
	public void delete(String cacheName, String key);

	public Object getNaitiveCache();
	
}

