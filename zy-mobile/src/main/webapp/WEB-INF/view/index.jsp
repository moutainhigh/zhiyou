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

<title>${sys}- 首页</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/index.css" rel="stylesheet" />
<link href="${stccdn}/css/task/task.css" rel="stylesheet" />
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
    <a class="flex-1" href="${ctx}/task?type=gift">
      <div class="img-wrap">
        <img class="round" src="${stccdn}/image/icon/icon_free.png">
      </div>
      <div class="title">礼品任务</div>
    </a> <a class="flex-1" href="${ctx}/task">
      <div class="img-wrap">
        <img class="round" src="${stccdn}/image/icon/icon_bag.png">
      </div>
      <div class="title">精品试用</div>
    </a> <a class="flex-1" href="${ctx}/task?type=brand">
      <div class="img-wrap">
        <img class="round" src="${stccdn}/image/icon/icon_tag.png">
      </div>
      <div class="title">品牌折扣</div>
    </a>
  </nav>

  <article class="task-list mb-15 clearfix">
    <h2>
      <i class="fa fa-heart-o"></i> 推荐试用<a href="${ctx}/task" class="right mr-10 fs-14 font-777">更多</a>
    </h2>
    <c:forEach items="${taskVos}" var="task">
      <a href="${ctx}/task/detail?id=${task.id}&giftId=${task.giftId}" class="task">
        <div class="image-box">
          <img class="abs-lt" src="${task.imageThumbnail}">
        </div>
        <h3>${task.title}</h3>
        <div class="task-info">
          <c:if test="${not empty task.totalQuantity}">
            <span class="right">限量${task.totalQuantity}份</span>
          </c:if>
          <span class="price">¥<del>${task.marketPrice}</del></span>
          <em class="round-2"><c:if test="${task.price <= 0.00}">免费</c:if><c:if test="${task.price > 0.00}">${task.price}元</c:if></em>
        </div>
      </a>
    </c:forEach>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
