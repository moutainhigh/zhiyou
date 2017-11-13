package com.zy.service.impl;


import com.zy.common.exception.BizException;
import com.zy.common.exception.UnauthorizedException;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderStore;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.mergeusr.MergeUserUpgrade;
import com.zy.entity.mergeusr.MergeV3ToV4;
import com.zy.entity.usr.User;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.MergeUserUpgradeQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.OrderStoreQueryModel;
import com.zy.service.UserCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-11-05.
 *  用户考核逻辑类
 */
@Service
@Validated
public class UserCheckServiceImpl implements UserCheckService {


    @Autowired
    private MergeUserMapper mergeUserMapper;

    @Autowired
    private MergeUserUpgradeMapper mergeUserUpgradeMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MergeV3ToV4Mapper mergeV3ToV4Mapper;

    @Autowired
    private OrderStoreMapper orderStoreMapper;
    /**
     * 处理用户关系 回调
     * @param id  购买人id
     * @param quantity 购买数量
     * @param prodectType 商品 类型
     */
    @Override
    public void checkUserLevel(Long id, Long quantity, Integer prodectType) {
        if(prodectType==2) { //先处理  等于2的
            List<MergeUser> mergeUserList = mergeUserMapper.findAll(MergeUserQueryModel.builder().userIdEQ(id).productTypeEQ(prodectType).build());
            if (mergeUserList == null || mergeUserList.size() != 1) {//仅有一条时 才处理 等级关系
                throw new BizException(BizCode.ERROR, "客户信息错误");
            }
            //开始逻辑
            MergeUser mergeUser = mergeUserList.get(0);
            if (mergeUser.getUserRank() == User.UserRank.V4) {//自身是 联合创始人 不用处理
              return ;
            }
            if (mergeUser.getUserRank() == User.UserRank.V3) {//品牌合伙人
                this.disposeV3(mergeUser,quantity,prodectType);
            }
            if (mergeUser.getUserRank() == User.UserRank.V2) {//品牌经理
                this.disposeV2(mergeUser,quantity,prodectType);
            }
            if (mergeUser.getUserRank() == User.UserRank.V1) {//VIP
                this.disposeV1(mergeUser,quantity,prodectType);
            }
            if (mergeUser.getUserRank() == User.UserRank.V0) {//普通用户
               this.disposeV0(mergeUser,quantity,prodectType);
            }
        }
    }


    /**
     * 处理 普通用户
     * @param mergeUser
     * @param quantity
     */
    private void disposeV0(MergeUser mergeUser,Long quantity,Integer prodectType) {
        //不允许零售 所以V0用的量 在4及其以上
        this.disposeQuantityLevel(mergeUser,quantity,prodectType, User.UserRank.V0);
    }

    /**
     * 处理 V1
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
    private void disposeV1(MergeUser mergeUser,Long quantity,Integer prodectType){
        MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
        mergeUserUpgrade.setFromUserRank(mergeUser.getUserRank());
        mergeUserUpgrade.setUserId(mergeUser.getUserId());
        mergeUserUpgrade.setUpgradedTime(new Date());
      if(quantity<20){//小于最小下单量 看累计量
         Boolean flage = this.v1ToOther(mergeUser,prodectType); //调v1的升级机制
       /*   if (flage){//自身有晋级才调 上级晋级机制*/
            this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
          //}
      }else{ //销量大于等于 20 是调销量晋升机制
          this.disposeQuantityLevel(mergeUser,quantity,prodectType, User.UserRank.V1);
      }

    }


    /**
     * 处理 V2
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
    private void disposeV2(MergeUser mergeUser,Long quantity,Integer prodectType){
        //量不足 一次晋升  看累计销量
        if (quantity < 150){
            Boolean flage = this.v2ToV3(mergeUser,prodectType); //调v2的升级机制
     /*       if (flage) {//自身有晋级才调 上级晋级机制*/
                this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
           // }
        }else {
            this.disposeQuantityLevel(mergeUser,quantity,prodectType, User.UserRank.V2);
        }
    }

    /**
     * 处理 V3
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
    private void disposeV3(MergeUser mergeUser,Long quantity,Integer prodectType){
        if ( quantity < 2000){
            Boolean flage = this.v3ToV4(mergeUser,prodectType); //调v2的升级机制
         /*   if (flage) {//自身有晋级才调 上级晋级机制*/
                this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
          //  }
         }else {
            this.disposeQuantityLevel(mergeUser,quantity,prodectType, User.UserRank.V3);
        }
    }

    /**
     * 查询上级
     * @param mergeUser
     * @param prodectType
     * @param num
     * @return
     */
    private MergeUser findParent(MergeUser mergeUser,Integer prodectType,int num){
        Map<String,Object> map = new HashMap<>();
        MergeUser parent =mergeUser;

        map.put("productType" , prodectType);
        int i=0;
        do {
             map.put("userId" , parent.getParentId());
             parent = mergeUserMapper.findByUserIdAndProductType(map);
            if(parent.getUserRank()== User.UserRank.V3||parent.getUserRank()== User.UserRank.V4){
                return parent;
            }
            if (parent.getUserRank().getLevel()>mergeUser.getUserRank().getLevel()){
                return parent;
            }
             i++;
        }while (i<num&&parent!=null);
      return parent;
    }

    /**
     * 统计 推荐人 大于等于当前等级的 新晋人数
     * @param mergeUser
     * @param prodectType
     * @param num
     * String type 1：代表推荐人 2：parentId 是这个和人的
     * @return
     */
 private  Integer count(MergeUser mergeUser,Integer prodectType,int num){
     Predicate<MergeUser> predicate = v -> {
         User.UserRank userRank = v.getUserRank();
         if (num==0){
             if (userRank != null && userRank == User.UserRank.V4|| userRank == User.UserRank.V3|| userRank == User.UserRank.V2|| userRank == User.UserRank.V1|| userRank == User.UserRank.V0) {
                 return true;
             }
         }
         if (num==1){
             if (userRank != null && userRank == User.UserRank.V4|| userRank == User.UserRank.V3|| userRank == User.UserRank.V2|| userRank == User.UserRank.V1) {
                 return true;
             }
         }
         if (num==2){
             if (userRank != null && userRank == User.UserRank.V4|| userRank == User.UserRank.V3|| userRank == User.UserRank.V2) {
                 return true;
             }
         }
         if (num==3){
             if (userRank != null && userRank == User.UserRank.V4|| userRank == User.UserRank.V3) {
                 return true;
             }
         }
         if (num==4) {
             if (userRank != null && userRank == User.UserRank.V4) {
                 return true;
             }
         }
         return false;
     };
     List<MergeUser> mergeUserList = null;
      mergeUserList = mergeUserMapper.findAll(MergeUserQueryModel.builder().parentIdEQ(mergeUser.getUserId())
                 .productTypeEQ(prodectType).build()).stream().filter(predicate).collect(Collectors.toList());
    Map<Long,List<MergeUserUpgrade>> map = mergeUserUpgradeMapper.findAll(MergeUserUpgradeQueryModel.builder().upgradedTimeGTE(DateUtil.getMonthData(new Date(),-2,0)).build())
             .stream().collect(Collectors.groupingBy(MergeUserUpgrade::getUserId));
     mergeUserList = mergeUserList.stream().filter(v->{
         List<MergeUserUpgrade> mergeUserUpgradeList= map.get(v.getUserId());
         if(mergeUserUpgradeList!=null&&!mergeUserUpgradeList.isEmpty()){
              Map<User.UserRank,List<MergeUserUpgrade>> map1 = mergeUserUpgradeList.stream().collect(Collectors.groupingBy(MergeUserUpgrade::getToUserRank));
             List<MergeUserUpgrade> list = map1.get(v.getUserRank());
             if (list!=null&&!list.isEmpty()){
                 return true;
             }
             return false;
         }
         return false;
     }).collect(Collectors.toList());
    /* return mergeUserList.size();*/
     return 0;
 }


    /**
     * 统计 推荐人 大于等于当前等级的 新晋人数 从Order表统计
     * @param mergeUser
     * @param prodectType
     * @param num
     * @return
     */
    private   List<Order> findOrderList(MergeUser mergeUser,Integer prodectType,int num){
        Predicate<Order> predicate = v -> {
            if (num==1){
                if (v .getQuantity()>=4 ) {
                    return true;
                }
            }
            if (num==2){
                if (v .getQuantity()>=20) {
                    return true;
                }
            }
            if (num==3){
                if (v .getQuantity()>=150) {
                    return true;
                }
            }
            if (num==4) {
                if (v .getQuantity()>=2000) {
                    return true;
                }
            }
            return false;
        };
        List<Order> orderList = orderMapper.findAll(OrderQueryModel.builder().inviterIdEQ(mergeUser.getInviterId()).paidTimeGTE(DateUtil.getMonthData(new Date(),-2,0))
                .orderStatusIN(new Order.OrderStatus[] {Order.OrderStatus.已支付, Order.OrderStatus.已发货, Order.OrderStatus.已完成})
                .productTypeEQ(prodectType).exaltFlageEQ(0).build()).stream().filter(predicate).collect(Collectors.toList());

        Map<Long,List<MergeUserUpgrade>> map = mergeUserUpgradeMapper.findAll(MergeUserUpgradeQueryModel.builder().upgradedTimeGTE(DateUtil.getMonthData(new Date(),-2,0)).build())
                .stream().collect(Collectors.groupingBy(MergeUserUpgrade::getUserId));
        return orderList;
    }


    /**
     * V1 晋升V2或升V3 逻辑
     * @param parentInvter
     * @param prodectType
     */
    private Boolean v1ToOther(MergeUser parentInvter,Integer prodectType) {
        MergeUserUpgrade parentInvterUpgrade = new MergeUserUpgrade();
        parentInvterUpgrade.setUserId(parentInvter.getUserId());
        parentInvterUpgrade.setFromUserRank(parentInvter.getUserRank());
        parentInvterUpgrade.setUpgradedTime(new Date());
          //先推荐关系 销量
        List<Order> InvterorderList = this.findOrderList(parentInvter, prodectType, 1);
        if (InvterorderList.size() >= 5) { //可以晋级
                     MergeUser parent1 = this.findParent(parentInvter, prodectType, 2);
                     parentInvter.setParentId(parent1.getUserId());
                        parentInvter.setUserRank(User.UserRank.V2);
                        mergeUserMapper.update(parentInvter);//更新推荐人
                        parentInvterUpgrade.setToUserRank(User.UserRank.V2);
                        mergeUserUpgradeMapper.insert(parentInvterUpgrade);  //填入晋级信息
                        for (Order order:InvterorderList){
                            order.setExaltFlage(1);
                            orderMapper.update(order);
                        }
            return true;
        } else{
                        //看自身累计销量
                        List<Order> orderList = orderMapper.findAll(OrderQueryModel.builder().productTypeEQ(prodectType).userIdEQ(parentInvter.getUserId())
                                .createdTimeGTE(DateUtil.getMonthData(new Date(), -2, 0)).build());
                            if (orderList != null && !orderList.isEmpty()) {
                                orderList = orderList.stream().filter(v -> v.getBuyerUserRank() == parentInvter.getUserRank()).collect(Collectors.toList());
                                Long sum = orderList.parallelStream().mapToLong(Order::getQuantity).sum();
                                if (sum >= 20 && sum < 150) {
                                    parentInvterUpgrade.setToUserRank(User.UserRank.V2);
                                    parentInvter.setUserRank(User.UserRank.V2);
                                    MergeUser parent1 = this.findParent(parentInvter, prodectType, 2);
                                    if (parent1 != null) {
                                        parentInvter.setParentId(parent1.getUserId());
                                        mergeUserMapper.update(parentInvter);//更新推荐人
                                        //填入晋级信息
                                        mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                                        return true;
                                    }
                                } else if (sum >= 150) {
                                    parentInvterUpgrade.setToUserRank(User.UserRank.V3);
                                    parentInvter.setUserRank(User.UserRank.V3);
                                    MergeUser parent1 = this.findParent(parentInvter, prodectType, 2);
                                    if (parent1 != null) {
                                        parentInvter.setParentId(parent1.getUserId());
                                        mergeUserMapper.update(parentInvter);//更新推荐人
                                        //填入晋级信息
                                        mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                                        return true;
                                    }
                                }
                            }
                }
          return false;
            }



    /**
     * v2升v3逻辑
     */
    private  Boolean v2ToV3(MergeUser parentInvter,Integer prodectType){
        MergeUserUpgrade parentInvterUpgrade = new MergeUserUpgrade();
        parentInvterUpgrade.setUserId(parentInvter.getUserId());
        parentInvterUpgrade.setFromUserRank(parentInvter.getUserRank());
        parentInvterUpgrade.setUpgradedTime(new Date());
            //先推荐关系
            List<Order> InvterorderList = this.findOrderList(parentInvter, prodectType, 2);
            if (InvterorderList.size() >= 5) { //可以晋级
                MergeUser parent1 = this.findParent(parentInvter, prodectType, 2);
                parentInvter.setParentId(parent1.getUserId());
                parentInvter.setUserRank(User.UserRank.V3);
                parentInvterUpgrade.setToUserRank(User.UserRank.V3);
                mergeUserMapper.update(parentInvter);//更新推荐人
                mergeUserUpgradeMapper.insert(parentInvterUpgrade);  //填入晋级信息
                for (Order order : InvterorderList) { //更新掉  订单
                    order.setExaltFlage(1);
                    orderMapper.update(order);
                }
                return true;
            } else {
                List<Order> orderList = orderMapper.findAll(OrderQueryModel.builder().productTypeEQ(prodectType).userIdEQ(parentInvter.getUserId())
                        .createdTimeGTE(DateUtil.getMonthData(new Date(), -2, 0)).build());
                if (orderList != null && !orderList.isEmpty()) {
                    orderList = orderList.stream().filter(v -> v.getBuyerUserRank() == parentInvter.getUserRank()).collect(Collectors.toList());
                    Long sum = orderList.parallelStream().mapToLong(Order::getQuantity).sum();
                    if (sum >= 150) {
                        parentInvterUpgrade.setToUserRank(User.UserRank.V3);
                        parentInvter.setUserRank(User.UserRank.V3);
                        mergeUserMapper.update(parentInvter);//更新推荐人
                        //填入晋级信息
                        mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                        return true;
                    }
                }
            }
        return false;
        }

    /**
     * V3晋升V4机制  需要 后台确认
     * @param parentInvter
     * @param prodectType
     */
    private Boolean v3ToV4(MergeUser parentInvter, Integer prodectType){
        int number = this.count(parentInvter,prodectType,3);
        if(number>=5) { //让推荐人晋级
           Map<Long,List<MergeUser>> mergeUserMap = mergeUserMapper.findAll(MergeUserQueryModel.builder().productTypeEQ(prodectType).build()).stream()
                    .collect(Collectors.groupingBy(MergeUser::getParentId));
            Map<Long,List<Order>> orderMap = orderMapper.findAll(OrderQueryModel.builder().productTypeEQ(prodectType)
                    .createdTimeGTE(DateUtil.getMonthData(new Date(),-2,0)).build()).stream().collect(Collectors.groupingBy(Order::getUserId));
            long  num =0;
            List<Order>myOrderLlist = orderMap.get(parentInvter.getUserId());
            if (myOrderLlist!=null&&!myOrderLlist.isEmpty()){ //只计算已经是V3的量
                num = myOrderLlist.stream().filter(v->v.getBuyerUserRank()== parentInvter.getUserRank()).mapToLong(Order::getQuantity).sum();
            }
            for (MergeUser mergeUser:mergeUserMap.get(parentInvter.getUserId())){
                if (mergeUser.getUserRank()== User.UserRank.V4){
                    num +=150;
                }else{
                 List<Order> orderList = orderMap.get(mergeUser.getId());
                    if(orderList!=null&&!orderList.isEmpty()){
                        Long sum = orderList.parallelStream().filter(v->v.getBuyerUserRank()== mergeUser.getUserRank()).mapToLong(Order::getQuantity).sum();
                        num+=sum;
                        if(mergeUserMap.get(mergeUser.getUserId())!=null){
                            for (MergeUser mergeUser1:mergeUserMap.get(mergeUser.getUserId())){
                                if (mergeUser1.getUserRank()== User.UserRank.V4){
                                    num +=150;
                                }else{
                                    List<Order> orderList1 = orderMap.get(mergeUser.getId());
                                    if(orderList1!=null&&!orderList1.isEmpty()){
                                        Long sum1 = orderList1.parallelStream().filter(v->v.getBuyerUserRank()== mergeUser1.getUserRank()).mapToLong(Order::getQuantity).sum();
                                        num+=sum1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(num>=1800){//记录一下升级
                MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
                mergeUserUpgrade.setFromUserRank(parentInvter.getUserRank());
                mergeUserUpgrade.setUserId(parentInvter.getUserId());
                mergeUserUpgrade.setUpgradedTime(new Date());
                parentInvter.setUserRank(User.UserRank.V4);
                mergeUserMapper.update(parentInvter);  //让购买者升级
                mergeUserUpgrade.setToUserRank(User.UserRank.V4);
                mergeUserUpgradeMapper.insert(mergeUserUpgrade);//填入晋级信息
                //插入 晋升标记
                MergeV3ToV4 mergeV3ToV4 = new MergeV3ToV4();
                mergeV3ToV4.setUserId(parentInvter.getUserId());
                User user  = userMapper.findOne(parentInvter.getUserId());
                mergeV3ToV4.setName(user==null?null:user.getNickname());
                mergeV3ToV4.setFlage(0);
                mergeV3ToV4.setDelFlage(0);
                mergeV3ToV4.setCreateBy(-1L);
                mergeV3ToV4.setCreateDate(new Date());
                mergeV3ToV4.setRemark("两个月有五个大于品牌合伙人且销量累计大于等于1800");
                mergeV3ToV4Mapper.insert(mergeV3ToV4);
                return true;
            }

        }
        return false;
    }


    /**
     * 处理 推荐人ID
     *
     */
    private  void disposeParent(MergeUser parentInvter,Integer prodectType){
        Long parentId = parentInvter.getParentId();
        Boolean flage = true;
        if (parentId==null){
            return;
        }
        Map<String ,Object>map = new HashMap<String, Object>();
        map.put("productType",prodectType);
       do{
           map.put("userId" , parentId);
           MergeUser mergeUser = mergeUserMapper.findByUserIdAndProductType(map);
               if (mergeUser != null) {//其他不处理
                   if (mergeUser.getUserRank() == User.UserRank.V1) {
                       this.v1ToOther(mergeUser, prodectType);
                   } else if (mergeUser.getUserRank() == User.UserRank.V2) {
                       this.v2ToV3(mergeUser, prodectType);
                   } else if (mergeUser.getUserRank() == User.UserRank.V3) {
                       this.v3ToV4(mergeUser, prodectType);
                       return;
                   }
               }
           parentId = mergeUser.getParentId();
           flage= parentId!=null&&mergeUser.getUserRank()!=User.UserRank.V4; //上级已经是V4的不做晋级处理
       }while (flage);
    }


    /**
     * 处理销量 升V1
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
   private void  orderToV1(MergeUser mergeUser,Long quantity,Integer prodectType,int number){
       if (quantity >= 4 && quantity < 19) {
           MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
           mergeUserUpgrade.setFromUserRank(mergeUser.getUserRank());
           mergeUserUpgrade.setUserId(mergeUser.getUserId());
           mergeUserUpgrade.setUpgradedTime(new Date());
           //先更新自己
           mergeUser.setUserRank(User.UserRank.V1);
           MergeUser parent = this.findParent(mergeUser, prodectType, number);
           mergeUser.setParentId(parent.getUserId());
           mergeUserMapper.update(mergeUser);
           mergeUserUpgrade.setToUserRank(User.UserRank.V1);
           mergeUserUpgradeMapper.insert(mergeUserUpgrade);  //填入晋级信息
           this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
       }
   }
    /**
     * 处理销量 升V2
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
    private void  orderToV2(MergeUser mergeUser,Long quantity,Integer prodectType,int number){
        if (quantity >= 20 && quantity < 149) {
            MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
            mergeUserUpgrade.setFromUserRank(mergeUser.getUserRank());
            mergeUserUpgrade.setUserId(mergeUser.getUserId());
            mergeUserUpgrade.setUpgradedTime(new Date());
            mergeUser.setUserRank(User.UserRank.V2);
            MergeUser parent = this.findParent(mergeUser, prodectType, number);
            mergeUser.setParentId(parent.getUserId());
            mergeUserMapper.update(mergeUser);  //让购买者升级
            mergeUserUpgrade.setToUserRank(User.UserRank.V2);
            mergeUserUpgradeMapper.insert(mergeUserUpgrade);//填入晋级信息
            this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
        }
    }

    /**
     * 处理销量 升V3
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
    private void  orderToV3(MergeUser mergeUser,Long quantity,Integer prodectType,int number){
        if(quantity >= 150 && quantity < 2000) {
            MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
            mergeUserUpgrade.setFromUserRank(mergeUser.getUserRank());
            mergeUserUpgrade.setUserId(mergeUser.getUserId());
            mergeUserUpgrade.setUpgradedTime(new Date());
            mergeUser.setUserRank(User.UserRank.V3);
            MergeUser parent = this.findParent(mergeUser, prodectType, number);
            mergeUser.setParentId(parent.getUserId());
            mergeUserMapper.update(mergeUser);  //让购买者升级
            mergeUserUpgrade.setToUserRank(User.UserRank.V3);
            mergeUserUpgradeMapper.insert(mergeUserUpgrade); //填入晋级信息
            this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
        }
    }

    /**
     * 处理销量 升V4
     * @param mergeUser
     * @param quantity
     * @param prodectType
     */
    private void  orderToV4(MergeUser mergeUser,Long quantity,Integer prodectType,int number){
        if(quantity >= 2000){
            MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
            mergeUserUpgrade.setFromUserRank(mergeUser.getUserRank());
            mergeUserUpgrade.setUserId(mergeUser.getUserId());
            mergeUserUpgrade.setUpgradedTime(new Date());
            mergeUser.setUserRank(User.UserRank.V4);
            MergeUser parent = this.findParent(mergeUser, prodectType, number);
            mergeUser.setParentId(parent.getUserId());
            mergeUserMapper.update(mergeUser);  //让购买者升级
            mergeUserUpgrade.setToUserRank(User.UserRank.V4);
            mergeUserUpgradeMapper.insert(mergeUserUpgrade);//填入晋级信息
            this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
            //记录升级
            MergeV3ToV4 mergeV3ToV4 = new MergeV3ToV4();
            mergeV3ToV4.setUserId(mergeUser.getUserId());
            User user = userMapper.findOne(mergeUser.getUserId());
            mergeV3ToV4.setName(user == null ? null : user.getNickname());
            mergeV3ToV4.setFlage(0);
            mergeV3ToV4.setDelFlage(0);
            mergeV3ToV4.setCreateBy(-1L);
            mergeV3ToV4.setCreateDate(new Date());
            mergeV3ToV4.setRemark("一次购买量大于等于2000");
            mergeV3ToV4Mapper.insert(mergeV3ToV4);
            this.disposeParent(mergeUser, prodectType); //处理paeren 晋级逻辑
        }

    }


    /**
     * 处理  购买数量晋级
     */
    private void disposeQuantityLevel(MergeUser mergeUser,Long quantity,Integer prodectType,User.UserRank userRank){
        if (userRank==User.UserRank.V0){
            //普通用户 需要 将所有的该买晋升机制检测一下
             this.orderToV1(mergeUser,quantity,prodectType,2);
             this.orderToV2(mergeUser,quantity,prodectType,3);
             this.orderToV3(mergeUser,quantity,prodectType,3);
             this.orderToV4(mergeUser,quantity,prodectType,3);
        }else if(userRank==User.UserRank.V1){
            this.orderToV2(mergeUser,quantity,prodectType,2);
            this.orderToV3(mergeUser,quantity,prodectType,2);
            this.orderToV4(mergeUser,quantity,prodectType,2);
        }else if (userRank==User.UserRank.V2){
            this.orderToV3(mergeUser,quantity,prodectType,0);
            this.orderToV4(mergeUser,quantity,prodectType,0);
        }else if (userRank==User.UserRank.V3){
            this.orderToV4(mergeUser,quantity,prodectType,0);
        }
    }


    /**
     * 处理入库
     * @param orderId
     * @param userId
     */
    @Override
    public void editOderStoreIn(Long orderId, Long userId,Integer productType) {
        Order order = orderMapper.findOne(orderId);
        Integer sendQuantity = order.getSendQuantity() == null ? 0 : order.getSendQuantity();
        if(order.getQuantity().intValue()>sendQuantity) {//订货量和发货量一致  不用进库存
            OrderStoreQueryModel orderStoreQueryModel = new OrderStoreQueryModel();
            orderStoreQueryModel.setIsEndEQ(1);
            orderStoreQueryModel.setUserIdEQ(userId);
            List<OrderStore> orderList = orderStoreMapper.findAll(orderStoreQueryModel);
            if (orderList != null && !orderList.isEmpty()) {//处理  业务逻辑
                OrderStore orderStoreOld = orderList.get(0);
                orderStoreOld.setIsEnd(0);
                orderStoreMapper.update(orderStoreOld);
                OrderStore orderStore = new OrderStore();
                orderStore.setIsEnd(1);
                orderStore.setOrderId(orderId);
                orderStore.setUserId(orderStoreOld.getUserId());
                orderStore.setCreateDate(new Date());
                orderStore.setNumber(order.getQuantity().intValue() - sendQuantity);
                orderStore.setType(2);
                orderStore.setBeforeNumber(orderStoreOld.getAfterNumber());
                orderStore.setAfterNumber(orderStoreOld.getAfterNumber() + (order.getQuantity().intValue() - sendQuantity));
                orderStore.setCreateBy(orderStoreOld.getUserId());
                orderStore.setProductType(productType);
                orderStoreMapper.insert(orderStore);
            } else {//没有数据  则插入一条
                OrderStore orderStore = new OrderStore();
                orderStore.setIsEnd(1);
                orderStore.setOrderId(orderId);
                orderStore.setUserId(userId);
                orderStore.setCreateDate(new Date());
                orderStore.setNumber(order.getQuantity().intValue() - sendQuantity);
                orderStore.setType(2);
                orderStore.setBeforeNumber(0);
                orderStore.setAfterNumber((order.getQuantity().intValue() - sendQuantity));
                orderStore.setCreateBy(userId);
                orderStore.setProductType(productType);
                orderStoreMapper.insert(orderStore);
            }
        }
    }

    /**
     * 处理出库
     * @param orderId
     * @param userId
     */
    @Override
    public void editOrderStoreOut(Long orderId, Long userId, Integer productType) {
        Order order = orderMapper.findOne(orderId);
        OrderStoreQueryModel orderStoreQueryModel = new OrderStoreQueryModel();
        orderStoreQueryModel.setIsEndEQ(1);
        orderStoreQueryModel.setUserIdEQ(userId);
        orderStoreQueryModel.setProductTypeEQ(productType);
        List<OrderStore> orderList = orderStoreMapper.findAll(orderStoreQueryModel);
        if (orderList!=null&&!orderList.isEmpty()){//处理  业务逻辑
            OrderStore orderStoreOld = orderList.get(0);
            orderStoreOld.setIsEnd(0);
            orderStoreMapper.update(orderStoreOld);
            OrderStore orderStore = new  OrderStore();
            orderStore.setIsEnd(1);
            orderStore.setOrderId(orderId);
            orderStore.setUserId(orderStoreOld.getUserId());
            orderStore.setCreateDate(new Date());
            orderStore.setNumber(order.getQuantity().intValue());
            orderStore.setType(1);
            orderStore.setProductType(productType);
            orderStore.setBeforeNumber(orderStoreOld.getAfterNumber());
            orderStore.setAfterNumber(orderStoreOld.getAfterNumber()-order.getQuantity().intValue());
            orderStore.setCreateBy(orderStoreOld.getUserId());
            orderStoreMapper.insert(orderStore);
        }else{
            throw new UnauthorizedException("库存异常");
        }
    }
}




