<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "charge" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <form action="${domainUrl}login/charge/confirm.html" method="post" id="user_recharge">
        <input type="hidden" name="payOrderId" value="${payOrderId!''}" />
        <input type="hidden" name="amount" class="amount" value="" />
        <div class="u-cont">
            <h3><b>充值中心</b></h3>
    		<div class="u-chong-box">
    			<div class="fipt">
    				<label class="f-h">充值金额：</label>
    				<div class="f-n">
                        <#if (recharge.amount)?? && (recharge.amount) gt 0>
                        <input type="text" class="txt money" value="${(recharge.amount/100)?c}" /> 
                        <#else>
                        <input type="text" class="txt money" value="" /> 
                        </#if>
                        元<br /><span class="tips">即时到账，搜狗彩票充值免收手续费，最小充值<em>${(minAmount/100)?c}</em>元</span>            
                    </div>
    			</div>
    			<ul class="u-bank-list cf" style="height:120px;">
                <#list chargeBank as x>
                <#if x??>
                    <#if recharge?? && recharge.bankId?? && recharge.bankId?number == x.bankId?number>
                    <li>
                        <label>
                            <input type="radio" name="bankId" value="${x.bankId}" checked />
                            <span class="ct bank-ico b-${x.bankId} cur">${x.bank}</span>
                        </label>
                    </li>
                    <#else>
                    <li>
                        <label>
                            <input type="radio" name="bankId" value="${x.bankId}" />
                            <span class="ct bank-ico b-${x.bankId}">${x.bank}</span>
                        </label>
                    </li>
                    </#if>
                </#if>
                </#list>
    			</ul>
                <div class="cf morebank">
                    没有找到您需要的银行？
                    <a href="javascript:;" class="show_bank">点击显示全部银行</a>
                </div>
    			<div class="u-btm-btn">
    				<a class="fbtn submit" href="javascript:;">去充值</a>
    			</div>
    		</div>
            <div class="btm-tips">
                <p><i></i>注意事项：<br />
                1.请确保您的银行卡已经开通网上银行服务，否则无法使用付款功能。<br />
                2.付款后，如因银行数据传输故障或网络信号延迟，造成资金未能及时到账，请联系在线客服！</p>
            </div>
        </div>
    </form>
</div>
<input type="hidden" id="maxAmount" value="${(maxAmount/100)?c}" />
<input type="hidden" id="minAmount" value="${(minAmount/100)?c}" />
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>