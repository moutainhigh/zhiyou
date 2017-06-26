<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="0" />

<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--<meta charset="utf-8">--%>
<%--<meta http-equiv="Cache-Control" content="no-store" />--%>
<%--<meta http-equiv="Pragma" content="no-cache" />--%>
<%--<meta http-equiv="Expires" content="0" />--%>
<%--<meta name="apple-mobile-web-app-capable" content="yes">--%>
<%--<meta name="apple-mobile-web-app-status-bar-style" content="black">--%>
<%--<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">--%>

<%--<title>${sys} - 首页</title>--%>
<%--<%@ include file="/WEB-INF/view/include/head.jsp"%>--%>
<%--<link href="${stccdn}/css/index.css" rel="stylesheet" />--%>
<%--<link href="${stccdn}/plugin/swipe-2.0/swipe.css" rel="stylesheet" />--%>
<%--<script src="${stccdn}/plugin/swipe-2.0/swipe.js"></script>--%>

<%--<script type="text/javascript">--%>

  <%--$(function() {--%>
	  <%--var swipeItems = $(".swipe .swipe-items a");--%>
	  <%--//var swipeTitle = $(".swipe .swipe-title");--%>
	  <%--var swipePos = $(".swipe .swipe-pos");--%>
	  <%--swipeItems.each(function(i) {--%>
		  <%--if (i == 0) {--%>
			  <%--swipePos.append('<li class="current"></li>');--%>
		  <%--} else {--%>
			  <%--swipePos.append('<li class=""></li>');--%>
		  <%--}--%>
	  <%--});--%>
	  <%--//swipeTitle.text(swipeItems.eq(0).find("img").attr("alt"));--%>

	  <%--$('.swipe').swipe({--%>
		  <%--startSlide : 0,--%>
		  <%--speed : 200,--%>
		  <%--auto : 5000,--%>
		  <%--continuous : true,--%>
		  <%--callback : function(pos) {--%>
			  <%--//console.debug(pos);--%>
		  <%--},--%>
		  <%--transitionEnd : function(index, elem) {--%>
			  <%--//swipeTitle.text($(elem).find("img").attr("alt"));--%>
			  <%--swipePos.find("li").removeClass('current').eq(swipeItems.length == 2 ? index % 2 : index).addClass('current');--%>
		  <%--}--%>
	  <%--});--%>

  <%--})--%>
<%--</script>--%>

<%--</head>--%>
<%--<body class="footer-fixed">--%>

  <%--<!-- 轮播图 -->--%>
  <%--<article class="swipe-wrap">--%>
    <%--<div class="swipe">--%>
      <%--<div class="swipe-items">--%>
        <%--<c:forEach items="${banners}" var="banner">--%>
          <%--<a href="${banner.url}" <c:if test="${banner.isOpenBlank}"> target="_blank"</c:if>>--%>
            <%--<img src="${banner.image}@250h_750w_1e_1c.jpg" alt="${banner.title}" />--%>
          <%--</a>--%>
        <%--</c:forEach>--%>
      <%--</div>--%>
      <%--&lt;%&ndash;<div class="swipe-title"></div> &ndash;%&gt;--%>
      <%--<ul class="swipe-pos"></ul>--%>
    <%--</div>--%>
  <%--</article>--%>

  <%--<nav class="index-nav flex box pt-15 pb-15">--%>
    <%--<a class="flex-1" href="${ctx}/u/report">--%>
      <%--<div class="text-center">--%>
        <%--<img class="round" src="${stccdn}/image/icon/icon_report.png">--%>
      <%--</div>--%>
      <%--<div class="mt-5 text-center">检测报告</div>--%>
    <%--</a>--%>
    <%--<a class="flex-1" href="${ctx}/matter">--%>
      <%--<div class="text-center">--%>
        <%--<img class="round" src="${stccdn}/image/icon/icon_bag.png">--%>
      <%--</div>--%>
      <%--<div class="mt-5 text-center">资源库</div>--%>
    <%--</a>--%>
    <%--<a class="flex-1" href="${ctx}/code">--%>
      <%--<div class="text-center">--%>
        <%--<img class="round" src="${stccdn}/image/icon/icon_about.png">--%>
      <%--</div>--%>
      <%--<div class="mt-5 text-center">授权查询</div>--%>
    <%--</a>--%>
  <%--</nav>--%>

  <%--<article class="article-list mb-15 clearfix">--%>
    <%--<h2 class="page-title">--%>
      <%--<i class="fa fa-photo"></i> 新闻资讯<a href="${ctx}/article" class="right mr-10 fs-14 font-777">更多</a>--%>
    <%--</h2>--%>
    <%--<div class="list-group mb-0">--%>
      <%--<c:forEach items="${articles}" var="article">--%>
      <%--<a class="list-item article" href="${ctx}/article/${article.id}">--%>
        <%--<img class="mr-10" src="${article.imageThumbnail}">--%>
        <%--<div class="list-text">--%>
          <%--<h2 class="fs-15 lh-24 o-hidden">${article.title}</h2>--%>
          <%--<div class="font-777 fs-12">${article.releasedTimeLabel} &nbsp; ${article.author}</div>--%>
        <%--</div>--%>
      <%--</a>--%>
      <%--</c:forEach>--%>
    <%--</div>--%>
  <%--</article>--%>

  <%--<%@ include file="/WEB-INF/view/include/footer.jsp"%>--%>
<%--</body>--%>
<%--</html>--%>

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
            <div class="swiper-slide slide"><a href="#"><img src="${ctx}/images/banner.png" /></a></div>
            <div class="swiper-slide slide"><a href="#"><img src="${ctx}/images/banner.png" /></a></div>
            <div class="swiper-slide slide"><a href="#"><img src="${ctx}/images/banner.png" /></a></div>
            <div class="swiper-slide slide"><a href="#"><img src="${ctx}/images/banner.png" /></a></div>
        </div>
        <div class="swiper-pagination"></div>
    </div>
    <article class="clearfloat">
        <a href="#" class="notice">
            <img src="${ctx}/images/notice.png" />
            <span>通知公告</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/newActivity.png" />
            <span>最新活动</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/new.png" />
            <span>新闻资讯</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/myMessage.png" />
            <span>我的信息</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/shop.png" />
            <span>商品服务</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/report.png" />
            <span>检测报告</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/inquiry.png" />
            <span>授权查询</span>
        </a>
        <a href="#" class="notice">
            <img src="${ctx}/images/realName.png" />
            <span>实名认证</span>
        </a>
        <a href="#" class="notice">
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
