<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="index" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>微信支付</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/weixinApi.jsp"%>

<script>

  $(function() {
    $('#btnPay').click(function() {
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
          "appId": "${fuiouWeixinPayRes.sdk_appid}", //公众号名称，由商户传入
          "timeStamp": "${fuiouWeixinPayRes.sdk_timestamp}", //时间戳，自1970年以来的秒数
          "nonceStr": "${fuiouWeixinPayRes.sdk_noncestr}", //随机串
          "package": "${fuiouWeixinPayRes.sdk_package}",
          "signType": "${fuiouWeixinPayRes.sdk_signtype}", //微信签名方式:
          "paySign": "${fuiouWeixinPayRes.sdk_paysign}" //微信签名
        },
        function(res){
          // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
          if (res.err_msg == "get_brand_wcpay_request:ok") {
            alert("支付成功");
            window.location.href="${ctx}/u/";
          }
          if (res.err_msg == "get_brand_wcpay_request:cancel") {
            alert("交易取消");
          }
          if (res.err_msg == "get_brand_wcpay_request:fail") {
            alert("支付失败");
          }
        }
      );
    });
  });
</script>

</head>
<body>
  <article class="bg-white pt-30">
    <div class="line-text m-15">
      <h2><i class="icon icon-weixin"></i> 微信支付</h2>
    </div>
    <div class="list-group mb-0">
      <div class="list-item borderless">
        <div class="list-text">标题：</div>
        <div class="list-unit">${title}</div>
      </div>
      <div class="list-item borderless">
        <div class="list-text">支付金额：</div>
        <div class="list-unit fs-16 font-red bold">¥ ${amount}</div>
      </div>
    </div>
    <div class="form-btn">
      <button id="btnPay" class="btn green btn-block round-2">确认支付</button>
    </div>
  </article>
</body>
</html>
