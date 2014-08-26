<%var red_dm = typeof red_dm == "undefined" ?0 :red_dm%>
<div class="l">
<%if(red_dm.length){%>
    胆拖
<%}else{%>
    <%if (red.length>7){%>
    复式
    <%}else{%>
    单式
    <%}%>
<%}%>
</div>
<div class="c">
    <%if (red_dm && red_dm.length>0){%><%=red_dm.join(" ")%>$<%}%><%=red.join(" ")%>
    <em>[</em><%=count%> <em>注,</em><%=count*2%> <em>元]</em>
</div>
<div class="r">
    <a href="#" class="edit">修改</a>
    <a href="#" class="del">删除</a>
</div>