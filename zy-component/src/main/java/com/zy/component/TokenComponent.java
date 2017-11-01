package com.zy.component;

import com.zy.common.util.weiXinUtils.Token;
import net.sf.json.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by it001 on 2017/7/25.
 */
public class TokenComponent {
    private  Token token;
    public Token getToken() {
        if (token==null){
            this.refresh();
        }
        return token;
    }

    private  Logger logger = LoggerFactory.getLogger(TokenComponent.class);
    // 凭证获取（GET）
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public final static  String appid ="wx8d8b06aa56074960";

    public final static  String appsecret ="e2a29086a0bd4c4083038bf61d6a6614";

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    //刷新 Token
    public void refresh(){
        logger.info("Token refresh begin...");
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(9000).
                    setConnectTimeout(9000).build();
            HttpGet httpGet = new HttpGet(requestUrl);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String result= EntityUtils.toString(response.getEntity());
            System.out.println(result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (jsonObject!=null){
                Token token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInt("expires_in"));
                this.token =token;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("error:", e);
        }
        logger.info("Token refresh end...");
    }

}
