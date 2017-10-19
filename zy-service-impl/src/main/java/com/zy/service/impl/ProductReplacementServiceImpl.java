package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.component.SysComponent;
import com.zy.entity.mal.ProductReplacement;
import com.zy.entity.sys.Area;
import com.zy.mapper.AreaMapper;
import com.zy.mapper.ProductReplacementMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.AreaDto;
import com.zy.model.dto.LogisticsDto;
import com.zy.model.query.ProductReplacementQueryModel;
import com.zy.service.ProductReplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ProductReplacementServiceImpl implements ProductReplacementService{

	@Autowired
	private ProductReplacementMapper productReplacementMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Autowired
	private SysComponent sysComponent;

	@Override
	public ProductReplacement create(@NotNull ProductReplacement productReplacement) {
		productReplacement.setFromProduct("优检一生1.0");
		productReplacement.setToProduct("优检一生2.0");
		productReplacement.setCreatedTime(new Date());
		productReplacement.setProductReplacementStatus(ProductReplacement.ProductReplacementStatus.已申请);

		Long areaId = productReplacement.getReceiverAreaId();
		Area area = areaMapper.findOne(areaId);
		validate(area, NOT_NULL, "area id " + areaId + " not found");
		AreaDto areaDto = sysComponent.findOneDto(areaId);

		productReplacement.setReceiverProvince(areaDto.getProvince());
		productReplacement.setReceiverCity(areaDto.getCity());
		productReplacement.setReceiverDistrict(areaDto.getDistrict());
		validate(productReplacement);
		productReplacementMapper.insert(productReplacement);
		return productReplacement;
	}

	@Override
	public ProductReplacement findOne(@NotNull Long id) {
		return productReplacementMapper.findOne(id);
	}

	@Override
	public void reject(@NotNull Long id, String remark, Long userId) {

		ProductReplacement andValidate = findAndValidate(id);

		ProductReplacement.ProductReplacementStatus productReplacementStatus = andValidate.getProductReplacementStatus();
		ProductReplacement.ProductReplacementStatus 已驳回 = ProductReplacement.ProductReplacementStatus.已驳回;
		if(productReplacementStatus == 已驳回) {
			return;
		}
		if(productReplacementStatus != ProductReplacement.ProductReplacementStatus.已申请) {
			throw new BizException(BizCode.ERROR);
		}

		ProductReplacement mergeEntity = new ProductReplacement();
		mergeEntity.setId(id);
		mergeEntity.setProductReplacementStatus(已驳回);
		mergeEntity.setRemark(remark);
		mergeEntity.setUpdateId(userId);
		mergeEntity.setUpdateTime(new Date());
		productReplacementMapper.merge(mergeEntity,"updateId","updateTime", "productReplacementStatus", "remark");
	}

	@Override
	public void deliver(@NotNull Long id, @NotNull LogisticsDto logisticsDto, Long userId) {
		ProductReplacement andValidate = findAndValidate(id);

		ProductReplacement.ProductReplacementStatus productReplacementStatus = andValidate.getProductReplacementStatus();
		if(productReplacementStatus == ProductReplacement.ProductReplacementStatus.已发货) {
			return;
		}
		if(productReplacementStatus != ProductReplacement.ProductReplacementStatus.已申请) {
			throw new BizException(BizCode.ERROR);
		}

		ProductReplacement mergeEntity = new ProductReplacement();
		mergeEntity.setId(id);
		mergeEntity.setDeliveredId(userId);
		mergeEntity.setLogisticsName(logisticsDto.getName());
		mergeEntity.setLogisticsSn(logisticsDto.getSn());
		mergeEntity.setLogisticsFee(logisticsDto.getFee());
		mergeEntity.setUpdateId(userId);
		mergeEntity.setUpdateTime(new Date());
		mergeEntity.setProductReplacementStatus(ProductReplacement.ProductReplacementStatus.已发货);
		productReplacementMapper.merge(mergeEntity, "logisticsName","updateId","updateTime", "logisticsSn", "logisticsFee", "logisticsSn", "productReplacementStatus");
	}

	@Override
	public void receive(@NotNull Long id) {
		ProductReplacement andValidate = findAndValidate(id);

		ProductReplacement.ProductReplacementStatus productReplacementStatus = andValidate.getProductReplacementStatus();
		if(productReplacementStatus == ProductReplacement.ProductReplacementStatus.已完成) {
			return;
		}
		if(productReplacementStatus != ProductReplacement.ProductReplacementStatus.已发货) {
			throw new BizException(BizCode.ERROR);
		}

		ProductReplacement mergeEntity = new ProductReplacement();
		mergeEntity.setId(id);
		mergeEntity.setProductReplacementStatus(ProductReplacement.ProductReplacementStatus.已完成);
		productReplacementMapper.merge(mergeEntity,"updateId","updateTime", "productReplacementStatus");
	}

	@Override
	public List<ProductReplacement> findAll(@NotNull ProductReplacementQueryModel productReplacementModel) {
		return productReplacementMapper.findAll(productReplacementModel);
	}

	@Override
	public Page<ProductReplacement> findPage(@NotNull ProductReplacementQueryModel productReplacementModel) {
		if (productReplacementModel.getPageNumber() == null)
			productReplacementModel.setPageNumber(0);
		if (productReplacementModel.getPageSize() == null)
			productReplacementModel.setPageSize(20);
		if (productReplacementModel.getOrderBy() == null) {
			productReplacementModel.setOrderBy("createdTime");
		}

		long total = productReplacementMapper.count(productReplacementModel);
		List<ProductReplacement> data = productReplacementMapper.findAll(productReplacementModel);
		Page<ProductReplacement> page = new Page<>();
		page.setPageNumber(productReplacementModel.getPageNumber());
		page.setPageSize(productReplacementModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	private ProductReplacement findAndValidate(Long id) {
		validate(id, NOT_NULL, "id not found");
		ProductReplacement one = productReplacementMapper.findOne(id);
		validate(one, NOT_NULL, "product replacement id " + id + " not found");
		return one;
	}
}
