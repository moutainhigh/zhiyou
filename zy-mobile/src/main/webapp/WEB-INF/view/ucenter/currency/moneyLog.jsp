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
  $(function() {
    if (!$('.list-more').hasClass('disabled')) {
      $('.list-more').click(loadMore);
    }
  });
  
  var url = '${ctx}/u/money';
  
  var timeLT = '${timeLT}';
  var pageNumber = 0;
  
  function loadMore() {
    $.ajax({
      url : url,
      data : {
        pageNumber : pageNumber + 1,
        timeLT : timeLT
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if(result.code != 0) {
          return;
        }
        var page = result.data.page;
        if (page.data.length) {
          timeLT = result.data.timeLT;
          pageNumber = page.pageNumber;
          var pageData = page.data;
          for ( var i in pageData) {
            var row = pageData[i];
            buildRow(row);
          }
        }
        if (!page.data.length || page.data.length < page.pageSize) {
          $('.list-more').addClass('disabled').html('<span>没有更多数据了</span>').unbind('click', loadMore);
        }
      }
    });
  }
  
  function buildRow(row){
    var html = '<div class="list-item">' + '<div class="title lh-24 text-ellipsis">' + row.title + '</div>' + '<div class="inout '
      + (row.inOut == '收入' ? 'currency-in' : 'currency-out') + ' lh-24 text-right">' + row.transAmount.toFixed(2) + '</div>' + '<div class="clearfix lh-24">'
      + '<span class="left fs-12 font-999">' + row.transTimeLabel + '</span>' + '<span class="right fs-12 font-999">余额: ' + row.afterAmount.toFixed(2) + '</span>'
      + '</div></div>';
    $(html).insertBefore($('.list-more'));
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
    <div class="list-group mb-0">
      
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
      
      <c:if test="${page.total > page.pageSize}">
      <a class="list-item list-more" href="javascript:;"><span>点击加载更多</span></a>
      </c:if>
      <c:if test="${page.total <= page.pageSize}">
      <a class="list-item list-more disabled" href="javascript:;"><span>没有更多数据了</span></a>
      </c:if>
      
    </div>
    </c:if>
    
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
