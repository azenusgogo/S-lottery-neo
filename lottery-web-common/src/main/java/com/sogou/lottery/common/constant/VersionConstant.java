package com.sogou.lottery.common.constant;

import org.apache.commons.lang3.StringUtils;

public final class VersionConstant {
	
	public static String VERSION_ID = String.valueOf(System.currentTimeMillis()/1000/60);// 版本号，从version.properities中读取，从versionBean中set
	
	public static final VersionConstant INSTANCE = new VersionConstant();
	
	private VersionConstant() {
	
		if (INSTANCE != null) {
			throw new RuntimeException("不能实例化单例VersionConstant");
		}
	}
	
	/**
	 * 通过传参更新版本号
	 */
	public static void setVersionId(String versionId) {
	
		if (StringUtils.isNotBlank(versionId)) {
			VERSION_ID = versionId;
		}
	}
}
