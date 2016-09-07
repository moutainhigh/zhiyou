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
<meta name="keywords" content="微信分销" />
<meta name="description" content="管理收货地址" />

<title>我的银行卡</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/bank.css" />
<script type="text/javascript">
	$(function() {

	});
</script>
</head>
<body class="bank-list">
  <header class="header">
    <h1>我的银行卡</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="${ctx}/u/bankCard/create" class="button-right"><i class="fa fa-plus"></i></a>
  </header>
  
  <article>
    <c:if test="${empty bankCards}">
      <div class="empty-tip">
        <i class="fa fa-file-o"></i>
        <span>您还木有绑定银行卡</span>
      </div>
    </c:if>
    
    <c:forEach items="${bankCards}" var="bankCard">
    <a href="${ctx}/u/bankCard/${bankCard.id}" class="bank round-4 relative">
      <i class="icon icon-bank-${bankCard.bankCode} round"></i>
      <span class="fs-16 lh-30">${bankCard.bankName}</span>
      <div class="fs-12 text-right"><span class="left mt-10">${bankCard.realname}</span><span class="fs-24">${bankCard.cardNumberLabel}</span></div>
    </a>
    </c:forEach>
    
    <div class="list-group">
      <a class="list-item-add list-item bd-0 mt-10" href="${ctx}/u/bankCard/create">
        <div class="list-icon"><i class="fa fa-plus-circle font-gray fs-20"></i></div>
        <label class="list-text font-gray">绑定新银行卡</label>
        <i class="list-arrow"></i>
      </a>
    </div>

  </article>
  
</body>
</html>
