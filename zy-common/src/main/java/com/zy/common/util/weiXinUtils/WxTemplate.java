package com.zy.common.util.weiXinUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by it001 on 2017/7/25.
 */
@Getter
@Setter
public class WxTemplate {

    /**
     * 模板消息id
     */
    private String template_id;
    /**
     * 用户openId
     */
    private String touser;
    /**
     * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
     */
    private String url;
    /**
     * 标题颜色
     */
    private String topcolor;
    /**
     * 详细内容
     */
    private Map<String,TemplateData> data;
}
