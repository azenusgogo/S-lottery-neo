package com.sogou.lottery.web.web.captcha;

import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/captcha")
public class CaptchaController extends BaseController {
	
	public final static String CAPTCHA_BASE_KEY = "captcha_";
	private final static String CAPTCHA_IMAGE_GEN = "/{captchaKey}/gen";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(CAPTCHA_IMAGE_GEN)
	public void generateImageCaptcha(@PathVariable("captchaKey")
	String captchaKey, HttpSession session, OutputStream os) {
	
		try {
			String[] captchaStr = userService.generateImageCaptcha(os);
			if (captchaStr == null) {
				return;
			}
			if (log.isDebugEnabled()) {
				log.debug("sessionid:" + session.getId() + ",captchaKey:" + captchaKey + ",captchaStr:" + captchaStr[1]);
			}
			session.setAttribute(getImageCaptchaKey(captchaKey), captchaStr[1]);
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	public static String getImageCaptchaKey(String key) {
	
		return CAPTCHA_BASE_KEY + key;
	}
}
