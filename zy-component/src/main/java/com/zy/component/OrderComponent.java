package com.zy.component;

import static com.zy.util.GcUtils.formatDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.Config;
import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.model.ImageVo;
import com.zy.model.query.PaymentQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.model.query.TransferQueryModel;
import com.zy.service.OrderItemService;
import com.zy.service.PaymentService;
import com.zy.service.ProfitService;
import com.zy.service.TransferService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.OrderAdminFullVo;
import com.zy.vo.OrderAdminVo;
import com.zy.vo.OrderDetailVo;
import com.zy.vo.OrderItemVo;
import com.zy.vo.OrderListVo;

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
	@Autowired
	private Config config;
	
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日 HH:mm";

	private static final String SPLIT = ",";


	public OrderAdminFullVo buildAdminFullVo(Order order) {
		OrderAdminFullVo orderAdminFullVo = new OrderAdminFullVo();
		BeanUtils.copyProperties(order, orderAdminFullVo);

		orderAdminFullVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		orderAdminFullVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		orderAdminFullVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		orderAdminFullVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		orderAdminFullVo.setDeliveredTimeLabel(formatDate(order.getDeliveredTime(), TIME_PATTERN));
		orderAdminFullVo.setAmountLabel(GcUtils.formatCurreny(order.getAmount()));
		if(order.getSellerId().equals(config.getSysUserId())){
			orderAdminFullVo.setIsPlatformDeliver(true);
		} else {
			orderAdminFullVo.setIsPlatformDeliver(false);
		}
		
		OrderItem orderItem = orderItemService.findByOrderId(order.getId()).get(0);
		if (orderItem != null) {
			orderAdminFullVo.setImageThumbnail(GcUtils.getThumbnail(orderItem.getImage()));
			orderAdminFullVo.setPrice(orderItem.getPrice());
			orderAdminFullVo.setPriceLabel(GcUtils.formatCurreny(orderItem.getPrice()));
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
		orderAdminFullVo.setOrderStatusStyle(GcUtils.getOrderStatusStyle(order.getOrderStatus()));
		
		String offlineImage = order.getOfflineImage();
		if (StringUtils.isNotBlank(offlineImage)) {
			String[] offlineImages = StringUtils.split(offlineImage, SPLIT);
			List<ImageVo> images = Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).map(v -> {
				ImageVo imageVo = new ImageVo();
				imageVo.setImage(v);
				imageVo.setImageThumbnail(GcUtils.getThumbnail(v));
				imageVo.setImageBig(GcUtils.getThumbnail(v, 640, 640));
				return imageVo;
			}).collect(Collectors.toList());
			orderAdminFullVo.setOfflineImages(images);
		}
		
		Boolean isUseLogistics = order.getIsUseLogistics();
		if (isUseLogistics == null) {
			orderAdminFullVo.setUseLogisticsLabel(null);
		} else if (isUseLogistics) {
			orderAdminFullVo.setUseLogisticsLabel("物流发货");
		} else {
			orderAdminFullVo.setUseLogisticsLabel("面对面发货");
		}
		return orderAdminFullVo;
	}
	
	public OrderAdminVo buildAdminVo(Order order) {
		OrderAdminVo orderAdminVo = new OrderAdminVo();
		BeanUtils.copyProperties(order, orderAdminVo);
		
		orderAdminVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		orderAdminVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		orderAdminVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		orderAdminVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		orderAdminVo.setAmountLabel(GcUtils.formatCurreny(order.getAmount()));
		if(order.getSellerId().equals(config.getSysUserId())){
			orderAdminVo.setIsPlatformDeliver(true);
		} else {
			orderAdminVo.setIsPlatformDeliver(false);
		}
		
		OrderItem orderItem = orderItemService.findByOrderId(order.getId()).get(0);
		if (orderItem != null) {
			orderAdminVo.setImageThumbnail(GcUtils.getThumbnail(orderItem.getImage()));
			orderAdminVo.setPrice(orderItem.getPrice());
			orderAdminVo.setPriceLabel(GcUtils.formatCurreny(orderItem.getPrice()));
			orderAdminVo.setQuantity(orderItem.getQuantity());
		}
		
		orderAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(order.getUserId())));
		orderAdminVo.setSeller(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(order.getSellerId())));
		orderAdminVo.setOrderStatusStyle(GcUtils.getOrderStatusStyle(order.getOrderStatus()));
		
		String offlineImage = order.getOfflineImage();
		if (StringUtils.isNotBlank(offlineImage)) {
			String[] offlineImages = StringUtils.split(offlineImage, SPLIT);
			List<ImageVo> images = Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).map(v -> {
				ImageVo imageVo = new ImageVo();
				imageVo.setImage(v);
				imageVo.setImageThumbnail(GcUtils.getThumbnail(v));
				imageVo.setImageBig(GcUtils.getThumbnail(v, 640, 640));
				return imageVo;
			}).collect(Collectors.toList());
			orderAdminVo.setOfflineImages(images);
		}

		Boolean isUseLogistics = order.getIsUseLogistics();
		if (isUseLogistics == null) {
			orderAdminVo.setUseLogisticsLabel(null);
		} else if (isUseLogistics) {
			orderAdminVo.setUseLogisticsLabel("物流发货");
		} else {
			orderAdminVo.setUseLogisticsLabel("面对面发货");
		}
		return orderAdminVo;
	}
	
	
	public OrderListVo buildListVo(Order order) {
		OrderListVo orderListVo = new OrderListVo();
		BeanUtils.copyProperties(order, orderListVo);
		
		orderListVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), SIMPLE_TIME_PATTERN));
		orderListVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), SIMPLE_TIME_PATTERN));
		orderListVo.setAmount(order.getAmount());
		orderListVo.setAmountLabel(GcUtils.formatCurreny(order.getAmount()));
		if(order.getSellerId().equals(config.getSysUserId())){
			orderListVo.setIsPlatformDeliver(true);
		} else {
			orderListVo.setIsPlatformDeliver(false);
		}
		
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
		orderDetailVo.setAmountLabel(GcUtils.formatCurreny(order.getAmount()));
		
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
		if(order.getSellerId().equals(config.getSysUserId())){
			orderDetailVo.setIsPlatformDeliver(true);
		} else {
			orderDetailVo.setIsPlatformDeliver(false);
		}
		
		
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
		Long quantity = orderItems.stream().map(v -> v.getQuantity()).reduce((x, y) -> x + y).get();
		orderDetailVo.setQuantity(quantity);
		
		String offlineImage = order.getOfflineImage();
		if (StringUtils.isNotBlank(offlineImage)) {
			String[] offlineImages = StringUtils.split(offlineImage, SPLIT);
			List<ImageVo> images = Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).map(v -> {
				ImageVo imageVo = new ImageVo();
				imageVo.setImage(v);
				imageVo.setImageThumbnail(GcUtils.getThumbnail(v));
				imageVo.setImageBig(GcUtils.getThumbnail(v, 640, 640));
				return imageVo;
			}).collect(Collectors.toList());
			orderDetailVo.setOfflineImages(images);
		}
		
		return orderDetailVo;
	}
}
