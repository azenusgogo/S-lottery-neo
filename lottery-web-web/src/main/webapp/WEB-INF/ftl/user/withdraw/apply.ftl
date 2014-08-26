<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "withdraw" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
	    <div class="u-tab cf">
			<a href="${domainUrl}login/user/withdraw.html" class="cur">提款申请</a>
			<a href="${domainUrl}login/user/withdraw/transopt.html">提款记录</a>
	    </div>
	    <div class="u-cash" id="my_cash">
	    	<div class="fipt" style="height:40px">
				<label class="f-h">当前现金账户可用余额：</label>
				<div class="f-n">&yen;<em class="mycash">&minus;</em>元</div>
			</div>
			<div class="fipt">
				<label class="f-h" style="line-height:45px">银行卡：</label>
				<div class="f-n bank-card">
					<ul class="card-box bdr">
                        <#if userinfo?? && userinfo.bankCardNo?? && userinfo.bankCardNo != "">
                        <li>
                            <i class="bank-ico b-${userinfo.bankId}"></i>
                            <b>卡号：${userinfo.displayBankCardNo}</b>
                        </li>
                        <#else>
						<li><b>&#43;</b> <a href="#" class="change_card">添加银行卡</a></li>
                        </#if>
					</ul>
				</div>
			</div>
			<div class="fipt">
				<label class="f-h">提款金额：</label>
				<div class="f-n"><input type="text" class="txt bdr money" tabindex="1" />&nbsp;&nbsp;<b>元</b><br />
				<span id="cash_tip">最小提现金额为${(minAmount/100)?c}元，最大为${(maxAmount/100)?c}元，每天可以提款<em>${maxTimes!"-"}</em>次，每笔手续费<em id="fee_v">${(fee/100)?c}</em>元</span></div>
			</div>
			<div class="fipt">
				<label class="f-h">支付密码：</label>
				<div class="f-n"><input type="password" class="txt bdr pwd" tabindex="2" />&nbsp;&nbsp;<a href="${domainUrl}login/user/pwd/find.html">忘记密码？</a><br />
				<span>此为您在搜狗彩票的支付密码，与银行卡无关</span></div>
			</div>
			<div class="fipt">
				<label class="f-h">验证码：</label>
				<div class="f-n"><input type="text" class="txt s1 bdr code" tabindex="3" /> <img id="codePic" class="add-on" src="${domainUrl}login/user/captcha/withdraw/gen.html?${.now?long?c}">&nbsp;&nbsp;<a href="#" onclick="var _this = document.getElementById('codePic');_this.src = _this.src.split('?')[0]+'?t='+ +new Date();return false">换一张</a></div>
			</div>
			<div class="u-btm-btn">
				<a class="fbtn submit" href="#">申请提款</a>
			</div>
	    </div>
		<div class="btm-tips">
			<p><i></i>提款注意事项：<br />
			<em>为了防止恶意提款、洗钱等不法行为，每笔存入资金的20%须用于实际消费，否则提款受限，请按实际购买额充值；</em><br />
			为了防止极个别用户进行信用卡套现、洗钱、网络钓鱼等违法行为，本站针对异常提款进行严格审核，正常现金提款不受影响。<br />
			为了保障您的资金安全，您的提款申请将通过系统审核处理再汇到您的账户，请1-3个工作日后查询银行帐户；
			查询提款是否到账以银行账户实际到账为准，手机短信只是代表您的提款申请我们已经受理；<br />
			<a href="/help/f-withdraw.html" target="_blank">更多关于提款的问题</a>
		</p>
		</div>
    </div>
</div>
<div class="dialog" id="change_card1">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>添加银行卡</div>
            <div class="modbox">
                <p class="ptxt"><b>提款绑定的银行卡开户行必须与您之前注册的实名一致，<br>否则无法提款。请确认信息无误后再提交！</b></p>
                <div class="dia-bank-ipt">
                    <div class="fipt">
                        <label class="f-h">银行开户人姓名：</label>
                        <div class="f-n"><b>${userinfo.displayTrueName!'-'}</b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">提款银行：</label>
                        <div class="f-n">
                        <select class="bank">
                            <option value="0">请选择银行</option>
                            <#list drawbanks as x>
                            <#if x??>
                            <option value="${x.bankId}">${x.bank}</option>
                            </#if>
                            </#list>
                        </select>
                        <br>
                        <em class="valid"></em>
                        </div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">银行卡卡号：</label>
                        <div class="f-n">
                            <input type="text" class="txt bdr bank_n" />
                            <span class="dia-red valid"></span>
                            <br /><span class="dia-gray">仅支持提款身份认证为<b>${userinfo.displayTrueName}</b>的银行借记卡</span>
                            <div class="bankcard-tip">
                                <span></span>
                                <sup class="ct"></sup>
                                <sub class="ct"></sub>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="reqtip">
                <b>提款手续费及到账时间</b>
                <p class="modpli"><i></i>本站目前支持提款至<em>${(drawbanks?size)!12}</em>家银行，每笔提款金额最少为<em>${(minAmount/100)?c}</em>元。</p>
                <p class="modpli"><i></i>提款手续费由银行收取，搜狗彩票为您提供补贴，您每笔仅需向银行支付<em>${(fee/100)?c}</em>元钱手续费。</p>
                <p class="modpli"><i></i>搜狗彩票会第一时间为您办理您的提款申请。</p>
                <p class="modpli"><i></i>您的提款将在三个工作日左右到账，具体到账时间由银行决定。</p>
            </div>
            <div class="modbtn">
                <a class="fbtn bind" href="#">确认绑定</a>
            </div>
        </div>
    </div>
</div>
<div class="dialog" id="change_card2">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>温馨提示</div>
            <div class="modbox">
                <p class="ptxt"><b>提款绑定的银行卡开户行必须与您之前注册的实名一致，<br />否则无法提款。请确认信息无误后再提交！</b></p>
                <div class="dia-bank-ipt">
                    <div class="fipt">
                        <label class="f-h">银行开户人姓名：</label>
                        <div class="f-n"><b>${userinfo.displayTrueName!'-'}</b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">提款银行：</label>
                        <div class="f-n"><b class="bank_name"></b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">银行卡号：</label>
                        <div class="f-n"><b class="bank_n"></b></div>
                    </div>
                    <div class="modbtn">
                        <a class="fbtn submit" href="#">确认提交</a>&nbsp;&nbsp;
                        <a class="fbtn btn-white back" href="#">返回修改</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="minMoney" value="${(minAmount/100)?c}" />
<input type="hidden" id="maxMoney" value="${(maxAmount/100)?c}" />
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>