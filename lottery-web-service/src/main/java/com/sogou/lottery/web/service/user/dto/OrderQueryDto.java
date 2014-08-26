package com.sogou.lottery.web.service.user.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 描述：订单查询
 * 构造查询用户订单列表sql 满足用户id查询、中奖订单、等待开奖条件、时间范围、订单状态、等条件查询
 *
 * @author haojiaqi
 */
public class OrderQueryDto implements Serializable {

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 中奖状态  0-代表未开奖，1-代表未中奖订单,2-代表中奖订单 null代表全部
     */
    private Integer prizeStatus;

    /**
     * 订单时间范围
     */
    private Integer timeLevel;

    /**
     * 未支付订单
     */
    private Boolean noPayOrderFlag;

    /**
     * 订单过期标志失效
     */
    private Boolean timeValidOrderFlag;


    private Integer pageNo = 1;
    private Integer pageSize = 10;

    /**
     * 查询状态 0-代表初始化，1代表查询中，2-代表查询成功
     */
    private Integer queryStatus = 0;


    public void initQuery() {
        this.queryStatus = 0;
    }

    public void doQuery() {
        this.queryStatus = 1;
    }

    public void doneQuery() {
        this.queryStatus = 2;
    }

    public boolean isSuccess() {
        return this.queryStatus == 2;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(Integer timeLevel) {
        this.timeLevel = timeLevel;
    }

    public Boolean getNoPayOrderFlag() {
        return noPayOrderFlag;
    }

    public void setNoPayOrderFlag(Boolean noPayOrderFlag) {
        this.noPayOrderFlag = noPayOrderFlag;
    }

    public Boolean getTimeValidOrderFlag() {
        return timeValidOrderFlag;
    }

    public void setTimeValidOrderFlag(Boolean timeValidOrderFlag) {
        this.timeValidOrderFlag = timeValidOrderFlag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Integer getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(Integer prizeStatus) {
        this.prizeStatus = prizeStatus;
    }
}
