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
  <script>
    $(function(){
    	$('#form').submit();
    });
  </script>
</head>

<body>
<form id="form" action="${payUrl}" method="POST">
  <input type="hidden" name="merchantNo" value="${payCreateMobile.merchantNo}"/>
  <input type="hidden" name="charset" value="${payCreateMobile.charset}"/>
  <input type="hidden" name="requestTime" value="${payCreateMobile.requestTime}"/>
  <input type="hidden" name="outMemberId" value="${payCreateMobile.outMemberId}"/>
  <input type="hidden" name="outMemberRegistTime" value="${payCreateMobile.outMemberRegistTime}"/>
  <input type="hidden" name="outMemberRegistIP" value="${payCreateMobile.outMemberRegistIP}"/>
  <input type="hidden" name="outMemberVerifyStatus" value="${payCreateMobile.outMemberVerifyStatus}"/>
  <input type="hidden" name="outMemberName" value="${payCreateMobile.outMemberName}"/>
  <input type="hidden" name="outMemberMobile" value="${payCreateMobile.outMemberMobile}"/>
  <input type="hidden" name="merchantOrderNo" value="${payCreateMobile.merchantOrderNo}"/>
  <input type="hidden" name="productName" value="${payCreateMobile.productName}"/>
  <input type="hidden" name="currency" value="${payCreateMobile.currency}"/>
  <input type="hidden" name="amount" value="${payCreateMobile.amount}"/>
  <input type="hidden" name="pageUrl" value="${payCreateMobile.pageUrl}"/>
  <input type="hidden" name="notifyUrl" value="${payCreateMobile.notifyUrl}"/>
  <input type="hidden" name="userIP" value="${payCreateMobile.userIP}"/>
  <input type="hidden" name="signType" value="${payCreateMobile.signType}"/>
  <input type="hidden" name="signMsg" value="${payCreateMobile.signMsg}"/>

</form>


</body>
</html>