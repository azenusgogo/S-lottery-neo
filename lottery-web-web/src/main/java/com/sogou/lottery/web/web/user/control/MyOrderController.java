package com.sogou.lottery.web.web.user.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class MyOrderController {
	
	protected final static String CTL_ORDER_VIEW = "/view";
	protected final static String FTL_ORDER_VIEW = "orders/view";
	
	@RequestMapping(CTL_ORDER_VIEW)
	public String views() {
	
		// TODO
		return FTL_ORDER_VIEW;
	}
}
