<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@ include file="/WEB-INF/view/include/head.jsp"%>
	<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css">
<style>
		#btn-next span:before, #btn-next span:after {
		  border-left: 15px solid #ccc;
		  border-right: 0;
		}
		#btn-prev span:before, #btn-prev span:after {
		  border-right: 15px solid #ccc;
		  border-left: 0;
		}

	</style>
</head>
<body>
<header class="header">
	<a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  	<h1>音频详情</h1>
</header>

<article>
  <div class="ziyuan"></div>
 <div class="main">
	 <h2 class="font-333 fs-18 lh-30">${matter.title}</h2>
	 <span>优检一生 &nbsp;&nbsp; ${matter.uploadTime}</span>
	 <div class='jAudio--player'>
	<audio></audio>
	<div class='jAudio--ui'>
	  <div class='jAudio--thumb'></div>
	  <div class='jAudio--status-bar'>
		<div class='jAudio--details'></div>
		<div class='jAudio--volume-bar'></div>
		<div class='jAudio--progress-bar'>
		  <div class='jAudio--progress-bar-wrapper'>
			<div class='jAudio--progress-bar-played'>
			  <span class='jAudio--progress-bar-pointer'></span>
			</div>
		  </div>
		</div>
		  <div class='jAudio--time'>
			  <span class='jAudio--time-elapsed'>00:00</span>
			  <span class='jAudio--time-total'>00:00</span>
		  </div>
	  </div>
	</div>
	<div class='jAudio--controls'>
	  <ul>
		<li><button  href="${matter.url}" class='btn' data-action='prev' id='btn-prev'><span></span></button></li>
		<li><button class='btn' data-action='play' id='btn-play'><span></span></button></li>
		<li><button class='btn' data-action='next' id='btn-next'><span></span></button></li>
	  </ul>
	</div>
	</div>
	 <div class="spanAll clearfloat">
		 <span>阅读 ${matter.clickedCount}</span>
		 <div class="spanlove clearfloat" onClick="changeLove(this)" change="love">
			 <img src="../../../images/love.png">
			 <span class="loveSpan">${matter.collectedCount}</span>
		 </div>
	 </div>
	</div>
</article>
 
<script type="text/javascript" src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script src='${ctx}/js/jaudio.js'></script>
<script>

var t = {
	playlist:[
		{
		  file: "${matter.url}",
		  thumb: "${ctx}/01.jpg",
		  trackName: "",
		  trackArtist: "",
		  trackAlbum: "Single",
		}
	]
}

$(".jAudio--player").jAudio(t);

$(function(){
	if(!${matter.isCollected}){
		$(".spanlove>img").attr("src","../../../love.png");
		$(".spanlove").attr("change","love");
	}else {
		$(".spanlove>img").attr("src","../../../love_check.png");
		$(".spanlove").attr("change","loveCheck");
	};
});

//点击点赞/取消点赞
  function changeLove(obj){
      if($(obj).attr("change")=="love"){
		  $.ajax({
			  url: '${ctx}/matter/collect',
			  data: {
				  id: '${matter.id}'
			  },
			  dataType: 'JSON',
			  type: 'post',
			  success: function (result) {
				  if (result.code == 0) {
//					  messageFlash('关注成功');
					  $(obj).find("img").attr("src","${ctx}/love_check.png");
					  $(obj).find("span").html(parseInt($(obj).find("span").html())+1);
					  $(obj).attr("change","loveCheck");
				  } else if (result.code == 401) {
					  showLogin();
				  }
			  }
		  });
	  }else {
		  $.ajax({
			  url: '${ctx}/matter/uncollect',
			  data: {
				  id: '${matter.id}'
			  },
			  dataType: 'JSON',
			  type: 'post',
			  success: function (result) {
				  if (result.code == 0) {
//					  messageFlash('取消关注');
					  $(obj).find("img").attr("src","${ctx}/love.png");
					  $(obj).find("span").html($(obj).find("span").html()-1);
					  $(obj).attr("change","love");
				  } else if (result.code == 401) {
					  showLogin();
				  }
			  }
		  });
	  }
  }
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>