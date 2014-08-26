<div class="c">
    <%for (var i=0;i<number.length;i++){%>
        <%
        var item = number[i];
        var str_left = "";
        var str_right = "";
        %>
        <%
        if (item.checked){
            str_left = "(";
            str_right = ")";
        }
        %>
        <%=str_left+item.n.join("")+str_right+" "%>
    <%}%>
    <em>[</em><%=count%> <em>注,</em><%=count*2%> <em>元]</em>
</div>
<div class="r">
    <a href="#" class="del">删除</a>
</div>