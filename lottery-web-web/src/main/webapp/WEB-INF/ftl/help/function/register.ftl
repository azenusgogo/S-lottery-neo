<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "f" />
<#assign m2 = "register" />
<#assign seoTitle = "注册-帮助中心-搜狗彩票" />
<#assign seoKeywords = "注册" />
<#assign seoDescription = "1. 在搜狗彩票首页（cp.sogou.com），点击右侧的“免费注册”；2. 在注册浮层中填写用户名、密码等注册信息，填写无误后点击“注册”；3. 注册成功后您可以马上充值购彩，请尽快完善您的个人信息，提升账户安全级别。"/>
<#include "../../common/header.ftl" />

<div class="help_main">
	<div class="sitepath">
		您所在的位置：<a href="/">搜狗彩票</a> &gt; 帮助中心
	</div>
	<div class="hcont cf" id="help_center">
		<div class="help_menu">
			<#include "../include/menu.ftl" />
		</div>

		<div class="hright">
			<div class="cont" style="display:block;">
<!--内容.start-->
<p id="reg01">
	<b>搜狗、搜狐网用户</b>
</p>
<p>
	如果您已有搜狗、搜狐帐号，您直接登录搜狗彩票就可以购彩，无需单独注册。
</p>
<p class="top" id="reg02">
	<b>腾讯QQ用户</b>
</p>
<p>
	如果您已有腾讯QQ帐号，您可以选择QQ登录（合作账号）。成功登录搜狗彩票就可以购彩，无需单独注册。
</p>
<p class="top" id="reg03">
	<b>新用户注册</b>
</p>
<p>
	1. 在搜狗彩票首页（<span><a href="http://cp.sogou.com/">cp.sogou.com</a></span>），点击右侧的“免费注册”；
</p>
<p>
	<img src="${cdnBaseUrl}img/help/function/help_reg01.jpg" /> 
</p>
<p>
	2. 在注册浮层中填写用户名、密码等注册信息，填写无误后点击“注册”；
</p>
<p>
	<img src="${cdnBaseUrl}img/help/function/help_reg02.jpg" /> 
</p>
<p>
	3. 注册成功后您可以马上充值购彩，请尽快完善您的个人信息，提升账户安全级别。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/function/help_reg03.jpg" /> 
</p>
<p class="top" id="reg04">
	<b>注册搜狗账号注意事项</b>
</p>
<p>
	1. 请务必正确填写真实姓名、身份证号码、开户行和银行卡号码。真实姓名、身份证号作为提款、重置密码等凭证，用户不能自行修改。
</p>
<p>
	2. 银行卡资料仅作提款使用，提款前请务必填写详细。真实姓名必须与银行卡户名相同，否则提款将不成功。
</p>
<p>
	3. 请填写真实有效的手机号码，以便收取手机验证码以及在您中大奖时能及时与您取得联系。
</p>
<p>
4. 注册搜狗账号是完全免费的，不收取任何费用。
</p>
<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>