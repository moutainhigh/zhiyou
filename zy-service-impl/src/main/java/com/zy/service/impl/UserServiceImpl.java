package com.zy.service.impl;

import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.component.FncComponent;
import com.zy.component.UsrComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.UserInfo;
import com.zy.extend.Producer;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.UserInfoMapper;
import com.zy.mapper.UserLogMapper;
import com.zy.mapper.UserInfoMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.model.dto.DepositSumDto;
import com.zy.model.dto.UserTeamCountDto;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserlongQueryModel;
import com.zy.service.UserService;
import com.zy.model.dto.UserDto;
import me.chanjar.weixin.common.util.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.*;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.CurrencyType.现金;
import static com.zy.entity.fnc.CurrencyType.积分;
import static com.zy.model.Constants.TOPIC_REGISTER_SUCCESS;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UsrComponent usrComponent;

    @Autowired
    private Producer producer;

    @Autowired
    private FncComponent fncComponent;

    @Autowired
    private UserLogMapper userLogMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public User findOne(@NotNull Long id) {
        return userMapper.findOne(id);
    }

    @Override
    public User findByPhone(@NotBlank String phone) {
        return userMapper.findByPhone(phone);
    }

    @Override
    public User findByCode(@NotBlank String code) {
        return userMapper.findByCode(code);
    }

    @Override
    public Page<User> findPage(@NotNull UserQueryModel userQueryModel) {
        if (userQueryModel.getPageNumber() == null)
            userQueryModel.setPageNumber(0);
        if (userQueryModel.getPageSize() == null)
            userQueryModel.setPageSize(20);
        long total = userMapper.count(userQueryModel);
        List<User> data = userMapper.findAll(userQueryModel);
        Page<User> page = new Page<>();
        page.setPageNumber(userQueryModel.getPageNumber());
        page.setPageSize(userQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<User> findAll(@NotNull UserQueryModel userQueryModel) {
        return userMapper.findAll(userQueryModel);
    }

    @Override
    public User registerAgent(@NotNull AgentRegisterDto agentRegisterDto) {
        validate(agentRegisterDto);

        Long inviterId = agentRegisterDto.getInviterId();
        if (inviterId != null) {
            User inviter = userMapper.findOne(inviterId);
            if (inviter == null || inviter.getUserType() != UserType.代理) {
                inviterId = null;
            }
        }

        String openId = agentRegisterDto.getOpenId();
        String phone = agentRegisterDto.getPhone();
        String unionId = agentRegisterDto.getUnionId();
        Long parentId = agentRegisterDto.getParentId();
        String realname = agentRegisterDto.getRealname();
        validate(realname, NOT_BLANK, "realname is blank");

        String avatar = agentRegisterDto.getAvatar();
        String nickname = agentRegisterDto.getNickname();
        String registerIp = agentRegisterDto.getRegisterIp();

        User user = userMapper.findByOpenId(openId);

        if (user != null) {
            if (user.getPhone().equals(phone)) {
                return user; // 幂等操作
            } else {
                throw new BizException(BizCode.ERROR, "微信已绑定其他手机");
            }
        }

        user = userMapper.findByPhone(phone);
        if (user != null) {
            if (user.getUserType() != UserType.代理) {
                throw new BizException(BizCode.ERROR, "微信只能绑定代理");
            }
            if (user.getOpenId() != null) {
                if (user.getOpenId().equals(openId)) {
                    return user; // 幂等操作 虽然不可能到达
                } else {
                    throw new BizException(BizCode.ERROR, "手机已经绑定其他微信");
                }
            } else {
                /* 绑定 */
                user.setNickname(nickname);
                user.setAvatar(avatar);
/*				user.setRegisterTime(new Date());
				user.setRegisterIp(registerIp);*/
                user.setOpenId(openId);
                user.setUnionId(unionId);
                userMapper.update(user);
            }
        } else {
			/* 注册 */
            user = new User();
            user.setPhone(phone);
            user.setUserType(UserType.代理);
            user.setRegisterIp(registerIp);
            user.setInviterId(inviterId);
            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setRegisterTime(new Date());
            user.setIsFrozen(false);
            user.setUserRank(UserRank.V0);
            user.setOpenId(openId);
            user.setUnionId(unionId);
            user.setLastloginTime(new Date());
            validate(user);
            userMapper.insert(user);
            insertAccount(user); // 初始化
            insertUserInfo(user.getId(), realname);
        }

        if (parentId != null) {
            User parent = userMapper.findOne(parentId);
            validate(parent, NOT_NULL, "parent user id[ " + parentId + " ] not found");
            setParentId(user.getId(), parentId);
        }


        producer.send(TOPIC_REGISTER_SUCCESS, user.getId());
        return user;
    }

    @Override
    public long count(@NotNull UserQueryModel userQueryModel) {
        return userMapper.count(userQueryModel);
    }

    @Override
    public String hashPassword(@NotBlank String plainPassword) {
        return ServiceUtils.hashPassword(plainPassword);
    }

    @Override
    public void freezeAdmin(@NotNull Long id, @NotNull Long operatorId) {
        User user = findAndValidate(id);
        if (user.getIsFrozen()) {
            return; // 幂等操作
        }
        user.setIsFrozen(true);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "冻结", null);
    }

    @Override
    public void unfreezeAdmin(@NotNull Long id, @NotNull Long operatorId) {

        User user = findAndValidate(id);
        user.setIsFrozen(false);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "解冻", null);
    }

    @Override
    public void modifyUserRankAdmin(@NotNull Long id, @NotNull UserRank userRank, @NotNull Long operatorId, String remark) {

        User user = findAndValidate(id);
        UserRank plainUserRank = user.getUserRank();

        if (user.getUserType() != UserType.代理) {
            throw new BizException(BizCode.ERROR, "修改无效, 用户类型必须为代理");
        }

        /*if (plainUserRank == UserRank.V0) {
            throw new BizException(BizCode.ERROR, "修改无效, 意向代理不能修改");
        }*/

        if (userRank == plainUserRank) {
            return; // 幂等操作
        }

        usrComponent.upgrade(id, plainUserRank, userRank);
        usrComponent.recordUserLog(id, operatorId, "设置用户等级", "从" + plainUserRank + "改为" + userRank + ", 备注" + remark);
    }

    @Override
    public User findByOpenId(@NotBlank String openId) {
        return userMapper.findByOpenId(openId);
    }


    /* 注册自动生成账号 */
    private void insertAccount(User user) {
        Long userId = user.getId();
        BigDecimal zero = new BigDecimal("0.00");
		/* account insert */
        {
            Account account = new Account();
            account.setAmount(zero);
            account.setCurrencyType(现金);
            account.setUserId(userId);
            account.setVersion(0);
            validate(account);
            accountMapper.insert(account);
        }
		{
			Account account = new Account();
			account.setAmount(zero);
			account.setCurrencyType(积分);
			account.setUserId(userId);
			account.setVersion(0);
			validate(account);
			accountMapper.insert(account);
		}
		/*{
			Account account = new Account();
			account.setAmount(zero);
			account.setCurrencyType(金币);
			account.setUserId(userId);
			account.setVersion(0);
			validate(account);
			accountMapper.insert(account);
		}*/

    }

    private void insertUserInfo(Long userId, String realname) {
        validate(realname, NOT_BLANK, "realname is blank");
        UserInfo userInfo = userInfoMapper.findByUserId(userId);
        if (userInfo != null) {
            return;
        }
        userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setRealname(realname);
        userInfo.setConfirmStatus(ConfirmStatus.待审核);
        userInfo.setAppliedTime(new Date());
        userInfo.setRealFlag(0);
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void modifyNickname(@NotNull Long userId, @NotBlank String nickname) {
        findAndValidate(userId);
        User userForMerge = new User();
        userForMerge.setId(userId);
        userForMerge.setNickname(nickname);
        userMapper.merge(userForMerge, "nickname");

    }

    @Override
    public void modifyLastLoginTime(@NotNull Long userId) {
        findAndValidate(userId);
        User user = new User();
        user.setId(userId);
        user.setLastloginTime(new Date());
        userMapper.merge(user, "lastloginTime");
    }

    @Override
    public void modifyAvatar(@NotNull Long userId, @NotBlank @URL String avatar) {
        findAndValidate(userId);

        User userForMerge = new User();
        userForMerge.setId(userId);
        userForMerge.setAvatar(avatar);
        userMapper.merge(userForMerge, "avatar");
    }

    static char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    static Random random = new Random();
    private String getCode() {
        //授权书
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            s.append(codeSeq[random.nextInt(codeSeq.length)]);
        }
        return "ZY" + s.toString();
    }

    @Override
    public void generateCode(@NotNull Long id) {
        User user = findAndValidate(id);
        if (StringUtils.isNotBlank(user.getCode())) {
            return; // 幂等操作
        }

        String code = getCode();
        User userByCode = userMapper.findByCode(code);
        int times = 0;
        while (userByCode != null) {
            if (times > 1000) {
                throw new BizException(BizCode.ERROR, "生成code失败");
            }
            code = getCode();
            userByCode = userMapper.findByCode(code);
            times++;
        }
        user.setCode(code);
        userMapper.update(user);

    }

    @Override
    public void modifyPhoneAdmin(@NotNull Long id, @NotBlank String phone, @NotNull Long operatorId) {
        User user = findAndValidate(id);
        String plainPhone = user.getPhone();
        if (phone.equals(plainPhone)) {
            return; // 幂等操作
        }
        if (userMapper.findByPhone(phone) != null) {
            throw new BizException(BizCode.ERROR, "手机号重复");
        }
        user.setPhone(phone);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "修改手机", "从" + plainPhone + "修改为" + phone);
    }


    @Override
    public void modifyNicknameAdmin(@NotNull Long id, @NotBlank String nickname, @NotNull Long operatorId) {
        User user = findAndValidate(id);
        String plainNickname = user.getNickname();
        if (nickname.equals(plainNickname)) {
            return; // 幂等操作
        }
        user.setNickname(nickname);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "修改昵称", "从" + plainNickname + "修改为" + nickname);
    }


    @Override
    public void modifyPassword(@NotNull Long id, @NotBlank String password) {
        User user = findAndValidate(id);
        user.setPassword(ServiceUtils.hashPassword(password));
        userMapper.update(user);
    }

    @Override
    public void modifyPasswordAdmin(@NotNull Long id, @NotBlank String password, @NotNull Long operatorId) {
        User user = findAndValidate(id);
        user.setPassword(ServiceUtils.hashPassword(password));
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "修改密码", null);
    }

    @Override
    public void unbindAdmin(@NotNull Long id, @NotNull Long operatorId, String remark) {
        User user = findAndValidate(id);
        if (user.getOpenId() == null) {
            return; // 幂等
        }
        user.setOpenId(null);
        user.setUnionId(null);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "微信解绑", remark);
    }

    @Override
    public void modifyIsRootAdmin(@NotNull Long id, boolean isRoot, String rootName, Long operatorId, String remark) {
        User user = findAndValidate(id);
        String label = isRoot ? "设置" : "取消";
        if (user.getIsRoot() != null) {
            if (user.getIsRoot() == isRoot) {
                return;
            }
        }
        user.setIsRoot(isRoot);
        user.setRootName(rootName);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, label + "子系统", remark);

    }

    @Override
    public void modifyIsBoss(@NotNull Long id, boolean isBoss, String bossName, Long operatorId) {
        User user = findAndValidate(id);
        String label = isBoss ? "设置" : "取消";
        if (user.getIsBoss() != null) {
            if (user.getIsBoss() == isBoss) {
                return;
            }
        }
        if (isBoss) {
            validate(bossName, NOT_BLANK, "boss name is blank");
        }
        user.setIsBoss(isBoss);
        user.setBossId(null);
        user.setBossName(bossName);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, label + "总经理", null);

        for(Long userId : findChildren(id)) {
            User merge = new User();
            merge.setId(userId);
            merge.setBossId(id);
            userMapper.merge(merge, "bossId");
        }
    }

    private List<Long> findChildren(Long id) {
        List<Long> userIds = new ArrayList<>();

        List<User> v4Children = userMapper.findAll(UserQueryModel.builder().parentIdEQ(id).userRankEQ(UserRank.V4).build());
        if (!v4Children.isEmpty()) {
            Long[] v4UserIds = v4Children.stream().map(v -> v.getId()).toArray(Long[]::new);
            List<User> v3User = userMapper.findAll(UserQueryModel.builder().parentIdIN(v4UserIds).userRankEQ(UserRank.V3).build());
            Long[] v3UserIds = v3User.stream().map(v -> v.getId()).toArray(Long[]::new);

            userIds.addAll(Arrays.asList(v4UserIds));
            userIds.addAll(Arrays.asList(v3UserIds));

            while(v3UserIds != null && v3UserIds.length > 0) {
                List<User> all = userMapper.findAll(UserQueryModel.builder().parentIdIN(v3UserIds).userRankEQ(UserRank.V3).build());
                v3UserIds = all.stream().map(v -> v.getId()).toArray(Long[]::new);
                userIds.addAll(Arrays.asList(v3UserIds));
            }

        }
        return userIds;
    }

    @Override
    public void modifyBossId(@NotNull Long id, @NotNull Long bossId) {
        validate(id, v -> !v.equals(bossId), "user id is same to bossId, error");
        findAndValidate(id);
        User boss = findAndValidate(bossId);
        validate(boss, v -> v.getIsBoss(), "boss id " + bossId + " is wrong");

        User merge = new User();
        merge.setId(id);
        merge.setBossId(bossId);
        userMapper.merge(merge, "bossId");
    }

    @Override
    public void modifyIsDirector(@NotNull Long id, boolean isDirector) {
        User userForMerge = new User();
        userForMerge.setId(id);
        userForMerge.setIsDirector(isDirector);
        userForMerge.setIsHonorDirector(!isDirector);
        userMapper.merge(userForMerge, "isDirector", "isHonorDirector");
    }

    @Override
    public void modifyIsHonorDirector(@NotNull Long id, boolean isHonorDirector) {
        User userForMerge = new User();
        userForMerge.setId(id);
        userForMerge.setIsHonorDirector(isHonorDirector);
        userForMerge.setIsDirector(!isHonorDirector);
        userMapper.merge(userForMerge, "isDirector", "isHonorDirector");
    }

    @Override
    public void modifyIsShareholder(@NotNull Long id, boolean isShareholder) {
        User user = findAndValidate(id);
        if(user.getIsShareholder() != null) {
            return ;
        }
        User userForMerge = new User();
        userForMerge.setId(id);
        userForMerge.setIsShareholder(isShareholder);
        userMapper.merge(userForMerge, "isShareholder");

        //升级股东获得50W股
        if(isShareholder) {
            fncComponent.createAndGrantProfit(id, Profit.ProfitType.股份奖励, null, user.getNickname() + "升级股东,获得股份奖励", CurrencyType.货币股份, new BigDecimal("500000.00"), new Date());
        }
    }

    @Override
	public void setParentId(Long id, Long parentId) {
		User user = findAndValidate(id);
        User parent = findAndValidate(parentId);
        if (parent.getUserType() != UserType.代理) {
            throw new BizException(BizCode.ERROR, "推荐人用户类型必须是代理");
        }
        if (parent.getUserRank() == UserRank.V0) {
            throw new BizException(BizCode.ERROR, "推荐人用户必须成为代理");
        }
        Long originParentId = parentId;
        Long plainParentId = user.getParentId();
        if (plainParentId != null && !originParentId.equals(plainParentId)) {
        	throw new BizException(BizCode.ERROR, "用户[id=" + id + "]已经存在推荐人, 不能设置推荐人");
        }
        if (parentId.equals(plainParentId)) {
            return; // 幂等操作
        }

        if (id.equals(parentId)) {
            throw new BizException(BizCode.ERROR, "推荐人不能是自己");
        }

        while (parentId != null) {
            parent = userMapper.findOne(parentId);
            parentId = parent.getParentId();
            if (parentId != null && parentId.equals(id)) {
                throw new BizException(BizCode.ERROR, "出现循环引用");
            }
        }
        user.setParentId(originParentId);
        userMapper.update(user);
	}
	
    @Override
    public void modifyParentIdAdmin(@NotNull Long id, @NotNull Long parentId, @NotNull Long operatorId, String remark) {
        User user = findAndValidate(id);
        User parent = findAndValidate(parentId);
        if (parent.getUserType() != UserType.代理) {
            throw new BizException(BizCode.ERROR, "推荐人用户类型必须是代理");
        }
        /*
        if (parent.getUserRank() == UserRank.V0) {
            throw new BizException(BizCode.ERROR, "上级用户必须成为代理");
        }*/

        Long originParentId = parentId;
        Long plainParentId = user.getParentId();
        if (originParentId.equals(plainParentId)) {
            return; // 幂等操作
        }

        if (id.equals(parentId)) {
            throw new BizException(BizCode.ERROR, "推荐人不能是自己");
        }

        while (parentId != null) {
            parent = userMapper.findOne(parentId);
            parentId = parent.getParentId();
            if (parentId != null && parentId.equals(id)) {
                throw new BizException(BizCode.ERROR, "出现循环引用");
            }
        }

        user.setParentId(originParentId);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, "设置推荐人", "从" + plainParentId + "设置为" + originParentId + ", 备注" + remark);
    }


    private User findAndValidate(Long userId) {
        User user = userMapper.findOne(userId);
        validate(user, NOT_NULL, "user id " + userId + "not found");
        return user;
    }



    /**
     * 统计 团队 总人数
     * @param userId
     * @return
     */
    @Override
    public long[] conyteamTotal(Long userId) {
        User user = this.findAndValidate(userId);
        validate(user, NOT_NULL, "user id " + userId + "not found");
        long []data = new long[]{0,0,0,0};
        if(user.getUserRank()==UserRank.V4){//特级的做   递归处理
            Map<String,Long> returnMap = new HashMap<String,Long>();
         Map<String,Long> dataMap = conyteamTotalV4(userId,returnMap);
            data[0] = dataMap.get("V4");
            data[1] = dataMap.get("V3");
            data[2] = dataMap.get("V2");
            data[3] = dataMap.get("V1");
         }else{ //直属查询
            List<UserTeamCountDto> dataList =userMapper.countByUserId(userId);
            for (UserTeamCountDto userTeamDto :dataList){
                if (UserRank.V4==userTeamDto.getUserRankEQ()){
                    data[0] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
                } else if (UserRank.V3==userTeamDto.getUserRankEQ()){
                    data[1] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
                }else if (UserRank.V2==userTeamDto.getUserRankEQ()){
                    data[2] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
                }else if (UserRank.V1==userTeamDto.getUserRankEQ()){
                    data[4] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
                }
            }
         }
        return data;
    }

    /**
     * 递归统计数据
     * @return
     */
   private Map<String,Long> conyteamTotalV4(Long userId, Map<String,Long> returnMap){
       List<UserTeamCountDto> dataList =userMapper.countByUserId(userId);
       if (dataList==null||dataList.isEmpty()){
           return returnMap;
       }
       for (UserTeamCountDto userTeamDto :dataList){
           if (UserRank.V4==userTeamDto.getUserRankEQ()){
               Long countV4 = returnMap.get("V4")==null?0L:returnMap.get("V4");
                  if (userTeamDto.getTotalnumber()!=null){
                      countV4=countV4+userTeamDto.getTotalnumber();
                  }
               returnMap.put("V4",countV4);
           }else if (UserRank.V3==userTeamDto.getUserRankEQ()){
               Long countV3 = returnMap.get("V3")==null?0L:returnMap.get("V3");
                   if (userTeamDto.getTotalnumber()!=null){
                       countV3=countV3+userTeamDto.getTotalnumber();
                   }
               returnMap.put("V3",countV3);

           }else if(UserRank.V2==userTeamDto.getUserRankEQ()){
               Long countV2 = returnMap.get("V2")==null?0L:returnMap.get("V2");
                   if (userTeamDto.getTotalnumber()!=null){
                       countV2=countV2+userTeamDto.getTotalnumber();
                   }
               returnMap.put("V2",countV2);

           }else if (UserRank.V1==userTeamDto.getUserRankEQ()){
               Long countV1 = returnMap.get("V1")==null?0L:returnMap.get("V1");
               if (userTeamDto.getTotalnumber()!=null){
                   countV1=countV1+userTeamDto.getTotalnumber();
               }
               returnMap.put("V1",countV1);
           }
       }
       UserQueryModel userQueryModel = new UserQueryModel();
       userQueryModel.setParentIdNL(userId);
       List<User> userList = userMapper.findAll(userQueryModel);
       for (User user :userList){
           conyteamTotalV4(user.getId(),returnMap);
       }
       return returnMap;
   }

    /**
     * 统计 直属团队
     * @param userId
     * @return
     */
    @Override
    public long[] countdirTotal(Long userId) {
        long []data = new long[]{0,0,0,0,0};
        List<UserTeamCountDto> dataList =userMapper.countByUserId(userId);
        for (UserTeamCountDto userTeamDto :dataList){
            if (UserRank.V4==userTeamDto.getUserRankEQ()){
                data[0] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            } else if (UserRank.V3==userTeamDto.getUserRankEQ()){
                data[1] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }else if (UserRank.V2==userTeamDto.getUserRankEQ()){
                data[2] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }else if (UserRank.V1==userTeamDto.getUserRankEQ()){
                data[3] = userTeamDto.getTotalnumber()==null?0L:userTeamDto.getTotalnumber();
            }else if (UserRank.V0==userTeamDto.getUserRankEQ()){
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
              long total=userLogMapper.count(dataMap);
              returnMap.put("total",total);
          }
        dataMap.put("parentid",userId);
        List<UserTeamCountDto>userTeamDtoList=userLogMapper.findGByRank(dataMap);
        for (UserTeamCountDto userTeamDto :userTeamDtoList){
            if (UserRank.V4==userTeamDto.getUserRankEQ()){
                data[0] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            } else if (UserRank.V3==userTeamDto.getUserRankEQ()){
                data[1] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }else if (UserRank.V2==userTeamDto.getUserRankEQ()){
                data[2] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }else if (UserRank.V1==userTeamDto.getUserRankEQ()){
                data[3] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }else if (UserRank.V0==userTeamDto.getUserRankEQ()){
                data[4] = userTeamDto.getTotalnumber()==null?0l:userTeamDto.getTotalnumber();
            }
        }
        returnMap.put("MTot",data);
        return returnMap;
    }

    /**
     * 获取 真实姓名
     * @param userId
     * @return
     */
    @Override
    public String findRealName(Long userId) {
        validate(userId, NOT_NULL, "user id null");
        validate(userMapper.findOne(userId), NOT_NULL, "user id null");
        UserInfo userInfo = userInfoMapper.findByUserId(userId);
        if(userInfo!=null&&userInfo.getRealname()!=null){
            return userInfo.getRealname();
        }else{
            User user = userMapper.findOne(userId);
            return user.getNickname();
        }
    }

    /**
     * 处理排名
     * @param
     * @param flag 判断是否是详细页面
     * @return
     */
    @Override
    public Page<UserTeamDto> disposeRank(UserlongQueryModel userlongQueryModel, boolean flag) {
        Long parentId = userlongQueryModel.getParentIdNL();//将id暂存下来
        userlongQueryModel.setRemark("从V0%");
        userlongQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
        userlongQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0,0));
        userlongQueryModel.setParentIdNL(null);
        List<UserTeamDto> userRankList= userLogMapper.findByRank(userlongQueryModel);
        Page<UserTeamDto> page = new Page<>();
        if (flag){//详情页
           long total =userLogMapper.countByRank(userlongQueryModel);
            page.setTotal(total);
        }
        page.setPageNumber(userlongQueryModel.getPageNumber());
        page.setPageSize(userlongQueryModel.getPageSize());
        page.setData(userRankList);
        return page;
    }

    /**
     * 统计查询 活跃人数
     * @param userQueryModel
     * @return
     */
    @Override
    public long countByActive(UserQueryModel userQueryModel) {
        return userMapper.countByActive(userQueryModel);
    }

    /**
     * 查询  不活跃的人数
     * @param userQueryModel
     * @param flag
     * @return
     */
    @Override
    public Page<User> findActive(UserQueryModel userQueryModel, boolean flag) {
        userQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
        userQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),-2,0));
        List<User> userRankList= userMapper.findByNotActive(userQueryModel);
        Page<User> page = new Page<>();
        if (flag) {
            long total = userMapper.countByNotActive(userQueryModel);
            page.setTotal(total);
        }
        page.setPageNumber(userQueryModel.getPageNumber());
        page.setPageSize(userQueryModel.getPageSize());
        page.setData(userRankList);
        return page;
    }

    /**
     * 统计 排名级别
     * @param userlongQueryModel
     * @return
     */
    @Override
    public List<DepositSumDto> findRankGroup(UserlongQueryModel userlongQueryModel) {
        return userLogMapper.findRankGroup(userlongQueryModel);
    }

    /**
     * 查询  当前人的排名
     * @param userlongQueryModel
     * @return
     */
    @Override
    public List<UserTeamDto> findByRank(UserlongQueryModel userlongQueryModel) {
        return userLogMapper.findByRank(userlongQueryModel);
    }

    /**
     *新进特级
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> findNewSup(long[] ids) {
        Map<String,Object>dataMap = new HashMap<String,Object>();
        dataMap.put("remark","%改为V4%");
        dataMap.put("endTime", DateUtil.getBeforeMonthEnd(new Date(),1,0));
        dataMap.put("beginTime",DateUtil.getBeforeMonthBegin(new Date(),0,0));
        List<User>userList = userMapper.findSupAll(dataMap);
        dataMap.put("parentIdIN",ids);
        List<User>myuserList = userMapper.findSupAll(dataMap);
        dataMap.put("UA",userList);
        dataMap.put("MY",myuserList);
        return dataMap;
    }

    /**
     * 查询团队新成员
     * @param userQueryModel
     * @return
     */
    @Override
    public Page<User> findAddpeople(UserQueryModel userQueryModel) {
        userQueryModel.setRemark("从V0%");
        userQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
        userQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0,0));
        List<User>myuserList = userMapper.findAddpeople(userQueryModel);
        Page<User> page = new Page<>();
        page.setPageNumber(userQueryModel.getPageNumber());
        page.setPageSize(userQueryModel.getPageSize());
        page.setData(myuserList);
        return page;
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
        long total=userLogMapper.count(dataMap);
        if(total>0){
            return true;
        }
        return false;
    }


    /**
     *查询所有  没有默认分页
     * @param userQueryModel
     * @return
     */
    @Override
    public Page<User> findPage1(UserQueryModel userQueryModel) {
        long total = userMapper.count(userQueryModel);
        List<User> data = userMapper.findAll(userQueryModel);
        Page<User> page = new Page<>();
        page.setPageNumber(userQueryModel.getPageNumber());
        page.setPageSize(userQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    /**
     * 查询说有用户
     * @param userQueryModel
     * @return
     */
    @Override
    public List<UserDto> findUserAll(UserQueryModel userQueryModel) {
        return userMapper.findUserAll(userQueryModel);
    }

    @Override
    public long countUserAll(UserQueryModel userQueryModel) {
        return userMapper.countUserAll(userQueryModel);
    }


}
