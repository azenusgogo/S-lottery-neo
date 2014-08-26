<table class="tool_tbl">
    <tr>
        <th width="120">奖级</th>
        <th width="120">中奖条件</th>
        <th width="150">奖金（元）</th>
        <th class="nbd">中奖注数（注）</th>
    </tr>
    <% if(is_new){ %>
    <tr>
        <td>一等奖</td>
        <td>5+2</td>
        <td><%=format(award[0].toString())%></td>
        <td class="nbd"><em><%=zhu[0]%></em></td>
    </tr>
    <tr>
        <td>二等奖</td>
        <td>5+1</td>
        <td><%=format(award[1].toString())%></td>
        <td class="nbd"><em><%=zhu[1]%></em></td>
    </tr>
    <tr>
        <td>三等奖</td>
        <td>5+0,4+2</td>
        <td><%=format(award[2].toString())%></td>
        <td class="nbd"><em><%=zhu[2]%></em></td>
    </tr>
    <tr>
        <td>四等奖</td>
        <td>4+1,3+2</td>
        <td><%=award[3]%></td>
        <td class="nbd"><em><%=zhu[3]%></em></td>
    </tr>
    <tr>
        <td>五等奖</td>
        <td>4+0,3+1,2+2</td>
        <td><%=award[4]%></td>
        <td class="nbd"><em><%=zhu[4]%></em></td>
    </tr>
    <tr>
        <td>六等奖</td>
        <td>3+0,2+1,1+2,0+2</td>
        <td>5</td>
        <td class="nbd"><em><%=zhu[5]%></em></td>
    </tr>
    <% }else{ %>
    <tr>
        <td>一等奖</td>
        <td>5+2</td>
        <td><%=format(award[0].toString())%></td>
        <td class="nbd"><em><%=zhu[0]%></em></td>
    </tr>
    <tr>
        <td>二等奖</td>
        <td>5+1</td>
        <td><%=format(award[1].toString())%></td>
        <td class="nbd"><em><%=zhu[1]%></em></td>
    </tr>
    <tr>
        <td>三等奖</td>
        <td>5+0</td>
        <td><%=format(award[2].toString())%></td>
        <td class="nbd"><em><%=zhu[2]%></em></td>
    </tr>
    <tr>
        <td>四等奖</td>
        <td>4+2</td>
        <td><%=award[3]%></td>
        <td class="nbd"><em><%=zhu[3]%></em></td>
    </tr>
    <tr>
        <td>五等奖</td>
        <td>4+1</td>
        <td><%=award[4]%></td>
        <td class="nbd"><em><%=zhu[4]%></em></td>
    </tr>
    <tr>
        <td>六等奖</td>
        <td>4+0,3+2</td>
        <td><%=award[5]%></td>
        <td class="nbd"><em><%=zhu[5]%></em></td>
    </tr>
    <tr>
        <td>七等奖</td>
        <td>3+1,2+2</td>
        <td><%=award[6]%></td>
        <td class="nbd"><em><%=zhu[6]%></em></td>
    </tr>
    <tr>
        <td>八等奖</td>
        <td>3+0,2+1,1+2,0+2</td>
        <td>5</td>
        <td class="nbd"><em><%=zhu[0]%></em></td>
    </tr>
    <% } %>
</table>
<div class="total">
    中奖金额：<em><%=format(total)%></em>元
</div>