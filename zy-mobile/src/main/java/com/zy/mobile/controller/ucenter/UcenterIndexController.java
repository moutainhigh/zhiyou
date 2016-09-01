package com.zy.mobile.controller.ucenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.entity.fnc.Account;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.MessageQueryModel;
import com.zy.service.AccountService;
import com.zy.service.MessageService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;

@RequestMapping("/u")
@Controller
public class UcenterIndexController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping
	public String index(Principal principal, Model model) {
		Long userId = principal.getUserId();
		
		User user = userService.findOne(userId);
		model.addAttribute("user", user);
		
		List<Account> accounts = accountService.findByUserId(userId);
		for (Account account : accounts) {
			switch (account.getCurrencyType()) {
			case 现金:
				model.addAttribute("money", account.getAmount());
				break;
			case 金币:
				model.addAttribute("coin", account.getAmount());			
				break;
			case 积分:
				model.addAttribute("point", account.getAmount().setScale(0));
				break;
			default:
				break;
			}
		}
		
		model.addAttribute("userAvatarSmall", GcUtils.getThumbnail(user.getAvatar(), 240, 240));
		
		MessageQueryModel messageQueryModel = new MessageQueryModel();
		messageQueryModel.setUserIdEQ(userId);
		messageQueryModel.setIsReadEQ(false);
		model.addAttribute("unreadMessageCount", messageService.count(messageQueryModel));
		
		return "ucenter/index";
	}
	
}
