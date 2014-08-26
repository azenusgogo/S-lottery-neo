<#if awardList?? && awardList?size gt 0>
<div class="jjc-cont" id="jjc_cont">
	<h2>
		<select class="h3r">
			<#list awardList as x>
			<#if (x.award)??>
			<option value="${x_index}_${x.award.periodNo}">${x.award.periodNo!}期</option>
			</#if>
			</#list>
		</select>
		<b>开奖信息</b>
	</h2>
<#list awardList as i>
    <div class="jjc-info-area"<#if i_index == 0> style="display:block"</#if>>
    <#if (i.award.prizeNumberDetail)?? && (i.award.prizeNumberDetail) != "">
    <#assign zl = i.award.prizeNumberDetail />
		<table class="kj-info" width="250" cellspacing="0">
			<tbody>
			<#list [0,1] as m>
			<tr>
				<#list zl?split(";") as x>
		        <#if x?? && x!= "">
				<td>${x?split(",")[m]}</td>
		        </#if>
				</#list>
			</tr>
			</#list>
			</tbody>
			<tbody class="bifen">
				<#list zl?split(";") as x>
				<#if x?? && x!= "">
				<td>${x?split(",")[2]}</td>
				</#if>
				</#list>
			</tbody>
		</table>
	</#if>
	<#if (i.award.bonusLevelDetail)?? && (i.award.bonusLevelDetail) != "">
		<#assign tcjj = i.award.bonusLevelDetail?split(";") />
		<div class="kj-info-btm">
			<span><i class="dot-cube"></i>一等奖：<em>${tcjj[0]?split("_")[1]}</em>注，每注<em>${tcjj[0]?split("_")[2]?number/100}</em>元</span>
			<#if game.gameId == "f14">
			<span><i class="dot-cube"></i>二等奖：<em>${tcjj[1]?split("_")[1]}</em>注，每注<em>${tcjj[1]?split("_")[2]?number/100}</em>元</span>
			</#if>
			<#if (i.totalSales)?? && (i.totalSales) gt 0>
			<em>　当期销售额<b>${i.totalSales/100}</b>元</em>
			</#if>
		</div>
	</#if>
    </div>
</#list>
</div>
</#if>
<#if matchPointRank?? && matchPointRank?size gt 0>
<div class="jjc-cont">
   <h2><b>联赛积分榜</b></h2>
   <div class="ls-fen cf" id="jf_tab">
       <span class="cur">英超<i class="ct"></i></span>
       <span>意甲<i class="ct"></i></span>
       <span>西甲<i class="ct"></i></span>
       <span>德甲<i class="ct"></i></span>
       <span>法甲<i class="ct"></i></span>
   </div>
	<#list matchPointRank as v_table>
		<table width="270" class="ls-fen-tbl"<#if v_table_index == 0> style="display: table"</#if>>
		    <tr>
		        <th>排名</th>
		        <th colspan="2" width="95">队名</th>
		        <th>赛</th>
		        <th>胜</th>
		        <th>平</th>
		        <th>负</th>
		        <th class="tdr">积分</th>
		    </tr>
		<#if v_table?? && v_table?size gt 0>
		    <#list v_table as item>		            
		    <#if item??>
		    <tr<#if item_index %2 == 0> class="bg"</#if>>
				<td>${item.rank!"-"}</td>
				<td class="team" width="75">
					<a href="javascript:void(0)">${item.team!"-"}</a>
				</td>
				<td class="ret" width="20">
					<#if item.trend == 1>
					<span class="win">&uarr;</span>
					<#elseif item.trend == 2>
					&rarr;
					<#elseif item.trend == 3>
					<span class="lose">&darr;</span>
					</#if>
				</td>
				<td>${item.screen!"-"}</td>
				<td>${item.winCount!"-"}</td>
				<td>${item.evenCount!"-"}</td>
				<td>${item.negativeCount!"-"}</td>
				<td class="tdr">${item.point!"-"}</td>
			</tr>
			</#if>
			</#list>
		</#if>
		</table>
	  </#list>
</div>
</#if>
<div class="jjc-cont">
   <h2><b>${game.gameCn!''}玩法介绍</b></h2>
   <ul class="jjc-list">
       <li><i class="dot-cube"></i><a href="/help/i-${game.gameId}-rule.html">${game.gameCn}玩法规则说明</a></li>
       <li><i class="dot-cube"></i><a href="/help/i-${game.gameId}-play.html">${game.gameCn}玩法介绍</a></li>
       <li><i class="dot-cube"></i><a href="/help/i-${game.gameId}-award.html">${game.gameCn}奖项规则</a></li>
   </ul>
</div>
<div class="jjc-cont">
    <h2><b>帮助中心</b></h2>
    <ul class="jjc-list">
        <#list helpList as h>
        <li><i class="dot-cube"></i><a class="anchor_hover" href="${h.url}">${h.title}</a></li>
        </#list>
    </ul>
</div>