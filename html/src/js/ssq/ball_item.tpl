<%var is_red_dm = (typeof red_dm == "undefined" || !red_dm.length)?false:true%>
<div class="l">
    <%if(is_red_dm){%>
        胆拖
    <%}else{%>
        <%if (red.length>6 || blue.length>1){%>
            复式
        <%}else{%>
            单式
        <%}%>
    <%}%>
</div>
<div class="c">
    <%if(is_red_dm){%><%=red_dm.join(" ")%>$<%}%><%=red.join(" ")%> + <span><%=blue.join(" ")%></span>
    <em>[</em><%=count%> <em>注,</em><%=count*2%> <em>元]</em>
</div>
<div class="r">
    <a href="#" class="edit">修改</a>
    <a href="#" class="del">删除</a>
</div>