<%@ page language="java" pageEncoding="UTF-8"%>

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

<title>盛付通支付</title>
  <script src="${stccdn}/plugin/jquery-1.11.0/jquery.min.js"></script>
</head>

<body>
<form action="${payUrl}" method="POST">
  <input type="text" name="merchantNo" value="${payCreateMobile.merchantNo}"/>
  <input type="text" name="charset" value="${payCreateMobile.charset}"/>
  <input type="text" name="requestTime" value="${payCreateMobile.requestTime}"/>
  <input type="text" name="outMemberId" value="${payCreateMobile.outMemberId}"/>
  <input type="text" name="outMemberRegistTime" value="${payCreateMobile.outMemberRegistTime}"/>
  <input type="text" name="outMemberRegistIP" value="${payCreateMobile.outMemberRegistIP}"/>
  <input type="text" name="outMemberVerifyStatus" value="${payCreateMobile.outMemberVerifyStatus}"/>
  <input type="text" name="outMemberName" value="${payCreateMobile.outMemberName}"/>
  <input type="text" name="outMemberMobile" value="${payCreateMobile.outMemberMobile}"/>
  <input type="text" name="merchantOrderNo" value="${payCreateMobile.merchantOrderNo}"/>
  <input type="text" name="productName" value="${payCreateMobile.productName}"/>
  <input type="text" name="currency" value="${payCreateMobile.currency}"/>
  <input type="text" name="amount" value="${payCreateMobile.amount}"/>
  <input type="text" name="pageUrl" value="${payCreateMobile.pageUrl}"/>
  <input type="text" name="notifyUrl" value="${payCreateMobile.notifyUrl}"/>
  <input type="text" name="userIP" value="${payCreateMobile.userIP}"/>
  <input type="text" name="signType" value="${payCreateMobile.signType}"/>
  <input type="text" name="signMsg" value="${payCreateMobile.signMsg}"/>

  <input type="submit" value="提交"/>
</form>


</body>
</html>