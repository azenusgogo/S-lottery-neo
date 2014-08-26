package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.order.OrderInfo;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 描述：订单查询
 *
 * @author haojiaqi
 */
public class OrderQueryResultDto implements Serializable {

    private OrderQueryDto orderQueryDto;

    private Integer totalNum = 0;

    private Integer totalPage = 0;

    private List<OrderInfo> orderInfoList = Collections.emptyList();

    public OrderQueryResultDto(OrderQueryDto orderQueryDto) {
        this.orderQueryDto = orderQueryDto;
    }


    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public List<OrderInfo> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    @JsonIgnore
    public OrderQueryDto getOrderQueryDto() {
        return orderQueryDto;
    }

    public void setOrderQueryDto(OrderQueryDto orderQueryDto) {
        this.orderQueryDto = orderQueryDto;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
