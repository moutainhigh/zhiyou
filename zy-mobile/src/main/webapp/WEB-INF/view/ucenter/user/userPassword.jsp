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
<meta name="keywords" content="微信分销" />
<meta name="description" content="修改密码 " />

<title>修改密码</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/login.css" rel="stylesheet">
<script type="text/javascript">
	$(function() {
		$('#userPasswordForm').submit(function() {
			var oldPwd = $('#oldPwd').val();
			var newPwd = $('#newPwd').val();
			var rePwd = $('#rePwd').val();
			var passwordRegex = /^\w{6,32}$/;
			if (!oldPwd || !oldPwd.match(passwordRegex)) {
				$.messageError('旧密码有误!');
				return false;
			}
			if (!newPwd || !newPwd.match(passwordRegex)) {
				$.messageError('新密码有误!');
				return false;
			}
			if (!rePwd ) {
				$.messageError('请确认密码!');
				return false;
			}
			if (rePwd!=newPwd ) {
				$.messageError('密码不一致,请重新确认输入!');
				return false;
			}
		});

		$(".login-tip").slideDown();
		setTimeout(function() {
			$(".login-tip").slideUp();
		}, 3000);
		$(".fa-times-circle").click(function() {
			$(".login-tip").slideUp();
		});
	});
</script>
</head>
<body>
  
  <header class="header">
    <h1><a class="logo" href="${ctx}/"><img src="${stccdn}/image/logo.png" alt="微信分销"></a></h1>
    <a href="javascript:history.back();" class="icon-left fa fa-angle-left"></a>
  </header>

  <div class="page-title">
    <h2>修改密码</h2>
  </div>
  <section class="form-section login">
    <form id="userPasswordForm" action="${ctx}/u/password" method="post">
      <div class="form-group">
        <label class="input-icon fa fa-key" for="oldPwd"></label>
        <input id="oldPwd" name="oldPassword" type="password" placeholder="请输入当前密码" class="form-control input">
      </div>
      <div class="form-group">
        <label class="input-icon fa fa-key" for="newPwd"></label>
        <input id="newPwd" name="password" type="password" placeholder="请输入新密码" class="form-control input">
      </div>
      <div class="form-group">
        <label class="input-icon fa fa-key" for="rePwd"></label>
        <input id="rePwd" name="password" type="password" placeholder="请确认密码" class="form-control input">
      </div>
      <input id="btnLogin" class="btn-login btn green mt-10" type="submit" value="保 存">
    </form>
  </section>
   

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>

 
   
</html>
