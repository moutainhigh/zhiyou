<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
    <meta content="no-cache" http-equiv="pragma" />
    <meta content="no-cache, must-revalidate" http-equiv="Cache-Control" />
    <meta content="Wed, 26 Feb 1997 08:21:57GMT" http-equiv="expires">
    <meta name="format-detection" content="telephone=no"/>
    <title>报名失败</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <!--移动端版本兼容 -->
    <script type="text/javascript">
        var phoneWidth =  parseInt(window.screen.width);
        var phoneScale = phoneWidth/640;
        var ua = navigator.userAgent;
        if (/Android (\d+\.\d+)/.test(ua)){
            var version = parseFloat(RegExp.$1);
            if(version>2.3){
                document.write('<meta name="viewport" content="width=640, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
            }else{
                document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
            }
        } else {
            document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
        }
    </script>
    <!--移动端版本兼容 end -->
    <style>
        *{margin:0;padding: 0;border:none;}
        body,html {
            height: 100%;
        }
        .main {
            width:640px;
            height: 100%;
            background: url("${stc}/fall.png") no-repeat top center;
            background-size: cover;
        }
        .mainFont {
            width: 450px;
            height: 30px;
            text-align: center;
            font-size: 30px;
            padding-top: 500px;
            line-height: 60px;
            padding-left: 170px;
        }
        .mainFont span {
            color: #f0b536;
        }
        .successHref {
            display: block;
            width:440px;
            height: 80px;
            margin-left:100px;
            background:#f0b536;
            color: #fff;
            text-align: center;
            line-height: 80px;
            font-size: 24px;
            border-radius:40px;
            -webkit-border-radius:40px;
            text-decoration: none;
            position:absolute;
            bottom:60px;
        }
    </style>
</head>
<body>
<div class="main">
    <div class="mainFont">操作失败,请<span>前去报名</span></div>
    <%--<c:if test="${not empty activityId}">--%>
        <a href="${ctx}/activity/${activityId}" class="successHref">去报名</a>
    <%--</c:if>--%>
</div>
</body>
</html>
 
