package com.sogou.lottery.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.sogou.lottery.common.constant.LOG;

public class ParamCheckUtil {
	
	private static final String XSS_PATTERN = "((<.+>)|(\\{.+\\})|(\\(.+\\).*>)|(/\\*.*/))+";
	private static final String XSS_PATTERN2 = "<|>|\\$|&gt|&lt|&#|/\\*.*\\*/|vbscript:|javascript:|=\\s*[\\[{\"']";
	private static Pattern pattern = Pattern.compile(XSS_PATTERN);
	private static Pattern pattern2 = Pattern.compile(XSS_PATTERN2);
	
	/**参数过滤，非法参数返回false，正确参数返回true
	 * @param param
	 * @return
	 */
	public static boolean check(String param) {

		if (StringUtils.isNotEmpty(param)) {
			if (pattern.matcher(param).find() || pattern2.matcher(param).find()) {
				LOG.controller.debug("risky param value: " + param);
				return false;
			}
		}
		return true;
	}
	
}
