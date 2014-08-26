---做一遍删除，测试使用user_id=caipiaotest@sohu.com
delete from TB_USER_ORDERS where payer_user_id='caipiaotest@sohu.com';
delete from tb_pay_orders where user_id='caipiaotest@sohu.com';
delete from tb_raw_bet_numbers where user_order_id=(select business_id from tb_pay_orders where user_id='caipiaotest@sohu.com');