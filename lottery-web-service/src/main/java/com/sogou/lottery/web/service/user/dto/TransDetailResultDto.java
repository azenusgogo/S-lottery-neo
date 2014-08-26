package com.sogou.lottery.web.service.user.dto;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransDetailResultDto {
	
	private String transactionId;
	private Timestamp createTime;
	/**
	 * 1 充值 2 提现 3 消费 4 退款收入 5 提现手续费 6 彩票返奖
	 */
	private Integer type;
	private String typeDesc;
    /**
     * 资金方向：收入（充值 退款收入 彩票返奖）、支出(提现 消费 提现手续费)
     */
    private String fundsDirection;

	/**
	 * 发生金额
	 */
	private Long amount;

    /**
     * 提现手续费,提现操作时才会有
     */
    private Long fee = null;

	/**
	 * 账户余额
	 */
	private Long balance;// 1.5.6接口中去掉了
	/**
	 * 支付渠道
	 */
	private String channel;
	private Integer status;
	private String statusDesc;
	private Timestamp confirmTime;
	
	public String getTransactionId() {
	
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
	
		this.transactionId = transactionId;
	}
	
	public Timestamp getCreateTime() {
	
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
	
		this.createTime = createTime;
	}
	
	public Integer getType() {
	
		return type;
	}
	
	public void setType(Integer type) {
	
		this.type = type;
	}
	
	public String getTypeDesc() {
	
		return typeDesc;
	}
	
	public void setTypeDesc(String typeDesc) {
	
		this.typeDesc = typeDesc;
	}
	
	public Long getAmount() {
	
		return amount;
	}
	
	public void setAmount(Long amount) {
	
		this.amount = amount;
	}
	
	public Long getBalance() {
	
		return balance;
	}
	
	public void setBalance(Long balance) {
	
		this.balance = balance;
	}
	
	public String getChannel() {
	
		return channel;
	}
	
	public void setChannel(String channel) {
	
		this.channel = channel;
	}
	
	public Integer getStatus() {
	
		return status;
	}
	
	public void setStatus(Integer status) {
	
		this.status = status;
	}
	
	public String getStatusDesc() {
	
		return statusDesc;
	}
	
	public void setStatusDesc(String statusDesc) {
	
		this.statusDesc = statusDesc;
	}
	
	public Timestamp getConfirmTime() {
	
		return confirmTime;
	}
	
	public void setConfirmTime(Timestamp confirmTime) {
	
		this.confirmTime = confirmTime;
	}

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getFundsDirection() {
        return fundsDirection;
    }

    public void setFundsDirection(String fundsDirection) {
        this.fundsDirection = fundsDirection;
    }

    public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
