<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
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

<title>${sys}- 登录</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {
    $('.valid-form').submit(function() {
      var phone = $('#phone').val();
      var password = $('#password').val();
      if (!phone) {
        messageFlash('请输入手机号');
        return false;
      }
      if (!password) {
        messageFlash('请输入密码');
        return false;
      }
    });
  });
</script>
</head>
<body>
  <header class="header">
    <h1>登录</h1>
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <form class="valid-form" action="${ctx}/login" method="post">
    <div class="list-group mt-15">
      <div class="list-item">
        <i class="list-icon fa fa-mobile fs-20"></i>
        <div class="list-text">
          <input type="text" id="phone" name="phone" class="form-input" value="${phone}" placeholder="输入手机号">
        </div>
      </div>
      <div class="list-item">
        <i class="list-icon fa fa-key"></i>
        <div class="list-text">
          <input type="password" id="password" name="password" class="form-input" value="" placeholder="输入密码">
        </div>
      </div>
    </div>

    <div class="form-btn">
      <input class="btn green btn-block round-2" type="submit" value="登 陆">
    </div>
  </form>
</body>
</html>
