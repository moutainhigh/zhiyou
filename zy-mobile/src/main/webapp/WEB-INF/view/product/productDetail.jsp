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
  <meta name="description" content="服务详情" />

  <title>服务详情</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <link href="${stccdn}/css/product.css" rel="stylesheet" />
  <script>
    $(function() {

      var MIN_QUANTITY = ${minQuantity};
      var MAX_QUANTITY = 10000;

      function editQuantity(quantity) {
        if (isNaN(quantity) || quantity < MIN_QUANTITY) {
          quantity = MIN_QUANTITY;
        } else if (quantity > MAX_QUANTITY) {
          quantity = MAX_QUANTITY;
        } else if(quantity % MIN_QUANTITY != 0) {
          quantity = quantity - quantity % MIN_QUANTITY;
        }
        if (quantity == MIN_QUANTITY) {
          $(".fa-minus").addClass('disabled');
          $(".fa-plus").removeClass('disabled');
        } else if (quantity == MAX_QUANTITY) {
          $(".fa-minus").removeClass('disabled');
          $(".fa-plus").addClass('disabled');
        } else {
          $(".fa-minus").removeClass('disabled');
          $(".fa-plus").removeClass('disabled');
        }
        $("#quantity").val(quantity);
      }

      //商品数量 加减
      $('#quantity').blur(function() {
        var quantity = $("#quantity").val();
        editQuantity(parseInt(quantity));
      });

      $('.fa-plus').click(function() {
        var quantity = $("#quantity").val();
        editQuantity(parseInt(quantity) + MIN_QUANTITY);
      });

      $('.fa-minus').click(function() {
        var quantity = $("#quantity").val();
        editQuantity(parseInt(quantity) - MIN_QUANTITY);
      });

      //下单
      $('#btnOrder').click(function() {
        var quantity = $("#quantity").val();
        if(quantity < MIN_QUANTITY || quantity % MIN_QUANTITY != 0) {
          messageAlert('省级服务商和特级服务商每次购买最小单位' + MIN_QUANTITY + '次');
          return;
        }
        $('#form').submit();
      });
    })
  </script>
</head>

<body class="product-detail footer-fixed">
<a class="header-back" href="${ctx}/product"><i class="fa fa-angle-left"></i></a>

<form id="form" action="${ctx}/u/order/create" method="get">
  <input type="hidden" name="productId" value="${product.id}">
  <article class="product-wrap">
    <figure class="product-image">
      <img class="abs-lt" src="${product.image1Big}">
    </figure>
    <div class="list-group">
      <div class="list-item">
        <h2 class="product-title font-333 fs-16 lh-30">${product.title}</h2>
      </div>
      <div class="list-item">
        <div class="font-777 fs-14">
          <span class="fs-15">编号： <span> ${product.skuCode}</span></span>
        </div>
      </div>
      <div class="list-item">
        <div class="font-777 fs-14">
          <span class="fs-15">服务零售价： <span> ¥ ${product.marketPrice}</span></span>
        </div>
      </div>
      <c:if test="${not empty product.price}">
        <div class="list-item">
          <div class="font-777 fs-14">
            <span class="fs-15">服务商价： </span><span class="font-orange fs-18 bold"> ¥ ${product.price}</span>
          </div>
        </div>
      </c:if>
    </div>

    <div class="list-group mb-0">
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-list-alt font-orange"></i></div>
        <div class="list-text">服务介绍</div>
      </div>
      <div class="list-item p-0">
        <div class="list-text">
          <div class="detail-wrap">
            <c:if test="${empty product.detail}"><p class="p-15">暂无介绍</p></c:if>
            <c:if test="${not empty product.detail}">${product.detail}</c:if>
          </div>
        </div>
      </div>
    </div>

  </article>

  <nav class="footer footer-nav flex">
    <div class="flex-2">
      <div class="quantity-wrap">
        <i class="fa fa-minus disabled"></i>
        <input type="text" class="input-quantity text-center fs-14" id="quantity" name="quantity" value="${minQuantity}">
        <i class="fa fa-plus"></i>
      </div>
    </div>
    <a id="btnOrder" class="flex-2 btn-order" href="javascript:;">购买</a>
  </nav>

</form>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
