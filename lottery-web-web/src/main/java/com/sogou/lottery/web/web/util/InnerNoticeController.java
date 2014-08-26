package com.sogou.lottery.web.web.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/notice")
public class InnerNoticeController extends BaseController {
	
	@RequestMapping("/list")
	public String introduction() {
	
		return "notice/list";
	}
	
	@RequestMapping("/list-{list}")
	public String list(@PathVariable("list")
	String list) {
	
		return "notice/" + list;
	}
	
}
