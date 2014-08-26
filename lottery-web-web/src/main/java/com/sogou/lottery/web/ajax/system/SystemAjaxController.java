package com.sogou.lottery.web.ajax.system;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.vo.user.UserChannelVo;
import com.sogou.lottery.dao.user.UserChannelDao;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.service.user.service.UserCacheService;
import com.sogou.lottery.web.web.BaseController;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 系统Ajax
 * 
 * @author lvzhenyu
 */
@Controller
@RequestMapping("/ajax/system")
public class SystemAjaxController extends BaseController {
	
	@Autowired
	private UserChannelDao userChannelDao;
	@Autowired
	private UserCacheService userCacheService;
	
	/**
	 * 获得系统时间 ,用于浏览器端与服务器端对时<br>
	 * 默认返回秒，如果有m=**的参数，则返回毫秒
	 * 
	 * @return
	 */
	@RequestMapping("/getTime/{type}")
	@ResponseBody
	public String getTime(@PathVariable("type")
	String type) {
	
		if (!type.equals("ms") && !type.equals("s")) {
			return null;
		}
		if (type.equals("ms")) {
			return String.valueOf(System.currentTimeMillis());
		} else {
			return String.valueOf(System.currentTimeMillis() / 1000);
		}
	}
	
	/**
	 * @return
	 */
	@RequestMapping("/stat-channel")
	@ResponseBody
	public String statChannel(HttpServletRequest request) {
	
		String COOKIE_CHANNEL = "fr";
		String CHANNEL_NONE = "none";
		String userId = Authentic.getUserId(request);
		if (StringUtils.isBlank(userId)) {
			return "not login";
		}
		String channel = null;
		// 取cookie里fr的值
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_CHANNEL.equals(cookie.getName())) {
					channel = cookie.getValue();
					break;
				}
			}
		}
		// 如果cache没有，则入库
		UserChannelVo vo = userCacheService.queryUserChannel(userId, false);
		if (vo == null) {
			vo = new UserChannelVo();
			vo.setUserId(userId);
			vo.setIp(LotteryUtil.getLastIp(request));
			if (StringUtils.isBlank(channel)) {
				channel = CHANNEL_NONE;
			}
			vo.setChannel(channel);
			int len = SystemConfigs.getIntValue("USER_CHANNEL_LENGTH", 50);
			if (StringUtils.isNotBlank(channel) && channel.length() < len || StringUtils.isBlank(channel)) {
				userChannelDao.insert(vo);
				userCacheService.queryUserChannel(userId, true);
			} else if (StringUtils.isNotBlank(channel) && channel.length() >= len) {
				log.error("[LoginChannelInterceptor][illegal channel length] " + userId + "|" + channel);
			}
		}
		return "success";
	}
	
}
