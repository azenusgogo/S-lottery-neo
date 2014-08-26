package com.sogou.lottery.web.service.qianbao.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakRefundResultDto extends PeakResultDto {
	
	@XStreamAlias("orders")
	private List<Order> orders;
	
	@XStreamAlias("order")
	public static class Order {
		
		private String ps_requestId;
		private String error_code;
		private String error_desc;
		
		public boolean isSuccess() {
		
			return "0".equals(StringUtils.trim(error_code));
		}
		
		public String getPs_requestId() {
		
			return ps_requestId;
		}
		
		public void setPs_requestId(String ps_requestId) {
		
			this.ps_requestId = ps_requestId;
		}
		
		public String getError_code() {
		
			return error_code;
		}
		
		public void setError_code(String error_code) {
		
			this.error_code = error_code;
		}
		
		public String getError_desc() {
		
			return error_desc;
		}
		
		public void setError_desc(String error_desc) {
		
			this.error_desc = error_desc;
		}
	}
	
	public List<Order> getOrders() {
	
		return orders;
	}
	
	public void setOrders(List<Order> orders) {
	
		this.orders = orders;
	}
	
}
