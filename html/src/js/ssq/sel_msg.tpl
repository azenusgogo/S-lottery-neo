<%if (redNum == 0 && blueNum == 0){%>
    至少选取6个红球，1个蓝球
<%}else{%>
    您选了（<%=redNum%>红 + <%=blueNum%>蓝）,请至少再选<%if (6-redNum>0){%><%=(6-redNum)%>个红球<%}%><%if (blueNum==0){%>1个蓝球<%}%>
<%}%>
