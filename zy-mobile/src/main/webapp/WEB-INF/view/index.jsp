<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="0" />

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

<title>${sys} - 首页</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/index.css" rel="stylesheet" />
<link href="${stccdn}/plugin/swipe-2.0/swipe.css" rel="stylesheet" />
<script src="${stccdn}/plugin/swipe-2.0/swipe.js"></script>

<script type="text/javascript">

  window.onload = function() {
    var swipeItems = $(".swipe .swipe-items a");
    //var swipeTitle = $(".swipe .swipe-title");
    var swipePos = $(".swipe .swipe-pos");
    swipeItems.each(function(i) {
      if (i == 0) {
        swipePos.append('<li class="current"></li>');
      } else {
        swipePos.append('<li class=""></li>');
      }
    });
    //swipeTitle.text(swipeItems.eq(0).find("img").attr("alt"));

    $('.swipe').swipe({
      startSlide : 0,
      speed : 200,
      auto : 5000,
      continuous : true,
      callback : function(pos) {
        //console.debug(pos);
      },
      transitionEnd : function(index, elem) {
        //swipeTitle.text($(elem).find("img").attr("alt"));
        swipePos.find("li").removeClass('current').eq(swipeItems.length == 2 ? index % 2 : index).addClass('current');
      }
    });
  };
</script>

</head>
<body class="footer-fixed">

  <!-- 轮播图 -->
  <article class="swipe-wrap">
    <div class="swipe">
      <div class="swipe-items">
        <c:forEach items="${banners}" var="banner">
          <a href="${banner.url}" <c:if test="${banner.isOpenBlank}"> target="_blank"</c:if>> <img src="${banner.image}" alt="${banner.title}" />
          </a>
        </c:forEach>
      </div>
      <%--<div class="swipe-title"></div> --%>
      <ul class="swipe-pos"></ul>
    </div>
  </article>

  <nav class="index-nav flex box pt-15 pb-15">
    <a class="flex-1" href="${ctx}/u/report">
      <div class="text-center">
        <img class="round" src="${stccdn}/image/icon/icon_report.png">
      </div>
      <div class="mt-5 text-center">检测报告</div>
    </a>
    <a class="flex-1" href="${ctx}/activity">
      <div class="text-center">
        <img class="round" src="${stccdn}/image/icon/icon_bag.png">
      </div>
      <div class="mt-5 text-center">活动</div>
    </a>
    <a class="flex-1" href="${ctx}/product">
      <div class="text-center">
        <img class="round" src="${stccdn}/image/icon/icon_about.png">
      </div>
      <div class="mt-5 text-center">服务</div>
    </a>
  </nav>
  
  <article class="article-list mb-15 clearfix">
    <h2 class="page-title">
      <i class="fa fa-photo"></i> 新闻资讯<a href="${ctx}/article" class="right mr-10 fs-14 font-777">更多</a>
    </h2>
    <div class="list-group mb-0">
      <c:forEach items="${articles}" var="article">
      <a class="list-item article" href="${ctx}/article/${article.id}">
        <img class="mr-10" src="${article.imageThumbnail}">
        <div class="list-text">
          <h2 class="fs-15 lh-24 o-hidden">${article.title}</h2>
          <div class="font-777 fs-12">${article.releasedTimeLabel} &nbsp; ${article.author}</div>
        </div>
      </a>
      </c:forEach>
    </div>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
