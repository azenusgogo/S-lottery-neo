<div class="l">
    <%if (count>1){%>
        复式
    <%}else{%>
        单式
    <%}%>
</div>
<div class="c">
    <%=row1.join("")%>,
    <%=row2.join("")%>,
    <%=row3.join("")%>,
    <%=row4.join("")%>,
    <%=row5.join("")%>,
    <%=row6.join("")%>,
    <%=row7.join("")%>
    <em>[</em><%=count%> <em>注,</em><%=count*2%> <em>元]</em>
</div>
<div class="r">
    <a href="#" class="edit">修改</a>
    <a href="#" class="del">删除</a>
</div>