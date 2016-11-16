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

<title>提现单</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/pageload.jsp"%>
<script type="text/javascript">
  function getUrl() {
    return '${ctx}/u/account/withdraw';
  }
  
  function buildRow(row) {
    var html = '<div class="list-item">' 
             +   '<div class="list-text pl-5">' 
             +     '<div class="fs-14">' + row.sn + '</div>'
             +     '<div class="fs-12 font-999">申请时间:' + row.createdTimeLabel + '</div>'
             + (row.withdrawStatus == '提现成功' ? '<div class="fs-12 font-999">到账时间:' + row.withdrawTimeLabel + '</div>' : '')
             +   '</div>' 
             +   '<div class="list-unit width-100 text-right">' 
             +     '<div class="currency-in">' + row.amountLabel + '</div>' 
             +     '<div class="fs-12 font-999">状态：' + row.withdrawStatus + '</div>'
             + (row.withdrawStatus == '提现成功' ? '<div class="fs-14">实际到账:<span class="font-red">¥' + row.realAmountLabel + '</span></div>' : '')
             +   '</div>' 
             + '</div>';
    return html;
  }
</script>
</head>
 
<body>
  <article class="page-wrap">
    <header class="header">
      <h1>提现单</h1>
      <a href="${ctx}/u/account" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
  
    <div class="page-inner">
      <div class="page-list list-group mb-0">
      </div>
    </div>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
