package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Order;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.dto.OrderSumDto;
import com.zy.model.query.OrderQueryModel;

import java.util.List;
import java.util.Map;

public interface OrderService {

	Order create(OrderCreateDto orderCreateDto);
	
	void cancel(Long id);
	
	Page<Order> findPage(OrderQueryModel orderQueryModel);
	
	Order findOne(Long id);
	
	Order findBySn(String sn);
	
	void confirmPay(Long id); // 确认支付
	
	void rejectPay(Long id, String remark); // 驳回支付

	void deliver(OrderDeliverDto orderDeliverDto); // 发货

	void platformDeliver(OrderDeliverDto orderDeliverDto); // 平台发货
	
	void receive(Long id); // 确认收货

	Long copy(Long orderId); // 复制(转订单)

	void settleUp(Long orderId); // 在订单完成时结算

	void settleUpProfit(String yearAndMonth); // 在订单支付成功后结算
	void settleUpOption(String yearAndMonth); // 重新结算期权奖励
	void settleUpRebate(String yearAndMonth); // 返利奖
	void settleUpDirector(String yearAndMonth); // 董事贡献奖

	void settleUpMonthly(String yearAndMonth); // 月结算

	List<Order> findAll(OrderQueryModel build);
	
	long count(OrderQueryModel build);
	
	OrderSumDto sum(OrderQueryModel orderQueryModel);
	
	void offlinePay(Long orderId, String offlineImage, String offlineMemo);
	
	void delete(Long orderId);
	
	void undelete(Long orderId);

	Map<String,Object> querySalesVolume(OrderQueryModel orderQueryModel);

	Map<String,Object> querySalesVolumeDetail(OrderQueryModel orderQueryModel);

	void deliverStore(OrderDeliverDto orderDeliverDto);//U库发货

	void editOderStoreIn(Long id,Long userId);
}
