package com.sogou.lottery.web.service.init;

import org.apache.commons.lang3.StringUtils;

/**
 * 环境变量bean，配置在spring-context.xml中，从env.properities中读取
 */
public class EnvironmentBean {
	
	private String cdnUrlString = "http://img01.caipiao.sogoucdn.com/,http://img02.caipiao.sogoucdn.com/,http://img03.caipiao.sogoucdn.com/,http://img04.caipiao.sogoucdn.com/";
	private String domainUrlString = "http://cp.sogou.com/";
	private String env = "production";
	private static String cdnBaseUrl = "http://img01.caipiao.sogoucdn.com/";
	private static String cdnBaseUrl2 = "http://img02.caipiao.sogoucdn.com/";
	private static String cdnBaseUrl3 = "http://img03.caipiao.sogoucdn.com/";
	private static String cdnBaseUrl4 = "http://img04.caipiao.sogoucdn.com/";
	private static String domainUrl = "http://cp.sogou.com/";
	private static String environment = "production";
	
	public String getCdnUrlString() {
	
		return cdnUrlString;
	}
	
	public void setCdnUrlString(String cdnUrlString) {
	
		this.cdnUrlString = cdnUrlString;
		if (StringUtils.isNotBlank(cdnUrlString)) {
			String[] cdnUrl = cdnUrlString.split(",");
			if (cdnUrl.length > 0) {
				cdnBaseUrl = cdnUrl[0];
			}
			if (cdnUrl.length > 1) {
				cdnBaseUrl2 = cdnUrl[1];
			}
			if (cdnUrl.length > 2) {
				cdnBaseUrl3 = cdnUrl[2];
			}
			if (cdnUrl.length > 3) {
				cdnBaseUrl4 = cdnUrl[3];
			}
		}
	}
	
	public String getDomainUrlString() {
	
		return domainUrlString;
	}
	
	public void setDomainUrlString(String domainUrlString) {
	
		this.domainUrlString = domainUrlString;
		if (StringUtils.isNotBlank(this.domainUrlString)) {
			setDomainUrl(this.domainUrlString);
		}
	}
	
	public String getEnv() {
	
		return env;
	}
	
	public void setEnv(String env) {
	
		this.env = env;
		if (StringUtils.isNotBlank(env)) {
			setEnvironment(env);
		}
	}
	
	public static String getCdnBaseUrl() {
	
		return cdnBaseUrl;
	}
	
	public static void setCdnBaseUrl(String cdnBaseUrl) {
	
		EnvironmentBean.cdnBaseUrl = cdnBaseUrl;
	}
	
	public static String getCdnBaseUrl2() {
	
		return cdnBaseUrl2;
	}
	
	public static void setCdnBaseUrl2(String cdnBaseUrl2) {
	
		EnvironmentBean.cdnBaseUrl2 = cdnBaseUrl2;
	}
	
	public static String getCdnBaseUrl3() {
	
		return cdnBaseUrl3;
	}
	
	public static void setCdnBaseUrl3(String cdnBaseUrl3) {
	
		EnvironmentBean.cdnBaseUrl3 = cdnBaseUrl3;
	}
	
	public static String getCdnBaseUrl4() {
	
		return cdnBaseUrl4;
	}
	
	public static void setCdnBaseUrl4(String cdnBaseUrl4) {
	
		EnvironmentBean.cdnBaseUrl4 = cdnBaseUrl4;
	}
	
	public static String getDomainUrl() {
	
		return domainUrl;
	}
	
	public static String getHttpsDomainUrl() {
		
		return domainUrl.replace("http://", "https://");
	}
	
	public static void setDomainUrl(String domainUrl) {
	
		EnvironmentBean.domainUrl = domainUrl;
	}
	
	public static String getEnvironment() {
	
		return environment;
	}
	
	public static void setEnvironment(String environment) {
	
		EnvironmentBean.environment = environment;
	}
	
}
