<table>
    <tr>
    	<th class="m1">场次</th>
    	<th class="m2">对阵</th>
    	<th class="m3">投注内容</th>
    	<th class="m4">赛果</th>
    </tr>
    <% for(var i = 0;i<schemas.length;i++) { %>
    <tr>
    	<td><%=schemas[i].matchNoCn%></td>
    	<td>
            <%=schemas[i].info.homeTeamName%> VS <%=schemas[i].info.awayTeamName%>
            <% if (schemas[i].danCn) { %><br />(胆)<% } %>
        </td>
    	<td><%=schemas[i].betNumberCn%></td>
    	<td><%=schemas[i].lottResCns.join(",") || "-"%></td>
    </tr>
    <% } %>
</table>