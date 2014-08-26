package com.sogou.lottery.web.web.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sogou.lottery.base.memcached.MemcachedConstantBase;
import com.sogou.lottery.base.memcached.MemcachedFactory;
import com.sogou.lottery.base.memcached.MemcachedTool;

public class HttpSessionSidWrapper extends HttpSessionWrapper {
	
	private String sid = "";
	
	@SuppressWarnings("rawtypes")
	private Map map = null;
	
	public Logger sessionLog = Logger.getLogger("session.log");
	
	@SuppressWarnings("rawtypes")
	public HttpSessionSidWrapper(String sid, HttpSession session) {
	
		super(session);
		this.sid = sid;
		this.map = new HashMap();
	}
	
	private MemcachedTool getMemcachedTool() {
	
		return MemcachedFactory.getInstance().getMemcachedTool(MemcachedConstantBase.MEMCACHED_SESSION);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Object getAttribute(String arg0) {
	
		MemcachedTool mt = getMemcachedTool();
		map = (Map) mt.get(sid);
		if (map == null) {
			sessionLog.info("session getAttribute sid failed: " + sid + ", name: " + arg0 + ", value: null");
			return null;
		}
		Object object = map.get(arg0);
		sessionLog.info("session getAttribute sid success: " + sid + ", name: " + arg0 + ", value: " + object);
		return object;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Enumeration getAttributeNames() {
	
		MemcachedTool mt = getMemcachedTool();
		map = (Map) mt.get(sid);
		if (map == null) {
			Set set = new TreeSet();
			return new Enumerator(set, true);
		}
		return (new Enumerator(this.map.keySet(), true));
	}
	
	@SuppressWarnings("rawtypes")
	public void invalidate() {
	
		MemcachedTool mt = getMemcachedTool();
		map = (Map) mt.get(sid);
		if (map != null) {
			this.map.clear();
			SessionService.getInstance().removeSession(this.sid);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void removeAttribute(String arg0) {
	
		MemcachedTool mt = getMemcachedTool();
		map = (Map) mt.get(sid);
		if (map != null) {
			this.map.remove(arg0);
			SessionService.getInstance().saveSession(this.sid, this.map);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setAttribute(String arg0, Object arg1) {
	
		MemcachedTool mt = getMemcachedTool();
		map = (Map) mt.get(sid);
		if (map == null) {
			map = new HashMap();
		}
		map.put(arg0, arg1);
		SessionService.getInstance().saveSession(this.sid, this.map);
		sessionLog.info("session setAttribute sid: " + sid + ", name: " + arg0 + ", value: " + arg1);
	}
	
	@Override
	public Object getValue(String arg0) {
	
		return getAttribute(arg0);
	}
	
	@Override
	public void putValue(String arg0, Object arg1) {
	
		setAttribute(arg0, arg1);
	}
	
	@Override
	public void removeValue(String arg0) {
	
		removeAttribute(arg0);
	}
}
