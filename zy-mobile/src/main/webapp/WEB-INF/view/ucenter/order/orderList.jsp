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

<title>我的订单</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />
</head>
<body class="header-fixed footer-fixed">
  <header class="header">
    <h1>${inOut == 'out' ? '出货' : '进货'}订单</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <nav class="tab-nav">
    <a href="${ctx}/u/order/${inOut}"<c:if test="${empty orderStatus}"> class="current"</c:if>>全部订单</a>
    <a href="${ctx}/u/order/${inOut}?orderStatus=0"<c:if test="${orderStatus == '待支付'}"> class="current"</c:if>>待付款 (${waitForPayConut})</a>
    <a href="${ctx}/u/order/${inOut}?orderStatus=1"<c:if test="${orderStatus == '已支付'}"> class="current"</c:if>>待发货 (${waitForDeliverConut})</a>
    <a href="${ctx}/u/order/${inOut}?orderStatus=2"<c:if test="${orderStatus == '已发货'}"> class="current"</c:if>>待收货 (${waitForReceiveConut})</a>
  </nav>
  
  <article class="order-list">
    <c:if test="${empty page.data}">
    <div class="empty-tip">
      <i class="fa fa-file-o"></i>
      <span>空空如也!</span>
    </div>
    </c:if>
    
    <c:forEach items="${page.data}" var="order">
    <a class="order mt-15 bd-t bd-b" href="${ctx}/u/order/${order.id}">
      <div class="order-sn pl-15 pr-15 font-777 fs-12">订单编号：${order.sn}</div>
      <label class="order-status label<c:if test="${order.orderStatus == '待支付'}"> red</c:if>
      <c:if test="${order.orderStatus == '已支付'}"> orange</c:if>
      <c:if test="${order.orderStatus == '已发货'}"> yellow</c:if>
      <c:if test="${order.orderStatus == '已完成'}"> green</c:if>
      <c:if test="${order.orderStatus == '已退款'}"> blue</c:if>
      <c:if test="${order.orderStatus == '已取消'}"> gray</c:if>">${order.orderStatus}</label>
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="${order.orderItems[0].imageThumbnail}">
          <div class="product-title">${order.orderItems[0].title}</div>
          <div class="product-price abs-rt text-right">
            <div class="lh-24 fs-12">¥ ${order.orderItems[0].price}</div>
            <div class="lh-24 fs-12 font-gray">x${order.orderItems[0].quantity}</div>
          </div>
        </div>
      </div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1 font-999 fs-12"><span>下单时间：${order.createdTimeLabel}</span></div>
          <div class="flex-1 font-777 text-right">总金额：<span class="fs-14 font-orange bold">¥ ${order.amount}</span></div>
        </div>
      </div>
    </a>
    </c:forEach>
    
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
