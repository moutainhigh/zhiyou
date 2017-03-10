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

<title></title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/about.css" rel="stylesheet" />
</head>

<body class="header-fixed">

<form action="${ctx}/notify/shengPay/mobile/sync">
  <input type="text" name="TransNo" value="HD20170310">
  <input type="text" name="orderNo" value="3">
  <input type="submit" value="提交">
</form>
</body>
</html>