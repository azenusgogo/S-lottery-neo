<table>
    <% if(splited) { %>
    <% for(var i = 0;i<betNumbers.length;i++){ %>
    <% var item = betNumbers[i] %>
    <tbody>
        <tr class="i1">
            <td colspan="5" class="lt info">
                <span><b>出票状态：</b><%=item.statusDesc%></span>
                <span class="m">
                    <b>彩票标识码：</b><%=item.ticketSerialNo || "-"%><i class="cp_sign"></i>
                    <div class="ball-list-tips">
                        <span class="top1 ct"></span>
                        <span class="top2 ct"></span>
                        <p>您彩票的唯一编码，由彩票中心提供，用以识别您的彩票</p>
                    </div>
                </span>
                <span><b>中奖状态：</b><%=item.prizeStatusDesc%></span>
            </td>
        </tr>
        <tr class="i2">
            <th class="t1">序号</th>
            <th class="t2">过关方式</th>
            <th class="t3">出票信息</th>
            <th class="t4">注数(倍数)</th>
            <th class="t5">奖金</th>
        </tr>
        <tr class="i3">
            <td><%=nowPage*pageSize + i + 1%></td>
            <td><%=item.passWay%></td>
            <td class="lt"><%=item.betNumberCn%></td>
            <td><%=item.stakeAmount/200%>（<%=item.betTimes%>倍）</td>
            <td>
            <% if(item.stakeBonus > 0){ %>
                <b class="red"><%=(item.stakeBonus/100).toFixed(2)%></b>
            <% }else{ %>
                0.00
            <% } %>
            </td>
        </tr>
    </tbody>
    <% } %>
    <% }else{ %>
    <tbody>
        <tr class="i2">
            <th class="t1">序号</th>
            <th class="t2">过关方式</th>
            <th class="t3">出票信息</th>
            <th class="t4">注数(倍数)</th>
            <th class="t5">奖金</th>
        </tr>
        <% for(var i = 0;i<betNumbers.length;i++){ %>
        <% var item = betNumbers[i] %>
        <tr class="i3">
            <td><%=nowPage*pageSize + i + 1%></td>
            <td><%=item.passWay%></td>
            <td class="lt"><%=item.betNumberCn%></td>
            <td><%=item.stakeAmount/200%>（<%=item.betTimes%>倍）</td>
            <td>&minus;</td>
        </tr>
        <% } %>
    </tbody>
    <% } %>
</table>