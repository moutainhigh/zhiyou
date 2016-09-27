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
<link href="${stccdn}/css/signIn.css" rel="stylesheet">
<script type="text/javascript">
  $(function() {

  });
</script>
 
</head>
<body class="body-bg signInFail pb-15">
    
    <div style="margin-top: 5%;">
      <div class="width-100p text-center">
        <img class="user-head " src="${user.avatarThumbnail}"/>
        <p class="width-100p fs-16 font-white mt-5">${user.nickname}</p>
      </div>
    </div>
    
    <img src="${stccdn}/image/signIn/failImg.png" class="width-50p block center" style="margin-top: 33%;" />
    
    <a href="${ctx}/activity/${id}">
      <img src="${stccdn}/image/signIn/sign.png" class="width-25p block center" style="margin-top: 5%;"/>
    </a>
    
    <img src="${stccdn}/image/signIn/footer.png" class="block center" style="width: 40%; margin-top: 50px;" />
</body>
</html>
 
