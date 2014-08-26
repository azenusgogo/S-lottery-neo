package com.sogou.lottery.web.service.user.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.sogou.lottery.common.constant.LOG;

@Aspect
@Component
public class UserCacheAspect {
	
	private Logger log = LOG.memcachedLog;
	
	private final static String POINT_CUT_AROUND_EXCEPTION = "execution(* *..UserCacheService.get*(..)) or execution(* *..UserCacheService.set*(..)) or execution(* *..UserCacheService.query*(..))";
	
	@Around(POINT_CUT_AROUND_EXCEPTION)
	public Object tryException(ProceedingJoinPoint jp) {
	
		String name = "";
		try {
			name = jp.getSignature().getName();
			return jp.proceed();
		} catch (Throwable e) {
			log.error(e, e);
			if (name.startsWith("set")) {
				return false;
			}
			return null;
		}
	}
}
