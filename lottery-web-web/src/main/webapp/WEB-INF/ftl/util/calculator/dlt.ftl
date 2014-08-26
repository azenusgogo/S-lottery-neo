<#include "../../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#-- 本页标识 -->
<#assign func = "calcu" />
<#assign seoTitle = "大乐透奖金计算器 – 搜狗彩票" />
<#assign seoKeywords = "大乐透，超级大乐透，体彩大乐透" />
<#assign seoDescription = "搜狗彩票提供体彩大乐透投注，大乐透开奖结果，走势图及专家预测分析服务。网上购买超级大乐透，首选搜狗彩票"/>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#if (awardWithPeriodList[0].award.bonusLevelDetail)??>
<#assign cash = awardWithPeriodList[0].award.bonusLevelDetail />
</#if>

<div class="wrapper t-main">
	<div class="t-left">
		<h2><b>奖金计算器</b></h2>
		<ul>
			<li><a href="${domainUrl}calculator/ssq/"><i class="logos ssq-logo"></i>双色球</a></li>
			<li><a href="${domainUrl}calculator/dlt/" class="cur"><i class="logos dlt-logo"></i>大乐透<em></em></a></li>
		</ul>
	</div>
	<div class="t-right">
		<h1><b>大乐透奖金计算器</b></h1>
        <div class="funtab">
            <a href="#" class="cur">按中奖个数计算奖金</a>
            <a href="#">投注号码按期次查询</a>
        </div>
		<div class="t-box" style="display:block" id="dlt_award">
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
                    前区
                    <select class="s_red">
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
                        <option value="31">31个</option>
                        <option value="32">32个</option>
                        <option value="33">33个</option>
                        <option value="34">34个</option>
                        <option value="35">35个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    后区
                    <select class="s_blue">
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
                    </select>&nbsp;&nbsp;
                    <label><input type="checkbox" class="zhui" /> <em>追加投注</em></label>
                </p>
                <p>
                    <b>投注金额：</b>
                    共计<em><b class="zhu_stat">1</b></em>注，<em><b class="yuan_stat">2</b></em>元
                </p>
                <p>
                    <b>我的命中：</b>
                    前区
                    <select class="t_red">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4">4个</option>
                        <option value="5">5个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    后区
                    <select class="t_blue">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                    </select>
                </p>
                </div>
                <div class="tz_type">
                <p>
                    <b>我的投注：</b>
                    前区胆码
                    <select class="s_red_dm">
                        <option value="1" selected>1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4">4个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    前区拖码
                    <select class="s_red_tm">
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4" selected>4个</option>
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
                        <option value="31">31个</option>
                        <option value="32">32个</option>
                        <option value="33">33个</option>
                        <option value="34">34个</option>
                    </select><br />
                    <span class="sj">
                    后区胆码
                    <select class="s_blue_dm">
                        <option value="1">1个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    后区拖码
                    <select class="s_blue_tm">
                        <option value="1" selected>1个</option>
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
                    </select>&nbsp;&nbsp;
                    <label><input type="checkbox" class="zhui" /> <em>追加投注</em></label>
                    </span>
                </p>
                <p>
                    <b>投注金额：</b>
                    共计<em><b class="zhu_stat">1</b></em>注，<em><b class="yuan_stat">2</b></em>元
                </p>
                <p>
                    <b>我的命中：</b>
                    前区胆码
                    <select class="t_red_dm">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    前区拖码
                    <select class="t_red_tm">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                        <option value="3">3个</option>
                        <option value="4">4个</option>
                    </select><br />
                    <span class="sj">
                    后区胆码
                    <select class="t_blue_dm">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
                    后区拖码
                    <select class="t_blue_tm">
                        <option value="0">0个</option>
                        <option value="1">1个</option>
                        <option value="2">2个</option>
                    </select>
                    </span>
                </p>
                </div>
            </div>
            <form action="${domainUrl}dlt/" method="post" id="try">
                <input type="hidden" name="quickBetNumber" value="" id="tryBet">
            </form>
			<div class="t-btns">
				<a href="javascript:;" class="fbtn">计算奖金</a>
				<a href="javascript:;" id="nowTry" class="fbtn">试试手气</a>
			</div>
			<div class="t-ret">您中奖金额（税前）是：<b>0</b>元</div>
			<div id="dlt_award_tbl"></div>
			<div class="t-intro-txt">
				<b>说明</b><br />
				中奖条件格式为“前区命中个数+后区命中个数”，未命中则为“0”。例如：“5+1”为前区命中5个+后区命中1个，“5+0”为前区命中5个+后区未命中。<br />
                奖金计算结果将根据实际开奖奖金进行录入数据后计算得出，如有偏差，请以官方开奖时公布奖金为准！
			</div>
		</div>
        <div class="t-box" id="dlt_ball">
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
                <span class="tip">请至少选择<i class="red">5</i>个前区号码和<i class="blue">2</i>个后区号码：</span>
                <div class="ball-sel dlt-ball cf">
                    <div class="red-ball">
                        <div class="h"><i class="red">前区号码</i></div>
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
                            <li>34</li>
                            <li>35</li>
                        </ul>
                    </div>
                    <div class="blue-ball">
                        <div class="h"><i class="blue">后区号码</i></div>
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
                        </ul>
                    </div>
                </div>
                <div class="tou-info">
                    您选了<i class="red">0</i>个红球，<i class="blue">0</i>个蓝球，共<i class="red ball_zhu">0</i>注，共<i class="red ball_yuan">0</i>元
                </div>
            </div>

            <form action="${domainUrl}dlt/" method="post" id="tou">
                <input type="hidden" name="quickBetNumber" value="" id="quickBet" />
            </form>
            <div class="t-btns">
                <label><input type="checkbox" class="ball_zhui" /> 追加投注</label>
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
                <b class="fr">方案金额：<em></em> 注*<em class="ball_price">2</em> 元=<em></em> 元</b>
                <strong>大乐透</strong> 第<span></span>期
            </div>
            <div class="num">
                开&nbsp;&nbsp;奖&nbsp;&nbsp;号&nbsp;&nbsp;码：<span class="kj_num"></span><br />
                您所选的号码：<span class="my_num"></span>
            </div>
            <div id="dlt_ball_ret"></div>
        </div>
    </div>
</div>
</div>
<#assign jin1 = "" />
<#assign jin2 = "" />
<#assign jin3 = "" />
<#assign zhui1 = "" />
<#assign zhui2 = "" />
<#assign zhui3 = "" />
<#assign qici = "" />
<#if awardWithPeriodList?? && awardWithPeriodList?size gt 0>
<#list awardWithPeriodList as x>
    <#if (x.award.bonusLevelDetail)??>
        <#assign jin1 = jin1 + "," + (x.award.bonusLevelDetail?split(";")[0]?split("_")[2]?number/100)?c />
        <#assign jin2 = jin2 + "," + (x.award.bonusLevelDetail?split(";")[2]?split("_")[2]?number/100)?c />
        <#assign jin3 = jin3 + "," + (x.award.bonusLevelDetail?split(";")[4]?split("_")[2]?number/100)?c />
        <#assign zhui1 = zhui1 + "," + (x.award.bonusLevelDetail?split(";")[1]?split("_")[2]?number/100)?c />
        <#assign zhui2 = zhui2 + "," + (x.award.bonusLevelDetail?split(";")[3]?split("_")[2]?number/100)?c />
        <#assign zhui3 = zhui3 + "," + (x.award.bonusLevelDetail?split(";")[5]?split("_")[2]?number/100)?c />
    <#else>
        <#assign jin1 = jin1 + ",0" />
        <#assign jin2 = jin2 + ",0" />
        <#assign jin3 = jin3 + ",0" />
        <#assign zhui1 = zhui1 + ",0" />
        <#assign zhui2 = zhui2 + ",0" />
        <#assign zhui3 = zhui3 + ",0" />
    </#if>
</#list>
<#list awardWithPeriodList as n>
    <#if n?? && n.period?? && n.period.periodNo??>
        <#assign qici = qici + "," + n.period.periodNo />
    </#if>
</#list>
<#assign jin1 = jin1?substring(1) />
<#assign jin2 = jin2?substring(1) />
<#assign jin3 = jin3?substring(1) />
<#assign zhui1 = zhui1?substring(1) />
<#assign zhui2 = zhui2?substring(1) />
<#assign zhui3 = zhui3?substring(1) />
<#assign qici = qici?substring(1) />
</#if>
<input type="hidden" id="aBack" value="${jin1};${jin2};${jin3}" />
<input type="hidden" id="aZhui" value="${zhui1};${zhui2};${zhui3}" />
<input type="hidden" id="qicihao" value="${qici}" />
<#include "../../common/footer.ftl" />
</body>
</html>