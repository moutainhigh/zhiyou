package com.zy.common.util.weiXinUtils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
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
 * Created by it001 on 2017/7/25.
 */
public class SendPaySuccessMsg {

   private  static final  Logger logger = LoggerFactory.getLogger(SendPaySuccessMsg.class);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void send_template_message(String sn,String type, BigDecimal money, String openId, Token token) {

        String access_token =token.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
        WxTemplate temp = new WxTemplate();
        temp.setUrl("http://192.168.1.222:8088/u/money?currencyType=0");
        temp.setTouser(openId);
        temp.setTopcolor("#000000");
        temp.setTemplate_id("7vodPLEmJFnI3SNmbItVTxVtkHutdXBBAHCKfzH4mwE");
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue("恭喜您充值成功！");
        m.put("first", first);
        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#000000");
        keyword1.setValue("智优生物U币充值");
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#000000");
        keyword2.setValue(sn);
        m.put("keyword2", keyword2);
        TemplateData keyword3 = new TemplateData();
        keyword3.setColor("#000000");
        keyword3.setValue(money+"元");
        m.put("keyword3", keyword3);
        TemplateData keyword4 = new TemplateData();
        keyword4.setColor("#000000");
        keyword4.setValue(dateFormat.format(new Date()));
        m.put("keyword4", keyword4);
        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("点击详情看看U币去哪了");
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
