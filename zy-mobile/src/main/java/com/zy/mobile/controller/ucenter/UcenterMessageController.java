package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.UnauthorizedException;
import com.zy.component.MessageComponent;
import com.zy.entity.sys.Message;
import com.zy.model.Principal;
import com.zy.model.query.MessageQueryModel;
import com.zy.service.MessageService;
import io.gd.generator.api.query.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/u/message")
@Controller
public class UcenterMessageController {

	Logger logger = LoggerFactory.getLogger(UcenterMessageController.class);
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageComponent messageComponent;
	
	@RequestMapping
	public String list(Principal principal, Model model, Integer pageNumber) {
		
		MessageQueryModel messageQueryModel = new MessageQueryModel();
		messageQueryModel.setUserIdEQ(principal.getUserId());
		messageQueryModel.setPageNumber((pageNumber == null || pageNumber < 0) ? 0 : pageNumber);
		messageQueryModel.setIsReadEQ(false);
		messageQueryModel.setPageSize(20);
		messageQueryModel.setOrderBy("createdTime");
		messageQueryModel.setDirection(Direction.DESC);
		model.addAttribute("unreadMessageVos", messageComponent.buildListVo(messageService.findPage(messageQueryModel).getData()));
		
		messageQueryModel.setIsReadEQ(true);
		model.addAttribute("readMessageVos", messageComponent.buildListVo(messageService.findPage(messageQueryModel).getData()));
		return "ucenter/message";
	}
	
	@RequestMapping("/{id}")
	public String detail(Principal principal, @PathVariable Long id, Model model) {
		Message message = messageService.findOne(id);
		if(message != null) {
			if(!message.getUserId().equals(principal.getUserId())) {
				throw new UnauthorizedException();
			}
			if(message.getIsRead() == null || !message.getIsRead()){
				messageService.readOne(id);
			}
		}
		model.addAttribute("message", messageComponent.buildDetailVo(message));
		return "ucenter/messageDetail";
	}
	
	
	@RequestMapping(value = "/readAll", method = RequestMethod.POST)
	public String readAll(Principal principal) {
		messageService.readAll(principal.getUserId());
		return "redirect:/u/message";
	}
}
