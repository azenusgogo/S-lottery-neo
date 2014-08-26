package com.sogou.lottery.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class LotteryUtil {
	
	/**
	 * 去除字符串的空格，如果为空，则返回""
	 * 
	 * @param string
	 * @return
	 */
	public static String trimString(String string) {
	
		if (StringUtils.isNotBlank(string)) {
			return string.trim();
		} else {
			return "";
		}
	}
	
	/**
	 * 获得用户最终ip地址
	 * 
	 * @param request
	 * @return 用户请求原始ip地址
	 */
	public static String getLastIp(HttpServletRequest request) {
	
		String rip = request.getRemoteAddr();
		String xff = request.getHeader("X-Forwarded-For");
		String ip;
		if (xff != null && xff.length() != 0) {
			int px = xff.lastIndexOf(',');
			if (px != -1) {
				ip = xff.substring(px + 1);
			} else {
				ip = xff;
			}
		} else {
			ip = rip;
		}
		return ip.trim();
	}
	
	/**
	 * 判断当前请求是否是iframe过来的
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isIframe(HttpServletRequest request) {
	
		String uri = request.getRequestURI();
		if (StringUtils.isNotBlank(uri) && uri.indexOf("Iframe") != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 从奖金池计算出有多少亿、多少万、多少元，可以换成多少500万
	 * 
	 * @param bonusPool
	 * @return 亿_万_元:多少注500万
	 */
	public static String getBonusPoolFormat(long bonusPool) {
	
		// 1.从分得到元
		bonusPool /= 100;
		// 2.从元得到亿
		long bonusYi = bonusPool / 10000 / 10000;
		// 3.从元得到万
		long bonusWan = (bonusPool - bonusYi * 10000 * 10000L) / 10000;
		// 4.从元得到元
		long bonusYuan = bonusPool - bonusWan * 10000L - bonusYi * 10000 * 10000L;
		// 5.得到开出一等奖的注数
		long betCount = bonusWan / 500 + bonusYi * 10000 / 500;
		return new StringBuilder().append(bonusYi).append("_").append(bonusWan).append("_").append(bonusYuan).append(":").append(betCount).toString();
	}
	
	public static void main(String[] args) {
	
		System.out.println(getBonusPoolFormat(3020001004200L));
	}
}
