package com.zy.common.util.weiXinUtils;

import net.sf.json.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by it001 on 2017-11-01.
 */
public class SenActiviteSucessMsg {

    private  static final Logger logger = LoggerFactory.getLogger(SenActiviteSucessMsg.class);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void send_template_message(String name, String openId, Token token) {
        String access_token =token.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
        WxTemplate temp = new WxTemplate();
        temp.setUrl("http://agentsystem.zhi-you.net/activity/45");
        temp.setTouser(openId);
        temp.setTopcolor("#000000");
        temp.setTemplate_id("F_EzG5fjwtSv67VcDuF2W2dtcitY_LkUcd3SrHZoRgk");
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue(name+"您好！恭喜您成功报名《幻化--智优生物2017品牌暨新品绽放盛典》！");
        m.put("first", first);
        TemplateData keyword1 = new TemplateData();
        keyword1.setValue("2017年11月12日 13:30");
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#000000");
        keyword2.setValue("杭州萧山区杭州市萧山区水博大道8号（宝盛水博园大酒店）");
        m.put("keyword2", keyword2);
        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("详细请登录官方系统。");
        m.put("remark", remark);
        temp.setData(m);

        //开始发请求
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String jsonString = JSONObject.fromObject(temp).toString();
            StringEntity entity = new StringEntity(jsonString, ContentType.create("application/xml", "utf-8"));
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(9000).
                    setConnectTimeout(9000).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String result= EntityUtils.toString(response.getEntity());
            System.out.print(result);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("error:", e);
        }
    }
}
