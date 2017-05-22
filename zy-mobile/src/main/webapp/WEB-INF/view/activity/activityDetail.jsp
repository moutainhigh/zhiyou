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
<%@ include file="/WEB-INF/view/include/weixinJsApi.jsp"%>
  <style>
    body,html{height:100%;}
    .flex-3 {
      -webkit-flex: 2;
    }
    .disDiv {
      width:100%;
      height:100%;
      background: #000;
      opacity: 0.5;
      -webkit-opacity:0.5;
      position: fixed;
      left:0;
      top:0;
      z-index:8;
      display: none;
    }
    .teamBlock {
      width:100%;
      height:200px;
      position: absolute;
      left:0;
      bottom:49px;
      background: #fff;
      z-index:8;
      display: none;
    }
    .teamPlay {width:100%;height:60px;background: #f2f3f5;}
    .ticket_button {
      width:80px;
      height:40px;
      float:right;
      margin-right:20px;
      margin-top:10px;
      margin-bottom: 10px;
      line-height: 40px;
      text-align: center;
      font-size:14px;
      background: #f86b3d;
      color: #fff;
    }
    .team_people {
        width:100%;
        height:80px;
      text-align: center;
      line-height: 80px;
      padding:20px;
      border-bottom: 1px solid #e5e5e5;
    }
    .team_people span {float:left;line-height: 40px;font-size: 16px;}
    .team_people div,.team_people input {
      float:right;
      width:40px;
      height:40px;
      line-height: 40px;
      border: 1px solid #333;
      font-size: 16px;
    }
    .team_people input {width:60px;}
    .team_animat {
      -webkit-animation:bounce .8s;
      animation:bounce .8s;
    }
    @-webkit-keyframes bounce {
      from {
        -webkit-transform: translate3d(0, 200px, 0);
        transform: translate3d(0, 200px, 0);
      }
      to {
        -webkit-transform: translate3d(0, 0, 0);
        transform: translate3d(0, 0, 0);
      }
    }
    @keyframes bounce {
      from {
        -webkit-transform: translate3d(0, 200px, 0);
        transform: translate3d(0, 200px, 0);
      }
      to {
        -webkit-transform: translate3d(0, 0, 0);
        transform: translate3d(0, 0, 0);
      }
    }
    .team_animatTwo {
      -webkit-animation:bounceTwo .8s;
      animation:bounceTwo .8s;
    }
    @-webkit-keyframes bounceTwo {
      from {
        -webkit-transform: translate3d(0, 0, 0);
        transform: translate3d(0, 0, 0);
      }
      to {
        -webkit-transform: translate3d(0, 200px, 0);
        transform: translate3d(0, 200px, 0);
      }
    }
    @keyframes bounceTwo {
      from {
        -webkit-transform: translate3d(0,0, 0);
        transform: translate3d(0, 0, 0);
      }
      to {
        -webkit-transform: translate3d(0, 200px, 0);
        transform: translate3d(0, 200px, 0);
      }
    }
  </style>
<script>
  $(function() {
    $('.tab-nav > a').tabs('.tab-content');
    
    $('#btnCollect').click(function(){
      var collected = $(this).hasClass('collected');
      $.ajax({
        url: '${ctx}/u/activity/' + (collected ? 'uncollect' : 'collect'),
        data: {
          id: '${activity.id}'
        },
        dataType: 'JSON',
        type: 'post',
        success: function(result){
          if(result.code == 0) {
            if(collected) {
              messageFlash('取消关注');
              $('#btnCollect').removeClass('collected').html('<i class="fa fa-heart-o"></i><span>关注 (<em>' + ((parseInt($('#btnCollect').find('em').text()) || 0) - 1) + '</em>)</span>');
            } else {
              messageFlash('关注成功');
              $('#btnCollect').addClass('collected').html('<i class="fa fa-heart font-orange"></i><span>已关注 (<em>' + ((parseInt($('#btnCollect').find('em').text()) || 0) + 1) + '</em>)</span>');
            }
          } else if(result.code == 401) {
            showLogin();
          }
        }
      });
    });
    
    $('#btnApply').click(function(){
      $.dialog({
        content : '您确定要参与此次活动吗？',
        callback : function(index) {
          if (index == 1) {
            $('#form').submit();
          }
        }
      });

    });

    $('#btnShare').click(function(){
        $('#asideShare').show();
    });

    $(document).on('click', '#asideShare', function() {
		$(this).hide();
	});

    <c:if test="${not empty inviter}">
    $('.inviter-alert > a').click(function(){
      $('.inviter-alert').slideUp(300, function(){
        $(this).remove();
      });
      $('.header-back').removeAttr('style');
      $('body').removeClass('header-fixed');
    });
    </c:if>
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
    var number=${activity.amountLabel};
    $(".team_price").text("￥"+number*parseInt($(".inputPeople").val()));
    $('#location').click(function() {
      $('article').hide();
      $('#asideMap').show();
      initMap();
    });
  });

  function initMap() {
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

  function closeMap(){
    $('article').show();
    $('#asideMap').fadeOut();
  }

  </c:if>

</script>

</head>
<body class="activity-detail footer-fixed${not empty inviter ? ' header-fixed' : ''}">
  <form id="form" action="${ctx}/u/activity/apply" method="post">

  <input type="hidden" name="id" value="${activity.id}">
  <c:if test="${not empty inviter}">
  <input type="hidden" name="inviterPhone" value="${inviter.phone}">
  <div class="inviter-alert alert alert-warning fix-top p-10 zindex-10">
    <img class="image-40 round" src="${inviter.avatarThumbnail}">
    <span class="ml-10">${inviter.nickname} 邀请您参与该活动.</span>
    <a class="right mt-10" href="javascript:;"><i class="fa fa-close"></i></a>
  </div>
  </c:if>
  <a class="header-back"${not empty inviter ? ' style="margin-top: 70px;"' : ''} href="${ctx}/activity"><i class="fa fa-angle-left"></i></a>
  <article>
    <figure class="image-wrap">
      <img class="abs-lt" src="${activity.imageBig}">
    </figure>

    <div class="list-group">
      <div class="list-item">
        <h2 class="list-text font-333 lh-30">${activity.title}</h2>
      </div>
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-clock-o font-gray"></i></div>
        <div class="list-text fs-14 font-777">${activity.startTimeLabel} 至 ${activity.endTimeLabel}</div>
      </div>
      <a id="location" class="list-item" href="javascript:;">
        <div class="list-icon"><i class="fa fa-map-marker font-gray"></i></div>
        <div class="list-text fs-14 font-777">${activity.province} ${activity.city} ${activity.district} ${activity.address}</div>
        <c:if test="${not empty activity.latitude && not empty activity.longitude}">
        <i class="list-arrow"></i>
        </c:if>
      </a>
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-clock-o font-gray"></i></div>
        <div class="list-text fs-14 font-777">报名截止  ${activity.applyDeadlineLabel}</div>
      </div>
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-cny font-gray"></i></div>
        <div class="list-text fs-14"><span class="font-green">${activity.amountLabel}</span></div>
      </div>
    </div>

    <div class="list-group">
      <%--<div class="list-item">--%>
        <%--<div class="list-icon"><i class="fa fa-user font-gray"></i></div>--%>
        <%--<div class="list-text fs-14"><span class="font-orange">${activity.appliedCount}</span> 人已报名</div>--%>
      <%--</div>--%>
      <%--<c:if test="${not empty activity.appliedUsers}">--%>
      <%--<div class="list-item pt-5 pl-20 users">--%>
        <%--<div class="list-text">--%>
          <%--<c:forEach items="${activity.appliedUsers}" var="user">--%>
          <%--<img class="image-40 mt-5 round" src="${user.avatarThumbnail}">--%>
          <%--</c:forEach>--%>
        <%--</div>--%>
      <%--</div>--%>
      <%--</c:if>--%>
    <%--</div>--%>

    <div class="list-group mb-0">
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-list-alt font-gray"></i></div>
        <div class="list-text">活动介绍</div>
      </div>
      <div class="list-item p-0">
        <div class="list-text">
          <div class="detail-wrap">
            <c:if test="${empty activity.detail}"><p class="p-15">暂无介绍</p></c:if>
            <c:if test="${not empty activity.detail}">${activity.detail}</c:if>
          </div>
        </div>
      </div>
    </div>

    <%--
    <div class="mb-15">
      <nav class="tab-nav">
        <a href="javascript:;" class="current">活动介绍</a>
        <a href="javascript:;">讨论 (43)</a>
      </nav>
      <!-- 活动介绍 -->
      <div class="tab-content detail-wrap">
        <c:if test="${empty activity.detail}"><p class="p-15">暂无详情</p></c:if>
        <c:if test="${not empty activity.detail}">${activity.detail}</c:if>
      </div>
      <div class="tab-content hide">
        <div class="list-group mt-10">
          <div class="list-item">
            <div class="list-text">讨论 (13)</div>
          </div>
          <div class="list-item">
            <div class="comments">
              <div class="comment relative mt-5 pr-5" style="padding-left: 55px;">
                <img class="image-40 round abs-lt mt-5" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947@80h_80w_1e_1c.jpg">
                <div class="fs-14 font-blue">哆来嘧<span class="fs-12 font-999 right">08-11 12:43</span></div>
                <p class="font-777 fa-14 bd-b pb-5">网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死</p>
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
    </div>
    --%>
  </article>
  <nav class="footer footer-nav flex bd-0">
    <a id="btnCollect" class="flex-1${isCollected ? ' collected' : ''}" href="javascript:;">
      <c:if test="${isCollected}">
      <i class="fa fa-heart font-orange"></i><span>已关注 <%--(<em>${activity.collectedCount}</em>)--%></span>
      </c:if>
      <c:if test="${!isCollected}">
      <i class="fa fa-heart-o"></i><span>关注 <%--(<em>${activity.collectedCount}</em>)--%></span>
      </c:if>
    </a>
    <a id="btnShare" class="flex-1" href="javascript:;">
      <i class="fa fa-share-alt"></i><span>分享</span>
    </a>

    <a class="flex-1" href="https://static.meiqia.com/dist/standalone.html?eid=32013">
      <i class="fa fa-headphones"></i><span>客服</span>
    </a>
    <a id="btnApplyTeam" class="flex-3 bg-green fs-14 font-white" href="javascript:;" style="border-right:1px solid #fff;" onclick="showDis()">团队报名</a>
    <c:if test="${activity.status == '报名中' || activity.status == '进行中'}">
    <c:if test="${isApplied}">
      <c:if test="${!toPay}">
        <a class="flex-3 bg-green fs-14 font-white" href="javascript:;"><div><i class="fa fa-check"></i> 您已报名</div></a>
      </c:if>
      <c:if test="${toPay and not empty payerId}">
        <a class="flex-3 bg-green fs-14 font-white" href="javascript:;"><div><i class="fa fa-circle-o-notch"></i> 待他人付款</div></a>
      </c:if>
      <c:if test="${toPay and empty payerId}">
        <a class="flex-3 bg-green fs-14 font-white" href="${ctx}/u/activity/${activity.id}/activityApply"><div><i class="fa fa-hand-o-right"></i> 点击付费</div></a>
      </c:if>
    </c:if>
    <c:if test="${!isApplied}">
    <a id="btnApply" class="flex-3 bg-blue fs-14 font-white" href="javascript:;">报名参与</a>
    </c:if>
    </c:if>
    <c:if test="${activity.status == '报名已结束'}">
    <a class="flex-3 fs-14 disabled" href="javascript:;">报名已结束</a>
    </c:if>
    <c:if test="${activity.status == '活动已结束'}">
    <a class="flex-3 fs-14 disabled" href="javascript:;">活动已结束</a>
    </c:if>
  </nav>

  </form>

  <aside id="asideMap" class="abs-lt size-100p bg-white hide zindex-1000">
    <a class="header-back" href="javascript:closeMap();"><i class="fa fa-angle-left"></i></a>
  </aside>

  <aside id="asideShare" class="hide fix-lt size-100p zindex-1000" style="background: rgba(0,0,0,.75);">
    <img class="right width-50p m-30" src="${stccdn}/image/arrow.png">
    <p class="left width-100p text-center font-white fs-24">点击右上角</p>
    <p class="left width-100p text-center font-white fs-18 mt-10">发送给朋友 或 分享到朋友圈</p>
  </aside>

  <div class="disDiv" onclick="hideDis()"></div>
  <div class="teamBlock">
        <div class="teamPlay">
          <div class="ticket_button ticket_now">立即支付</div>
        </div>
        <div class="team_people">
            <span>人数</span>
            <div style="border-left: none;" onclick="addPeople()">+</div>
            <input type="text" style="text-align:center;border-left: none;" value="1" onkeyup="changePeople()" class="inputPeople"/>
            <div onclick="removePeople()">-</div>
        </div>
      <div class="team_people" style="padding-top:10px;">
         <span>价格</span>
         <div class="team_price" style="float:right;border:none;margin-right:30px;color:#f86b3d;"></div>
      </div>
  </div>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
<script>
  var number=${activity.amountLabel};
  //添加
  function addPeople(){

    $(".inputPeople").val(($(".inputPeople").val()-0)+1);
    $(".team_price").text("￥"+number*parseInt($(".inputPeople").val()));
  }
  //减去
  function removePeople(){
    if($(".inputPeople").val()==1){
        alert("人数不能为0！");
      return false;
    }else {
      $(".inputPeople").val($(".inputPeople").val()-1);

      $(".team_price").text("￥"+number*($(".inputPeople").val()-0));
    }
  }
  //input输入
  function changePeople(){
    if($(".inputPeople").val()==""){
       $(".team_price").text("￥"+number);
    }else {
      $(".team_price").text("￥"+number*($(".inputPeople").val()-0));
    }
  }
  function showDis(){
    $(".teamBlock").addClass("team_animat");
    $(".teamBlock,.disDiv").show();
  }
  function hideDis(){
    $(".teamBlock").removeClass("team_animat");
    $(".teamBlock").addClass("team_animatTwo");
    setTimeout(function () {
      $(".teamBlock").removeClass("team_animatTwo");
      $(".teamBlock,.disDiv").hide();
    },800)

  }
</script>
</body>
</html>
