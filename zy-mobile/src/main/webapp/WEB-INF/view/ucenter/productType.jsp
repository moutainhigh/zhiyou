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

  <title>选择商品体系</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <link href="${stccdn}/css/product.css" rel="stylesheet" />

</head>

<body class="product-list footer-fixed">


<header class="header">
  <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
  <h1>选择商品体系</h1>
</header>

<article class="mb-15 clearfix">
  <c:forEach items="${products}" var="product">
    <c:if test="${product.productType == 1 && isOld}">
      <a href="${ctx}/u/team/newTeam?productType=${product.productType}"  class="product">
        <div class="image-box">
          <img class="abs-lt" src="${product.image1Thumbnail}">
        </div>
        <h3>${product.title}</h3>
      </a>
    </c:if>
    <c:if test="${product.productType == 2 && isnew}">
      <a href="${ctx}/u/team/newTeam?productType=${product.productType}"  class="product">
        <div class="image-box">
          <img class="abs-lt" src="${product.image1Thumbnail}">
        </div>
        <h3>${product.title}</h3>
      </a>
    </c:if>
  </c:forEach>
</article>

<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
