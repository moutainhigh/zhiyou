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
    .order-list p {
      height:40px;
      color: #838584;
      font-size: 18px;
      text-align: center;
      line-height: 40px;
    }
    .Ushop {
       width: 100%;
    }
    .UshopDetil {
      display: block;
      color: #6cb92d;
      font-size: 18px;
      text-align: center;
    }
    .orderStock,.orderStockTwo {
      display: block;
      float: left;
      height:40px;
      line-height: 40px;
      text-align: center;
      width:40%;
      -webkit-border-radius:5px;
      -moz-border-radius:5px;
      border-radius:5px;
      color: #fff;
      position: absolute;
      left:6%;
      font-size: 18px;
      bottom: 50px;
      background: #6cb92d;
      <%--background: url("${ctx}/images/car2.png") #6cb92d center center no-repeat;--%>
      <%--background-size: 26px;--%>
      <%--background-position: 5%;--%>
    }
    .orderStockTwo {
      position: absolute;
      left:54%;
      bottom: 50px;
      <%--background: url("${ctx}/images/car.png") #6cb92d center center no-repeat;--%>
      <%--background-size: 26px;--%>
      <%--background-position: 5%;--%>
    }
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
  <img src="${ctx}/images/Ushop.png" class="Ushop"/>
  <p>优库存量：${num}件</p>
  <a href="/u/store/details" class="UshopDetil">详情 >>></a>
  <a href="${ctx}/product" class="orderStock">我要进货</a>
  <a href="${ctx}/u/store/toOutOrder?orderStatus=2" class="orderStockTwo">我要发货</a>
</article>

<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
