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

<title>新闻资讯</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/dcss.css" rel="stylesheet" />
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <h1>新闻资讯</h1>
    <a href="${ctx}/article" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <div class="detail-wrap dcss">
      <h2 class="font-333 fs-18 lh-30">${article.title}</h2>
      <div class="mt-10 font-999 fs-14 lh-20">${article.author} &nbsp;&nbsp; ${article.releasedTimeLabel}</div>
      <img class="mt-10" src="${article.imageBig}">
      ${article.content}
      <div class="text-right"><i class="fa fa-eye font-999"></i> <span class="font-777">${article.visitCount}</span></div>
    </div>
  </article>
</body>
</html>
