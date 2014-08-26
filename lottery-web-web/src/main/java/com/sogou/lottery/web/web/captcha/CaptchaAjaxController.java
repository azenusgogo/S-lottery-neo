package com.sogou.lottery.web.web.captcha;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/ajax/captcha")
public class CaptchaAjaxController extends BaseController {
	
	private final static String CAPTCHA_IMAGE_CHECK = "/check/{captchaKey}";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(CAPTCHA_IMAGE_CHECK)
	@ResponseBody
	public ResultUserDto<Boolean> checkImageCaptcha(@PathVariable("captchaKey")
	String captchaKey, @RequestParam("captcha")
	String captchaStr, HttpSession session) {
	
		String correctCaptchaStr = (String) session.getAttribute(CaptchaController.getImageCaptchaKey(captchaKey));
		if (log.isDebugEnabled()) {
			log.debug("sessionid:" + session.getId() + ",captchaKey:" + captchaKey + ",captchaStr:" + captchaStr + ",correctCaptchaStr:" + correctCaptchaStr);
		}
		if (userService.checkImageCaptcha(captchaStr, correctCaptchaStr)) {
			ResultUserDto<Boolean> resultDto = new ResultUserDto<>(UserErrorCode.ImageCaptcha);
			return resultDto;
		}
		return new ResultUserDto<>(UserErrorCode.CommonOK);
	}
	
}
