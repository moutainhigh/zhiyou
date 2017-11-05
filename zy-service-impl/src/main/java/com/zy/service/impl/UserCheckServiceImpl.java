package com.zy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zy.common.exception.BizException;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.mapper.MergeUserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.service.UserCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

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
        //VIP
       if(quantity>=4&&quantity<19){
           mergeUser.setUserRank(User.UserRank.V1);
           MergeUser parent = this.findParent(mergeUser,prodectType,2);
               if(parent!=null){
                   mergeUser.setParentId(parent.getUserId());
                   mergeUserMapper.update(mergeUser);
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
                mergeUserMapper.update(mergeUser);
            }else{
                throw new BizException(BizCode.ERROR, "数据异常");
            }
            //处理 推荐人 晋升
            Map<String,Object> map = new HashMap<>();
            map.put("productType" , prodectType);
            map.put("userId" , mergeUser.getInviterId());
            MergeUser parentInvter =  mergeUserMapper.findByUserIdAndProductType(map);
            if(parentInvter!=null&&parentInvter.getUserRank()!=null){
                if (parentInvter.getUserRank()== User.UserRank.V1){ //vip 则看满足晋升不
                    List<MergeUser> mergeUserList = mergeUserMapper.findAll(MergeUserQueryModel.builder().inviterIdEQ(parentInvter.getUserId()).build());
                    if (mergeUserList==null){
                        mergeUserList = mergeUserList.stream().filter(v->(v.getUserRank()==User.UserRank.V1
                        ||v.getUserRank()==User.UserRank.V2||v.getUserRank()==User.UserRank.V3||v.getUserRank()==User.UserRank.V4)).collect(Collectors.toList());
                        if(mergeUserList.size()+1>=5){//晋级

                        }
                    }
                }
            }
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


}
