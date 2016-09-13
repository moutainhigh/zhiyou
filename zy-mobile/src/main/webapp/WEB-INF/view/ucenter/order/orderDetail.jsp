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

<link href="${stccdn}/css/ucenter/order.css" rel="stylesheet"> 

<title>订单详情</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stccdn}/js/area.js"></script>

<script>
  $(function() {

    /*
     * 进货订单操作(买家)
     */

    //买家支付
    $('#btnPay').click(function() {
      $.dialog({
        content : '请选择支付方式',
        skin : 'footer',
        btn : [ '<a id="btnPay1" class="btn orange block round-2">余额支付</a>', '<a id="btnPay2" class="btn green block round-2">银行汇款</a>' ],
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/pay/order/${order.id}?payType=0';
          } else if (index == 2) {
            location.href = '${ctx}/u/pay/order/${order.id}?payType=1';
          }
        }
      });
    });
    
    $('#btnConfirm').click(function() {
      $.dialog({
        content : '请确认您已收到该订单商品，您确定要确认收货吗？',
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/order/confirmDelivery?id=${order.id}';
          }
        }
      });
    });
    
    

    /*
     * 出货订单操作(卖家)
     */
    $('#btnPlatformDeliver').click(function() {
      $.dialog({
        content : '您确定要将此订单转给公司发货吗？',
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/order/platformDeliver?id=${order.id}';
          }
        }
      });
    });
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
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：买家未支付</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已支付'}">
    <div class="note note-danger mb-0">
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：已支付，等待卖家发货</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已发货'}">
    <div class="note note-success mb-0">
      <p><i class="fa fa-truck fs-16"></i> 订单状态：卖家已发货</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已完成'}">
    <div class="note note-success mb-0">
      <p><i class="fa fa-check fs-16"></i> 订单状态：已完成</p>
      <%-- 结算信息 --%>
      <c:if test="${order.isSettledUp}">
      
      </c:if>
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
      
    <div class="list-group">
      <div class="list-item lh-20">
        <div class="list-text fs-14">下单时间</div>
        <div class="list-unit fs-12">${order.createdTimeLabel}</div>
      </div>
      <c:if test="${not empty order.paidTimeLabel}">
      <div class="list-item lh-20">
        <div class="list-text fs-14">支付时间</div>
        <div class="list-unit fs-12">${order.paidTimeLabel}</div>
      </div>
      </c:if>
      <c:if test="${not empty order.deliveredTimeLabel}">
      <div class="list-item lh-20">
        <div class="list-text fs-14">发货时间</div>
        <div class="list-unit fs-12">${order.deliveredTimeLabel}</div>
      </div>
      </c:if>
      <c:if test="${not empty order.refundedTimeLabel}">
      <div class="list-item lh-20">
        <div class="list-text fs-14">退款时间</div>
        <div class="list-unit fs-12">${order.refundedTimeLabel}</div>
      </div>
      </c:if>
    </div>
    
    <div class="list-title">买家留言</div>
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">
          <p class="fs-14 font-999">${empty order.buyerMemo ? '买家无留言' : order.buyerMemo}</p>
        </div>
        <%-- 
        <div class="list-unit">
          <a class="input-unit font-orange right" href="${ctx}/ucenter/order/edit/${order.sn}"><i class="fa fa-edit"></i></a>
        </div>
        --%>
      </div>
    </div>
    
    <%-- 买家操作(进货) --%>
    <c:if test="${inOut == 'in'}">
      <c:if test="${order.orderStatus == '待支付'}">
        <div class="form-btn">
          <a id="btnPay" class="btn btn-block green round-2" href="javascript:;"><i class="fa fa-cny"></i> 立即支付</a>
        </div>
      </c:if>
      <c:if test="${order.orderStatus == '已发货'}">
        <div class="form-btn">
          <a id="btnConfirm" class="btn green btn-block round-2"><i class="fa fa-check"></i> 确认收货</a>
        </div>
      </c:if>
      <c:if test="${order.orderStatus == '已完成'}">
        <form id="orderForm" action="${ctx}/u/order/create" method="get">
        <input type="hidden" name="productId" value="${order.orderItems[0].productId}">
        <input type="hidden" name="quantity" value="${order.orderItems[0].quantity}">
        <div class="form-btn">
          <button id="btnReOrder" type="submit" class="btn btn-block red round-2"><i class="fa fa-redo"></i> 再来一单</button>
        </div>
        </form>
      </c:if>
      <c:if test="${order.orderStatus == '已取消'}">
        <form id="orderForm" action="${ctx}/u/order/create" method="get">
        <input type="hidden" name="productId" value="${order.orderItems[0].productId}">
        <input type="hidden" name="quantity" value="${order.orderItems[0].quantity}">
        <div class="form-btn">
          <button id="btnReOrder" type="submit" class="btn btn-block red round-2"><i class="fa fa-redo"></i> 重新下单</button>
        </div>
        </form>
      </c:if>
    </c:if>
    
    <%-- 卖家操作(出货) --%>
    <c:if test="${inOut == 'out'}">
      <c:if test="${order.orderStatus == '待支付'}">
        <div class="form-btn">
          <a class="btn disabled btn-block round-2" href="javascript:;"><i class="fa fa-clock"></i> 等待买家支付</a>
        </div>
      </c:if>
      <c:if test="${order.orderStatus == '已支付' && !order.isPlatformDeliver}">
        <c:if test="${userRank == 'V4'}">
          <div class="form-btn">
            <a id="btnPlatformDeliver" class="btn blue btn-block round-2"><i class="fa fa-share"></i> 转给公司发货</a>
          </div>
        </c:if>
        <form id="orderForm" action="${ctx}/u/order/deliver" method="get">
          <input type="hidden" name="id" value="${order.id}">
          <div class="form-btn">
            <button id="btnDeliver" type="submit" class="btn btn-block orange round-2"><i class="fa fa-truck"></i> 发货</button>
          </div>
        </form>
      </c:if>
      <c:if test="${order.orderStatus == '已支付' && order.isPlatformDeliver}">
        <div class="form-btn">
          <a class="btn disabled btn-block round-2" href="javascript:;"><i class="fa fa-share"></i> 已转给公司发货，等待公司发货。</a>
        </div>
      </c:if>
    </c:if>
    
  </article>
  
</body>
</html>
