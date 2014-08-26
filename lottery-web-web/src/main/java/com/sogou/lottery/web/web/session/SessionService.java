package com.sogou.lottery.web.web.session;

import java.util.Date;
import java.util.Map;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.memcached.MemcachedConstantBase;
import com.sogou.lottery.base.memcached.MemcachedFactory;
import com.sogou.lottery.base.memcached.MemcachedTool;

public class SessionService {
	
	private static volatile SessionService instance = null;
	
	// session超时时间, 默认一个小时
	public static Integer SESSION_TIMEOUT = SystemConfigs.getIntValue("sessionTimeOut", 60 * 60 * 1000);
	
	public static SessionService getInstance() {
	
		if (instance == null) {
			instance = new SessionService();
		}
		return instance;
	}
	
	private MemcachedTool getMemcachedTool() {
	
		return MemcachedFactory.getInstance().getMemcachedTool(MemcachedConstantBase.MEMCACHED_SESSION);
	}
	
	public void saveSession(String id, @SuppressWarnings("rawtypes")
	Map session) {
	
		MemcachedTool mt = getMemcachedTool();
		mt.set(id, (Object)session, new Date(System.currentTimeMillis() + SESSION_TIMEOUT));
	}
	
	public void removeSession(String id) {
	
		MemcachedTool mc = getMemcachedTool();
		mc.delete(id);
	}
	
}
