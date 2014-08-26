<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "pwd" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/pwd/change.html" class="cur">修改支付密码</a>
            <a href="${domainUrl}login/user/pwd/find.html">找回支付密码</a>
            <a href="https://account.sogou.com/web/security/password" target="_blank">修改登录密码</a>
            <a href="https://passport.sohu.com/web/RecoverPwdInput.action?ru=https:%2F%2Faccount.sogou.com" target="_blank">找回登录密码</a>
            <a href="${domainUrl}login/user/safe/change.html">重置安全问题</a>
        </div>
        <div class="pwd-cont" id="change_paypwd">
            <p class="tips">支付密码是您购彩付款时唯一的凭证，请您牢记</p>
            <div class="fipt">
                <label class="f-h">旧的支付密码：</label>
                <div class="f-n">
                    <input type="password" class="txt bdr pwd" />&nbsp;&nbsp;<a href="${domainUrl}login/user/pwd/find.html">忘记密码？</a>
                    <br><em class="valid"></em>
                </div>
            </div>
            <div class="fipt">
                <label class="f-h">新的支付密码：</label>
                <div class="f-n">
                    <input type="password" class="txt bdr pwd1" />
                    <br><em class="valid"></em>
                </div>
            </div>
            <div class="fipt">
                <label class="f-h">确认密码：</label>
                <div class="f-n">
                    <input type="password" class="txt bdr pwd2" />
                    <br><em class="valid"></em>
                </div>
            </div>
            <div class="u-btm-btn">
                <a class="fbtn" href="#">确认修改</a>
            </div>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>