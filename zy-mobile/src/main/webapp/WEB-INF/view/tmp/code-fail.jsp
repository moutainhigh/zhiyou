<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>扫码失败页面</title>
    <%@ include file="/WEB-INF/view/include/head.jsp" %>
    <script type="text/javascript">
        $(function () {

        });
    </script>
    <style>
        html,body{
            width: 100%;height: 100%;
        }
        body{
            background: #78C8B4;
        }
        .codefail-info{
            width: 14%;height: 8%;
            left: 43%;top: 5%;
        }
        .codefail-name{
            width: 34%;height: 6%;text-align: center;
            left: 33%;top: 15%;
        }
    </style>
</head>
<body class="relative">
    <img src="${stccdn}/image/tmp/fail.png"  class="codefail-header size-100p"/>
    <img src="${stccdn}/image/tmp/fail_sound.png" class="codefail-sound size-100p ab-lt" />
    <img src="${stccdn}/image/tmp/fail-sign.png" class="codefail-sign size-100p ab-lt" />
    <img src="${stccdn}/image/tmp/fail-logo.png" class="codefail-logo size-100p abs-lb"/>
    <a class="codefail-info block absolute">
        <img src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg" class="block round size-100p"/>
    </a>
    <span class="codefail-name absolute font-white">昵称昵称</span>
</body>
</html>

