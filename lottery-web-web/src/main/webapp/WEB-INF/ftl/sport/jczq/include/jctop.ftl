<div class="jctop cf">
    <#if allLeagues??>
    <span class="selmatch">
        <em>赛事筛选</em> | <i class="arrud"></i>
        <div class="matchbox">
            <div class="cf">
                <#list allLeagues?split(",") as x>
                <label><input type="checkbox" checked league="${x!}" /> ${x!}</label>
                </#list>
            </div>
            <p>
                <a href="javascript:;" class="close">关闭</a>
                <a href="javascript:;" class="all">全选</a> /
                <a href="javascript:;" class="inverse">反选</a>
            </p>
        </div>
        <div class="xmask"></div>
    </span>
    </#if>
    <label class="onlyfive" data="${popularLeagues}"><input type="checkbox" /> 只显示五大联赛</label>
    <span class="showall">
        <a href="javascript:;" id="showAllLeague">显示全部</a> <em class="leagueStat">[已隐藏0场]</em>
    </span>
    <span class="end">
        已结束66场比赛 <a href="#" target="_blank">查看</a>
    </span>
    <label class="r"><input type="radio" name="cashtype"> 浮动奖金</label>
    <label class="r"><input type="radio" name="cashtype"> 固定奖金</label>
</div>