package com.zy.job.extend;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.zy.common.util.Identities;

import java.io.InputStream;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;


public class AliyunOssSupport {
    private final String DEFAULT_ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private final OSSClient ossClient;
    private String group;

    public AliyunOssSupport(String endpoint, String accessKeyId, String accessKeySecret, String group) {
        if (endpoint == null) {
            endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        }
        this.group = group;
        this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }


    public String putPublicObject(String bucketName, String path, InputStream inputStream, long contentLength, String contentType, String domain) {
        validate(domain, NOT_BLANK.and(v -> v.startsWith("http") ), "domain " + domain + " is invalid");
        validate(path, NOT_BLANK.and(v -> !v.startsWith("/")).and((v) -> v.matches("[/\\w]+")), "path " + path + " is invalid");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentLength);
        objectMetadata.setContentType(contentType);
        if (!path.endsWith("/")) {
            path = path + "/";
        }

        String key = path + Identities.uuid();
        if (this.group != null && !"".equals(this.group)) {
            key = path + this.group + "/" + Identities.uuid();
        }

        this.ossClient.putObject(bucketName, key, inputStream, objectMetadata);
        if (!domain.endsWith("/")) {
            domain = domain + "/";
        }

        return domain + key;
    }
}
