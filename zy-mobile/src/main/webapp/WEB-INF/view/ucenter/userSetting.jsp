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

<title>设置</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {
    $('#isDefault').click(function(event) {
      var $this = $(this);
      var checked = $this.is(':checked');

      $.ajax({
        url : '${ctx}/u/userSetting/modifyIsReceiveTaskSms',
        data : {
          isReceiveTaskSms : checked
        },
        type : 'POST',
        dataType : 'JSON',
        success : function(result) {
        },
        error : function() {
        }
      });
    });
  });
</script>
</head>
<body class="user-settings">

  <header class="header">
    <h1>设置</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">接受短信提醒</div>
        <div class="list-unit form-switch">
          <input type="checkbox" id="isDefault" name="isDefault" <c:if test="${userSetting.isReceiveTaskSms}"> checked="checked"</c:if>>
          <label class="i-switch" for="isDefault"></label>
        </div>
      </div>
      <div class="list-item">
        <div class="list-text">系统版本</div>
        <div class="list-unit">v1.1.8 Beta</div>
      </div>
    </div>

    <div class="form-btn">
      <a href="${ctx}/logout" class="btn red btn-block round-2">退出</a>
    </div>

  </article>

</body>

</html>
