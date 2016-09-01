<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="1" />
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

<title>我的订单</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />
<script>
	$(function() {

	});
</script>

</head>
<body class="header-fixed footer-fixed">
  <header class="header">
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <nav class="header-nav round-4">
      <a href="${ctx}/u/order?orderStatus=0"<c:if test="${orderStatus == '待支付'}"> class="current"</c:if>>待支付</a>
      <a href="${ctx}/u/order?orderStatus=1"<c:if test="${orderStatus == '待发货'}"> class="current"</c:if>>待发货</a>
      <a href="${ctx}/u/order"<c:if test="${empty orderStatus}"> class="current"</c:if>>全部订单</a>
    </nav>
  </header>
  
  <nav class="tab-nav">
    <a href="${ctx}/u/order?orderStatus=0"<c:if test="${orderStatus == '待支付'}"> class="current"</c:if>>待付款 (2)</a>
    <a href="${ctx}/u/order?orderStatus=1"<c:if test="${orderStatus == '待发货'}"> class="current"</c:if>>待发货 (1)</a>
    <a href="${ctx}/u/order"<c:if test="${empty orderStatus}"> class="current"</c:if>>全部订单</a>
  </nav>
  
  <article class="order-list">
    <c:if test="${empty page.data}">
    <div class="empty-tip">
      <i class="fa fa-file-o"></i>
      <span>空空如也!</span>
    </div>
    </c:if>
    
    <a class="order mt-15 bd-t bd-b" href="${ctx}/u/order/1">
      <div class="order-sn pl-15 pr-15 font-777 fs-12">订单编号：${order.sn}</div>
      <label class="order-status label orange">已支付</label>
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@80w_80h_1e_1c.jpg">
          <div class="product-title">优检一生检测试剂 300 盒装</div>
          <div class="product-price abs-rt text-right">
            <div class="lh-24 fs-12">¥ 32400.00</div>
            <div class="lh-24 fs-12 font-gray">x1</div>
          </div>
        </div>
      </div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1 font-999 fs-12"><span>下单时间：2016-08-21 13:45:04</span></div>
          <div class="flex-1 font-777 text-right">总金额：<span class="fs-14 font-orange bold">¥ 32400.00</span></div>
        </div>
      </div>
    </a>
    
    <a class="order mt-15 bd-t bd-b" href="${ctx}/u/order/1">
      <div class="order-sn pl-15 pr-15 font-777 fs-12">订单编号：P20160823181550020178-3124</div>
      <label class="order-status label blue">已发货</label>
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@80w_80h_1e_1c.jpg">
          <div class="product-title">优检一生检测试剂 300 盒装</div>
          <div class="product-price abs-rt text-right">
            <div class="lh-24 fs-12">¥ 32400.00</div>
            <div class="lh-24 fs-12 font-gray">x1</div>
          </div>
        </div>
      </div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1"><span class="font-999 fs-12">下单时间：2016-08-21 13:45:04</span></div>
          <div class="flex-1 text-right">总金额：<span class="fs-14 font-orange bold">¥ 32400.00</span></div>
        </div>
      </div>
    </a>
    
    <a class="order mt-15 bd-t bd-b" href="${ctx}/u/order/1">
      <div class="order-sn pl-15 pr-15 font-777 fs-12">订单编号：P20160823181550020178-3124</div>
      <label class="order-status label green">已完成</label>
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@80w_80h_1e_1c.jpg">
          <div class="product-title">优检一生检测试剂 300 盒装</div>
          <div class="product-price abs-rt text-right">
            <div class="lh-24 fs-12">¥ 32400.00</div>
            <div class="lh-24 fs-12 font-gray">x1</div>
          </div>
        </div>
      </div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1"><span class="font-999 fs-12">下单时间：2016-08-21 13:45:04</span></div>
          <div class="flex-1 text-right">总金额：<span class="fs-14 font-orange bold">¥ 32400.00</span></div>
        </div>
      </div>
    </a>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
