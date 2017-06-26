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
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <c:forEach items="${banners}" var="banner">
                <div class="swiper-slide slide"><a href="${banner.url}"><img src="${banner.image}@250h_750w_1e_1c.jpg" /></a></div>
            </c:forEach>
        </div>
        <div class="swiper-pagination"></div>
    </div>
    <article class="clearfloat">
        <a href="#" class="notice">
            <img src="${ctx}/images/notice.png" />
            <span>通知公告</span>
        </a>
        <a href="${ctx}/activity" class="notice">
            <img src="${ctx}/images/newActivity.png" />
            <span>最新活动</span>
        </a>
        <a href="${ctx}/article" class="notice">
            <img src="${ctx}/images/new.png" />
            <span>新闻资讯</span>
        </a>
        <a href="${ctx}/u" class="notice">
            <img src="${ctx}/images/myMessage.png" />
            <span>我的信息</span>
        </a>
        <a href="${ctx}/product" class="notice">
            <img src="${ctx}/images/shop.png" />
            <span>商品服务</span>
        </a>
        <a href="${ctx}/u/report" class="notice">
            <img src="${ctx}/images/report.png" />
            <span>检测报告</span>
        </a>
        <a href="${ctx}/code" class="notice">
            <img src="${ctx}/images/inquiry.png" />
            <span>授权查询</span>
        </a>
        <a href="${ctx}/u/userInfo" class="notice">
            <img src="${ctx}/images/realName.png" />
            <span>实名认证</span>
        </a>
        <a href="${ctx}/about" class="notice">
            <img src="${ctx}/images/aboutT.png" />
            <span>关于智优</span>
        </a>
    </article>
</div>
<script src="${ctx}/js/swiper.min.js"></script>
<script>

    var mySwiper = new Swiper('.swiper-container',{
        autoplay : 2000,
        speed:300,
        loop:true,
        pagination : '.swiper-pagination'
    });
</script>
</body>
</html>
