<#if !cdnBaseUrl??><#include "../../fakeData/user-trans.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#--本页对应URL，查询条件时使用-->
<#assign pageBase = "/login/user/trans/charge.html" />

<#--本页对应左侧菜单功能-->
<#assign func = "deal" />

<#--分页所需参数-->
<#assign pageNo = trans.pageNo />
<#assign totalPage = trans.totalPage />
<#assign pageArg = ["month="+transDto.month] />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/trans/trans.html">收支明细</a>
            <a href="${domainUrl}login/user/trans/charge.html" class="cur">充值记录</a>
            <a href="${domainUrl}login/user/trans/withdraw.html">提款记录</a>
        </div>
        <div class="u-search">
            <form action="${pageBase}" id="month">
            <select name="month">
                <option value="0"<#if transDto.month == 0> selected</#if>>最近一周</option>
                <option value="1"<#if transDto.month == 1> selected</#if>>最近一个月</option>
                <option value="3"<#if transDto.month == 3> selected</#if>>最近三个月</option>
                <option value="6"<#if transDto.month == 6> selected</#if>>最近半年</option>
                <option value="12"<#if transDto.month == 12> selected</#if>>最近一年</option>
            </select>
            <a href="#" onclick="document.getElementById('month').submit();return false;" class="fbtn min-white">查询</a>
            <span>您最多可查询最近一年的收支记录</span>
            </form>
        </div>
        
        <table class="u-tbl-li">
            <tr>
                <th width="200">充值流水号</th>
                <th>充值时间</th>
                <th>充值金额</th>
                <th>充值渠道</th>
                <th width="200">状态</th>
            </tr>
            <#if trans.transList?? && trans.transList?size gt 0>
            <#assign data = trans.transList />
            <#list data as x>
            <#if x??>
            <tr<#if x_index%2 == 1> class="bg"</#if>>
                <td>${x.transactionId}</td>
                <td>${x.createTime?date}<br />${x.createTime?string("HH:mm:ss")}</td>
                <td><b class="red">${(x.amount/100)?string("0.00")}</b>元</td>
                <td>${x.channel}</td>
                <td>
                    <#if x.statusDesc == "失败">
                    <span class="red">失败</span>
                    <#else>
                    ${x.statusDesc}
                    </#if>
                </td>
            </tr>
            </#if>
            </#list>
            <#else>
            <tr>
                <td colspan="5" class="no-order">没有任何记录</td>
            </tr>
            </#if>
        </table>
        <#include "../include/u-page.ftl" />
        <div class="btm-tips">
            <p><i></i>充值后，银行账户已扣钱，但搜狗账户显示未到账，请联系在线客服！</p>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>