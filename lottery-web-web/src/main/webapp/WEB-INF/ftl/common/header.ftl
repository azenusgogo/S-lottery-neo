<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${seoTitle!'搜狗彩票网 - 网上购买彩票首选平台'}</title>
    <meta name="description" content="${seoDescription!''}" />
    <meta name="keywords" content="${seoKeywords!''}" />
    <#if func?? && func == "index">
    <link rel="stylesheet" href="${cdnBaseUrl}css/index.css?v=${versionId}"/>
    <base target="_blank" />
    <#else>
    <link rel="stylesheet" href="${cdnBaseUrl}css/main.css?v=${versionId}"/>
    </#if>
</head>
<body>
<#if (func!"") != "login">
<div class="topbar" pbflag="wc_0_顶部导航">
    <div class="wrapper">
        <ul class="toplinks" id="topFunc">
            <li><a href="javascript:;" class="addfav anchor_hover">收藏本站</a></li> |
            <li><a href="javascript:;" class="sethome anchor_hover">设为首页</a></li>
            <!-- | <li><a href="/desktop/nav/save.html" target="_self">保存到桌面</a></li> -->
        </ul>
        <div class="top_ulink" id="top_nav_login">
            <div class="loading"></div>
            欢迎您来到搜狗彩票！
            <a class="uout top_login_btn" href="javascript:;">登录</a> | 
            <a class="uout top_reg_btn" href="javascript:;">注册</a> (<a class="uout" href="/help/f-register.html#reg03">如何注册?</a>)
            <a rel="nofollow" href="/login/user/order/bets.html" class="anchor_hover">购彩记录</a>
            <a rel="nofollow" href="/login/user/trans/trans.html" class="anchor_hover">资金明细</a>
            <a rel="nofollow" href="/login/charge/pre.html" class="anchor_hover">充值</a>
            <a rel="nofollow" href="/login/user/withdraw.html" class="anchor_hover">提款</a>
        </div>
    </div>
</div>
</#if>
<div class="header">
    <div class="wrapper cf">
        <#if (func!) == "index">
        <a href="${domainUrl}" target="_self" class="logo ct">
        	<h1>搜狗彩票</h1>
        </a>
        <#else>
        <a href="${domainUrl}" class="logo ct">搜狗彩票</a>
        </#if>
        <div class="banner"></div>
        <div class="kefuwrapper">
            <div class="kefu">
            	<div class="ol-kf">
            		<div class="chat2serv" title="点击与客服对话"><i></i><em class="refillin" id="online_serv"></em></div>
            	</div>
            	<p>客服电话<span>010-5689 8998</span></p>
            </div>
        </div>
    </div>
</div>
<div class="menu" pbflag="wc_0_主导航">
    <div class="wrapper">
        <#if func?? && func == "index">
        <div class="cp-type">
            <b>选择彩票</b>
        </div>
        <#else>
        <div class="cp-type" id="nav_memu">
            <b>选择彩票</b>
            <ul pbflag="wc_0_彩种列表">
                <#if commonGames??>
                    <#list commonGames as x>
                    <#if x??>
                    <li><a class="${x.gameId}" href="${domainUrl}${x.gameId}/">${x.gameCn}<span>${x.desc}</span><#if (x.tag)??><div class="redWordsWraper"><span class="redWords" <#if x.tag==''>style="display:none;"</#if>><i class="arrowsIcon"></i>${x.tag!""}</span></div></#if></a></li>
                    </#if>
                    </#list>
                </#if>
                <#if highFrequencyGames??>
                    <#list highFrequencyGames as x>
                    <#if x??>
                    <li><a class="${x.gameId}" href="${domainUrl}${x.gameId}/">${x.gameCn}<span>${x.desc}</span></a></li>
                    </#if>
                    </#list>
                </#if>
                <#if traditionalSportGames??>
                    <#list traditionalSportGames as x>
                    <#if x??>
                    <li><a class="${x.gameId}" href="${domainUrl}${x.gameId}/">${x.gameCn}<span>${x.desc}</span></a></li>
                    </#if>
                    </#list>
                </#if>
            </ul>
        </div>
        </#if>
        <ul class="dh">
            <li><a target="_self" href="${domainUrl}">首页</a></li>
            <li><a target="_self" href="${domainUrl}goucai/"<#if (func!'') == "goucai"> class="cur"</#if>>购彩大厅</a></li>
            <li><a target="_self" href="${domainUrl}kaijiang/"<#if (func!'') == "kaijiang"> class="cur"</#if>>彩票开奖</a></li>
            <li><a target="_self" href="${domainUrl}calculator/ssq/"<#if (func!'') == "calcu"> class="cur"</#if>>购彩工具</a></li>
            <li><a target="_self" href="${domainUrl}help/" class="<#if (func!'') == 'help'>cur<#else>nobg</#if>">帮助中心</a></li>
        </ul>
    </div>
</div>