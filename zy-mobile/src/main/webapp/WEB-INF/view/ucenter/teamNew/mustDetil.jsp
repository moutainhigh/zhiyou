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
  <title>直属特级详情</title>
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
    .teamAll a,.teamAll span.paim {
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
      height:40px;
      -webkit-border-radius:50%;
      -moz-border-radius:50%;
      border-radius:50%;
      margin-top: 10px;
      margin-bottom: 10px;
    }
    .ranking {
      float: left;
      width:200px;
      height:100%;
      text-align: center;
      padding-top:10px;
    }
    .rankingAll>span.tel {
      width:60px;
      float: right;
      margin-right: 20px;
      color: #303134;
    }
    .telAll {
      float: right;
      margin-right: 20px;
      height:60px;
    }
    .telAll img {
      width:20px;
      margin-top: 20px;
    }
    .jian {margin-right: 0;padding-right: 20px;}
    .jian img {
      margin-top: 24px;
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
    .rankingNum {
      width:100%;
      height:60px;
      display: none;
    }
    .rankingAllList:last-child {
      border-bottom: none;
    }
    .ranknumber {
      float: left;
      width:25%;
    }
    .ranknumber p {
      width:100%;
      text-align: center;
      line-height: 25px;
      color: #a6a6a6;
    }
    .tuijian {
      font-size:12px;
    }
    .must-bu {
      width:100%;
      height:20px;
    }

    .disDiv {
      width:100%;
      height:100%;
      background: #000;
      opacity:0.5;
      -webkit-opacity:0.5;
      position: fixed;
      top:0;
      left:0;
      z-index:80;
      display: none;
    }
    .disAll {
      width:80%;
      height:120px;
      background: #fff;
      position: fixed;
      top:40%;
      left:50%;
      -webkit-transform:translate(-50%,-50%);
      transform:translate(-50%,-50%);
      z-index: 90;
      -webkit-border-radius:10px;
      -moz-border-radius:10px;
      border-radius:10px;
      display: none;
    }
    .disPhone {
      width:100%;
      height:60px;
      border-bottom: 1px solid #e5e5e5;
      color: #6cb92d;
    }
    .disPhone p {
      float: left;
      width:100%;
      height:60px;
      text-align: center;
      line-height: 60px;
      font-size: 17px;
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
    .rankingSpan {
      padding:0 8px 0 8px;
      color: #fff;
      font-size: 12px;
    }
    /*特级*/
    .must {
      background: #ffcd48;
    }
    .province {
      background: #7ed1df;
    }
    .city {
      background: #ffb558;
    }
    .VIP {
      background: #51c187;
    }
    .com {
      background: #91c7ae;
    }
    @media (device-height:568px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone5 */
      .ranking {
        width:150px;
      }
    }
  </style>
</head>
<body>
<header class="header">
  <h1 id="h1must">直属特级详情</h1>
  <a href="${ctx}/u/team/newTeam" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>
<div class="teamTop">
  <img src="${ctx}/images/seatch.png" />
  <input type="search" class="searchInput" placeholder="请输入姓名或手机号" />
  <img src="${ctx}/images/searchBtn.png" class="seatchImg" onclick="seatch()" />
  <div class="searchBtn" onclick="seatch()">搜索</div>
</div>

<article>
  <div class="numberList">
    <div class="all allLast">
      <c:forEach items="${data}" var="uvo" varStatus="indexs">
        <div class="rankingAllList">
          <div class="rankingAll">
            <img src="${uvo.image1}" style="margin-left: 20px;margin-right: 20px;"/>
            <div class="ranking" style="text-align: left;font-size: 18px;">
              <span>${uvo.realname}</span>
              <c:if test="${uvo.newflag eq 'T'}"><img src="${ctx}/images/new.png"style="width: 30px;"/></c:if>
              <p class="tuijian">[推荐人：${uvo.pname}]</p>
            </div>
            <div class="telAll jian" onclick="showNum(this,${uvo.id},${indexs.index})" change="true">
              <img src="${ctx}/images/jian.png" />
            </div>
            <div class="telAll" onclick="showDis(this)">
              <img src="${ctx}/images/tel.png" />
            </div>
            <input type="hidden" class="my" value="tel:${uvo.phone}" />
            <input type="hidden" class="parentPeoplr" value="tel:${uvo.pphone}" />

            <%--<a href="tel:${uvo.phone}" class="telAll" title="自己">--%>
              <%--<img src="${ctx}/tel.png" />--%>
            <%--</a>--%>
            <%--<a href="tel:${uvo.pphone}" class="telAll" title="推荐人">
              <img src="${ctx}/tel.png" />
            </a>--%>
          </div>
          <div class="rankingNum">
            <div class="ranknumber">
              <p id="must${indexs.index}"></p>
              <p>特级</p>
            </div>
            <div class="ranknumber">
              <p id="pro${indexs.index}"></p>
              <p>省级</p>
            </div>
            <div class="ranknumber">
              <p id="city${indexs.index}"></p>
              <p>市级</p>
            </div>
            <div class="ranknumber">
              <p id="vip${indexs.index}"></p>
              <p>VIP</p>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
  <div class="searchList">查无此人!</div>
  <div class="searchListShow">

  </div>

  </div>
</article>
<div class="disDiv" onclick="hideDiv()"></div>
<div class="disAll">
     <a href="#" class="disPhone">
         <p>呼叫<span class="fontName">赵春华</span>：<span class="phone" style="margin-left: 20px;"></span></p>
     </a>
     <a href="#" class="disPhone disPhonePeople" style="border-bottom: none;">
         <p>呼叫<span class="font">推荐人</span>：<span class="phoneT" style="margin-left: 20px;"></span></p>
     </a>
</div>
<script>
  //点击搜索
  function seatch() {
    $("#h1must").text("");
    if($(".searchInput").val()==""){
      $("#h1must").text("直属特级详情");
      $(".numberList").show();
      $(".searchListShow").hide();
    }else {
      $("#h1must").text("找人");
      $(".numberList").hide();
      $(".searchListShow").show();
      $(".searchList").hide();
      $(".searchListShow").html("");
      pageNumber=0;
      addTate();
    }
  }
  //点击黑幕
  function hideDiv(){
      $(".disDiv,.disAll").hide();
  }
  //点击电话
  function showDis(obj){
          $(".disDiv,.disAll").show();
          $(".disPhone").attr("href",$(obj).siblings(".my").val());
          $(".disPhonePeople").attr("href",$(obj).siblings(".parentPeoplr").val());

          var length=$(obj).siblings(".my").val();
          var phone=length.substring(4,length.length);
          $(".disPhone .phone").text(phone);

          var lengthT=$(obj).siblings(".parentPeoplr").val();
          var phoneT=lengthT.substring(4,length.length);
          $(".disPhone .phoneT").text(phoneT);

          $(".disPhone .fontName").text($(obj).siblings(".ranking").find("span").text());

  }
  var pageNumber = 0;
  //点击下拉箭头
  function showNum(obj,id,index){
    $.ajax({
      url : '${ctx}/u/team/ajaxfindDirectlySup',
      data : {
        userId : id
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if(result.code != 0) {
          return;
        }
        var arrys = result.message;
        var arry =arrys.split(",");
        $("#must"+index).text(arry[0]+"人");
        $("#pro"+index).text(arry[1]+"人");
        $("#city"+index).text(arry[2]+"人");
        $("#vip"+index).text(arry[3]+"人");
        if($(obj).attr("change")=="true"){
          $(obj).parents(".rankingAll").next(".rankingNum").show();
          $(obj).find("img").attr("src","${ctx}/images/jian2.png");
          $(obj).attr("change","false");
        }else {
          $(obj).parents(".rankingAll").next(".rankingNum").hide();
          $(obj).find("img").attr("src","${ctx}/images/jian.png");
          $(obj).attr("change","true");
        }

      }
    });

  }
  function addTate(){
    $(".searchList").hide();
    $.ajax({
      url : '${ctx}/u/team/ajaxfindUserAll',
      data : {
        nameorPhone:$(".searchInput").val(),
        pageNumber : pageNumber
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
        if (!page.data.length || page.data.length <0) {
          $(".page-more").remove();
          $(".searchList").show();
          //$('.page-more').addClass('disabled').html('<span>没有更多数据了</span>').unbind('click', loadMore);
        }else if(!page.data.length || page.data.length >=page.pageSize){
          $(".page-more").remove();
          $(".searchListShow").append('<div class="page-more" onclick="addTate()"><span>点击加载更多</span></div>');
        }
        pageNumber=pageNumber+1;
      }
    });
  }
  function buildRow(row,indexs){
    var rowTpl = document.getElementById('rowTpl').innerHTML;
    laytpl(rowTpl).render(row,function(html) {
      $('.searchListShow').append(html);
      $(".searchList").hide();
    });
  }
</script>
<script id="rowTpl" type="text/html">
  <div class="rankingAllList">
    <div class="rankingAll">
      <img src="{{d.avatar}}" style="margin-left: 20px;margin-right: 20px;"/>
      <div class="ranking" style="text-align: left;font-size: 18px;">
        <span>{{d.nickname == null?"":d.nickname}}</span>
        <span class="rankingSpan {{ d.userRank =='V4'?'must':d.userRank =='V3'?'province':d.userRank =='V2'?'city':d.userRank =='V1'?'VIP':d.userRank=='V0'?'com':''}}">{{d.userRank =='V4'?'特级':d.userRank =='V3'?'省级':d.userRank =='V2'?'市级':d.userRank =='V1'?'VIP':d.userRank =='V0'?'普通':''}}</span>
        <p class="tuijian">[推荐人：{{d.pnickname==null?"":d.pnickname}}</p>
      </div>
      <%--<div class="telAll" onclick="showDis(this)">
        <img src="${ctx}/tel.png" />
      </div>--%>
      <input type="hidden" class="my" value="tel:{{d.phone}}" />
      <input type="hidden" class="parentPeoplr" value="tel:{{d.pphone==null?'':d.pphone}}" />
    </div>
  </div>
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
