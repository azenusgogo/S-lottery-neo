<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "pwd" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/pwd/change.html">修改支付密码</a>
            <a href="${domainUrl}login/user/pwd/find.html">找回支付密码</a>
            <a href="https://account.sogou.com/web/security/password" target="_blank">修改登录密码</a>
            <a href="https://passport.sohu.com/web/RecoverPwdInput.action?ru=https:%2F%2Faccount.sogou.com" target="_blank">找回登录密码</a>
            <a href="${domainUrl}login/user/safe/change.html" class="cur">重置安全问题</a>
        </div>
        <div class="pwd-cont">
            <p class="tips">请您通过以下方式重置安全问题：</p>
        </div>
        <table class="findpwd-way" id="find_paypwd">
            <tr>
                <td width="60" align="right"><i class="pwd-ico pwd-3"></i></td>
                <td><b>重置安全问题和答案</b><br /><span>重置您的安全问题和答案，以方便您以后在找回密码时使用</span></td>
                <td width="200"><a class="fbtn reset_answer" href="#">重置安全问题</a></td>
            </tr>
        </table>
    </div>
</div>
<!--dialog-->
<div class="dialog" id="reset_answer">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>重置安全问题和答案</div>
            <div class="modpwd">
                <div class="tips">提醒：您将重置安全问题</div>
                <dl>
                    <dd class="req">
                        <div class="fipt">
                            <label class="f-h">重置安全答案：</label>
                            <div class="f-n"><span class="stxt">（重置成功后，以后忘记支付密码可以通过安全答案和绑定手机<a href="${domainUrl}login/user/pwd/find.html" class="bluelink">自助找回</a>）</span></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 之前的安全问题：</label>
                            <div class="f-n"><b class="f14">${question}</b></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 之前的问题答案：</label>
                            <div class="f-n"><input type="text" class="txt bdr answer" /><br />
                            <em class="valid"></em></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 新的安全问题：</label>
                            <div class="f-n">
                                <select class="new_question">
                                    <#list questions as x>
                                    <#if x??>
                                    <option value="${x.code}">${x.question}</option>
                                    </#if>
                                    </#list>
                                </select><br />
                            <em class="valid"></em>
                            </div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 新的问题答案：</label>
                            <div class="f-n"><input type="text" class="txt bdr new_answer" /><br />
                            <em class="valid"></em></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 请输入支付密码：</label>
                            <div class="f-n"><input type="password" class="txt bdr pwd" /> <a href="${domainUrl}login/user/pwd/find.html" class="bluelink">忘记支付密码？</a><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                </dl>
                <div class="modbtn">
                    <a class="fbtn long submit" href="#">重置答案</a>
                </div>
                <div class="reqtip">
                    <p class="modpli"><i></i>
                        温馨提示：<br />
                        1.您每天只能重置一次安全问题和答案；<br />
                        2.请牢记您的安全问题和答案，以方便您以后再找回密码时使用；<br />
                        3.如有任何问题，欢迎您联系我们的在线客服。
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>