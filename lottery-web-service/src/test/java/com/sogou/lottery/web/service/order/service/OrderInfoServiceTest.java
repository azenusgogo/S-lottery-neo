package com.sogou.lottery.web.service.order.service;

import com.sogou.lottery.web.service.user.dto.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 描述：订单信息查询测试
 *
 * @author haojiaqi
 */
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = false)
public class OrderInfoServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static Log log = LogFactory.getLog(OrderInfoServiceTest.class);

    @Before
    public void init() {
    }

    @Autowired
    OrderService orderService;

    @Test
    public void queryUserBetsOrderListTest() {
        OrderQueryDto orderQueryDto = new OrderQueryDto();
        orderQueryDto.setUserId("liaoxu@sogou-inc.com");
        orderQueryDto.setTimeLevel(1);//默认是1
        orderQueryDto.setNoPayOrderFlag(false);
        orderQueryDto.setTimeValidOrderFlag(null);
        orderQueryDto.setPageNo(1);
        orderQueryDto.setPageSize(10);
        OrderQueryResultDto orderQueryResultDto = orderService.findUserOrderList(orderQueryDto);
        log.info("orderQueryResultDto:" + orderQueryResultDto);
        Assert.assertTrue(orderQueryResultDto.getOrderQueryDto().isSuccess());
    }


    @Test
    public void queryUserNopayOrderListTest() {
        OrderQueryDto orderQueryDto = new OrderQueryDto();
        orderQueryDto.setUserId("sgcaipiao@sogou.com");
        orderQueryDto.setNoPayOrderFlag(true);
        orderQueryDto.setPrizeStatus(null);
        orderQueryDto.setTimeLevel(null);
        OrderQueryResultDto orderQueryResultDto = orderService.findUserOrderList(orderQueryDto);
        log.info("orderQueryResultDto:" + orderQueryResultDto);
        Assert.assertTrue(orderQueryResultDto.getOrderQueryDto().isSuccess());
    }

    @Test
    public void getUserOrderDetailByPageTest(){
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setOrderId("14032819000000000005");
        orderDetailDto.setPageSize(10);
        orderDetailDto.setPageNo(1);
        orderDetailDto =  orderService.getUserOrderDetail(orderDetailDto);
        orderDetailDto = new OrderDetailDto();
        orderDetailDto.setOrderId("14032819000000000005");
        orderDetailDto.setSplitFlag(true);
        orderDetailDto = orderService.findStakeBetListByOrderByPage(orderDetailDto);

    }



}
