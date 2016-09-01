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
<meta name="description" content="404错误">

<title>404错误</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/error.css" rel="stylesheet" />
</head>

<body class="footer-fixed">
  <header class="header">
    <h1>出错啦</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <section class="error">
    <i class="error-icon font-orange fa fa-exclamation-triangle"></i>
    <div class="error-status">404</div>
    <div class="error-info">
      <p>抱歉！页面无法访问…</p>
      <p>请检查您输入的页面地址是否正确</p>
    </div>
    <div class="error-btn">
      <a class="btn green" href="javascript:history.go(-1);">返回上一页</a>
    </div>
  </section>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>