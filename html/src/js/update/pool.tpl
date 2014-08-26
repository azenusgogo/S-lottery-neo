
<% var game = object.game; %>
<% var gameId =  game.gameId%><%// ssq,dlt,qxc,qlc,k3js,k3gx,k3jl,f14,f9%>

<% if (gameId == "ssq" || gameId == "dlt" || gameId == "qxc" || gameId == "qlc"){ %>
    <% var awardPool = object.awardPool %>
    <% if (awardPool){ %>
        <% var arr = awardPool.split(":") %>
        <% var arr1 = arr[0].split("_") %>
        <p>
            <strong><% if (arr1[0]>0){ %><%=arr1[0]%><em>亿</em><% } %><% if (arr1[1]>0){ %><%=arr1[1]%><em>万</em><% } %><% if (arr1[2]>0){ %><%=arr1[2]%><em>元<% } %></em></strong><br />
            <span>至少可开出<i><%=arr[1]%></i>注500万大奖</span>
        </p>
    <% } else { %>
        <p>
            <b>等待公布...</b><br />
            每周二、四、日晚上21:30开奖
        </p>
    <% } %>
<% } else if (gameId == "k3js" || gameId == "k3gx" || gameId == "k3jl") {%>
    <% var openingAwardPeriodNo = object.openingAwardPeriodNo %>
    <% var periodNo = openingAwardPeriodNo ? openingAwardPeriodNo : object.awardList[0].periodNo %>
    <h4><%=game.gameCn%> 第<span><%=periodNo %></span>期 开奖</h4>
    <div>
        <% if (openingAwardPeriodNo){ %>
            开奖号码获取中...
        <% } else { %>
            <% var first = object.awardList[0] %>
            <% var item = first.prizeNumber.split(",")[0].split(" "); %>
            <% for (var i = 0,len = item.length; i<len; i++){ %>
            <i class="r_<%=item[i]%>"></i>
            <% } %>
        <% } %>
    </div>
    <% var No = object.availablePeriod.periodNo.slice(-2) - 1 %>
    <% var No1 =  gameId == "k3jl" ? (79 - No)  : gameId == "k3js" ? (82 - No) : (78 - No) %>
    <p>今天已售 <%=No%> 期，还剩 <%=No1%> 期</p>
<% } else if(gameId == "f14" || gameId == "f9") {%>
    
<% } %>





