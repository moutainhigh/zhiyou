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
  .body-bg{
    height: 100%; overflow: hidden;  min-height: 480px;
    background-image: url('http://state.zhi-you.net/image/tmp/bg.png'); 
    background-size: 100% auto;
    background-repeat: no-repeat;
    background-color: #faac0c;
  }
  .m-img {
    max-width: 600px; min-width: 320px;   
    margin: 0 auto;
    background: #f00;
  }
  .header-img{
    width: 90%;
    margin: 10% auto 5% auto;
  }
  .user-info{
    position: absolute; left: 0; top: -10px;
    width: 100%;
    text-align: center;
  }
  .user-head{
    width: 20%;  border-radius: 100%;
    border: solid 3px #fff;
  }
  .link{
    width: 40%;  margin: 0 auto;
  }
  .footer-img{
    bottom: 1%; left: 26%;
    width: 48%;  margin: 0 auto;
    max-width: 290px;
  }
</style>
</head>
<body class="body-bg">
    
    <div class="header-img relative block">
      <img src="${stccdn}/image/tmp/header.png" class="width-100p"/>
      <div class="user-info">
        <img class="user-head " src="http://image.zhi-you.net/avatar/ed673dc0-9e83-4e99-9206-1f33064dc099@240h_240w_1e_1c.jpg"/>
        <p class="width-100p fs-15">昵称昵称昵称</p>
      </div>
    </div>
    
    <a href="#">
      <img src="${stccdn}/image/tmp/enter_03.png" class="link block"/>
    </a>
    
    <img src="${stccdn}/image/tmp/footer.png" class="footer-img absolute block" />
  
</body>
</html>
 