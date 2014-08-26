<#--
    需要定义：当前页数pageNo,总页数totalPage,其他参数pageArg(非必须)
-->

<#assign pageUrl = "" />
<#if pageArg??>
<#list pageArg as x>
<#assign pageUrl = pageUrl + x + "&" />
</#list>
</#if>

<#if totalPage gt 1>
<div class="u-page">
    <span>共${totalPage}页</span>
    <a href="${pageBase!''}?${pageUrl}pageNo=1">首页</a>
    <#if pageNo == 1>
    <a href="javascript:;" class="disabled">上一页</a>
    <#else>
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo-1}">上一页</a>
    </#if>
<#if totalPage lt 5>
    <#list (1..totalPage) as x>
    <a href="${pageBase!''}?${pageUrl}pageNo=${x}"<#if x== pageNo> class="cur"</#if>>${x}</a>
    </#list>
<#else>
    <#if pageNo lt 4>
    <#list (1..5) as x>
    <a href="${pageBase!''}?${pageUrl}pageNo=${x}"<#if pageNo == x> class="cur"</#if>>${x}</a>
    </#list>
    ...
    <#elseif pageNo gt totalPage-3>
    ...
    <#list (totalPage-4..totalPage) as x>
    <a href="${pageBase!''}?${pageUrl}pageNo=${x}"<#if pageNo == x> class="cur"</#if>>${x}</a>
    </#list>
    <#else>
    ...
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo-2}">${pageNo-2}</a>
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo-1}">${pageNo-1}</a>
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo}" class="cur">${pageNo}</a>
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo+1}">${pageNo+1}</a>
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo+2}">${pageNo+2}</a>
    ...
    </#if>
</#if>
    <#if pageNo == totalPage>
    <a href="javascript:;" class="disabled">下一页</a>
    <#else>
    <a href="${pageBase!''}?${pageUrl}pageNo=${pageNo+1}">下一页</a>
    </#if>
    <a href="${pageBase!''}?${pageUrl}pageNo=${totalPage}">末页</a>&nbsp;&nbsp;
    <form style="display:inline" action="" id="pageJump">
    <#if pageArg??>
    <#list pageArg as x>
    <input type="hidden" name="${x?split('=')[0]}" value="${x?split('=')[1]}" />
    </#list>
    </#if>
    到第 <input type="text" name="pageNo" /> 页 <a href="#" onclick="document.getElementById('pageJump').submit();return false;" class="page-btn">确定</a>
    </form>
</div>
</#if>