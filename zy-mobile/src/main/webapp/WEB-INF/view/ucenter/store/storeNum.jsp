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

  <title>全部服务</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />
  <style>
    .orderStock,.orderStockTwo {
      display:block;
      width: 80%;
      height: 40px;
      line-height: 40px;
      text-align: center;
      border-radius:5px;
      -webkit-border-radius:5px;
      border: 1px solid #6cb92d;
      color: #6cb92d;
      font-size: 18px;
      position: absolute;left: 50%;margin-left: -40%;
      top:200px;
    }
    .orderStockTwo {top:300px;}
  </style>
  <script>
    $(function() {
      $('.miui-scroll-nav').scrollableNav();
    });
  </script>
</head>
<body class="header-fixed footer-fixed">
<header class="header">
  <h1>全部服务</h1>
  <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  <%--<a href="${ctx}/u/productReplacement/create" class="button-right"><i class="fa fa-edit"></i>换货</a>--%>
</header>

<article class="order-list">
  <p>优库存量：${num}件</p>
  <a href="${ctx}/product" class="orderStock">我要进货</a>
  <a href="${ctx}/u/store/toOutOrder?orderStatus=2" class="orderStockTwo">我要发货</a>
</article>

<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
