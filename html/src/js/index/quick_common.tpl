<% if (gameId == "ssq") { %>
    <% var redMax = 33 %>
    <% var blueMax = 16 %>
<% } else if (gameId == "dlt") { %>
    <% var redMax = 35 %>
    <% var blueMax = 12 %>
<% } else if (gameId == "qxc") { %>
    <% var redMax = 9 %>
<% } else if (gameId == "qlc") { %>
    <% var redMax = 30 %>
<% } %>
<form action="/<%=gameId%>/" method="post">
    <input type="hidden" name="quickBetNumber">
</form>
<div class="info">
    <span><b><%=gameCn%></b>&nbsp;&nbsp;
	第<b><%=periodNo%></b>期</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    奖池：<em><b><%=bonusPool || "-"%></b></em>
</div>
<div class="ball">
    <div class="ball-num">
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <% if (gameId == "ssq") { %>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <i class="home-ico blue"><input type="text" maxlength="2" data-max="<%=blueMax%>"/></i>
        <% } else if (gameId == "dlt") { %>
        <i class="home-ico blue"><input type="text" maxlength="2" data-max="<%=blueMax%>"/></i>
        <i class="home-ico blue"><input type="text" maxlength="2" data-max="<%=blueMax%>"/></i>
        <% } else if (gameId == "qxc" || gameId == "qlc") { %>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <i class="home-ico"><input type="text" maxlength="2" data-max="<%=redMax%>"/></i>
        <% } %>
    </div>
    <div class="go">
        <a href="" class="home-tou-btn">立即投注</a>
        <a href="" class="change">换一注</a>
    </div>
</div>
<div class="time">
    <span>截止时间还有：<em data-endTime="<%=endTime%>" data-gameId="<%=gameId%>"></em></span>
    开奖时间：2014年2月14日 21:35 <i>今日开奖</i>
</div>