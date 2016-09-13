package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.model.query.PaymentQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.model.query.TransferQueryModel;
import com.zy.service.OrderItemService;
import com.zy.service.PaymentService;
import com.zy.service.ProfitService;
import com.zy.service.TransferService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.formatDate;

@Component
public class OrderComponent {
	
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProfitService profitService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private TransferService transferService;

	@Autowired
	private ProfitComponent profitComponent;
	@Autowired
	private PaymentComponent paymentComponent;
	@Autowired
	private TransferComponent transferComponent;
	@Autowired
	private CacheComponent cacheComponent;
	
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日 HH:mm";


	public OrderAdminFullVo buildAdminFullVo(Order order) {
		OrderAdminFullVo orderAdminFullVo = new OrderAdminFullVo();
		BeanUtils.copyProperties(order, orderAdminFullVo);

		orderAdminFullVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		orderAdminFullVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		orderAdminFullVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		orderAdminFullVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		orderAdminFullVo.setDeliveredTimeLabel(formatDate(order.getDeliveredTime(), TIME_PATTERN));
		
		OrderItem orderItem = orderItemService.findByOrderId(order.getId()).get(0);
		if (orderItem != null) {
			orderAdminFullVo.setImageThumbnail(GcUtils.getThumbnail(orderItem.getImage()));
			orderAdminFullVo.setPrice(orderItem.getPrice());
			orderAdminFullVo.setQuantity(orderItem.getQuantity());
		}
		
		Long orderId = order.getId();
		orderAdminFullVo.setProfits(profitService
				.findAll(ProfitQueryModel.builder().profitTypeIN(new Profit.ProfitType[] {Profit.ProfitType.特级平级奖, Profit.ProfitType.订单收款, Profit.ProfitType.销量奖}).refIdEQ(orderId).build())
				.stream()
				.map(profitComponent::buildAdminVo).collect(Collectors.toList())

		);

		orderAdminFullVo.setPayments(paymentService
				.findAll(PaymentQueryModel.builder().paymentTypeEQ(Payment.PaymentType.订单支付).refIdEQ(orderId).build())
				.stream()
				.map(paymentComponent::buildAdminVo).collect(Collectors.toList())

		);

		orderAdminFullVo.setTransfers(transferService
				.findAll(TransferQueryModel.builder().transferTypeIN(new Transfer.TransferType[] {Transfer.TransferType.一级平级奖, Transfer.TransferType.一级越级奖, Transfer.TransferType.邮费}).refIdEQ(orderId).build())
				.stream()
				.map(transferComponent::buildAdminVo).collect(Collectors.toList())

		);

		orderAdminFullVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(order.getUserId())));
		orderAdminFullVo.setSeller(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(order.getSellerId())));
		return orderAdminFullVo;
	}
	
	public OrderAdminVo buildAdminVo(Order order) {
		OrderAdminVo orderAdminVo = new OrderAdminVo();
		BeanUtils.copyProperties(order, orderAdminVo);
		
		orderAdminVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		orderAdminVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		orderAdminVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		orderAdminVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		
		OrderItem orderItem = orderItemService.findByOrderId(order.getId()).get(0);
		if (orderItem != null) {
			orderAdminVo.setImageThumbnail(GcUtils.getThumbnail(orderItem.getImage()));
			orderAdminVo.setPrice(orderItem.getPrice());
			orderAdminVo.setQuantity(orderItem.getQuantity());
		}
		
		orderAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(order.getUserId())));
		orderAdminVo.setSeller(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(order.getSellerId())));
		return orderAdminVo;
	}
	
	
	public OrderListVo buildListVo(Order order) {
		OrderListVo orderListVo = new OrderListVo();
		BeanUtils.copyProperties(order, orderListVo);
		
		orderListVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), SIMPLE_TIME_PATTERN));
		orderListVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), SIMPLE_TIME_PATTERN));
		orderListVo.setAmount(order.getAmount());
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemVo> orderItemVos = orderItems.stream().map(v ->{
			OrderItemVo orderItemVo = new OrderItemVo();
			BeanUtils.copyProperties(v, orderItemVo);
			
			orderItemVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemVo.setPrice(v.getPrice());
			orderItemVo.setAmount(v.getAmount());
			return orderItemVo;
		}).collect(Collectors.toList());
		orderListVo.setOrderItems((ArrayList<OrderItemVo>) orderItemVos);
		
		return orderListVo;
	}
	
	public OrderDetailVo buildDetailVo(Order order) {
		OrderDetailVo orderDetailVo = new OrderDetailVo();
		BeanUtils.copyProperties(order, orderDetailVo);
		
		orderDetailVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		if(order.getExpiredTime() != null){
			orderDetailVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		}
		if(order.getExpiredTime() != null){
			orderDetailVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		}
		if(order.getExpiredTime() != null){
		orderDetailVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		}
		orderDetailVo.setAmount(order.getAmount());
		orderDetailVo.setRefund(order.getAmount());
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemVo> orderItemVos = orderItems.stream().map(v ->{
			OrderItemVo orderItemVo = new OrderItemVo();
			BeanUtils.copyProperties(v, orderItemVo);
			
			orderItemVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemVo.setPrice(v.getPrice());
			orderItemVo.setAmount(v.getAmount());
			return orderItemVo;
		}).collect(Collectors.toList());
		orderDetailVo.setOrderItems((ArrayList<OrderItemVo>) orderItemVos);
		
		return orderDetailVo;
	}
}
