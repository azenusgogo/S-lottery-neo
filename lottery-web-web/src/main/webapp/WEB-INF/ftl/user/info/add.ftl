<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "fill_info" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <div class="pwd-cont" id="fill_info">
            <p class="tips">为了保护您的账户安全，请设置支付密码。支付密码不仅有效保护您的资金安全，也防止账号被他人盗用时动用您的资金！</p>
            <dl class="pwd-safe" id="fill_info">
                <#if pwdneed!false>
                <dd>
                    <p>验证账号信息</p>
                    <div class="fipt">
                        <label class="f-h">登录账户：</label>
                        <div class="f-n"><b>${userId}</b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 登录密码：</label>
                        <div class="f-n"><input type="password" class="txt bdr pwd" /> <a target="_blank" href="https://passport.sohu.com/web/RecoverPwdInput.action?ru=https:%2F%2Faccount.sogou.com">忘记登录密码？</a><br />
                        <em class="valid"></em></div>
                    </div>
                </dd>
                </#if>
                <dd>
                    <p>设置购彩昵称<span>（您的个性称号）</span></p>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 购彩昵称：</label>
                        <div class="f-n"><input type="text" class="txt bdr nickname" /><br />
                        <em class="valid"></em></div>
                    </div>
                </dd>
                <dd>
                    <p>设置支付密码<span>（网上支付时使用，不等同于登录密码）</span></p>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 支付密码：</label>
                        <div class="f-n"><input type="password" class="txt bdr paypwd1" /><br />
                        <em class="valid"></em></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 再次确认：</label>
                        <div class="f-n"><input type="password" class="txt bdr paypwd2" /><br />
                        <em class="valid"></em></div>
                    </div>
                </dd>
                <dd>
                    <p>账户安全设置<span>（保护您的支付密码，遗忘支付密码是可以通过安全问题和手机验证码找回）</span></p>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 安全问题：</label>
                        <div class="f-n">
                            <select class="question">
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
                        <label class="f-h"><em>*</em> 问题答案：</label>
                        <div class="f-n"><input type="text" class="txt bdr answer" /><br />
                        <em class="valid"></em></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 手机号码：</label>
                        <div class="f-n"><input type="text" class="txt bdr phone_n" />
                        <span class="gray">中奖通知和找回支付密码时使用，请谨慎填写</span><br />
                        <em class="valid"></em></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 手机验证码：</label>
                        <div class="f-n">
                            <input type="text" class="txt bdr s2 code" />
                            <span class="add-on">
                                <a href="#" class="fbtn min-white send-code">发送验证码</a>
                                <span class="dia-green f12"> 发送成功</span>
                            </span>
                            <br />
                            <em class="valid"></em>
                        </div>
                    </div>
                    <!-- <div class="fipt">
                        <label class="f-h"><em>*</em> 常用邮箱：</label>
                        <div class="f-n"><input type="text" class="txt bdr email" /><br />
                        <em class="valid"></em></div>
                    </div> -->
                </dd>
                <dd>
                    <p>补充实名信息</p>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 真实姓名：</label>
                        <div class="f-n"><input type="text" class="txt bdr uname" /><br />
                        <em class="valid"></em></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 选择证件类型：</label>
                        <div class="f-n">
                            <select class="zj">
                                <#list idCardTypes as x>
                                <#if x??>
                                <option value="${x.key}">${x.value}</option>
                                </#if>
                                </#list>
                            </select><br />
                            <em class="valid"></em>
                        </div>
                    </div>
                    <div class="fipt">
                        <label class="f-h"><em>*</em> 证件号码：</label>
                        <div class="f-n"><input type="text" class="txt bdr zj_n" /><br />
                        <em class="valid"></em></div>
                    </div>
                </dd>
            </dl>

            <div class="u-btm-btn">
                <p><label><input type="checkbox" class="pact" checked="true" /> 我已满18周岁</label></p>
                <a class="fbtn submit" href="#">提 交</a>
            </div>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>