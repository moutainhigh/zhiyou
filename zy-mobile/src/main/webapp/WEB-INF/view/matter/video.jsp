<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@ include file="/WEB-INF/view/include/head.jsp"%>
	<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
    <title>视频详情</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/main.css">
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
  <h1>视频详情</h1>
</header>

<article>
  <div class="main">
	  <h2 class="font-333 fs-18 lh-30">${matter.title}</h2>
	  <span>优检一生 &nbsp;&nbsp; ${matter.uploadTime}</span>
	  <div class='jAudio--player'>
		   <video preload="auto" id="video" width="100%" >
				<source src="${matter.url}">
		   </video>
		  <div class='jAudio--controls'>
			  <ul>
				  <li><button class='btn' data-action='prev' id='btn-prev'><span></span></button></li>
				  <li onClick="playVideo(this)"><button class='btn btnVedio' data-action='play' id='btn-play'><span></span></button></li>
				  <li><button class='btn' data-action='next' id='btn-next'><span></span></button></li>
			  </ul>
		  </div>
	  </div>
	  <div class="spanAll clearfloat">
		  <span>阅读 ${matter.clickedCount}</span>
		  <div class="spanlove clearfloat" onClick="changeLove(this)" change="">
			  <img src="">
			  <span class="loveSpan">${matter.collectedCount}</span>
		  </div>
	  </div>
	</div>

</article>
  
</body>
<script type="text/javascript" src="${ctx}/jquery-1.10.2.min.js"></script>
<script>
	$(function(){
		if(!${matter.isCollected}){
			$(".spanlove>img").attr("src","../../../love.png");
			$(".spanlove").attr("change","love");
		}else {
			$(".spanlove>img").attr("src","../../../love_check.png");
			$(".spanlove").attr("change","loveCheck");
		};
	});
   //点击video播放/暂停视频	
  var video = document.getElementById("video");
  function playVideo(obj){
     if($(".btnVedio").attr("id")=="btn-play"){
	    $(obj).find("button").attr("id","btn-pause");
		video.play();
	}
	else{
	    $(obj).find("button").attr("id","btn-play");
        video.pause(); 
	}
  }
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
//					   messageFlash('关注成功');
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
//					   messageFlash('取消关注');
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
  
  window.setInterval(function(){
	  if(video.paused){
		  $(".btnVedio").attr("id","btn-play");
	  }else {
	      $(".btnVedio").attr("id","btn-pause");
	  }
  },100);
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</html>