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

<title>微信分销-游客</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/index.css" />
<script type="text/javascript">

	$(function() {
		
	});

</script>
</head>
<body class="header-fixed footer-fixed">

  <header class="header">
    <h1>游客</h1>
    <a href="${ctx}/u" class="button-left">
      <i class="fa fa-bell-o"></i>
      <!-- <em class="badge badge-success">1</em> -->
    </a>
    <a href="${ctx}/u" class="button-right"><i class="fa fa-cog"></i></a>
  </header>
  
  <article class="user-profile">
    <a class="avatar-wrap" href="${ctx}/u">
      <img class="avatar round" src="${stccdn}/image/avatar_default.jpg">
    </a>
    <a class="user-info block" href="${ctx}/u">
      <div class="fs-18 mt-15">登录/注册</div>
      <i class="icon-right fa fa-angle-right"></i>
    </a>
  </article>
  
  <aside class="water">
    <div class="water-1"></div>
    <div class="water-2"></div>
  </aside>
  
  <article class="user-currency flex">
    <a class="flex-1 bd-r" href="${ctx}/u">
      <i class="icon icon-money icon-2x"></i>
      <em>余额</em>
      <span></span>
    </a>
    <a class="flex-1" href="${ctx}/u">
      <i class="icon icon-point icon-2x"></i>
      <em>积分</em>
      <span></span>
    </a>
  </article>
  
  <article>
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u">
        <label class="list-label"><i class="icon icon-account"></i>成为代理</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u">
        <label class="list-label"><i class="icon icon-account"></i>我的团队</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
    </div>
    
    <div class="list-group">
      <a class="list-item list-item-icon" href="${ctx}/u">
        <label class="list-label"><i class="icon icon-coupon"></i>我的订单</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="#">
        <label class="list-label"><i class="icon icon-task"></i>我的活动</label>
        <span class="list-text"><i class="list-arrow"></i></span>
      </a>
      <a class="list-item list-item-icon" href="${ctx}/u">
        <label class="list-label"><i class="icon icon-achievement"></i>检测报告</label>
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
