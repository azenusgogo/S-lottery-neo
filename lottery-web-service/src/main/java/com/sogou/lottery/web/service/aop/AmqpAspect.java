package com.sogou.lottery.web.service.aop;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sogou.lottery.amqp.AmqpService;
import com.sogou.lottery.amqp.AmqpTaskTopics;
import com.sogou.lottery.base.vo.order.UserOrder;

@Aspect
@Component
public class AmqpAspect {
	
	private Log LOG = LogFactory.getLog(getClass());
	@Autowired
	// (required = false)
	private AmqpService amqpService;
	
	private final static String POINT_CUT_SPLIT_ORDER = "execution(* *..PayService.successPay(..))";
	
	@AfterReturning(pointcut = POINT_CUT_SPLIT_ORDER, returning = "ret")
	public void order(JoinPoint jp, Object ret) {
	
		try {
			if (amqpService == null) {
				return;
			}
			int res = (int) ret;
			
			UserOrder userOrder = findUserOrderArg(jp.getArgs());
			if (userOrder == null) {
				LOG.error(String.format("Not found [%s] argument [%s]", getClass(), jp));
				return;
			}
			if (res > 0) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(ReflectionToStringBuilder.toString(userOrder, ToStringStyle.SHORT_PREFIX_STYLE));
				}
				amqpService.sendMessage(AmqpTaskTopics.ORDER_TOSPLIT, userOrder.getUserOrderId());
			} else if (res < 0) {
				// TODO 暂时不加，上线后看报警多不多 如果，有的话，先临时调数处理
				// 此消息
				// amqpService.sendMessage(BusinessTaskTopics.ORDER_REFUND_MARK,
				// userOrder.getUserOrderId());
			}
		} catch (Exception e) {
			LOG.error(e, e);
		}
	}
	
	private UserOrder findUserOrderArg(Object[] args) {
	
		for (Object arg : args) {
			if (arg instanceof UserOrder) {
				return (UserOrder) arg;
			}
		}
		return null;
	}
}
