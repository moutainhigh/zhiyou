package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.component.SysComponent;
import com.gc.entity.sys.Area;
import com.gc.entity.sys.Area.AreaType;
import com.gc.entity.usr.Address;
import com.gc.entity.usr.User;
import com.gc.mapper.AddressMapper;
import com.gc.mapper.AreaMapper;
import com.gc.mapper.UserMapper;
import com.gc.model.dto.AreaDto;
import com.gc.model.query.AddressQueryModel;
import com.gc.service.AddressService;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SysComponent sysComponent;

	@Override
	public Address findDefaultByUserId(@NotNull  Long userId) {
		AddressQueryModel addressQueryModel = new AddressQueryModel();
		addressQueryModel.setUserIdEQ(userId);
		addressQueryModel.setIsDeletedEQ(false);
		List<Address> addresses = addressMapper.findAll(addressQueryModel);
		if (addresses.isEmpty()) {
			return null;
		}
		return addresses.stream().filter(v -> v.getIsDefault()).findFirst()
				.orElse(addresses.get(0));
	}

	@Override
	public List<Address> findByUserId(@NotNull  Long userId) {
		AddressQueryModel addressQueryModel = new AddressQueryModel();
		addressQueryModel.setUserIdEQ(userId);
		addressQueryModel.setIsDeletedEQ(false);
		addressQueryModel.setOrderBy("isDefault");
		addressQueryModel.setDirection(Direction.DESC);
		return addressMapper.findAll(addressQueryModel);
	}

	@Override
	public Address findOne(@NotNull Long id) {
		return addressMapper.findOne(id);
	}

	@Override
	public List<Address> findAll(@NotNull AddressQueryModel addressQueryModel) {
		return addressMapper.findAll(addressQueryModel);
	}

	@Override
	public void delete(@NotNull Long id) {
		Address address = addressMapper.findOne(id);
		validate(address, NOT_NULL, "address id " + id + " is not found");
		Address addressForMerge = new Address();
		addressForMerge.setId(id);
		addressForMerge.setIsDeleted(true);
		addressForMerge.setIsDefault(false);
		addressMapper.merge(addressForMerge, "isDeleted", "isDefault");
	}
	
	@Override
	public Address create(@NotNull Address address) {

		checkArea(address.getAreaId());

		Long userId = address.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id" + userId + "not found");

		AddressQueryModel addressQueryModel = new AddressQueryModel();
		addressQueryModel.setUserIdEQ(userId);
		addressQueryModel.setIsDeletedEQ(false);
		long count = addressMapper.count(addressQueryModel);
		validate(count, v -> v <= 20, "只允许存在20个收货地址");
		
		if (address.getIsDefault() == null) {
			address.setIsDefault(false);
		}
		
		if (address.getIsDefault()) {
			addressQueryModel = new AddressQueryModel();
			addressQueryModel.setUserIdEQ(userId);
			List<Address> persistentAddresses = addressMapper.findAll(addressQueryModel);
			for(Address persistentAddress : persistentAddresses) {
				if (persistentAddress.getIsDefault()) {
					persistentAddress.setIsDefault(false);
					addressMapper.update(persistentAddress);
				}
			}
		}

		AreaDto areaDto = sysComponent.findOneDto(address.getAreaId());
		address.setProvince(areaDto.getProvince());
		address.setCity(areaDto.getCity());
		address.setDistrict(areaDto.getDistrict());
		address.setIsDeleted(false);
		validate(address);
		addressMapper.insert(address);

		return address;
	}

	@Override
	public Address update(@NotNull Address address) {
		Long id = address.getId();
		validate(id, NOT_NULL, "address id is null");

		Address persistence = addressMapper.findOne(id);
		validate(persistence, NOT_NULL, "address id " + id + " is not found");

		checkArea(address.getAreaId());

		if (address.getIsDefault() == null) {
			address.setIsDefault(false);
		}
		
		if (address.getIsDefault()) {
			AddressQueryModel addressQueryModel = new AddressQueryModel();
			addressQueryModel.setUserIdEQ(persistence.getUserId());
			List<Address> persistentAddresses = addressMapper.findAll(addressQueryModel);
			for(Address persistentAddress : persistentAddresses) {
				if (persistentAddress.getIsDefault() && !persistentAddress.getId().equals(id)) {
					persistentAddress.setIsDefault(false);
					addressMapper.update(persistentAddress);
				}
			}
		}
		AreaDto areaDto = sysComponent.findOneDto(address.getAreaId());
		address.setProvince(areaDto.getProvince());
		address.setCity(areaDto.getCity());
		address.setDistrict(areaDto.getDistrict());
		addressMapper.merge(address, "areaId", "province", "city", "district",
				"realname", "phone", "address", "isDefault");

		return address;
	}
	
	private void checkArea(Long areaId) {
		validate(areaId, NOT_NULL, "area id is null");
		Area area = areaMapper.findOne(areaId);
		validate(area, NOT_NULL, "地区不存在");
		validate(area, v -> v.getAreaType() == AreaType.区, "必须选择到区");
	}

	@Override
	public Page<Address> findPage(@NotNull AddressQueryModel addressQueryModel) {
		if(addressQueryModel.getPageNumber() == null)
			addressQueryModel.setPageNumber(0);
		if(addressQueryModel.getPageSize() == null)
			addressQueryModel.setPageSize(20);
		long total = addressMapper.count(addressQueryModel);
		List<Address> data = addressMapper.findAll(addressQueryModel);
		Page<Address> page = new Page<>();
		page.setPageNumber(addressQueryModel.getPageNumber());
		page.setPageSize(addressQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

}
