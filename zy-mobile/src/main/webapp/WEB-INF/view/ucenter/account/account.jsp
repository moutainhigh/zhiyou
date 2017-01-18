<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/view/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>积分钱包</title>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<link rel="stylesheet" href="${stccdn}/css/ucenter/account.css">
</head>

<body class="account">
<header class="header">
  <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>
<article>
  <nav class="flex account-nav pb-30 mb-10">
    <a class="flex-1 text-center" href="${ctx}/u/money?currencyType=0">
      <div class="mb-10"><i class="icon icon-account-money"></i></div>
      <div class="fs-16 lh-24">U币余额</div>
      <div class="fs-12 lh-20 font-ccc">${amount1}</div>
    </a>
    <a class="flex-1 text-center" href="${ctx}/u/money?currencyType=1">
      <div class="mb-10"><i class="icon icon-account-money"></i></div>
      <div class="fs-16 lh-24">积分余额</div>
      <div class="fs-12 lh-20 font-ccc">${amount2}</div>
    </a>
    <a class="flex-1 text-center" href="${ctx}/u/bankCard">
      <div class="mb-10"><i class="icon icon-account-card"></i></div>
      <div class="fs-16 lh-24">银行卡</div>
      <c:if test="${bankCardCount == 0}">
        <div class="fs-12 lh-20 font-red">未绑定</div>
      </c:if>
      <c:if test="${bankCardCount > 0}">
        <div class="fs-12 lh-20 font-ccc">${bankCardCount} 张</div>
      </c:if>
    </a>
  </nav>

  <div class="list-group mb-0">
    <div class="list-title">财务单据</div>
    <div class="list-item p-0">
      <div class="list-text account-link">
        <a class="bd-r bd-b" href="${ctx}/u/account/deposit">
          <div><i class="icon icon-account-deposit"></i></div>
          <div class="fs-14 font-333">充值单</div>
        </a>
        <a class="bd-r bd-b" href="${ctx}/u/account/withdraw">
          <div><i class="icon icon-account-withdraw"></i></div>
          <div class="fs-14 font-333">提现单</div>
        </a>
      </div>
    </div>
  </div>

  <div class="list-group mb-10">
    <div class="list-title">我的收入</div>
    <div class="list-item p-0">
      <div class="list-text account-link">
        <a class="bd-r bd-b" href="${ctx}/u/account/profit?type=1">
          <div><i class="icon icon-account-order"></i></div>
          <div class="fs-14 font-333">订单收款</div>
        </a>
        <a class="bd-r bd-b" href="${ctx}/u/account/profit?type=2">
          <div><i class="icon icon-account-data"></i></div>
          <div class="fs-14 font-333">数据奖</div>
        </a>
        <a class="bd-b" href="${ctx}/u/account/profit?type=3">
          <div><i class="icon icon-account-sale"></i></div>
          <div class="fs-14 font-333">销量奖</div>
        </a>
        <a class="bd-r" href="${ctx}/u/account/profit?type=4">
          <div><i class="icon icon-account-teping"></i></div>
          <div class="fs-14 font-333">特级平级奖</div>
        </a>
      </div>
    </div>
  </div>

  <div class="list-group mb-0">
    <div class="list-title">转账单</div>
    <div class="list-item p-0">
      <div class="list-text account-link">
        <a class="bd-r bd-b" href="${ctx}/u/account/transferIn?status=0">
          <div><i class="icon icon-account-in"></i></div>
          <div class="fs-14 font-333">转入</div>
          <em class="badge red">${transferInCount}</em>
        </a>
        <a class="bd-r bd-b" href="${ctx}/u/account/transferOut?status=0">
          <div><i class="icon icon-account-out"></i></div>
          <div class="fs-14 font-333">转出</div>
          <em class="badge red">${transferOutCount}</em>
        </a>
      </div>
    </div>
  </div>
</article>
</body>

</html>
