<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="0" />

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text; charset=utf-8">
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title>通知公告</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <style>
        * {margin:0;padding:0;border:none;}
    </style>
</head>
<body>
<header class="header">
    <h1>通知公告</h1>
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>
<div class="main">
     <img src="${ctx}/images/notification23.png" style="width:100%;" />
</div>

</body>
</html>
