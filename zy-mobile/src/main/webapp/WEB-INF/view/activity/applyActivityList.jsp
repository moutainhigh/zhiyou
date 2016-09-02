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

<title>我参与的活动</title>
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
    <h1>线下活动</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="javascript" class="button-right"><i class="fa fa-edit"></i></a>
  </header>

  <article>
    <div class="list-group mb-15">
      <c:forEach items="${activities}" var="activity">
      <a class="list-item activity" href="${ctx}/activity/${activity.id}">
        <figure class="image-wrap">
          <img src="${activity.imageThumbnail}">
        </figure>
        <h2>${activity.title}</h2>
        <div class="font-999 fs-12 lh-20">${activity.startTimeLabel} 开始</div>
        <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> ${activity.province} ${activity.city} ${activity.district}</div>
        <div class="fs-14 abs-rb mr-15 mb-15">
          <c:if test="${activity.status == '报名中'}"><label class="label blue">报名中</label></c:if>
          <c:if test="${activity.status == '报名已结束'}"><label class="label gray">报名已结束</label></c:if>
          <c:if test="${activity.status == '进行中'}"><label class="label orange">进行中</label></c:if>
          <c:if test="${activity.status == '活动已结束'}"><label class="label gray">活动已结束</label></c:if>
        </div>
      </a>
      </c:forEach>
    </div>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
