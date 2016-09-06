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

<title>签到失败</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {

  });
</script>
<style>
  * { margin: 0; outline: 0; padding: 0; font-size: 100%; }
  html { height: 100%;  }
  .m-img {
    max-width: 420px; min-width: 320px;  min-height: 480px;
    margin: 0 auto;background: #78C8B4;
  }
  .header-img{
    top: 20px; left: 20%;
    width: 60%;
  }
  .user-head{
    top: 20px; left: 40%;
    width: 20%;
    border-radius: 100%;
  }
  .name{
   top: 0px; left: 0;margin-top: 32%;
    text-align: center;
  }
  .link{
    left: 40%;bottom: 22%;
    width: 20%;
    margin: 0 auto;
  }
  .footer-img{
     bottom: 10px; left: 29%;
    width: 42%;
    margin: 0 auto;
  }
  .middle{
   top: 0px; left: 0;
    margin: 0 auto;
  }
  .middle1{
    top: 43%; left: 37%;
    width: 28%;
    margin: 0 auto;
  }
</style>
</head>
<body>
    <div class="m-img relative size-100p o-hidden">
      <img src="${stccdn}/image/tmp/middle.png" class="middle absolute block width-100p"/>
      <img src="${stccdn}/image/tmp/middle1.png" class="middle1 absolute block" />
      <div class="header-img absolute block">
        <img class="user-head absolute" src="${stccdn}/image/tmp/3.jpg"/>
        <p class="name absolute width-100p fs-12">昵称昵称昵称</p>
      </div>
        <a href="#">
            <img src="${stccdn}/image/tmp/sign.png" class="link absolute block"/>
        </a>
      <img src="${stccdn}/image/tmp/logo.png" class="footer-img absolute block" />
    </div>
</body>
</html>

