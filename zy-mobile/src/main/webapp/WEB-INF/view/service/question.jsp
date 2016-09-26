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
<meta name="keywords" content="微信分销">
<meta name="description" content="帮助中心 - ${helpCategoryName}">

<title>${helpCategoryName}</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/service.css" />
<script type="text/javascript">

</script>
</head>
<body class="header-fixed footer-fixed">

  <header class="header">
    <h1>${helpCategoryName}</h1>
    <a href="javascript:history.back(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="service-question">
  	<c:forEach items="${helps}" var="help" varStatus="varStatus">
    <div class="qa">
      <h3 id="q${varStatus.index + 1}">Q${varStatus.index + 1}: ${help.title}</h3>
      <p>${help.content}</p>
    </div>
    </c:forEach>
  </article>
  
</body>
</html>
