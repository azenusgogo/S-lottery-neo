<%for(var i=n;i<len+1;i++){%>
<option value="<%=i%>"<%if(i==n){%> selected<%}%>><%=i%></option>
<%}%>