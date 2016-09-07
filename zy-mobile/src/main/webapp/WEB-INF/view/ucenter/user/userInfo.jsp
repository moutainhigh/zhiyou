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
        <div class="list-text">头像</div>
        <div class="list-unit">
          <img class="image-60 round bd" src="${userAvatarSmall}">
        </div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item" href="${ctx}/u/nickname">
        <div class="list-text">昵称</div>
        <div class="list-unit">${user.nickname}</div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item"<c:if test="${empty user.phone}"> href="${ctx}/u/bindPhone"</c:if><c:if test="${not empty user.phone}"> href="javascript:;"</c:if>>
        <div class="list-text">手机号</div>
        <c:if test="${empty user.phone}">
        <div class="list-unit">未绑定</div>
        <i class="list-arrow"></i>
        </c:if>
        <c:if test="${not empty user.phone}">
        <div class="list-unit">${user.phone}</div>
        </c:if>
      </a>
      <a class="list-item" href="${ctx}/help/userRank">
        <div class="list-text">代理等级</div>
        <div class="list-unit">
          <c:if test="${user.userRank == 'V1'}"><i class="icon icon-newbie"></i>三级代理</c:if>
          <c:if test="${user.userRank == 'V2'}"><i class="icon icon-tongpai"></i>二级代理</c:if>
          <c:if test="${user.userRank == 'V3'}"><i class="icon icon-yinnpai"></i>一级代理</span></c:if>
          <c:if test="${user.userRank == 'V4'}"><i class="icon icon-jinpai"></i>特级代理</c:if>
        </div>
        <i class="list-arrow"></i>
      </a>
    </div>

    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/portrait">
        <i class="list-icon icon icon-portrait"></i>
        <div class="list-text">个人资料</div>
        <div class="list-unit"><c:if test="${!isCompletedPortrait}">未填写</c:if></div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/appearance">
        <i class="list-icon icon icon-appearance"></i>
        <div class="list-text">实名认证</div>
        <div class="list-unit"><c:if test="${!isCompletedAppearance}">未认证</c:if></div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/address">
        <i class="list-icon icon icon-address"></i>
        <div class="list-text">收货地址</div>
        <div class="list-unit"><c:if test="${!hasAddress}">未填写</c:if></div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/bankCard">
        <i class="list-icon icon icon icon-card"></i>
        <div class="list-text">银行卡</div>
        <div class="list-unit"><c:if test="${!hasBankCard}">未绑定</c:if></div>
        <i class="list-arrow"></i>
      </a>
    </div>

  </article>

</body>

</html>
