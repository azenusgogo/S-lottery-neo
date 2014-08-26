<% var first = object.awardList[0] %>
<% var arr =  first.prizeNumber.split(",") %>
<% var number = first.periodNo.slice(-2) %>
<span class="gray"><%=number%></span>
<em class="code"><%=arr[0]%></em>
<em class="sum"><%=arr[1]%></em>
<% if (arr[6] == 1){ %>
<em class="orange">三连号</em>
<% } else if (arr[4] == 1){ %>
<em class="blue">三不同</em>
<% } else if (arr[5] == 1){ %>
<em class="green">三同号</em>
<% } else { %>
<em class="gray">二同号</em>
<% } %>