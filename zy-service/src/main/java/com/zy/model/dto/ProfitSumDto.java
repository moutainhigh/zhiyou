package com.zy.model.dto;

import com.zy.entity.fnc.Profit;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by it001 on 2017/6/10.
 */
@Getter
@Setter
public class ProfitSumDto implements Serializable {
    private Profit.ProfitType profitType;
    private BigDecimal sumAmount;
}
