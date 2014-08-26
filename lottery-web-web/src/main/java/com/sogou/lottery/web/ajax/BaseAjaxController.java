package com.sogou.lottery.web.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.util.ResponseUtil;

/**
 * 所有Ajax Controller都需要的操作
 */
@Controller
public class BaseAjaxController {
	
	@ExceptionHandler(Exception.class)
	public void handleException(Exception e) {
	
		e.printStackTrace();
		Map<String,Object> map = getRtMap(AjaxConstant.RETURN_CODE_NAME, AjaxConstant.SYSTEM_ERROR_CODE, null);
		getRtMap(AjaxConstant.RETURN_DESC_NAME, AjaxConstant.SYSTEM_ERROR_DESC, map);
		ResponseUtil.setCharacterEncoding("utf-8");
		ResponseUtil.print(JSON.toString(map));
		ResponseUtil.close();
	}
	
	private Map<String,Object> getRtMap(String key, Object value, Map<String,Object> rtMap) {
	
		if (rtMap == null) {
			rtMap = new HashMap<String,Object>();
		}
		rtMap.put(key, value);
		return rtMap;
	}
	
	public boolean verifyPara(String... params) {
	
		for (String param : params) {
			if (StringUtils.isBlank(param)) {
				return false;
			}
		}
		return true;
	}
	
}
