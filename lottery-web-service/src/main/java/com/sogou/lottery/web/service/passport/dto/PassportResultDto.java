package com.sogou.lottery.web.service.passport.dto;

/**
 * http://updwiki.sogou-inc.com/pages/viewpage.action?pageId=6460900 10001 系统级错误
 * 10002 参数错误,请输入必填的参数或参数验证失败 10003 code签名错误 20206 用户名或密码不正确 20231 账号未激活 20232
 * 账号已封杀 20233 手机号未被绑定 20230 账号或者IP登陆异常 密码格式校验，必须为字母、数字、字符且长度为6~16位； 校验验证码是否正确；
 * 一天内，同一IP登陆失败50次（黑名单网段为5次），或者同一IP登陆成功1000次，该IP被封禁，直到缓存失效，才可以进行再次登陆；
 * 一小时内，用一用户登录失败10次，或者登陆成功20次，该账号被封禁，直到缓存失效，才可以进行再次登陆；
 * 
 * @author huangtao
 */
public class PassportResultDto {
	
	private PassportData data;
	private String status;
	private String statusText;
	
	public static class PassportData {
		
		private String userid;
		private String uniqname;
		private String createtime;
		private String flag;//检查唯一接口使用
		
		public String getUserid() {
		
			return userid;
		}
		
		public void setUserid(String userid) {
		
			this.userid = userid;
		}
		
		public String getUniqname() {
		
			return uniqname;
		}
		
		public void setUniqname(String uniqname) {
		
			this.uniqname = uniqname;
		}
		
		public String getCreatetime() {
		
			return createtime;
		}
		
		public void setCreatetime(String createtime) {
		
			this.createtime = createtime;
		}
		
		public String getFlag() {
		
			return flag;
		}
		
		public void setFlag(String flag) {
		
			this.flag = flag;
		}
	}
	
	public PassportData getData() {
	
		return data;
	}
	
	public void setData(PassportData data) {
	
		this.data = data;
	}
	
	public String getStatus() {
	
		return status;
	}
	
	public void setStatus(String status) {
	
		this.status = status;
	}
	
	public String getStatusText() {
	
		return statusText;
	}
	
	public void setStatusText(String statusText) {
	
		this.statusText = statusText;
	}
	
}
