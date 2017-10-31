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
        <h2 class="product-title font-333 fs-16 lh-30">新品</h2>
      </div>
      <div class="list-item">
        <div class="font-777 fs-14">
          <span class="fs-15">编号： <span> ${product.skuCode}</span></span>
        </div>
      </div>
      <%--联合创始人--%>
      <div class="list-item">
        <div class="font-777 fs-14">
          <span class="fs-15">数量： <span>160</span></span>
        </div>
      </div>
      <div class="list-item">
        <div class="font-777 fs-14">
          <span class="fs-15">服务零售价： <span> ¥ 248</span></span>
        </div>
      </div>
      <div class="list-item">
        <div class="font-777 fs-14">
          <span class="fs-15">服务总价： <span> ¥ 39680</span></span>
        </div>
      </div>

      <%--品牌创始人--%>
      <div style="display: none;">
        <div class="list-item">
          <div class="font-777 fs-14">
            <span class="fs-15">数量： <span>20</span></span>
          </div>
        </div>
        <div class="list-item">
          <div class="font-777 fs-14">
            <span class="fs-15">服务零售价： <span> ¥ 320</span></span>
          </div>
        </div>
        <div class="list-item">
          <div class="font-777 fs-14">
            <span class="fs-15">服务总价价： <span> ¥ 6400</span></span>
          </div>
        </div>
      </div>
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
    <a id="btnOrder" class="flex-2 btn-order" href="javascript:;">购买</a>
  </nav>

</form>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
