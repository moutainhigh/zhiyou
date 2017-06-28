package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.tour.Sequence;
import com.zy.model.query.TransferQueryModel;

import java.math.BigDecimal;
import java.util.List;

public interface TourService {
     Sequence create(Sequence sequence);
     Sequence findSequenceOne(String seqName,String seqType);
     void updateSequence(Sequence sequence);

}
