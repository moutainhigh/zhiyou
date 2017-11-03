package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.account.AccountNumber;
import com.zy.entity.usr.User;
import com.zy.model.query.AccountNumberQueryModel;

import java.util.List;

/**
 * Created by it001 on 2017-11-03.
 */
public interface AccountNumberService {

    List<AccountNumber> findAll(AccountNumberQueryModel accountNumberQueryModel);

    Page<AccountNumber> findPage(AccountNumberQueryModel accountNumberQueryModel);

    void update(AccountNumber accountNumber);

    AccountNumber findOne(Long id);

    AccountNumber findOneByPhone(String phone,String type);

    void   insert(AccountNumber accountNumber);

    void insertAccountNumber(String newPhone, User oldUser, Long id);

    void insertChangeAccountNumber(User oldUser, User newUser, Long id);

    void insertChangeAccountNumber(AccountNumber accountNumber, User user);
}
