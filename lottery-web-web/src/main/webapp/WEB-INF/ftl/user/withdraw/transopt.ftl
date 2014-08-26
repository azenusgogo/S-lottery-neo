<#if !cdnBaseUrl??><#include "../../fakeData/user-trans.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "withdraw" />
<#assign pageNo = trans.pageNo />
<#assign totalPage = trans.totalPage />

<#--本页对应URL，查询条件时使用-->
<#assign pageBase = "/login/user/withdraw/transopt.html" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/withdraw.html">提款申请</a>
            <a href="${domainUrl}login/user/withdraw/transopt.html" class="cur">提款记录</a>
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
                <th width="180">提款流水号</th>
                <th>申请时间</th>
                <th>到账时间</th>
                <th>提款金额（元）</th>
                <th width="120">提款银行</th>
                <th>手续费</th>
                <th width="80">状态</th>
            </tr>
			<#if trans.transList?? && trans.transList?size gt 0>
            <#assign data = trans.transList />
            <#list data as x>
            <#if x??>
            <tr<#if x_index%2 == 1> class="bg"</#if>>
                <td>${x.transactionId}</td>
                <td>${x.createTime?date}<br />${x.createTime?string("HH:mm:ss")}</td>
                <td>
                    <#if x.confirmTime??>
                    ${x.confirmTime?date}<br />${x.confirmTime?string("HH:mm:ss")}
                    <#else>
                    &minus;
                    </#if>
                </td>
                <td><b class="red">${(x.amount/100)?string("0.00")}</b>元</td>
                <td>${x.channel}</td>
                <td>
                    <#if x.fee??>
                    <b class="red">${(x.fee/100)?string("0.00")}</b>元
                    <#else>
                    &minus;
                    </#if>
                </td>
                <td>
                    <#if x.statusDesc == "失败">
                    <span class="red">失败</span>
                    <#else>
                    ${x.statusDesc}
                    </#if>
                </td>
            </tr>
            </#if>
            <#if x_index == transDto.pageSize-1><#break></#if>
            </#list>
            <#else>
            <tr>
                <td colspan="7" class="no-order">没有任何记录</td>
            </tr>
            </#if>
        </table>
        <#include "../include/u-page.ftl" />
		<div class="btm-tips">
			<p><i></i>提款注意事项：<br />
			<em>为了防止恶意提款、洗钱等不法行为，每笔存入资金的20%须用于实际消费，否则提款受限，请按实际购买额充值；</em><br />
			为了防止极个别用户进行信用卡套现、洗钱、网络钓鱼等违法行为，本站针对异常提款进行严格审核，正常现金提款不受影响。<br />
			为了保障您的资金安全，您的提款申请将通过系统审核处理再汇到您的账户，请1-3个工作日后查询银行帐户；
			查询提款是否到账以银行账户实际到账为准，手机短信只是代表您的提款申请我们已经受理；<br />
			<a href="/help/f-withdraw.html" target="_blank">更多关于提款的问题</a></p>
		</div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>