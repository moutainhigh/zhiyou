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

<title>${hasPassword ? '修改' : '设置'}密码</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {
    $('#form').submit(function() {
      var passwordRegex = /^\w{6,32}$/;
      <c:if test="${hasPassword}">
      var oldPassword = $('#oldPassword').val();
      if (!oldPassword || !oldPassword.match(passwordRegex)) {
        $.messageError('旧密码有误!');
        return false;
      }
      </c:if>
      var password = $('#password').val();
      var password2 = $('#password2').val();
      if (!password || !password.match(passwordRegex)) {
        $.messageError('新密码有误!');
        return false;
      }
      if (!password2) {
        $.messageError('请输入确认密码!');
        return false;
      }
      if (password2 != password) {
        $.messageError('密码不一致,请重新确认输入!');
        return false;
      }
    });
  });
</script>
</head>
<body>

  <header class="header">
    <h1>${hasPassword ? '修改' : '设置'}密码</h1>
    <a href="${ctx}/u/info" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form id="form" action="${ctx}/u/password" method="post">
    <div class="list-group">
      <c:if test="${hasPassword}">
      <div class="list-item">
        <label class="list-label" for="oldPassword">旧密码</label>
        <div class="list-text">
          <input type="password" id="oldPassword" name="oldPassword" class="form-input" value="" placeholder="输入当前密码">
        </div>
      </div>
      </c:if>
      <div class="list-item">
        <label class="list-label" for="password">设置密码</label>
        <div class="list-text">
          <input type="password" id="password" name="password" class="form-input" value="" placeholder="输入要设置的密码">
        </div>
      </div>
      <div class="list-item">
        <label class="list-label" for="password2">确认密码</label>
        <div class="list-text">
          <input type="password" id="password2" name="password2" class="form-input" value="" placeholder="重新输入密码">
        </div>
      </div>
    </div>
    <div class="form-btn">
      <input class="btn orange btn-block round-2" type="submit" value="提 交">
    </div>
    </form>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
