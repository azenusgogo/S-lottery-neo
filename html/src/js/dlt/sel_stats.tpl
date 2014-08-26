<%var dm = (typeof red_dmNum =="undefined" || typeof blue_dmNum =="undefined") ?false :true%>
<%var red_dmNum = typeof red_dmNum =="undefined" ?0 :red_dmNum%>
<%var blue_dmNum = typeof blue_dmNum =="undefined" ?0 :blue_dmNum%>
您选了<em><%=redNum+red_dmNum%></em>个前区号码，
<%if(dm){%>（<em><%=red_dmNum%></em>个胆码，<em><%=redNum%></em>个拖码）<%}%>
<span><%=blueNum+blue_dmNum%></span>个后区号码
<%if(dm){%>(<span><%=blue_dmNum%></span>个胆码，<span><%=blueNum%></span>个拖码)<%}%>，
共<em><%=count%></em>注，<em><%=count*2+add%></em>元




