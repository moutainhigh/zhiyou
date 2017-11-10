package com.zy.service.impl;


import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.mapper.MergeUserLogMapper;
import com.zy.mapper.MergeUserMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.UserTeamCountDto;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserlongQueryModel;
import com.zy.service.MergeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

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
    public MergeUser findByUserIdAndProductType( Long userId, Integer productType) {
        Map<String , Object> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("productType" , productType);
        return mergeUserMapper.findByUserIdAndProductType(map);
    }

    @Override
    public MergeUser findBycodeAndProductType( String code, Integer productType) {
        Map<String , Object> map = new HashMap<>();
        map.put("code" , code);
        map.put("productType" , productType);
        return mergeUserMapper.findBycodeAndProductType(map);
    }

    @Override
    public void create(MergeUser mergeUser) {
        validate(mergeUser);
        mergeUserMapper.insert(mergeUser);
    }

    @Override
    public void createByUserId(Long userId, Integer productType) {
        MergeUser exitMergeuser = findByUserIdAndProductType(userId, productType);
        if(exitMergeuser == null){
            Date now = new Date();
            User user = userMapper.findOne(userId);
            MergeUser mergeUser = new MergeUser();
            mergeUser.setUserId(userId);
            mergeUser.setInviterId(user.getParentId());
            mergeUser.setProductType(2);
            mergeUser.setUserRank(user.getUserRank());
            mergeUser.setRegisterTime(now);
            mergeUser.setLastUpgradedTime(now);
            if (user.getUserRank() != User.UserRank.V0){
                mergeUser.setCode(this.getCode());
            }
            mergeUser.setV4Id(null);
            //查询直属上级
            User parent = user;
            if(user.getParentId() == null){
                //原始关系没有上级，将新上级设置为万伟民
                mergeUser.setParentId(10370l);
            }else {
                do{
                    parent = userMapper.findOne(parent.getParentId());
                }while (parent != null && findByUserIdAndProductType(parent.getId(),2) == null && parent.getParentId() != null );
                if(parent == null){
                    //父级团队里面没有一个转移的，将新上级设置为万伟民
                    mergeUser.setParentId(10370l);
                }else {
                    if(findByUserIdAndProductType(parent.getId(),2) == null){
                        mergeUser.setParentId(10370l);
                    }else {
                        mergeUser.setParentId(parent.getId());
                    }
                }
            }
//            //查找直属特级
//            User v4User = user;
//            do{
//                v4User = userMapper.findOne(v4User.getParentId());
//            }while(v4User != null && v4User.getParentId() != null && (findByUserIdAndProductType(parent.getId(),2) == null || findByUserIdAndProductType(parent.getId(),2).getUserRank() != User.UserRank.V4));
//            Long v4UserId = v4User == null ? null : v4User.getId();
//            mergeUser.setV4Id(v4UserId);
            mergeUserMapper.insert(mergeUser);
            //修改parentId
            List<User> all = userMapper.findAll(UserQueryModel.builder().parentIdEQ(userId).build()) ;
            if(all != null && !all.isEmpty()){
                for (User u: all) {
                    MergeUser m = findByUserIdAndProductType(u.getId(), 2);
                    if(m != null){
                       m.setParentId(userId);
                       mergeUserMapper.merge(m,"parentId");
                    }
                }
            }
        }
    }

    static char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    static Random random = new Random();
    private String createCode() {
        //授权书
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            s.append(codeSeq[random.nextInt(codeSeq.length)]);
        }
        return "ZY" + s.toString();
    }

    public String getCode() {

        String code = createCode();
        MergeUser mergeUser = this.findBycodeAndProductType(code,2);
        int times = 0;
        while (mergeUser != null) {
            if (times > 1000) {
                throw new BizException(BizCode.ERROR, "生成code失败");
            }
            code = createCode();
            mergeUser = this.findBycodeAndProductType(code,2);
            times++;
        }
        return code;
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
    public void modifyParentId(Long id, Long parentId,Integer productType) {
        MergeUser mergeUser = findAndValidateMergeUser(id,productType);
        mergeUser.setParentId(parentId);
        mergeUserMapper.merge(mergeUser,"parentId");
    }


    @Override
    public void modifyV4Id(Long id, Long v4Id,Integer productType) {
        MergeUser mergeUser = this.findAndValidateMergeUser(id,productType);
        mergeUser.setV4Id(v4Id);
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

    private MergeUser findAndValidateMergeUser(Long id,Integer productType) {
        validate(id, NOT_NULL, "id is null");
        MergeUser mergeUser = findByUserIdAndProductType(id,productType);
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
            long total=mergeUserLogMapper.count(dataMap);
            returnMap.put("total",total);
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
     * 判断是不是新晋特级
     * @param id
     * @return
     */
    @Override
    public boolean findNewOne(Long id) {
        Map<String,Object>dataMap = new HashMap<String,Object>();
        dataMap.put("remark","%改为V4%");
        dataMap.put("operatedTimeBegin", DateUtil.getBeforeMonthBegin(new Date(),0,0));
        dataMap.put("operatedTimeEnd",DateUtil.getBeforeMonthEnd(new Date(),1,0));
        dataMap.put("userId",id);
        long total=mergeUserLogMapper.count(dataMap);
        if(total>0){
            return true;
        }
        return false;
    }


}
