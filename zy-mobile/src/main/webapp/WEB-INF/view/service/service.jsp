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

<title>帮助中心</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/service.css" />
<script type="text/javascript">

</script>
</head>
<body class="header-fixed footer-fixed">

  <header class="header">
    <h1>帮助中心</h1>
    <a href="javascript:history.back(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="service-contact flex mb-15 bd-b">
    <a class="flex-1 bd-r" href="https://static.meiqia.com/dist/standalone.html?eid=32013">
      <i class="fa fa-headphones font-orange"></i>
      <span>在线客服</span>
    </a>
    <a class="flex-1" href="tel:18017676668">
      <i class="fa fa-phone font-blue"></i>
      <span>服务热线</span>
    </a>
  </article>
  
  <article>
    <div class="list-group">
      <c:forEach items="${helpCategories}" var="helpCategory">
      <a class="list-item" href="${ctx}/help/${helpCategory.code}">
        <label class="list-text">${helpCategory.name}</label>
        <i class="list-arrow"></i>
      </a>
      </c:forEach>
    </div>
    
  </article>

</body>
</html>
