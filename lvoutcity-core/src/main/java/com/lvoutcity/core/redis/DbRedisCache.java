package com.lvoutcity.core.redis;

import com.jfinal.plugin.activerecord.cache.ICache;
import com.jfinal.plugin.redis.Redis;

/** 
* @ClassName: DbRedisCache 
* @Description: ActiveRecord Redis Cache 扩展 
* @author Gdh
* @date 2016年4月25日 上午11:20:02 
*  
*/
public class DbRedisCache implements ICache{

	@Override
	public <T> T get(String cacheName, Object key) {
		return Redis.use().hget(cacheName, key);
	}

	@Override
	public void put(String cacheName, Object key, Object value) {
		Redis.use().hset(cacheName, key, value);
	}

	@Override
	public void remove(String cacheName, Object key) {
		Redis.use().hdel(cacheName, key);
	}

	@Override
	public void removeAll(String cacheName) {
		Redis.use().hdel(cacheName, Redis.use().hkeys(cacheName));
	}
}
