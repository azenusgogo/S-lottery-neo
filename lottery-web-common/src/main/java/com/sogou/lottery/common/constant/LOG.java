package com.sogou.lottery.common.constant;

import org.apache.log4j.Logger;

public class LOG {
	
	public static final Logger runLog = Logger.getLogger("run.log");// 运行日志，可以往里面记录所有业务的运行日志
	
	public static final Logger message = Logger.getLogger("message.log");// 消息
	public static final Logger memcachedLog = Logger.getLogger("memcached.log");
	
	public static final Logger accessLog = Logger.getLogger("lottery_access.log");// 控制层
	public static final Logger controller = Logger.getLogger("controller.log");// 控制层
	public static final Logger sessionLog = Logger.getLogger("session.log");
	
	public static final Logger qianbao = Logger.getLogger("qianbao.log");// 消息
	public static final Logger order = Logger.getLogger("order.log");
	
	public static final Logger user = Logger.getLogger("user.log");
}
