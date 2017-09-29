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
import com.zy.entity.report.LargeAreaProfit;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.mapper.LargeAreaProfitMapper;
import com.zy.mapper.ProfitMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.DepositSumDto;
import com.zy.model.dto.ProfitSumDto;
import com.zy.model.query.LargeAreaProfitQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.LargeAreaProfitService;
import com.zy.service.ProfitService;
import com.zy.service.SystemCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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

	@Autowired
	private SystemCodeService systemCodeService;

	@Autowired
	private LargeAreaProfitService largeAreaProfitService;

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
	public Map<String, Object> queryRevenue(@NotNull ProfitQueryModel profitQueryModel) {
		Map<String ,Object> returnMap = new HashMap<>();
		int moth = DateUtil.getMoth(new Date());
		double polyLineData [] = new double[moth-1];
		for (int i = moth - 1; i >= 1;i--){
			profitQueryModel.setCreatedTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0-i,0));
			profitQueryModel.setCreatedTimeLT(DateUtil.getBeforeMonthEnd(new Date(),0-(i-1),0));
			double data = 0d;
			DepositSumDto dto = profitMapper.queryRevenue(profitQueryModel);
			if (dto != null && dto.getSumAmount() != null){
				data= dto.getSumAmount().doubleValue();
			}
			polyLineData[i-1]=data;
		}
		returnMap.put("revenue", DateUtil.arryToString(polyLineData,true));
		returnMap.put("revenues", DateUtil.arryToString(polyLineData,false));
		returnMap.put("len", moth - 1);
		return returnMap;
	}

	@Override
	public List<Profit> orderRevenueDetail(@NotNull ProfitQueryModel profitQueryModel) {
		profitQueryModel.setCreatedTimeGTE(DateUtil.getMonthBegin(new Date(),profitQueryModel.getMonth(),0));
		profitQueryModel.setCreatedTimeLT(DateUtil.getBeforeMonthEnd(new Date(),profitQueryModel.getMonth() + 7,-1));
		List<Profit> list = profitMapper.orderRevenueDetail(profitQueryModel);
		return list;
	}

	//所有特级收益
	@Override
	public void insert(List<User> v4Users) {
		LargeAreaProfit largeAreaProfit = null;
		//获取当前月份
		int moth = DateUtil.getMoth(new Date());
		ProfitQueryModel profitQueryModel = new ProfitQueryModel();
		profitQueryModel.setCreatedTimeGTE(DateUtil.getMonthBegin(new Date(),moth -1,0));
		profitQueryModel.setCreatedTimeLT(DateUtil.getBeforeMonthEnd(new Date(),moth +3,-1));
		profitQueryModel.setProfitStatusEQ(Profit.ProfitStatus.已发放);
		for (User user: v4Users) {
			largeAreaProfit = new LargeAreaProfit();
			largeAreaProfit.setLargeAreaValue(user.getLargearea());
			SystemCode largeArea = systemCodeService.findByTypeAndValue("LargeAreaType", user.getLargearea().toString());
			largeAreaProfit.setLargeAreaName(largeArea.getSystemName());
			largeAreaProfit.setYear(DateUtil.getYear(new Date()));
			largeAreaProfit.setMonth(moth-1);
			largeAreaProfit.setUserId(user.getId());
			largeAreaProfit.setCreateTime(new Date());
			//查询每一位上月收益
			profitQueryModel.setUserIdEQ(user.getId());
			Double profirs =  profitMapper.findRevenue(profitQueryModel);
			largeAreaProfit.setProfit(profirs);
			//环比
			LargeAreaProfitQueryModel largeAreaProfitQueryModel = new LargeAreaProfitQueryModel();
			largeAreaProfitQueryModel.setUserIdEQ(user.getId());
			largeAreaProfitQueryModel.setYearEQ(DateUtil.getYear(new Date()));
			largeAreaProfitQueryModel.setMonthEQ(moth-2);
			LargeAreaProfit la = largeAreaProfitService.findLargeAreaProfit(largeAreaProfitQueryModel);
			if (la != null){
				if (la.getProfit() == 0.00 && profirs > 0){
					largeAreaProfit.setRelativeRate(100.00);
				}else if (la.getProfit() > 0 && profirs > 0 ){
					largeAreaProfit.setRelativeRate(DateUtil.formatDouble( (profirs - la.getProfit()) / la.getProfit() * 100));
				}else if (la.getProfit() == 0.00 && profirs == 0){
					largeAreaProfit.setRelativeRate(0.00);
				}
			}else {
				if (profirs > 0){
					largeAreaProfit.setRelativeRate(100.00);
				}else {
					largeAreaProfit.setRelativeRate(0.00);
				}
			}
			//同比
			LargeAreaProfitQueryModel largeQueryModel = new LargeAreaProfitQueryModel();
			largeQueryModel.setUserIdEQ(user.getId());
			largeQueryModel.setYearEQ(DateUtil.getYear(new Date()) - 1);
			largeQueryModel.setMonthEQ(moth-1);
			LargeAreaProfit largeA = largeAreaProfitService.findLargeAreaProfit(largeQueryModel);
			if (largeA != null){
				if (largeA.getProfit() == 0.00 && profirs > 0){
					largeAreaProfit.setSameRate(100.00);
				}else if (largeA.getProfit() > 0 && profirs > 0 ){
					largeAreaProfit.setSameRate(DateUtil.formatDouble( (profirs - largeA.getProfit()) / largeA.getProfit() * 100));
				}else if (largeA.getProfit() == 0.00 && profirs == 0){
					largeAreaProfit.setSameRate(0.00);
				}
			}else {
				if (profirs > 0){
					largeAreaProfit.setSameRate(100.00);
				}else {
					largeAreaProfit.setSameRate(0.00);
				}
			}
			largeAreaProfitService.insert(largeAreaProfit);
		}
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
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-1,0));
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,0));
		profitQueryModel.setProfitTypeIN( new ProfitType[]{ProfitType.订单收款,ProfitType.历史收益,ProfitType.销量奖,ProfitType.特级平级奖,ProfitType.平级推荐奖, ProfitType.推荐奖, ProfitType.返利奖,ProfitType.董事贡献奖});
		DepositSumDto depositSumDto =profitMapper.sum(profitQueryModel);//上一个月的收益数据
		double beforeData=0d;
		if (depositSumDto!=null&&depositSumDto.getSumAmount()!=null){
			beforeData=depositSumDto.getSumAmount().doubleValue();
		}
		map.put("BM",com.zy.common.util.DateUtil.formatDouble(beforeData));
        //构建查询累计 受益参数
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,0));
		profitQueryModel.setCreatedTimeGTE(null);
		double totalData =0d;
		DepositSumDto depositSumDtoTot =profitMapper.sum(profitQueryModel);//截止到当前时间  总的收益
		if (depositSumDtoTot!=null&&depositSumDtoTot.getSumAmount()!=null){
			totalData=depositSumDtoTot.getSumAmount().doubleValue();
		}
		map.put("TOT",com.zy.common.util.DateUtil.formatDouble(totalData));
		//处理环比
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-2,0));
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),-1,0));
		double nextBeforeData=0d;
		DepositSumDto depositSumDtonextTot =profitMapper.sum(profitQueryModel);//上上一个月的收益数据 QoQ
		if(depositSumDtonextTot!=null&&depositSumDtonextTot.getSumAmount()!=null){
			nextBeforeData= depositSumDtonextTot.getSumAmount().doubleValue();
		}
		if (nextBeforeData==0){//环比数据
			if(beforeData==0){
				map.put("QoQ",0);
			}else {
				map.put("QoQ", 100);
			}
		}else{
			double QoQ = (beforeData-nextBeforeData)/nextBeforeData*100;
			map.put("QoQ",com.zy.common.util.DateUtil.formatDouble(QoQ));
		}
		//处理同比
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-1,-1));
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,-1));
		double nextYearBeforeData =0d;
		DepositSumDto depositSumDtoforeData =profitMapper.sum(profitQueryModel);//上一年的当前时间的前一个月 YoY
		if(depositSumDtoforeData!=null&&depositSumDtoforeData.getSumAmount()!=null){
			nextYearBeforeData=depositSumDtoforeData.getSumAmount().doubleValue();
		}
		if(nextYearBeforeData==0){
			 if(beforeData==0){
				map.put("YoY",0);
			}else{
			  map.put("YoY",100);
			}
		}else{
			double YoY = (beforeData-nextYearBeforeData)/nextYearBeforeData*100;
			map.put("YoY",com.zy.common.util.DateUtil.formatDouble(YoY));
		}
       //循环处理折线图数据
		int moth = com.zy.common.util.DateUtil.getMoth(new Date());
		double polyLineData [] = new double[moth-1];
		for (int i=moth-1;i>=1;i--){
			profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),0-i,0));
			profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0-(i-1),0));
			double data=0d;
			DepositSumDto depositSumDtodata =profitMapper.sum(profitQueryModel);
			if (depositSumDtodata!=null&&depositSumDtodata.getSumAmount()!=null){
				data=depositSumDtodata.getSumAmount().doubleValue();
			}
			polyLineData[i-1]=data;
		}
       map.put("PL",com.zy.common.util.DateUtil.arryToString(polyLineData,true)); //折线图数据
		//处理饼图 及 详细显示  特级平级奖,订单收益,返利奖,历史收益,董事贡献奖,特级推荐奖,平级推荐奖,销量奖
		double pieDate[] = new double[]{0,0,0,0,0,0,0,0};  //sumGroupBy
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,0));
		profitQueryModel.setCreatedTimeGTE(null);
		profitQueryModel.setProfitTypeIN(null);
		profitQueryModel.setGroupby(1);
		List<ProfitSumDto> porfitList = profitMapper.sumGroupBy(profitQueryModel);
         for (ProfitSumDto profitSumDto :porfitList){
			 if(profitSumDto.getProfitType()==ProfitType.特级平级奖){
				 pieDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.订单收款){
				 pieDate[1]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.返利奖){
				 pieDate[2]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.历史收益){
				 pieDate[3]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.董事贡献奖){
				 pieDate[4]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.推荐奖){
				 pieDate[5]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.平级推荐奖){
				 pieDate[6]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
			 if(profitSumDto.getProfitType()==ProfitType.销量奖){
				 pieDate[7]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				 continue;
			 }
		 }
		map.put("pie",com.zy.common.util.DateUtil.arryToString(pieDate,false));
		//在统计  各个 上月详细
		profitQueryModel.setCreatedTimeGTE(com.zy.common.util.DateUtil.getBeforeMonthBegin(new Date(),-1,0));
		profitQueryModel.setCreatedTimeLT(com.zy.common.util.DateUtil.getBeforeMonthEnd(new Date(),0,0));
		List<ProfitSumDto> porfitMothList = profitMapper.sumGroupBy(profitQueryModel);
		//先初始化值
		map.put("ftl",new double[]{0.00,pieDate[0]});
		map.put("ord",new double[]{0.00,pieDate[1]});
		map.put("red",new double[]{0.00,pieDate[2]});
		map.put("dat",new double[]{0.00,pieDate[3]});
		map.put("sen",new double[]{0.00,pieDate[4]});
		map.put("rec",new double[]{0.00,pieDate[5]});
		map.put("ltl",new double[]{0.00,pieDate[6]});
		map.put("sal",new double[]{0.00,pieDate[7]});
		for (ProfitSumDto profitSumDto :porfitMothList){
			double mothDate[] = new double[2];
			if(profitSumDto.getProfitType()==ProfitType.特级平级奖){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[0];
				map.put("ftl",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.订单收款){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[1];
				map.put("ord",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.返利奖){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[2];
				map.put("red",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.历史收益){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[3];
				map.put("dat",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.董事贡献奖){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[4];
				map.put("sen",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.推荐奖){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[5];
				map.put("rec",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.平级推荐奖){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[6];
				map.put("ltl",mothDate);
				continue;
			}
			if(profitSumDto.getProfitType()==ProfitType.销量奖){
				mothDate[0]=com.zy.common.util.DateUtil.formatDouble(profitSumDto.getSumAmount()==null?0:profitSumDto.getSumAmount().doubleValue());
				mothDate[1]=pieDate[7];
				map.put("sal",mothDate);
				continue;
			}
		}
		return map;
	}

}
