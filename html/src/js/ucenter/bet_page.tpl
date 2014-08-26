<% var stakeBetInfoDtoList = result.stakeBetInfoDtoList %>
<% var betList = [],cpSign,s,tHz,is_zhui,num_tou %>
<% var tRed = "("+ kRed +")" %>
<% var tBlue = "("+ kBlue +")" %>
<% var tAll = "("+ kRed + kBlue +")" %>
<% var qRed = kRed.split("|") %>

<table class="mynum-tbl">
<% if(!result.splitFlag){ %>
    <tr>
        <th width="120">第<%=stakeBetInfoDtoList[0].betNumber.periodNo%>期</th>
        <th class="lth">选号方案</th>
        <th>方式</th>
        <th width="80">倍数</th>
    </tr>
<% } %>
<% for (var i = 0;i<stakeBetInfoDtoList.length;i++) {%>
    <% if(result.splitFlag) { %>
    <% betList = stakeBetInfoDtoList[i].betNumberList %>
    <% cpSign = (stakeBetInfoDtoList[i].stakeOrder.officialTicketId || "-").split(",") %>
    <tr>
        <td colspan="4" class="mynum-stat">
            <b>出票状态：</b><%=stakeBetInfoDtoList[i].stakeOrder.stakeOrderStatusDesc%>&nbsp;&nbsp;&nbsp;&nbsp;
            <b>彩票标识码：</b>
            <% if(cpSign.length > 1) {%>
            <span class="cpsign_list"><%=cpSign[0]%>..</span>
            <div class="ball-list-tips" style="width:auto">
                <span class="top1 ct"></span>
                <span class="top2 ct"></span>
                <ul>
                <% for(var x=0;x<cpSign.length;x++){ %>
                    <li><%=cpSign[x]%></li>
                <% } %>
                </ul>
            </div>
            <% }else{ %>
            <%=cpSign[0]%>&nbsp;&nbsp;
            <% } %>
            <i class="cp_sign"></i>
            <div class="ball-list-tips">
                <span class="top1 ct"></span>
                <span class="top2 ct"></span>
                <p>您彩票的唯一编码，由彩票中心提供，用以识别您的彩票</p>
            </div>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <b>中奖状态：</b><%=stakeBetInfoDtoList[i].stakeOrder.stakePrizeStatusDesc%>
        </td>
    </tr>
    <tr>
        <th width="120">第<%=stakeBetInfoDtoList[i].betNumberList[0].periodNo%>期</th>
        <th class="lth">选号方案</th>
        <th>方式</th>
        <th width="80">倍数</th>
    </tr>
    <% }else{ %>
    <% betList = [stakeBetInfoDtoList[i].betNumber] %>

    <% } %>
    <% for (var j = 0;j<betList.length;j++){ %>
    <% var item = betList[j] %>
    <% var localBets = item.localBetNumber || "" %>
    <tr>
        <td>
            <% if(result.splitFlag) { %>
            <%= j+1 %>
            <% }else{ %>
            <%= i+1+(nowPage*10) %>
            <% } %>
        </td>
        <td class="mynum">
            <% if("ssq,dlt".indexOf(item.gameId) != -1) {%>
                <% if(localBets.indexOf("+") != -1) {%>
                <% is_zhui = 1 %>
                <%= localBets.split(":")[0].substring(1).replace(new RegExp(tRed,"g"),"<b>$1</b>") %>
                <% }else{ %>
                <% is_zhui = 0 %>
                <%= localBets.split(":")[0].replace(new RegExp(tRed,"g"),"<b>$1</b>") %>
                <% } %>
                + <%= localBets.split(":")[1].replace(new RegExp(tBlue,"g"),"<em>$1</em>") %>
            <% }else if (item.gameId.indexOf("k3") != -1) { %>
                <span>选号：[
                <% if(localBets.indexOf("HZ_") != -1) { %>
                <% tHz = "("+ kHz +")" %>
                和值
                <% }else if (localBets.indexOf("AAA_") != -1) { %>
                    <% if (localBets.indexOf("AAA_*") != -1) { %>
                    三同号通选
                    <% }else{ %>
                    三同号单选
                    <% } %>
                <% }else if (localBets.indexOf("3BT_") != -1) { %>
                    <% if (localBets.split("_")[1].length > 3 && localBets.indexOf("$") == -1){ %>
                    三不同复式
                    <% }else{ %>
                    三不同
                    <% } %>
                <% }else if (localBets.indexOf("3LH_*") != -1) { %>
                    三连号通选
                <% }else if (localBets.indexOf("AA_") != -1) { %>
                    二同号复选
                <% }else if (localBets.indexOf("AAX_") != -1) { %>
                    二同号单选
                <% }else if (localBets.indexOf("2BT_") != -1) { %>
                    <% if (localBets.split("_")[1].length > 2 && localBets.indexOf("$") == -1){ %>
                    二不同复式
                    <% }else{ %>
                    二不同
                    <% } %>
                <% } %>
                <% if(localBets.indexOf(",") != -1) {%>复式<% } %>
                <% if(localBets.indexOf("$") != -1) {%>胆拖<% } %>
                ]</span>
                <% if(localBets.indexOf("HZ_") != -1) { %>
                <%=localBets.split("_")[1].replace(new RegExp(tHz,"g"),"<b>$1</b>")%>
                <% }else{ %>
                <%=localBets.split("_")[1].replace(new RegExp(tRed,"g"),"<b>$1</b>")%>
                <% } %>
            <% }else if(item.gameId == "qlc") { %>
                <%=localBets.replace(new RegExp(tAll,"g"),"<b>$1</b>") %>
            <% }else if("qxc,f14,f9".indexOf(item.gameId) != -1){ %>
                <% num_tou = localBets.split(" ") %>
                <% for(var q=0;q<num_tou.length;q++){ %>
                    <%=num_tou[q].replace(new RegExp("("+qRed[q]+")"),"<b>$1</b>")%> 
                <% } %>
            <% } %>
        </td>
        <td>
            <%=(item.betTypeDesc) ? item.betTypeDesc : "-" %>
            <% if(is_zhui) {%>
            <%="追加"%>
            <% } %>
        </td>
        <td><%=(item.betTimes) ? item.betTimes : "-" %>倍</td>
    </tr>
    <% } %>
<% } %>
</table>