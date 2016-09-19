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

<title>修改昵称</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script>
  $(function() {
    $('#form').submit(function() {
      if (!$('#nickname').val()) {
        messageFlash('请输入昵称');
        return false;
      }
    });
  });
</script>
</head>
<body>
  <header class="header">
    <h1>修改昵称</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <article>
    <form id="form" class="valid-form" action="${ctx}/u/nickname" method="post">
      <div class="list-group">
        <div class="list-item">
          <label class="list-label" for="nickname">昵称</label>
          <div class="list-text">
            <input type="text" id="nickname" name="nickname" class="form-input" value="${user.nickname}" placeholder="填写昵称">
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>
    </form>
  </article>

</body>
</html>
