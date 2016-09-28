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

<title>我的银行卡</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/bank.css" />
</head>
<body class="bankcard-list">
  <header class="header">
    <h1>我的银行卡</h1>
    <a href="${ctx}/u/money" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article>
    <c:if test="${empty bankCards}">
      <div class="empty-tip">
        <i class="fa fa-file-o"></i>
        <span>您还木有绑定银行卡</span>
      </div>
    </c:if>
    
    <c:forEach items="${bankCards}" var="bankCard">
    <a href="${ctx}/u/bankCard/${bankCard.id}" class="bankcard round-4 relative">
      <div class="bank-icon round"><i class="icon icon-bank-${bankCard.bankCode} round"></i></div>
      <div class="bank-info">
        <div class="fs-15 lh-24">${bankCard.bankName}</div>
        <div class="fs-12 lh-20">${bankCard.realname}</div>
        <div class="fs-24 lh-30">**** **** **** ${bankCard.cardNumberLabel}</div>
      </div>
    </a>
    </c:forEach>
    
    <div class="list-group">
      <a class="list-item-add list-item bd-0 mt-10" href="${ctx}/u/bankCard/create">
        <div class="list-icon"><i class="fa fa-plus-circle font-ccc fs-20"></i></div>
        <label class="list-text font-ccc">添加新银行卡</label>
        <i class="list-arrow"></i>
      </a>
    </div>

  </article>
  
</body>
</html>
