package com.sogou.lottery.web.service.qianbao.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.sogou.lottery.base.constant.Dictionary;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.user.QianBaoConstant;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.DateUtil.Format;
import com.sogou.lottery.base.util.HttpPoolUtil;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.util.MD5Util;
import com.sogou.lottery.base.util.MoneyUtil;
import com.sogou.lottery.base.util.XStreamUtil;
import com.sogou.lottery.base.vo.qianbao.AccountVo;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.util.PageUtil;
import com.sogou.lottery.web.service.init.EnvironmentBean;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakBalanceResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakCommonResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakCompleteResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakDetailsResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakDetailsResultDto.PeakDetailDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakIdResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakMobileResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakPayResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakSafeResultDto;
import com.sogou.lottery.web.service.qianbao.dto.PeakTokenResultDto;
import com.sogou.lottery.web.service.user.constant.TransType;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.ChangePwdDto;
import com.sogou.lottery.web.service.user.dto.ChangeSafeDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.FindPwdByMobileDto;
import com.sogou.lottery.web.service.user.dto.FindPwdBySafeDto;
import com.sogou.lottery.web.service.user.dto.IdResultDto;
import com.sogou.lottery.web.service.user.dto.InfoDto;
import com.sogou.lottery.web.service.user.dto.InfoResultDto;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.SafeResultDto;
import com.sogou.lottery.web.service.user.dto.TransDetailResultDto;
import com.sogou.lottery.web.service.user.dto.TransDto;
import com.sogou.lottery.web.service.user.dto.TransResultDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.dto.WithdrawAuditDto;
import com.sogou.lottery.web.service.user.dto.WithdrawDto;

@Service
public class QianBaoService {
	
	private Logger log = LOG.qianbao;
	@Autowired
	private QianBaoAccountService qianBaoAccountService;
	
	// private static final Type mapType = new TypeToken<Map<String,String>>()
	// {}.getType();
	
	/**
	 * 接口2.2 查询账户余额
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<BalanceDto> queryUserBalance(UserDto dto) {
	
		try {
			PeakBalanceResultDto resultDto = peakQueryUserBalance(dto);
			String signFrom = resultDto.getKey();
			// Md5(username + securitylevel + error_code + availableamount+
			// frozenamt + availablewithdraw +code+ seed
			String signTo = getSign(getCommonPsid(), resultDto.getUsername(), resultDto.getSecuritylevel(), resultDto.getError_code(), resultDto.getAvailableamount(), resultDto.getFrozenamt(), resultDto.getAvailablewithdraw(), resultDto.getCode());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Balance Querying Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			}
			// status 0 成功 1 参数错误 2 验证错误 3 其他异常
			// code 0-正常,1-账户未激活，2-账户冻结中，3-异常状态,4-禁用状态
			if ("0".equals(resultDto.getError_code())) {
				BalanceDto result = new BalanceDto(dto);
				result.setAvailableAmount(MoneyUtil.yuan2fen(resultDto.getAvailableamount()));
				result.setAvailableWithDrawAmount(MoneyUtil.yuan2fen(resultDto.getAvailablewithdraw()));
				result.setFrozenAmount(MoneyUtil.yuan2fen(resultDto.getFrozenamt()));
				result.setStatus(Integer.parseInt(resultDto.getError_code()));
				ResultUserDto<BalanceDto> res = null;
				// 黄涛 18:17:04
				// 我知道，补全之前，充值，查余额都没问题?
				// 搜狗-李少松 18:17:12
				// 是的
				// 黄涛 18:18:19
				// 未激活只是对应未补全信息这一个状态吧?
				// 补全之后，账户还会变成未激活码？
				// 搜狗-李少松 18:18:44
				// 不会
				if ("0".equals(resultDto.getCode()) || "1".equals(resultDto.getCode())) {
					return new ResultUserDto<>(result);
				} else {
					if ("2".equals(resultDto.getCode())) {
						res = new ResultUserDto<>(UserErrorCode.BalanceFrozen);
					} else if ("4".equals(resultDto.getCode())) {
						res = new ResultUserDto<>(UserErrorCode.BalanceForbidden);
					} else {
						res = new ResultUserDto<>(UserErrorCode.BalanceUserExcep);
					}
					res.setResult(result);
					return res;
				}
			} else if ("1".equals(resultDto.getError_code())) {
				return new ResultUserDto<>(UserErrorCode.BalanceArgument);
			} else if ("2".equals(resultDto.getError_code())) {
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			} else {
				return new ResultUserDto<>(UserErrorCode.QianbaoSystem);
			}
		} catch (Exception e) {
			log.fatal("PEAK Balance Querying Error - " + dto, e);
			return new ResultUserDto<>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakBalanceResultDto peakQueryUserBalance(UserDto dto) {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		String querytime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDD);
		params.put("querytime", querytime);
		params.put("requestip", getIp(dto.getUserIp()));
		// md5(psid+querytime+username + requestip +seed)
		String key = getSign(psid, psid, querytime, dto.getUserId(), dto.getUserIp());
		params.put("Key", key);
		String response = sendRequest("getAccount.action", params);
		return (PeakBalanceResultDto) XStreamUtil.fromXml(response, PeakBalanceResultDto.class);
	}
	
	/**
	 * 接口2.3 这里不填写手机号，手机号通过绑定手机传入钱包系统
	 * 
	 * @param info
	 * @return
	 */
	public ResultUserDto<InfoResultDto> doCompleteInfo(InfoDto dto) {
	
		try {
			PeakCompleteResultDto resultDto = peakDoCompletedInfo(dto);
			String signFrom = resultDto.getSignstr();
			// Md5(username+error_code+seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getUsername(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Complte Info Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<InfoResultDto>(UserErrorCode.QianbaoSign);
			}
			int errorCode = Integer.parseInt(resultDto.getError_code());
			if (errorCode == 0) {
				ResultUserDto<InfoResultDto> ret = new ResultUserDto<InfoResultDto>();
				ret.setRetcode(0);
				return ret;
			} else {
				return buildCommonErrorResultDto(resultDto, "Info");
			}
		} catch (Exception e) {
			log.fatal("PEAK Complte Info Error - " + dto, e);
			return new ResultUserDto<InfoResultDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakCompleteResultDto peakDoCompletedInfo(InfoDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		String username = dto.getUserId();
		params.put("username", username);
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		String realName = URLEncoder.encode(dto.getTrueName(), QianBaoConstant.PEAK_ENCODE);
		params.put("realName", realName);
		params.put("password", dto.getPayPwd());
		String safeq = getSafeQuestion(dto.getQuestionType());
		dto.setSafeQuestion(safeq);
		String safeQuestion = URLEncoder.encode(safeq, QianBaoConstant.PEAK_ENCODE);
		params.put("safeQuestion", safeQuestion);
		String safeAnswer = dto.getSafeAnswer();
		params.put("safeAnswer", safeAnswer);
		String cardType = getIdCardType(dto.getIdCardType());
		params.put("cardType", cardType);
		params.put("idCard", dto.getIdCardNo());
		params.put("mobile", dto.getMobile());
		params.put("verifyCode", dto.getCaptcha());// peak后加的 少松
		// md5(psid+username+ reqtime requestip + realName + password +
		// safeQuestion + safeAnswer + cardType + idCard + mobile + email +seed)
		String sign = getSign(psid, psid, username, reqtime, requestip, dto.getTrueName(), dto.getPayPwd(), safeq, dto.getSafeAnswer(), cardType, dto.getIdCardNo(), dto.getMobile(), dto.getCaptcha());
		params.put("signstr", sign);
		String response = sendRequest("completeAccount.action", params);
		PeakCompleteResultDto result = (PeakCompleteResultDto) XStreamUtil.fromXml(response, PeakCompleteResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	private void decodeErrorDesc(String psid, PeakCommonResultDto dto) throws UnsupportedEncodingException {
	
		if (dto == null) {
			return;
		}
		if (psid != null) {
			dto.setPsid(psid);
		}
		if (StringUtils.isNotBlank(dto.getError_desc())) {
			dto.setError_desc(URLDecoder.decode(dto.getError_desc(), QianBaoConstant.PEAK_ENCODE));
		}
	}
	
	/**
	 * 2.4接口
	 * 
	 * @return
	 */
	public String queryRechargeToken() {
	
		try {
			PeakTokenResultDto result = peakQueryRechargeToken();
			// MD5(psid+ token +seed)
			String signFrom = result.getSignstr();
			// 搜狗-李少松 16:23:01
			// psid+token+errorCode+merchant.getSeed() 加上错误码 文档没有啊
			String signTo = getSign(result.getPsid(), result.getPsid(), result.getToken(), result.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Recharge Token Sign Error - " + result + " - " + signTo);
				return null;
			}
			int errorCode = Integer.parseInt(result.getError_code());
			String desc = URLDecoder.decode(result.getError_desc(), QianBaoConstant.PEAK_ENCODE);
			if (errorCode != 0) {
				log.fatal("PEAK Recharge Token Querying Failed - " + errorCode + "|" + desc);
				return null;
			}
			return result.getToken();
		} catch (Exception e) {
			log.fatal("PEAK Recharge Token Querying Error", e);
			return null;
		}
	}
	
	private PeakTokenResultDto peakQueryRechargeToken() throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("service", "token");
		params.put("reqtime", reqtime);
		String sign = getSign(psid, psid, "token", reqtime);
		// MD5(psid+ service + reqtime +seed)
		params.put("signstr", sign);
		// 线上token获取和线下地址不一样
		String url = getRechargeTokenHost() + "tokenApply.action";
		String response = sendRequest(url, params);
		PeakTokenResultDto result = (PeakTokenResultDto) XStreamUtil.fromXml(response, PeakTokenResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.5接口，拼接发起充值的url
	 * 
	 * @param dto
	 * @return
	 */
	public String queryRechargeUrl(RechargeDto dto) {
	
		Map<String,String> params = new LinkedHashMap<>();
		String psid = getCommonPsid();
		params.put("psid", psid);
		params.put("ps_requestId", dto.getId());
		params.put("username", dto.getUserId());
		String amount = MoneyUtil.fen2yuan(dto.getAmount());
		params.put("amt", amount);
		String paygate = getChargePayGateCode(dto.getBankId());
		params.put("paygate", paygate);
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("reqtime", reqtime);
		String userIp = getIp(dto.getUserIp());
		params.put("requestip", userIp);
		params.put("token", dto.getToken());
		String returl = getRechargeReturnUrl();
		params.put("returl", returl);
		String returlPage = getRechargePageReturnUrl();
		params.put("returl_page", returlPage);
		// MD5(psid+ps_requestId
		// +username+amt+paygate+reqtime+req
		// uestip+token+returl+seed)
		String sign = getSign(psid, psid, dto.getId(), dto.getUserId(), amount, paygate, reqtime, userIp, dto.getToken(), returl, returlPage);
		params.put("signstr", sign);
		StringBuffer sb = new StringBuffer(getRechargeHost()).append("?");
		int i = 0;
		for (Entry<String,String> entry : params.entrySet()) {
			if (i > 0) {
				sb.append("&");
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue());
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 2.6接口 余额支付
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<PayResultDto> doBalancePay(PayDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getCashAmount());
			Validate.notBlank(dto.getUserId());
			Validate.notBlank(dto.getUserIp());
			Validate.notBlank(dto.getPayPwd());
			Validate.notBlank(dto.getPayOrderId());
			Validate.notNull(dto.getPayDeadline());
			// Validate.notNull(dto.getCreateTime());
			// 这里实际走的是批量支付接口
			PeakPayResultDto resultDto = peakDoBalancePay(dto);
			// 1.String t1=orderid1+amt1+ transnum1+
			// + succmark1
			// 2.String t= psid+ ymd+
			// moneytype+t1+t2+t3…+seed
			String signFrom = resultDto.getSignstr();
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getMoneytype(), resultDto.getOrderid(), resultDto.getAmount(), resultDto.getTransnum(), resultDto.getSuccmark(), resultDto.getYmd());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Balance Pay Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			}
			if ("Y".equalsIgnoreCase(resultDto.getSuccmark())) {
				PayResultDto result = new PayResultDto();
				result.setPayOrderId(resultDto.getOrderid());
				result.setCashAmount(MoneyUtil.yuan2fen(resultDto.getAmount()));
				result.setPaySystemId(resultDto.getTransnum());
				result.setPaySystemTime(new Timestamp(DateUtil.getCurrentDate().getTime()));
				return new ResultUserDto<>(result);
			} else if (StringUtils.isNotBlank(resultDto.getCause())) {
				if (resultDto.getCause().contains("余额不足")) {
					return new ResultUserDto<>(UserErrorCode.PayBalance.getCode(), UserErrorCode.PayBalance.getDesc());
				} else if (resultDto.getCause().contains("不能为空")) {
					return new ResultUserDto<>(UserErrorCode.CommonBlankArgument.getCode(), UserErrorCode.CommonBlankArgument.getDesc());
				} else if (resultDto.getCause().contains("签名错误")) {
					return new ResultUserDto<>(UserErrorCode.CommonSign.getCode(), UserErrorCode.CommonSign.getDesc());
				} else if (resultDto.getCause().contains("转换错误")) {
					return new ResultUserDto<>(UserErrorCode.CommonEncode.getCode(), UserErrorCode.CommonEncode.getDesc());
				} else if (resultDto.getCause().contains("未登记")) {
					return new ResultUserDto<>(UserErrorCode.CommonPsid.getCode(), UserErrorCode.CommonPsid.getDesc());
				} else if (resultDto.getCause().contains("用户不存在")) {
					return new ResultUserDto<>(UserErrorCode.CommonUserNotExist.getCode(), UserErrorCode.CommonUserNotExist.getDesc());
				} else if (resultDto.getCause().contains("不存在")) {
					return new ResultUserDto<>(UserErrorCode.PayOrderNotExist.getCode(), UserErrorCode.PayOrderNotExist.getDesc());
				} else if (resultDto.getCause().contains("补全账户")) {
					return new ResultUserDto<>(UserErrorCode.CommonIncomplet.getCode(), UserErrorCode.CommonIncomplet.getDesc());
				} else if (resultDto.getCause().contains("连续输入")) {
					return new ResultUserDto<>(UserErrorCode.PayPwdFrozen.getCode(), UserErrorCode.PayPwdFrozen.getDesc());
				} else if (resultDto.getCause().contains("密码错误")) {
					return new ResultUserDto<>(UserErrorCode.PayPwd.getCode(), UserErrorCode.PayPwd.getDesc());
				} else if (resultDto.getCause().contains("金额格式")) {
					return new ResultUserDto<>(UserErrorCode.PayAmountFormat.getCode(), UserErrorCode.PayAmountFormat.getDesc());
				} else if (resultDto.getCause().contains("支付错误")) {
					return new ResultUserDto<>(UserErrorCode.PayError.getCode(), UserErrorCode.PayError.getDesc());
				} else if (resultDto.getCause().contains("超出范围") || resultDto.getCause().contains("之间")) {
					return new ResultUserDto<>(UserErrorCode.PayAmountLimit.getCode(), UserErrorCode.PayAmountLimit.getDesc());
				} else if (resultDto.getCause().contains("已失败")) {
					return new ResultUserDto<>(UserErrorCode.PayOrderFail.getCode(), UserErrorCode.PayOrderFail.getDesc());
				} else if (resultDto.getCause().contains("已过期")) {
					return new ResultUserDto<>(UserErrorCode.PayOrderExpried.getCode(), UserErrorCode.PayOrderExpried.getDesc());
				} else if (resultDto.getCause().contains("已关闭")) {
					return new ResultUserDto<>(UserErrorCode.PayOrderClosed.getCode(), UserErrorCode.PayOrderClosed.getDesc());
				}
			}
			log.fatal("PEAK Balance Pay Error - " + resultDto);
			return new ResultUserDto<>(UserErrorCode.QianbaoSystem);
		} catch (Exception e) {
			log.fatal("PEAK Balance Pay Querying Error - " + dto, e);
			return new ResultUserDto<>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakPayResultDto peakDoBalancePay(PayDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String moneytype = "0";
		// String billdetail = "";
		String notifytype = "0";
		String paygate = "000";
		String psid = getCommonPsid();
		String amount = MoneyUtil.fen2yuan(dto.getCashAmount());
		String returl = getBalancePayReturnUrl();
		String ymd = DateUtil.formatDate(dto.getCreateTime() == null ? DateUtil.getCurrentDate() : dto.getCreateTime(), Format.YYYYMMDD);
		String requestip = getIp(dto.getUserIp());
		String refundlimit = getRefundlimit();
		String expire = getPayTimeOut(dto.getPayDeadline());
		String suborders = Joiner.on("^").join(dto.getPayOrderId(), ymd, refundlimit, amount, expire, dto.getTitle());
		params.put("psid", psid);
		// params.put("orderid", dto.getPayOrderId());
		params.put("username", dto.getUserId());
		params.put("pwd", dto.getPayPwd());
		// params.put("amount", amount);
		params.put("moneytype", moneytype);
		// params.put("billdetail", billdetail);
		// params.put("ymd", ymd);
		params.put("suborders", URLEncoder.encode(suborders, QianBaoConstant.PEAK_ENCODE));
		params.put("paygate", paygate);
		// String expire = getPayTimeOut(dto.getPayDeadline());
		// params.put("expire", expire);
		params.put("returl", returl);
		params.put("notifytype", notifytype);
		// params.put("productname", productname);
		// String refundlimit = getRefundlimit();
		// params.put("refundlimit", refundlimit);
		params.put("requestip", requestip);
		// params.put("memo1", "");
		// params.put("memo2", "");
		// MD5(psid+username+pwd+ymd+moneytype+suborde
		// rs+paygate+returl+notifytyp+request
		// ip+seed)
		String sign = getSign(psid, psid, dto.getUserId(), dto.getPayPwd(), moneytype, suborders, paygate, returl, notifytype, requestip);
		params.put("signstr", sign);
		// 搜狗-森强 18:07:52
		// 单笔的之前有其他业务现在用，所以改起来复杂些。批量的是搜狗彩票专用的，所以修改比较简单。
		String response = sendRequest("batchpaybycmd.action", params);
		response = response.replace("<order>", "").replace("<orders>", "").replace("</order>", "").replace("</orders>", "");
		PeakPayResultDto result = (PeakPayResultDto) XStreamUtil.fromXml(response, PeakPayResultDto.class);
		if (StringUtils.isNotBlank(result.getCause())) {
			String desc = URLDecoder.decode(result.getCause(), QianBaoConstant.PEAK_ENCODE);
			result.setCause(desc);
		}
		return result;
	}
	
	/**
	 * 2.10接口 提现申请
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<IdResultDto> doWithDraw(WithdrawDto dto) {
	
		try {
			PeakIdResultDto resultDto = peakDoWithDraw(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+ ps_requestId+ error_code +seed
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getPs_requestId(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Withdraw Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<IdResultDto>(UserErrorCode.QianbaoSign);
			}
			return buildCommonIdResultDto(resultDto, "Draw");
		} catch (Exception e) {
			log.fatal("PEAK Withdraw Error - " + dto, e);
			return new ResultUserDto<IdResultDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakIdResultDto peakDoWithDraw(WithdrawDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		// psid：2255 搜狗彩票4 （推广账户，发起提现用这个psid）
		String psid = getPromoPsid();
		String returl = getWithDrawReturnUrl();
		params.put("psid", psid);
		params.put("ps_requestId", dto.getId());
		params.put("username", dto.getUserId());
		params.put("pwd", dto.getPayPwd());
		String audit = dto.getAudit() ? "1" : "0";
		params.put("needaudit", audit);
		String maxuserfee = MoneyUtil.fen2yuan(dto.getFee());
		params.put("maxuserfee", maxuserfee);
		String trueName = URLEncoder.encode(dto.getTrueName(), QianBaoConstant.PEAK_ENCODE);
		params.put("receiveuser", trueName);
		params.put("receiveaccount", dto.getBankCardNo());
		String amount = MoneyUtil.fen2yuan(dto.getAmount());
		params.put("amount", amount);
		String bankName = getDrawBankName(dto.getBankId());
		String bank = URLEncoder.encode(bankName, QianBaoConstant.PEAK_ENCODE);
		params.put("bank", bank);
		String[] local = getProvinceCity(dto.getProvince(), dto.getCity());
		if (local != null && local.length == 2) {
			if (local[0] != null) {
				String province = URLEncoder.encode(local[0], QianBaoConstant.PEAK_ENCODE);
				params.put("province", province);
			}
			if (local[1] != null) {
				String city = URLEncoder.encode(local[1], QianBaoConstant.PEAK_ENCODE);
				params.put("city", city);
			}
		}
		
		if (dto.getBranch() != null) {
			String branch = URLEncoder.encode(dto.getBranch(), QianBaoConstant.PEAK_ENCODE);
			params.put("branch", branch);
		}
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		params.put("returl", returl);
		// MD5(psid + ps_requestId +
		// +username+pwd+ needaudit
		// +maxuserfee+receiveuser +
		// receiveaccount +amount+bank+
		// reqtime+ requestip +seed)
		String sign = getSign(psid, psid, dto.getId(), dto.getUserId(), dto.getPayPwd(), audit, maxuserfee, dto.getTrueName(), dto.getBankCardNo(), amount, bankName, local[0], local[1], dto.getBranch(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("withdrawReq.action", params);
		PeakIdResultDto result = (PeakIdResultDto) XStreamUtil.fromXml(response, PeakIdResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.11接口 提现申请审核
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<IdResultDto> doWithDrawAudit(WithdrawAuditDto dto) {
	
		try {
			PeakIdResultDto resultDto = peakDoWithDrawAudit(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+ ps_requestId+ error_code +seed
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getPs_requestId(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Withdraw Audit Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			}
			return buildCommonIdResultDto(resultDto, "Draw");
		} catch (Exception e) {
			log.fatal("PEAK Withdraw Audit Error - " + dto, e);
			return new ResultUserDto<>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakIdResultDto peakDoWithDrawAudit(WithdrawAuditDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		params.put("psid", psid);
		params.put("ps_requestId", dto.getDrawId());
		String audit = dto.getAudit() ? "confirm" : "cancel";
		params.put("audit", audit);
		// MD5(psid + ps_requestId +audit +seed
		String sign = getSign(psid, psid, dto.getDrawId(), audit);
		params.put("signstr", sign);
		String response = sendRequest("withdrawAudit.action", params);
		PeakIdResultDto result = (PeakIdResultDto) XStreamUtil.fromXml(response, PeakIdResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	public static enum CaptchaType {
		BingMobile, FindPwd;
	}
	
	/**
	 * 2.14接口 服务端会把验证码保存 5 分钟，下发频率为 1 分钟 1 次，调用方也最好控制下下发频 率
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> queryMobileCaptcha(UserDto dto, CaptchaType type) {
	
		try {
			PeakMobileResultDto resultDto = peakQueryMobileCaptcha(dto, type);
			// MD5(psid+username+error_code+seed
			String signFrom = resultDto.getSignstr();
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getUsername(), resultDto.getStatus(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Captcha Querying Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, "Captcha");
		} catch (Exception e) {
			log.fatal("PEAK Captcha Querying Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	/**
	 * 1，服务端会把验证码保存 5 分钟，下发频率为 1 分钟 1 次，调用方也最好控制下下发频 率
	 * 
	 * @param dto
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private PeakMobileResultDto peakQueryMobileCaptcha(UserDto dto, CaptchaType type) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		String service = CaptchaType.BingMobile.equals(type) ? "1" : "2";// 必填，1-绑定手机，2-找回支付密码
		params.put("service", service);
		params.put("mobilenum", dto.getMobile());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+service+mobilen
		// um+reqtime+requestip+seed
		String sign = getSign(psid, psid, dto.getUserId(), service, dto.getMobile(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("sendVerifyCode.action", params);
		PeakMobileResultDto result = JsonUtil.fromJson(response, PeakMobileResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.15 绑定及修改绑定手机号码 调用此接口前，先调用下发验证码接口，获取验证码
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> doMobileBinding(UserDto dto) {
	
		try {
			PeakMobileResultDto resultDto = peakDoMobileBinding(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+error_code +seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getUsername(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Mobile Binding Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, "Bind");
		} catch (Exception e) {
			log.fatal("PEAK Mobile Binding Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakMobileResultDto peakDoMobileBinding(UserDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		params.put("verifyCode", dto.getCaptcha());
		params.put("mobilenum", dto.getMobile());
		params.put("reqtime", reqtime);
		params.put("pwd", dto.getPayPwd());
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+pwd+mobilenum+
		// verifyCode+reqtime+requestip+keyid+seed
		String sign = getSign(psid, psid, dto.getUserId(), dto.getPayPwd(), dto.getMobile(), dto.getCaptcha(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("bindMobile.action", params);
		PeakMobileResultDto result = JsonUtil.fromJson(response, PeakMobileResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.28 验证短信验证码
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> checkMobileCaptcha(UserDto dto, CaptchaType type) {
	
		try {
			PeakMobileResultDto resultDto = peakDocheckMobileCaptcha(dto, type);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+error_code +seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getUsername(), resultDto.getService(), resultDto.getMobilenum(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Mobile Checking Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, "Check");
		} catch (Exception e) {
			log.fatal("PEAK Mobile Checking Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakMobileResultDto peakDocheckMobileCaptcha(UserDto dto, CaptchaType type) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		String service = CaptchaType.BingMobile.equals(type) ? "1" : "2";// 必填，1-绑定手机，2-找回支付密码
		params.put("service", service);
		params.put("verifyCode", dto.getCaptcha());
		params.put("mobilenum", dto.getMobile());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+pwd+mobilenum+
		// verifyCode+reqtime+requestip+keyid+seed
		String sign = getSign(psid, psid, dto.getUserId(), service, dto.getMobile(), dto.getCaptcha(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("checkVerifyCode.action", params);
		PeakMobileResultDto result = JsonUtil.fromJson(response, PeakMobileResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.16 获取密保问题
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<SafeResultDto> querySafeQuestion(UserDto dto) {
	
		try {
			PeakSafeResultDto resultDto = peakQuerySafeQuestion(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+safeQuestion+error_code +seed
			// String safeQuestion =
			// URLDecoder.decode(resultDto.getSafeQuestion(),
			// QianBaoConstant.PEAK_ENCODE);
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getSafeQuestion(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Safe Question Querying Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<SafeResultDto>(UserErrorCode.QianbaoSign);
			}
			int errorCode = Integer.parseInt(resultDto.getError_code());
			if (errorCode == 0) {
				SafeResultDto safeRes = new SafeResultDto();
				safeRes.setUserId(dto.getUserId());
				safeRes.setSafeQuestion(resultDto.getSafeQuestion());
				return new ResultUserDto<SafeResultDto>(safeRes);
			} else {
				return buildCommonErrorResultDto(resultDto, null);
			}
		} catch (Exception e) {
			log.fatal("PEAK Safe Question Querying Error - " + dto, e);
			return new ResultUserDto<SafeResultDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakSafeResultDto peakQuerySafeQuestion(UserDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+reqtime+requestip+seed
		String sign = getSign(psid, psid, dto.getUserId(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("getSafeQuestion.action", params);
		PeakSafeResultDto result = JsonUtil.fromJson(response, PeakSafeResultDto.class);
		decodeErrorDesc(psid, result);
		if (StringUtils.isNoneBlank(result.getSafeQuestion())) {
			String safeQuestion = URLDecoder.decode(result.getSafeQuestion(), QianBaoConstant.PEAK_ENCODE);
			result.setSafeQuestion(safeQuestion);
		}
		return result;
	}
	
	/**
	 * 2.17 修改密保问题
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> doChangeSafeQuestion(ChangeSafeDto dto) {
	
		try {
			PeakSafeResultDto resultDto = peakDoChangeSafeQuestion(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+ status + error_code +seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Change Safe Question Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, "ChgSafe");
		} catch (Exception e) {
			log.fatal("PEAK Change Safe Question Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakSafeResultDto peakDoChangeSafeQuestion(ChangeSafeDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		params.put("safeAnswer", dto.getSafeAnswer());
		params.put("paypwd", dto.getPayPwd());
		String newSafe = getSafeQuestion(dto.getNewQuestion());
		String newSafequestion = URLEncoder.encode(newSafe, QianBaoConstant.PEAK_ENCODE);
		params.put("newSafequestion", newSafequestion);
		params.put("newSafeAnswer", dto.getNewAnswer());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+reqtime+requestip+seed
		String sign = getSign(psid, psid, dto.getUserId(), dto.getSafeAnswer(), dto.getPayPwd(), newSafe, dto.getNewAnswer(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("changeSafequestion.action", params);
		PeakSafeResultDto result = JsonUtil.fromJson(response, PeakSafeResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.18 通过密保问题找回支付密码
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> doFindPwdByQuesiton(FindPwdBySafeDto dto) {
	
		try {
			PeakCommonResultDto resultDto = peakDoFindPwdByQuesiton(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+ status + error_code +seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Find PWD by Question Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, null);
		} catch (Exception e) {
			log.fatal("PEAK Find PWD by Question Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakCommonResultDto peakDoFindPwdByQuesiton(FindPwdBySafeDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		String answer = URLEncoder.encode(dto.getSafeAnswer(), QianBaoConstant.PEAK_ENCODE);
		params.put("safeAnswer", answer);
		params.put("newPaypwd", dto.getNewPassowrd());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+safeAnswer+newPaypwd+ reqtime+ requestip+key
		String sign = getSign(psid, psid, dto.getUserId(), answer, dto.getNewPassowrd(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("findPaypwdBySafequestion.action", params);
		PeakCommonResultDto result = JsonUtil.fromJson(response, PeakCommonResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.19 通过绑定手机找回支付密码
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> doFindPwdByMobile(FindPwdByMobileDto dto) {
	
		try {
			PeakCommonResultDto resultDto = peakDoFindPwdByMobile(dto);
			String signFrom = resultDto.getSignstr();
			// MD5(psid+ status + error_code +seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Find PWD by Mobile Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, "FindByMobile");
		} catch (Exception e) {
			log.fatal("PEAK Find PWD by Mobile Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakCommonResultDto peakDoFindPwdByMobile(FindPwdByMobileDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		params.put("dynamicPwd", dto.getCaptcha());
		params.put("newPaypwd", dto.getNewPassowrd());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+dynamicPwd+newPaypwd+ reqtime +requestip +key
		String sign = getSign(psid, psid, dto.getUserId(), dto.getCaptcha(), dto.getNewPassowrd(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("findPaypwdByBindMobile.action", params);
		PeakCommonResultDto result = JsonUtil.fromJson(response, PeakCommonResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.27收支明细接口
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<TransResultDto> queryDetails(TransDto dto) {
	
		try {
			PeakDetailsResultDto resultDto = peakDoQueryDetails(dto);
			String signFrom = resultDto.getSignstr();
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Detail Querying Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			}
			TransResultDto result = new TransResultDto();
			long total = Long.parseLong(StringUtils.isBlank(resultDto.getTotalnum()) ? "0" : resultDto.getTotalnum());
			PageUtil page = new PageUtil(dto.getPageSize().intValue(), (int) total, dto.getPageNo());
			long totalPage = page.getPageCount();
			// long totalPage = total / dto.getPageSize() + total %
			// dto.getPageSize();
			// long pageNo = dto.getPageNo() * dto.getPageSize() > total ?
			// totalPage : dto.getPageNo();
			long pageNo = dto.getPageNo();
			result.setTotal(total);
			result.setPageNo(pageNo);
			result.setPageSize(dto.getPageSize());
			result.setTotalPage(totalPage);
			result.setUserId(dto.getUserId());
			result.setStartTime(dto.getStartTime());
			result.setEndTime(dto.getEndTime());
			Long charge = MoneyUtil.yuan2fen(resultDto.getCHARGE());
			charge = charge == null ? 0L : charge;
			result.setChargeAmount(charge);
			Long pay = MoneyUtil.yuan2fen(resultDto.getPAY());
			pay = pay == null ? 0L : pay;
			result.setPayAmount(pay);
			Long refund = MoneyUtil.yuan2fen(resultDto.getDRAWBACK());
			refund = refund == null ? 0L : refund;
			result.setRefundAmount(refund);
			Long bonus = MoneyUtil.yuan2fen(resultDto.getBONUS_LOTTERY());
			bonus = bonus == null ? 0L : bonus;
			result.setBonusAmount(bonus);
			Long draw = MoneyUtil.yuan2fen(resultDto.getDRAWBACK());
			draw = draw == null ? 0L : draw;
			result.setWithDrawAmount(draw);
			List<TransDetailResultDto> details = new ArrayList<>();
			result.setTransList(details);
			for (PeakDetailDto peakDto : resultDto.getList()) {
				TransDetailResultDto detail = new TransDetailResultDto();
				// 这里摄入彩票自己的流水号 modified by 黄涛 20140417
				detail.setTransactionId(peakDto.getRequestId());
				// "createdOn":"2014-03-12 18:44:49","amt":"2.00"
				Timestamp createTime = null;
				try {
					createTime = new Timestamp(DateUtil.formatDate(peakDto.getCreatedOn()).getTime());
				} catch (IllegalArgumentException e) {
					
				}
				detail.setCreateTime(createTime);
				// 这个字段1.5.6的文档里去掉了
				detail.setBalance(MoneyUtil.yuan2fen(peakDto.getAccountBalance()));
				detail.setAmount(MoneyUtil.yuan2fen(peakDto.getAmt()));
				String t = getTransType(peakDto.getOperType());
				Integer type = StringUtils.isBlank(t) ? null : Integer.parseInt(t);
				// TODO 状态不统一 资金明细有问题
				String statusStr = URLDecoder.decode(peakDto.getStatus(), QianBaoConstant.PEAK_ENCODE);
				Integer status = null;
				if (dto.getType() != null) {
					status = StringUtils.isBlank(statusStr) ? null : Integer.parseInt(peakDto.getStatus());
				}
				// Integer.parseInt(getTransStatus(peakDto.getStatus()));
				if (peakDto.getChannel() != null) {
					String bank = URLDecoder.decode(peakDto.getChannel(), QianBaoConstant.PEAK_ENCODE);
					if (bank.contains("中行")) {
						bank = "中国银行";
					} else if (bank.contains("工行") || bank.contains("工商")) {
						bank = "中国工商银行";
					} else if (bank.contains("招行") || bank.contains("工商")) {
						bank = "中国招商银行";
					} else if (bank.contains("建行")) {
						bank = "中国建设银行";
					} else if (bank.contains("农行")) {
						bank = "中国农业银行";
					} else if (bank.contains("深发")) {
						bank = "深圳发展银行";
					} else if (bank.contains("浦发")) {
						bank = "上海浦东发展银行";
					} else if (bank.contains("广发")) {
						bank = "广东发展银行";
					} else if (bank.contains("政储")) {
						bank = "中国邮政储蓄银行";
					} else if (bank.contains("光大")) {
						bank = "中国光大银行";
					} else if (bank.contains("支付宝")) {
						bank = "支付宝账户";
					} else if (bank.contains("快钱")) {
						bank = "快钱账户";
					} else if (bank.contains("财付通") || bank.contains("账户")) {
						bank = "财付通账户";
					}
					detail.setChannel(bank);// 直接用中文
				}
				if (type != null && status != null) {
					detail.setFee(null);
					if (type == TransType.Recharge.getType()) {
						if (status == 0) {
							detail.setStatusDesc("成功");
						} else if (status == 1) {
							detail.setStatusDesc("失败");
						} else if (status == 2) {
							detail.setStatusDesc("充值中");
						} else {
							detail.setStatusDesc("关闭");
						}
					} else if (type == TransType.WithDraw.getType()) {
						if (status == 0) {
							detail.setStatusDesc("审核中");
						} else if (status == 1) {
							detail.setStatusDesc("处理中");
						} else if (status == 2) {
							detail.setStatusDesc("成功");
							// 加入手续费处理 by haojiaqi 2014.03.26
							detail.setFee(MoneyUtil.yuan2fen(peakDto.getFee()));
						} else {
							detail.setStatusDesc("失败");
						}
					} else if (type == TransType.Consume.getType()) {
						if (status == 0 || status == 2) {
							detail.setStatusDesc("等待支付");
						} else if (status == 1) {
							detail.setStatusDesc("已付款");
						} else if (status == 4) {
							detail.setStatusDesc("已关闭");
						} else {
							detail.setStatusDesc("失败");
						}
					} else if (type == TransType.Refund.getType()) {
						if (status == 0 || status == 1) {
							detail.setStatusDesc("处理中");
						} else if (status == 2) {
							detail.setStatusDesc("已退款");
						} else {
							detail.setStatusDesc("失败");
						}
					} else if (type == TransType.Award.getType()) {
						if (status == 2 || status == 3 || status == 4 || status == 5) {
							detail.setStatusDesc("处理中");
						} else if (status == 0) {
							detail.setStatusDesc("已派奖");
						} else {
							detail.setStatusDesc("失败");
						}
					}
				}
				// TODO
				if (status == null) {
					detail.setStatusDesc(statusStr);
				}
				detail.setType(type);
				detail.setTypeDesc(getTransTypeDesc(type));
				// 设置资金方向 by haojiaqi 2014.03.26
				detail.setFundsDirection(this.buildFundsDirection(type));
				// detail.setStatusDesc(getTransStatusDesc(status));
				detail.setStatus(status);
				details.add(detail);
				if (StringUtils.isNotBlank(peakDto.getConfirmTime())) {
					Timestamp confirmTime = null;
					try {
						confirmTime = new Timestamp(DateUtil.formatDate(peakDto.getConfirmTime()).getTime());
					} catch (IllegalArgumentException e) {
						
					}
					detail.setConfirmTime(confirmTime);
				}
			}
			int errorCode = Integer.parseInt(resultDto.getError_code());
			if (errorCode == 0) {
				return new ResultUserDto<>(result);
			} else {
				return buildCommonErrorResultDto(resultDto, null);
			}
		} catch (Exception e) {
			log.fatal("PEAK Detail Querying Error - " + dto, e);
			return new ResultUserDto<>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private String buildFundsDirection(Integer type) {
	
		// 资金方向：收入（1充值 4退款收入 6彩票返奖）、支出(2提现 3消费 5提现手续费)
		if (type == 1 || type == 4 || type == 6) {
			return "收入";
		} else if (type == 2 || type == 3 || type == 5) {
			return "支出";
		} else {
			return null;
		}
	}
	
	private PeakDetailsResultDto peakDoQueryDetails(TransDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		params.put("currentPage", String.valueOf(dto.getPageNo()));
		params.put("currentRecord", String.valueOf(dto.getPageSize()));
		// 黄涛 14:18:21
		// 是>=startTime and <= endTime还是什么
		// 搜狗-李少松4446 14:18:43
		// 包含
		String startTime = DateUtil.formatDate(dto.getStartTime(), Format.YYYYMMDDHHMMSS);
		String endTime = DateUtil.formatDate(dto.getEndTime(), Format.YYYYMMDDHHMMSS);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		String transType = dto.getType() != null ? getTransType(dto.getType()) : "";
		transType = StringUtils.isBlank(transType) ? "" : transType;
		params.put("transType", transType);
		String sign = getSign(psid, psid, dto.getUserId(), startTime, endTime, transType);
		params.put("signstr", sign);
		String response = sendRequest("qryUserTrans.action", params);
		PeakDetailsResultDto result = JsonUtil.fromJson(response, PeakDetailsResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	/**
	 * 2.26 修改支付密码
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<CommonDto> doChangePayPwd(ChangePwdDto dto) {
	
		try {
			PeakCommonResultDto resultDto = peakDoChangePayPwd(dto);
			
			String signFrom = resultDto.getSignstr();
			// MD5(psid + error_code +seed)
			String signTo = getSign(resultDto.getPsid(), resultDto.getPsid(), resultDto.getError_code());
			if (!signFrom.equals(signTo)) {
				log.fatal("PEAK Change Pay PWD Sign Error - " + resultDto + " - " + signTo);
				return new ResultUserDto<>(UserErrorCode.QianbaoSign);
			}
			return buildCommonResultDto(resultDto, "ChgPwd");
		} catch (Exception e) {
			log.fatal("PEAK Change Pay PWD Error - " + dto, e);
			return new ResultUserDto<CommonDto>(UserErrorCode.QianbaoSystem);
		}
	}
	
	private PeakCommonResultDto peakDoChangePayPwd(ChangePwdDto dto) throws Exception {
	
		Map<String,String> params = new HashMap<>();
		String psid = getCommonPsid();
		String reqtime = DateUtil.formatDate(DateUtil.getCurrentDate(), Format.YYYYMMDDHHMMSS);
		params.put("psid", psid);
		params.put("username", dto.getUserId());
		params.put("oldpwd", dto.getPayPwd());
		params.put("newpwd", dto.getNewPayPwd());
		params.put("reqtime", reqtime);
		String requestip = getIp(dto.getUserIp());
		params.put("requestip", requestip);
		// MD5(psid+username+dynamicPwd+newPaypwd+ reqtime +requestip +key
		String sign = getSign(psid, psid, dto.getUserId(), dto.getPayPwd(), dto.getNewPayPwd(), reqtime, requestip);
		params.put("signstr", sign);
		String response = sendRequest("changePaypwd.action", params);
		PeakCommonResultDto result = JsonUtil.fromJson(response, PeakCommonResultDto.class);
		decodeErrorDesc(psid, result);
		return result;
	}
	
	private ResultUserDto<CommonDto> buildCommonResultDto(PeakCommonResultDto dto, String errorType) throws Exception {
	
		int errorCode = Integer.parseInt(dto.getError_code());
		if (errorCode == 0) {
			return new ResultUserDto<>(new CommonDto());
		} else {
			return buildCommonErrorResultDto(dto, errorType);
		}
	}
	
	private ResultUserDto<IdResultDto> buildCommonIdResultDto(PeakIdResultDto dto, String errorType) throws Exception {
	
		int errorCode = Integer.parseInt(dto.getError_code());
		if (errorCode == 0) {
			IdResultDto drawDto = new IdResultDto();
			drawDto.setId(dto.getPs_requestId());
			return new ResultUserDto<IdResultDto>(drawDto);
		} else {
			return buildCommonErrorResultDto(dto, errorType);
		}
	}
	
	private <T> ResultUserDto<T> buildCommonErrorResultDto(PeakCommonResultDto dto, String errorType) throws Exception {
	
		String desc = null;
		if (StringUtils.isNotBlank(dto.getError_desc())) {
			desc = URLDecoder.decode(dto.getError_desc(), QianBaoConstant.PEAK_ENCODE);
		}
		dto.setError_desc(desc);
		int errorCode = Integer.parseInt(dto.getError_code());
		UserErrorCode error = UserErrorCode.getQianBaoCode(errorCode, errorType);
		if (error == null) {
			return new ResultUserDto<>(errorCode, dto.getError_desc());
		} else {
			return new ResultUserDto<>(error.getCode(), error.getDesc());
		}
	}
	
	private String getSafeQuestion(Integer question) {
	
		return Dictionary.getValue(Dictionary.Type.SafeQuesiton, String.valueOf(question));
	}
	
	private String getChargePayGateCode(String bankId) {
	
		return Dictionary.getValue(Dictionary.Type.ChargeBankQB, bankId);
	}
	
	private String getIdCardType(Integer type) {
	
		return Dictionary.getValue(Dictionary.Type.IdCardTypeQB, String.valueOf(type));
	}
	
	private String getTransType(Integer type) {
	
		return Dictionary.getValue(Dictionary.Type.TransTypeQB, String.valueOf(type));
	}
	
	private String getTransType(String type) {
	
		return Dictionary.getKey(Dictionary.Type.TransTypeQB, type);
	}
	
	private String getTransTypeDesc(Integer code) {
	
		return Dictionary.getValue(Dictionary.Type.TransType, "" + code);
	}
	
	private String getDrawBankName(String code) {
	
		String value = Dictionary.getValue(Dictionary.Type.DrawBank, code);
		if (StringUtils.isBlank(value)) {
			value = Dictionary.getValue(Dictionary.Type.DrawBankExtra, code);
		}
		return value;
	}
	
	private String[] getProvinceCity(String prov, String cit) {
	
		String[] values = new String[2];
		values[0] = Dictionary.getValue(Dictionary.Type.Province, prov);
		values[1] = Dictionary.getValue(Dictionary.Type.City, cit);
		return values;
	}
	
	public String getSign(String psid, String... str) {
	
		String seed = getSeed(psid);
		String sign = Joiner.on("").skipNulls().join(str) + seed;
		String md5 = MD5Util.getMD5(sign, QianBaoConstant.PEAK_ENCODE);
		if (log.isDebugEnabled()) {
			log.debug("签名原串 :" + sign);
		}
		
		return md5;
	}
	
	private String sendRequest(String command, Map<String,String> paramsMap) {
	
		int onoff = SystemConfigs.getIntValue("PEAK_LOG_SWITCH", 1);
		if (onoff == 1 && !command.equals("getAccount.action")) {
			log.info("[peak][command][" + command + "]" + JsonUtil.toJson(paramsMap));
		}
		// 只是给充值查询token用，查询token线上用单独的地址
		if (!command.startsWith("http")) {
			command = getQueryUrl(command);
		}
		String response = HttpPoolUtil.sendPostHttpRequestStatic(command, paramsMap, null, QianBaoConstant.CAIPIAO_ENCODE, getConnectTimeout(), getSocketTimeout(), QianBaoConstant.PEAK_ENCODE);
		if (onoff == 1) {
			log.info("[peak][response][" + command + "]" + response);
		}
		return response;
	}
	
	private static int getSocketTimeout() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_TIMEOUT_SOCK, QianBaoConstant.PEAK_TIMEOUT_SOCK_DEFAULT);
	}
	
	private static int getConnectTimeout() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_TIMEOUT_CONN, QianBaoConstant.PEAK_TIMEOUT_CONN_DEFAULT);
	}
	
	private static String getPayTimeOut(Timestamp deadline) {
	
		long days = DateUtil.diff(DateUtil.getCurrentDate(), deadline, TimeUnit.DAYS) + 1;
		if (days < 2) {
			days = 2;
		}
		return days + "d";
	}
	
	private static String getRefundlimit() {
	
		return SystemConfigs.get(QianBaoConstant.PEAK_CP_REFUND_LIMIT, "");// 默认90天
	}
	
	private static String getIp(String ip) {
	
		return StringUtils.isBlank(ip) ? "" : ip;
	}
	
	private String getCommonPsid() {
	
		AccountVo vo = qianBaoAccountService.getSogouMiddle();
		return vo.getAccountId();
		// return SystemConfigs.get(QianBaoConstant.PEAK_CP_ACCOUNT_MIDDLE,
		// "2252");
	}
	
	private String getPromoPsid() {
	
		AccountVo vo = qianBaoAccountService.getSogouPromo();
		return vo.getAccountId();
	}
	
	private String getSeed(String psid) {
	
		// System.out.println(psid);
		AccountVo vo = qianBaoAccountService.getByAccountId(psid).get(0);
		return vo.getKey();
		// return SystemConfigs.get(QianBaoConstant.PEAK_SEED,
		// "!qaz@wsx#Edc$rfv%tgb");
	}
	
	private String getQueryUrl(String suffix) {
	
		return formatUrl(getQianBaoHost(), suffix);
	}
	
	private String getQianBaoHost() {
	
		return SystemConfigs.get(QianBaoConstant.PEAK_HOST, QianBaoConstant.PEAK_HOST_DEFAULT);
		// return "http://paytest.sohu.com:8080/payment/internal/";
	}
	
	private static String getNotifyHost() {
	
		return SystemConfigs.get(QianBaoConstant.PEAK_NOTIFY_HOST, QianBaoConstant.PEAK_NOTIFY_HOST_DEFAULT);
	}
	
	private static String getBalancePayReturnUrl() {
	
		return formatUrl(getNotifyHost(), QianBaoConstant.PEAK_RETURL_PAY + ".html");
	}
	
	public static String getRefundReturnUrl() {
	
		return formatUrl(getNotifyHost(), QianBaoConstant.PEAK_RETURL_REFUND + ".html");
	}
	
	public static String getWithDrawReturnUrl() {
	
		return formatUrl(getNotifyHost(), QianBaoConstant.PEAK_RETURL_WITHDRAW + ".html");
	}
	
	public static String getRechargeReturnUrl() {
	
		return formatUrl(getNotifyHost(), QianBaoConstant.PEAK_RETURL_CHARGE + ".html");
	}
	
	public static String getRechargePageReturnUrl() {
	
		return formatUrl(EnvironmentBean.getDomainUrl(), SystemConfigs.get(QianBaoConstant.PEAK_CP_CHARGE_RETURN_PAGE, QianBaoConstant.PEAK_CP_CHARGE_RETURN_PAGE_DEFAULT));
	}
	
	private static String formatUrl(String prefix, String suffix) {
	
		if (prefix.endsWith("/") && suffix.startsWith("/")) {
			return prefix + suffix.substring(1);
		}
		if (prefix.endsWith("/") || suffix.startsWith("/")) {
			return prefix + suffix;
		} else {
			return prefix + "/" + suffix;
		}
	}
	
	private static String getRechargeTokenHost() {
	
		return SystemConfigs.get(QianBaoConstant.PEAK_CP_TOKEN_HOST, QianBaoConstant.PEAK_CP_TOKEN_HOST_DEFAULT);
	}
	
	private static String getRechargeHost() {
	
		return SystemConfigs.get(QianBaoConstant.PEAK_CP_CHARGE_HOST, QianBaoConstant.PEAK_CP_CHARGE_HOST_DEFAULT);
		// return "http://paytest.sohu.com:8080/payment/internal/charge.action";
	}
}
