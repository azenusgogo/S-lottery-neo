<#if !cdnBaseUrl??><#include "../../fakeData/user-order.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#--本页对应URL，查询条件时使用-->
<#assign pageBase = "/login/user/order/nopay.html" />

<#--本页对应左侧菜单功能-->
<#assign func = "order" />

<#--分页所需参数-->
<#assign pageNo = orderList.orderQueryDto.pageNo />
<#assign totalPage = orderList.totalPage />
<#if orderList.orderQueryDto.timeValidOrderFlag??>
<#assign timeValidOrderFlag = orderList.orderQueryDto.timeValidOrderFlag?string("1","0") />
<#assign pageArg = ["timeValidOrderFlag=" + timeValidOrderFlag] />
</#if>

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/order/bets.html">投注记录</a>
            <a href="${domainUrl}login/user/order/nopay.html?timeValidOrderFlag=1" class="cur">未付款订单</a>
        </div>
        <div class="u-search">
            <label onclick="window.location.href='${pageBase}'"><input type="radio" name="timeValidOrderFlag"<#if !timeValidOrderFlag??> checked</#if> />全部订单</label>
            <label onclick="window.location.href='${pageBase}?timeValidOrderFlag=1'"><input type="radio" name="timeValidOrderFlag" value="1"<#if timeValidOrderFlag?? && timeValidOrderFlag == "1"> checked</#if> />未过期订单</label>
            <label onclick="window.location.href='${pageBase}?timeValidOrderFlag=0'"><input type="radio" name="timeValidOrderFlag" value="0"<#if timeValidOrderFlag?? && timeValidOrderFlag == "0"> checked</#if> />过期订单</label>
        </div>
        <table class="u-tbl-li">
            <tr>
                <th width="100">时间</th>
                <th width="80">彩种</th>
                <th width="120">订单信息</th>
                <th width="120">订单金额（元）</th>
                <th>投注截止时间</th>
                <th width="200">操作</th>
            </tr>
            <#if (orderList.orderInfoList)?? && (orderList.orderInfoList)?size gt 0>
            <#list orderList.orderInfoList as x>
            <#if x??>
            <tr<#if x_index%2 == 1> class="bg"</#if>>
                <td><b>${x.createTime?string("M月d日")}</b><br />${x.createTime?time}</td>
                <td><a target="_blank" href="${domainUrl}${x.gameId!""}/">${x.gameDesc!"-"}</a></td>
                <td>第${x.periodNo!"-"}期<br />${x.orderTypeDesc!"普通"}投注</td>
                <td>${(x.userOrderAmount/100)?string("0.00")}元</td>
                <td>
                <#if x.deadline??>
                <#assign today = (.now?date + " 00:00:00")?datetime("yyyy-MM-dd HH:mm:ss")?long />
                <#assign deadLong = x.deadline?long />
                    <#if deadLong gt today && deadLong lt (today+86400000)>
                    今天
                    <#elseif deadLong gt (today+86400000) && deadLong lt (today+86400000*2)>
                    明天
                    <#elseif deadLong gt (today+86400000*2) && deadLong lt (today+86400000*3)>
                    后天
                    <#else>
                    ${x.deadline?string("M月d日")} 
                    </#if>${x.deadline?time}
                <#else>
                &minus;
                </#if>
                </td>
                <td>
                    <#if x.deadline?? && .now lt x.deadline>
                    <a href="javascript:;" data-payorderid="${x.payOrderId}" class="fbtn fbtn2 ucenter_pay_btn">付款</a>&nbsp;&nbsp;
                    </#if>
                    <a target="_blank" href="${domainUrl}login/user/order/detail.html?orderId=${x.userOrderId}">查看详情</a>
                </td>
            </tr>
            </#if>
            </#list>
            <#else>
            <tr>
                <td colspan="6" class="no-order">没有任何记录</td>
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