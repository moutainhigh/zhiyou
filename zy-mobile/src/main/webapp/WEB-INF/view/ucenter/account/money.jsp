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

<title>我的${currencyType.alias}余额</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
</head>

<body>

  <header class="header">
    <h1>我的${currencyType.alias}余额</h1>
    <a href="${ctx}/u/account" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a class="button-right" href="${ctx}/u/money/log?currencyType=${currencyType}"><span>明细</span></a>
  </header>

  <article>
    <i class="icon icon-money icon-6x block center mt-30"></i>
    <h2 class="font-777 fs-16 lh-30 text-center mt-20">我的${currencyType.alias}余额(元)</h2>
    <div class="font-333 fs-36 lh-60 text-center">${amount}</div>
    <c:if test="${currencyType == '现金'}">
    <div class="form-btn mt-20">
      <a href="${ctx}/u/pay/deposit" class="btn orange btn-block round-2">充值</a>
    </div>
    <c:if test="${bankCardCount == 0}">
    <div class="form-btn">
      <a href="javascript:;" class="btn disabled btn-block round-2">提现(请先添加银行卡)</a>
    </div>
    </c:if>
    </c:if>
    <c:if test="${bankCardCount > 0}">
      <div class="form-btn">
        <c:if test="${isWithdrawOn}">
          <a href="${ctx}/u/money/withdraw?currencyType=${currencyType}" class="btn green btn-block round-2">提现</a>
        </c:if>
      </div>
    </c:if>
    <a href="${ctx}/help/money" class="mt-30 mb-10 block width-100p font-999 fs-12 text-center"><i class="fa fa-question-circle-o"></i> 积分余额问题</a>
  </article>

</body>

</html>
