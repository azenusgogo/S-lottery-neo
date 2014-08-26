<form action="<%=gameId%>" method="post">
    <input type="hidden" name="quickBetNumber">
</form>
<div class="info">
    <span><b><%=gameCn%></b>&nbsp;&nbsp;
    第<b><%=periodNo%></b>期</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    [和值玩法]
</div>
<div class="ball">
    <div class="k3ball">
        <i class="r_5"></i><b>+</b>
        <i class="r_4"></i><b>+</b>
        <i class="r_6"></i><b>=</b>
        <i class="ret">
            <span class="sum"></span>
            <span class="cash"></span>
        </i>
    </div>
    <div class="go">
        <a href="#" class="home-tou-btn">立即投注</a>
        <a href="#" class="change">换一注</a>
    </div>
</div>
<div class="time">
    <span>截止时间还有：<em data-endTime="<%=endTime%>" data-gameId="<%=gameId%>"></em></span>
    开奖时间：每十分钟一期
</div>




