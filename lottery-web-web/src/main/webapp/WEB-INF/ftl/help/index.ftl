<#if !cdnBaseUrl??><#include "../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#include "../common/header.ftl" />

<div class="help_main">
	<div class="sitepath">
		您所在的位置：<a href="/">搜狗彩票</a> &gt; 帮助中心
	</div>
	<div class="hcont cf" id="help_center">
		<div class="help_menu">
			<#include "include/menu.ftl" />
		</div>

		<div class="h_index">
			<div class="h_nav">
                <h2>购买流程</h2>
                <div class="step">
                    1.选号 <i class="i_help s1"></i><i class="i_help next"></i>
                    2.完善个人信息 <i class="i_help s2"></i><i class="i_help next"></i>
                    3.支付 <i class="i_help s3"></i><i class="i_help next"></i>
                    4.兑奖 <i class="i_help s4"></i>
                </div>
            </div>
            <div class="h_nav">
                <h2>彩种介绍</h2>
                <ul class="czintro cf">
                    <li class="pt0">
                        <i class="logos"></i>
                        <a href="/help/i-ssq-play.html"><b>双色球</b><br />2元可中1000万<br />每周二、四、日开奖</a>
                        <em></em>
                    </li>
                    <li class="pt0">
                        <i class="logos dlt"></i>
                        <a href="/help/i-dlt-play.html"><b>大乐透</b><br />大小奖两头火<br >每周一、三、六开奖</a>
                        <em></em>
                    </li>
                    <li class="pt0 br0">
                        <i class="logos qlc"></i>
                        <a href="/help/i-qlc-play.html"><b>七乐彩</b><br />2元可中500万<br />每周一、三、五开奖</a>
                        <em></em>
                    </li>
                    <li>
                        <i class="logos qxc"></i>
                        <a href="/help/i-qxc-play.html"><b>七星彩</b><br />2元可中500万<br />每周二、五、日开奖</a>
                        <em></em>
                    </li>
                    <li>
                        <i class="logos f14"></i>
                        <a href="/help/i-f14-play.html"><b>胜负彩</b><br />足球彩票，竞猜14场<br />最高奖500万</a>
                        <em></em>
                    </li>
                    <li class="br0">
                        <i class="logos f9"></i>
                        <a href="/help/i-f9-play.html"><b>任选9</b><br />足球彩票，竞猜9场<br />最高奖500万</a>
                        <em></em>
                    </li>
                    <li class="bb0">
                        <i class="logos k3"></i>
                        <a href="/help/i-k3gx-play.html"><b>新快3</b><br />每天78期<br />返奖率59%</a>
                        <em></em>
                    </li>
                    <li class="bb0">
                        <i class="logos k3"></i>
                        <a href="/help/i-k3js-play.html"><b>老快3</b><br />快乐猜大小<br />返奖率59%</a>
                        <em></em>
                    </li>
                    <li class="bb0 br0">
                        <i class="logos k3"></i>
                        <a href="/help/i-k3jl-play.html"><b>快3</b><br />每天79期<br />返奖率59%</a>
                        <em></em>
                    </li>
                </ul>
            </div>
            <div class="h_nav">
                <h2>功能指引</h2>
                <ul class="func_guide cf">
                    <li>
                        <b>投注</b>
                        <a href="/help/f-bet.html#bet01">投注流程</a> |
                        <a href="/help/f-bet.html#bet02">代购</a> |
                        <a href="/help/f-bet.html#bet04">单式</a><br />
                        <a href="/help/f-bet.html#bet05">复式</a> |
                        <a href="/help/f-bet.html#bet06">胆拖</a> |
                        <a href="/help/f-bet.html#bet03">机选</a> |
                        <a href="/help/f-bet.html#bet08">和值</a><br />
                        <a href="/help/f-bet.html#bet07">定胆杀号</a> |
                        <a href="/help/f-bet.html#bet09">过关方式</a>
                    </li>
                    <li>
                        <b>充值/支付</b>
                        <a href="/help/f-recharge.html#rec03">网上充值</a> |
                        <a href="/help/f-recharge.html#rec04">资金安全</a><br />
                        <a href="/help/f-pay.html#pay01">如何支付</a> |
                        <a href="/help/f-recharge.html#rec07">开通网上银行</a><br />
                        <a href="/help/f-pay.html#pay03">支付密码</a> |
                        <a href="/help/f-pay.html#pay05">修改支付密码</a>
                    </li>
                    <li>
                        <b>兑奖/提款</b>
                        <a href="/help/f-reward.html#rew01">如何兑奖</a> |
                        <a href="/help/f-reward.html#rew02">查询中奖</a><br />
                        <a href="/help/f-withdraw.html#wit01">提款须知</a> |
                        <a href="/help/f-withdraw.html#wit02">如何提款</a><br />
                        <a href="/help/f-withdraw.html#wit05">提款记录</a> |
                        <a href="/help/f-withdraw.html#wit04">提款不成功</a> 
                    </li>
                    <li style="border:none">
                        <b>身份信息</b>
                        <a href="/help/f-userinfo.html#use01">真实信息</a> |
                        <a href="/help/f-userinfo.html#use04">安全问题</a><br />
                        <a href="/help/f-userinfo.html#use03">手机号码</a> |
                        <a href="/help/f-userinfo.html#use05">我的昵称</a>
                    </li>
                </ul>
            </div>
            <div class="h_nav">
                <h2>热点问题</h2>
                <ul class="hot_ques cf">
                    <li class="br1">
                        <b>登录注册</b>
                        <a href="/help/f-register.html#reg03"><i></i>如何注册搜狗彩票用户？</a><br />
                        <a href="/help/p-all.html#hot02"><i></i>注册新用户收费吗？</a><br />
                        <a href="/help/p-all.html#hot03"><i></i>忘记登录密码怎么办？</a><br />
                        <a href="/help/p-all.html#hot04"><i></i>忘记用户名怎么办？</a>
                    </li>
                    <li>
                        <b>充值提款</b>
                        <a href="/help/f-recharge.html#rec01"><i></i>搜狗彩票有哪些充值方式？</a><br />
                        <a href="/help/f-recharge.html#rec02"><i></i>网上银行充值的优点有哪些？</a><br />
                        <a href="/help/f-withdraw.html#wit01"><i></i>提款须知及注意事项是什么？</a><br />
                        <a href="/help/f-withdraw.html#wit04"><i></i>我申请的提款为什么被驳回？</a>
                    </li>
                    <li class="bt1 br1">
                        <b>投注购彩</b>
                        <a href="/help/f-bet.html#bet01"><i></i>搜狗彩票的购彩流程是什么样？</a><br />
                        <a href="/help/f-bet.html#bet11"><i></i>在哪里查询我的投注记录？</a><br />
                        <a href="/help/f-bet.html#bet12"><i></i>在搜狗买彩票有没有时间限制？</a><br />
                        <a href="/help/f-bet.html#bet10"><i></i>限号是什么意思？</a>
                    </li>
                    <li class="bt1">
                        <b>中奖兑奖</b>
                        <a href="/help/f-reward.html#rew01"><i></i>我中奖了，怎么领到奖金？</a><br />
                        <a href="/help/f-reward.html#rew02"><i></i>怎么查询我的投注是否中奖？</a><br />
                        <a href="/help/f-reward.html#rew04"><i></i>怎么保护中奖者的个人信息？</a><br />
                    </li>
                    <em></em>
                </ul>
            </div>
		</div>
	</div>
</div>
<#include "../common/footer.ftl" />
</body>
</html>