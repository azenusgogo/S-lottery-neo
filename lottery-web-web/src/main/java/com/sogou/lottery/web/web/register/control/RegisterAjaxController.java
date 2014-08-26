package com.sogou.lottery.web.web.register.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.passport.PassportErrorCode;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;
import com.sogou.lottery.web.web.captcha.CaptchaController;
import com.sogou.lottery.web.web.interceptor.WebRegisterInterceptor;

@Controller
@RequestMapping("/ajax/register")
public class RegisterAjaxController extends BaseController {
	
	protected final static String CTL_REGISTER = "/do";
	protected final static String CTL_REGISTER_CHECK = "/check";
	protected final static String CTL_REGISTER_PRE = "/pre";
	protected final static String CAPTCHA_REGISTER = "register";
	
	protected final static String COOKIE_UUID = "cpuid";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(CTL_REGISTER_PRE)
	public void pre(@CookieValue(value = COOKIE_UUID, required = false)
	String cpuid, HttpServletRequest request, HttpServletResponse response) {
	
		try {
			if (StringUtils.isBlank(cpuid)) {
				WebRegisterInterceptor.generateUID(response);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	@RequestMapping(CTL_REGISTER)
	@ResponseBody
	public ResultUserDto<CommonDto> register(UserDto dto, BindingResult errors, HttpServletRequest request, HttpSession session) {
	
		try {
			new UserValidator().validate(dto, errors);
			if (errors.hasErrors()) {
				printError(errors);
				return new ResultUserDto<>(UserErrorCode.CommonArgument.getCode(), getFirstError(errors));
			}
			if (!checkImageCaptcha(CAPTCHA_REGISTER, dto.getCaptcha(), session)) {
				return new ResultUserDto<>(UserErrorCode.ImageCaptcha);
			}
			ResultUserDto<CommonDto> res = userService.registerUser(dto);
			if (res.isSucces()) {
				request.setAttribute(WebRegisterInterceptor.REGISTER, Boolean.TRUE);
			}
			return res;
		} catch (Exception e) {
			log.error(e, e);
			return new ResultUserDto<>(UserErrorCode.CommonError);
		}
	}
	
	@RequestMapping(CTL_REGISTER_CHECK)
	@ResponseBody
	public ResultUserDto<CommonDto> check(UserDto dto, BindingResult errors) {
	
		try {
			new UserValidator(false).validate(dto, errors);
			if (errors.hasErrors()) {
				printError(errors);
				return new ResultUserDto<>(UserErrorCode.CommonArgument.getCode(), getFirstError(errors));
			}
			ResultUserDto<CommonDto> res = userService.checkUserExist(dto);
			if (res.getRetcode() == PassportErrorCode.RegRegisted.getCode()) {
				return new ResultUserDto<>(UserErrorCode.CommonOK);
			} else {
				return res;
			}
		} catch (Exception e) {
			log.error(e, e);
			return new ResultUserDto<>(UserErrorCode.CommonError);
		}
	}
	
	protected boolean checkImageCaptcha(String key, String captchaStr, HttpSession session) {
	
		String correctCaptchaStr = (String) session.getAttribute(CaptchaController.getImageCaptchaKey(key));
		if (log.isDebugEnabled()) {
			log.debug("sessionid:" + session.getId() + ",captchaStr:" + captchaStr + ",correctCaptchaStr:" + correctCaptchaStr);
		}
		return userService.checkImageCaptcha(captchaStr, correctCaptchaStr);
	}
	
	public class UserValidator implements Validator {
		
		private boolean strict = true;
		private final String regex = "[a-z]([0-9a-zA-Z\\.\\-_]{3,15})";
		public final Pattern pattern = Pattern.compile(regex);
		private final String regexKey = "^[\\s\\S]{6,16}$";
		public final Pattern patternKey = Pattern.compile(regexKey);
		
		public UserValidator() {
		
		}
		
		public UserValidator(boolean strict) {
		
			this.strict = strict;
		}
		
		@Override
		public boolean supports(Class<?> clazz) {
		
			return UserDto.class.equals(clazz);
		}
		
		@Override
		public void validate(Object obj, Errors errors) {
		
			try {
				if (obj == null) {
					errors.rejectValue("userId", null, "参数不能为空");
					return;
				}
				if (obj instanceof UserDto) {
					UserDto dto = (UserDto) obj;
					dto.setUserIp(RegisterAjaxController.this.getClientIP());
					/*
					 * http://updwiki.sogou-inc.com/pages/viewpage.action?pageId
					 * = 6461099 4-16位的数字、字母、点、减号或下划线组成，必须以小写字母开头，不区分大小写
					 * 不能包含以下内容：admin、master、Abuse、contact、 help、 info、 jobs、
					 * owner、 sales、 staff、sales、support、www
					 * 为小纸条加的临时规则：如果登录名保护.，将.换成_的登录名如果存在，则提示用户“已被占用”
					 * 不允许sohu域账号注册 密码必须为字母、数字、字符且长度为6~16位
					 */
					if (StringUtils.isBlank(dto.getUserId())) {
						errors.rejectValue("userId", null, "用户名不能为空");
						return;
					}
					Matcher m = pattern.matcher(dto.getUserId());
					if (!m.find()) {
						errors.rejectValue("userId", null, "用户名格式有误");
					}
					dto.setUserId(dto.getUserId() + "@sogou.com");
					if (strict) {
						if (StringUtils.isBlank(dto.getUserIp())) {
							errors.rejectValue("userIp", null, "用户IP地址不能为空");
						}
						if (StringUtils.isBlank(dto.getPwd())) {
							errors.rejectValue("pwd", null, "登录密码不能为空");
							return;
						}
						if (StringUtils.isBlank(dto.getCaptcha())) {
							errors.rejectValue("captcha", null, "验证码不能为空");
						}
						Matcher mKey = patternKey.matcher(dto.getPwd());
						if (!mKey.find()) {
							errors.rejectValue("pwd", null, "登录密码格式有误");
						}
						String[] illegals = new String[] { "admin", "master", "abuse", "contact", "help", "info", "jobs", "owner", "sales", "staff", "support", "www" };
						for (String illegal : illegals) {
							if (StringUtils.containsIgnoreCase(dto.getPwd(), illegal)) {
								errors.rejectValue("pwd", null, "登录密码格式有误");
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				errors.rejectValue("userId", null, "注册信息参数错误");
			}
		}
	}
}
