<#if !cdnBaseUrl??><#include "../../fakeData/user-trans.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#--本页对应URL，查询条件时使用-->
<#assign pageBase = "/login/user/trans/trans.html" />

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
            <a href="${domainUrl}login/user/trans/trans.html" class="cur">收支明细</a>
            <a href="${domainUrl}login/user/trans/charge.html">充值记录</a>
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
                <th width="180">时间</th>
                <th width="185">交易类型</th>
                <th>收入</th>
                <th>支出</th>
            </tr>
            <#if trans.transList?? && trans.transList?size gt 0>
            <#assign data = trans.transList />
            <#list data as x>
            <#if x??>
            <tr<#if x_index%2 == 1> class="bg"</#if>>
                <td>${x.createTime?date}<br />${x.createTime?string("HH:mm:ss")}</td>
                <td>${x.typeDesc}</td>
                <#if "146"?contains(x.type?string)>
                <td><b class="green">${(x.amount/100)?string("0.00")}</b>元</td>
                <td>&minus;</td>
                <#else>
                <td>&minus;</td>
                <td><b class="red">${(x.amount/100)?string("0.00")}</b>元</td>
                </#if>
            </tr>
            </#if>
            </#list>
            <#else>
            <tr>
                <td colspan="4" class="no-order">没有任何记录</td>
            </tr>
            </#if>
        </table>
        <#include "../include/u-page.ftl" />
        <div class="btm-tips">
            <p><i></i>您最多可查询最近一年的收支明细，如果您需要查询更多的收支明细，请联系在线客服！</p>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>