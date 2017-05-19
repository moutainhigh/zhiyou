<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="nav" value="2" />

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
  <title>资源库</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <%--<link href="${stccdn}/css/activity.css" rel="stylesheet" />--%>
  <script type="text/javascript">
    funW();
    $(window).resize(function(event) {
      funW();
    });
    function funW(){
      var winW = $(window).width();
      var fontSize = $("html").css('font-size',winW/6.4);
    }
  </script>
  <style>
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
    html{
      font-size:100px;
    }
    body{
      box-sizing: border-box;
      font-size:0.28rem;
      height:100%;
      font-family:Microsoft YaHei;
      background:#fff;
    }
    .ziyuan {
      width:4.2rem;
      height:6.4rem;
      background:url(${ctx}/ziyuan.png) no-repeat;
      background-size: 4.2rem 6.4rem;
      position:absolute;
      left:0;
      top:50%;
      margin-top:-3.2rem;
    }
    article a {
      display:block;
      width:2.2rem;
      height:1.11rem;
    }
    article a.image {
      background:url(${ctx}/image.png) no-repeat;
      background-size: 2.2rem 1.11rem;
      position:absolute;
      left:2.9rem;
      top:50%;
      margin-top:-3.2rem;
    }
    article a.word {
      background:url(${ctx}/word.png) no-repeat;
      background-size: 2.2rem 1.11rem;
      position:absolute;
      left:3.9rem;
      top:50%;
      margin-top:-1.5rem;
    }
    article a.audio {
      background:url(${ctx}/audio.png) no-repeat;
      background-size: 2.2rem 1.11rem;
      position:absolute;
      left:3.9rem;
      top:50%;
      margin-top:0.5rem;
    }
    article a.video {
      background:url(${ctx}/video.png) no-repeat;
      background-size: 2.2rem 1.11rem;
      position:absolute;
      left:2.9rem;
      top:50%;
      margin-top:2.1rem;
    }
  </style>
</head>
<body class="matter-list footer-fixed">
<header class="header">
  <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
  <h1>资源库</h1>
</header>

<article>


  <div class="ziyuan"></div>
  <a href="${ctx}/matter/matterList?type=2" class="image" nme="图片库"></a>
  <a href="${ctx}/matter/matterList?type=3" class="word" nme="文档库"></a>
  <a href="${ctx}/matter/matterList?type=4" class="audio" nme="音频库"></a>
  <a href="${ctx}/matter/matterList?type=1" class="video" nme="视频库"></a>
</article>

<%@ include file="/WEB-INF/view/include/footer.jsp"%>
<script>
  $(function(){
    $(".footer-nav > a").removeClass("current");
  })
  </script>
</body>
</html>
