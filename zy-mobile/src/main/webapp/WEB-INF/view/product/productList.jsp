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

  <title>全部服务</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <link href="${stccdn}/css/product.css" rel="stylesheet" />

</head>
<script>
 var userRank ='${userRank}';
 function  tohref(id) {
   if(userRank=="V0"||userRank=="V1"||userRank=="V2"){
     messageShow('敬请等待新品上线', 'error', 2);
   }else {
     $("#newproduct").attr("href","${ctx}/u/newOrder/"+id);
     $("#newproduct").click();
   }
 }
</script>

<body class="product-list footer-fixed">

<header class="header">
  <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
  <h1>全部服务</h1>
</header>

<article class="mb-15 clearfix">
  <c:forEach items="${products}" var="product">
    <c:if test="${product.productType == 1}">
      <a href="${ctx}/product/${product.id}" class="product">
        <div class="image-box">
          <img class="abs-lt" src="${product.image1Thumbnail}">
        </div>
        <h3>${product.title}</h3>
        <div class="product-info">
          <span class="fs-16 font-orange">¥ ${product.marketPrice}</span>
        </div>
      </a>
    </c:if>
    

      <c:if test="${product.productType == 2 and product.skuCode == 'zy-slj-pyqzy'}">
          <a href="javascript:void(0)" class="product" onclick="tohref(${product.id})" id="newproduct">
          <div class="image-box">
            <img class="abs-lt" src="${product.image1Thumbnail}">
          </div>
          <h3>${product.title}</h3>
          <div class="product-info">
            <span class="fs-16 font-orange">¥ ${product.marketPrice}</span>
          </div>
        </a>
      </c:if>

      <c:if test="${product.productType == 2 and product.skuCode == 'zy-slj'}">
          <a href="${ctx}/u/newOrder/newDetail/${product.id}" class="product">
          <div class="image-box">
            <img class="abs-lt" src="${product.image1Thumbnail}">
          </div>
          <h3>${product.title}</h3>
          <div class="product-info">
            <span class="fs-16 font-orange">¥ ${product.marketPrice}</span>
          </div>
        </a>
      </c:if>
  </c:forEach>
</article>

<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
