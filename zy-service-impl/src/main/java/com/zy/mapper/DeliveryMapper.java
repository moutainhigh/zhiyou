package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.mal.Delivery;
import com.gc.model.query.DeliveryQueryModel;


public interface DeliveryMapper {

	int insert(Delivery delivery);

	int update(Delivery delivery);

	int merge(@Param("delivery") Delivery delivery, @Param("fields")String... fields);

	int delete(Long id);

	Delivery findOne(Long id);

	List<Delivery> findAll(DeliveryQueryModel deliveryQueryModel);

	long count(DeliveryQueryModel deliveryQueryModel);

	Delivery fetchOne();

	Delivery findBySn(String sn);

}