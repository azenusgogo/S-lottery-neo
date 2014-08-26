<#if !cdnBaseUrl??><#include "../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "contact" />
<#include "../common/header.ftl" />

<div class="help_main">
	<div class="sitepath">
		您所在的位置：<a href="/">搜狗彩票</a> &gt; 帮助中心
	</div>
	<div class="hcont cf" id="help_center">
		<div class="help_menu">
            <#include "include/menu.ftl" />
        </div>

		<div class="hright">
			<h2>客服联系方式</h2>
			<p class="contact">
				客服电话：010-5689 8998&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				客服邮箱：kfcp@sogou-inc.com
			</p>
			<h2>商务合作联系方式</h2>
			<p class="contact">
				商务电话：010-5689 8954&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				客服邮箱：swcp@sogou-inc.com
			</p>
			<h2>公司地址</h2>
			<p class="contact" style="border:none">
				北京市海淀区中关村东路1号搜狐网络大厦10层
			</p>
		</div>
	</div>
</div>
<#include "../common/footer.ftl" />
</body>
</html>