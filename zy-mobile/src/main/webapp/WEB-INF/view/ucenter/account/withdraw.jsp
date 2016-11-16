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
             +     '<div class="fs-14">单据号:' + row.sn + '</div>'
             +     '<div class="fs-12 font-999">申请时间: ' + row.createdTimeLabel + '</div>'
             + (row.withdrawStatus == '提现成功' ? '<div class="fs-12 font-999 bdd-t mt-5 pt-5">到账时间: ' + row.withdrawedTimeLabel + '</div>' : '')
             + (row.withdrawStatus == '提现成功' ? '<div class="fs-12 font-777">实际到账 <span class="fs-14 font-orange">¥ ' + row.realAmountLabel + '</span></div>' : '')
             +   '</div>' 
             +   '<div class="list-unit width-100 text-right">' 
             +     '<div class="currency-out">' + row.amountLabel + '</div>' 
             +     '<div class="fs-12 ' + getStyle(row.withdrawStatus) + '">' + row.withdrawStatus + '</div>'
             +   '</div>' 
             + '</div>';
    return html;
  }
  
  function getStyle(status){
    switch (status) {
      case '已申请':
        return 'font-222';
      case '提现成功':
        return 'font-orange';
      case '已取消':
        return 'font-aaa';
      default:
        return 'font-aaa';
    }
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
