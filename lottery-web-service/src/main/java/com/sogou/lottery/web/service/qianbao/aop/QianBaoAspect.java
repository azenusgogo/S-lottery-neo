package com.sogou.lottery.web.service.qianbao.aop;

import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.common.constant.LOG;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class QianBaoAspect {

    private Logger log = LOG.qianbao;

    private final static String POINT_CUT_QIANBAO = "execution(* com.sogou.lottery.web.service.qianbao.service.QianBaoService.query*(..)) or execution(* com.sogou.lottery.web.service.qianbao.service.QianBaoService.do*(..)) or execution(* com.sogou.lottery.web.service.qianbao.service.QianBaoService.check*(..))";

    @Around(POINT_CUT_QIANBAO)
    public Object peakLog(ProceedingJoinPoint jp) throws Throwable {
        Date startDate = new Date();
        try {
            Object object = jp.proceed();
            Date endDate = new Date();
            log.info("[qianBaoLog-executeTime] method:"+jp.getSignature()+",startDate:"+ DateUtil.formatDate(startDate, DateUtil.Format.HYPHEN_YYYYMMDDHHMMSS) +",endDate:"+ DateUtil.formatDate(endDate, DateUtil.Format.HYPHEN_YYYYMMDDHHMMSS)+",timecosts:"+ (endDate.getTime() - startDate.getTime()));
            return object;
        } catch (Throwable e) {
            Date endDate = new Date();
            log.error("[qianBaoLog-error] method:"+jp.getSignature()+",startDate:"+ DateUtil.formatDate(startDate, DateUtil.Format.HYPHEN_YYYYMMDDHHMMSS) +",endDate:"+ DateUtil.formatDate(endDate, DateUtil.Format.HYPHEN_YYYYMMDDHHMMSS)+",timecosts:"+ (endDate.getTime() - startDate.getTime()));
            log.error(e, e);
            throw e;
        }
    }


}


