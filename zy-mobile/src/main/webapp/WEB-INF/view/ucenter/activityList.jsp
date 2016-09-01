<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="2" />

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

<title>线下活动</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/activity.css" rel="stylesheet" />
<script>
  $(function() {
    $('.tab-nav > a').tabs('.tab-content');
  });
</script>
</head>
<body class="activity-list footer-fixed">
  <header class="header">
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
    <h1>线下活动</h1>
  </header>

  <article>
    <div class="list-group mb-15">
      <a class="list-item activity" href="${ctx}/activity/1">
        <figure class="image-wrap">
          <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg">
        </figure>
        <h2>网赢研习社《创新网络营销总裁班》9-17杭州 免费报名中</h2>
        <div class="font-999 fs-12 lh-20">09-03 14:00 开始</div>
        <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> 徐家汇教堂</div>
        <div class="font-orange fs-16 abs-rb mr-15 mb-15">¥ 108.00</div>
      </a>
      
      <a class="list-item activity" href="${ctx}/activity/1">
        <figure class="image-wrap">
          <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg">
        </figure>
        <h2>网赢研习社《创新网络营销总裁班》9-17杭州 免费报名中</h2>
        <div class="font-999 fs-12 lh-20">09-03 14:00 开始</div>
        <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> 徐家汇教堂</div>
        <div class="font-orange fs-16 abs-rb mr-15 mb-15">免费</div>
      </a>
      
      <a class="list-item activity" href="${ctx}/activity/1">
        <figure class="image-wrap">
          <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg">
        </figure>
        <h2>网赢研习社《创新网络营销总裁班》9-17杭州 免费报名中</h2>
        <div class="font-999 fs-12 lh-20">09-03 14:00 开始</div>
        <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> 徐家汇教堂</div>
        <div class="font-orange fs-16 abs-rb mr-15 mb-15">免费</div>
      </a>
      
      <a class="list-item activity" href="${ctx}/activity/1">
        <figure class="image-wrap">
          <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg">
        </figure>
        <h2>网赢研习社《创新网络营销总裁班》9-17杭州 免费报名中</h2>
        <div class="font-999 fs-12 lh-20">09-03 14:00 开始</div>
        <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> 徐家汇教堂</div>
        <div class="font-orange fs-16 abs-rb mr-15 mb-15">免费</div>
      </a>
      
      <a class="list-item activity" href="${ctx}/activity/1">
        <figure class="image-wrap">
          <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg">
        </figure>
        <h2>网赢研习社《创新网络营销总裁班》9-17杭州 免费报名中</h2>
        <div class="font-999 fs-12 lh-20">09-03 14:00 开始</div>
        <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> 徐家汇教堂</div>
        <div class="font-orange fs-16 abs-rb mr-15 mb-15">¥ 108.00</div>
      </a>
    </div>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
