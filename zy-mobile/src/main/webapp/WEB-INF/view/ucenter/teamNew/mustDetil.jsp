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
  </style>
</head>
<body>
<header class="header">
  <h1>直属特级详情</h1>
  <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
  <div class="must-bu"></div>
  <div class="numberList">
    <div class="all allLast">
      <div class="rankingAllList">
        <div class="rankingAll">
          <img src="${ctx}/headPortrait.png" style="margin-left: 20px;margin-right: 20px;"/>
          <div class="ranking" style="text-align: left;font-size: 18px;">
            <span>赵春华</span>
            <img src="${ctx}/new.png"style="width: 30px;"/>
            <p class="tuijian">[推荐人：小能能]</p>
          </div>
          <div class="telAll jian" onclick="showNum(this)" change="true">
            <img src="${ctx}/jian.png" />
          </div>
          <a href="tel:13656174839" class="telAll">
            <img src="${ctx}/tel.png" />
          </a>
        </div>
        <div class="rankingNum">
          <div class="ranknumber">
            <p>6人</p>
            <p>特级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>省级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>市级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>VIP</p>
          </div>
        </div>
      </div>
      <div class="rankingAllList">
        <div class="rankingAll">
          <img src="${ctx}/headPortrait.png" style="margin-left: 20px;margin-right: 20px;"/>
          <div class="ranking" style="text-align: left;font-size: 18px;">
            <span>赵春华</span>
            <img src="${ctx}/new.png"style="width: 30px;"/>
            <p class="tuijian">[推荐人：小能能]</p>
          </div>
          <div class="telAll jian" onclick="showNum(this)" change="true">
            <img src="${ctx}/jian.png" />
          </div>
          <a href="tel:13656174839" class="telAll">
            <img src="${ctx}/tel.png" />
          </a>
        </div>
        <div class="rankingNum">
          <div class="ranknumber">
            <p>6人</p>
            <p>特级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>省级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>市级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>VIP</p>
          </div>
        </div>
      </div>
      <div class="rankingAllList">
        <div class="rankingAll">
          <img src="${ctx}/headPortrait.png" style="margin-left: 20px;margin-right: 20px;"/>
          <div class="ranking" style="text-align: left;font-size: 18px;">
            <span>赵春华</span>
            <p class="tuijian">[推荐人：小能能]</p>
          </div>
          <div class="telAll jian" onclick="showNum(this)" change="true">
            <img src="${ctx}/jian.png" />
          </div>
          <a href="tel:13656174839" class="telAll">
            <img src="${ctx}/tel.png" />
          </a>
        </div>
        <div class="rankingNum">
          <div class="ranknumber">
            <p>6人</p>
            <p>特级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>省级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>市级</p>
          </div>
          <div class="ranknumber">
            <p>6人</p>
            <p>VIP</p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="searchList">查无此人!</div>
  <div class="searchListShow">
    <div class="rankingAllList">
      <div class="rankingAll">
        <img src="${ctx}/headPortrait.png" style="margin-left: 20px;margin-right: 20px;"/>
        <div class="ranking" style="text-align: left;font-size: 18px;">
          <span>赵春华</span>
        </div>
        <div class="telAll jian" onclick="showNum(this)" change="true">
          <img src="${ctx}/jian.png" />
        </div>
        <a href="tel:13656174839" class="telAll">
          <img src="${ctx}/tel.png" />
        </a>
      </div>

    </div>
    <div class="rankingAllList">
      <div class="rankingAll">
        <img src="${ctx}/headPortrait.png" style="margin-left: 20px;margin-right: 20px;"/>
        <div class="ranking" style="text-align: left;font-size: 18px;">
          <span>赵春华</span>
        </div>
        <div class="telAll jian" onclick="showNum(this)" change="true">
          <img src="${ctx}/jian.png" />
        </div>
        <a href="tel:13656174839" class="telAll">
          <img src="${ctx}/tel.png" />
        </a>
      </div>

    </div>
    <div class="rankingAllList">
      <div class="rankingAll">
        <img src="${ctx}/headPortrait.png" style="margin-left: 20px;margin-right: 20px;"/>
        <div class="ranking" style="text-align: left;font-size: 18px;">
          <span>赵春华</span>
        </div>
        <div class="telAll jian" onclick="showNum(this)" change="true">
          <img src="${ctx}/jian.png" />
        </div>
        <a href="tel:13656174839" class="telAll">
          <img src="${ctx}/tel.png" />
        </a>
      </div>

    </div>
  </div>

  </div>
</article>
<script>
  //点击下拉箭头
  function showNum(obj){
    if($(obj).attr("change")=="true"){
      $(obj).parents(".rankingAll").next(".rankingNum").show();
      $(obj).find("img").attr("src","${ctx}/jian2.png");
      $(obj).attr("change","false");
    }else {
      $(obj).parents(".rankingAll").next(".rankingNum").hide();
      $(obj).find("img").attr("src","${ctx}/jian.png");
      $(obj).attr("change","true");
    }
  }
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
