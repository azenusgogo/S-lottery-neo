<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "pwd" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="u-tab cf">
            <a href="${domainUrl}login/user/pwd/change.html">修改支付密码</a>
            <a href="${domainUrl}login/user/pwd/find.html" class="cur">找回支付密码</a>
            <a href="https://account.sogou.com/web/security/password" target="_blank">修改登录密码</a>
            <a href="https://passport.sohu.com/web/RecoverPwdInput.action?ru=https:%2F%2Faccount.sogou.com" target="_blank">找回登录密码</a>
            <a href="${domainUrl}login/user/safe/change.html">重置安全问题</a>
        </div>
        <div class="pwd-cont">
            <p class="tips">请您通过以下方式找回您的支付密码：</p>
        </div>
        <table class="findpwd-way" id="find_paypwd">
            <tr>
                <td width="60" align="right"><i class="pwd-ico"></i></td>
                <td><b>通过实名认证和绑定的手机号码校验自助找回支付密码</b><br /><span>如果您认证过实名信息，且绑定的手机可用，推荐用这种方法</span></td>
                <td width="200"><a class="fbtn find_name" href="#">找回密码</a></td>
            </tr>
            <tr>
                <td width="60" align="right"><i class="pwd-ico pwd-2"></i></td>
                <td><b>通过安全问题和绑定的手机号码校验自助找回支付密码</b><br /><span>如果您还记得安全问题的答案，且绑定的手机可用，推荐用这种方法</span></td>
                <td width="200"><a class="fbtn find_question" href="#">找回密码</a></td>
            </tr>
            <tr>
                <td width="60" align="right"><i class="pwd-ico pwd-4"></i></td>
                <td><b>联系在线客服修改密码</b><br /><span>本方法验证周期较长，建议您优先选择自主找回方式</span></td>
                <td width="200"><a class="fbtn btn-white" id="toOnlineServ" href="#">联系客服</a></td>
            </tr>
        </table>
    </div>
</div>
<!--dialog-->
<div class="dialog" id="find_name">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>找回密码</div>
            <div class="modpwd">
                <div class="tips">提醒：您将通过实名信息+手机验证码双重校验重置支付密码。</div>
                <dl>
                    <dd>
                        <p>第一步：实名信息校验</p>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 真实姓名：</label>
                            <div class="f-n"><input type="text" class="txt bdr name" /><br />
                            <em class="valid"></em></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 证件类型：</label>
                            <div class="f-n">
                            <select class="zj">
                                <#list idCardTypes as x>
                                <option value="${x.key}">${x.value}</option>
                                </#list>
                            </select><br />
                            <em class="valid"></em>
                            </div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 证件号码：</label>
                            <div class="f-n"><input type="text" class="txt bdr zj_number" /><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                    <dd>
                        <p>第二步：绑定手机号验证<span>（15分钟内最多可以获取三次短信验证码，如暂时未收到短信，请稍后再尝试。）</span></p>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 手机号码：</label>
                            <div class="f-n">
                                ${userinfo.displayMobile} <a href="#" class="fbtn min-white send_code">发送验证码</a>
                                <span class="dia-green f12">发送成功</span>
                            </div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 输入验证码：</label>
                            <div class="f-n"><input type="text" class="txt bdr val code" value="请填写收到的短信验证码" /> <a target="_blank" href="/help/p-all.html#hot17" class="bluelink">手机无法收到验证码？</a><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                    <dd>
                        <p>第三步：重置支付密码</p>
                        <div class="fipt">
                            <label class="f-h">新密码：</label>
                            <div class="f-n"><input type="password" class="txt bdr pwd1" /><br />
                            <em class="valid"></em></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h">再输入一遍：</label>
                            <div class="f-n"><input type="password" class="txt bdr pwd2" /><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                </dl>
                <div class="modbtn">
                    <a class="fbtn long submit" href="#">提交</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="dialog" id="find_question">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>找回密码</div>
            <div class="modpwd">
                <div class="tips">提醒：您将通过密码保护问题重置支付密码。</div>
                <dl>
                    <dd>
                        <p>第一步：安全问题验证</p>
                        <div class="fipt">
                            <label class="f-h">原问题：</label>
                            <div class="f-n"><b class="exQuestion"></b></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h">答案：</label>
                            <div class="f-n"><input type="text" class="txt bdr answer" /><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                    <dd>
                        <p>第二步：绑定手机号验证<span>（15分钟内最多可以获取三次短信验证码，如暂时未收到短信，请稍后再尝试。）</span></p>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 手机号码：</label>
                            <div class="f-n">
                                ${userinfo.displayMobile} <a href="#" class="fbtn min-white send_code">发送验证码</a>
                                <span class="dia-green f12">发送成功</span>
                            </div>
                        </div>
                        <div class="fipt">
                            <label class="f-h"><em>*</em> 输入验证码：</label>
                            <div class="f-n"><input type="text" class="txt bdr val code" value="请填写收到的短信验证码" /> <a target="_blank" href="/help/p-all.html#hot17" class="bluelink">手机无法收到验证码？</a><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                    <dd>
                        <p>第三步：重置支付密码</p>
                        <div class="fipt">
                            <label class="f-h">新密码：</label>
                            <div class="f-n"><input type="password" class="txt bdr pwd1" /><br />
                            <em class="valid"></em></div>
                        </div>
                        <div class="fipt">
                            <label class="f-h">再输入一遍：</label>
                            <div class="f-n"><input type="password" class="txt bdr pwd2" /><br />
                            <em class="valid"></em></div>
                        </div>
                    </dd>
                </dl>
                <div class="modbtn">
                    <a class="fbtn long submit" href="#">提交</a>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>