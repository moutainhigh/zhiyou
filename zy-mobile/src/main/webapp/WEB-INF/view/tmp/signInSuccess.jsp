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

<title>签到成功</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {
  });
</script>
<style>
html, body {
  width: 100%; height: 100%;
}
body {
  background: #78C8B4 url(${stccdn}/image/tmp/success_background.png); background-size: cover;
}
.codesuccess-info {
  left: 43%; top: 5%; width: 14%; height: 8%;
}
.codesuccess-name {
  left: 33%; top: 14%; width: 34%; height: 5%; color: #FAAC0C; text-align: center;
}
</style>
</head>
<body class="relative">
  <img src="${stccdn}/image/tmp/success.png" class="size-100p">
  <img src="${stccdn}/image/tmp/success_enter.png" class="size-100p abs-lt">
  <img src="${stccdn}/image/tmp/success_footer.png" class="size-100p abs-lb">
  <a class="codesuccess-info absolute block">
    <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg" class="block round size-100p">
  </a>
  <span class="codesuccess-name absolute">昵称昵称</span>
</body>
</html>

