package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.entity.account.AccountNumber;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.mapper.AccountNumberMapper;
import com.zy.mapper.UserInfoMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.AccountNumberQueryModel;
import com.zy.service.AccountNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

/**
 * Created by it001 on 2017-11-03.
 */
@Service
@Validated
public class AccountNumberServiceImpl implements AccountNumberService {

    @Autowired
    private AccountNumberMapper accountNumberMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    /**
     * 查询 用户
     * @param accountNumberQueryModel
     * @return
     */
    @Override
    public List<AccountNumber> findAll(AccountNumberQueryModel accountNumberQueryModel) {
        return accountNumberMapper.findAll(accountNumberQueryModel);
    }

    /**
     * 分页查询
     * @param accountNumberQueryModel
     * @return
     */
    @Override
    public Page<AccountNumber> findPage(AccountNumberQueryModel accountNumberQueryModel) {
        if (accountNumberQueryModel.getPageNumber() == null)
            accountNumberQueryModel.setPageNumber(0);
        if (accountNumberQueryModel.getPageSize() == null)
            accountNumberQueryModel.setPageSize(20);
        long total = accountNumberMapper.count(accountNumberQueryModel);
        List<AccountNumber> data = accountNumberMapper.findAll(accountNumberQueryModel);
        Page<AccountNumber> page = new Page<>();
        page.setPageNumber(accountNumberQueryModel.getPageNumber());
        page.setPageSize(accountNumberQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    /**
     * 更新
     * @param accountNumber
     */
    @Override
    public void update(AccountNumber accountNumber) {
        accountNumberMapper.update(accountNumber);
    }

    /**
     * 按id查询
     * @param id
     * @return
     */
    @Override
    public AccountNumber findOne(Long id) {
        return accountNumberMapper.findOne(id);
    }

    /**
     *按 手机号 查询账号  迁移数据<br/>
     * N:newphone ,O :oldphone
     * @param phone
     * @param type
     * @return
     */
    @Override
    public AccountNumber findOneByPhone(String phone,String type) {
        AccountNumberQueryModel accountNumberQueryModel = new AccountNumberQueryModel();
        if("N".equals(type)){
            accountNumberQueryModel.setNewPhoneEQ(phone);
        }else{
            accountNumberQueryModel.setOldPhoneEQ(phone);
        }
        accountNumberQueryModel.setFlageEQ("0"); //未使用的
        List<AccountNumber> accountNumbersList = accountNumberMapper.findAll(accountNumberQueryModel);
        if (accountNumbersList!=null&&!accountNumbersList.isEmpty()){
            return accountNumbersList.get(0); //返回 id倒序的最后一条
        }
        return null;
    }

    /**
     * 插入 数据
     * @param accountNumber
     */
    @Override
    public void insert(AccountNumber accountNumber) {
        accountNumberMapper.insert(accountNumber);
    }

    /**
     * 新增  账号交换 数据
     * @param newPhone
     * @param oldUser
     * @param id
     */
    @Override
    public void insertAccountNumber(String newPhone, User oldUser, Long id) {
        AccountNumber accountNumber = new AccountNumber();
        accountNumber.setCreateBy(id);
        accountNumber.setCreateDate(new Date());
        accountNumber.setNewPhone(newPhone);
        accountNumber.setOldName(oldUser.getNickname());
        accountNumber.setOldPhone(oldUser.getPhone());
        accountNumber.setFlage("0");
        accountNumberMapper.insert(accountNumber);
    }

    /**
     * 交换 用户信息
     * @param oldUser
     * @param newUser
     * @param id
     */
    @Override
    public void insertChangeAccountNumber(User oldUser, User newUser, Long id) {
        AccountNumber accountNumber = new AccountNumber();
        accountNumber.setCreateBy(id);
        accountNumber.setCreateDate(new Date());
        accountNumber.setNewPhone(newUser.getPhone());
        accountNumber.setOldName(oldUser.getNickname());
        accountNumber.setOldPhone(oldUser.getPhone());
        accountNumber.setFlage("1");
        accountNumberMapper.insert(accountNumber);
        //修改 关系
        //把数据copy出来
        String openid =oldUser.getOpenId();
        String unionId = oldUser.getUnionId();
        String avatar = oldUser.getAvatar();
        String nickName =oldUser.getNickname();
        String pwd = oldUser.getPassword();
        String phone = oldUser.getPhone();
       //设置  信息

        oldUser.setOpenId(newUser.getOpenId());
        oldUser.setUnionId(newUser.getUnionId());
        oldUser.setAvatar(newUser.getAvatar());
        oldUser.setNickname(newUser.getNickname());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setPhone(newUser.getPhone());
        newUser.setPhone("-1");
        userMapper.update(newUser);
        userMapper.update(oldUser);
        newUser.setOpenId(openid);
        newUser.setUnionId(unionId);
        newUser.setAvatar(avatar);
        newUser.setNickname(nickName);
        newUser.setPassword(pwd);
        newUser.setPhone(phone);
        newUser.setIsDeleted(true);//将原来的账号  置成删除态
        userMapper.update(newUser);
        //处理实名认证
        UserInfo oldUserInfo = userInfoMapper.findByUserId(oldUser.getId());
        UserInfo newUserInfo = userInfoMapper.findByUserId(newUser.getId());
        if (oldUserInfo!=null){
            if (newUserInfo!=null){//两个都认证过   相互交换
                oldUserInfo.setUserId(-1L);
                userInfoMapper.update(oldUserInfo);
                newUserInfo.setUserId(oldUser.getId());
                userInfoMapper.update(newUserInfo);
                oldUserInfo.setUserId(newUser.getId());
                userInfoMapper.update(oldUserInfo);
            }else{
                oldUserInfo.setRealname(oldUser.getNickname());
                oldUserInfo.setIdCardNumber(null);
                oldUserInfo.setImage1(null);
                oldUserInfo.setImage2(null);
                oldUserInfo.setConfirmStatus(ConfirmStatus.待审核);
                oldUserInfo.setBirthday(null);
                oldUserInfo.setAreaId(null);
                oldUserInfo.setRealFlag(0);
                userInfoMapper.update(oldUserInfo);
            }
        }else{
            if (newUserInfo!=null){
                newUserInfo.setUserId(oldUser.getId());
                userInfoMapper.update(newUserInfo);
            }

        }

    }


    /**
     * 新账号逻辑
     * @param accountNumber
     * @param user
     */
    @Override
    public void insertChangeAccountNumber(AccountNumber accountNumber, User user) {
        accountNumber.setFlage("1");
        accountNumber.setUpdateBy(-1L);
        accountNumber.setUpdateDate(new Date());
        accountNumberMapper.update(accountNumber);
        //处理用户
        User oldUser = userMapper.findByPhone(accountNumber.getOldPhone());
        if (oldUser==null){
            throw new BizException(BizCode.ERROR, "账号授权异常");
        }
        oldUser.setNickname(user.getNickname());
        oldUser.setPhone(user.getPhone());
        oldUser.setOpenId(user.getOpenId());
        oldUser.setUnionId(user.getUnionId());
        oldUser.setAvatar(user.getAvatar());
        oldUser.setLastloginTime(new Date());
        userMapper.update(oldUser);
        //处理实名认证
        UserInfo oldUserInfo = userInfoMapper.findByUserId(oldUser.getId());
        if(oldUserInfo!=null){
            oldUserInfo.setRealname(oldUser.getNickname());
            oldUserInfo.setIdCardNumber(null);
            oldUserInfo.setImage1(null);
            oldUserInfo.setImage2(null);
            oldUserInfo.setConfirmStatus(ConfirmStatus.待审核);
            oldUserInfo.setBirthday(null);
            oldUserInfo.setAreaId(null);
            oldUserInfo.setRealFlag(0);
            userInfoMapper.update(oldUserInfo);
        }
    }
}
