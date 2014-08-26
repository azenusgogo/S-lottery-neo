package com.sogou.lottery.web.service.passport.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.passport.PassportConstant;
import com.sogou.lottery.base.passport.PassportErrorCode;
import com.sogou.lottery.base.passport.dto.ResultPassportDto;
import com.sogou.lottery.base.util.HttpPoolUtil;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.util.MD5Util;
import com.sogou.lottery.web.service.passport.dto.PassportDto;
import com.sogou.lottery.web.service.passport.dto.PassportResultDto;
import com.sogou.lottery.web.service.passport.dto.PassportResultDto.PassportData;
import com.sogou.lottery.web.service.user.aop.UserLogAspect;

/**
 * http://updwiki.sogou-inc.com/pages/viewpage.action?pageId=6460900
 * 公用方法里*User命名的方法会被拦截
 * 
 * @author huangtao
 * @see UserLogAspect
 */
@Service
public class PassportService {
	
	private Log LOG = LogFactory.getLog(getClass());
	
	private final static String SUCCESS = "0";
	/**
	 * // 搜狗-史鹏治-通行证 18:16:12 // utf8
	 */
	private final static String ENCODE = "UTF-8";
	
	public ResultPassportDto<PassportData> authUser(PassportDto passportDto) {
	
		Validate.notEmpty(passportDto.getUserId());
		Validate.notEmpty(passportDto.getPassword());
		Map<String,String> params = generateParams(passportDto);
		params.put("password", passportDto.getPassword());
		params.put("createip ", passportDto.getUserIp());
		String response = HttpPoolUtil.sendPostHttpRequestStatic(getAuthUrl(), params, null, ENCODE, getConnectTimeout(), getSocketTimeout(), ENCODE);
		PassportResultDto dto = JsonUtil.fromJson(response, PassportResultDto.class);
		String error = dto.getStatus();
		ResultPassportDto<PassportData> result = null;
		if (SUCCESS.equals(error)) {
			result = new ResultPassportDto<PassportData>(dto.getData());
			return result;
		}
		PassportErrorCode passError = PassportErrorCode.getCode(error);
		result = new ResultPassportDto<PassportData>(passError == null ? -1 : passError.getCode(), passError == null ? "" : passError.getDesc());
		if (PassportErrorCode.CommonSystem.getPassportCode().equals(error) || PassportErrorCode.CommonArgument.getPassportCode().equals(error) || PassportErrorCode.CommonSign.getPassportCode().equals(error)) {
			// 系统错误
			LOG.fatal(result + " - " + passportDto);
		} else if (PassportErrorCode.AuthActive.getPassportCode().equals(error) || PassportErrorCode.AuthBlock.getPassportCode().equals(error) || PassportErrorCode.AuthBind.getPassportCode().equals(error) || PassportErrorCode.AuthLogin.getPassportCode().equals(error)) {
			// 账户异常
			LOG.error(result + " - " + passportDto);
		}
		return result;
	}
	
	/**
	 * http://updwiki.sogou-inc.com/pages/viewpage.action?pageId=6461099
	 * 4-16位的数字、字母、点、减号或下划线组成，必须以小写字母开头，不区分大小写
	 * 不能包含以下内容：admin、master、Abuse、contact、 help、 info、 jobs、 owner、 sales、
	 * staff、sales、support、www 为小纸条加的临时规则：如果登录名保护.，将.换成_的登录名如果存在，则提示用户“已被占用”
	 * 不允许sohu域账号注册 密码必须为字母、数字、字符且长度为6~16位
	 * 
	 * @param passportDto
	 *            注册账号的密码，密码为明文
	 * @return 注册只使用搜狗通行证的个性化注册
	 */
	public ResultPassportDto<PassportData> registerUser(PassportDto passportDto) {
	
		Validate.notEmpty(passportDto.getUserId());
		Validate.notEmpty(passportDto.getPassword());// 明文传输
		Validate.notEmpty(passportDto.getUserIp());
		Map<String,String> params = generateParams(passportDto);
		params.put("password", passportDto.getPassword());
		params.put("createip", passportDto.getUserIp());
		String response = HttpPoolUtil.sendPostHttpRequestStatic(getRegisterUrl(), params, null, ENCODE, getConnectTimeout(), getSocketTimeout(), ENCODE);
		PassportResultDto dto = JsonUtil.fromJson(response, PassportResultDto.class);
		String error = dto.getStatus();
		ResultPassportDto<PassportData> result = null;
		if (SUCCESS.equals(error)) {
			result = new ResultPassportDto<PassportData>(dto.getData());
			return result;
		}
		PassportErrorCode passError = PassportErrorCode.getCode(error);
		result = new ResultPassportDto<PassportData>(passError == null ? -1 : passError.getCode(), passError == null ? "" : passError.getDesc());
		if (PassportErrorCode.CommonSystem.getPassportCode().equals(error) || PassportErrorCode.CommonArgument.getPassportCode().equals(error) || PassportErrorCode.CommonSign.getPassportCode().equals(error) || PassportErrorCode.RegFailed.getPassportCode().equals(error) || PassportErrorCode.RegMobile.getPassportCode().equals(error) || PassportErrorCode.RegUrl.getPassportCode().equals(error) || PassportErrorCode.RegIp.getPassportCode().equals(error) || PassportErrorCode.RegTimes.getPassportCode().equals(error)) {
			// 系统错误
			LOG.fatal(result + " - " + passportDto);
		} else if (PassportErrorCode.RegEmail.getPassportCode().equals(error) || PassportErrorCode.RegUserId.getPassportCode().equals(error)) {
			// 账户异常
			LOG.error(result + " - " + passportDto);
		}
		return result;
	}
	
	/**
	 * http://updwiki.sogou-inc.com/pages/viewpage.action?pageId=6461007
	 * 
	 * @param passportDto
	 * @return
	 */
	public ResultPassportDto<PassportData> checkUser(PassportDto passportDto) {
	
		Validate.notEmpty(passportDto.getUserId());
		Map<String,String> params = generateParams(passportDto);
		String response = HttpPoolUtil.sendPostHttpRequestStatic(getCheckUrl(), params, null, ENCODE, getConnectTimeout(), getSocketTimeout(), ENCODE);
		PassportResultDto dto = JsonUtil.fromJson(response, PassportResultDto.class);
		String error = dto.getStatus();
		ResultPassportDto<PassportData> result = null;
		if (SUCCESS.equals(error)) {
			result = new ResultPassportDto<PassportData>(dto.getData());
			return result;
		}
		PassportErrorCode passError = PassportErrorCode.getCode(error);
		result = new ResultPassportDto<PassportData>(passError == null ? -1 : passError.getCode(), passError == null ? "" : passError.getDesc());
		if (PassportErrorCode.CommonSystem.getPassportCode().equals(error) || PassportErrorCode.CommonArgument.getPassportCode().equals(error) || PassportErrorCode.CommonSign.getPassportCode().equals(error) || PassportErrorCode.RegMobile.getPassportCode().equals(error)) {
			// 系统错误
			LOG.fatal(result + " - " + passportDto);
		}
		if (PassportErrorCode.RegRegisted.getPassportCode().equals(error)) {
			result.setRetcode(PassportErrorCode.getCode(PassportErrorCode.RegUsed.getPassportCode()).getCode());
		}
		return result;
	}
	
	private Map<String,String> generateParams(PassportDto passportDto) {
	
		String clientId = getClientId();
		String pwd = getClientPwd();
		String time = String.valueOf(System.currentTimeMillis());
		String code = MD5Util.getMD5(passportDto.getUserId() + clientId + pwd + time);
		passportDto.setClientId(clientId);
		passportDto.setCode(code);
		Map<String,String> params = new HashMap<>();
		params.put("client_id", clientId);
		params.put("ct", time);
		params.put("code", code);
		params.put("userid", passportDto.getUserId());
		return params;
	}
	
	/**
	 * 搜狗-史鹏治-通行证 18:17:00
	 * clientid：2012，serverSecret：=Rlo"PjYrM_epd3PTEG{`Ww$mR2@og
	 * 
	 * @return
	 */
	private String getClientId() {
	
		return SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_CLIENT_ID, PassportConstant.CONFIG_PASSPORT_CLIENT_ID_DEFAULT);
	}
	
	private String getClientPwd() {
	
		return SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_CLIENT_PWD, PassportConstant.CONFIG_PASSPORT_CLIENT_PWD_DEFAULT);
	}
	
	private String getCheckUrl() {
	
		return getHostUrl() + SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_CHK_URL, PassportConstant.CONFIG_PASSPORT_CHK_URL_DEFAULT);
	}
	
	private String getAuthUrl() {
	
		return getHostUrl() + SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_AUTH_URL, PassportConstant.CONFIG_PASSPORT_AUTH_URL_DEFAULT);
	}
	
	private String getRegisterUrl() {
	
		return getHostUrl() + SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_REG_URL, PassportConstant.CONFIG_PASSPORT_REG_URL_DEFAULT);
	}
	
	private String getHostUrl() {
	
		return SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_URL, PassportConstant.CONFIG_PASSPORT_URL_DEFAULT);
	}
	
	private int getSocketTimeout() {
	
		return Integer.parseInt(SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_AUTH_TIMEOUT, PassportConstant.CONFIG_PASSPORT_AUTH_TIMEOUT_DEFAULT));
	}
	
	private int getConnectTimeout() {
	
		return Integer.parseInt(SystemConfigs.get(PassportConstant.CONFIG_PASSPORT_AUTH_TIMEOUT, PassportConstant.CONFIG_PASSPORT_AUTH_TIMEOUT_DEFAULT));
	}
}
