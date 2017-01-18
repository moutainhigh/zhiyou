package com.zy.service.impl;

import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.component.UsrComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.extend.Producer;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import me.chanjar.weixin.common.util.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
            validate(user);
            userMapper.insert(user);
            insertAccount(user); // 初始化
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

    @Override
    public void modifyNickname(@NotNull Long userId, @NotBlank String nickname) {
        findAndValidate(userId);

        User userForMerge = new User();
        userForMerge.setId(userId);
        userForMerge.setNickname(nickname);
        userMapper.merge(userForMerge, "nickname");

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
        if (user.getIsRoot() == null) {
            // 无视
        } else if (user.getIsRoot()) {
            if (isRoot) {
                return; // 幂等操作
            }
        } else {
            if (!isRoot) {
                return; // 幂等操作
            }
        }
        user.setIsRoot(isRoot);
        user.setRootName(rootName);
        userMapper.update(user);
        usrComponent.recordUserLog(id, operatorId, label + "子系统", remark);
    }

	@Override
	public void setParentId(Long id, Long parentId) {
		User user = findAndValidate(id);
        User parent = findAndValidate(parentId);
        if (parent.getUserType() != UserType.代理) {
            throw new BizException(BizCode.ERROR, "上级用户类型必须是代理");
        }
        if (parent.getUserRank() == UserRank.V0) {
            throw new BizException(BizCode.ERROR, "上级用户必须成为代理");
        }
        Long originParentId = parentId;
        Long plainParentId = user.getParentId();
        if (plainParentId != null && !originParentId.equals(plainParentId)) {
        	throw new BizException(BizCode.ERROR, "用户[id=" + id + "]已经存在上级, 不能设置上级");
        }
        if (parentId.equals(plainParentId)) {
            return; // 幂等操作
        }

        if (id.equals(parentId)) {
            throw new BizException(BizCode.ERROR, "上级不能是自己");
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
            throw new BizException(BizCode.ERROR, "上级用户类型必须是代理");
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
            throw new BizException(BizCode.ERROR, "上级不能是自己");
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
        usrComponent.recordUserLog(id, operatorId, "设置上级", "从" + plainParentId + "设置为" + originParentId + ", 备注" + remark);
    }


    private User findAndValidate(Long userId) {
        User user = userMapper.findOne(userId);
        validate(user, NOT_NULL, "user id " + userId + "not found");
        return user;
    }


}
