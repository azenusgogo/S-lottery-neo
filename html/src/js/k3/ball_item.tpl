<div class="l">
    <%
    var text;
    var red_dm;
    red_dm || (red_dm = []);
    var isEdit = 0;
    var join_str = ",";
    switch (bet_type) {
            case 0:
            text = "和值";
            isEdit = 1;
        break;
            case 1:
            text = "三同号通选";
        break;
            case 2:
            text = "三同号单选";
        break;
            case 3:
            text = red_dm.length == 0 ? number.length > 3 ? "三不同号复式" : "三不同号单式" : "三不同号胆拖"
            isEdit = 1;
            join_str = "";
        break;
            case 4:
            text = "三连号通选";
        break;
            case 5:
            text = "二同号复选";
        break;
            case 6:
            text = "二同号单选";
        break;
            case 7:
            text = red_dm.length == 0 ? number.length > 2 ? "二不同号复式" : "二不同号单式" : "二不同号胆拖"
            isEdit = 1;
            join_str = "";
        break;
    }
    %>
    <%=text%>
</div>
<div class="c">
    <%if(red_dm.length>0){%><%=red_dm.join(join_str)%>$<%}%><%=number.join(join_str)%>
    <em>[</em><%=count%> <em>注,</em><%=count*2%> <em>元]</em>
</div>
<div class="r">
    <%if (isEdit){%>
    <a href="#" class="edit">修改</a>
    <%}%>
    <a href="#" class="del">删除</a>
</div>