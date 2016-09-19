package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.sys.Message;
import com.zy.mapper.MessageMapper;
import com.zy.model.query.MessageQueryModel;
import com.zy.service.MessageService;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Message findOne(@NotNull Long id) {
        return messageMapper.findOne(id);
    }

    @Override
    public Message create(@NotNull Message message) {
        validate(message);
        List<Message> messages = messageMapper.findAll(MessageQueryModel.builder().tokenEQ(message.getToken()).build());
        if (messages.isEmpty()) {
            messageMapper.insert(message);
            return message;
        }
        return messages.get(0);
    }

    @Override
    public Page<Message> findPage(@NotNull MessageQueryModel messageQueryModel) {
        if (messageQueryModel.getPageNumber() == null)
            messageQueryModel.setPageNumber(0);
        if (messageQueryModel.getPageSize() == null)
            messageQueryModel.setPageSize(20);
        long total = messageMapper.count(messageQueryModel);
        List<Message> data = messageMapper.findAll(messageQueryModel);
        Page<Message> page = new Page<>();
        page.setPageNumber(messageQueryModel.getPageNumber());
        page.setPageSize(messageQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public long count(@NotNull MessageQueryModel messageQueryModel) {
        return messageMapper.count(messageQueryModel);
    }

    @Override
    public void readOne(@NotNull Long id) {
        Message message = new Message();
        message.setId(id);
        message.setIsRead(true);
        message.setReadTime(new Date());
        messageMapper.merge(message, "isRead", "readTime");
    }

    @Override
    public void readAll(@NotNull Long userId) {
        messageMapper.readAll(userId, new Date());
    }

    @Override
    public List<Message> findGroupByBatchNumber() {
        return messageMapper.findGroupByBatchNumber();
    }

    @Override
    public void deleteByBatchNumber(@NotBlank String batchNumber) {
        messageMapper.deleteByBatchNumber(batchNumber);
    }

    @Override
    public void delete(@NotNull Long id) {
        messageMapper.delete(id);
    }

    @Override
    public void send(@NotNull Message messageForCreate, @NotNull List<Long> userIds) {
        messageForCreate.setCreatedTime(new Date());
        messageForCreate.setIsRead(false);
        for (Long userId : userIds) {
            messageForCreate.setUserId(userId);
            messageMapper.insert(messageForCreate);
        }
    }

}
