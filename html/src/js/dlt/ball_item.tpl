<%var is_red_dm = (typeof red_dm == "undefined" || !red_dm.length)?false:true%>
<%var is_blue_dm = (typeof blue_dm == "undefined" || !blue_dm.length)?false:true%>
<%var add = (typeof add == "undefined" || !add)?0:count%>
<div class="l">
    <%if(is_red_dm || is_blue_dm){%>
        胆拖
    <%}else{%>
        <%if (red.length>5 || blue.length>2){%>
            复式
        <%}else{%>
            单式
        <%}%>
    <%}%>
</div>
<div class="c">
    <%if(is_red_dm){%><%=red_dm.join(" ")%>$<%}%><%=red.join(" ")%> +
    <span>
     <%if(is_blue_dm){%><%=blue_dm.join(" ")%>$<%}%><%=blue.join(" ")%>
    </span>
    <em>[</em><%=count%> <em>注,</em><%=count*2+add%> <em>元]</em>
</div>
<div class="r">
    <a href="#" class="edit">修改</a>
    <a href="#" class="del">删除</a>
</div>