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

<title>收入 - 订单收款</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/pageload.jsp"%>
<script type="text/javascript">
  function getUrl() {
    return '${ctx}/u/account/in?type=${type}';
  }
  
  function buildRow(row) {
    var html = '<div class="list-item">' 
             +   '<div class="list-text pl-5">' 
             +     '<div class="fs-14">' + row.title + '</div>'
             +     '<div class="fs-12 font-999">' + row.transTimeLabel + '</div>' 
             +   '</div>' 
             +   '<div class="list-unit width-100 text-right">' 
             +     '<div class="' + (row.inOut == '收入' ? 'currency-in' : 'currency-out') + '">' + row.transAmount.toFixed(2) + '</div>' 
             +     '<div class="fs-12 font-999">余额: ' + row.afterAmount.toFixed(2) + '</div>' 
             +   '</div>' 
             + '</div>';
    return html;
  }
</script>
</head>
 
<body class="account-list">
  <article class="page-wrap">
    <header class="header">
      <h1>收入 - ${title}</h1>
      <a href="${ctx}/u/account" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
  
    <div class="page-inner">
      <c:if test="${empty page.data}">
        <div class="page-empty">
          <i class="fa fa-file-o"></i>
          <span>暂无记录</span>
        </div>
      </c:if>
      
      <div class="page-list list-group mb-0">
        <c:forEach items="${page.data}" var="accountLog">
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
      
      <c:if test="${page.total <= page.pageSize}">
        <div class="page-more disabled"><span>没有更多数据了</span></div>
      </c:if>
    </div>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
