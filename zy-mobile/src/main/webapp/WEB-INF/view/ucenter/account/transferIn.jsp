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

<title>转入</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/pageload.jsp"%>
<script type="text/javascript">
  function getUrl() {
    return '${ctx}/u/account/transferIn?status=${status}';
  }
  
  function buildRow(row) {
    var html = '<div class="list-item" data-id="' + row.id + '">' 
             +   '<img class="image-40 round mr-10" src="' + row.fromUser.avatarThumbnail + '">'
             +   '<div class="list-text pl-5">'
             +     '<div class="transfer-nickname fs-14">' + row.fromUser.nickname + '</div>'
             +     '<div class="fs-12 font-999">' + row.createdTimeLabel + '</div>' 
             +   '</div>' 
             +   '<div class="list-unit width-100 text-right">' 
             +     '<div class="transfer-amount currency-in">' + row.amountLabel + '</div>' 
             +     '<div class="fs-12 font-999">状态: ' + row.transferStatus + '</div>'
             +   '</div>'
             + '</div>';
    return html;
  }

  $(function(){

    $('.miui-scroll-nav').scrollableNav();

  });
  
</script>
</head>
 
<body class="account-list">
  <article class="page-wrap">
    <header class="header">
      <h1>转入</h1>
      <a href="${ctx}/u/account" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>

    <nav class="miui-scroll-nav">
      <ul>
        <li<c:if test="${status == '0'}"> class="current"</c:if>><a href="${ctx}/u/account/transferIn?status=0">待处理</a></li>
        <li<c:if test="${status == '1'}"> class="current"</c:if>><a href="${ctx}/u/account/transferIn?status=1">已完成</a></li>
      </ul>
    </nav>
  
    <div class="page-inner">
      <div class="page-list list-group mb-0">
      </div>
    </div>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
