package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.cms.Banner;
import com.zy.model.query.BannerQueryModel;

import com.zy.entity.cms.Banner.BannerPosition;

public interface BannerMapper {

	int insert(Banner banner);

	int update(Banner banner);

	int merge(@Param("banner") Banner banner, @Param("fields")String... fields);

	int delete(Banner id);

	Banner findOne(Long id);

	List<Banner> findAll(BannerQueryModel bannerQueryModel);

	long count(BannerQueryModel bannerQueryModel);

	int deleteByBannerPosition(BannerPosition bannerPosition);

}