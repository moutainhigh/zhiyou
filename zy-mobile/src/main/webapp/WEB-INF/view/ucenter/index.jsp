<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="3" />

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
<meta name="keywords" content="微信分销">

<title>微信分销-我的</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/index.css" />
<script type="text/javascript">

	$(function() {
		<%--
		$.message({
			content: '测试测试',
			type: 'info',
			callback: function() {
			}
		});
        --%>
	});

</script>
</head>
<body class="header-fixed footer-fixed">

  <header class="header">
    <h1>我的</h1>
    <a href="${ctx}/u/message" class="button-left">
      <i class="fa fa-bell-o"></i>
      <c:if test="${unreadMessageCount > 0}">
      <em class="badge badge-success">${unreadMessageCount}</em>
      </c:if>
    </a>
    <a href="${ctx}/u/userSetting" class="button-right"><i class="fa fa-cog"></i></a>
  </header>
  
  <article class="user-profile">
    <a class="avatar-wrap" href="${ctx}/u/userInfo">
      <img class="avatar round" src="${userAvatarSmall}">
    </a>
    <a class="user-info block" href="${ctx}/u/userInfo">
      <div class="nickname">${user.nickname} <c:if test="${user.userRank == 'V1'}"><i class="icon icon-newbie"></i></c:if>
      <c:if test="${user.userRank == 'V2'}"><i class="icon icon-tongpai"></i></c:if>
      <c:if test="${user.userRank == 'V3'}"><i class="icon icon-yinnpai"></i></c:if>
      <c:if test="${user.userRank == 'V4'}"><i class="icon icon-jinpai"></i></c:if></div>
      <i class="icon-right fa fa-angle-right"></i>
    </a>
  </article>
  
  <aside class="water">
    <div class="water-1"></div>
    <div class="water-2"></div>
  </aside>
  
  <article>
    <div class="list-group">
      <c:if test="${user.userRank == 'V0'}">
      <a class="list-item list-item-icon" href="${ctx}/product?isFirst=true">
        <label class="list-label"><i class="icon icon-account"></i>成为代理</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      </c:if>
      <c:if test="${user.userRank != 'V0'}">
      <a class="list-item list-item-icon" href="${ctx}/u/invite">
        <label class="list-label"><i class="icon icon-account"></i>我的团队</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      </c:if>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/order/in">
        <label class="list-label"><i class="icon icon-coupon"></i>收到的订单</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/order/out">
        <label class="list-label"><i class="icon icon-coupon"></i>发出的订单</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/report">
        <label class="list-label"><i class="icon icon-achievement"></i>检测报告</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/activity/applyList">
        <label class="list-label"><i class="icon icon-task"></i>我参与的活动</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/activity/collectList">
        <label class="list-label"><i class="icon icon-task"></i>我关注的活动</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/help">
        <label class="list-label"><i class="icon icon-service"></i>服务中心</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/about">
        <label class="list-label"><i class="icon icon-about"></i>关于优检一生</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
    </div>
    
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
