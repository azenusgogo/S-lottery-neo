<#include "../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/index-data.ftl" /></#if>
<#-- 定义本页功能 -->
<#assign func = "index" />
<#-- 引入header部分 -->
<#include "../common/header.ftl" />

<div class="wrapper home-sec">
	<div class="home-left">
		<div class="cp-list">
	            <div class="tit m2">数字彩</div>
	            <ul class="cf">
	            <#if commonGames??>
	            	<#list commonGames as x>
	            	<#if x??>
	                <li<#if !x_has_next> class="nbdr"</#if>><a class="${x.gameId}" href="${domainUrl}${x.gameId}/">${x.gameCn}<span>${x.desc}</span><#if (x.tag)??><div class="redWordsWraper"><span class="redWords" <#if x.tag==''>style="display:none;"</#if>><i class="arrowsIcon"></i>${x.tag!""}</span></div></#if></a></li>
	                </#if>
	                </#list>
	            </#if>
	            </ul>
	            <div class="tit">高频彩</div>
	            <ul class="cf">
	            <#if highFrequencyGames??>
	            	<#list highFrequencyGames as x>
	            	<#if x??>
	                <li<#if !x_has_next> class="nbdr"</#if>><a class="${x.gameId}" href="${domainUrl}${x.gameId}/">${x.gameCn}<span>${x.desc}</span></a></li>
	                </#if>
	                </#list>
	            </#if>
	            </ul>
	            <div class="tit">竞技彩</div>
	            <ul class="cf">
	            <#if traditionalSportGames??>
	            	<#list traditionalSportGames as x>
	            	<#if x??>
	                <li<#if !x_has_next> class="nbdr"</#if>><a class="${x.gameId}" href="${domainUrl}${x.gameId}/">${x.gameCn}<span>${x.desc}</span></a></li>
	                </#if>
	                </#list>
	            </#if>
	            </ul>
            </div>
		<div class="nav bobao">
			<h3>中奖播报</h3>
			<div class="scroll" id="news_slider">
				<ul>
				<#if recentAwardRecords?? && recentAwardRecords?size gt 0>
				<#list recentAwardRecords as x>
					<#if x??>
					<li><span class="cz">[${x.gameCn}]</span><span class="mj">${x.nickName}</span><em>${(x.amount/100)?c}元</em></li>
					</#if>
				</#list>
				</#if>
				</ul>
			</div>
		</div>
		<div class="nav tools" pbflag="wc_0_购彩工具">
			<h3>购彩工具</h3>
			<ul>
				<li><a href="${domainUrl}calculator/ssq/" class="anchor_hover"><i class="home-ico cp-ssq"></i><br />双色球计算器</a></li>
				<li><a href="${domainUrl}calculator/dlt/" class="anchor_hover"><i class="home-ico cp-dlt"></i><br />大乐透计算器</a></li>
			</ul>
		</div>
		<div class="nav safe">
			<h3>购彩保障</h3>
			<ul>
				<li class="home-ico"><b>账户安全可靠</b><span>登录、交易双重密码保护</span></li>
				<li class="home-ico dui"><b>兑奖安全方便</b><span>奖金自动打入搜狗彩票账户</span></li>
				<li class="home-ico tik"><b>提款安全快速</b><span>免费短信通知，1-2日到账</span></li>
			</ul>
		</div>
		<div class="nav help" pbflag="wc_0_帮助中心">
			<h3>帮助中心</h3>
			<ul>
				<#list helpList as h>
				<li><i class="cube"></i><a href="${h.url}" class="anchor_hover">${h.title}</a></li>
				</#list>
			</ul>
		</div>
	</div>
	<div class="home-right">
		<div class="kj-tip" id="kj-tip" pbflag="wc_0_开奖提醒">
			开奖提醒：
			<ul>
			<#if awardAnnouncements??>
				<#list awardAnnouncements as k>
				<li data-url="${k.gameId!''}">
				<b>${k.gameCn!"-"}</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				第 <b>${k.periodNo!"-"}</b> 期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<b>${k.prizeNumber?split(":")[0]}</b> 
				<#if k.prizeNumber?split(":")[1]??>
				<b class="blue">${k.prizeNumber?split(":")[1]}</b>
				</#if>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
				</li>
				</#list>
			</#if>
			</ul>
			<div class="tou">
				<a class="tou-btn" href="/${(awardAnnouncements[0].gameId)!'#'}/">我要投注</a>
			</div>
		</div>
		<div class="hr-sec1 cf">
			<div class="eye">
				<div class="qiepic">
					<div class="banner" id="index_focus" pbflag="wc_0_banner">
				        <ul>
				            <#list adsLinks![] as x>
							<#if (x.position!-1) == 0>
							<li><a href="${x.link!''}"><img alt="${x.title!''}" src="${x.image!''}" /></a></li>
							</#if>
							</#list>
				        </ul>
				    </div>
				</div>
				<div class="annoc" pbflag="wc_0_公告">
					<h3>站内公告</h3>
					<div class="box">
						<#if announcementMap??>
						<#assign key1 = announcementMap?keys[0] />
						<#assign key2 = announcementMap?keys[1] />
						<p><b>${key1!"-"}</b> | 
						<#list announcementMap[key1] as x>
						<a href="${x.link}">${x.title}</a>
						</#list></p>
						<p><b>${key2!"-"}</b> | 
						<#list announcementMap[key2] as x>
						<a href="${x.link}">${x.title}</a>
						</#list></p>
						</#if>
					</div>
				</div>
			</div>
			<div class="hr-nav1">
				<div class="limit">
					<div class="nav login" id="index_login" pbflag="wc_0_登录区">
						<div class="loading"></div>
						<h3>请登录</h3>
						<dl>
							<dd>账号：<input type="text" class="uname" tabindex="11" /><a class="index_reg_btn" href="#">免费注册</a></dd>
							<dd>密码：<input type="password" class="upwd" tabindex="12" /><a href="https://passport.sohu.com/web/RecoverPwdInput.action?ru=https:%2F%2Faccount.sogou.com">忘记密码</a></dd>
							<dd class="valid-nav">验证码：<input type="text" class="valid" tabindex="13" />
								<img src="${cdnBaseUrl}img/blank.gif" />
								<a href="#">换一张</a>
							</dd>
							<dd class="tip"></dd>
							<dd class="login-btn">
								<a href="#" class="hbtn submit">登&nbsp;&nbsp;录</a><a href="#" class="qqconnect">QQ登录</a>
							</dd>
						</dl>
					</div>
					<div class="nav guide" id="tab_dis" pbflag="wc_0_购彩教程">
						<div class="tab" id="tab_dis_t_1">
							<span class="cur">购彩教程</span>
							<span>如何领奖</span>
							<span>安全保障</span>
						</div>
						<div id="tab_dis_c_1" class="box" style="display:block">
							<div class="tab2 cf">
								<span class="cur">注册登录<i class="ct"></i></span>
								<span>充值购彩<i class="ct"></i></span>
								<span>中奖提款<i class="ct"></i></span>
							</div>
							<ul style="display:block">
								<li><i></i><a href="/help/f-register.html#reg03" class="anchor_hover">如何注册搜狗彩票账户？</a></li>
								<li><i></i><a href="/help/f-register.html#reg02" class="anchor_hover">如何使用第三方账号登录？</a></li>
							</ul>
							<ul>
								<li><i></i><a href="/help/f-recharge.html#rec01" class="anchor_hover">搜狗彩票有哪些充值方式？</a></li>
								<li><i></i><a href="/help/f-bet.html#bet01" class="anchor_hover">搜狗彩票的投注购彩流程</a></li>
							</ul>
							<ul>
								<li><i></i><a href="/help/f-reward.html#rew01" class="anchor_hover">中奖后如何兑奖？</a></li>
								<li><i></i><a href="/help/f-withdraw.html#wit01" class="anchor_hover">提款注意事项和提款流程</a></li>
							</ul>
						</div>
						<div id="tab_dis_c_2" class="box">
							<div class="tab2 cf">
								<span class="long cur">大奖如何领取<i class="ct"></i></span>
								<span class="long">小奖如何领取<i class="ct"></i></span>
							</div>
							<p style="display:block">双色球大乐透等头奖为大奖，中奖后搜狗彩票将电话通知您并说明领奖流程</p>
							<p>奖金直接派送到中奖者搜狗彩票账户内，用户可以继续购彩或提取奖金</p>
						</div>
						<div id="tab_dis_c_3" class="box">
							<div class="tab2 cf">
								<span class="cur">账户安全<i class="ct"></i></span>
								<span>购彩放心<i class="ct"></i></span>
								<span>提款快速<i class="ct"></i></span>
							</div>
							<ul style="display:block">
								<li><i></i><a href="/help/f-userinfo.html#use01" class="anchor_hover">为什么要填写真实信息？</a></li>
								<li><i></i><a href="/help/f-userinfo.html#use02" class="anchor_hover">真实信息填写错误怎么修改？</a></li>
							</ul>
							<ul>
								<li><i></i><a href="/help/f-reward.html#rew04" class="anchor_hover">在线购彩，我的信息会不会泄露？</a></li>
							</ul>
							<ul>
								<li><i></i><a href="/help/f-withdraw.html#wit01" class="anchor_hover">申请提款后，资金多久能到账？</a></li>
								<li><i></i><a href="/help/f-withdraw.html#wit03" class="anchor_hover">提款需要手续费吗？</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="hr-sec2 cf">
			<div class="ktou">
				<#if commonPeriods??>
				<div class="tounav" id="quick_bet_tab" pbflag="wc_0_数字彩快捷投注">				    
				    <#if quickBetTags??>
				       <#if quickBetTags["ssq"]?? && quickBetTags["ssq"] !="">
				             <em class="agile_tag pos_ssq"><i class="arrowsIcon"></i>${quickBetTags["ssq"]}</em>
				       </#if>
					   <#if quickBetTags["dlt"]?? && quickBetTags["dlt"] !="">
					         <em class="agile_tag pos_dlt"><i class="arrowsIcon"></i>${quickBetTags["dlt"]}</em>
					   </#if>
					   <#if quickBetTags["qlc"]?? && quickBetTags["qlc"] !="">
					         <em class="agile_tag pos_qlc"><i class="arrowsIcon"></i>${quickBetTags["qlc"]}</em>
					   </#if>
					   <#if quickBetTags["qxc"]?? && quickBetTags["qxc"] !="">
					         <em class="agile_tag pos_qxc"><i class="arrowsIcon"></i>${quickBetTags["qxc"]}</em>
				       </#if>
				    </#if>
					<h2>数字彩 快捷投注</h2>
					<div class="cptab">
						<#list commonPeriods as t>
						<#if t??>
						<a href="/${t.gameId}/"<#if t_index == 0> class="cur"</#if>>${t.gameCn}</a>
						</#if>
						</#list>
					</div>
					<#list commonPeriods as x>
					<#if x??>
					<#assign pool = (x.bonusPool!"0_0_0:0")?split(":")[0] />
					<#assign yiwan = ["亿","万","元"]>
					<div class="cpret shuzi"<#if x_index == 0> style="display:block"</#if>>
						<form action="${domainUrl}${x.gameId}/" method="post">
                            <input type="hidden" name="quickBetNumber">
                        </form>
						<div class="info">
							<span><b>${x.gameCn!"-"}</b>&nbsp;&nbsp;
							第<b>${x.periodNo!"-"}</b>期</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<#if pool?? && pool != "0_0_0">
							奖池：<em><b>
							<#list pool?split("_") as m>
							<#if m?number gt 0>
							${m} ${yiwan[m_index]}
							</#if>
							</#list>
							</b></em>
							</#if>
						</div>
						<div class="ball">
							<div class="ball-num">
							<#if x.gameId = "ssq">
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="33" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="33" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="33" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="33" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="33" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="33" type="text" value="" /></i>
								<i class="home-ico blue"><input pattern="[0-9]{2}" maxlength="2" data-max="16" type="text" value="" /></i>
							<#elseif x.gameId = "dlt">
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="35" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="35" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="35" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="35" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="35" type="text" value="" /></i>
								<i class="home-ico blue"><input pattern="[0-9]{2}" maxlength="2" data-max="12" type="text" value="" /></i>
								<i class="home-ico blue"><input pattern="[0-9]{2}" maxlength="2" data-max="12" type="text" value="" /></i>
							<#elseif x.gameId = "qlc">
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="30" type="text" value="" /></i>
							<#elseif x.gameId = "qxc">
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
								<i class="home-ico"><input pattern="[0-9]{2}" maxlength="2" data-max="9" type="text" value="" /></i>
							</#if>
							</div>
							<div class="go">
								<a href="#" class="home-tou-btn">立即投注</a>
								<a href="#" class="change">换一注</a>
							</div>
						</div>
						<div class="time">
							<span>截止时间还有：<em data-endTime="${x.endTime?long?c}" data-gameId="${x.gameId!''}"></em></span>
							开奖时间：${x.offcialAwardTime?string("yyyy年MM月dd日 HH:mm")}
							<#if x.todayOpen!false && x.todayOpen><i>今日开奖</i></#if>
						</div>
					</div>
					</#if>
					</#list>
				</div>
				</#if>
				
				<#if highFrequencyPeriods?? && highFrequencyPeriods?size gt 0>
				<div class="tounav" id="quick_bet_tab2" pbflag="wc_0_高频彩快捷投注">
					<#assign k3 = highFrequencyPeriods[0]!"" />
					<#assign k3tabs = "和值,三同号通选,三同号单选,三不同号,三连号通选,二同号复选,二同号单选,二不同号" />
					<#list k3tabs?split(",") as k>
	                    <#if k == k3.hotPlayMethod>
	                    <#assign tab_index = "#" + k_index />
	                    </#if>
	                </#list>
					<h2>高频彩 快捷投注</h2>
					<div class="cptab">
						<a href="/${k3.gameId}/" class="cur">${k3.gameCn!"-"}</a>
					</div>
					<div class="cpret">
						<form action="${domainUrl}${k3.gameId}/" method="post">
                            <input type="hidden" name="quickBetNumber">
                        </form>
						<div class="info">
							<span><b>${k3.gameCn!"-"}</b>&nbsp;&nbsp;
							第<b>${k3.periodNo!"-"}</b>期</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							[${k3.hotPlayMethod}玩法]
						</div>
						<div class="ball">
							<div class="k3ball">
								<div class="loading"></div>
							</div>
							<div class="go">
								<a href="#" class="home-tou-btn">立即投注</a>
								<a href="#" class="change">换一注</a>
							</div>
						</div>
						<div class="time">
							<span>截止时间还有：<em data-endTime="${k3.endTime?long?c}" data-gameId="${k3.gameId!''}"></em></span>
							开奖时间：每十分钟一期
						</div>
					</div>
				</div>
				</#if>
			</div>
			<div class="nav kjret" pbflag="wc_0_开奖公告区">
				<h3><a class="more" href="${domainUrl}kaijiang/">更多&gt;&gt;</a>彩票开奖</h3>
				<#if awardAnnouncements?? && awardAnnouncements?size gt 0>
					<#list awardAnnouncements as x>
					<#if x??>
					<div class="retbox">
						<b>${x.gameCn!"-"}</b> 第${x.periodNo!"-"}期 （${x.openAwardDate!"-"}）<br />
						<span class="ball">
							<em class="red">${x.prizeNumber?split(":")[0]}</em>
							<#if x.prizeNumber?split(":")[1]??> + <em class="blue">${x.prizeNumber?split(":")[1]}</em></#if>
						</span><br />
						<span class="link">
							<#if "ssq,dlt"?contains(x.gameId)><a href="${domainUrl}calculator/${x.gameId}/" class="anchor_hover">奖金计算器</a> | </#if><a href="${domainUrl}${x.gameId!""}/" class="anchor_hover">投注</a>
						</span>
					</div>
					</#if>
					</#list>
				</#if>
			</div>
		</div>
		<div class="banr">
			<#list adsLinks![] as x>
			<#if (x.position!-1) == 1>
			<a href="${x.link!''}"><img alt="${x.title!''}" src="${x.image!''}" /></a>
			</#if>
			</#list>
		</div>
		<div class="ticai" pbflag="wc_0_体彩快捷投注">
			<div class="tounav">
				<h2>体彩 快捷投注</h2>
				<div class="cptab" id="ftab">
					<a target="ticai" href="/iframe/index/football/f14.html" class="cur">胜负彩</a>
					<a target="ticai" href="/iframe/index/football/f9.html">任选九</a>
				</div>
			</div>
			<iframe src="/iframe/index/football/f14.html" scrolling="no" class="home-frame" name="ticai" width="748" height="695" frameborder="0"></iframe>
		</div>
	</div>
</div>
<input type="hidden" id="serverTime" value="${.now?long?c}" />
<#include "../common/footer.ftl" />
</body>
</html>