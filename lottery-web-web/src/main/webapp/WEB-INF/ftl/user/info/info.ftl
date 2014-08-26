<#if !cdnBaseUrl??><#include "../../fakeData/user-info.ftl" /></#if>
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />
<#assign func = "info" />

<div class="wrapper cf ucenter">
    <#include "../include/u-menu.ftl" />
    <div class="u-cont">
        <h3><b>我的身份信息</b></h3>
        <div class="u-idinfo" id="my_info">
            <div class="fipt">
                <label class="f-h">购彩昵称：</label>
                <div class="f-n"><b>${nickName!}</b></div>
            </div>
            <div class="fipt">
                <label class="f-h">真实姓名：</label>
                <div class="f-n"><b>${userinfo.displayTrueName!"-"}</b></div>
            </div>
            <div class="fipt">
                <label class="f-h">身份证号：</label>
                <div class="f-n"><b>${userinfo.displayIdCardNo!"-"}</b></div>
            </div>
            <div class="fipt">
                <label class="f-h">手机号码：</label>
                <div class="f-n"><b class="phone_area">${userinfo.displayMobile!"-"}</b><a href="#" class="change_phone">修改手机号码</a></div>
            </div>
            <#if userinfo.bankId??>
            <div class="fipt">
                <label class="f-h">绑定银行卡卡号：</label>
                <div class="f-n">
                    <i class="bank-ico b-${userinfo.bankId}"></i>
                    <b>${userinfo.displayBankCardNo!"-"}</b>
                    <a href="#" class="change_card">修改银行卡</a>
                </div>
            </div>
            <#else>
            <div class="fipt">
                <label class="f-h">绑定银行卡卡号：</label>
                <div class="f-n">
                    <b>您尚未绑定银行卡，请尽快处理</b>
                    <a href="/login/user/withdraw.html">立即绑定</a>
                </div>
            </div>

            </#if>
        </div>
        <div class="btm-tips">
            <p><i></i>修改绑定时，请先验证已绑定手机号，若当前手机号遗失/不用，或无法收到验证码，<br />请联系客服中心：010-5689 8998</p>
        </div>
    </div>
</div>
<!--  dialog  -->
<div class="dialog" id="change_phone1">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>验证旧手机号码</div>
            <div class="modbox">
                <p class="mod-tips">修改绑定时，请先验证已绑定手机号，若当前手机号遗失/不用，或无法收到验证码，请联系客服中心：010-5689 8998</p>
                <div class="u-re-tel">
                    <div class="fipt">
                        <label class="f-h">您的旧手机号码：</label>
                        <div class="f-n">
                        ${userinfo.displayMobile}
                        <a href="#" class="fbtn min-white send_code">
                            获取验证码
                        </a>
                        <span class="dia-green f12">发送成功</span>
                        </div>
                    </div>
                    <div class="fipt">
                       <label class="f-h">请输入验证码：</label>
                       <div class="f-n"><input type="text" class="txt code" /><br />
                       <em class="valid"></em></div>
                    </div>
                    <div class="fipt">
                       <label class="f-h">请输入支付密码：</label>
                       <div class="f-n"><input type="password" class="txt pwd" /><br />
                       <em class="valid"></em></div>
                    </div>
                </div>
                <div class="modbtn"><a class="fbtn next" href="#">下一步</a></div>
            </div>
        </div>
    </div>
</div>
<div class="dialog" id="change_phone2">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>绑定新手机号码</div>
            <div class="modbox">
               <p class="mod-tips">修改绑定时，请先验证已绑定手机号，若当前手机号遗失/不用，或无法收到验证码，请联系客服中心：010-5689 8998</p>
               <div class="u-re-tel">
                    <div class="fipt">
                        <label class="f-h">您的新手机号码：</label>
                        <div class="f-n">
                            <input type="text" class="txt phone" />
                            <a href="#" class="fbtn min-white send-code">
                                获取验证码
                            </a>
                            <span class="dia-green f12">发送成功</span>
                            <br>
                            <em class="valid"></em>
                        </div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">请输入验证码：</label>
                        <div class="f-n">
                            <input type="text" class="txt code" />
                            <br>
                            <em class="valid"></em>
                        </div>
                    </div>
                    <div class="fipt">
                       <label class="f-h">请输入支付密码：</label>
                       <div class="f-n">
                            <input type="password" class="txt pwd" />
                            <br>
                            <em class="valid"></em>
                       </div>
                    </div>
                </div>
               <div class="modbtn"><a class="fbtn submit" href="#">确认修改</a></div>
            </div>
        </div>
    </div>
</div>
<div class="dialog" id="change_card1">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>添加银行卡</div>
            <div class="modbox">
                <p class="ptxt"><b>提款绑定的银行卡开户行必须与您之前注册的实名一致，<br>否则无法提款。请确认信息无误后再提交！</b></p>
                <div class="dia-bank-ipt">
                    <div class="fipt">
                        <label class="f-h">银行开户人姓名：</label>
                        <div class="f-n"><b>${userinfo.displayTrueName}</b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">提款银行：</label>
                        <div class="f-n">
                        <select class="bank">
                            <#list drawbanks as x>
                            <#if x??>
                            <option value="${x.bankId}">${x.bank}</option>
                            </#if>
                            </#list>
                        </select>
                        <br>
                        <em class="valid"></em>
                        </div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">银行卡卡号：</label>
                        <div class="f-n">
                            <input type="text" class="txt bdr bank_n" />
                            <span class="dia-red valid"></span>
                            <br /><span class="dia-gray">仅支持提款身份认证为<b>${userinfo.displayTrueName!"-"}</b>的银行借记卡</span>
                            <div class="bankcard-tip">
                                <span></span>
                                <sup class="ct"></sup>
                                <sub class="ct"></sub>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="reqtip">
                <b>提款手续费及到账时间</b>
                <p class="modpli"><i></i>本站目前支持提款至<em>${(drawbanks?size)!12}</em>家银行，每笔提款金额最少为<em>${minAmount/100}</em>元。</p>
                <p class="modpli"><i></i>提款手续费由银行收取，搜狗彩票为您提供补贴，您每笔仅需向银行支付<em>1</em>元钱手续费。</p>
                <p class="modpli"><i></i>搜狗彩票会第一时间为您办理您的提款申请。</p>
                <p class="modpli"><i></i>您的提款将在三个工作日左右到账，具体到账时间由银行决定。</p>
            </div>
            <div class="modbtn">
                <a class="fbtn bind" href="#">确认绑定</a>
            </div>
        </div>
    </div>
</div>
<div class="dialog" id="change_card2">
    <div class="modal">
        <div class="modcon">
            <div class="title"><i title="关闭" class="close dialog-close">&times;</i>温馨提示</div>
            <div class="modbox">
                <p class="ptxt"><b>提款绑定的银行卡开户行必须与您之前注册的实名一致，<br />否则无法提款。请确认信息无误后再提交！</b></p>
                <div class="dia-bank-ipt">
                    <div class="fipt">
                        <label class="f-h">银行开户人姓名：</label>
                        <div class="f-n"><b>${userinfo.displayTrueName}</b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">提款银行：</label>
                        <div class="f-n"><b class="bank_name"></b></div>
                    </div>
                    <div class="fipt">
                        <label class="f-h">银行卡号：</label>
                        <div class="f-n"><b class="bank_n"></b></div>
                    </div>
                    <div class="modbtn">
                        <a class="fbtn submit" href="#">确认提交</a>&nbsp;&nbsp;
                        <a class="fbtn btn-white back" href="#">返回修改</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "../../common/footer.ftl" />
<#include "../include/u-footer.ftl" />
</body>
</html>