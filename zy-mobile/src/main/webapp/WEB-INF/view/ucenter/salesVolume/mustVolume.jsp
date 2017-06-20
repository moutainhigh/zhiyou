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

  <title>直属特级进销单详情</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <style>
    body {background: #f7f7f9}
    .all {
      background: #fff !important;
      margin-top: 10px;
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
    .TeamName span {
      display: block;
      margin-top:10px;
    }
    .rankingAll {
      border-bottom: 1px solid #e5e5e5;
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
      height: 40px;
      margin-top: 10px;
      margin-bottom: 10px;
      margin-left: 15px;
      -webkit-border-radius:50%;
      -moz-border-radius:50%;
      border-radius:50%;
    }
    .ranking {
      float: left;
      width:120px;
      height:100%;
      text-align: center;
      line-height: 60px;
    }
    .allLast {
      margin-bottom: 10px;
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
  </style>
</head>
<body>
<header class="header">
  <h1>直属特级进销单详情</h1>
  <a href="${ctx}/u/salesVolume/salesVolume" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
  <div class="teamTop">
    <img src="${ctx}/seatch.png" />
    <input type="search" class="searchInput" placeholder="请输入姓名或手机号" />
    <img src="${ctx}/searchBtn.png" class="seatchImg" onclick="seatch()" />
    <div class="searchBtn" onclick="seatch()">搜索</div>
  </div>
  <div class="all allLast">
    <div class="rankingAll">
      <img src="${ctx}/headPortrait.png" />
      <div class="ranking">
        <span>赵春华</span>
      </div>
      <span class="tel">13656174839</span>
      <a href="#"  class="rankingBtn">查看销量</a>
    </div>
    <div class="rankingAll">
      <img src="${ctx}/headPortrait.png" />
      <div class="ranking">
        <span>赵春华</span>
      </div>
      <span class="tel">13656174839</span>
      <a href="#" class="rankingBtn">查看销量</a>
    </div>
    <div class="rankingAll">
      <img src="${ctx}/headPortrait.png" />
      <div class="ranking">
        <span>赵春华</span>
      </div>
      <span class="tel">13656174839</span>
      <a href="#" class="rankingBtn">查看销量</a>
    </div>
  </div>
</article>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
