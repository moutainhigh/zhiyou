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
<body>

  <header class="header">
    <h1>我的信息</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article class="user-info">
    <div class="list-group mt-10">
      <div class="list-title">基本信息</div>
      <a class="list-item" href="${ctx}/u/avatar">
        <div class="list-text">头像</div>
        <div class="list-unit">
          <img class="image-60 round bd" src="${user.avatarThumbnail}">
        </div>
        <i class="list-arrow"></i>
      </a>
      <c:if test="${isUserInfoCompleted}">
        <a class="list-item" href="javascript:;">
          <div class="list-text">昵称</div>
          <div class="list-unit">${user.nickname}</div>
        </a>
      </c:if>
      <c:if test="${!isUserInfoCompleted}">
        <a class="list-item" href="${ctx}/u/nickname">
          <div class="list-text">昵称</div>
          <div class="list-unit">${user.nickname}</div>
          <i class="list-arrow"></i>
        </a>
      </c:if>
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
        <div class="list-text">服务商等级</div>
        <div class="list-unit">
          <c:if test="${user.userRank == 'V1'}"><label class="label purple">三级服务商</label></c:if>
          <c:if test="${user.userRank == 'V2'}"><label class="label blue">二级服务商</label></c:if>
          <c:if test="${user.userRank == 'V3'}"><label class="label orange">一级服务商</label></c:if>
          <c:if test="${user.userRank == 'V4'}"><label class="label red">特级服务商</label></c:if>
        </div>
        <i class="list-arrow"></i>
      </a>
      <c:if test="${user.userRank != 'V0'}">
      <a class="list-item" href="${ctx}/code?userId=${user.id}">
        <div class="list-text">我的授权码</div>
        <div class="list-unit"><c:if test="${empty code}">点击查看</c:if><c:if test="${not empty code}">${code}</c:if></div>
      </a>
      </c:if>
    </div>

    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/password">
        <i class="list-icon icon icon icon-key"></i>
        <div class="list-text">${hasPassword ? '修改' : '设置'}密码</div>
        <div class="list-unit"><c:if test="${!hasPassword}">未设置</c:if></div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/userInfo">
        <i class="list-icon icon icon-idcard"></i>
        <div class="list-text">实名认证</div>
        <div class="list-unit"><c:if test="${!isUserInfoCompleted}">未认证</c:if></div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/address">
        <i class="list-icon icon icon-address"></i>
        <div class="list-text">收货地址</div>
        <div class="list-unit"><c:if test="${!hasAddress}">未填写</c:if></div>
        <i class="list-arrow"></i>
      </a>
      <%--
      <a class="list-item list-item-icon" href="${ctx}/u/bankCard">
        <i class="list-icon icon icon icon-card"></i>
        <div class="list-text">银行卡</div>
        <div class="list-unit"><c:if test="${!hasBankCard}">未绑定</c:if></div>
        <i class="list-arrow"></i>
      </a>
      --%>
    </div>

  </article>

</body>

</html>
