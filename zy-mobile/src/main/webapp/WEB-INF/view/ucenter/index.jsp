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

<title>${sys} - 我的</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/index.css" />
</head>
<body class="footer-fixed">

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
  
  <div class="list-group mb-0">
    <a class="list-item user-profile bd-0" href="${ctx}/u/info">
      <img class="avatar round mr-20 ml-10" src="${userAvatarSmall}">
      <div class="list-text font-white lh-30">
        <div class="fs-18 bold">${user.nickname}</div>
        <c:if test="${user.userRank == 'V1'}"><label class="label purple">VIP服务商</label></c:if>
        <c:if test="${user.userRank == 'V2'}"><label class="label blue">市级服务商</label></c:if>
        <c:if test="${user.userRank == 'V3'}"><label class="label orange">省级服务商</label></c:if>
        <c:if test="${user.userRank == 'V4'}"><label class="label red">特级服务商</label></c:if>
      </div>
      <i class="list-arrow"></i>
    </a>
  </div>
  
  <aside class="water">
    <div class="water-1"></div>
    <div class="water-2"></div>
  </aside>
  
  <div class="user-account flex">
    <a class="flex-1 bd-r" href="${ctx}/u/account">
      <i class="icon icon-money icon-2x"></i>
      <em>积分钱包</em>
      <span>¥${money}</span>
    </a>
    <a class="flex-1 bd-r" href="${ctx}/u/order/in">
      <i class="icon icon-in icon-2x"></i>
      <em>订单</em>
      <span>${inSumQuantity}次</span>
    </a>
    <a class="flex-1" href="${ctx}/u/order/out">
      <i class="icon icon-out icon-2x"></i>
      <em>服务单</em>
      <span>${outSumQuantity}次</span>
    </a>
  </div>
  
  <article>
    <div class="list-group">
      <c:if test="${user.userRank == 'V0'}">
      <a class="list-item" href="${ctx}/u/agent">
        <i class="list-icon icon icon-agent"></i>
        <div class="list-text">成为服务商</div>
        <i class="list-arrow"></i>
      </a>
      </c:if>
      <c:if test="${user.userRank == 'V1' || user.userRank == 'V2'}">
      <a class="list-item" href="${ctx}/u/agent">
        <i class="list-icon icon icon-upgrade"></i>
        <div class="list-text">升级服务商</div>
        <i class="list-arrow"></i>
      </a>
      </c:if>
      <c:if test="${user.userRank != 'V0'}">
      <a class="list-item list-item-icon" href="${ctx}/u/team">
        <i class="list-icon icon icon-users"></i>
        <div class="list-text">我的团队</div>
        <i class="list-arrow"></i>
      </a>
      </c:if>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/activity/applyList">
        <i class="list-icon icon icon-join"></i>
        <div class="list-text">报名的活动</div>
        <i class="list-arrow"></i>
      </a>
      <%--<a class="list-item list-item-icon" href="${ctx}/u/activity/payer">--%>
        <%--<i class="list-icon icon icon-money"></i>--%>
        <%--<div class="list-text">代付款活动</div>--%>
        <%--<i class="list-arrow"></i>--%>
      <%--</a>--%>
      <a class="list-item list-item-icon" href="${ctx}/u/activity/collectList">
        <i class="list-icon icon icon-heart"></i>
        <div class="list-text">关注的活动</div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/order/ticket">
        <i class="list-icon icon icon-heart"></i>
        <div class="list-text">票务管理</div>
        <i class="list-arrow"></i>
      </a>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u/report">
        <i class="list-icon icon icon-report"></i>
        <div class="list-text">检测报告</div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u/policy">
        <i class="list-icon icon icon-policy"></i>
        <div class="list-text">保险</div>
        <i class="list-arrow"></i>
      </a>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/help">
        <i class="list-icon icon icon-service"></i>
        <div class="list-text">服务中心</div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/about">
        <i class="list-icon icon icon-about"></i>
        <div class="list-text">关于${sys}</div>
        <i class="list-arrow"></i>
      </a>
    </div>
    
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
