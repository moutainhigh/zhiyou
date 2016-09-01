package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.mal.Gift;
import com.gc.model.query.GiftQueryModel;


public interface GiftMapper {

	int insert(Gift gift);

	int update(Gift gift);

	int merge(@Param("gift") Gift gift, @Param("fields")String... fields);

	int delete(Long id);

	Gift findOne(Long id);

	List<Gift> findAll(GiftQueryModel giftQueryModel);

	long count(GiftQueryModel giftQueryModel);

}