<% var nickName = result.userId %>
<% var title = nickName %>
<% if (nickName.length > 10) { %>
<% nickName = nickName.slice(0,10) + "..." %>
<% } %>
欢迎您，<a href="/login/user/order/bets.html" title="<%=title%>" class="nick"><%=nickName%></a>
&nbsp;&nbsp;&nbsp;
 可用余额：<em class="m1">********</em> 元&nbsp;&nbsp;
<a href="#" class="uout">显示</a>
<a class="anchor_hover" href="/login/user/order/bets.html">购彩记录</a>
<a class="anchor_hover" href="/login/user/trans/trans.html">资金明细</a>
<a class="anchor_hover" href="/login/charge/pre.html">充值</a>
<a class="anchor_hover" href="/login/user/withdraw.html">提款</a>
<a class="anchor_hover" href="/login/user/pwd/change.html">修改密码</a>
<a href="#" class="exit">退出登录</a>