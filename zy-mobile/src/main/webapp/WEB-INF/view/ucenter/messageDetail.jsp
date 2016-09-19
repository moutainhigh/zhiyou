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
<meta name="keywords" content="微信分销" />
<meta name="description" content="消息详情" />

<title>消息详情</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <h1>消息详情</h1>
    <a href="${ctx}/u/message" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article class="p-15">
    <h2 class="font-333 fs-16 text-center">${message.title}</h2>
    <div class="font-999 fs-12 lh-20 text-center">${message.createdTimeLabel}</div>
    <div class="font-555 fs-14 lh-24 mt-10">${message.content}</div>
  </article>
</body>
</html>
