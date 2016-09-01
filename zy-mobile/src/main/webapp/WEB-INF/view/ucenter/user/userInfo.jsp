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
<meta name="keywords" content="微信分销" />
<meta name="description" content="用户信息 " />

<title>用户信息</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/index.css" />
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body class="header-fixed">

  <header class="header">
    <h1>我的信息</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article class="user-info">
    <div class="list-group mt-10">
      <div class="list-title">基本信息</div>
      <a class="list-item list-item-avatar" href="${ctx}/u/avatar">
        <label class="list-label">头像</label>
        <span class="list-text">
          <img class="image-60 round bd" src="${userAvatarSmall}">
          <i class="list-arrow"></i>
        </span>
      </a>
      <a class="list-item" href="${ctx}/u/nickname">
        <label class="list-label">昵称</label>
        <span class="list-text">
          <span>${user.nickname}</span>
          <i class="list-arrow"></i>
        </span>
      </a>
      <a class="list-item"<c:if test="${empty user.phone}"> href="${ctx}/u/bindPhone"</c:if><c:if test="${not empty user.phone}"> href="javascript:;"</c:if>>
        <label class="list-label">手机号</label>
        <span class="list-text">
        <c:if test="${empty user.phone}">
          <span>未绑定</span>
          <i class="list-arrow"></i>
        </c:if>
        <c:if test="${not empty user.phone}">
          <span>${user.phone}</span>
        </c:if>
        </span>
      </a>
      <a class="list-item" href="${ctx}/help/userRank">
        <label class="list-label">代理等级</label>
        <span class="list-text">
          <c:if test="${user.userRank == 'V1'}"><i class="icon icon-newbie"></i> <span>三级代理</span></c:if>
          <c:if test="${user.userRank == 'V2'}"><i class="icon icon-tongpai"></i> <span>二级代理</span></c:if>
          <c:if test="${user.userRank == 'V3'}"><i class="icon icon-yinnpai"></i> <span>一级代理</span></c:if>
          <c:if test="${user.userRank == 'V4'}"><i class="icon icon-jinpai"></i> <span>特级代理</span></c:if>
          <i class="list-arrow"></i>
        </span>
      </a>
    </div>

    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/portrait">
        <label class="list-label"><i class="icon icon-portrait"></i>详细资料</label>
        <span class="list-text">
          <span><c:if test="${!isCompletedPortrait}">未填写</c:if></span>
          <i class="list-arrow"></i>
        </span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/address">
        <label class="list-label"><i class="icon icon-address"></i>收货地址</label>
        <span class="list-text">
          <span><c:if test="${!hasAddress}">未填写</c:if></span>
          <i class="list-arrow"></i>
        </span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/bankCard">
        <label class="list-label"><i class="icon icon-appearance"></i>银行卡</label>
        <span class="list-text">
          <span><c:if test="${!hasBankCard}">未绑定</c:if></span>
          <i class="list-arrow"></i>
        </span>
      </a>
    </div>

  </article>

</body>

</html>
