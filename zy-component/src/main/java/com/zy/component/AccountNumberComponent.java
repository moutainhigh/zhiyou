package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.account.AccountNumber;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.AccountNumberAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by it001 on 2017-11-03.
 */
@Component
public class AccountNumberComponent {

    @Autowired
    private UserService userService;

    public AccountNumberAdminVo buildAdminVo(AccountNumber v) {
        AccountNumberAdminVo accountNumberAdminVo= new AccountNumberAdminVo();
        BeanUtils.copyProperties(v, accountNumberAdminVo);
        accountNumberAdminVo.setCreateDateLable(GcUtils.formatDate(v.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
       // accountNumberAdminVo.setUpdateDateLable(GcUtils.formatDate(v.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
        accountNumberAdminVo.setCreateName(userService.findRealName(v.getCreateBy()));
        return accountNumberAdminVo;

    }
}
