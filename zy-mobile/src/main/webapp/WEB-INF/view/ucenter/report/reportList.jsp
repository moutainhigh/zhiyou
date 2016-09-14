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

<title>检测报告列表</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
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
      url : '${ctx}/u/report',
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
  <a class="list-item" href="${ctx}/u/report/{{ d.id }}">
    <div class="list-text report">
      <div class="lh-30">{{ d.realname }}<span class="ml-10 fs-12 font-999">&lt;{{ d.gender }}  {{ d.age }}岁&gt;</span><span class="right fs-12 font-999">{{ d.phone }}</span></div>
      <div class="lh-30 fs-14">
      {{# if(d.reportResult == '阴性'){ }}
      <span class="font-red">阴性</span>
      {{# } else if(d.reportResult == '弱阳性'){ }}
      <span class="font-orange">弱阳性</span>
      {{# } else if(d.reportResult == '阳性'){ }}
      <span class="font-green">阳性</span>
      {{# } else if(d.reportResult == '干扰色'){ }}
      <span class="ont-purple">干扰色</span>
      {{# } }}
      {{# if(d.confirmStatus == '待审核'){ }}
      <span class="right lh-20 label orange">待审核</span>
      {{# } else if(d.confirmStatus == '未通过'){ }}
      <span class="right lh-20 label gray">未通过</span>
      {{# } else if(d.confirmStatus == '已通过'){ }}
      <span class="right lh-20 label blue">已通过</span>
      {{# } }}
      </div>
    </div>
  </a>
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>检测报告</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="${ctx}/u/report/create" class="button-right"><i class="fa fa-plus"></i></a>
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
      <c:forEach items="${page.data}" var="report">
      <a class="list-item" href="${ctx}/u/report/${report.id}">
        <div class="list-text report">
          <div class="lh-30">${report.realname}<span class="ml-10 fs-12 font-999">&lt;${report.gender}  ${report.age}岁&gt;</span><span class="right fs-12 font-999">${report.phone}</span></div>
          <div class="lh-30 fs-14">
          <c:choose>
            <c:when test="${report.reportResult == '阴性'}">
            <span class="font-red">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '弱阳性'}">
            <span class="font-orange">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '阳性'}">
            <span class="font-green">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '干扰色'}">
            <span class="ont-purple">${report.reportResult}</span>
            </c:when>
          </c:choose>
          <c:choose>
            <c:when test="${report.confirmStatus == '待审核'}">
            <span class="right lh-20 label orange">待审核</span>
            </c:when>
            <c:when test="${report.confirmStatus == '未通过'}">
            <span class="right lh-20 label gray">未通过</span>
            </c:when>
            <c:when test="${report.confirmStatus == '已通过'}">
            <span class="right lh-20 label blue">已通过</span>
            </c:when>
          </c:choose>
          </div>
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
    </c:if>
  </article>
</body>
</html>
