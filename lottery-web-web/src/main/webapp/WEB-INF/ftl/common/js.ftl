<#if func??>
    <#if func == "help">
    <#assign pingback = "help_" + (m1!) + "_" + (m2!) />
    <#else>
    <#assign pingback = func />
    </#if>
<#else>
    <#assign pingback = "other" />
</#if>
<div id="login_body"></div>
<script src="http://account.sogou.com/static/api/passport.js"></script>
<script src="${cdnBaseUrl}js/base.js?v=${versionId}"></script>
<script>
    //pingback
    var spb_vars = spb_vars || {
    "ptype":"caipiao",
    "pcode":"${pingback}"
    };
    (function() {
        var pb = document.createElement('script'); pb.type = 'text/javascript'; pb.async = true;
        pb.src = 'http://d.123.sogou.com/u/pb/pb.257175.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(pb, s);
    })();
</script>
<#if "ssq,dlt,qlc,qxc,jczq,calcu,goucai,index,login"?contains(pingback)>
<script src="${cdnBaseUrl}js/${pingback}.js?v=${versionId}"></script>
<#elseif "f14,f9"?contains(pingback)>
<script src="${cdnBaseUrl}js/zc.js?v=${versionId}"></script>
<#elseif "k3js,k3jl,k3gx"?contains(pingback)>
<script src="${cdnBaseUrl}js/k3.js?v=${versionId}"></script>
<#elseif pingback?contains("help") || pingback == "notice">
<script src="${cdnBaseUrl}js/help.js?v=${versionId}"></script>
<#elseif "charge,info,fill_info,order,pwd,deal,withdraw"?contains(pingback)>
<script src="${cdnBaseUrl}js/ucenter.js?v=${versionId}"></script>
<#elseif pingback == "kaijiang">
<script src="${cdnBaseUrl}js/common.js?v=${versionId}"></script>
<#else>
<script src="${cdnBaseUrl}js/main.js?v=${versionId}"></script>
</#if>
<script>
//live800
(function() {
    function _load(fn) {
        if (window.attachEvent) {
            window.attachEvent('onload', fn);
        } else {
            window.addEventListener('load', fn, false);
        }
    }
    function async_load(src) {
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = src;
        document.body.appendChild(script);
    }
    _load(function() {
        async_load("https://v2.live800.com/live800/chatClient/textButton.js?jid=1525490804&companyID=364717&configID=123172&codeType=custom&delayload=1&renderid=online_serv");
    });
})();

//baidu tongji
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F5ccb6ebf1237dbafd08d72c1b77e1f5a' type='text/javascript'%3E%3C/script%3E"));
</script>