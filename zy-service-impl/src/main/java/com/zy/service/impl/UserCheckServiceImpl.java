package com.zy.service.impl;


import com.zy.common.exception.BizException;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.mergeusr.MergeUserUpgrade;
import com.zy.entity.usr.User;
import com.zy.mapper.MergeUserMapper;
import com.zy.mapper.MergeUserUpgradeMapper;
import com.zy.mapper.OrderMapper;
import com.zy.model.BizCode;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.MergeUserUpgradeQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.UserCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            if (mergeUser.getUserRank() == User.UserRank.V4) {//联合创始人

            }
            if (mergeUser.getUserRank() == User.UserRank.V3) {//品牌合伙人

            }
            if (mergeUser.getUserRank() == User.UserRank.V2) {//品牌经理

            }
            if (mergeUser.getUserRank() == User.UserRank.V1) {//VIP

            }
            if (mergeUser.getUserRank() == User.UserRank.V0) {//普通用户

            }
        }
    }


    /**
     * 处理 普通用户
     * @param mergeUser
     * @param quantity
     */
    private void disposeV0(MergeUser mergeUser,Long quantity,Integer prodectType){
        MergeUserUpgrade mergeUserUpgrade = new MergeUserUpgrade();
        mergeUserUpgrade.setFromUserRank(mergeUser.getUserRank());
        mergeUserUpgrade.setUserId(mergeUser.getUserId());
        mergeUserUpgrade.setUpgradedTime(new Date());
        Map<String,Object> map = new HashMap<>();
        map.put("productType" , prodectType);
        map.put("userId" , mergeUser.getInviterId());
        MergeUser parentInvter =  mergeUserMapper.findByUserIdAndProductType(map);
        //VIP
           if(quantity>=4&&quantity<19){
               mergeUser.setUserRank(User.UserRank.V1);
               MergeUser parent = this.findParent(mergeUser,prodectType,2);
                   if(parent!=null){
                       if(parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V1){
                           this.v1ToV2(parentInvter,prodectType);
                       }
                       mergeUser.setParentId(parent.getUserId());
                       mergeUserMapper.update(mergeUser);
                       mergeUserUpgrade.setToUserRank(User.UserRank.V1);
                       //填入晋级信息
                       mergeUserUpgradeMapper.insert(mergeUserUpgrade);
                   }else{
                       throw new BizException(BizCode.ERROR, "数据异常");
                   }
           }
        //品牌经理
            if (quantity>=20&&quantity<149){
                mergeUser.setUserRank(User.UserRank.V2);
                MergeUser parent = this.findParent(mergeUser,prodectType,3);
                if(parent!=null){
                    mergeUser.setParentId(parent.getUserId());
                }else{
                    throw new BizException(BizCode.ERROR, "数据异常");
                }
                //处理 推荐人 晋升
                if(parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V1){
                    this.v1ToV2(mergeUser,prodectType);
                }else if(parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V2){
                    this.v2ToV3(mergeUser,prodectType);
                }
                mergeUserMapper.update(mergeUser);  //让购买者升级
                mergeUserUpgrade.setToUserRank(User.UserRank.V2);
                //填入晋级信息
                mergeUserUpgradeMapper.insert(mergeUserUpgrade);
            }
        //品牌合伙人
        if (quantity>=150&&quantity<2000){
            mergeUser.setUserRank(User.UserRank.V3);
            MergeUser parent = this.findParent(mergeUser,prodectType,3);
            mergeUser.setParentId(parent.getUserId());
            //处理 推荐人 晋升
            if(parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V1){
                this.v1ToV2(mergeUser,prodectType);
            }else if (parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V2){
                this.v2ToV3(mergeUser,prodectType);
            }else if (parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V3){
                this.v3ToV4(mergeUser,prodectType);
            }
            mergeUserMapper.update(mergeUser);  //让购买者升级
            mergeUserUpgrade.setToUserRank(User.UserRank.V3);
            //填入晋级信息
            mergeUserUpgradeMapper.insert(mergeUserUpgrade);
        }
        //处理
        if (quantity>=2000){
            //处理 推荐人 晋升
            if(parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V1){
                this.v1ToV2(mergeUser,prodectType);
            }else if (parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V2){
                this.v2ToV3(mergeUser,prodectType);
            }else if (parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V3){
                this.v3ToV4(mergeUser,prodectType);
            }else if (parentInvter!=null&&parentInvter.getUserRank()== User.UserRank.V4){

            }

            mergeUser.setUserRank(User.UserRank.V3);
            mergeUserMapper.update(mergeUser);  //让购买者升级
            mergeUserUpgrade.setToUserRank(User.UserRank.V3);
            //填入晋级信息
            mergeUserUpgradeMapper.insert(mergeUserUpgrade);
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
             i++;
        }while (num<i);
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
 private  Integer count(MergeUser mergeUser,Integer prodectType,int num,Integer type){
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
     if(1==type) {
         mergeUserList = mergeUserMapper.findAll(MergeUserQueryModel.builder().inviterIdEQ(mergeUser.getUserId())
                 .productTypeEQ(prodectType).build()).stream().filter(predicate).collect(Collectors.toList());
     }else{
         mergeUserList = mergeUserMapper.findAll(MergeUserQueryModel.builder().parentIdEQ(mergeUser.getUserId())
                 .productTypeEQ(prodectType).build()).stream().filter(predicate).collect(Collectors.toList());
     }
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
     return mergeUserList.size();
 }


    /**
     * V1 晋升V2或升V3 逻辑
     * @param parentInvter
     * @param prodectType
     */
    private void v1ToV2(MergeUser parentInvter,Integer prodectType){
            MergeUserUpgrade parentInvterUpgrade = new MergeUserUpgrade();
            parentInvterUpgrade.setUserId(parentInvter.getUserId());
            parentInvterUpgrade.setFromUserRank(parentInvter.getUserRank());
            parentInvterUpgrade.setUpgradedTime(new Date());
             int number = this.count(parentInvter,prodectType,1,1);
             if(number+1>=5){ //让推荐人晋级
                    parentInvterUpgrade.setToUserRank(User.UserRank.V2);
                    parentInvter.setUserRank(User.UserRank.V2);
                    MergeUser parent1 = this.findParent(parentInvter,prodectType,2);
                    if(parent1!=null){
                        parentInvter.setParentId(parent1.getUserId());
                        mergeUserMapper.update(parentInvter);//更新推荐人
                        //填入晋级信息
                        mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                    }
                }else{//看销量
                  List<Order> orderList = orderMapper.findAll(OrderQueryModel.builder().productTypeEQ(prodectType).userIdEQ(parentInvter.getUserId())
                          .createdTimeGTE(DateUtil.getMonthData(new Date(),-2,0)).build());
                   if(orderList!=null&&!orderList.isEmpty()){
                       orderList = orderList.stream().filter(v->v.getBuyerUserRank()==parentInvter.getUserRank()).collect(Collectors.toList());
                       Long sum = orderList.parallelStream().mapToLong(Order::getQuantity).sum();
                       if (sum>=20&&sum<150){
                           parentInvterUpgrade.setToUserRank(User.UserRank.V2);
                           parentInvter.setUserRank(User.UserRank.V2);
                           MergeUser parent1 = this.findParent(parentInvter,prodectType,2);
                           if(parent1!=null){
                               parentInvter.setParentId(parent1.getUserId());
                               mergeUserMapper.update(parentInvter);//更新推荐人
                               //填入晋级信息
                               mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                           }
                       }else if(sum>=150){
                           parentInvterUpgrade.setToUserRank(User.UserRank.V3);
                           parentInvter.setUserRank(User.UserRank.V3);
                           MergeUser parent1 = this.findParent(parentInvter,prodectType,2);
                           if(parent1!=null){
                               parentInvter.setParentId(parent1.getUserId());
                               mergeUserMapper.update(parentInvter);//更新推荐人
                               //填入晋级信息
                               mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                           }
                       }
                   }
                }
    }


    /**
     * v2升v3逻辑
     */
    private  void v2ToV3(MergeUser parentInvter,Integer prodectType){
        MergeUserUpgrade parentInvterUpgrade = new MergeUserUpgrade();
        parentInvterUpgrade.setUserId(parentInvter.getUserId());
        parentInvterUpgrade.setFromUserRank(parentInvter.getUserRank());
        parentInvterUpgrade.setUpgradedTime(new Date());
        int number = this.count(parentInvter,prodectType,2,1);
        if(number+1>=5){ //让推荐人晋级
            parentInvterUpgrade.setToUserRank(User.UserRank.V3);
            parentInvter.setUserRank(User.UserRank.V3);
                mergeUserMapper.update(parentInvter);//更新推荐人
                //填入晋级信息
                mergeUserUpgradeMapper.insert(parentInvterUpgrade);
        }else{//看销量
            List<Order> orderList = orderMapper.findAll(OrderQueryModel.builder().productTypeEQ(prodectType).userIdEQ(parentInvter.getUserId())
                    .createdTimeGTE(DateUtil.getMonthData(new Date(),-2,0)).build());
            if(orderList!=null&&!orderList.isEmpty()){
                orderList = orderList.stream().filter(v->v.getBuyerUserRank()==parentInvter.getUserRank()).collect(Collectors.toList());
                Long sum = orderList.parallelStream().mapToLong(Order::getQuantity).sum();
            if(sum>=150){
                    parentInvterUpgrade.setToUserRank(User.UserRank.V3);
                    parentInvter.setUserRank(User.UserRank.V3);
                    mergeUserMapper.update(parentInvter);//更新推荐人
                        //填入晋级信息
                    mergeUserUpgradeMapper.insert(parentInvterUpgrade);
                    }
                }
            }
        }

    /**
     * V3晋升V4机制
     * @param parentInvter
     * @param prodectType
     */
    private void v3ToV4(MergeUser parentInvter,Integer prodectType){
        MergeUserUpgrade parentInvterUpgrade = new MergeUserUpgrade();
        parentInvterUpgrade.setUserId(parentInvter.getUserId());
        parentInvterUpgrade.setFromUserRank(parentInvter.getUserRank());
        parentInvterUpgrade.setUpgradedTime(new Date());
        int number = this.count(parentInvter,prodectType,2,1);
        if(number+1>=5) { //让推荐人晋级
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
            if(num>=1800){
                parentInvterUpgrade.setToUserRank(User.UserRank.V4);
                parentInvter.setUserRank(User.UserRank.V4);
                mergeUserMapper.update(parentInvter);//更新推荐人
                //填入晋级信息
                mergeUserUpgradeMapper.insert(parentInvterUpgrade);
            }


        }
    }






}




