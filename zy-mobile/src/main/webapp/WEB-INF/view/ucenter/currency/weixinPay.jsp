<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>微信支付</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
$(function() {
  wx.config({
    debug: false,
    appId: '${weixinJsModel.appId}',
    timestamp: ${weixinJsModel.timestamp},
    nonceStr: '${weixinJsModel.nonceStr}',
    signature: '${weixinJsModel.signature}',
    jsApiList: ['wxChoosePay']
  });
  
  $('#btnPay').click(function() {
    wx.chooseWXPay({
      "timestamp": "${weixinPayModel.timeStamp}",
      "nonceStr":  "${weixinPayModel.nonceStr}",
      "package": "${weixinPayModel.pkg}",
      "signType":  "${weixinPayModel.signType}",
      "paySign": "${weixinPayModel.paySign}",
      "success": function(res) {
        //printMap(res);
        messageAlert('支付成功');
        window.location.href="${ctx}/u/coin";
      },
      fail: function(res) {
        messageAlert('支付失败, 原因' + JSON.stringify(res));
      }
    });
  });
});
</script>
</head>
<style>
.order-title {
  margin: 25px 15px 35px; text-align: center; overflow: hidden;
}
.order-title h2 {
  position: relative; display: inline-block; font-size: 21px; line-height: 1;
}
.order-title h2:before, .order-title h2:after {
  position: absolute; top: 50%; width: 300%; border-top: 1px solid #d9d9d9; content: "";
}
.order-title h2:before {
  left: -310%;
}
.order-title h2:after {
  right: -310%;
}
</style>
<body class="header-fixed">

  <header class="header">
    <h1>确认支付</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <section class="clearfix">

    <div class="order-title">
      <h2>
        <i class="icon icon-weixin"></i>订单详情
      </h2>
    </div>
    <p class="clearfix pl-15 pr-15">
      <span class="left font-333">商品标题：</span> <span class="right font-black">${deposit.title}</span>
    </p>
    <p class="clearfix pl-15 pr-15 mt-15">
      <span class="left font-333">收款商户：</span> <span class="right font-black">微信分销</span>
    </p>
    <p class="clearfix pl-15 pr-15 mt-15">
      <span class="left font-333">支付金额：</span> <span class="right font-red">￥${deposit.totalAmount}</span>
    </p>
    <div class="form-btn mt-30">
      <a id="btnPay" class="btn btn-block green round-2" href="javascript:;">确认支付</a>
    </div>

  </section>

</body>
</html>
