<% var nickName = result.nickName || result.partyUserNickName || result.userId %>
<% var title = nickName %>
<% if (nickName.length > 10) { %>
    <% for (var i = 0, j = 0, len = nickName.length; i < len; i++){ %>
        <% j += getBytes(nickName.charAt(i)); %>
        <% if (j >= 7) { %>
            <% break %>
        <% } %>
    <% } %>
    <% nickName = nickName.slice(0,i) + "..." %>
<% } %>
<h3><a href="#" class="goout">退出</a>您好，<span title="<%=title%>"><%=nickName%></span></h3>
<div class="userinfo">
    <b>可用余额：</b><em class="m1"><span class="red">*******</span>元</em> <a href="#" class="show">显示</a><br />
    <b>提款申请：</b><em class="m2">********<!-- <span class="red">***</span>元 --></em>
    <p>
        <a href="/login/charge/pre.html" class="hbtn">充值</a>
        <a href="/login/user/withdraw.html" class="hbtn graybtn">提款</a>
        <a href="/login/user/order/bets.html" class="hbtn graybtn">购彩记录</a>
    </p>
</div>