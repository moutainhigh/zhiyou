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

<title>代理套餐商品</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/product.css" rel="stylesheet" />

<script>
  $(function() {

  });
</script>

</head>
<body class="product-list">

  <header class="header">
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a> <a class="button-right" href="${ctx}/help"><i class="fa fa-question-circle"></i></a>
    <h1>代理套餐商品</h1>
  </header>
  
  <div class="note note-warning mb-0">
    <p>
      <i class="fa fa-exclamation-circle"></i>购买以下套餐产品即可成为相应代理。
    </p>
  </div>

  <article class="mb-15 clearfix">
    <c:forEach items="${products}" var="product">
    <a href="${ctx}/product/${product.id}" class="product">
      <div class="image-box">
        <img class="abs-lt" src="${product.image1Thumbnail}">
      </div>
      <h3>${product.title}</h3>
      <div class="product-info">
        <span class="label orange mr-10">一级代理</span>
        <span class="fs-16 font-orange">¥ ${product.priceLabel}</span>
      </div>
    </a>
    </c:forEach>
  </article>

</body>
</html>
