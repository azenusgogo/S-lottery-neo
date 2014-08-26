<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#assign m2 = "qlc" />
<#assign seoTitle = "七乐彩玩法介绍-帮助中心-搜狗彩票" />
<#assign seoKeywords = "七乐彩玩法介绍" />
<#assign seoDescription = "“七乐彩”投注号码范围为01～30，每期从30个号码中开出7个基本号码和1个特别号码作为中奖号码，即是竞猜开奖号码中的7个基本号码和1个特别号码，顺序不限。每注金额人民币2元，每周销售三期。有单式、复式和胆拖三种投注类型。"/>
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
			<!--切换标签-->
			<div class="tab cf">
				<a href="/help/i-qlc-rule.html">玩法规则</a>
				<a href="/help/i-qlc-play.html" class="cur">玩法介绍</a>
				<a href="/help/i-qlc-award.html">奖项规则</a>
			</div>
			<div class="cont">
<!--内容.start-->
<p>
	“七乐彩”投注号码范围为01～30，每期从30个号码中开出7个基本号码和1个特别号码作为中奖号码，即是竞猜开奖号码中的7个基本号码和1个特别号码，顺序不限。每注金额人民币2元，每周销售三期。有单式、复式和胆拖三种投注类型。
</p>
<p class="top">
	<b>七乐彩单式投注：</b>通过自选或机选从01~30个号码中选择7个号码，组合为一注投注号码的投注，即为单式投注。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/qlc01.jpg" />
</p>
<p class="top">
	<b>七乐彩复试投注：</b>从01~30个号码中选择8个或以上号码，组合为多注投注号码的投注，即为复试投注。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/qlc02.jpg" />
</p>
<p class="top">
	<b>七乐彩胆拖投注：</b>从01-30个号码中选择您认为必出的号码（1~6个）作为每注都有的号码_胆码，再选择除胆码以外的号码作为拖码（胆码和拖码之和至少8个），由胆码和拖码组合成多注投注。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/qlc03.jpg" />
</p>
<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>