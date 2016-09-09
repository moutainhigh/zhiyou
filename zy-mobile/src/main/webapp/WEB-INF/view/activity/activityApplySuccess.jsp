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

<title>活动报名成功</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
</head>

<body>

  <header class="header">
    <h1>活动报名成功</h1>
    <a href="${ctx}/activity/${activity.id}" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <img class="image-80 bd round block mt-30 center" src="${user.avatarThumbnail}">
    <h2 class="font-555 fs-16 lh-30 text-center mt-30 mb-20">恭喜您，报名成功！</h2>
    <div class="list-group pl-15 pr-15 pt-10 pb-10">
      <h2 class="font-333 text-center lh-60">${activity.title}</h2>
      <div class="font-999 fs-14 lh-24"><i class="fa fa-clock-o"></i> 活动时间</div>
      <p class="font-555 fs-14 lh-30">${activity.startTimeLabel} 至 ${activity.endTimeLabel}</p>
      <div class="font-999 fs-14 lh-24"><i class="fa fa-map-marker"></i> 活动地点</div>
      <p class="font-555 fs-14 lh-30">${activity.province}${activity.city}${activity.district}${activity.address}</p>
    </div>
    <div class="form-btn mt-30">
      <a href="javascript:history.back();" class="btn green btn-block round-2">确 定</a>
    </div>
  </article>

</body>

</html>
