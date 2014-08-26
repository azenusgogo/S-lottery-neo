package com.sogou.lottery.web.web.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/help")
public class HelpCenterController extends BaseController {
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@RequestMapping("/index")
	public String index() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_help_center_index", "帮助中心-搜狗彩票"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_help_center_index", ""));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_help_center_index", ""));
		return "help/index";
	}
	
	@RequestMapping("/i-{gameId}-{page}")
	public String introduction(@PathVariable("gameId")
	String gameId, @PathVariable("page")
	String page) {
	
		if (GameType.getGameType(gameId) == GameType.GAME_NULL) {
			return WebConstant.FAIL_FTL;
		}
		if (!page.equals("rule") && !page.equals("play") && !page.equals("award")) {
			return WebConstant.FAIL_FTL;
		}
		return "help/introduction/" + gameId + "-" + page;
	}
	
	@RequestMapping("/f-{function}")
	public String function(@PathVariable("function")
	String function) {
	
		return "help/function/" + function;
	}
	
	@RequestMapping("/p-{problem}")
	public String problem(@PathVariable("problem")
	String problem) {
	
		return "help/problem/" + problem;
	}
	
	@RequestMapping("/about")
	public String about() {
	
		return "help/about";
	}
	
	@RequestMapping("/contact")
	public String contact() {
	
		return "help/contact";
	}
}
