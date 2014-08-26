<%var dm = (typeof red_dmNum == "undefined")?false:true%>
<%var red_dmNum = typeof red_dmNum =="undefined" ?0 :red_dmNum%>
您选了<em><%=redNum+red_dmNum%></em>个红球，
<%if(dm){%>（<em><%=red_dmNum%></em>个红球胆码，<em><%=redNum%></em>个红球拖码）<%}%>
<span><%=blueNum%></span>个蓝球，共<em><%=count%></em>注，<em><%=count*2%></em>元




