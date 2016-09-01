package com.gc.component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.sys.Message;
import com.gc.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.gc.vo.MessageAdminVo;
import com.gc.vo.MessageDetailVo;
import com.gc.vo.MessageListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public List<MessageListVo> buildListVo(List<Message> messages) {
		return messages.stream().map(v -> {
			MessageListVo messageListVo = new MessageListVo();
			messageListVo.setId(v.getId());
			messageListVo.setTitle(v.getTitle());
			messageListVo.setMessageType(v.getMessageType());
			messageListVo.setCreatedTimeLabel(GcUtils.getDateLabel(v.getCreatedTime(), "yyyy-MM-dd"));
			messageListVo.setIsRead(v.getIsRead());
			return messageListVo;
		}).collect(Collectors.toList());
	}
	
	public MessageDetailVo buildDetailVo(Message message){
		MessageDetailVo messageDetailVo = new MessageDetailVo();
		messageDetailVo.setId(message.getId());
		messageDetailVo.setTitle(message.getTitle());
		messageDetailVo.setContent(message.getContent());
		messageDetailVo.setCreatedTimeLabel(GcUtils.getDateLabel(message.getCreatedTime(), "yyyy-MM-dd"));
		messageDetailVo.setIsRead(message.getIsRead());
		return messageDetailVo;
	}

	public MessageAdminVo buildAdminVo(Message message) {
		MessageAdminVo messageAdminVo = new MessageAdminVo();
		BeanUtils.copyProperties(message, messageAdminVo);
		User user = cacheComponent.getUser(message.getUserId());
		messageAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		return messageAdminVo;
	}

}
