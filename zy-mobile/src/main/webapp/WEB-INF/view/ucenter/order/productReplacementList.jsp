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

<title>更换产品列表</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />

</head>
<body>
<body class="header-fixed footer-fixed">
  <header class="header">
    <h1>更换产品</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <%--<a href="${ctx}/u/productReplacement/create" class="button-right"><i class="fa fa-edit"></i>换货</a>--%>
  </header>
  
  <article class="order-list">
    <c:if test="${empty productReplacements}">
    <div class="page-empty">
      <i class="fa fa-file-o"></i>
      <span>空空如也!</span>
    </div>
    </c:if>
    
    <c:forEach items="${productReplacements}" var="productReplacement">
    <a class="order mt-15 bd-t bd-b" href="${ctx}/u/productReplacement/detail?id=${productReplacement.id}">
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <div style="min-height: 30px; margin-top: 5px;">${productReplacement.fromProduct}更换为${productReplacement.toProduct}</div>
          <div class="product-price abs-rt text-right">
            <div class="lh-24 fs-12">x${productReplacement.quantity}</div>
          </div>
        </div>
      </div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1 font-999 fs-12"><span>更换时间：${productReplacement.createdTimeLabel}</span></div>
          <div class="flex-1 font-777 text-right">
            <label class="label<c:if test="${productReplacement.productReplacementStatus == '已申请'}"> orange</c:if>
              <c:if test="${productReplacement.productReplacementStatus == '已发货'}"> yellow</c:if>
              <c:if test="${productReplacement.productReplacementStatus == '已完成'}"> green</c:if>
              <c:if test="${productReplacement.productReplacementStatus == '已驳回'}"> gray</c:if>">
              ${productReplacement.productReplacementStatus}</label></div>
        </div>
      </div>
    </a>
    </c:forEach>
    
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
