<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text; charset=utf-8">
  <meta http-equiv="Cache-Control" content="no-store" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="0" />
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

  <title>新晋占比排名</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
  <style>
    body {background: #f7f7f9}
    body,html {font-family:  "Microsoft YaHei";text-overflow: ellipsis;
      white-space: nowrap;}
    a {display: block;}
    .all {
      width: 100%;
      background: #fff !important;
      border-bottom: 1px solid #e5e5e5;
    }
    .teamAll img {float: left;width: 50px;height: 50px;margin-left: 8px;}
    .teamAll span {
      float: left;line-height: 50px;margin-left: 20px;font-size: 18px;
      color: #838385;
    }
    .teamAll a {
      float: right;
      line-height: 50px;
      color: #6cb92d;
      margin-right: 15px;
    }
    .TeamName img {
      width:55%;
      margin-top:20px;
    }
    .TeamName span {
      display: block;
      margin-top:10px;
    }
    .rankingAllList {border-bottom: 1px solid #e5e5e5;}
    .rankingAll {
      width:100%;
      height:60px;
    }
    .rankingAll>span {
      float: left;
      display: block;;
      width:65px;
      height:100%;
      text-align: center;
      line-height:60px;
    }
    .rankingAll>img {
      float: left;
      width: 40px;
      margin-top: 10px;
      margin-bottom: 10px;
      height:40px;
      -webkit-border-radius:50%;
      -moz-border-radius:50%;
      border-radius:50%;
    }
    .ranking {
      float: left;
      width:100px;
      height:100%;
      text-align: center;
      line-height: 60px;
    }
    .teamTop {
      width:90%;
      height:60px;
      margin-left: 5%;
      position: relative;
    }
    .teamTop img {
      position: absolute;
      width:70%;
      height:30px;
      top: 15px;
      z-index: 1;
    }
    .teamTop input {
      position: absolute;
      background: none;
      width:70%;
      height:30px;
      border: none;
      top: 15px;
      z-index: 2;
    }
    .teamTop img.seatchImg,.searchBtn {
      width:20%;
      height:30px;
      color: #6cb92d;
      text-align: center;
      line-height: 30px;
      position: absolute;
      left:80%;
      top:15px;
      z-index: 1;
    }
    .searchBtn {
      background: none;
      border:none;
      z-index: 2;
    }
    .rankingAll>a.tel,.rankingAll>span.tel {
      width:110px;
      float: right;
      margin-right: 20px;
      color: #303134;
      line-height: 60px;
    }
    .rankingAll>span.telDetil {width:50px;}
    .jiaoTwo {
      width: 0;
      height: 0;
      border-left:8px solid transparent;
      border-right:8px solid transparent;
      border-top:8px solid #b4b4b4;
      border-bottom: 0px solid transparent;
      position: absolute;
      left: 50%;
      top:50%;
      margin-left: -8px;
      margin-top: -4px;
    }

    .searchList {
      width:100%;
      height: 80px;
      background: #fff;
      text-align: center;
      line-height:80px;
      font-size: 20px;
      display: none;
    }
    .searchListShow {
      background: #fff;
      display: none;
    }
    .rankingAllList:last-child {
      border-bottom: none;
    }
    .ranknumber p {
      width:100%;
      text-align: center;
      line-height: 25px;
      color: #a6a6a6;
    }
  </style>
</head>
<body>
<header class="header">
  <h1>新晋占比排名</h1>
  <a href="${ctx}/u/team/newTeam" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
  <div class="teamTop">
    <img src="${ctx}/seatch.png" />
    <input type="search" class="searchInput" placeholder="请输入姓名或手机号" />
    <img src="${ctx}/searchBtn.png" class="seatchImg" onclick="seatch()" />
    <div class="searchBtn" onclick="seatch()">搜索</div>
  </div>
  <div class="numberList">
    <c:forEach items="${page.data}" var="udto">
     <div class="all allLast">
        <div class="rankingAllList">
          <div class="rankingAll">
            <span>${udto.rank}</span>
            <img src="${udto.avatar}" />
            <div class="ranking">
              <span>${udto.nickname}(${udto.num}人)</span>
            </div>
            <a href="tel:{{d.phone}}" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">${udto.phone}</a>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
  <c:if test="${page.total > page.pageSize}">
    <div class="page-more"><span>点击加载更多</span></div>
  </c:if>
  <c:if test="${page.total <= page.pageSize}">
    <div class="page-more disabled" id="page-moredis"><span>没有更多数据了</span></div>
  </c:if>
  <div class="searchList">查无此人!</div>
  <div class="searchListShow">

  </div>

  </div>
</article>
<script>
  //点击特级省级列表
  function showList(obj){
    if($(obj).attr("change")=="false"){
      $(obj).next(".allLast").show();
      $(obj).attr("change","true");
      $(obj).find(".jiaoOne").addClass("jiaoTwo");
    }else {
      $(obj).find(".jiaoOne").removeClass("jiaoTwo");
      $(obj).attr("change","false");
      $(obj).next(".allLast").hide();
    }
  }

  //点击搜索
  function seatch() {
    $(".searchList").hide();
    $(".searchListShow").html("");
    $(".page-more").hide();
    $(".page-moredis").hide();
    if($(".searchInput").val()==""){
      $(".numberList").show();
      $(".searchListShow").hide();
    }else {
      $(".numberList").hide();
      $(".searchListShow").show();
      $.ajax({
        url : '${ctx}/u/team/ajaxteamRank',
        data : {
          nameorPhone:$(".searchInput").val(),
          pageNumber : -1
        },
        dataType : 'json',
        type : 'POST',
        success : function(result) {
          if(result.code != 0) {
            return;
          }
          var page = result.data.page;
          if (page.data.length) {
            var pageData = page.data;
            for ( var i in pageData) {
              var row = pageData[i];
              buildRow(row,"searchListShow");
              $(".searchList").hide();
            }
          }
          if (!page.data.length || page.data.length < 0) {
            $(".searchList").show();
          }
        }
      });
    }

  }

  $(function() {
    if (!$('.page-more').hasClass('disabled')) {
      $('.page-more').click(loadMore);
    }
  });
  var pageNumber = 0;

  function loadMore() {
    $.ajax({
      url : '${ctx}/u/team/ajaxteamRank',
      data : {
        pageNumber : pageNumber + 1
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
            buildRow(row,"numberList");
          }
        }
        if (!page.data.length || page.data.length < page.pageSize) {
          $('.page-more').addClass('disabled').html('<span>没有更多数据了</span>').unbind('click', loadMore);
        }
      }
    });
  }

  function buildRow(row,obj){
    var rowTpl = document.getElementById('rowTpl').innerHTML;
    laytpl(rowTpl).render(row, function(html) {
      $('.'+obj).append(html);
    });
  }
</script>
<script id="rowTpl" type="text/html">
  <div class="all allLast">
    <div class="rankingAllList">
      <div class="rankingAll">
        <span>{{d.rank}}</span>
        <img src="{{d.avatar}}" />
        <div class="ranking">
          <span>{{d.nickname}}({{d.num}}人)</span>
        </div>
        <a href="tel:{{d.phone}}" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">{{d.phone}}</a>
      </div>
    </div>
  </div>
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
