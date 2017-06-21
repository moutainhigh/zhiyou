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

  <title>直属团队进销单详情</title>
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
      margin-left: 15px;
    }
    .ranking {
      float: left;
      width:100px;
      height:100%;
      text-align: center;
      line-height: 60px;
    }
    .rankingSpan {
      padding:0 8px 0 8px;
      color: #fff;
      font-size: 12px;
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
    .rankingTop {font-size: 18px}
    .rankingAll>a.tel,.rankingAll>span.tel {
      width:110px;
      float: right;
      margin-right: 20px;
      color: #303134;
      line-height: 60px;
    }
    .rankingAll>span.telDetil {width:50px;}
    .sanjiao {
      float: left;
      height:60px;
      width:50px;
      position: relative;
    }
    .jiaoOne {
      width: 0;
      height: 0;
      border-top:8px solid transparent;
      border-bottom: 8px solid transparent;
      border-left: 8px solid #b4b4b4;
      position: absolute;
      left: 50%;
      top:50%;
      margin-left: -4px;
      margin-top: -8px;
    }
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
    /*特级*/
    .must {
      background: #5d77e5;
    }
    .province {
      background: #6cb92d;
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
    .telAll img {
      width:20px;
      margin-top: 20px;
    }
    .allLast {display: none;}
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
    .rankingBtn {
      float: right;
      width:80px;
      height:30px;
      margin-top: 15px;
      text-align: center;
      line-height: 30px;
      margin-right: 10px;
      border:1px solid #00d747;
      -webkit-border-radius:5px;
      -moz-border-radius:5px;
      border-radius:5px;
      color: #00d747;
    }
  </style>
</head>
<body>
<header class="header">
  <h1>直属团队进销单详情</h1>
  <a href="#" onclick="javascript :history.back(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
  <div class="teamTop">
    <img src="${ctx}/seatch.png" />
    <input type="search" class="searchInput" placeholder="请输入姓名或手机号" />
    <img src="${ctx}/searchBtn.png" class="seatchImg" onclick="seatch()" />
    <div class="searchBtn" onclick="seatch()">搜索</div>
  </div>
  <div class="numberList">
    <div class="all" change="false" onclick="showList(this)">
      <div class="rankingAll">
        <div class="ranking">
          <div class="sanjiao">
            <div class="jiaoOne"></div>
          </div>
          <span class="rankingTop">特级服务商</span>
          <span class="rankingSpan must">特级</span>
        </div>
        <span class="tel telDetil">${fn:length(v4)}人</span>
      </div>
    </div>
    <div class="all allLast">
      <div class="rankingAllList">
        <c:forEach items="${v4}" var="user">
          <div class="rankingAll">
            <img src="${user.avatar}" />
            <div class="ranking">
              <span>${user.nickname}</span>
            </div>
            <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
            <span class="tel">${user.phone}</span>
          </div>
        </c:forEach>
      </div>
    </div>

    <div class="all" change="false" onclick="showList(this)">
      <div class="rankingAll">
        <div class="ranking">
          <div class="sanjiao">
            <div class="jiaoOne"></div>
          </div>
          <span class="rankingTop">省级服务商</span>
          <span class="rankingSpan province">省级</span>
        </div>
        <span class="tel telDetil">${fn:length(v3)}人</span>
      </div>
    </div>
    <div class="all allLast">
      <div class="rankingAllList">
        <c:forEach items="${v3}" var="user">
          <div class="rankingAll">
            <img src="${user.avatar}" />
            <div class="ranking">
              <span>${user.nickname}</span>
            </div>
            <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
            <span class="tel">${user.phone}</span>
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="all" change="false" onclick="showList(this)">
      <div class="rankingAll">
        <div class="ranking">
          <div class="sanjiao">
            <div class="jiaoOne"></div>
          </div>
          <span class="rankingTop">市级服务商</span>
          <span class="rankingSpan city">市级</span>
        </div>
        <span class="tel telDetil">${fn:length(v2)}人</span>
      </div>
    </div>
    <div class="all allLast">
      <div class="rankingAllList">
        <c:forEach items="${v2}" var="user">
          <div class="rankingAll">
            <img src="${user.avatar}" />
            <div class="ranking">
              <span>${user.nickname}</span>
            </div>
            <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
            <span class="tel">${user.phone}</span>
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="all" change="false" onclick="showList(this)">
      <div class="rankingAll">
        <div class="ranking">
          <div class="sanjiao">
            <div class="jiaoOne"></div>
          </div>
          <span class="rankingTop">VIP服务商</span>
          <span class="rankingSpan VIP">VIP</span>
        </div>
        <span class="tel telDetil">${fn:length(v1)}人</span>
      </div>
    </div>
    <div class="all allLast">
      <c:forEach items="${v1}" var="user">
        <div class="rankingAll">
          <img src="${user.avatar}" />
          <div class="ranking">
            <span>${user.nickname}</span>
          </div>
          <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
          <span class="tel">${user.phone}</span>
        </div>
      </c:forEach>
    </div>
    <div class="all" change="false" onclick="showList(this)">
      <div class="rankingAll">
        <div class="ranking">
          <div class="sanjiao">
            <div class="jiaoOne"></div>
          </div>
          <span class="rankingTop">普通用户</span>
          <span class="rankingSpan com">普通</span>
        </div>
        <span class="tel telDetil">${fn:length(v0)}人</span>
      </div>
    </div>
    <div class="all allLast">
      <c:forEach items="${v0}" var="user">
        <div class="rankingAll">
          <img src="${user.avatar}" />
          <div class="ranking">
            <span>${user.nickname}</span>
          </div>
          <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
          <span class="tel">${user.phone}</span>
        </div>
      </c:forEach>
    </div>
  </div>
  </div>
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

    if($(".searchInput").val()==""){
      $(".numberList").show();
      $(".searchListShow").hide();
    }else {
      $(".numberList").hide();
      $(".searchListShow").show();
      var phoneOrmame = $(".searchInput").val();
      $.ajax({
        url : '${ctx}/u/salesVolume/ajaxTeamDetail',
        data : {
          nameorPhone:phoneOrmame
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
              buildRow(row);
            }
          }
          if (!page.data.length || page.data.length <= 0) {
            $(".searchList").show();
          }
        }
      });
    }
  }
  function buildRow(row,indexs){
    var rowTpl = document.getElementById('rowTpl').innerHTML;
    laytpl(rowTpl).render(row,function(html) {
      $('.searchListShow').append(html);
    });
  }
</script>
<script id="rowTpl" type="text/html">
  <div class="rankingAllList">
    <div class="rankingAll">
      <img src="{{d.avatar}}" />
      <div class="ranking">
        <span>{{ d.nickname }}</span>
        <span class="rankingSpan {{ d.userRank =='V4'?'must':d.userRank =='V3'?'province':d.userRank =='V2'?'city':d.userRank =='V1'?'VIP':d.userRank=='V0'?'com':''}}">{{d.userRank =='V4'?'特级':d.userRank =='V3'?'省级':d.userRank =='V2'?'市级':d.userRank =='V1'?'VIP':d.userRank =='V0'?'普通':''}}</span>
      </div>
      <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId={{ d.id }}&userName={{ d.nickname }}"  class="rankingBtn">查看销量</a>
      <span class="tel">{{d.phone}}</span>

    </div>
  </div>
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
