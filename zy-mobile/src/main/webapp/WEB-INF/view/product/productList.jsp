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
    
    <c:if test="${userRank == 'V4' || isUse == 1}">
      <c:if test="${product.productType == 2}">
        <a href="${ctx}/u/newOrder/${product.id}" class="product">
          <div class="image-box">
            <img class="abs-lt" src="${product.image1Thumbnail}">
          </div>
          <h3>${product.title}</h3>
          <div class="product-info">
            <span class="fs-16 font-orange">¥ ${product.marketPrice}</span>
          </div>
        </a>
      </c:if>
    </c:if>
    <%--&lt;%&ndash;新增代码 start&ndash;%&gt;--%>
    <%--<a href="${ctx}/product/${product.id}" class="product">--%>
      <%--<div class="image-box">--%>
        <%--<img class="abs-lt" src="${product.image1Thumbnail}">--%>
      <%--</div>--%>
      <%--<h3>新品</h3>--%>
        <%--&lt;%&ndash;联合创始人&ndash;%&gt;--%>
      <%--<div class="product-info">--%>
        <%--<span class="fs-16 font-orange">¥ 248.00</span>--%>
      <%--</div>--%>
        <%--&lt;%&ndash;品牌创始人&ndash;%&gt;--%>
      <%--<div class="product-info" style="display: none;">--%>
        <%--<span class="fs-16 font-orange">¥ 320.00</span>--%>
      <%--</div>--%>
    <%--</a>--%>
    <%--&lt;%&ndash;新增代码 end&ndash;%&gt;--%>
  </c:forEach>
</article>

<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
