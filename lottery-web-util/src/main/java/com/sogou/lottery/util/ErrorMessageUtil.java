package com.sogou.lottery.util;

import java.util.ArrayList;
import java.util.List;

import com.sogou.lottery.common.constant.WebConstant;

public class ErrorMessageUtil {
	
	public static void put(String errorMessage) {

		List<String> emList = WebConstant.ERROR_MESSAGE_TL.get();
		if (emList == null) {
			emList = new ArrayList<String>();
			WebConstant.ERROR_MESSAGE_TL.set(emList);
		}
		emList.add(errorMessage);
	}
	
	public static List<String> get() {

		List<String> emList = WebConstant.ERROR_MESSAGE_TL.get();
		if (emList == null || emList.size() == 0) {
			return null;
		}
		return emList;
	}
	
	public static void clear() {

		List<String> emList = WebConstant.ERROR_MESSAGE_TL.get();
		if (emList != null) {
			emList.clear();
		}
		
	}
}
