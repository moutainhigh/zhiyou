package com.zy.common.support.cache;

import org.springframework.util.Assert;

import net.spy.memcached.MemcachedClient;

public class MemcachedSupport implements CacheSupport {
	
	private final MemcachedClient memcachedClient;
	
	private final String group;
	
	public MemcachedSupport(MemcachedClient memcachedClient, String group) {
		this.memcachedClient = memcachedClient;
		this.group = group;
	}

	private String wrapKey(String cacheName, Object key) {
		Assert.notNull(key, "key must not be null");
		Assert.notNull(cacheName, "cacheName must not be null");
		return group + ":" + cacheName + ":" + key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S get(String cacheName, String key) {
		String wrappedKey = wrapKey(cacheName, key);
		Object value = memcachedClient.get(wrappedKey);
		return (S) value;
	}

	@Override
	public void set(String cacheName, String key, Object value) {
		set(cacheName, key, value, DEFAULT_EXPIRE);
		
	}

	@Override
	public void set(String cacheName, String key, Object value, int expire) {
		String wrappedKey = wrapKey(cacheName, key);
		memcachedClient.set(wrappedKey, expire, value);
	}

	@Override
	public void delete(String cacheName, String key) {
		String wrappedKey = wrapKey(cacheName, key);
		memcachedClient.delete(wrappedKey);
	}

	@Override
	public MemcachedClient getNaitiveCache() {
		return memcachedClient;
	}
	
}
