<% var awardList = object.awardList %>
<table class="k3-old-tbl">
    <tr>
        <th>期次</th>
        <th>开奖号</th>
        <th>和值</th>
        <th>类型</th>
    </tr>
    <% for(var i=0,len = awardList.length; i<len; i++){ %>
    <% var item = awardList[i] %>
    <% var arr = item.prizeNumber.split(",") %>
    <tr>
        <td><%=item.periodNo%></td>
        <td class="red"><%=arr[0]%></td>
        <td><%=arr[1]%></td>
        <% if (arr[6] == 1){ %>
        <td class="orange">三连号</td>
        <% } else if (arr[4] == 1){ %>
        <td class="blue">三不同</td>
        <% } else if (arr[5] == 1){ %>
        <td class="green">三同号</td>
        <% } else { %>
        <td class="gray">二同号</td>
        <% } %>
    </tr>
    <% } %>
</table>


