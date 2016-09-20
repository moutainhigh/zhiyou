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
<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
<style>
.article img {
  width: 120px; height: 72px;
}
.article h2 {
  max-height: 48px;
}
</style>
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
      url : '${ctx}/article',
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
    var rowTpl = document.getElementById('rowTpl').innerHTML;
    laytpl(rowTpl).render(row, function(html) {
      $(html).insertBefore($('.list-more'));
    });
  }
</script>
<script id="rowTpl" type="text/html">
  <a class="list-item article" href="${ctx}/article/{{ d.id }}">
    <img class="mr-10" src="{{ d.imageThumbnail }}">
    <div class="list-text">
      <h2 class="fs-15 lh-24 o-hidden">{{ d.title }}</h2>
      <div class="font-777 fs-12">{{ d.releasedTimeLabel }} &nbsp; {{ d.author }}</div>
    </div>
  </a>
</script>
</head>
<body class="article-list">

  <header class="header">
    <a href="${ctx}/" class="button-left"><i class="fa fa-home"></i></a>
    <h1>新闻资讯</h1>
  </header>

  <article>
    <div class="list-group mb-0">
      <c:forEach items="${page.data}" var="article">
      <a class="list-item article" href="${ctx}/article/${article.id}">
        <img class="mr-10" src="${article.imageThumbnail}">
        <div class="list-text">
          <h2 class="fs-15 lh-24 o-hidden">${article.title}</h2>
          <div class="font-777 fs-12">${article.releasedTimeLabel} &nbsp; ${article.author}</div>
        </div>
      </a>
      </c:forEach>
      <c:if test="${page.total > page.pageSize}">
      <a class="list-item list-more" href="javascript:;"><span>点击加载更多</span></a>
      </c:if>
      <c:if test="${page.total <= page.pageSize}">
      <a class="list-item list-more disabled" href="javascript:;"><span>没有更多数据了</span></a>
      </c:if>
    </div>
  </article>

</body>
</html>
