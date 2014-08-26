package com.sogou.lottery.web.service.init;

import com.sogou.lottery.common.constant.VersionConstant;


/**
 * 版本号bean，配置在ini-config.xml中，从version.properities中读取
 */
public class VersionBean {
	
	private String versionId;
	
	public String getVersionId() {

		return versionId;
	}
	
	public void setVersionId(String versionId) {

		this.versionId = versionId;
		VersionConstant.setVersionId(versionId);
	}
}
