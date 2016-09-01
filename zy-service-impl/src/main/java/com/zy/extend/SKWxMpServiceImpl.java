package com.gc.extend;

import com.zy.common.util.Identities;
import com.thoughtworks.xstream.XStream;

import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.Utf8ResponseHandler;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxRedpackResult;

import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.slf4j.helpers.MessageFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.KeyStore;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.String.valueOf;
import static me.chanjar.weixin.common.util.crypto.WxCryptUtil.createSign;

public class SKWxMpServiceImpl extends WxMpServiceImpl implements SKWxMpService {

    protected String ip;

    public SKWxMpServiceImpl() throws Exception {
        ip = InetAddress.getLocalHost().getHostAddress();
    }

    String transferUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


    @Override
    public WxRedpackResult transfer(String sn, String userOpenId, BigDecimal amount, String title) throws WxErrorException {
        SortedMap<String, String> packageParams = getStringStringSortedMap(sn, userOpenId, amount.multiply(BigDecimal.valueOf(100)).intValue(), title);
        StringBuilder request = new StringBuilder("<xml>");
        for (Map.Entry<String, String> para : packageParams.entrySet()) {
            request.append(String.format("<%s>%s</%s>", para.getKey(), para.getValue(), para.getKey()));
        }
        request.append("</xml>");

        HttpPost httpPost = new HttpPost(transferUrl);
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(config);
        }

        StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
        httpPost.setEntity(entity);
        try {
            CloseableHttpResponse response = getHttpclient().execute(httpPost);
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            XStream xstream = XStreamInitializer.getInstance();
            xstream.processAnnotations(WxRedpackResult.class);
            WxRedpackResult wxMpRedpackResult = (WxRedpackResult) xstream.fromXML(responseContent);
            return wxMpRedpackResult;
        } catch (IOException e) {
            log.error(MessageFormatter.format("The exception was happened when transfer '{}'.", request.toString()).getMessage(), e);
            WxError error = new WxError();
            error.setErrorCode(-1);
            throw new WxErrorException(error);
        }
    }

    private SortedMap<String, String> getStringStringSortedMap(String sn, String userOpenId, int amount, String desc) {
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("mch_appid", wxMpConfigStorage.getAppId());
        packageParams.put("mchid", wxMpConfigStorage.getPartnerId());
        packageParams.put("check_name", "NO_CHECK");
        packageParams.put("nonce_str", Identities.uuid2());
        packageParams.put("spbill_create_ip", ip);
        packageParams.put("openid", userOpenId);
        packageParams.put("partner_trade_no", sn);
        packageParams.put("amount", valueOf(amount));
        packageParams.put("desc", desc);


        String sign = createSign(packageParams, wxMpConfigStorage.getPartnerKey());
        packageParams.put("sign", sign);
        return packageParams;
    }

    public static class SkWxMpInMemoryConfigStorage extends WxMpInMemoryConfigStorage {

        public SkWxMpInMemoryConfigStorage(String certPath, String password, boolean isDev) throws Exception {
            if (isDev) return;
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream instream = new FileInputStream(new File(certPath))) {
                keyStore.load(instream, password.toCharArray());
            }
            // Trust own CA and all self-signed certs
            this.setSSLContext(SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build());
        }
    }


}