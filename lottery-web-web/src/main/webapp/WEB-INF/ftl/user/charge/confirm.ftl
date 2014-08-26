<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "charge" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <h3><b>充值中心</b></h3>
        <form action="${domainUrl}login/charge/charge.html" method="post" target="_blank" id="user_recharge2">
        <#if recharge??>
            <input type="hidden" value="${recharge.id}" name="id" />

    		<div class="u-chong-box">
    			<div class="charge-confirm">
                	<span>请确认充值信息</span>
                	<div class="charge-info">
                		<p><b>账 户 名：</b>${recharge.userId!"-"}</p>
                		<p><b>充值金额：</b><em>${(recharge.amount/100)?c}</em>元</p>
                		<p class="sel-bank"><b>支付银行：</b><i class="bank-ico b-${recharge.bankId}"></i> <a href="${domainUrl}login/charge/pre.html?amount=${recharge.amount?c}&bankId=${recharge.bankId}"><i class="ico-back"></i>返回重新选择银行</a></p>
                	</div>
                </div>
    			<div class="u-btm-btn">
    				<a class="fbtn submit" href="javascript:;">立即充值</a>
    				<p class="tips"><i class="ico-blank"></i> 将会在新窗口中打开网上银行<br /><span>提示：充值成功后，请勿立即关闭窗口</span></p>
    			</div>
    		</div>
            <div class="btm-tips">
                <p><i></i>注意事项：<br />
                1.请确保您的银行卡已经开通网上银行服务，否则无法使用付款功能。<br />
                2.付款后，如因银行数据传输故障或网络信号延迟，造成资金未能及时到账，请联系在线客服！</p>
            </div>
        </#if>
        </form>
    </div>
</div>
<div class="dialog" id="charge_complete">
<div class="modal">
    <div class="modcon">
        <div class="title"><i title="关闭" class="close dialog-close">&times;</i>提示</div>
        <div class="modbox">
            <div class="tip-ico" style="margin:15px 0 0 20px">
                <i class="tip-warn"></i>
                <p class="warn-cont">
                    <b>请在新打开的网上银行<br />页面上完成充值</b><br />
                    <span class="dia-gray">充值完成前请不要关闭此窗口。<br />
                    完成充值后请根据您的情况点击下面的按钮</span>
                </p>
            </div>
            <div class="modbtn">
                <a class="fbtn" href="/login/charge/done.html?id=${recharge.id}&payOrderId=${recharge.payOrderId!''}">已完成充值</a>&nbsp;&nbsp;
                <a class="fbtn btn-white" href="/help/f-recharge.html#rec05" target="_blank">充值遇到问题</a>
                <p class="backstep"><a href="${domainUrl}login/charge/pre.html?amount=${recharge.amount?c}&bankId=${recharge.bankId}"><i class="ico-back"></i>返回重新选择银行</a></p>
            </div>
        </div>
    </div>
</div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>