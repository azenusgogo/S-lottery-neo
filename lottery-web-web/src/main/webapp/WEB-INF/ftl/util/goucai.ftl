<#include "../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/extra.ftl" /></#if>
<#-- 本页标识 -->
<#assign func = "goucai" />
<#-- 引入header部分 -->
<#assign seoTitle="网购彩票_网上彩票投注_网上购彩平台 – 搜狗彩票">
<#assign seoDescription="购彩大厅为您提供网购彩票、网上彩票投注、彩票代购等服务。搜狗彩票是一个安全、稳定、有信誉保证的网上购彩平台。">
<#assign seoKeywords="网购彩票,彩票投注,网上彩票投注,网上购彩">
<#include "../common/header.ftl" />

<div class="wrapper hall">
    <div class="row cf">
        <div class="row-left" id="goucaiHall">
        	<div class="sitepath">
		        您当前的位置：<a href="${domainUrl}">彩票首页</a> &gt; 购彩大厅
		    </div>
            <h2><b>福彩、体彩</b></h2>
            <ul class="gc-list cf">
				<li pbflag="wc_0_双色球">
					<div class="infoWrapper">
					  <div class="info">
						<i class="logos"></i>
						<a href="${domainUrl}ssq/">双色球</a>
						<span>最受欢迎，<em>2</em>元可中<em>1000</em>万</span>						
					  </div>
					</div>
					<#if gameTags??>
						   <#if gameTags["ssq"]?? && gameTags["ssq"] !=""><em class="gc_tag">${gameTags["ssq"]}</em></#if>
					</#if>
					<div class="go">
						<b>双色球</b><#if todayOpenAwardGameId?? && todayOpenAwardGameId?contains("ssq")><i>今日开奖</i></#if><br />
						<em>最受欢迎，2元可中1000万</em><br />
						<span>每周二、四、日开奖</span>
						<p>
							<a href="${domainUrl}ssq/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
				<li pbflag="wc_0_超级大乐透">
					<div class="infoWrapper">
						<div class="info">
							<i class="logos cp-dlt"></i>
							<a href="${domainUrl}dlt/">超级大乐透</a>
							<span>好玩易中，<em>3</em>元可中<em>2400</em>万</span>						
						</div>
					 </div>
				     <#if gameTags??>
						   <#if gameTags["dlt"]?? && gameTags["dlt"] !=""><em class="gc_tag">${gameTags["dlt"]}</em></#if>
				     </#if>
				     <div class="go">
						<b>大乐透</b><#if todayOpenAwardGameId?? && todayOpenAwardGameId?contains("dlt")><i>今日开奖</i></#if><br />
						<em>奖金丰厚，大小奖两头火</em><br />
						<span>每周一、三、六开奖</span>
						<p>
							<a href="${domainUrl}dlt/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
				<li pbflag="wc_0_七乐彩">
				    <div class="infoWrapper">
						<div class="info">
							<i class="logos cp-qlc"></i>
							<a href="${domainUrl}qlc/">七乐彩</a>
							<span>大奖<em>500</em>万，每周一、三、五开奖</span>							
						</div>
				    </div>
				    <#if gameTags??>
						   <#if gameTags["qlc"]?? && gameTags["qlc"] !=""><em class="gc_tag">${gameTags["qlc"]}</em></#if>
				    </#if>
					<div class="go">
						<b>七乐彩</b><#if todayOpenAwardGameId?? && todayOpenAwardGameId?contains("qlc")><i>今日开奖</i></#if><br />
						<em>30选7，2元可中500万</em><br />
						<span>每周一、三、五开奖</span>
						<p>
							<a href="${domainUrl}qlc/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
				<li pbflag="wc_0_七星彩">
				   <div class="infoWrapper">
					<div class="info">
						<i class="logos cp-qxc"></i>
						<a href="${domainUrl}qxc/">七星彩</a>
						<span>大奖<em>500</em>万，每周二、五、日开奖</span>						
					</div>
					</div>
					<#if gameTags??>
						   <#if gameTags["qxc"]?? && gameTags["qxc"] !=""><em class="gc_tag">${gameTags["qxc"]}</em></#if>
					</#if>
					<div class="go">
						<b>七星彩</b><#if todayOpenAwardGameId?? && todayOpenAwardGameId?contains("qxc")><i>今日开奖</i></#if><br />
						<em>简单易中，2元可中500万</em><br />
						<span>每周二、五、日开奖</span>
						<p>
							<a href="${domainUrl}qxc/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
			</ul>
            <h2><b>高频彩</b></h2>
            <ul class="gc-list cf">
				<li pbflag="wc_0_快3">
				   <div class="infoWrapper">
					<div class="info">
						<i class="logos cp-k3"></i>
						<a href="${domainUrl}k3jl/">快3</a>
						<span>10分钟一期，返奖率59%</span>
						
					</div>
					</div>
					<#if gameTags??>
						   <#if gameTags["k3jl"]?? && gameTags["k3jl"] !=""><em class="gc_tag">${gameTags["k3jl"]}</em></#if>
					</#if>
					<div class="go">
						<b>快3</b><i>今日开奖</i><br />
						<em>10分钟1期，返奖率59%</em>
						<p>
							<a href="${domainUrl}k3jl/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
				<li pbflag="wc_0_新快3">
				    <div class="infoWrapper">
						<div class="info">
							<i class="logos cp-k3"></i>
							<a href="${domainUrl}k3gx/">新快3</a>
							<span>10分钟1期，每天78期</span>							
						</div>
					</div>
					<#if gameTags??>
							   <#if gameTags["k3gx"]?? && gameTags["k3gx"] !=""><em class="gc_tag">${gameTags["k3gx"]}</em></#if>
					</#if>
					<div class="go">
						<b>新快3</b><i>今日开奖</i><br />
						<em>10分钟1期，每天78期</em>
						<p>
							<a href="${domainUrl}k3gx/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
				<li pbflag="wc_0_老快3">
				    <div class="infoWrapper">
						<div class="info">
							<i class="logos cp-k3"></i>
							<a href="${domainUrl}k3js/">老快3</a>
							<span>10分钟1期，快乐猜大小</span>						
						</div>
				    </div>
				    <#if gameTags??>
						   <#if gameTags["k3js"]?? && gameTags["k3js"] !=""><em class="gc_tag">${gameTags["k3js"]}</em></#if>
					</#if>
					<div class="go">
						<b>老快3</b><i>今日开奖</i><br />
						<em>10分钟1期，快乐猜大小</em>
						<p>
							<a href="${domainUrl}k3js/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
			</ul>
			<h2><b>体育竞彩</b></h2>
			<ul class="gc-list cf">
				<li pbflag="wc_0_胜负彩">
				   <div class="infoWrapper">
					<div class="info">
						<i class="logos cp-f14"></i>
						<a href="${domainUrl}f14/">胜负彩</a>
						<span>14场猜胜负，<em>最高奖500</em>万</span>						
					</div>
					</div>
					<#if gameTags??>
						   <#if gameTags["f14"]?? && gameTags["f14"] !=""><em class="gc_tag">${gameTags["f14"]}</em></#if>
					</#if>
					<div class="go">
						<b>胜负彩</b><i>今日开奖</i><br />
						<em>足球彩票，竞猜14场</em><br />
						<span>最高奖500万</span>
						<p>
							<a href="${domainUrl}f14/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
				<li pbflag="wc_0_任选九">
				  <div class="infoWrapper">
					<div class="info">
						<i class="logos cp-f9"></i>
						<a href="${domainUrl}f9/">任选九场</a>
						<span>难度简化猜9场，<em>最高奖500</em>万</span>						
					</div>
					</div>
					<#if gameTags??>
						   <#if gameTags["f9"]?? && gameTags["f9"] !=""><em class="gc_tag">${gameTags["f9"]}</em></#if>
					</#if>
					<div class="go">
						<b>任选九场</b><i>今日开奖</i><br />
						<em>足球彩票，竞猜9场</em><br />
						<span>最高奖500万</span>
						<p>
							<a href="${domainUrl}f9/" class="fbtn">立即购彩</a>
							<!-- <br />
							<a href="">开奖公告</a>
							<a href="">机选包月</a>
							<a href="">走势图</a> -->
						</p>
					</div>
				</li>
			</ul>
        </div>
        <div class="row-right">
            <h2><b>大奖奖池</b></h2>
            <div class="cpinfo-nav" pbflag="wc_0_大奖奖池">
            	<dl>
					<#if (dltAward.award)?? && (dltAward.period)??>
					<dd>
						<p><a href="${domainUrl}dlt/">大乐透</a><span>${dltAward.period.offcialAwardTime?string("MM月dd日")}</span>第${dltAward.period.periodNo}期</p>
						<div class="kj-num">
							<#if dltAward.award.prizeNumber??>
							<#assign dlt_num = dltAward.award.prizeNumber?split(":")!"" />
								<#list dlt_num[0]?split(" ") as m>
								<i>${m}</i>
								</#list>
								<#list dlt_num[1]?split(" ") as n>
								<i class="blue-ball-min">${n}</i>
								</#list>
							</#if>
						</div>
						<div class="intro">
							<#if dltAward.award.bonusPool??>
							奖&nbsp;&nbsp;&nbsp;池：<em>${(dltAward.award.bonusPool/10000000000)?string("0.00")}</em>亿<br />
							</#if>
							<#if dltAward.award.bonusLevelDetail??>
							<#assign dlt_award = dltAward.award.bonusLevelDetail?split(";")[0]?split("_")[2] />
							一等奖：<em>${(dlt_award?number/1000000)?string("0")}</em>万
							</#if>
							<a href="${domainUrl}dlt/" class="buybtn">购买彩票</a>
						</div>
					</dd>
					</#if>
            		<#if ssqAward?? && ssqAward.period?? && ssqAward.award??>
					<dd>
						<p><a href="${domainUrl}ssq/">双色球</a><span>${ssqAward.period.offcialAwardTime?string("MM月dd日")}</span>第${ssqAward.period.periodNo}期</p>
						<div class="kj-num">
							<#if ssqAward.award.prizeNumber??>
							<#assign ssq_num = ssqAward.award.prizeNumber?split(":")!"" />
								<#list ssq_num[0]?split(" ") as m>
								<i>${m}</i>
								</#list>
								<i class="blue-ball-min">${ssq_num[1]}</i>
							</#if>
						</div>
						<div class="intro">
							<#if ssqAward.award.bonusPool??>
							奖&nbsp;&nbsp;&nbsp;池：<em>${(ssqAward.award.bonusPool/10000000000)?string("0.00")}</em>亿<br />
							</#if>
							<#if ssqAward.award.bonusLevelDetail??>
							<#assign ssq_award = ssqAward.award.bonusLevelDetail?split(";")[0]?split("_")[2] />
							一等奖：<em>${(ssq_award?number/1000000)?string("0")}</em>万
							</#if>
							<a href="${domainUrl}ssq/" class="buybtn">购买彩票</a>
						</div>

					</dd>
					</#if>
					<#if qxcAward?? && qxcAward.award?? && qxcAward.period??>
            		<dd>
            			<p><a href="${domainUrl}qxc/">七星彩</a><span>${qxcAward.period.offcialAwardTime?string("MM月dd日")}</span>第${qxcAward.period.periodNo}期</p>
            			<div class="kj-num">
            				<#if qxcAward.award.prizeNumber??>
            				<#list qxcAward.award.prizeNumber?split(" ") as x>
							<i>${x}</i>
							</#list>
							</#if>
						</div>
						<div class="intro">
							<#if qxcAward.award.bonusPool??>
							奖&nbsp;&nbsp;&nbsp;池：<em>${(qxcAward.award.bonusPool/10000000000)?string("0.00")}</em>亿<br />
							</#if>
							<#if qxcAward.award.bonusLevelDetail??>
							<#assign ssq_award = qxcAward.award.bonusLevelDetail?split(";")[0]?split("_")[2] />
							一等奖：<em>${(ssq_award?number/1000000)?string("0")}</em>万
							</#if>
							<a href="${domainUrl}qxc/" class="buybtn">购买彩票</a>
						</div>
            		</dd>
            		</#if>
            	</dl>
            </div>
            <h2><b>购彩承诺</b></h2>
            <ul class="gccn-nav">
            	<li><i class="i1"></i><b>账户安全可靠</b><br />登录、交易双重密码保护</li>
            	<li><i class="i2"></i><b>兑奖安全方便</b><br />奖金自动打入搜狗彩票账户</li>
            	<li><i class="i3"></i><b>提款安全快速</b><br />免费短信通知，1-2日到账</li>
            </ul>
            <h2><a href="/help/" class="h3r">更多</a><b>帮助中心</b></h2>
            <ul class="fc-list">
            	<#list helpList as h>
				<li><a href="${h.url}" class="anchor_hover">${h.title}</a></li>
				</#list>
            </ul>
        </div>
    </div>
</div>
<#include "../common/footer.ftl" />
</body>
</html>