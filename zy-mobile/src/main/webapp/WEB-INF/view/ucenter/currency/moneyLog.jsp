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

<title>本金记录</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
</script>
</head>
 
<body class="log-list header-fixed">

  <header class="header">
    <h1>本金记录</h1>
    <a href="${ctx}/u/money" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article>
    <c:if test="${empty accountLogs}">
      <div class="empty-tip">
        <i class="fa fa-file-o"></i>
        <span>暂无记录</span>
      </div>
    </c:if>
    
    <c:if test="${not empty accountLogs}">
    <div class="list-group mb-0">
      
      <c:forEach items="${accountLogs}" var="accountLog">
      <div class="list-item">
        <div class="list-text pl-5">
          <div class="fs-14">${accountLog.title}</div>
          <div class="fs-12 font-999">${accountLog.transTimeLabel}</div>
        </div>
        <div class="list-unit width-100 text-right">
          <div class="<c:if test="${accountLog.inOut == '支出'}"> currency-out</c:if><c:if test="${accountLog.inOut == '收入'}"> currency-in</c:if>">${accountLog.transAmount}</div>
          <div class="fs-12 font-999">余额: ${accountLog.afterAmount}</div>
        </div>
      </div>
      </c:forEach>
    </div>
    </c:if>
    
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
