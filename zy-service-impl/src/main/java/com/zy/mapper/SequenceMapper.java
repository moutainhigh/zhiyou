package com.zy.mapper;


import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.tour.Sequence;
import com.zy.model.query.AccountQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SequenceMapper {

	int update(Sequence sequence);

	Sequence findOne(Map<String,Object> map);

	void insert(Sequence sequence);
}