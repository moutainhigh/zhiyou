package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Delivery;
import com.zy.model.query.DeliveryQueryModel;

public interface DeliveryService {

	Delivery findOne(Long id);
	Delivery create(Delivery delivery);
	Delivery update(Delivery delivery);
	void importData(List<Delivery> deliveryList);
	List<Delivery> findExportData(DeliveryQueryModel deliveryQueryModel);

	Page<Delivery> findPage(DeliveryQueryModel deliveryQueryModel);
	List<Delivery> findAll(DeliveryQueryModel deliveryQueryModel);
	
}
