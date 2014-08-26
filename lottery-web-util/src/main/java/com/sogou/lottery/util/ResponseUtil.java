package com.sogou.lottery.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.sogou.lottery.common.constant.WebConstant;

public class ResponseUtil {
	
	public static void print(String str) {

		try {
			WebConstant.responseTL.get().getWriter().print(str);
		} catch (IOException e) {
		}
	}
	
	public static void println(String str) {

		try {
			WebConstant.responseTL.get().getWriter().println(str);
		} catch (IOException e) {
		}
	}
	
	public static void setCharacterEncoding(String charset) {

		WebConstant.responseTL.get().setCharacterEncoding(charset);
	}
	
	public static void close() {

		try {
			WebConstant.responseTL.get().getWriter().close();
		} catch (IOException e) {
		}
	}
	
	public static void addHeader(String key, String value) {

		WebConstant.responseTL.get().addHeader(key, value);
	}
	
	public static HttpServletResponse getResponse() {

		return WebConstant.responseTL.get();
	}
}
