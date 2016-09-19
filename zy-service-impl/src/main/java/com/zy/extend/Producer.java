package com.zy.extend;

import com.zy.Config;
import com.zy.entity.sys.Notify;
import com.zy.mapper.NotifyMapper;
import com.zy.model.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * Created by freeman on 16/7/15.
 */
@Component
public class Producer {

    @Resource
    private NotifyMapper notifyMapper;

    @Resource
    private Config config;

    public void send(String topic, Long refId) {
        Notify notify = new Notify();
        notify.setRefId(refId);
        notify.setTopic(topic);
        String settingNotifyVersion = Constants.SETTING_NOTIFY_VERSION;
        if (config.isDev()) {
            settingNotifyVersion += "-dev";
        }
        notify.setVersion(settingNotifyVersion);
        notify.setToken(UUID.randomUUID().toString());
        notify.setCreatedTime(new Date());
        notify.setSentTime(null);
        notify.setIsSent(false);
        this.notifyMapper.insert(notify);
    }


}
