package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mal.OrderStore;
import com.zy.model.query.OrderStoreQueryModel;


public interface OrderStoreMapper {

	int insert(OrderStore orderStore);

	int update(OrderStore orderStore);

	int merge(@Param("orderStore") OrderStore orderStore, @Param("fields")String... fields);

	int delete(Long id);

	OrderStore findOne(Long id);

	List<OrderStore> findAll(OrderStoreQueryModel orderStoreQueryModel);

	long count(OrderStoreQueryModel orderStoreQueryModel);

}