<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
  <meta content="no-cache" http-equiv="pragma" />
  <meta content="no-cache, must-revalidate" http-equiv="Cache-Control" />
  <meta content="Wed, 26 Feb 1997 08:21:57GMT" http-equiv="expires">
  <meta name="format-detection" content="telephone=no"/>
  <title>票务详情</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <!--移动端版本兼容 -->
  <script type="text/javascript">
    var phoneWidth =  parseInt(window.screen.width);
    var phoneScale = phoneWidth/640;
    var ua = navigator.userAgent;
    if (/Android (\d+\.\d+)/.test(ua)){
      var version = parseFloat(RegExp.$1);
      if(version>2.3){
        document.write('<meta name="viewport" content="width=640, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
      }else{
        document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
      }
    } else {
      document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
    }
  </script>
  <!--移动端版本兼容 end -->
  <style>
    *{margin:0;padding: 0;border:none;}
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
    .main {
      width:640px;
    }
    .erweima {
      float: left;
      width:320px;
      border-bottom:1px dashed #333;
    }
    .erweima:nth-child(2n-1) {
      border-right:1px dashed #333;
      box-sizing:border-box;
    }
    .erweima:last-child {
      border-bottom:none;
    }

    .erweima img {
      width:240px;
      height:240px;
      margin:40px;
      margin-bottom:20px;
    }
    .erweima div {
      width:320px;
      height:70px;
      text-align: center;
      line-height: 30px;
      font-size: 30px;
      color: red;
      padding-bottom:10px;
    }
    .error-font {
      padding:30px 30px 0 30px;
      color: red;
      font-size:24px;
      text-align: left;
    }
  </style>
</head>
<body>
<%--<header class="header">--%>
  <%--<a href="${ctx}/u/activity/teamApplyList" class="button-left"><i class="fa fa-angle-left"></i></a>--%>
  <%--<h1>票务详情</h1>--%>
<%--</header>--%>
<p class="error-font"><b>注意：*购票成功，请务必将二维码发送给好友，让好友识别二维码报名。否则好友现场无法签到！！！</b></p>
<div class="main clearfloat">

  <c:if test="${empty activityTickets}">
    <div class="page-empty" style="display: none;">
      <i class="fa fa-file-o"></i>
      <span>空空如也!</span>
    </div>
  </c:if>


  <c:if test="${not empty activityTickets}">
    <c:forEach items="${activityTickets}" var="activityTicket" >
      <div class="erweima">
        <img src="${activityTicket.codeImageUrl}" class="erweimaImg"/>
        <c:if test="${not empty activityTicket.usedUser && activityTicket.isUsed == 1}">
          <div>使用者：${activityTicket.usedUser.nickname}</div>
          <div><a href="tel:${activityTicket.usedUser.phone}" style="">${activityTicket.usedUser.phone}&nbsp;&nbsp;<i class="fa fa-phone font-blue"></i></a></div>
        </c:if>
        <c:if test="${empty activityTicket.usedUser && activityTicket.isUsed == 0}">
          <div><p style="color: limegreen">无人使用</p></div>
        </c:if>
      </div>
    </c:forEach>
  </c:if>
</div>
<script>
  $(function(){
    if($(".erweima").length%2==0){
      $(".erweima:last-child").prev(".erweima").css("border-bottom","none");
    }
  })
</script>
</body>
</html></html>
