<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="0" />

<!DOCTYPE html>
<html>
<head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
    <meta content="no-cache" http-equiv="pragma" />
    <meta content="no-cache, must-revalidate" http-equiv="Cache-Control" />
    <meta content="Wed, 26 Feb 1997 08:21:57GMT" http-equiv="expires">
    <meta name="format-detection" content="telephone=no"/>
    <title>首页</title>
    <link rel="stylesheet" href="${ctx}/css/swiper.min.css">
    <style>
        * {margin:0;padding:0;border:none;}
        body,html {
            height:100%;
        }
        /*清除浮动代码*/
        .clearfloat:before, .clearfloat:after {
            content:"";
            display:table;
        }
        .clearfloat:after{
            clear:both;
            overflow:hidden;
        }
        .clearfloat{
            zoom:1;
        }
        a {display: block;text-decoration: none;color: #333;}
        .main {
            width:720px;
            min-height:100%;
            margin: auto;
        }
        .swiper-container {
            width:720px;
            height:360px;
        }
        .swiper-slide img {width:720px;height:360px;}
        .notice {
            float: left;
            width:168px;
            font-size: 28px;
            margin: 45px 25px 0px 40px;
            text-align: center;
        }
        .notice span {display:block;width: 100%;}
    </style>
    <!--移动端版本兼容 -->
    <script type="text/javascript">
        var phoneWidth =  parseInt(window.screen.width);
        var phoneScale = phoneWidth/720;
        var ua = navigator.userAgent;
        if (/Android (\d+\.\d+)/.test(ua)){
            var version = parseFloat(RegExp.$1);
            if(version>2.3){
                document.write('<meta name="viewport" content="width=720, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
            }else{
                document.write('<meta name="viewport" content="width=720, target-densitydpi=device-dpi">');
            }
        } else {
            document.write('<meta name="viewport" content="width=720, user-scalable=no, target-densitydpi=device-dpi">');
        }
    </script>
    <!--移动端版本兼容 end -->
</head>
<body>
<div class="main">
     <img src="${ctx}/images/notification23.png" style="width:720px;" />
</div>

</body>
</html>
