package com.zy.admin.controller.account;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.AccountNumberComponent;
import com.zy.entity.account.AccountNumber;
import com.zy.entity.usr.User;
import com.zy.model.query.AccountNumberQueryModel;
import com.zy.service.AccountNumberService;
import com.zy.service.UserService;
import com.zy.vo.AccountNumberAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-11-03.
 */

@RequestMapping("/accountnumber")
@Controller
public class AccountNumberController {

    @Autowired
    private AccountNumberService accountNumberService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountNumberComponent accountNumberComponent;

    @RequiresPermissions("accountNumber:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "account/accountNumberList";
    }

    @RequiresPermissions("accountNumber:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<AccountNumberAdminVo> list(AccountNumberQueryModel accountNumberQueryModel){
        accountNumberQueryModel.setFlageIN(new String[]{"0","1"});
      Page<AccountNumber> page = accountNumberService.findPage(accountNumberQueryModel);
        List<AccountNumberAdminVo> list = page.getData().stream().map(v -> {
            return accountNumberComponent.buildAdminVo(v);
        }).collect(Collectors.toList());
        return new Grid<AccountNumberAdminVo>(PageBuilder.copyAndConvert(page, list));
    }


    /**
     * 跳转到 添加页面
     * @return
     */
    @RequestMapping(value = "toCreate")
    public String toCreate(){
     return "account/accountNumberEdit";
    }

    /**
     * 准备更新数据
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "toupdate")
    public String toupdate(Long id, Model model){
        AccountNumber accountNumber = accountNumberService.findOne(id);
        model.addAttribute("accountNumber",accountNumber);
        return "account/accountNumberEdit";
    }


    @RequestMapping(value = "delete")
    @ResponseBody
    public Result<?> delete(Long id){
        try {
            AccountNumber accountNumber = accountNumberService.findOne(id);
            accountNumber.setFlage("3");
            accountNumberService.update(accountNumber);
            return  ResultBuilder.ok("操作成功");
        }catch (Exception e){
            return ResultBuilder.error(e.getMessage());
        }
    }

    /**
     * 更新数据
     * @param id
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> update(Long id,String phone,AdminPrincipal adminPrincipal){
        try {
            AccountNumber accountNumber = accountNumberService.findOne(id);
            User user = userService.findByPhone(phone);
            if (user == null) {
                return ResultBuilder.error("原账号不存在");
            }
            accountNumber.setNewPhone(phone);
            accountNumber.setUpdateBy(adminPrincipal.getUserId());
            accountNumber.setUpdateDate(new Date());
            accountNumberService.update(accountNumber);
        }catch (Exception e){
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.ok("操作成功");
    }



    /**
     * 插入数据
     * @return
     */
    @RequestMapping(value = "/addAccountNumber" ,method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addAccountNumber(String newPhone, String oldPhone, AdminPrincipal adminPrincipal) {
        try {
            User oldUser = userService.findByPhone(oldPhone);
            if (oldUser == null) {
                return ResultBuilder.error("原账号不存在");
            }
            User newUser = userService.findByPhone(newPhone);
            if (newUser == null) {//新用户不存在 则插入一笔在 注册时用
                accountNumberService.insertAccountNumber(newPhone, oldUser, adminPrincipal.getUserId());
            } else { //存在则交换两个用户的用户信息
                accountNumberService.insertChangeAccountNumber(oldUser,newUser,adminPrincipal.getUserId());
            }
            return ResultBuilder.ok("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
    }

}
