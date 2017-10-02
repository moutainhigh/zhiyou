package com.zy.component;

import com.sun.tools.javac.util.*;
import com.zy.common.util.DateUtil;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.Profit;
import com.zy.entity.mal.Order;
import com.zy.entity.report.NewReportTeam;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserTargetSales;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.query.*;
import com.zy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-09-28.
 */
@Component
public class NewReportComponent {
    @Autowired
    private LocalCacheComponent localCacheComponent;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private UserUpgradeService userUpgradeService;

    @Autowired
    private TeamReportNewService teamReportNewService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private NewReportTeamService newReportTeamService;

    @Autowired
    private  UserTargetSalesService userTargetSalesService;

    /**
     * 处理查询特级分布数据
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeamAreatNumber(String type) {
        //过滤出所有的V4成员 且分过大区的 有大区总裁的
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).collect(Collectors.toList());
        List<User>largeUserList = users.stream().filter(user ->  user.getLargearea()!=null).collect(Collectors.toList());
        List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");//查询说有大区
        List<User>presidentUserList =users.stream().filter(user -> user.getIsPresident()!=null).collect(Collectors.toList());//查询所有大区总裁
        Map<String ,Object> map = new HashMap<>();
        map.put("number",this.disposeTeamNumber(type,largeUserList,largeAreaTypes,presidentUserList));
        //map.put("newV4",this.disposeTeamNewV4(type,largeUserList,largeAreaTypes,presidentUserList));
        //map.put("sleep",this.disposeTeamSleep(type,largeUserList,largeAreaTypes,presidentUserList));
       // map.put("areat",this.disposeTeamAreat(type,users));
        return map;
    }

       /**
     * 处理查询新晋特级分布
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeamAreatNewNumber(String type) {
        //过滤出所有的V4成员 且分过大区的 有大区总裁的
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).collect(Collectors.toList());
        List<User>largeUserList = users.stream().filter(user ->  user.getLargearea()!=null).collect(Collectors.toList());
        List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");//查询说有大区
        List<User>presidentUserList =users.stream().filter(user -> user.getIsPresident()!=null).collect(Collectors.toList());//查询所有大区总裁
        Map<String ,Object> map = new HashMap<>();
        map.put("newV4",this.disposeTeamNewV4(type,largeUserList,largeAreaTypes,presidentUserList));
        return map;
    }

    /**
     * 处理查询沉睡特级分布
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeamAreatSleepNumber(String type) {
        //过滤出所有的V4成员 且分过大区的 有大区总裁的
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).collect(Collectors.toList());
        List<User>largeUserList = users.stream().filter(user ->  user.getLargearea()!=null).collect(Collectors.toList());
        List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");//查询说有大区
        List<User>presidentUserList =users.stream().filter(user -> user.getIsPresident()!=null).collect(Collectors.toList());//查询所有大区总裁
        Map<String ,Object> map = new HashMap<>();
        map.put("sleep",this.disposeTeamSleep(type,largeUserList,largeAreaTypes,presidentUserList));
        // map.put("areat",this.disposeTeamAreat(type,users));
        return map;
    }


    //处理团队人数
    private List<Map<String,Integer>> disposeTeamNumber(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList){
        List<Map<String,Integer>> resultList = new ArrayList<>();
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if ("0".equals(type)){//公司不需要 过滤大区总裁
            for (SystemCode systemCode :largeAreaTypes){
                Map<String,Integer> naemap= new HashMap<>();
                naemap.put(systemCode.getSystemName(),userMap.get(Integer.valueOf(systemCode.getSystemValue()))!=null?userMap.get(Integer.valueOf(systemCode.getSystemValue())).size():0);
                resultList.add(naemap);
            }
          }else{//统计各个大区的分布
           List<User> areatUserList = userMap.get(Integer.valueOf(type));//获取大区的所有人
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));
            for(User user :presidentUserList){
                Map<String,Integer> naemap= new HashMap<>();
                naemap.put(user.getNickname(),areatMap.get(user.getId())!=null?userMap.get(user.getId()).size():0);
                resultList.add(naemap);
            }
         }
       return resultList;
    }

    /**
     * 处理 新晋特级
     * @param type
     * @param userList
     * @param largeAreaTypes
     * @param presidentUserList
     * @return
     */
    private List<Map<String,Integer>> disposeTeamNewV4(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList){
        List<Map<String,Integer>> resultList = new ArrayList<>();
        //统计所有的新晋特
        UserUpgradeQueryModel userUpgradeQueryModel = new UserUpgradeQueryModel();
        userUpgradeQueryModel.setToUserRankEQ(User.UserRank.V4);
        userUpgradeQueryModel.setUpgradedTimeGTE(DateUtil.getBeforeMonthBegin(new Date(), 0, 0));
        userUpgradeQueryModel.setUpgradedTimeLT(DateUtil.getBeforeMonthEnd(new Date(), 1, 0));
        Map<Long,List<UserUpgrade >>userUpgradeList = userUpgradeService.findAll(userUpgradeQueryModel).stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));//查询所有的升级用户
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if ("0".equals(type)){//处理公司
            long V4 = userUpgradeService.count(userUpgradeQueryModel);//统计 新晋特级
            for (SystemCode systemCode :largeAreaTypes){
                int count = 0;
                Map<String,Integer> naemap= new HashMap<>();
                List<User> areatList = userMap.get(Integer.valueOf(systemCode.getSystemValue()))==null?new ArrayList<>():userMap.get(Integer.valueOf(systemCode.getSystemValue()));
                for(User user : areatList ){
                    if (userUpgradeList.get(user.getId())!=null){//能取到  说明是新晋的
                        count++;
                    }
                }
                if (V4==0){
                    naemap.put(systemCode.getSystemName(),0);
                }else{
                    Double duty  = DateUtil.formatDouble( new Double(count) / new Double(V4) * 100);
                    naemap.put(systemCode.getSystemName(),count);
                }
                resultList.add(naemap);
            }
        }else{//处理  大区
            List<User> areatUserList = userMap.get(Integer.valueOf(type));//获取大区的所有人
            int sum=0;
            for(User user0:areatUserList){
                if (userUpgradeList.get(user0.getId())!=null){//能取到  说明是新晋的
                    sum++;
                }
            }
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));

            for(User user :presidentUserList){
                int count = 0;
                Map<String,Integer> naemap= new HashMap<>();
                List<User> areatuserList =areatMap.get(user.getId());
                for(User user1 :areatuserList){
                    if (userUpgradeList.get(user1.getId())!=null){//能取到  说明是新晋的
                        count++;
                    }
                }
                if (sum==0){
                    naemap.put(user.getNickname(),count);
                }else{
                    Double duty  = DateUtil.formatDouble( new Double(count) / new Double(sum) * 100);
                    naemap.put(user.getNickname(),count);
                }
                naemap.put(user.getNickname(),count);
                resultList.add(naemap);
            }

        }
       return resultList;
    }

    /**
     * 处理沉睡用户
     * @param type
     * @param userList
     * @param largeAreaTypes
     * @param presidentUserList
     * @return
     */
    private List<Map<String,Integer>> disposeTeamSleep(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList) {
        List<Map<String,Integer>> resultList = new ArrayList<>();
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if("0".equals(type)){//公司
           userList = userList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());//统计所有的沉睡用户
          for (SystemCode systemCode :largeAreaTypes){
              Map<String,Integer> naemap= new HashMap<>();
              int count =0;
              List<User> areatUserList = userMap.get(Integer.valueOf(systemCode.getSystemValue()));
              if (areatUserList!=null){
                  areatUserList =  areatUserList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());
                 count=areatUserList.size();
              }
              if (userList.size()==0){
                  naemap.put(systemCode.getSystemName(),0);
              }else{
                  Double duty   = DateUtil.formatDouble( new Double(count)/ new Double(userList.size())  * 100);
                  naemap.put(systemCode.getSystemName(),count);
              }
              resultList.add(naemap);
          }
        }else{//大区
            List<User> areatUserList = userMap.get(Integer.valueOf(type));
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));
            areatUserList =areatUserList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());//统计这个大区所有沉睡用户
            for(User user1 :presidentUserList){
             Map<String,Integer> naemap= new HashMap<>();
              int count =0;
              List<User>  presidentAreatUserList = areatMap.get(user1.getId());
                if (presidentAreatUserList!=null) {
                    presidentAreatUserList = presidentAreatUserList.stream().filter(user -> user.getLastloginTime().getTime() < DateUtil.getMonthData(new Date(), -3, 0).getTime()).collect(Collectors.toList());
                    count=presidentAreatUserList.size();
                }
                if (areatUserList.size()==0){
                    naemap.put(user1.getNickname(),0);
                }else{
                    Double duty = DateUtil.formatDouble( new Double(count)/new Double(areatUserList.size())  * 100);
                    naemap.put(user1.getNickname(),areatUserList.size());
                }
                resultList.add(naemap);
            }
        }
        return resultList;
    }

    /**
     * 处理人员分布
     * @param type
     * @return
     */
    private List<NewReportTeam> disposeTeamAreat(String type,List<User> userList){
        List<Area> areaList = teamReportNewService.findParentAll();//查询 所有省
        Map<Long, Area> areaMap= teamReportNewService.findAreaAll(new AreaQueryModel()).stream().collect(Collectors.toMap(v -> v.getId(), v -> v));//将区域转成map类型
        Map<Long,UserInfo>userInfoMap = userInfoService.findAll(new UserInfoQueryModel()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
        NewReportTeamQueryModel newReportTeamQueryModel =  new NewReportTeamQueryModel();
        newReportTeamQueryModel.setYearEQ(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        newReportTeamQueryModel.setMonthEQ(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        Map<Integer,Map<Long,List<NewReportTeam>>> newReportTeamOld = newReportTeamService.findAll(newReportTeamQueryModel).stream().collect(Collectors.groupingBy(NewReportTeam::getRegion, Collectors.groupingBy(NewReportTeam::getProvinceId)));
        List<User> newList = new ArrayList<>();

        for (User user:userList ){//将没有实名认证的或者 没有地址的 过滤掉
            Long areaId = userInfoMap.get(user.getId())==null?null:userInfoMap.get(user.getId()).getAreaId();
            Area area =null;
            if (areaId != null) {
                do {
                    area =  areaMap.get(areaId);
                    areaId=area!=null?area.getParentId():null;
                }while (areaId!=null);
                if (area!=null) {
                    user.setBossId(area.getId()); //暂时借用这个字段 修改时 注意
                    newList.add(user);
                }
            }
        }
        List<NewReportTeam> newReportTeamList = new ArrayList<>();
        if ("0".equals(type)){ //公司
            Map<Long,List<User>> gsMap = newList.stream().collect(Collectors.groupingBy(User::getBossId));
            for (Area area :areaList){
                List<User> newUserList = gsMap.get(area.getId());
                NewReportTeam newReportTeam = new NewReportTeam();
                newReportTeam.setCreateDate(new Date());
                newReportTeam.setProvinceId(area.getId());
                newReportTeam.setProvinceName(area.getName());
                newReportTeam.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                newReportTeam.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                newReportTeam.setNumber(newUserList!=null?newUserList.size():0);
                newReportTeam.setRegion(0);
                newReportTeamList.add(newReportTeam);
            }
         }else{//大区
            newList =newList.stream().filter(user -> user.getLargearea()!=null).collect(Collectors.toList());//将没有分配大区的过滤掉
            Map<Integer, Map<Long, List<User>>> counting = newList.stream().collect(
                    Collectors.groupingBy(User::getLargearea, Collectors.groupingBy(User::getBossId)));

            Map<Long, List<User>>userMap= counting.get(Integer.valueOf(type));
            if (userMap!=null){
                for (Area area :areaList){
                    List<User> newUserList = userMap.get(area.getId());
                    NewReportTeam newReportTeam = new NewReportTeam();
                    newReportTeam.setCreateDate(new Date());
                    newReportTeam.setProvinceId(area.getId());
                    newReportTeam.setProvinceName(area.getName());
                    newReportTeam.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                    newReportTeam.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                    newReportTeam.setNumber(newUserList!=null?newUserList.size():0);
                    newReportTeam.setRegion(Integer.valueOf(type));
                    newReportTeamList.add(newReportTeam);
                }
            }

         }

        //开始处理排名
        newReportTeamList = newReportTeamList.stream().sorted((teamReportNew1, teamReportNew2) ->
                teamReportNew2.getNumber() - teamReportNew1.getNumber()).collect(Collectors.toList());
        int i =1;
        int v4number=-1;
        for(NewReportTeam newReportTeam :newReportTeamList) {

            if (newReportTeam.getNumber()<v4number){
                v4number=newReportTeam.getNumber();
                i++;
            }
            newReportTeam.setRank(i);
            NewReportTeam teamReportNewOld = null;
            if (newReportTeamOld!=null&&newReportTeamOld.get(newReportTeam.getRegion())!=null&&newReportTeamOld.get(newReportTeam.getRegion()).get(newReportTeam.getProvinceId())!=null){
                teamReportNewOld = newReportTeamOld.get(newReportTeam.getRegion()).get(newReportTeam.getProvinceId()).get(0);
            }
            if(teamReportNewOld!=null){
                newReportTeam.setRankChange(i-newReportTeam.getRank());
            }else{
                newReportTeam.setRankChange(i);
            }
            if (v4number==-1){
                v4number = newReportTeam.getNumber();
            }
        }
        return newReportTeamList;
    }


    /**
     * 处理查询大区数据
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeamAreat(String type) {
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).collect(Collectors.toList());
        Map<String ,Object> map = new HashMap<>();
        map.put("areat",this.disposeTeamAreat(type,users));
        return map;
    }


    /**
     * 统计服务次数
     * @param type
     * @param yorm y:年 m：月 d：日
     * @return
     */
    public String statOrderNumber(String type,String yorm) {
        String number = "000000";
         //处理订单
        List<Order> orders = localCacheComponent.getOrders().stream()
                .filter(v -> {
                    Order.OrderStatus orderStatus = v.getOrderStatus();
                    if (orderStatus == Order.OrderStatus.已支付 || orderStatus == Order.OrderStatus.已发货 || orderStatus == Order.OrderStatus.已完成) {
                        return true;
                    }
                    return false;
                })
                .filter(v -> {
                    Date paidTime = v.getPaidTime();
                    Date endDate = new Date();
                    if("d".equals(yorm)){
                         endDate = DateUtil.getDateEnd(DateUtil.getMonthData(new Date(),0,-1));
                    }else{//暂不处理年  需要在加（按月处理）
                        endDate = DateUtil.getBeforeMonthBegin(new Date(),0,0);
                    }
                    if (paidTime.after(endDate)) {
                        return true;
                    }
                    return false;
                }).filter(v->v.getSellerId()==1).collect(Collectors.toList());

        if("0".equals(type)){ //处理公司
            Long sum = orders.parallelStream().mapToLong(Order::getQuantity).sum();
            number = DateUtil.toStringLength(sum,8);
        }else{//处理大区
          //将订单按用户进行分组
            Map<Long ,List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getUserId));
            List<User> userList = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()== User.UserRank.V4).filter(user -> user.getLargearea()!=null).collect(Collectors.toList());
            Map<Integer,List<User>> usertMap = userList.stream().collect(Collectors.groupingBy(User::getLargearea));
            List <User> areatUserList = usertMap.get(Integer.valueOf(type));
             List<Order> newOrderList = new ArrayList<>();
            for (User user : areatUserList){
                List<Order> orderOldList = orderMap.get(user.getId());
                if (orderOldList!=null&&!orderOldList.isEmpty()){
                    newOrderList.addAll(orderOldList);
                }
            }
            Long sum = newOrderList.parallelStream().mapToLong(Order::getQuantity).sum();
            number = DateUtil.toStringLength(sum,8);
        }

        return  number;

    }

    /**
     * 统计 团队 特级
     * @param type
     * @return
     */
    public Map<String,String> disposeTeamV4(String type) {
        Map<String,String>V4Map = new HashMap<>();
        List <User>userNewList = new  ArrayList<>();
        List<User> userList = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()== User.UserRank.V4).collect(Collectors.toList());
        if ("0".equals(type)){
            userNewList=userList;
        }else{
          userList = localCacheComponent.getUsers().stream().filter(user -> user.getLargearea()!=null).collect(Collectors.toList());
           Map<Integer,List<User>> areatUserMap =  userList.stream().collect(Collectors.groupingBy(User::getLargearea));
            userNewList = areatUserMap.get(Integer.valueOf(type));
        }
        if (userNewList!=null&&!userNewList.isEmpty()){//处理逻辑
            V4Map.put("V4Number",userNewList.size()+"");//将总人数放进去
            //处理新晋特级
              List<UserUpgrade> upgradeList =localCacheComponent.getUserUpgrades().stream().filter(userUpgrade ->userUpgrade.getToUserRank()== User.UserRank.V4 ).filter(userUpgrade -> {
                  Date upgradedTime = userUpgrade.getUpgradedTime();
                  if (upgradedTime.after(DateUtil.getBeforeMonthBegin(new Date(),0,0))) {
                      return true;
                  }
                  return false;
              }).collect(Collectors.toList());
            Map<Long,List<UserUpgrade>> upgradeMap = upgradeList.stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));
            List<User> newV4UserList = userNewList.stream().filter(user -> {
                if (upgradeMap.get(user.getId())!=null){
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            V4Map.put("V4NewNumber",newV4UserList.size()+"");//将总人数放进去
            //处理 沉睡特级
            userNewList=userNewList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());
            V4Map.put("V4SleepNumber",userNewList.size()+"");//将总人数放进去
        }
        return V4Map;
    }

    /**
     * 处理  U币级奖金及发放情况
     * @param type
     * @return
     */

    public Map<String,String> disposeTeamUb(String type) {
        //处理U币逻辑
        Map<String,String>UMap = new HashMap<>();
        List<Deposit> depositList=localCacheComponent.getDeposits().stream().filter(deposit ->(deposit.getPaidTime()!=null &&deposit.getPaidTime().after(DateUtil.getBeforeMonthEnd(new Date(),0,0)))).
                filter(deposit -> deposit.getDepositStatus()== Deposit.DepositStatus.充值成功).collect(Collectors.toList());
        List<User> userList = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()== User.UserRank.V4).filter(user -> user.getLargearea()!=null).collect(Collectors.toList());
        Map<Integer,List<User>> userMap = userList.stream().collect(Collectors.groupingBy(User::getLargearea));
        Map<Long ,User> userV4list= userList.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
        if (!"0".equals(type)){
            List<User> ateatUaerList = userMap.get(Integer.valueOf(type));
            if(ateatUaerList!=null) {//大区有人才有充值
                Map<Long,User> areatUserMap = ateatUaerList.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
                depositList = depositList.stream().filter(deposit -> {
                    if (areatUserMap.get(deposit.getUserId())!=null){
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
        }
        double sum = depositList.stream().mapToDouble(((Deposit o)->o.getAmount1().doubleValue())).sum();
        UMap.put("Ub",DateUtil.formatString(sum));
        //处理奖励发放
        List<Profit> profitList=localCacheComponent.getProfits().stream().filter(profit -> (profit.getCurrencyType()== CurrencyType.积分||profit.getCurrencyType()== CurrencyType.现金)).
                filter(profit -> profit.getProfitStatus()== Profit.ProfitStatus.已发放).filter(profit -> { //是V4的
            if(userV4list.get(profit.getUserId())!=null){
                return true;
            }
            return false;
        }).filter(profit -> (profit.getProfitType()!=Profit.ProfitType.补偿||profit.getProfitType()!=Profit.ProfitType.订单收款)).filter(profit->{//过滤成本年的
            String year = DateUtil.getYear(new Date())+"";
            String  title = profit.getTitle().substring(0,4);
            if (year.equals(title)){
                return true;
            }
            return false;
        }).collect(Collectors.toList()); //取到  所有奖励
        List<Profit> profitListYear = profitList;
        //处理月
        this.disposeProfit(profitList,type,"m",UMap,userMap);
        //处理年
        this.disposeProfit(profitListYear,type,"y",UMap,userMap);
        return UMap;
    }


    /**
     * 处理 奖励
     * @param profitList
     * @param type
     * @param mory 年或者月 m 月 Y是年
     */
    private  void disposeProfit(List<Profit> profitList,String type ,String mory,Map<String,String>UMap,Map<Integer,List<User>> userMap){

        if ("m".equals(mory)){ //月
            String m = DateUtil.getMothNum(new Date())-1+"";//上个月
            profitList = profitList.stream().filter(profit -> {
                String title  =profit.getTitle().substring(5,6);
                if(m.equals(title)){
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        }else{//年
            List<String> tempList = Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12");
            profitList = profitList.stream().filter(profit -> {
                String title  =profit.getTitle().substring(5,6);
                if (tempList.contains(title)){
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        }
        if(!"0".equals(type)){//公司的不需要处理
            List<User> ateatUaerList = userMap.get(type);
            if (ateatUaerList!=null){//处理大区的奖励
                Map<Long,User> areatUserMap = ateatUaerList.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
                profitList = profitList.stream().filter(deposit -> {
                    if (areatUserMap.get(deposit.getUserId())!=null){
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
        }
        double sum = profitList.stream().mapToDouble(((Profit o)->o.getAmount().doubleValue())).sum();
        UMap.put(mory+"Munber",DateUtil.formatString(sum));
    }


    /**
     * 处理目标量
     * @param type
     * @return
     */
    public Map<String,String> disposeTeamTage(String type) {
        Map<String,String>tageMap = new HashMap<>();
        //处理目标量
         List<User> userList = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()== User.UserRank.V4).filter(user -> user.getLargearea()!=null).collect(Collectors.toList());
        List<UserTargetSales> userTargetSalesList =  userTargetSalesService.findAll(
                UserTargetSalesQueryModel.builder().
                        yearEQ(DateUtil.getYear(new Date())).
                        monthEQ(DateUtil.getMothNum(new Date())).build());
        if (!"0".equals(type)){//处理大区  公司
          Map<Integer,List<User>> userMap =  userList.stream().collect(Collectors.groupingBy(User::getLargearea));
          List<User> areatUserList =  userMap.get(type);
            if (areatUserList!=null) {
                Map<Long,User> areatUserMap = areatUserList.stream().collect(Collectors.toMap(v->v.getId(),v->v));
                userTargetSalesList = userTargetSalesList.stream().filter(userTargetSales -> {
                    if (areatUserMap.get(userTargetSales.getUserId())!=null){
                        return true;
                    }
                    return  false;
                }).collect(Collectors.toList());
            }
        }
        int targetCount = userTargetSalesList.stream().mapToInt(UserTargetSales::getTargetCount).sum();
        tageMap.put("tageNumber",DateUtil.formatString(targetCount));
        //处理完成量
        String number = this.statOrderNumber(type,"m");
        tageMap.put("finishNumber",DateUtil.formatString(Double.valueOf(number)));
        if(targetCount==0){
            tageMap.put("rate","100.00");
        }else {
            String rate = DateUtil.formatString(Double.valueOf(number)/Double.valueOf(targetCount)*100);
            tageMap.put("rate",rate);
        }
        return tageMap;
    }

    public Map<String,Object> disposeLargeareaSalesHaveRate(String type) {
        Date now = new Date();
        //过滤订单
        localCacheComponent.getOrders().stream().filter(v -> {
            Order.OrderStatus orderStatus = v.getOrderStatus();
            if (orderStatus == Order.OrderStatus.已支付 || orderStatus == Order.OrderStatus.已发货 || orderStatus == Order.OrderStatus.已完成) {
                if( v.getSellerUserRank() == User.UserRank.V4 && v.getPaidTime().after(DateUtil.getMonthBegin(now,0,0)) ){
                    return true;
                }
            }
            return false;
        }).filter(v -> v.getPaidTime().after(DateUtil.getMonthBegin(now,0,0))).collect(Collectors.toList());

        return null;
    }


}
