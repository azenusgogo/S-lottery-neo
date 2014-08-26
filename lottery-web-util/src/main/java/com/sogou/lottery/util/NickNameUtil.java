package com.sogou.lottery.util;

import org.apache.commons.lang.StringUtils;

public class NickNameUtil {
	
	//昵称替换
	private static final String NICKNAME_REPLACE = "*";
	private static final String NICKNAME_REPLACE_TWO = "**";
	private static final String NICKNAME_REPLACE_THREE = "***";

	public static String getNickNameShow(String nickName) {
		String nickNameShow = "";
		if (StringUtils.isNotBlank(nickName)) {
			int length = nickName.length();
			switch (length) {
				case 1:
					nickNameShow = NICKNAME_REPLACE;
					break;
				case 2:
					nickNameShow = nickName.charAt(0) + NICKNAME_REPLACE;
					break;
				case 3:
					nickNameShow = nickName.charAt(0) + NICKNAME_REPLACE + nickName.charAt(length - 1);
					break;
				case 4:
					nickNameShow = nickName.charAt(0) + NICKNAME_REPLACE_TWO + nickName.charAt(length - 1);
					break;
				default:
					nickNameShow = nickName.charAt(0) + NICKNAME_REPLACE_THREE + nickName.charAt(length - 1);
					break;
			}
		}
		return nickNameShow;
	}
}
