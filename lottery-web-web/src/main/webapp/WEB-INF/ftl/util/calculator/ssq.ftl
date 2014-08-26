<#include "../../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#-- 本页标识 -->
<#assign func = "calcu" />
<#assign seoTitle = "双色球奖金计算器 – 搜狗彩票" />
<#assign seoKeywords = "双色球投注，福利彩票双色球投注，网上购买双色球" />
<#assign seoDescription = "搜狗彩票提供中国福利彩票双色球投注，双色球开奖结果，走势图及专家预测分析服务。网上购买双色球，首选搜狗彩票"/>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#if awardWithPeriodList[0]?? && awardWithPeriodList[0].award?? && awardWithPeriodList[0].award.bonusLevelDetail??>
<#assign cash = awardWithPeriodList[0].award.bonusLevelDetail />
</#if>

<div class="wrapper t-main">
	<div class="t-left">
		<h2><b>奖金计算器</b></h2>
		<ul>
			<li><a href="${domainUrl}calculator/ssq/" class="cur"><i class="logos ssq-logo"></i>双色球<em></em></a></li>
			<li><a href="${domainUrl}calculator/dlt/"><i class="logos dlt-logo"></i>大乐透</a></li>
		</ul>
	</div>
	<div class="t-right">
		<h1><b>双色球奖金计算器</b></h1>
        <div class="funtab">
            <a href="#" class="cur">按中奖个数计算奖金</a>
            <a href="#">投注号码按期次查询</a>
        </div>
		<div class="t-box" style="display:block" id="ssq_award">
			<div class="t-sel">
				<p>
					<b>查询期数：</b>
					<select class="d_period">
                        <#list awardWithPeriodList![] as x>
                        <#if x.period??>
						<option value="${x_index}">${x.period.periodNo!"-"}</option>
                        </#if>
                        </#list>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<b>开奖号码：
                    <#list awardWithPeriodList![] as x>
                    <#if x?? && x.award?? && x.award.prizeNumber??>
                    <#assign betNo = x.award.prizeNumber />
                    <span class="bet_num"<#if x_index == 0> style="display:inline"</#if>>
                        <#list betNo?split(":") as m>
                            <#list m?split(" ") as n>
                            <#if m_index == 0>
                            <i>${n}</i>
                            <#else>
                            <i class="blue">${n}</i>
                            </#if>
                            </#list>
                        </#list>
                    </span>
                    </#if>
                    </#list>
                    </b>
				</p>
				<p>
					<b>投注类型：</b>
					<label><input type="radio" class="d_type" value="0" name="type" checked /> 普通投注</label> 
					<label><input type="radio" class="d_type" value="1" name="type" /> 胆拖投注</label>
				</p>
                <div class="tz_type" style="display:block">
				<p>
					<b>我的投注：</b>
					红球
					<select class="s_red">
                        <option value="6">6个</option>
                        <option value="7">7个</option>
                        <option value="8">8个</option>
                        <option value="9">9个</option>
                        <option value="10">10个</option>
                        <option value="11">11个</option>
                        <option value="12">12个</option>
                        <option value="13">13个</option>
                        <option value="14">14个</option>
                        <option value="15">15个</option>
                        <option value="16">16个</option>
                        <option value="17">17个</option>
                        <option value="18">18个</option>
                        <option value="19">19个</option>
                        <option value="20">20个</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
					蓝球
					<select class="s_blue">
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4">4个</option>
                        <option value="5">5个</option>
                        <option value="6">6个</option>
                        <option value="7">7个</option>
                        <option value="8">8个</option>
                        <option value="9">9个</option>
                        <option value="10">10个</option>
                        <option value="11">11个</option>
                        <option value="12">12个</option>
                        <option value="13">13个</option>
                        <option value="14">14个</option>
                        <option value="15">15个</option>
						<option value="16">16个</option>
					</select>
				</p>
				<p>
					<b>投注金额：</b>
					共计<em><b class="zhu_stat">1</b></em>注，<em><b class="yuan_stat">2</b></em>元
				</p>
				<p>
					<b>我的命中：</b>
					红球
					<select class="t_red">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4">4个</option>
                        <option value="5">5个</option>
                        <option value="6">6个</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
					蓝球
					<select class="t_blue">
                        <option value="0">0个</option>
						<option value="1">1个</option>
					</select>
				</p>
                </div>
                <div class="tz_type">
                <p>
                    <b>我的投注：</b>
                    红球胆码
                    <select class="s_red_dm dt_red">
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3" selected>3个</option>
                        <option value="4">4个</option>
                        <option value="5">5个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    红球拖码
                    <select class="s_red dt_red">
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3" selected>3个</option>
                        <option value="4">4个</option>
                        <option value="5">5个</option>
                        <option value="6">6个</option>
                        <option value="7">7个</option>
                        <option value="8">8个</option>
                        <option value="9">9个</option>
                        <option value="10">10个</option>
                        <option value="11">11个</option>
                        <option value="12">12个</option>
                        <option value="13">13个</option>
                        <option value="14">14个</option>
                        <option value="15">15个</option>
                        <option value="16">16个</option>
                        <option value="17">17个</option>
                        <option value="18">18个</option>
                        <option value="19">19个</option>
                        <option value="20">20个</option>
                        <option value="21">21个</option>
                        <option value="22">22个</option>
                        <option value="23">23个</option>
                        <option value="24">24个</option>
                        <option value="25">25个</option>
                        <option value="26">26个</option>
                        <option value="27">27个</option>
                        <option value="28">28个</option>
                        <option value="29">29个</option>
                        <option value="30">30个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    蓝球
                    <select class="s_blue">
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4">4个</option>
                        <option value="5">5个</option>
                        <option value="6">6个</option>
                        <option value="7">7个</option>
                        <option value="8">8个</option>
                        <option value="9">9个</option>
                        <option value="10">10个</option>
                        <option value="11">11个</option>
                        <option value="12">12个</option>
                        <option value="13">13个</option>
                        <option value="14">14个</option>
                        <option value="15">15个</option>
                        <option value="16">16个</option>
                    </select>
                </p>
                <p>
                    <b>投注金额：</b>
                    共计<em><b class="zhu_stat">1</b></em>注，<em><b class="yuan_stat">2</b></em>元
                </p>
                <p>
                    <b>我的命中：</b>
                    红球胆码
                    <select class="t_red_dm">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    红球拖码
                    <select class="t_red">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    蓝球
                    <select class="t_blue">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                    </select>
                </p>
                </div>
			</div>

            <form action="${domainUrl}ssq/" method="post" id="try">
                <input type="hidden" name="quickBetNumber" value="" id="tryBet" />
            </form>

			<div class="t-btns">
				<a href="javascript:;" rel="nofollow" class="fbtn">计算奖金</a>
				<a href="javascript:;" class="fbtn" id="nowTry">试试手气</a>
			</div>
			<div class="t-ret">您中奖金额（税前）是：<b>0</b>元</div>
			<table class="t-intro">
                <tr>
                    <th class="bg">奖级</th>
                    <th class="bg" width="100">一等奖</th>
                    <th class="bg" width="100">二等奖</th>
                    <th class="bg" width="100">三等奖</th>
                    <th class="bg" width="100">四等奖</th>
                    <th class="bg" width="100">五等奖</th>
                    <th class="bg" width="100">六等奖</th>
                </tr>
                <tr>
                    <td class="bg">中奖条件</td>
                    <td>6+1</td>
                    <td>6+0</td>
                    <td>5+1</td>
                    <td>5+0,4+1</td>
                    <td>4+0,3+1</td>
                    <td>2+1,1+1,0+1</td>
                </tr>
                <tr>
                    <td class="bg">中奖注数</td>
                    <td class="t_zhu">0注</td>
                    <td class="t_zhu">0注</td>
                    <td class="t_zhu">0注</td>
                    <td class="t_zhu">0注</td>
                    <td class="t_zhu">0注</td>
                    <td class="t_zhu">0注</td>
                </tr>
                <tr>
                    <td class="bg">单注奖金</td>
                    <td class="ssq_jiang">
                        <#if cash??>
                        ${cash?split(";")[0]?split("_")[2]?number/100}元
                        <#else>
                        &minus;
                        </#if>
                    </td>
                    <td class="ssq_jiang">
                        <#if cash??>
                        ${cash?split(";")[1]?split("_")[2]?number/100}元
                        <#else>
                        &minus;
                        </#if>
                    </td>
                    <td class="ssq_jiang">3,000元</td>
                    <td class="ssq_jiang">200元</td>
                    <td class="ssq_jiang">10元</td>
                    <td class="ssq_jiang">5元</td>
                </tr>
                <tr>
                    <td class="bg">中奖奖金</td>
                    <td class="t_cash">0元</td>
                    <td class="t_cash">0元</td>
                    <td class="t_cash">0元</td>
                    <td class="t_cash">0元</td>
                    <td class="t_cash">0元</td>
                    <td class="t_cash">0元</td>
                </tr>
            </table>
			<div class="t-intro-txt">
				<b>说明</b><br />
				中奖条件格式为“红球命中个数+蓝球命中个数”，未命中则为“0”。例如：“6+1”为红球命中6个+蓝球命中1个，“6+0”为红球命中6个+蓝球未命中。<br />
				奖金计算结果将根据实际开奖奖金进行录入数据后计算得出，如有偏差，请以官方开奖时公布奖金为准！
			</div>
		</div>
        <div class="t-box" id="ssq_ball">
            <div class="t-sel">
                <p>
                    <b>查询期次：</b>
                    <select class="t-ret-sel">
                    <#list awardWithPeriodList![] as x>
                    <#if x?? && x.award?? && x.award.prizeNumber??>
                    <#assign prizeNo = x.award.prizeNumber />
                        <option value="${prizeNo}">第${x.award.periodNo!"-"}期开奖结果：${prizeNo?replace(":"," + ")}</option>
                    </#if>
                    </#list>
                    </select>
                </p>
            </div>
            <div class="tool-ball-area">
                <span class="tip">请至少选择<i class="red">6</i>个红球和<i class="blue">1</i>个蓝球：</span>
                <div class="ball-sel cf">
                    <div class="red-ball">
                        <div class="h"><i class="red">红球</i></div>
                        <ul class="cf">
                            <li>01</li>
                            <li>02</li>
                            <li>03</li>
                            <li>04</li>
                            <li>05</li>
                            <li>06</li>
                            <li>07</li>
                            <li>08</li>
                            <li>09</li>
                            <li>10</li>
                            <li>11</li>
                            <li>12</li>
                            <li>13</li>
                            <li>14</li>
                            <li>15</li>
                            <li>16</li>
                            <li>17</li>
                            <li>18</li>
                            <li>19</li>
                            <li>20</li>
                            <li>21</li>
                            <li>22</li>
                            <li>23</li>
                            <li>24</li>
                            <li>25</li>
                            <li>26</li>
                            <li>27</li>
                            <li>28</li>
                            <li>29</li>
                            <li>30</li>
                            <li>31</li>
                            <li>32</li>
                            <li>33</li>
                        </ul>
                    </div>
                    <div class="blue-ball">
                        <div class="h"><i class="blue">蓝球</i></div>
                        <ul class="cf">
                            <li>01</li>
                            <li>02</li>
                            <li>03</li>
                            <li>04</li>
                            <li>05</li>
                            <li>06</li>
                            <li>07</li>
                            <li>08</li>
                            <li>09</li>
                            <li>10</li>
                            <li>11</li>
                            <li>12</li>
                            <li>13</li>
                            <li>14</li>
                            <li>15</li>
                            <li>16</li>
                        </ul>
                    </div>
                </div>
                <div class="tou-info">
                    您选了<i class="red">0</i>个红球，<i class="blue">0</i>个蓝球，共<i class="red">0</i>注，共<i class="red">0</i>元
                </div>
            </div>
            <form action="${domainUrl}ssq/" method="post" id="tou">
                <input type="hidden" name="quickBetNumber" value="" id="quickBet" />
            </form>
            <div class="t-btns">
                <a href="javascript:;" class="fbtn">计算奖金</a>
                <a href="javascript:;" id="nowTou" class="fbtn">立即投注</a>
            </div>

            <div class="t-intro-txt">
                <b>说明</b><br />
                奖金计算结果将根据实际开奖奖金进行录入数据后计算得出，如有偏差，请以官方开奖时公布奖金为准！
            </div>
        </div>
	</div>
</div>
<div id="tool_dia" class="dialog tool_dia">
<div class="modal">
    <div class="modcon">
        <div class="title"><i title="关闭" class="close dialog-close">&times;</i>中奖结果</div>
        <div class="tool_award">
            <div class="info">
                <b class="fr">方案金额：<em></em> 注*<em>2</em> 元=<em></em> 元</b>
                <strong>双色球</strong> 第<span>-</span>期
            </div>
            <div class="num">
                开&nbsp;&nbsp;奖&nbsp;&nbsp;号&nbsp;&nbsp;码：<span class="kj_num"></span><br />
                您所选的号码：<span class="my_num"></span>
            </div>
            <table class="tool_tbl">
                <tr>
                    <th width="120">奖级</th>
                    <th width="120">中奖条件</th>
                    <th width="150">奖金（元）</th>
                    <th class="nbd">中奖注数（注）</th>
                </tr>
                <tr>
                    <td>一等奖</td>
                    <td>6+1</td>
                    <td class="ball_jiang">
                        <#if cash??>
                        ${(cash?split(";")[0]?split("_")[2]?number/100)?c}
                        <#else>
                        &minus;
                        </#if>
                    </td>
                    <td class="nbd"><em>0</em></td>
                </tr>
                <tr>
                    <td>二等奖</td>
                    <td>6+0</td>
                    <td class="ball_jiang">
                        <#if cash??>
                        ${(cash?split(";")[1]?split("_")[2]?number/100)?c}
                        <#else>
                        &minus;
                        </#if>
                    </td>
                    <td class="nbd"><em>0</em></td>
                </tr>
                <tr>
                    <td>三等奖</td>
                    <td>5+1</td>
                    <td>3000</td>
                    <td class="nbd"><em>0</em></td>
                </tr>
                <tr>
                    <td>四等奖</td>
                    <td>5+0,4+1</td>
                    <td>200</td>
                    <td class="nbd"><em>0</em></td>
                </tr>
                <tr>
                    <td>五等奖</td>
                    <td>4+0,3+1</td>
                    <td>10</td>
                    <td class="nbd"><em>0</em></td>
                </tr>
                <tr>
                    <td>六等奖</td>
                    <td>2+1,1+1,0+1</td>
                    <td>5</td>
                    <td class="nbd"><em>0</em></td>
                </tr>
            </table>
            <div class="total">
                中奖金额：<em>0</em>元
            </div>
        </div>
    </div>
</div>
</div>

<#assign jin1 = "" />
<#assign jin2 = "" />
<#assign qici = "" />
<#if awardWithPeriodList?? && awardWithPeriodList?size gt 0>
<#list awardWithPeriodList as x>
    <#if x?? && x.award?? && x.award.bonusLevelDetail??>
        <#assign jin1 = jin1 + "," + (x.award.bonusLevelDetail?split(";")[0]?split("_")[2]?number/100)?c />
        <#assign jin2 = jin2 + "," + (x.award.bonusLevelDetail?split(";")[1]?split("_")[2]?number/100)?c />
        <#else>
        <#assign jin1 = jin1 + ",0" />
        <#assign jin2 = jin2 + ",0" />
    </#if>
</#list>
<#list awardWithPeriodList as n>
    <#if n?? && n.period?? && n.period.periodNo??>
        <#assign qici = qici + "," + n.period.periodNo />
    </#if>
</#list>
<#assign jin1 = jin1?substring(1) />
<#assign jin2 = jin2?substring(1) />
<#assign qici = qici?substring(1) />
</#if>
<input type="hidden" id="jiangs" value="${jin1};${jin2}" />
<input type="hidden" id="qicihao" value="${qici}" />
<#include "../../common/footer.ftl" />
</body>
</html>