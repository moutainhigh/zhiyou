package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.ProductReplacement;
import com.zy.model.dto.LogisticsDto;
import com.zy.model.query.ProductReplacementQueryModel;

import java.util.List;

public interface ProductReplacementService {

	ProductReplacement create(ProductReplacement productReplacement);

	ProductReplacement findOne(Long id);

	void reject(Long id, String remark, Long userId); // 驳回

	void deliver(Long id, LogisticsDto logisticsDto, Long userId); // 发货

	void receive(Long id); // 确认收货

	List<ProductReplacement> findAll(ProductReplacementQueryModel productReplacementModel);

	Page<ProductReplacement> findPage(ProductReplacementQueryModel productReplacementModel);
}
