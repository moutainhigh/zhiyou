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
<script type="text/javascript">
	$(function() {
		
	});
</script>
</head>
 
<body class="header-fixed">

  <header class="header">
    <h1>活动报名成功</h1>
    <a href="${ctx}/avtivity/${activity.id}" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="mt-30">
      <i class="icon icon-clock icon-6x block center"></i>
      <h2 class="font-black fs-16 lh-30 text-center mt-20 mb-20">您已经成功报名活动</h2>
      <div class="list-group pl-15 pr-15 pt-10 pb-10">
        <p class="clearfix fs-14 lh-30"><span class="left font-999">活动时间</span><span class="right font-black">${activity.startTimeLabel} 至 ${activity.endTimeLabel}</span></p>
        <p class="clearfix fs-14 lh-30"><span class="left font-999">活动地点</span><span class="right font-black">${activity.province} ${activity.city} ${activity.district} ${activity.address}</span></p>
      </div>
      <div class="form-btn mt-30">
         <a href="${ctx}/avtivity/${activity.id}" class="btn green btn-block round-4">确 定</a>
      </div>
  </article>
  
</body>
   
</html>
