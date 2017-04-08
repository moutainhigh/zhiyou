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

  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
  <style>
    .article img {
      width: 120px; height: 72px;
    }
    .article h2 {
      max-height: 48px;
    }
  </style>
  <script type="text/javascript">
    $(function() {
      if (!$('.page-more').hasClass('disabled')) {
        $('.page-more').click(loadMore);
      }
    });

    var type = '${type}';
    var timeLT = '${timeLT}';
    var pageNumber = 0;

    function loadMore() {
      $.ajax({
        url : '${ctx}/matter/matterList',
        data : {
          pageNumber : pageNumber + 1,
          timeLT : timeLT,
          type : type
        },
        dataType : 'json',
        type : 'POST',
        success : function(result) {
          if(result.code != 0) {
            return;
          }
          var page = result.data.page;
          if (page.data.length) {
            timeLT = result.data.timeLT;
            pageNumber = page.pageNumber;
            var pageData = page.data;
            for ( var i in pageData) {
              var row = pageData[i];
              buildRow(row);
            }
          }
          if (!page.data.length || page.data.length < page.pageSize) {
            $('.page-more').addClass('disabled').html('<span>没有更多数据了</span>').unbind('click', loadMore);
          }
        }
      });
    }

    function buildRow(row){
      var rowTpl = document.getElementById('rowTpl').innerHTML;
      laytpl(rowTpl).render(row, function(html) {
        $('.list-group').append(html);
      });
    }
  </script>
  <script id="rowTpl" type="text/html">
    <c:if test="${type == 1 || type == 3 || type == 4}">
      <a class="list-item matter" href="${ctx}/matter/{{ d.id }}">
      <c:if test="${type == 1}">
        <img class="mr-10" src="${ctx}/video.jpg">
      </c:if>
      <c:if test="${type == 3}">
        <img class="mr-10" src="${ctx}/word.jpg">
      </c:if>
      <c:if test="${type == 4}">
        <img class="mr-10" src="${ctx}/audio.jpg">
      </c:if>
        <div class="list-text">
          <h2 class="fs-15 lh-24 o-hidden">{{ d.title }}</h2>
          <div class="font-777 fs-12">{{ d.uploadTime }} &nbsp; 优检一生</div>
          <%--<input type="hidden" class="isCollected" value="{{ d.isCollected }}" >--%>
        </div>
      </a>
    </c:if>
    <c:if test="${type == 2}">
      <a class="list-item matter" href="${ctx}/matter/{{ d.id }}">
        <img class="mr-10" src="{{ d.url }}">
        <div class="list-text">
          <h2 class="fs-15 lh-24 o-hidden">{{ d.title }}</h2>
          <div class="font-777 fs-12">{{ d.uploadTime }} &nbsp; 优检一生</div>
            <%--<input type="hidden" class="id" value="{{ d.id }}">--%>
            <%--<input type="hidden" class="isCollected" value="{{ d.isCollected }}" >--%>
            <%--<input type="hidden" class="description" value="{{ d.description }}">--%>
            <%--<input type="hidden" class="clickedCount" value="{{ d.clickedCount }}">--%>
            <%--<input type="hidden" class="collectedCount" value="{{ d.collectedCount }}">--%>
        </div>
      </a>
    </c:if>
  </script>
  <style>
    header {
      position:fixed;
    }
    .header_block {
      width:100%;
      height:48px;
    }
    .list-text h2.o-hidden {
      font-size: 17px !important;
      overflow : hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;    /* 数值代表显示几行 */
      -webkit-box-orient: vertical;
    }
    .article img.playBtn {
      width:50px;
      height:50px;
      position:absolute;
      left:50px;
    }
    .disDiv {
      width:120px;
      height:72px;
      border:none;
      background: rgba(0,0,0,0.3);
      position:absolute;
    }
    .disDiv {
      width:120px;
      height:72px;
      border:none;
      background: rgba(0,0,0,0.3);
      position:absolute;
    }
    .showImage {
      width:100%;
      height:100%;
      background: rgba(0,0,0,0.8);
      position: fixed;
      left:0;
      top:0;
      z-index:88;
      display: none;
    }
    .showImageSrc {
      width:100%;
      position: fixed;
      top:50%;
      left:50%;
      -wekit-transform: translate(-50%, -50%);
      transform: translate(-50%, -50%);
      z-index:90;
      display: none;
    }
    .spanAll {
      width:100%;
      position: fixed;
      bottom:0;
      z-index:99;
      display:none;
    }
    .spanAll>span {
      float:left;
      height:30px;
      font-size:20px;
      line-height:30px;
      color: #fff;
      padding-left:7%;
    }
    .spanAll .spanlove {
      float:left;
    }
    .spanAll img {
      float:left;
      width:20px;
      margin-left:30px;
      margin-top:7px;
    }
    .spanAll span.loveSpan {float:left;margin-left:10px;font-size:20px;margin-top:2px;color:#fff;}
    .spanInnor {
      width:100%;
      padding-left:7%;
      padding-right:7%;
      padding-bottom:5%;
      box-sizing: border-box;
      font-size: 20px;
      color: #fff;
    }
    .mr-10 {
      width:120px;
      height:72px;
    }
  </style>
</head>
<body class="matter-list">

<header class="header">
  <a href="${ctx}/matter" class="button-left"><i class="fa fa-angle-left"></i></a>
    <c:if test="${type == 1}">
      <h1>视频库</h1>
    </c:if>
    <c:if test="${type == 2}">
      <h1>图片</h1>
    </c:if>
    <c:if test="${type == 3}">
      <h1>文档</h1>
    </c:if>
    <c:if test="${type == 4}">
      <h1>音频</h1>
    </c:if>
</header>
<div class="header_block"></div>
<article>
  <div class="list-group mb-0">
    <c:forEach items="${page.data}" var="matter">
      <c:if test="${type == 1 || type == 3 || type == 4}">
        <a class="list-item matter" href="${ctx}/matter/${matter.id}">
          <c:if test="${type == 1}">
            <img class="mr-10" src="${ctx}/video.jpg">
          </c:if>
          <c:if test="${type == 3}">
            <img class="mr-10" src="${ctx}/word.jpg">
          </c:if>
          <c:if test="${type == 4}">
            <img class="mr-10" src="${ctx}/audio.jpg">
          </c:if>
          <div class="list-text">
            <h2 class="fs-15 lh-24 o-hidden">${matter.title}</h2>
            <div class="font-777 fs-12">${matter.uploadTime} &nbsp; 优检一生</div>
            <%--<input type="hidden" class="isCollected" value="${matter.isCollected}" >--%>
          </div>
        </a>
      </c:if>
      <c:if test="${type == 2}">
        <a class="list-item matter" href="${ctx}/matter/${matter.id}">
          <img class="mr-10" src="${matter.url}">
          <div class="list-text">
            <h2 class="fs-15 lh-24 o-hidden">${matter.title}</h2>
            <div class="font-777 fs-12">${matter.uploadTime} &nbsp; 优检一生</div>
            <input type="hidden" class="id" value="${matter.id}" >
            <%--<input type="hidden" class="isCollected" value="${matter.isCollected}">--%>
            <%--<input type="hidden" class="description" value="${matter.description}">--%>
            <%--<input type="hidden" class="clickedCount" value="${matter.clickedCount}">--%>
            <%--<input type="hidden" class="collectedCount" value="${matter.collectedCount}">--%>
          </div>
        </a>
      </c:if>
    </c:forEach>
  </div>

  <c:if test="${page.total > page.pageSize}">
    <div class="page-more"><span>点击加载更多</span></div>
  </c:if>
  <c:if test="${page.total <= page.pageSize}">
    <div class="page-more disabled"><span>没有更多数据了</span></div>
  </c:if>
</article>
<div></div>
<script>
var id;
$(function(){
   var type=${type};
   if(type == 2){
       $(".mr-10,.list-text,.o-hidden,.fs-12").css("pointer-events","none");
       $(".matter").attr("href","javascript:;");
       $("body").append('<div class="showImage" onclick="displayimage()"></div> <img src="" class="showImageSrc" onclick="displayimage()"> <div class="spanAll clearfloat"><div class="spanInnor"></div>'
             +'<span></span><div class="spanlove clearfloat" onClick="changeLove(this)" change="love"><img src="../../../love.png"><span class="loveSpan"></span></div></div>');
   }else {
     $("body").remove('<div class="showImage" onclick="displayimage()"></div> <img src="" class="showImageSrc" onclick="displayimage()"> <div class="spanAll clearfloat"><div class="spanInnor"></div>'
             +'<span></span><div class="spanlove clearfloat" onClick="changeLove(this)" change="love"><img src="../../../love.png"><span class="loveSpan"></span></div></div>');
   }
   $(document).on("click",".matter",function(event){
      id = $(event.target).find(".id").val();
      $.ajax({
        url: '${ctx}/matter/clicke',
        data: {
          id: id
        },
        dataType: 'JSON',
        type: 'post',
        success: function (result) {
          if (result.data.matter != null) {
              $(".showImageSrc").attr("src",$(event.target).find("img").attr("src"));
              $(".showImageSrc,.showImage,.spanAll").show();
              $(".spanAll>span").html("阅读 "+ result.data.matter.clickedCount);
              $(".spanAll span.loveSpan").html(result.data.matter.collectedCount);
              $(".spanInnor").html(result.data.matter.description);
              if(!result.data.matter.isCollected){
                $(".spanlove>img").attr("src","../../../love.png");
                $(".spanlove").attr("change","love");
              }else {
                $(".spanlove>img").attr("src","../../../love_check.png");
                $(".spanlove").attr("change","loveCheck");
              };
          } else if (result.data.matter == null) {
            showLogin();
          }
        }
      });
  });
})
function displayimage(){
  $(".showImageSrc").attr("src","");
  $(".showImageSrc,.showImage,.spanAll").hide();
}
//点击点赞/取消点赞
function changeLove(obj){
  if($(obj).attr("change")=="love"){
    $.ajax({
      url: '${ctx}/matter/collect',
      data: {
        id: id
      },
      dataType: 'JSON',
      type: 'post',
      success: function (result) {
        if (result.code == 0) {
//            messageFlash('关注成功');
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
        id: id
      },
      dataType: 'JSON',
      type: 'post',
      success: function (result) {
        if (result.code == 0) {
//            messageFlash('取消关注');
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
</body>
</html>
