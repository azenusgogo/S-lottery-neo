<#if !cdnBaseUrl??><#include "../../fakeData/user-order.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#--本页对应URL，查询条件时使用-->
<#assign pageBase = "/login/user/order/bets.html" />

<#--本页对应左侧菜单功能-->
<#assign func = "order" />

<#--分页所需参数-->
<#assign pageNo = orderList.orderQueryDto.pageNo!1 />
<#assign totalPage = orderList.totalPage />
<#assign timeLevel = orderList.orderQueryDto.timeLevel />
<#assign pageArg = ["timeLevel="+timeLevel] />
<#if orderList.orderQueryDto.prizeStatus??>
<#assign prizeStatus = orderList.orderQueryDto.prizeStatus />
<#assign pageArg = pageArg + ["prizeStatus=" + prizeStatus] />
</#if>

<div class="wrapper cf ucenter" id="order_bets">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/order/bets.html" class="cur">投注记录</a>
            <a href="${domainUrl}login/user/order/nopay.html?timeValidOrderFlag=1">
                未付款订单
                <i class="nopay_num">-</i>
            </a>
        </div>
        <div class="u-search">
        	<form action="${pageBase}" id="timeLevel">
            <#if prizeStatus??>
            <input type="hidden" name="prizeStatus" value="${prizeStatus}" />
            </#if>
            <select name="timeLevel">
                <option value="0"<#if timeLevel == 0> selected</#if>>最近一周</option>
                <option value="1"<#if timeLevel == 1> selected</#if>>最近一个月</option>
                <option value="3"<#if timeLevel == 3> selected</#if>>最近三个月</option>
                <option value="6"<#if timeLevel == 6> selected</#if>>最近半年</option>
                <option value="12"<#if timeLevel == 12> selected</#if>>最近一年</option>
            </select>
            <a href="#" onclick="document.getElementById('timeLevel').submit();return false;" class="fbtn min-white">查询</a>
            </form>
            <label><input type="radio" onclick="window.location.href='?timeLevel=${timeLevel}'"<#if !prizeStatus??>checked</#if> />全部订单</label>
            <label><input type="radio" onclick="window.location.href='?timeLevel=${timeLevel}&prizeStatus=2'"<#if prizeStatus?? && prizeStatus == 2>checked</#if> />中奖订单</label>
            <label><input type="radio" onclick="window.location.href='?timeLevel=${timeLevel}&prizeStatus=0'"<#if prizeStatus?? && prizeStatus == 0>checked</#if> />等待开奖</label>
            
        </div>
        <table class="u-tbl-li">
            <tr>
                <th width="100">时间</th>
                <th width="80">彩种</th>
                <th width="100">订单信息</th>
                <th width="100">订单金额（元）</th>
                <th width="80">订单状态</th>
                <th>中奖结果</th>
                <th>奖金（元）</th>
                <th width="100">操作</th>
            </tr>
            <#if (orderList.orderInfoList)?? && orderList.orderInfoList?size gt 0>
            <#list orderList.orderInfoList as x>
            <#if x??>
            <tr<#if x_index%2 == 1> class="bg"</#if> style="${(x.prizeStatus>1)?string('background:#FFFDE9','')}">
                <td><b>${x.createTime?string("M月d日")}</b><br />${x.createTime?time}</td>
                <td><a target="_blank" href="${domainUrl}${x.gameId!""}/">${x.gameDesc!"-"}</a></td>
                <td>第${x.periodNo!"-"}期<br />${x.orderTypeDesc!"普通"}投注</td>
                <td>${(x.userOrderAmount/100)?string("0.00")}元</td>
                <td>${x.orderStatusDesc!"-"}</td>
                <td>${x.prizeStatusDesc!"-"}</td>
                <#if x.officialBonus?? && x.prizeStatus != 0>
                <td>
                    <b class="red">${(x.officialBonus/100)?string("0.00")}</b>元
                    <#if x.prizeStatus gt 1><img src="${cdnBaseUrl}img/i/zj.gif" class="zhong" /></#if>
                </td>
                <#else>
                <td>&minus;</td>
                </#if>
                <td><a target="_blank" href="${domainUrl}login/user/order/detail.html?orderId=${x.userOrderId}">查看详情</a><br /><a target="_blank" href="${domainUrl}${x.gameId}/?orderId=${x.userOrderId}">继续购买</a></td>
            </tr>
            </#if>
            </#list>
            <#else>
            <tr>
            	<td colspan="8" class="no-order">没有任何记录</td>
            </tr>
            </#if>
        </table>
        <#include "../include/u-page.ftl" />
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>