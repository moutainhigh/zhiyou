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

<title>我的余额</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
</head>

<body>

  <header class="header">
    <h1>我的余额</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a class="button-right" href="${ctx}/u/money/log"><span>明细</span></a>
  </header>

  <article class="mt-30">
    <i class="icon icon-money icon-6x block center"></i>
    <h2 class="font-777 fs-16 lh-30 text-center mt-20">我的余额(元)</h2>
    <div class="font-333 fs-36 lh-60 text-center">${amount}</div>
    
    <div class="form-btn mt-20">
      <a href="${ctx}/u/pay/deposit?payType=1" class="btn orange btn-block round-2">充值</a>
    </div>
    <c:if test="${!isBoundBankCard}">
    <div class="form-btn">
      <a href="javascript:;" class="btn disabled btn-block round-2">提现(请先添加银行卡)</a>
    </div>
    </c:if>
    <c:if test="${isBoundBankCard}">
    <div class="form-btn">
      <a href="${ctx}/u/money/withdraw" class="btn green btn-block round-2">提现</a>
    </div>
    </c:if>
    <div class="form-btn">
      <a href="${ctx}/u/bankCard" class="btn default btn-block round-2">我的银行卡</a>
    </div>
  </article>
  <a href="${ctx}/help/money" class="abs-lb mb-10 width-100p font-999 fs-12 text-center"><i class="fa fa-question-circle-o"></i> 余额问题</a>

</body>

</html>
