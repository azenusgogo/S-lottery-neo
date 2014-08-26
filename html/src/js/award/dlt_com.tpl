<table class="t-intro">
    <% if(is_new){ %>
    <tr>
        <th class="bg">奖级</th>
        <th class="bg">一等奖</th>
        <th class="bg">二等奖</th>
        <th class="bg">三等奖</th>
        <th class="bg">四等奖</th>
        <th class="bg">五等奖</th>
        <th class="bg">六等奖</th>
    </tr>
    <tr>
        <td class="bg">中奖条件</td>
        <td>5+2</td>
        <td>5+1</td>
        <td>5+0,4+2</td>
        <td>4+1,3+2</td>
        <td>4+0,3+1,2+2</td>
        <td>3+0,2+1,1+2,0+2</td>
    </tr>
    <% }else{ %>
    <tr>
        <th class="bg">奖级</th>
        <th class="bg">一等奖</th>
        <th class="bg">二等奖</th>
        <th class="bg">三等奖</th>
        <th class="bg">四等奖</th>
        <th class="bg">五等奖</th>
        <th class="bg">六等奖</th>
        <th class="bg">七等奖</th>
        <th class="bg">八等奖</th>
    </tr>
    <tr>
        <td class="bg">中奖条件</td>
        <td>5+2</td>
        <td>5+1</td>
        <td>5+0</td>
        <td>4+2</td>
        <td>4+1</td>
        <td>4+0,3+2</td>
        <td>3+1,2+2</td>
        <td>3+0,2+1,1+2,0+2</td>
    </tr>
    <% } %>
    <tr>
        <td class="bg">中奖注数</td>
        <% for(var z=0;z<zhuNo.length;z++) { %>
        <td><%=zhuNo[z]%>注</td>
        <% } %>
    </tr>
    <tr>
        <td class="bg">单注奖金</td>
        <% for(var i=0;i<award.length;i++) { %>
        <td><%=format(award[i].toString())%>元</td>
        <% } %>
    </tr>
    <tr>
        <td class="bg">中奖奖金</td>
        <% for(var i=0;i<zhuNo.length;i++) { %>
        <td>
            <% if(zhuNo[i] > 0){%>
            <em><%=format((award[i]*zhuNo[i]).toString())%></em>元
            <% }else{ %>
            0元
            <% } %>
        </td>
        <% } %>
    </tr>
</table>