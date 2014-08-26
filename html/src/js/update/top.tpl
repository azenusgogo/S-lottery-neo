<% var game = object.game; %>
<% var availablePeriod = object.availablePeriod; %>
<% var gameType =  game.gameType%><%//  1普通，2高频，3传统足彩，4竞技彩，5单场%>
<% var gameId =  game.gameId%><%// ssq,dlt,qxc,qlc,k3js,k3gx,k3jl,f14,f9%>
<div class="cpname"><h1><%=game.gameCn%></h1> 第 <b><%=availablePeriod.periodNo%></b> 期</div>
投注还剩：<em id="ball_timer"></em>&nbsp;&nbsp;
<% if (gameType == 1){ %>
购彩截止：<%=formatDate(new Date(availablePeriod.endTime),"{yyyy}-{MM}-{dd} <b>{HH}:{mm}</b>")%>&nbsp;
<% if (gameId == "ssq"){ %>
周二、四、日 <b>21:35</b> 开奖
<% } else if (gameId == "dlt") {%>
周一、三、六 <b>20:30</b> 开奖
<% } else if (gameId == "qxc") {%>
周二、五、日 <b>20:30</b> 开奖
<% } else if (gameId == "qlc") {%>
周一、三、五 <b>21:30</b> 开奖
<% } %>
<% } else if (gameType == 2) { %>
<% var period = gameId == "k3jl" ? 79 : gameId == "k3js" ? 82 : 78 %>
<% var time = gameId == "k3jl" ? "8:20&#65374;21:40" : gameId == "k3js" ? "8:30&#65374;22:10" : "9:28&#65374;22:28" %>
销售时间：<%=time%>（<%=period%>期）10分钟开奖 返奖率<b>59%</b>
<% } %>
<% if (object.todayOpenAward == 1){ %>
<span>今日开奖</span>
<% } else {%>
<span>正在销售</span>
<% } %>




