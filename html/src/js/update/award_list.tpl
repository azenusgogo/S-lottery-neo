<% var awardList = object.awardList %>
<% var first = awardList[0] %>
<% var red_n = first.prizeNumber.split(":")[0] %>
<% var red_arr = red_n.split(" ") %>
<% var blue_n = first.prizeNumber.split(":")[1] %>
<% var blue_arr = blue_n ? blue_n.split(" ") : []%>
<% var bonusLevelDetail = first.bonusLevelDetail %>
<% var zh = ["一","二","三","四","五","六","七","八","九","十"] %>
<strong><%=first.periodNo%></strong>期 
<em><%=formatDate(new Date(first.offcialAwardTime),"{yyyy}-{MM}-{dd}")%></em>
<div class="kj-num">
    <% for(var i=0,len=red_arr.length; i<len; i++){ %>
    <i><%=red_arr[i]%></i>
    <% } %>
    <% for(var i=0,len=blue_arr.length; i<len; i++){ %>
    <i class="blue-ball-min"><%=blue_arr[i]%></i>
    <% } %>
</div>
<p>
    <% if (bonusLevelDetail){ %>
    <% var arr = bonusLevelDetail.split(";") %>
    <% for(var i=0,len=(object.game.gameId == "ssq" ? 2 : 3); i<len; i++){ %>
    <% var item = arr[i].split("_") %>
    <%=zh[item[0]-1]%>等奖：<em><%=item[1]%></em>注，每注<em><%=formatCurrency(item[2])%></em>元<br />
    <% } %>
    <% } %>
</p>
<table cellspacing="0" width="198">
    <tr>
        <th>期次</th>
        <th>开奖号码</th>
    </tr>
    <% for(var i=0,len = (awardList.length > 6 ? 6 : awardList.length); i<len; i++){ %>
    <% var item = awardList[i] %>
    <% var prizeNumber = item.prizeNumber.split(":") %>
    <tr>
        <td><%=item.periodNo%>期</td>
        <td class="wq-num"><%=prizeNumber[0]%> <b><%=prizeNumber[1]%></b></td>
    </tr>
    <% } %>
</table>