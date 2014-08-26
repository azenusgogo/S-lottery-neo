package com.sogou.lottery.web.service.sensitive.service;

import org.springframework.stereotype.Service;

import com.sogou.lottery.web.service.sensitive.ACTree;
import com.sogou.lottery.web.service.sensitive.SensitiveData;

@Service
public class SensitiveService {
	
	private static ACTree aCTree = null;
	
	static {
		aCTree = new ACTree(SensitiveData.SENSITIVE_LIST);
	}
	
	public boolean containsSensitive(String str) {
	
		String findStr = aCTree.findFirstKeyWord(str);
		if (findStr == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public String sensitiveWords(String str) {
	
		return aCTree.findFirstKeyWord(str);
	}
}
