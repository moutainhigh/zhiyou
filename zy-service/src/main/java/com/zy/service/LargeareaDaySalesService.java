package com.zy.service;


import com.zy.entity.report.LargeareaDaySales;
import com.zy.model.query.LargeareaDaySalesQueryModel;

import java.util.List;

public interface LargeareaDaySalesService {

     List<LargeareaDaySales> findAll(LargeareaDaySalesQueryModel largeareaDaySalesQueryModel);

     void insert(LargeareaDaySales largeareaDaySales);

}
