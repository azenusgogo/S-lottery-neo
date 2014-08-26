package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.order.BetNumber;
import com.sogou.lottery.base.vo.order.StakeOrder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：订单-票号信息
 *
 * @author haojiaqi
 */
public class StakeBetInfoDto implements Serializable {

    BetNumber betNumber;

    StakeOrder stakeOrder;

    List<BetNumber> betNumberList = new ArrayList<>();

    public StakeBetInfoDto(BetNumber betNumber) {
        this.betNumber = betNumber;
    }

    public StakeBetInfoDto(StakeOrder stakeOrder, List<BetNumber> betNumbers) {
        this.stakeOrder = stakeOrder;
        this.betNumberList = betNumbers;
    }

    public StakeBetInfoDto(StakeOrder stakeOrder) {
        this.stakeOrder = stakeOrder;
    }

    public BetNumber getBetNumber() {
        return betNumber;
    }

    public void setBetNumber(BetNumber betNumber) {
        this.betNumber = betNumber;
    }

    public StakeOrder getStakeOrder() {
        return stakeOrder;
    }

    public void setStakeOrder(StakeOrder stakeOrder) {
        this.stakeOrder = stakeOrder;
    }

    public List<BetNumber> getBetNumberList() {
        return betNumberList;
    }

    public void setBetNumberList(List<BetNumber> betNumberList) {
        this.betNumberList = betNumberList;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }


}
