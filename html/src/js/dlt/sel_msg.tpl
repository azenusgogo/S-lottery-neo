<%if (redNum == 0 && blueNum == 0){%>
    至少选取5个前区号码，2个后区号码
<%}else{%>
    您选了（<%=redNum%>前区号码 + <%=blueNum%>后区号码）,请至少再选<%if (5-redNum>0){%><%=(5-redNum)%>个红前区号码<%}%><%if (blueNum<2){%><%=2-blueNum%>个后区号码<%}%>
<%}%>
