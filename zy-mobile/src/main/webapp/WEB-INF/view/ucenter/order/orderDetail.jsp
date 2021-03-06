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

<title>订单详情</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stccdn}/js/area.js"></script>

<script>
  $(function() {
    
    $('.header .button-popmenu').click(function(){
      $('.header-popmenu').toggle(300);
    });
    
    $("article").click(function() {
      $('.header-popmenu').slideUp();
    });
    
    $('.image-view').click(function() {
      var images = $(this).find('img');
      if (images.length == 0) {
        var url = $(this).attr('data-src');
        var title = $(this).attr('data-title');
        $.imageview({
          url : url,
          title : title
        });
      } else {
        var title = $(this).attr('data-title');
        var imageUrls = [];
        $.each(images, function(n, image) {
          imageUrls.push($(image).attr('data-src'));
        })
        $.imageview({
          url : imageUrls,
          title : title
        });
      }
    });

    /*
     * 进货订单操作(买家)
     */

    //买家支付
    $('#btnPay').click(function() {
      $.dialog({
        content : '请选择支付方式',
        skin : 'footer',
        btn : [ '<a id="btnPay1" class="btn orange block round-2">U币积分支付</a>'/*, '<a id="btnPay2" class="btn green block round-2">银行汇款</a>' */],
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/pay/order/${order.id}?payType=0';
          }/*  else if (index == 2) {
            location.href = '${ctx}/u/pay/order/${order.id}?payType=1';
          } */
        }
      });
    });
    
    //确认收货
    $('#btnConfirm').click(function() {
      $.dialog({
        content : '您确定已收到该订单的商品吗？',
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
    
    //确认收款
    $('#btnConfirmPay').click(function() {
      $.dialog({
        content : '您确定已收到买家款项吗？',
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/order/confirmPay?id=${order.id}';
          }
        }
      });
    });
    
  //确认收款
    $('#btnRejectPay').click(function() {
      $.dialog({
        content : '您确定未收到买家款项并驳回此订单吗？',
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/order/rejectPay?id=${order.id}';
          }
        }
      });
    });
    
    /* 取消订单 */
    $('#btnDelete').click(function() {
      $.dialog({
        content : '您确定要删除此订单吗？',
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/order/delete?id=${order.id}';
          }
        }
      });
    });
    
    /* 删除订单 */
    $('#btnCancel').click(function() {
      $.dialog({
        content : '您确定要取消此订单吗？',
        callback : function(index) {
          if (index == 1) {
            location.href = '${ctx}/u/order/cancel?id=${order.id}';
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
    <c:if test="${ inOut == 'in' && (order.orderStatus == '已取消' || order.orderStatus == '已完成' || order.orderStatus == '待支付')}">
    <a href="javascript:;" class="button-right button-popmenu"><i class="fa fa-ellipsis-h"></i></a>
    <nav class="header-popmenu hide">
    <c:if test="${order.orderStatus == '待支付'}">
      <a id="btnCancel" href="javascript:;"><i class="fa fa-times-circle-o"></i> 取消</a>
      </c:if>
      <c:if test="${order.orderStatus == '已取消' || order.orderStatus == '已完成'}">
      <a id="btnDelete" href="javascript:;"><i class="fa fa-trash-o"></i> 删除</a>
      </c:if>
    </nav>
    </c:if>
  </header>
  
  <article>
    <%-- 待支付, 待确认, 已支付, 已发货, 已完成, 已退款, 已取消 --%>
    <c:if test="${order.orderStatus == '待支付'}">
    <div class="note note-danger mb-0">
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：买家未支付</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '待确认'}">
    <div class="note note-danger mb-0">
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：买家已支付，等待卖家确认</p>
    </div>
    </c:if>
    <c:if test="${order.orderStatus == '已支付'}">
    <div class="note note-danger mb-0">
      <p><i class="fa fa-clock-o fs-16"></i> 订单状态：已支付成功，等待卖家发货</p>
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
    
    <%-- 结算信息 --%>
    <c:if test="${order.isSettledUp && not empty profits}">
    
    <div class="list-group mt-10">
      <div class="list-title">我的收益</div>
      <c:forEach items="${profits}" var="profit">
      <div class="list-item lh-20">
        <div class="list-text">${profit.title}</div>
        <div class="list-unit font-orange">${profit.amount}元</div>
      </div>
      </c:forEach>
    </div>
    
    </c:if>
    
    <div class="list-group">
      <div class="list-title">收货人信息</div>
      <c:if test="${inOut == 'out'}">
        <div class="list-item">
          <img class="image-40 round mr-10" src="${buyer.avatarThumbnail}">
          <div class="list-text fs-14">${buyer.nickname}</div>
          <div class="list-unit">${buyer.phone}</div>
        </div>
      </c:if>
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-map-marker font-orange fs-24"></i></div>
        <div class="list-text fs-14 font-333 pl-5">
          <div>${order.receiverRealname}<span class="right">${order.receiverPhone}</span></div>
          <div class="fs-14 font-777">${order.receiverProvince} ${order.receiverCity} ${order.receiverDistrict} ${order.receiverAddress}</div>
        </div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">发货信息</div>
      <c:if test="${inOut == 'in'}">
        <div class="list-item lh-20">
          <div class="list-text fs-14">发货人</div>
          <c:if test="${order.isPlatformDeliver}">
            <div class="list-unit">公司发货</div>
          </c:if>
          <c:if test="${!order.isPlatformDeliver}">
            <div class="list-unit">${seller.nickname} <small>${seller.phone}</small></div>
            <img class="image-40 round ml-10" src="${seller.avatarThumbnail}">
          </c:if>
        </div>
      </c:if>
      <c:if test="${inOut == 'out'}">
        <div class="list-item lh-20">
          <div class="list-text fs-14">发货人</div>
          <c:if test="${order.isPlatformDeliver}">
            <div class="list-unit">公司发货</div>
          </c:if>
          <c:if test="${!order.isPlatformDeliver}">
            <div class="list-unit">自己发货</div>
          </c:if>
        </div>
      </c:if>
      <c:if test="${order.isUseLogistics == true}">
      <div class="list-item lh-20">
        <div class="list-text fs-14">物流公司</div>
        <div class="list-unit">${order.logisticsName}</div>
      </div>
      <div class="list-item lh-20">
        <div class="list-text fs-14">物流单号</div>
        <div class="list-unit fs-12">${order.logisticsSn}</div>
      </div>
      <div class="list-item lh-20">
        <div class="list-text fs-14">物流费用</div>
        <div class="list-unit">${order.logisticsFee}</div>
      </div>
      </c:if>
      <c:if test="${order.isUseLogistics == false}">
      <div class="list-item lh-20">
        <div class="list-text fs-14">发货方式</div>
        <div class="list-unit">面对面发货</div>
      </div>
      </c:if>
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
        <div class="list-text fs-14">订单编号</div>
        <div class="list-unit fs-12">${order.sn}</div>
      </div>
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
      <div class="list-item lh-20">
        <div class="list-text fs-14">付款给</div>
        <c:if test="${order.isPayToPlatform}">
        <div class="list-unit fs-12">平台</div>
        </c:if>
        <c:if test="${!order.isPayToPlatform}">
        <div class="list-unit">${seller.nickname} <small>${seller.phone}</small></div>
        <img class="image-40 round ml-10" src="${seller.avatarThumbnail}">
        </c:if>
      </div>
      <div class="list-item lh-20">
        <div class="list-text fs-14">类型</div>
        <div class="list-unit fs-12">${order.orderType}</div>
      </div>
    </div>
    
    <c:if test="${not empty order.offlineImages}">
    <div class="list-group">
      <div class="list-title">支付凭证</div>
      <div class="list-item">
        <div class="list-text list-image image-view" data-title="支付凭证">
        <c:forEach items="${order.offlineImages}" var="image">
          <img src="${image.imageThumbnail}" data-src="${image.imageBig}">
        </c:forEach>
        </div>
      </div>
      <div class="list-item">
        <div class="list-text">${order.offlineMemo}</div>
      </div>
    </div>
    </c:if>

    <div class="list-group">
      <div class="list-title">买家留言</div>
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
        <c:if test="${order.isPayToPlatform}">
        <div class="form-btn">
          <a id="btnPay" class="btn btn-block green round-2" href="javascript:;"><i class="fa fa-cny"></i> 立即支付</a>
        </div>
        </c:if>
        <c:if test="${!order.isPayToPlatform}">
        <div class="form-btn">
          <a id="btnPayOffline" class="btn btn-block green round-2" href="${ctx}/u/order/pay/${order.id}?payType=1"><i class="fa fa-cny"></i> 立即支付</a>
        </div>
        </c:if>
      </c:if>
      <c:if test="${order.orderStatus == '已发货'}">
        <c:if test="${!order.isCopied || !order.isPlatformDeliver}">
        <div class="form-btn">
          <a id="btnConfirm" class="btn green btn-block round-2"><i class="fa fa-check"></i> 确认收货</a>
        </div>
        </c:if>
        <c:if test="${order.isPlatformDeliver && order.isCopied}">
        <div class="form-btn">
          <a class="btn disabled btn-block round-2" href="javascript:;"><i class="fa fa-send"></i> 请等待下级服务商确认收货</a>
        </div>
        </c:if>
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
    </c:if>
    
    <%-- 卖家操作(出货) --%>
    <c:if test="${inOut == 'out'}">
      <c:if test="${order.orderStatus == '待支付'}">
        <div class="form-btn">
          <a class="btn disabled btn-block round-2" href="javascript:;"><i class="fa fa-clock"></i> 等待买家支付</a>
        </div>
      </c:if>
      <c:if test="${order.orderStatus == '待确认'}">
        <div class="form-btn">
          <a id="btnConfirmPay" class="btn green btn-block round-2" href="javascript:;"><i class="fa fa-check"></i> 确认已打款</a>
        </div>
        <div class="form-btn">
          <a id="btnRejectPay" class="btn red btn-block round-2" href="javascript:;"><i class="fa fa-times"></i> 驳回已打款</a>
        </div>
      </c:if>
      
      <c:if test="${order.orderStatus == '已支付' && !order.isCopied}">
        <c:if test="${canCopy}">
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
      <c:if test="${order.orderStatus == '已支付' && order.isCopied}">
        <div class="form-btn">
          <a class="btn disabled btn-block round-2" href="javascript:;"><i class="fa fa-share"></i> 已转给公司发货，等待公司发货。</a>
        </div>
      </c:if>
    </c:if>
    
  </article>
  
</body>
</html>
