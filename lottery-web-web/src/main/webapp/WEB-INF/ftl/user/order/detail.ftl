<#if !cdnBaseUrl??><#include "../../fakeData/user-order.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "order" />
<#assign fmOrderInfo = orderDetail.orderInfo />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont" id="u_orderDetail">
        <h3><b>订单详情</b></h3>
        <div class="order-detail">
            <div class="u-cp-info cf">
                <i class="logos ${fmOrderInfo.gameId!''} ct">${fmOrderInfo.gameDesc!"-"}</i>
                <p>
                    <b>${fmOrderInfo.gameDesc!"-"}</b>&nbsp;&nbsp;第${fmOrderInfo.periodNo}期
                    <#if fmOrderInfo.gameId == "ssq">
                    <span>最受欢迎，<em>2</em>元可中<em>1000</em>万<br />
                    每周二、四、日 21:30 开奖</span>
                    <#elseif fmOrderInfo.gameId == "dlt">
                    <span>好玩易中，<em>3</em>元可中<em>1600</em>万<br />
                    每周一、三、六 20:30 开奖</span>
                    <#elseif fmOrderInfo.gameId == "qlc">
                    <span>30选7简单易中，<em>2</em>元可中<em>500</em>万<br />
                    每周一、三、五 21:30 开奖</span>
                    <#elseif fmOrderInfo.gameId == "qxc">
                    <span>30选7简单易中，<em>2</em>元可中<em>500</em>万<br />
                    每周二、五、日 20:30 开奖</span>
                    <#elseif fmOrderInfo.gameId == "k3js">
                    <span>10分钟1期，快乐猜大小</span>
                    <#elseif fmOrderInfo.gameId == "k3jl">
                    <span>10分钟1期，返奖率59%</span>
                    <#elseif fmOrderInfo.gameId == "k3gx">
                    <span>10分钟1期，每天78期</span>
                    <#elseif fmOrderInfo.gameId == "f14">
                    <span>14场猜胜负，<em>最高奖500</em>万</span>
                    <#elseif fmOrderInfo.gameId == "f9">
                    <span>难度简化猜9场，<em>最高奖500</em>万</span>
                    </#if>
                </p>
                <#if fmOrderInfo.orderStatus == 0 && .now lt fmOrderInfo.deadline>
                <a href="javascript:;" data-payorderid="${fmOrderInfo.payOrderId}" class="fbtn fbtn2 ucenter_pay_btn">支付</a>
                <#else>
                <a target="_blank" href="${domainUrl}${fmOrderInfo.gameId}/?orderId=${orderDetail.orderId}" class="fbtn fbtn2">继续购彩</a>
                </#if>
            </div>
        </div>
        <div class="order-stat">
            <dl>
                <dt>订单状态：</dt>
                <dd>${fmOrderInfo.orderStatusDesc}</dd>
            </dl>
            <dl>
                <dt>中奖金额：</dt>
            <#if (fmOrderInfo.orderStatus!4) == 4>
                <dd>&minus;</dd>
            <#else>
                <#if fmOrderInfo.prizeStatus == 0>
                <dd>&minus; <span>奖金还没有开出，请您耐心等待！</span></dd>
                <#elseif fmOrderInfo.prizeStatus ==1>
                   <dd>0元 <span>不要灰心，也许下一个大奖就是你！</span></dd>
                <#elseif fmOrderInfo.prizeStatus ==4>
                  <dd>${(fmOrderInfo.officialBonus/100)?string("0.00")}元 <span>手气正旺，鸿运当头！</span></dd>
                <#elseif fmOrderInfo.prizeStatus ==5>
                  <dd>${(fmOrderInfo.officialBonus/100)?string("0.00")}元 <span>财运滚滚，势不可挡！</span></dd>
                </#if>
            </#if>
            </dl>
            <dl>
                <dt>本期开奖号码：</dt>
                <dd>
                    <#include "detail-prizeNo.ftl" />
                </dd>
            </dl>
            <dl>
                <dt>订单编号：</dt>
                <dd>${orderDetail.orderId}</dd>
            </dl>
        </div>
        <h3 id="selTit"><b>选号详情</b> 共<em class="bets_num">-</em>注</h3>
        <div class="mynum_tbl_list"></div>
        <div class="u-page2"></div>
        <h3><b>投注信息</b></h3>
        <div class="order-stat">
            <dl>
                <dt>投注金额：</dt>
                <dd><b class="red">${(fmOrderInfo.userOrderAmount/100)?string("0.00")}</b>元</dd>
            </dl>
            <dl>
                <dt>注数：</dt>
                <dd><span class="red bets_num">-</span>注</dd>
            </dl>
            <dl>
                <dt>下单时间：</dt>
                <dd>${fmOrderInfo.createTime?date}</dd>
            </dl>
        </div>
        <div class="u-order-do">
            <a target="_blank" href="${domainUrl}${fmOrderInfo.gameId}/?orderId=${orderDetail.orderId}" class="fbtn fbtn2">继续购彩</a>&nbsp;
            <a href="${domainUrl}login/user/order/bets.html" class="fbtn fbtn2">返回订单</a>
        </div>
    </div>
</div>
<input type="hidden" id="betOrderId" value="${orderDetail.orderId}" />
<input type="hidden" id="betPageSize" value="${orderDetail.pageSize!10}" />
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>