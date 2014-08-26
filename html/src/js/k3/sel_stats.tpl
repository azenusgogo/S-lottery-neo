您选了<em><%=count%></em>注，共<em><%=count*2%></em>元
<%if(count){%>
若中奖，奖金<em><%=min%></em><%if(max>min){%>至<em><%=max%></em><%}%>元，
您将盈利<em><%=min-count*2%></em><%if(max>min){%>至<em><%=max-count*2%></em><%}%>元
<%}%>