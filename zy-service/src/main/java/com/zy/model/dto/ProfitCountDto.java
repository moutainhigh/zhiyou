package com.zy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Author: Xuwq
 * Date: 2017/6/13.
 */
@Getter
@Setter
public class ProfitCountDto implements Serializable {
    private Date date;
    private BigDecimal sumAmount;
}
