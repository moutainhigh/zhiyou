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

<title>保险申请列表</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
<script type="text/javascript">
  $(function() {
    if (!$('.page-more').hasClass('disabled')) {
      $('.page-more').click(loadMore);
    }
  });
  
  var timeLT = '${timeLT}';
  var pageNumber = 0;

  function loadMore() {
    $.ajax({
      url : '${ctx}/u/policy',
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
          $('.page-more').addClass('disabled').html('<span>没有更多数据了</span>').unbind('click', loadMore);
        }
      }
    });
  }
  
  function buildRow(row){
    var rowTpl = document.getElementById('rowTpl').innerHTML;
    laytpl(rowTpl).render(row, function(html) {
      $('.list-group').append(html);
    });
  }
</script>
<script id="rowTpl" type="text/html">
  <a class="list-item" href="javascript:;">
    <div class="list-text policy">
      <div class="lh-30 fs-14">报告编号：<span class="fs-13 font-orange">{{ d.reportId }}</span></div>
      <div class="lh-30 fs-14">{{ d.realname }}<span class="ml-10 fs-12 font-999">&lt;{{ d.gender }}  {{ d.age }}岁&gt;</span><span class="right fs-12 font-999">{{ d.phone }}</span></div>
    </div>
  </a>
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>保险申请列表</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="${ctx}/u/policy/create" class="button-right">新增</a>
  </header>
  
  <article>
    <c:if test="${userRank == 'V0'}">
    <div class="note note-warning mb-0">
      <i class="fa fa-exclamation-circle"></i> 您必须成为服务商才能上传检测报告
    </div>
    </c:if>
    
    <c:if test="${empty page.data}">
      <div class="page-empty">
        <i class="fa fa-file-o"></i>
        <span>暂无记录</span>
      </div>
    </c:if>
    
    <c:if test="${not empty page.data}">
      <div class="list-group mb-0">
        <c:forEach items="${page.data}" var="policy">
        <a class="list-item" href="javascript:;">
          <div class="list-text policy">
            <div class="lh-30 fs-14">保单编号：<span class="font-orange fs-13">${policy.code}</span></div>
            <div class="lh-30 fs-14">${policy.realname}<span class="ml-10 fs-12 font-999">&lt;${policy.gender}&gt;</span><span class="right fs-12 font-999">${policy.phone}</span></div>
          </div>
        </a>
        </c:forEach>
      </div>
      <c:if test="${page.total > page.pageSize}">
        <div class="page-more"><span>点击加载更多</span></div>
      </c:if>
      <c:if test="${page.total <= page.pageSize}">
        <div class="page-more disabled"><span>没有更多数据了</span></div>
      </c:if>
    </c:if>
  </article>
</body>
</html>
