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
  .m-img {
    max-width: 557px; min-width: 320px;  min-height: 480px; margin: 0 auto;
  }
  .header-img{
    top: 20px; left: 20%;  width: 60%;
  }
  .user-head{
    top: 0px; left: 40%;  width: 20%;  border-radius: 100%;
  }
  .name{
     top: 24%; left: 0;  text-align: center;
  }
  .link{
    left: 30%;bottom: 40%;
    width: 40%;  margin: 0 auto;
  }
  .footer-img{
     bottom: 10px; left: 26%;
    width: 48%;  margin: 0 auto;
  }
</style>
</head>
<body class="height-100p">
  <div class="m-img relative size-100p o-hidden">
    <img src="${stccdn}/image/tmp/bg.png" class="size-100p" />
    <div class="header-img absolute block">
      <img src="${stccdn}/image/tmp/header.png" class="width-100p"/>
      <img class="user-head absolute" src="${stccdn}/image/tmp/3.jpg"/>
      <p class="name absolute width-100p fs-12">昵称昵称昵称</p>
    </div>
    <a href="#">
      <img src="${stccdn}/image/tmp/enter_03.png" class="link absolute block"/>
    </a>
    <img src="${stccdn}/image/tmp/footer.png" class="footer-img absolute block" />
  </div>
</body>
</html>

