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

<title>新闻资讯</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/article.css" rel="stylesheet" />

</head>
<body class="article-list">

  <header class="header">
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
    <h1>新闻资讯</h1>
  </header>

  <article class="mb-15 clearfix">
    <c:forEach items="${articles}" var="article">
    <a href="${ctx}/article/${article.id}" class="article">
      <div class="image-box">
        <img class="abs-lt" src="${article.imageThumbnail}">
      </div>
      <h3>${article.title}</h3>
      <div class="article-info">
        <span class="fs-13 font-777">¥ ${article.releasedTimeLabel}</span>
      </div>
    </a>
    </c:forEach>
  </article>

</body>
</html>
