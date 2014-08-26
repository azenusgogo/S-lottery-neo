<% var dstr = ["今天","昨天","前天"] %>
<% var hstr = (type == 0) ? "统计以下几种形态，"+dstr[index]+"出现多少次" : "统计和值号码，"+dstr[index]+"出现多少次" %>
<li class="intro"><%=hstr%></li>
<% if (list) { %>
    <% for (var i = 0, len = list.length; i < len; i++ ) { %>
        <% var arr = list[i].split(":") %>
        <% var className = "" %>
        <% var width =  Math.floor(arr[1] / maxn * 100)%>
        <% if (maxn == arr[1]) { %>
            <% className = "cblue" %>
        <% } %>
        <li>
            <% if (type == 0) { %>
                <span><%=arr[0]%></span>
                <div class="progress">
                    <div class="bar ct <%=className%>" style="width:<%=width%>%"><%=width%>%</div>
                </div>
                <em><b><%=arr[1]%></b>次</em>
            <% } else if (type == 1) { %>
                <i><%=arr[0]%></i>
                <div class="progress sum-bar">
                    <div class="bar ct <%=className%>" style="width:<%=width%>%"><%=width%>%</div>
                </div>
                <em><b><%=arr[1]%></b>次</em>
            <% } %>
        </li>
    <% } %>
<% } else { %>
    <% if (type == 0) { %>
        <% var str = ["三同号","三不同号","二同号","三不同号","三同号"] %>
        <% for (var i = 0; i < 5; i++ ) { %>
            <li>
                <span><%=str[i]%></span>
                <div class="progress">
                    <div class="bar ct" style="width:0%">0%</div>
                </div>
                <em><b>0</b>次</em>
            </li>
        <% } %>
    <% } else if (type == 1) { %>
        <% for (var i = 3; i < 19; i++ ) { %>
            <li>
                <i><%=i%></i>
                <div class="progress sum-bar">
                    <div class="bar ct" style="width:0%">0%</div>
                </div>
                <em><b>0</b>次</em>
            </li>
        <% } %>
    <% } %>
<% } %>
