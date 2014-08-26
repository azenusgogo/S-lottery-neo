<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#assign m2 = "f9" />
<#assign seoTitle = "任选九玩法介绍-帮助中心-搜狗彩票" />
<#assign seoKeywords = "任选九玩法介绍" />
<#assign seoDescription = "从14场比赛中任意选择9场比赛，每场比赛选择1种比赛结果为1注，每场比赛最多可选3种结果，单注投注金额2元奖金最高500万元！标准投注时可选择1～8场比赛结果作为胆码，其它比赛场次结果作为拖码进行胆拖投注，单注最高奖金500万元！" />
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
				<a href="/help/i-f9-rule.html">玩法规则</a>
				<a href="/help/i-f9-play.html" class="cur">玩法介绍</a>
				<a href="/help/i-f9-award.html">奖项规则</a>
			</div>
			<div class="cont">
<!--内容.start-->
<p>
	<b>开奖时间</b>：奖期截止后第二日开奖。开奖结果通过媒体向社会公布。
</p>
<p class="top">
	<b>玩法说明</b>：
</p>
<p>
	1、胜负彩任选9场玩法竟猜规定的14场比赛中任意9场比赛90分钟内(含伤停补时)的胜平负结果。
</p>
<p>
	2、竞猜某场比赛的主队获胜，请在该场比赛的选号区选择本场比赛的结果为“3”；竞猜主队与客队打平，请选择“1”；竞猜主队会负于客队，请选择“0”。
</p>
<p class="top">
	<b>标准投注</b>：从14场比赛中任意选择9场比赛，每场比赛选择1种比赛结果为1注，每场比赛最多可选3种结果，单注投注金额2元奖金最高500万元！标准投注时可选择1～8场比赛结果作为胆码，其它比赛场次结果作为拖码进行胆拖投注，单注最高奖金500万元！.
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/f901.jpg" />　
</p>
<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>