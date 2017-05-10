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

  wx.config({
	debug: false,
	appId: '${weixinJsModel.appId}',
	timestamp: ${weixinJsModel.timestamp},
	nonceStr: '${weixinJsModel.nonceStr}',
	signature: '${weixinJsModel.signature}',
	jsApiList: ['wxChoosePay']
  });
  $(function() {
    $('#btnPay').click(function() {
      wx.chooseWXPay({
    	 "timestamp": "${weixinPayModel.timeStamp}",
    	 "nonceStr":  "${weixinPayModel.nonceStr}",
    	 "package":	"${weixinPayModel.pkg}",
    	 "signType":	"${weixinPayModel.signType}",
    	 "paySign":	"${weixinPayModel.paySign}",
    	 "success":	function(res) {
    		 //printMap(res);
    		 alert('支付成功');
    		 window.location.href="${ctx}/u";
    	 },
    	 "fail": function(res) {
    		 alert('支付失败, 原因' + JSON.stringify(res));
    	 }
      });
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
