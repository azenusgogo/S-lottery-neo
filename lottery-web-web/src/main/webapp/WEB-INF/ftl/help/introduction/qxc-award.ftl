<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#assign m2 = "qxc" />
<#assign seoTitle = "七星彩奖项规则-帮助中心-搜狗彩票" />
<#assign seoKeywords = "七星彩奖项规则" />
<#assign seoDescription = "七星彩按当期销售额的50%、15%、35%分别计提返奖奖金、彩票发行费和彩票公益金。返奖奖金分为当期奖金和调节基金，其中，49%为当期奖金，1%为调节基金。高等奖奖金＝奖金总额－固定奖奖金。当奖池奖金超过8000万元(含)时，下期一等奖与二等奖奖金分配比例倒置，即：一等奖奖金总额为高等奖奖金的10%；二等奖奖金总额为高等奖奖金的90%。单注奖金封顶500万元。"/>
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
				<a href="/help/i-qxc-rule.html">玩法规则</a>
				<a href="/help/i-qxc-play.html">玩法介绍</a>
				<a href="/help/i-qxc-award.html" class="cur">奖项规则</a>
			</div>
			<div class="cont">
<!--内容.start-->
<p>
	<img src="${cdnBaseUrl}img/help/introduction/qxc03.jpg" />
</p>
<p class="top">
	注：
</p>
<p>
	1. 七星彩按当期销售额的50%、15%、35%分别计提返奖奖金、彩票发行费和彩票公益金。返奖奖金分为当期奖金和调节基金，其中，49%为当期奖金，1%为调节基金。
</p>
<p>
	2. 假设当期的开奖号码为1234567。
</p>
<p>
	3. 定位：定位指投注号码与开奖号码按位一致。示例：开奖号码为1234567，1234567则定位中7码，7651234则为不定位中7码。
</p>
<p>
	4. 连续：指投注号码连续N位号码与开奖号码的连续N位号码相符。示例：开奖号码为1234567，1234982则中连续中前4码，1284599虽然也按位中4码，但不连续。
</p>
<p>
	5. 高等奖奖金＝奖金总额－固定奖奖金。
</p>
<p>
	6. 当奖池奖金超过8000万元(含)时，下期一等奖与二等奖奖金分配比例倒置，即：一等奖奖金总额为高等奖奖金的10%；二等奖奖金总额为高等奖奖金的90%。
</p>
<p>
	7. 单注奖金封顶500万元。
</p>
<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>