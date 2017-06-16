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
<meta name="description" content="购物车" />

<title>购物车</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stc}/css/cart.css" rel="stylesheet" />
<script>
  $(function() {
    var MAX_QUANTITY = 10000;
    //计算商品总数量、 总价格
    var calculate = function() {
      var $cartItems = $('.cart-item');
      if ($cartItems.length == 0) {
        $('.cart-item').remove();
        $('#totalQuantity').text(0);
        $('#totalPrice').text(0);
        $('#btnClear').remove();
        $('#btnSubmit').addClass('disabled');
        $('.page-empty').show();
        return;
      }
      var totalQuantity = 0;
      var totalPrice = 0;
      for (var i = 0; i < $cartItems.length; i++) {
        var quantity = Number($cartItems.eq(i).find('.quantity-num').text());
        totalQuantity += quantity;
        totalPrice += quantity * Number($cartItems.eq(i).find('.price').text());
      }
      $('#totalQuantity').text(totalQuantity);
      $('#totalPrice').text(totalPrice);
    }
    //calculate();

    var refreshCart = function(cart){
      if (cart.cartItems.length == 0) {
        $('.cart-item').remove();
        $('#totalQuantity').text(0);
        $('#totalPrice').text(0);
        $('#btnClear').remove();
        $('#btnSubmit').addClass('disabled');
        $('.page-empty').show();
        return;
      }
      for(var i in cart.cartItems) {
        var cartItem = cart.cartItems[i];
        var quantity = cartItem.quantity;
        console.info(cartItem.skuId + '/price=' + cartItem.price);
        var $cartItem = $('[data-skuId="' + cartItem.skuId + '"]');
        $cartItem.find('.price').text(cartItem.price.toFixed(2));
        $cartItem.find('.input-quantity').val(quantity);
        $cartItem.find('.quantity-num').text(quantity);
        if (quantity == 1) {
          $cartItem.find(".fa-minus").addClass('disabled');
          $cartItem.find(".fa-plus").removeClass('disabled');
        } else if (quantity == MAX_QUANTITY) {
          $cartItem.find(".fa-minus").removeClass('disabled');
          $cartItem.find(".fa-plus").addClass('disabled');
        } else {
          $cartItem.find(".fa-minus").removeClass('disabled');
          $cartItem.find(".fa-plus").removeClass('disabled');
        }
      }
      $('#totalQuantity').text(cart.totalQuantity);
      $('#totalPrice').text(cart.amount.toFixed(2));
    }

    //编辑
    $('.btn-edit').click(function() {
      $(this).parents('.options').removeClass('edit-off').addClass('edit-on');
      var num = $(this).parents('.options').find('.quantity-num').text();
      $(this).parents('.options').find('.input-quantity').val(num);
    });

    //完成
    $('.btn-ok').click(function() {
      $(this).parents('.options').removeClass('edit-on').addClass('edit-off');
      var editNum = $(this).parents('.options').find('.input-quantity').val();
      $(this).parents('.options').find('.quantity-num').text(editNum);
      //calculate();
    });

    //删除
    $('.btn-remove').click(function() {
      var $this = $(this);
      $.dialog({
        content : '您确定要删除吗？',
        callback : function(index){
          if(index == 1) {
            var skuId = $this.parents('.cart-item').attr('data-skuId');
            $.ajax({
              url : '${ctx}/cart/remove',
              data : {
                skuId : skuId
              },
              type : 'POST',
              dataType : 'json',
              success : function(result) {
                $this.parents('.cart-item').remove();
                var cart = result.data;
                refreshCart(cart);
              }
            });
            return true;
          }
        }
      });
    });

    //清空购物车
    $('#btnClear').click(function() {
      $.dialog({
        content : '您确定要删除吗？',
        callback : function(index){
          if(index == 1) {
            $.ajax({
              url : '${ctx}/cart/removeAll',
              type : 'POST',
              dataType : 'json',
              success : function(result) {
                var cart = result.data;
                refreshCart(cart);
              }
            });
            return true;
          }
        }
      });
    });

    function editQuantity(cartItem, quantity) {
      if (isNaN(quantity) || quantity < 1) {
        quantity = 1;
      } else if (quantity > MAX_QUANTITY) {
        quantity = MAX_QUANTITY;
      }
      $.ajax({
        url : '${ctx}/cart/modify',
        data : {
          skuId : cartItem.attr('data-skuId'),
          quantity : quantity
        },
        type : 'POST',
        dataType : 'json',
        success : function(result) {
          var cart = result.data;
          refreshCart(cart);
        }
      });
    }

    //商品数量 加减
    $('.input-quantity').blur(function() {
      editQuantity($(this).parents('.cart-item'), parseInt($(this).val()));
    });

    //加法操作
    $('.fa-plus').click(function() {
      var $quantity = $(this).siblings(".input-quantity");
      editQuantity($(this).parents('.cart-item'), parseInt($quantity.val()) + 1);
    });

    //减法操作
    $('.fa-minus').click(function() {
      var $quantity = $(this).siblings(".input-quantity");
      editQuantity($(this).parents('.cart-item'), parseInt($quantity.val()) - 1)
    });

    $('#btnSubmit').click(function() {
      if ($(this).hasClass('disabled')) {
        return false;
      }
      var quantity = $('#totalQuantity').text();
      if (quantity == 0) {
        messageFlash('购物车没有商品，不能结算');
        return false;
      }
      location.href = '${ctx}/u/order/create';
    });

  });
</script>

</head>

<body class="footer-fixed">
  <header class="header">
    <h1>购物车</h1>
    <a href="<c:if test="${__hasReferer}">javascript:history.back();</c:if><c:if test="${not __hasReferer}">${ctx}/</c:if>" class="button-left"><i class="fa fa-angle-left"></i></a>
    <c:if test="${!empty cart.cartItems}">
      <a href="javascript:;" id="btnClear" class="button-right"><span>清空</span></a>
    </c:if>
  </header>

  <article>
    <div class="page-empty<c:if test="${not empty cart.cartItems}"> hide</c:if>">
      <i class="fa fa-shopping-cart"></i>
      <a href="/product">您的购物车空空如也<br>赶紧去抢购商品吧!</a>
    </div>

    <div class="list-group">
      <c:forEach items="${cart.cartItems}" var="cartItem">
        <div class="list-item cart-item" data-skuId="${cartItem.skuId}">
          <a href="${ctx}/product/${cartItem.productId}"><img class="image-80" src="${cartItem.productImageThumbnail}" alt=""></a>
          <div class="list-text ml-10">
            <div class="fs-14 lh-20">${cartItem.productTitle}</div>
            <div class="fs-14 lh-20 font-red" data-price="${cartItem.price}"> ¥ <span class="price">${cartItem.price}</span></div>
            <div class="options edit-off">
              <div class="product-quantity mt-5">
                <span class="quantity-num">${cartItem.quantity}</span>
                <div class="quantity-wrap round-4">
                  <i class="fa fa-minus<c:if test="${cartItem.quantity == 1}"> disabled</c:if>"></i>
                  <input type="text" class="input-quantity roundless p-0 text-center fs-14" name="quantity" value="${cartItem.quantity}">
                  <i class="fa fa-plus<c:if test="${cartItem.quantity == 10000}"> disabled</c:if>"></i>
                </div>
              </div>
              <div class="option-btns mt-10">
                <a class="btn-edit" href="javascript:;">编辑</a>
                <a class="btn-ok" href="javascript:;">完成</a>
                <a class="btn-remove" href="javascript:;">删除</a>
              </div>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </article>

  <nav class="footer footer-nav flex">
    <div class="total-wrap flex-2 pl-20">
      <div class="cart-total">合计: <span class="ml-5 font-red bold">¥ <span id="totalPrice">${cart.amount}</span></span></div>
    </div>
    <a id="btnSubmit" class="flex-1 bg-red font-white fs-16 text-center<c:if test="${empty cart.cartItems}"> disabled</c:if>" href="javascript:;">去结算(<span id="totalQuantity">${cart.totalQuantity}</span>)</a>
  </nav>

</body>
</html>
