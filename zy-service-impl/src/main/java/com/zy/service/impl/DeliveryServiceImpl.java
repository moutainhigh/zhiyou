package com.gc.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.gc.entity.mal.Delivery;
import com.gc.entity.mal.Delivery.DeliveryStatus;
import com.gc.mapper.DeliveryMapper;
import com.gc.model.BizCode;
import com.gc.model.query.DeliveryQueryModel;
import com.gc.service.DeliveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private DeliveryMapper deliveryMapper;
	
	@Override
	public Delivery findOne(@NotNull Long id) {
		return deliveryMapper.findOne(id);
	}
	
	@Override
	public Delivery create(@NotNull Delivery delivery) {
		validate(delivery);
		deliveryMapper.insert(delivery);
		return delivery;
	}
	
	@Override
	public Delivery update(@NotNull Delivery delivery) {
		deliveryMapper.update(delivery);
		return delivery;
	}
	
	@Override
	public Page<Delivery> findPage(@NotNull DeliveryQueryModel deliveryQueryModel) {
		if(deliveryQueryModel.getPageNumber() == null)
			deliveryQueryModel.setPageNumber(0);
		if(deliveryQueryModel.getPageSize() == null)
			deliveryQueryModel.setPageSize(20);
		long total = deliveryMapper.count(deliveryQueryModel);
		List<Delivery> data = deliveryMapper.findAll(deliveryQueryModel);
		Page<Delivery> page = new Page<>();
		page.setPageNumber(deliveryQueryModel.getPageNumber());
		page.setPageSize(deliveryQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Delivery> findAll(DeliveryQueryModel deliveryQueryModel) {
		return deliveryMapper.findAll(deliveryQueryModel);
	}

	@Override
	public void importData(List<Delivery> deliveryList) {
		deliveryList.stream().forEach(v -> {
			Delivery delivery = new Delivery();
			String sn = v.getSn();
			delivery.setName(v.getName());
			delivery.setSn(sn);
			delivery.setDeliveryStatus(DeliveryStatus.待发货);
			validate(delivery, "name", "sn", "deliveryStatus", "version");
			Delivery deliveryCheck = deliveryMapper.findBySn(sn);
			if(deliveryCheck != null){
				throw new BizException(BizCode.ERROR, "sn：" + sn + "已经存在，请重新校对数据后再行上传");
			}
			deliveryMapper.insert(delivery);
		});
	}

	@Override
	public List<Delivery> findExportData(DeliveryQueryModel deliveryQueryModel) {
		List<Delivery> deliverys = deliveryMapper.findAll(deliveryQueryModel);
		if(deliveryQueryModel.getDeliveryStatusEQ() != null && deliveryQueryModel.getDeliveryStatusEQ().equals(DeliveryStatus.待发货)){
			deliverys.stream().forEach(v ->{
				Delivery delivery = new Delivery();
				delivery.setId(v.getId());
				delivery.setName(v.getName());
				delivery.setSn(v.getSn());
				delivery.setDeliveryStatus(DeliveryStatus.已发货);
				validate(delivery, "id", "deliveryStatus", "version");
				if (deliveryMapper.update(delivery) == 0) {
					throw new ConcurrentException();
				}
			});
		}
		return deliverys;
	}

}
