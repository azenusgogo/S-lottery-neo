package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.order.OrderInfo;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：订单明细
 *
 * @author haojiaqi
 */
public class OrderDetailDto implements Serializable {


    /**
     * 订单id
     */
    private String orderId;

    /**
     * 拆单标志
     */
    private Boolean splitFlag;

    /**
     * 开奖号码
     */
    private String prizeNumber;

    /**
     * 用户id
     */
    private String userId;

    private Long totalBetNums = 0L;

    /**
     * 订单信息
     */
    OrderInfo orderInfo;

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private Integer totalNum;

    private Integer totalPage = 0;

    /**
     * 票号信息
     */
    List<StakeBetInfoDto> stakeBetInfoDtoList;

    @JsonIgnore
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonIgnore
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Boolean getSplitFlag() {
        return splitFlag;
    }

    public void setSplitFlag(Boolean splitFlag) {
        this.splitFlag = splitFlag;
    }

    public List<StakeBetInfoDto> getStakeBetInfoDtoList() {
        return stakeBetInfoDtoList;
    }

    public void setStakeBetInfoDtoList(List<StakeBetInfoDto> stakeBetInfoDtoList) {
        this.stakeBetInfoDtoList = stakeBetInfoDtoList;
    }

    @JsonIgnore
    public String getPrizeNumber() {
        return prizeNumber;
    }

    public void setPrizeNumber(String prizeNumber) {
        this.prizeNumber = prizeNumber;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
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

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTotalBetNums() {
        return totalBetNums;
    }

    public void setTotalBetNums(Long totalBetNums) {
        this.totalBetNums = totalBetNums;
    }
}
