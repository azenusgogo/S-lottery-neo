package com.sogou.lottery.web.service.qianbao.dto;

import java.util.ArrayList;
import java.util.List;

public class PeakDetailsResultDto extends PeakCommonResultDto {
	
	private List<PeakDetailDto> list = new ArrayList<>();
	private String CHARGE;
	private String DEPOSIT;
	private String PAY;
	private String DRAWBACK;
	private String BONUS_LOTTERY;
	private String totalnum;
	
	public static class PeakDetailDto {
		
		private String transId;
		private String createdOn;
		private String transType;
		private String amt;
        private String fee;
		private String accountBalance;
		private String channel;// 这里返回银行中文名称，非代码
		private String status;
		private String requestId;
		private String confirmTime;
		
		public String getTransId() {
		
			return transId;
		}
		
		public void setTransId(String transId) {
		
			this.transId = transId;
		}
		
		public String getCreatedOn() {
		
			return createdOn;
		}
		
		public void setCreatedOn(String createdOn) {
		
			this.createdOn = createdOn;
		}
		
		public String getOperType() {
		
			return transType;
		}
		
		public void setOperType(String operType) {
		
			this.transType = operType;
		}
		
		public String getAmt() {
		
			return amt;
		}
		
		public void setAmt(String amt) {
		
			this.amt = amt;
		}
		
		public String getAccountBalance() {
		
			return accountBalance;
		}
		
		public void setAccountBalance(String accountBalance) {
		
			this.accountBalance = accountBalance;
		}
		
		public String getRequestId() {
		
			return requestId;
		}
		
		public void setRequestId(String requestId) {
		
			this.requestId = requestId;
		}
		
		public String getChannel() {
		
			return channel;
		}
		
		public void setChannel(String channel) {
		
			this.channel = channel;
		}
		
		public String getStatus() {
		
			return status;
		}
		
		public void setStatus(String status) {
		
			this.status = status;
		}
		
		public String getTransType() {
		
			return transType;
		}
		
		public void setTransType(String transType) {
		
			this.transType = transType;
		}
		
		public String getConfirmTime() {
		
			return confirmTime;
		}
		
		public void setConfirmTime(String confirmTime) {
		
			this.confirmTime = confirmTime;
		}

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
    }
	
	public List<PeakDetailDto> getList() {
	
		return list;
	}
	
	public void setList(List<PeakDetailDto> list) {
	
		this.list = list;
	}
	
	public String getCHARGE() {
	
		return CHARGE;
	}
	
	public void setCHARGE(String cHARGE) {
	
		CHARGE = cHARGE;
	}
	
	public String getDEPOSIT() {
	
		return DEPOSIT;
	}
	
	public void setDEPOSIT(String dEPOSIT) {
	
		DEPOSIT = dEPOSIT;
	}
	
	public String getPAY() {
	
		return PAY;
	}
	
	public void setPAY(String pAY) {
	
		PAY = pAY;
	}
	
	public String getDRAWBACK() {
	
		return DRAWBACK;
	}
	
	public void setDRAWBACK(String dRAWBACK) {
	
		DRAWBACK = dRAWBACK;
	}
	
	public String getBONUS_LOTTERY() {
	
		return BONUS_LOTTERY;
	}
	
	public void setBONUS_LOTTERY(String bONUS_LOTTERY) {
	
		BONUS_LOTTERY = bONUS_LOTTERY;
	}
	
	public String getTotalnum() {
	
		return totalnum;
	}
	
	public void setTotalnum(String totalnum) {
	
		this.totalnum = totalnum;
	}
}
