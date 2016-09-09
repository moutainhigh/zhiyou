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
<meta name="keywords" content="微信分销" />
<meta name="description" content="订单详情 " />

<link href="${stccdn}/css/ucenter/order.css" rel="stylesheet"> 
<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>

<title>确认订单信息</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stccdn}/js/area.js"></script>

<script>
  $(function() {
    
  });
</script>
</head>
<body class="order-detail">
  <header class="header">
    <h1>订单详情</h1>
    <a href="${ctx}/u/order/${inOut}" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article>
    <%-- 待支付, 已支付, 已发货, 已完成, 已退款, 已取消 --%>
    <c:if test="${order.orderStatus == '待支付'}">
    <div class="note note-danger mb-0">
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：待支付</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已支付'}">
    <div class="note note-danger mb-0">
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：已支付，请耐心等待发货</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已发货'}">
    <div class="note note-success mb-0">
      <p><i class="fa fa-truck fs-16"></i> 订单状态：已发货</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已完成'}">
    <div class="note note-success mb-0">
      <p><i class="fa fa-check fs-16"></i> 订单状态：已完成</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已退款'}">
    <div class="note note-success mb-0">
      <p><i class="fa fa-reply fs-16"></i> 订单状态：已退款</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已取消'}">
    <div class="note note-success mb-0">
      <p><i class="fa fa-close fs-16"></i> 订单状态：已取消</p>
    </div>
    </c:if>
    
    <c:if test="${order.orderStatus == '待支付'}">
    <form id="orderForm" class="valid-form" action="${ctx}/u/pay" method="post">
    </c:if>
    <c:if test="${order.orderStatus == '已发货'}">
    <form id="orderForm" class="valid-form" action="${ctx}/order/confirmDelivery method="post">
    </c:if>
    <c:if test="${order.orderStatus == '已完成'}">
    <form id="orderForm" class="valid-form" action="${ctx}/order/create" method="post">
    <input type="hidden" name="productId" value="${order.orderItem[0].productId}">
    <input type="hidden" name="quantity" value="${order.orderItem[0].quantity}">
    </c:if>
    <c:if test="${order.orderStatus == '已取消'}">
    <form id="orderForm" class="valid-form" action="${ctx}/order/create" method="post">
    <input type="hidden" name="productId" value="${order.orderItem[0].productId}">
    <input type="hidden" name="quantity" value="${order.orderItem[0].quantity}">
    </c:if>
    
    <input type="hidden" name="sn" value="${order.sn}">
    
    <div class="list-group mt-10">
      <div class="list-title">收件人信息</div>
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-map-marker fs-24"></i></div>
        <div class="list-text fs-14 font-333 pl-5 pr-5">
          <div>${order.receiverRealname}<span class="right">${order.receiverPhone}</span></div>
          <div class="fs-14 font-777">${order.receiverProvince} ${order.receiverCity} ${order.receiverDistrict} ${order.receiverAddress}</div>
        </div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">商品信息</div>
      <c:forEach items="${order.orderItems}" var="orderItem">
      <div class="list-item">
        <img class="image-80 block mr-10" alt="" src="${orderItem.imageThumbnail}">
        <div class="list-text relative font-333">
          <div class="fs-14 lh-24">¥ ${orderItem.title}</div>
          <div class="lh-24 fs-14 text-right font-orange">¥ ${orderItem.price}</div>
          <div class="lh-24 fs-12 text-right font-gray">x ${orderItem.quantity}</div>
        </div>
      </div>
      </c:forEach>
      <div class="list-item">
        <div class="list-text text-right">
          <%-- <div class="fs-12">合计：<span class="font-red">¥ ${product.price * quantity}</span></div> --%>
          <div class="fs-14">应付款： <span class="font-orange fs-16 bold">¥ ${order.amount}</span></div>
        </div>
      </div>
    </div>
      
    
    <div class="list-title">买家留言</div>
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">
          <p class="fs-14 font-999">${order.buyerMemo}</p>
        </div>
        <%-- 
        <div class="list-unit">
          <a class="input-unit font-orange right" href="${ctx}/ucenter/order/edit/${order.sn}"><i class="fa fa-edit"></i></a>
        </div>
        --%>
      </div>
    </div>
    
    <c:if test="${order.orderStatus == '待支付'}">
    <div class="form-btn">
      <input type="submit" id="btnSubmit" value="立即支付" class="btn btn-block green round-2">
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已发货'}">
    <div class="form-btn">
      <input type="submit" id="btnSubmit" value="确认收货" class="btn btn-block green round-2">
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已完成'}">
    <div class="form-btn">
      <input type="submit" id="btnSubmit" value="再来一单" class="btn btn-block green round-2">
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已取消'}">
    <div class="form-btn">
      <input type="submit" id="btnSubmit" value="重新下单" class="btn btn-block green round-2">
    </div>
    </c:if>
    
    </form>
  </article>
  
</body>
</html>
