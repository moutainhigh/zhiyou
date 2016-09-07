<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
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
<meta name="keywords" content="微信分销" />
<meta name="description" content="我的收货地址" />

<title>我的收货地址</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/address.css" />
<script type="text/javascript">
	$(function() {

	});
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>收货地址</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="address-list">
    <c:if test="${empty list}">
      <div class="empty-tip">
        <i class="fa fa-map-marker"></i>
        <span>空空如也!</span>
      </div>
    </c:if>
    
    <div class="list-group">
      <c:forEach items="${list}" var="address" varStatus="status">
      <a href="${ctx}/u/address/${address.id}" class="address<c:if test="${address.isDefault}"> default</c:if>">
        <i class="i-default fa fa-check-circle"></i>
        <i class="i-arrow"></i>
        <div class="address-wrap relative">
          <div class="fs-16 bold lh-24">${address.realname}<span class="right">${address.phone}</span></div>
          <address class="fs-12">${address.province} ${address.city} ${address.district} ${address.address}</address>
        </div>
      </a>
      </c:forEach>
    </div>
    
    <div class="list-group">
      <a class="list-item" href="${ctx}/u/address/create">
        <div class="list-icon"><i class="fa fa-plus-circle font-green fs-20"></i></div>
        <label class="list-text">新增收货地址</label>
        <i class="list-arrow"></i>
      </a>
    </div>
  </article>
  
</body>
</html>
