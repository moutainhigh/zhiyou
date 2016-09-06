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
<link rel="stylesheet" href="${stccdn}/css/ucenter/currency.css" />
<script type="text/javascript">
  $(function() {
    if (!$('.list-more').hasClass('disabled')) {
      $('.list-more').click(loadMore);
    }
  });

  var timeLT = '${timeLT}';
  var pageNumber = 0;

  function loadMore() {
    $.ajax({
      url : '${ctx}/u/money/log',
      data : {
        pageNumber : pageNumber + 1,
        timeLT : timeLT
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        var page = result.data.page;
        if (page.data.length) {
          timeLT = result.data.timeLT;
          pageNumber = page.pageNumber;
          var pageData = page.data;
          for ( var i in pageData) {
            var item = pageData[i];
            var html = '<div class="list-item">' + '<div class="title lh-24 text-ellipsis">' + item.title + '</div>' + '<div class="inout '
                + (item.inOut == '收入' ? 'currency-in' : 'currency-out') + ' lh-24 text-right">' + item.transAmount.toFixed(2) + '</div>' + '<div class="clearfix lh-24">'
                + '<span class="left fs-12 font-999">' + item.transTimeLabel + '</span>' + '<span class="right fs-12 font-999">余额: ' + item.afterAmount.toFixed(2) + '</span>'
                + '</div></div>';
            $(html).insertBefore($('.list-more'));
          }
        }
        if (!page.data.length || page.data.length < page.pageSize) {
          $('.list-more').addClass('disabled').text('没有更多数据了').unbind('click', loadMore);
        }
      }
    });
  }
</script>
</head>
 
<body class="log-list header-fixed">

  <header class="header">
    <h1>本金记录</h1>
    <a href="${ctx}/u/money" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article>
    <c:if test="${empty page.data}">
      <div class="empty-tip">
        <i class="fa fa-file-o"></i>
        <span>暂无记录</span>
      </div>
    </c:if>
    
    <c:if test="${not empty page.data}">
    <div class="list-group">
    	
      <c:forEach items="${page.data}" var="accountLog">
      <div class="list-item">
        <div class="log-item">
          <div class="title lh-24 text-ellipsis">${accountLog.title}</div>
          <div class="inout<c:if test="${accountLog.inOut == '支出'}"> currency-out</c:if><c:if test="${accountLog.inOut == '收入'}"> currency-in</c:if> lh-24 text-right">${accountLog.transAmount}</div>
          <div class="clearfix lh-24">
            <span class="left fs-12 font-999">${accountLog.transTimeLabel}</span>
            <span class="right fs-12 font-999">余额: ${accountLog.afterAmount}</span>
          </div>
        </div>
      </div>
      </c:forEach>
      
      <c:if test="${page.total > page.pageSize}">
      <a class="list-item list-more" href="javascript:;">
        <div class="text-center">点击加载更多</div>
      </a>
      </c:if>
      <c:if test="${page.total <= page.pageSize}">
      <a class="list-item list-more disabled" href="javascript:;">
        <div class="text-center">没有更多数据了</div>
      </a>
      </c:if>
    </div>
  	</c:if>
    
  </article>

</body>
   
</html>
