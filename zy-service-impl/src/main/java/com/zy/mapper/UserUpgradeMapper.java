package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.UserUpgrade;
import com.zy.model.query.UserUpgradeQueryModel;


public interface UserUpgradeMapper {

	int insert(UserUpgrade userUpgrade);

	int update(UserUpgrade userUpgrade);

	int merge(@Param("userUpgrade") UserUpgrade userUpgrade, @Param("fields")String... fields);

	int delete(Long id);

	UserUpgrade findOne(Long id);

	List<UserUpgrade> findAll(UserUpgradeQueryModel userUpgradeQueryModel);

	long count(UserUpgradeQueryModel userUpgradeQueryModel);

}