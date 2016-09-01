package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.Address;
import com.zy.model.query.AddressQueryModel;

public interface AddressService {

	Address create(Address address);
	
	Address findDefaultByUserId(Long userId);
	
	void delete(Long id);
	
	List<Address> findByUserId(Long userId);
	
	Address update(Address address);
	
	Address findOne(Long id);
	
	List<Address> findAll(AddressQueryModel addressQueryModel);
	
	Page<Address> findPage(AddressQueryModel addressQueryModel);
}
