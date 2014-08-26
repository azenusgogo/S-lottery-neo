<#if !cdnBaseUrl??><#include "../fakeData/common.ftl" /></#if>
<#include "../common/header.ftl" />
<#assign func = "login" />

<div class="section cf" style="padding:0">
    <div class="u_login">
        <h2>账号登录</h2>
        <form class="login-box" id="login_page">
            <div class="login-ipt cf">
                <span class="fl">登录账号：</span>
                <a href="/help/p-all.html#hot04" target="_blank" class="fr">忘记账号？</a>
                <input type="text" class="uname" tabindex="1" />
                <em class="tip">请输入用户名</em>
            </div>
            <div class="login-ipt cf">
                <span class="fl">登录密码：</span>
                <a href="https://passport.sohu.com/web/RecoverPwdInput.action?ru=https:%2F%2Faccount.sogou.com" target="_blank" class="fr">忘记密码？</a>
                <input type="password" class="upwd" tabindex="2" />
                <em class="tip">请输入密码</em>
            </div>
            <div class="login-ipt cf captchaimg">
                <span>验证码：</span><br />
                <input type="text" class="validcode" tabindex="3" />
                <img src="${cdnBaseUrl}img/blank.gif" class="validpic" />
                <a href="#" class="changevalid">换一张</a>
                <em class="tip">请输入验证码</em>
            </div>
            <div class="login-btn">
                <a class="fbtn submit" href="#">&nbsp;&nbsp;登&nbsp;录&nbsp;&nbsp;</a>
                <a href="" class="qqconnect">QQ登录</a>
                <em class="tip">用户名或密码错误</em>
            </div>
        </form>
    </div>
    <div class="u_cover">
        <a href="javascript:void(0)" class="fbtn btn-white">免费注册</a><#--https://account.sogou.com/web/reg/nick -->
    </div>
</div>
<div class="u_footer">
    <a rel="nofollow" href="/help/about.html"><i class="about"></i> 关于搜狗彩票</a>
    <a rel="nofollow" href="/help/contact.html">联系我们</a>
    <a rel="nofollow" href="#" id="feedback">意见反馈</a>
    Copyright@2013 Sogou.com. All Rights Reserved.
    京ICP证050897号
</div>
<input type="hidden" id="redirectUrl" value="${redirectUrl}"/>
<#include "../common/js.ftl" />
</body>
</html>