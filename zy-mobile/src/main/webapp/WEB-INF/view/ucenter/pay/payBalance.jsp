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

<title>余额支付</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/activity.css">
<script type="text/javascript">
  $(function() {
	  $("#btnSubmit").click(function(){
		 $("#form").submit();
      });
  });
</script>

</head>
<body>

  <header class="header">
    <h1>余额支付</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <c:if test="${amount > balance}">
  <div class="note note-warning mb-0">
    <p><i class="fa fa-exclamation-circle"></i> 对不起，您账户余额不足！</p>
  </div>
  </c:if>
  
  <form action="${ctx}/u/pay/payment" class="valid-form" id="form" method="post">
  <input type="hidden" name="paymentId" value="${paymentId}" >
  <article class="mt-15 mb-15 clearfix">
     <div class="list-title">单据信息</div>
     <div class="list-group">
       <div class="list-item">
          <div class="list-text">标题</div>
          <div class="list-unit">${title}</div>
       </div>
       <div class="list-item">
          <div class="list-text">单据号</div>
          <div class="list-unit">${sn}</div>
       </div>
     </div>  
     
     <div class="list-title">支付信息</div> 
     <div class="list-group">
       <div class="list-item">
         <div class="list-text">账户余额</div>
         <div class="list-unit">${balance}元</div>
       </div>
       <div class="list-item">
         <div class="list-text">支付金额</div>
         <div class="list-unit"><span class="font-orange">${amount}</span>元</div>
       </div>
     </div>
     
     <c:if test="${amount <= balance}">
     <div class="form-btn">
        <div id="btnSubmit" class="btn orange btn-block round-2">确认支付</div>
     </div>
     </c:if>
     <c:if test="${amount > balance}">
     <div class="form-btn">
        <div id="btn" class="btn disable btn-block round-2">余额不足，请先充值</div>
     </div>
     </c:if>
  </article>  
  </form>
</body>
</html>
