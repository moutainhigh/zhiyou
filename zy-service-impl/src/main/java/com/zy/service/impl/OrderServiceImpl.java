package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.Area.AreaType;
import com.zy.mapper.AreaMapper;
import com.zy.mapper.OrderMapper;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private AreaMapper areaMapper;
	
	@Override
	public Order create(OrderCreateDto orderCreateDto) {
		return null;
	}

	@Override
	public void pay(@NotNull Long id) {
//		fncComponent.payOrder(id);
	}

	@Override
	public void cancel(@NotNull Long id) {
		throw new UnsupportedOperationException(); // 暂不支持主动取消订单
	}

	@Override
	public Page<Order> findPage(@NotNull OrderQueryModel orderQueryModel) {
		if (orderQueryModel.getPageNumber() == null)
			orderQueryModel.setPageNumber(0);
		if (orderQueryModel.getPageSize() == null)
			orderQueryModel.setPageSize(20);
		long total = orderMapper.count(orderQueryModel);
		List<Order> data = orderMapper.findAll(orderQueryModel);
		Page<Order> page = new Page<>();
		page.setPageNumber(orderQueryModel.getPageNumber());
		page.setPageSize(orderQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Order findOne(@NotNull Long id) {
		return orderMapper.findOne(id);
	}

	@Override
	public Order findBySn(@NotBlank String sn) {
		return orderMapper.findBySn(sn);
	}

	@Override
	public void deliver(@NotNull Order order) {
		String sn = order.getSn();
		validate(sn, NOT_BLANK, "sn is null");
		Order persistence = orderMapper.findBySn(sn);
		validate(persistence, NOT_NULL, "order sn" + sn + " not found");
		validate(persistence.getOrderStatus(), v -> v == OrderStatus.已支付, "order status error: " + persistence.getOrderStatus());
		
		Long receiverAreaId = order.getReceiverAreaId();
		validate(receiverAreaId, NOT_NULL, "receiver area id is null");
		Area area = areaMapper.findOne(receiverAreaId);
		validate(area, NOT_NULL, "receiver area id" + receiverAreaId + " not found");
		validate(area.getAreaType(), v -> v == AreaType.区, "area type error: " + area.getAreaType());
		
		persistence.setOrderStatus(OrderStatus.已发货);
		persistence.setReceiverAreaId(receiverAreaId);
		persistence.setReceiverProvince(order.getReceiverProvince());
		persistence.setReceiverCity(order.getReceiverCity());
		persistence.setReceiverDistrict(order.getReceiverDistrict());
		persistence.setReceiverAddress(order.getReceiverAddress());
		persistence.setReceiverRealname(order.getReceiverRealname());
		persistence.setReceiverPhone(order.getReceiverPhone());
		persistence.setLogisticsName(order.getLogisticsName());
		persistence.setLogisticsSn(order.getLogisticsSn());
		persistence.setDeliveredTime(new Date());
		validate(persistence);
		
		orderMapper.update(persistence);
		
	}

	@Override
	public void confirmDelivery(@NotBlank String sn) {
		Order persistence = orderMapper.findBySn(sn);
		validate(persistence, NOT_NULL, "order sn" + sn + " not found");
		validate(persistence.getOrderStatus(), v -> v == OrderStatus.已发货, "order status error: " + persistence.getOrderStatus());
		
		Order orderForMerge = new Order();
		orderForMerge.setId(persistence.getId());
		orderForMerge.setOrderStatus(OrderStatus.已完成);
		orderMapper.merge(orderForMerge, "orderStatus");
		
	}

}
