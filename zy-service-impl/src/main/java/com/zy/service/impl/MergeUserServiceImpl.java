package com.zy.service.impl;


import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.mapper.MergeUserLogMapper;
import com.zy.mapper.MergeUserMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.dto.UserTeamCountDto;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.UserlongQueryModel;
import com.zy.service.MergeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class MergeUserServiceImpl implements MergeUserService  {

    @Autowired
    private MergeUserMapper mergeUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MergeUserLogMapper mergeUserLogMapper;

    @Override
    public MergeUser findOne(Long id) {
        return mergeUserMapper.findOne(id);
    }

    @Override
    public MergeUser findByUserIdAndProductType( Long userId, Integer productType) {
        Map<String , Object> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("productType" , productType);
        return mergeUserMapper.findByUserIdAndProductType(map);
    }

    @Override
    public void create(MergeUser mergeUser) {
        validate(mergeUser);
        mergeUserMapper.insert(mergeUser);
    }

    @Override
    public Page<MergeUser> findPage(MergeUserQueryModel mergeUserQueryModel) {
        if(mergeUserQueryModel.getPageNumber() == null)
            mergeUserQueryModel.setPageNumber(0);
        if(mergeUserQueryModel.getPageSize() == null)
            mergeUserQueryModel.setPageSize(20);
        long total = mergeUserMapper.count(mergeUserQueryModel);
        List<MergeUser> data = mergeUserMapper.findAll(mergeUserQueryModel);
        Page<MergeUser> page = new Page<>();
        page.setPageNumber(mergeUserQueryModel.getPageNumber());
        page.setPageSize(mergeUserQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel) {
        return mergeUserMapper.findAll(mergeUserQueryModel);
    }

    @Override
    public void modifyParentId(Long id, Long parentId) {
        MergeUser mergeUser = findAndValidateMergeUser(id);
        mergeUser.setParentId(parentId);
        mergeUserMapper.merge(mergeUser,"parentId");
    }

    @Override
    public void modifyV4Id(Long id, Long v4Id) {
        MergeUser mergeUser = findAndValidateMergeUser(id);
        mergeUser.setParentId(v4Id);
        mergeUserMapper.merge(mergeUser,"v4Id");
    }

    @Override
    public long count(MergeUserQueryModel mergeUserQueryModel) {
        return 0;
    }

    private User findAndValidateUser(Long id) {
        validate(id, NOT_NULL, "id is null");
        User user = userMapper.findOne(id);
        validate(user, NOT_NULL, "product id" + id + " not found");
        return user;
    }

    private MergeUser findAndValidateMergeUser(Long id) {
        validate(id, NOT_NULL, "id is null");
        MergeUser mergeUser = mergeUserMapper.findOne(id);
        validate(mergeUser, NOT_NULL, "product id" + id + " not found");
        return mergeUser;
    }

    /**
     * 统计 直属团队
     * @param userId
     * @return
     */
    @Override
    public long[] countdirTotal(Long userId,Integer productType) {
        Map<String , Object> map = new HashMap<>();
        map.put("parentId" , userId);
        map.put("productType" , productType);
        long []data = new long[]{0,0,0,0,0};
        List<UserTeamCountDto> dataList =mergeUserMapper.countByUserId(map);
        for (UserTeamCountDto userTeamDto :dataList){
            if (User.UserRank.V4==userTeamDto.getUserRankEQ()){
                data[0] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            } else if (User.UserRank.V3==userTeamDto.getUserRankEQ()){
                data[1] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }else if (User.UserRank.V2==userTeamDto.getUserRankEQ()){
                data[2] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }else if (User.UserRank.V1==userTeamDto.getUserRankEQ()){
                data[3] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }else if (User.UserRank.V0==userTeamDto.getUserRankEQ()){
                data[4] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }
        }
        return data;
    }

    /**
     * 统计新增成员
     * @param userId
     * @param flag  是否统计 总数
     * @return
     */
    @Override
    public Map<String,Object> countNewMemTotal(Long userId, boolean flag) {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        long []data = new long[]{0,0,0,0,0};
        Map<String,Object>dataMap = new HashMap<String,Object>();
        dataMap.put("remark","从V0%");
        dataMap.put("operatedTimeBegin", DateUtil.getBeforeMonthBegin(new Date(),0,0));
        dataMap.put("operatedTimeEnd",DateUtil.getBeforeMonthEnd(new Date(),1,0));
        if(flag){
         //   long total=mergeUserLogMapper.count(dataMap);
         //   returnMap.put("total",total);
        }
        dataMap.put("parentid",userId);
        List<UserTeamCountDto>userTeamDtoList=mergeUserLogMapper.findGByRank(dataMap);
        for (UserTeamCountDto userTeamDto :userTeamDtoList){
            if (User.UserRank.V4==userTeamDto.getUserRankEQ()){
                data[0] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            } else if (User.UserRank.V3==userTeamDto.getUserRankEQ()){
                data[1] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }else if (User.UserRank.V2==userTeamDto.getUserRankEQ()){
                data[2] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }else if (User.UserRank.V1==userTeamDto.getUserRankEQ()){
                data[3] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }else if (User.UserRank.V0==userTeamDto.getUserRankEQ()){
                data[4] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }
        }
        returnMap.put("MTot",data);
        return returnMap;
    }

    /**
     * 处理排名
     * @param
     * @param flag 判断是否是详细页面
     * @return
     */
    @Override
    public Page<UserTeamDto> disposeRank(UserlongQueryModel userlongQueryModel, boolean flag) {
//        Long parentId = userlongQueryModel.getParentIdNL();//将id暂存下来
//        userlongQueryModel.setRemark("从V0%");
//        userlongQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
//        userlongQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0,0));
//        userlongQueryModel.setParentIdNL(null);
//        List<UserTeamDto> userRankList= mergeUserLogMapper.findByRank(userlongQueryModel);
//        Page<UserTeamDto> page = new Page<>();
//        if (flag){//详情页
//            long total =userLogMapper.countByRank(userlongQueryModel);
//            page.setTotal(total);
//        }
//        page.setPageNumber(userlongQueryModel.getPageNumber());
//        page.setPageSize(userlongQueryModel.getPageSize());
//        page.setData(userRankList);
        return null;
    }
}
