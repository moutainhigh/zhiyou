package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.Address;
import com.zy.model.query.AddressQueryModel;


public interface AddressMapper {

	int insert(Address address);

	int update(Address address);

	int merge(@Param("address") Address address, @Param("fields")String... fields);

	int delete(Long id);

	Address findOne(Long id);

	List<Address> findAll(AddressQueryModel addressQueryModel);

	long count(AddressQueryModel addressQueryModel);

}