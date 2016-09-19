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
<script>
  $(function() {
    var itemWidth = $(".scroll-nav-list li.current").width();
    var listWidth = $(".scroll-nav-list").width();
    var wrapWidth = $(".scroll-nav-wrap").width();
    $(".current-line").width(itemWidth);
    
    $(".scroll-nav-list li").on('click', function() {
      itemWidth = $(this).width();
      $(".current-line").stop(true);
      $(".current-line").animate({
        left : $(this).position().left
      }, 300);
      $(".current-line").animate({
        width : itemWidth
      }, 100);
      $(this).addClass("current").siblings().removeClass("current");
      var leftHalf = ($(".scroll-nav").width() - itemWidth) / 2;
      var posLeft = parseInt($(this).position().left);
      var scrollLeft;
      if (posLeft <= leftHalf) {
        scrollLeft = 0;
      } else if (leftHalf - posLeft <= wrapWidth - listWidth) {
        scrollLeft = wrapWidth - listWidth;
      } else {
        scrollLeft = leftHalf - posLeft;
      }
      $(".scroll-nav-list").animate({
        "left" : scrollLeft
      }, 300);
    });
    
    $(".scroll-nav-list li.current").click();
    
    $(".scroll-nav-list").on('touchstart', function(e) {
      var touch1 = e.originalEvent.targetTouches[0];
      x1 = touch1.pageX;
      y1 = touch1.pageY;
      cssLeft = parseInt($(this).css("left"));
    });
    
    $(".scroll-nav-list").on('touchmove', function(e) {
      var touch2 = e.originalEvent.targetTouches[0];
      var x2 = touch2.pageX;
      var y2 = touch2.pageY;
      if (cssLeft + x2 - x1 >= 0) {
        $(this).css("left", 0);
      } else if (cssLeft + x2 - x1 <= wrapWidth - listWidth) {
        $(this).css("left", wrapWidth - listWidth);
      } else {
        $(this).css("left", cssLeft + x2 - x1);
      }
      if (Math.abs(y2 - y1) > 0) {
        e.preventDefault();
      }
    });
  });
</script>
</head>
<body>
<body class="header-fixed footer-fixed">
  <header class="header">
    <h1>${inOut == 'out' ? '出货' : '进货'}订单</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <div class="scroll-nav">
    <div class="scroll-nav-wrap">
      <ul class="scroll-nav-list">
        <li<c:if test="${empty orderStatus}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}">全部订单 (${orderCount})</a></li>
        <li<c:if test="${orderStatus == '待支付'}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}?orderStatus=0">待支付 (${orderCount0})</a></li>
        <li<c:if test="${orderStatus == '已支付'}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}?orderStatus=1">已支付 (${orderCount1})</a></li>
        <li<c:if test="${orderStatus == '已发货'}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}?orderStatus=2">已发货 (${orderCount2})</a></li>
        <li<c:if test="${orderStatus == '已完成'}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}?orderStatus=3">已完成 (${orderCount3})</a></li>
        <li<c:if test="${orderStatus == '已退款'}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}?orderStatus=4">已退款 (${orderCount4})</a></li>
        <li<c:if test="${orderStatus == '已取消'}"> class="current"</c:if>><a href="${ctx}/u/order/${inOut}?orderStatus=5">已取消 (${orderCount5})</a></li>
        <li class="current-line"></li>
      </ul>
    </div>
  </div>
  
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
