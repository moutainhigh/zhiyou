package com.zy.service.impl;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Profit.ProfitStatus;
import com.zy.entity.fnc.Profit.ProfitType;
import com.zy.mapper.ProfitMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.ProfitSumDto;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.ProfitService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.AccountLog.InOut.支出;
import static com.zy.entity.fnc.AccountLog.InOut.收入;

@Service
@Validated
public class ProfitServiceImpl implements ProfitService {

	@Autowired
	private ProfitMapper profitMapper;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private Config config;

	@Override
	public Page<Profit> findPage(@NotNull ProfitQueryModel profitQueryModel) {
		if (profitQueryModel.getPageNumber() == null)
			profitQueryModel.setPageNumber(0);
		if (profitQueryModel.getPageSize() == null)
			profitQueryModel.setPageSize(20);
		long total = profitMapper.count(profitQueryModel);
		List<Profit> data = profitMapper.findAll(profitQueryModel);
		Page<Profit> page = new Page<>();
		page.setPageNumber(profitQueryModel.getPageNumber());
		page.setPageSize(profitQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Profit> findAll(@NotNull ProfitQueryModel profitQueryModel) {
		return profitMapper.findAll(profitQueryModel);
	}


	@Override
	public Profit createAndGrant(Long userId, String title, CurrencyType currencyType, BigDecimal amount, Date createdTime) {
		return fncComponent.createAndGrantProfit(userId, ProfitType.补偿, null, title, currencyType, amount, createdTime);
	}

	@Override
	public void grant(@NotNull Long id) {
		Profit profit = profitMapper.findOne(id);
		Profit.ProfitStatus profitStatus = profit.getProfitStatus();
		if (profitStatus == Profit.ProfitStatus.已发放) {
			return; // 幂等操作
		}

		String title = profit.getTitle();
		CurrencyType currencyType = profit.getCurrencyType();
		BigDecimal amount = profit.getAmount();
		Long userId = profit.getUserId();
		Long sysUserId = config.getSysUserId();
		if (!sysUserId.equals(userId)) {
			fncComponent.recordAccountLog(sysUserId, title, currencyType, amount, 支出, profit, userId);
			fncComponent.recordAccountLog(userId, title, currencyType, amount, 收入, profit, sysUserId);
		}
		profit.setGrantedTime(new Date());
		profit.setProfitStatus(Profit.ProfitStatus.已发放);
		if (profitMapper.update(profit) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public Profit findOne(@NotNull Long id) {
		return profitMapper.findOne(id);
	}


	@Override
	public List<BigDecimal> queryRevenue(@NotNull ProfitQueryModel profitQueryModel) {
		return profitMapper.queryRevenue(profitQueryModel);
	}

	@Override
	public void cancel(@NotNull Long id) {
		Profit profit = profitMapper.findOne(id);
		validate(profit, NOT_NULL, "profit id " + id + " not found");

		if (profit.getProfitStatus() == ProfitStatus.已取消) {
			return;
		} else if (profit.getProfitStatus() != ProfitStatus.待发放) {
			throw new BizException(BizCode.ERROR, "只有待发放收益才能取消");
		}

		profit.setProfitStatus(ProfitStatus.已取消);
		if (profitMapper.update(profit) == 0) {
			throw new ConcurrentException();
		}
	}


	/**
	 * 封装受益  数据
	 * @param userId
	 * @param map
	 */
	@Override
	public Map<String,Object> countIncomeDataByUser(Long userId, Map<String, Object> map) {
		//统计上月合，计收入以及环比同比情况
		//构建查询 参数
		ProfitQueryModel profitQueryModel = new ProfitQueryModel();
		profitQueryModel.setUserIdEQ(userId);
		profitQueryModel.setProfitStatusEQ(ProfitStatus.已发放);
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-1,0));
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,0));
		profitQueryModel.setProfitTypeIN( new ProfitType[]{ProfitType.订单收款,ProfitType.数据奖,ProfitType.销量奖,ProfitType.特级平级奖,ProfitType.平级推荐奖, ProfitType.特级推荐奖, ProfitType.返利奖,ProfitType.董事贡献奖});
		Double beforeData =profitMapper.sum(profitQueryModel);//上一个月的收益数据
		map.put("BM",beforeData==null?0D:beforeData);
        //构建查询累计 受益参数
		profitQueryModel.setCreatedTimeLT(new Date());
		profitQueryModel.setCreatedTimeGTE(null);
		Double totalData =profitMapper.sum(profitQueryModel);//截止到当前时间  总的收益
		map.put("TOT",totalData==null?0D:totalData);
		//处理环比
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-2,0));
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),-1,0));
		Double nextBeforeData =profitMapper.sum(profitQueryModel);//上上一个月的收益数据 QoQ
		if (nextBeforeData==null||nextBeforeData==0){//环比数据
			map.put("QoQ",100);
		}else if (beforeData==null||beforeData==0){
			map.put("QoQ",0);
		}else{
			double QoQ = (beforeData-nextBeforeData)/nextBeforeData*100;
			map.put("QoQ",com.zy.common.util.DateUtil.formatDouble(QoQ));
		}
		//处理同比
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-1,-1));
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,-1));
		Double nextYearBeforeData =profitMapper.sum(profitQueryModel);//上一年的当前时间的前一个月 YoY
		if(nextYearBeforeData==null||nextYearBeforeData==0){
			map.put("YoY",100);
		}else if(beforeData==null||beforeData==0){
			map.put("YoY",0);
		}else{
			double YoY = (beforeData-nextYearBeforeData)/nextYearBeforeData*100;
			map.put("YoY",com.zy.common.util.DateUtil.formatDouble(YoY));
		}
       //循环处理折线图数据
		int moth = com.zy.common.util.DateUtil.getMoth(new Date());
		double polyLineData [] = new double[moth-1];
		for (int i=1;i<moth;i++){
			profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-i,0));
			profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),-(i-1),0));
			Double data =profitMapper.sum(profitQueryModel);
			polyLineData[i-1]=data==null?0:data;
		}
       map.put("PL",polyLineData); //折线图数据
		//处理饼图 及 详细显示  特级平级奖,订单收益,返利奖,数据奖,董事贡献奖,特级推荐奖,平级推荐奖,销量奖
		double pieDate[] = new double[]{0,0,0,0,0,0,0,0};  //sumGroupBy
		profitQueryModel.setCreatedTimeLT(new Date());
		profitQueryModel.setCreatedTimeGTE(null);
		profitQueryModel.setProfitTypeIN(null);
		profitQueryModel.setGroupby(1);
		List<ProfitSumDto> porfitList = profitMapper.sumGroupBy(profitQueryModel);
         for (ProfitSumDto profitSumDto :porfitList){
			 if(profitSumDto.getProfitType()==ProfitType.特级平级奖){
				 pieDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.订单收款){
				 pieDate[1]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.返利奖){
				 pieDate[2]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.数据奖){
				 pieDate[3]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.董事贡献奖){
				 pieDate[4]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.特级推荐奖){
				 pieDate[5]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.平级推荐奖){
				 pieDate[6]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.销量奖){
				 pieDate[7]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				 continue;
			 }
		 }
		map.put("pie",pieDate);
		//在统计  各个 上月详细
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-1,0));
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,0));
		List<ProfitSumDto> porfitMothList = profitMapper.sumGroupBy(profitQueryModel);
		double mothDate[] = new double[]{0,0};
		//先初始化值
		map.put("ftl",mothDate);
		map.put("ord",mothDate);
		map.put("red",mothDate);
		map.put("dat",mothDate);
		map.put("sen",mothDate);
		map.put("rec",mothDate);
		map.put("ltl",mothDate);
		map.put("sal",mothDate);
		for (ProfitSumDto profitSumDto :porfitMothList){
			if(profitSumDto.getProfitType()==ProfitType.特级平级奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[0];
				map.put("ftl",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.订单收款){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[1];
				map.put("ord",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.返利奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[2];
				map.put("red",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.数据奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[3];
				map.put("dat",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.董事贡献奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[4];
				map.put("sen",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.特级推荐奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[5];
				map.put("rec",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.平级推荐奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[6];
				map.put("ltl",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.销量奖){
				mothDate[0]=profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount();
				mothDate[1]=pieDate[7];
				map.put("sal",mothDate);
				continue;
			}
		}
		return map;
	}

}
