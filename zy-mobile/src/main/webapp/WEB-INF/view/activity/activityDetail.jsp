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

<title>${activity.title}</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/activity.css" rel="stylesheet" />
<script>
  $(function() {
    $('.tab-nav > a').tabs('.tab-content');
    
    $('#btnCollect').click(function(){
      $.ajax({
        url: '${ctx}/activity/collect',
        data: {
          id: '${activity.id}'
        },
        dataType: 'JSON',
        type: 'post',
        success: function(result){
          if(result.code == 0) {
            if(result.data) {
              messageFlash('关注成功');
              $('#btnCollect').html('<i class="fa fa-heart font-orange"></i><span>已关注 (' + ((parseInt($('#btnCollect').find('em').text()) || 0) + 1) + ')</span>');
            }
          }
        }
      });
    });
  });
  
  <%-- 加载地图插件 --%>
  <c:if test="${not empty activity.latitude && not empty activity.longitude}">
  
  var latitude = ${activity.latitude};
  var longitude = ${activity.longitude};
  
  function loadScript() {
    var script = document.createElement("script");
    script.charset = "utf-8";
    script.src = "http://map.qq.com/api/js?v=2.exp&callback=init";
    document.body.appendChild(script);
  }
  
  window.onload = loadScript;
  
  $(function() {
    $('#location').click(function() {
      $('article').hide();
      $('#asideMap').show();
      initMap();
    });
  });
  
  var initMap = function() {
    var center = new qq.maps.LatLng(latitude, longitude);
    var map = new qq.maps.Map(document.getElementById('asideMap'), {
      center : center,
      zoom : 13
    });
    var infoWin = new qq.maps.InfoWindow({
      map : map
    });
    infoWin.open();
    //tips  自定义内容
    var html = '<div class="width-200">' 
    	+ '<p class="fs-14 font-orange lh-24">活动地址：</p>'
    	+ '<p class="fs-12 font-555 lh-24">${activity.province} ${activity.city} ${activity.district}</p>'
    	+ '<p class="fs-12 font-555 lh-24">${activity.address}</p>'
    	+ '</div>';
    infoWin.setContent(html);
    infoWin.setPosition(center);
  }
  
  var closeMap = function(){
    $('article').show();
    $('#asideMap').fadeOut();
  }
  
  </c:if>
  
</script>

</head>
<body class="activity-detail footer-fixed">
  <a class="header-back" href="${ctx}/activity?type=${type}"><i class="fa fa-angle-left"></i></a>
  <article>
    <figure class="image-wrap">
      <img class="abs-lt" src="${activity.imageBig}">
    </figure>

    <div class="list-group">
      <div class="list-item">
        <h2 class="font-333 fs-16 lh-30">${activity.title}</h2>
      </div>
      <div class="list-item list-item-icon">
        <div class="list-label"><i class="fa fa-clock-o font-gray"></i><span class="font-555 fs-14">${activity.startTimeLabel} 至 ${activity.endTimeLabel}</span></div>
      </div>
      <a id="location" class="list-item list-item-icon" href="javascript:;">
        <div class="list-label"><i class="fa fa-map-marker font-gray"></i><span class="font-555 fs-14">${activity.province} ${activity.city} ${activity.district} ${activity.address}</span></div>
        <c:if test="${not empty activity.latitude && not empty activity.longitude}">
        <span class="list-text"><i class="list-arrow"></i></span>
        </c:if>
      </a>
      <div class="list-item list-item-icon">
        <div class="list-label"><i class="fa fa-clock-o font-gray"></i><span class="font-555 fs-14">报名截止  ${activity.applyDeadlineLabel}</span></div>
      </div>
      <div class="list-item list-item-icon">
        <div class="list-label"><i class="fa fa-cny font-gray"></i><span class="font-green fs-14">免费</span></div>
      </div>
    </div>
    
    <div class="list-group mt-10">
      <div class="list-item list-item-icon">
        <div class="list-label"><i class="fa fa-user font-gray"></i><span class="font-555 fs-14">已报名 <span class="font-orange">${activity.appliedCount}</span> 人</span></div>
      </div>
      <div class="list-item pt-5 pl-20 users">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/589995ef-76ae-4c84-bee8-1b27592e7b77@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/f75d581b-87ba-454d-b0bb-27c7383334cb@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/ce887059-c408-448f-a8d8-cfaae62e72d7@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/4e08f740-828f-4709-afe8-78ad36b25849@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/b2e3c0ac-1cc7-4df9-96e5-3eecf5470ac9@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/7d100ece-0e9a-4fe1-851b-63bbc2c72c27@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/af24f57a-4875-4740-ad5d-aefe86e4df87@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/037d0589-a2d2-430b-871b-50602e004371@80h_80w_1e_1c.jpg">
        <img class="image-40 mt-5 round" src="http://image.mayishike.com/avatar/c8200571-f585-456b-953c-3b0e23796c75@80h_80w_1e_1c.jpg">
      </div>
    </div>
    
    <div class="mb-15">
      <nav class="tab-nav">
        <a href="javascript:;" class="current">活动介绍</a>
        <a href="javascript:;">讨论 (43)</a>
      </nav>
      <!-- 商品详情 -->
      <div class="tab-content detail-wrap">
        <c:if test="${empty activity.detail}"><p class="p-15">暂无详情</p></c:if>
        <c:if test="${not empty activity.detail}">${activity.detail}</c:if>
      </div>
      
      <div class="tab-content hide">
        <div class="list-group mt-10">
          <div class="list-item">
            <div class="font-555">讨论 (13)</div>
          </div>
          <div class="list-item comments">
            <div class="comment relative mt-5 pr-5" style="padding-left: 55px;">
              <img class="image-40 round abs-lt mt-5" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947@80h_80w_1e_1c.jpg">
              <div class="fs-14 font-blue">哆来嘧<span class="fs-12 font-999 right">08-11 12:43</span></div>
              <p class="font-777 fa-14 bd-b pb-5">网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死</p>
            </div>
            <div class="comment relative mt-5 pr-5" style="padding-left: 55px;">
              <img class="image-40 round abs-lt mt-5" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947@80h_80w_1e_1c.jpg">
              <div class="fs-14 font-blue">哆来嘧<span class="fs-12 font-999 right">08-11 12:43</span></div>
              <p class="font-777 fa-14 bd-b pb-5">传统企业完成互联网转型的生死生死</p>
            </div>
            <div class="comment relative mt-5 pr-5" style="padding-left: 55px;">
              <img class="image-40 round abs-lt mt-5" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947@80h_80w_1e_1c.jpg">
              <div class="fs-14 font-blue">哆来嘧<span class="fs-12 font-999 right">08-11 12:43</span></div>
              <p class="font-777 fa-14 bd-b pb-5">代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死</p>
            </div>
            <a class="block mt-5 fs-12 font-999 lh-30 text-center" href="javascript:;">查看更多评论</a>
          </div>
        </div>
      </div>
    </div>
  </article>

  <nav class="footer footer-nav flex bd-0">
    <a id="btnCollect" class="flex-1" href="javascript:;"><i class="fa fa-heart-o"></i><span>关注 (<em>${activity.collectedCount}</em>)</span></a>
    <c:if test="${activity.status == '报名中'}">
    <a id="btnApply" class="flex-2 bg-blue fs-14 font-white" href="javascript:;">报名参与</a>
    </c:if>
    <c:if test="${activity.status == '进行中'}">
    <a id="btnSignIn" class="flex-2 bg-orange fs-14 font-white" href="javascript:;">活动签到</a>
    </c:if>
    <c:if test="${activity.status == '报名已结束'}">
    <a class="flex-2 fs-14 disabled" href="javascript:;">报名已结束</a>
    </c:if>
    <c:if test="${activity.status == '活动已结束'}">
    <a class="flex-2 fs-14 disabled" href="javascript:;">活动已结束</a>
    </c:if>
  </nav>
  
  <aside id="asideMap" class="abs-lt size-100p bg-white hide" style="z-index: 9999">
    <a class="header-back" href="javascript:closeMap();"><i class="fa fa-angle-left"></i></a>
  </aside>
  
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
