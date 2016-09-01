package com.zy.admin.controller.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.entity.sys.Message;
import com.zy.entity.sys.Message.MessageType;
import com.zy.entity.usr.User;
import com.zy.model.query.MessageQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.MessageService;
import com.zy.service.UserService;

@RequestMapping("/message")
@Controller
public class MessageController {

	Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	private static final int MAX_SEND_COUNT = 100;
	
	@RequiresPermissions("message:view")
	@RequestMapping
	public String list(Model model) {
		List<MessageType> list = new ArrayList<>();
		for(MessageType messageType : MessageType.values()) {
			list.add(messageType);
		}
		model.addAttribute("messageTypes", list);
		return "sys/messageList";
	}
	
	@RequiresPermissions("message:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Message> list(MessageQueryModel messageQueryModel) {
		messageQueryModel.setOrderBy("createdTime");
		Page<Message> page = messageService.findPage(messageQueryModel);
		List<Message> messages = page.getData();
		if(!messages.isEmpty()) {
			messages.stream().forEach(v->{
				v.setContent(v.getContent());
			});
		}
		return new Grid<Message>(page);
	}
	
	@RequiresPermissions("message:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("batchNumber", UUID.randomUUID().toString());
		model.addAttribute("maxSendCount", MAX_SEND_COUNT);
		return "sys/messageCreate";
	}
	
	@RequiresPermissions("message:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@RequestParam String batchNumber, @RequestParam String content, UserQueryModel userQueryModel, RedirectAttributes redirectAttributes) {
		List<User> users = userService.findAll(userQueryModel);
		if(users.isEmpty()){
			redirectAttributes.addFlashAttribute("content", content);
			redirectAttributes.addFlashAttribute(ResultBuilder.error("消息接收人为空"));
			return "redirect:/message/create";
		}
		if(users.size() > MAX_SEND_COUNT) {
			redirectAttributes.addFlashAttribute("content", content);
			redirectAttributes.addFlashAttribute(ResultBuilder.error("发送条数超过限制，请重新筛选用户，最大发送条数：" + MAX_SEND_COUNT));
			return "redirect:/message/create";
		}
		List<Long> userIds = users.stream().map(v -> v.getId()).collect(Collectors.toList());
		Message messageForCreate = new Message();
		messageForCreate.setBatchNumber(batchNumber);
		messageForCreate.setContent(content);
		messageForCreate.setMessageType(MessageType.任务通知);
		messageService.send(messageForCreate, userIds);
		return "redirect:/message";
	}
	
	@RequestMapping(value="userList", method = RequestMethod.POST)
	@ResponseBody
	public Result<Page<User>> userList(UserQueryModel userQueryModel) {
		Page<User> page = userService.findPage(userQueryModel);
		return ResultBuilder.result(page);
	}
	
	@RequiresPermissions("message:edit")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		messageService.delete(id);
		return "redirect:/message";
	}
	
	@RequiresPermissions("message:edit")
	@RequestMapping(value = "/deleteByBatchNumber", method = RequestMethod.GET)
	public String delete(Model model) {
		List<Message> messages = messageService.findGroupByBatchNumber();
		if(!messages.isEmpty()) {
			messages.stream().forEach(v->{
				v.setContent(v.getContent());
			});
		}
		model.addAttribute("messages", messages);
		return "sys/messageDelete";
	}
	
	@RequiresPermissions("message:edit")
	@RequestMapping(value = "/deleteByBatchNumber", method = RequestMethod.POST)
	public String delete(@RequestParam String batchNumber, RedirectAttributes redirectAttributes) {
		MessageQueryModel messageQueryModel = new MessageQueryModel();
		messageQueryModel.setBatchNumberEQ(batchNumber);
		Page<Message> page = messageService.findPage(messageQueryModel);
		if(page.getData().isEmpty()){
			redirectAttributes.addFlashAttribute(ResultBuilder.error("批次号【" +batchNumber + "】不存在"));
			return "redirect:/message/deleteByBatchNumber";
		}
		messageService.deleteByBatchNumber(batchNumber);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("批次号【" +batchNumber + "】删除成功"));
		return "redirect:/message";
	}
}
