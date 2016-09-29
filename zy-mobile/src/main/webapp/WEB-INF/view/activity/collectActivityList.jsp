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

<title>我关注的活动</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/activity.css" rel="stylesheet" />
<script>
  $(function() {
    
  });
</script>
</head>
<body class="activity-list">
  <header class="header">
    <h1>我关注的活动</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <c:if test="${empty activities && empty historyActivities}">
    <div class="page-empty">
      <i class="fa fa-flag"></i>
      <span>您没有关注活动</span>
    </div>
    </c:if>
  
    <c:if test="${not empty activities}">
    <div class="list-group">
      <c:forEach items="${activities}" var="activity">
      <a class="list-item" href="${ctx}/activity/${activity.id}">
        <div class="activity">
          <figure class="image-wrap abs-lt">
            <img src="${activity.imageThumbnail}">
          </figure>
          <h2>${activity.title}</h2>
          <div class="font-999 fs-12 lh-20">${activity.startTimeLabel} 开始</div>
          <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> ${activity.province} ${activity.city} ${activity.district}</div>
          <div class="fs-14 abs-rb mb-5">
            <c:if test="${activity.status == '报名中'}"><label class="label blue">报名中</label></c:if>
            <c:if test="${activity.status == '报名已结束'}"><label class="label gray">报名已结束</label></c:if>
            <c:if test="${activity.status == '进行中'}"><label class="label orange">进行中</label></c:if>
            <c:if test="${activity.status == '活动已结束'}"><label class="label gray">活动已结束</label></c:if>
          </div>
        </div>
      </a>
      </c:forEach>
    </div>
    </c:if>
    
    <c:if test="${not empty historyActivities}">
    <div class="list-group mb-15">
      <div class="list-title">已结束的活动</div>
      <c:forEach items="${historyActivities}" var="activity">
      <a class="list-item" href="${ctx}/activity/${activity.id}">
        <div class="activity">
          <figure class="image-wrap abs-lt">
            <img src="${activity.imageThumbnail}">
          </figure>
          <h2>${activity.title}</h2>
          <div class="font-999 fs-12 lh-20">${activity.startTimeLabel} 开始</div>
          <div class="font-999 fs-12 lh-20"><i class="fa fa-map-marker font-gray"></i> ${activity.province} ${activity.city} ${activity.district}</div>
          <div class="fs-14 abs-rb">
            <c:if test="${activity.status == '报名中'}"><label class="label blue">报名中</label></c:if>
            <c:if test="${activity.status == '报名已结束'}"><label class="label gray">报名已结束</label></c:if>
            <c:if test="${activity.status == '进行中'}"><label class="label orange">进行中</label></c:if>
            <c:if test="${activity.status == '活动已结束'}"><label class="label gray">活动已结束</label></c:if>
          </div>
        </div>
      </a>
      </c:forEach>
    </div>
    </c:if>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
