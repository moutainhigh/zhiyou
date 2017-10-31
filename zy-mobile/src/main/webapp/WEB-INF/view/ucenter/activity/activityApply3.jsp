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
  <title>支付</title>
  <!--移动端版本兼容 -->
  <script type="text/javascript">
    var phoneWidth =  parseInt(window.screen.width);
    var phoneScale = phoneWidth/750;
    var ua = navigator.userAgent;
    if (/Android (\d+\.\d+)/.test(ua)){
      var version = parseFloat(RegExp.$1);
      if(version>2.3){
        document.write('<meta name="viewport" content="width=750, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
      }else{
        document.write('<meta name="viewport" content="width=750, target-densitydpi=device-dpi">');
      }
    } else {
      document.write('<meta name="viewport" content="width=750, user-scalable=no, target-densitydpi=device-dpi">');
    }
  </script>
  <!--移动端版本兼容 end -->

  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <script src="${stc}/js/layer/layer.js"></script>
  <style>
    .acitivityANew>img{
      display: block;
      width:750px;
    }
    .ANeInput {
      position: absolute;
      left:215px;
      top:735px;
    }
    .ANeInput p {
      color: #fff;
    }
    .ANeInput img {
      position: absolute;
      top:1px;
      left:0;
    }
    .ANeInput .number{
      width: 190px;
      height: 50px;
      border:none;
      -webkit-border-radius:5px;
      -moz-border-radius:5px;
      border-radius:5px;
      position: absolute;
      top: -6px;
      left: 125px;
      font-size: 24px;
      padding-left: 10px;
    }
    .ANewtrue {
      width:320px;
      height:68px;
      background: #f2c837;
      color: #4d3205;
      font-size: 34px;
      text-align: center;
      line-height: 68px;
      -webkit-border-radius:5px;
      -moz-border-radius:5px;
      border-radius:5px;
      position: absolute;
      left:0;
      top:70px;
    }
    .backNew {
      position: fixed;
      left:20px;
      top:20px;
      z-index: 99;
    }
    .header-message .message-text {font-size: 20px !important;}
  </style>
  <script type="text/javascript">
    function Anew(){
      var pass=$(".ANeInput input").val();
      if(pass==null||pass==""){
        messageShow("邀请码不能为空，请输入！");
        return;
      }
      $.ajax({
        url : '${ctx}/u/activity/ndtInviteNumber',
        data : {
          number : pass,
          activityApplyId:${activityApplyId}
        },
        dataType : 'json',
        type : 'POST',
        success : function(result) {
          var code = result.code;
          if(code!=0){
            messageShow("邀请码输入不正确，请重新输入！");
          }else{
            messageShow("报名成功111");
             window.location.href="${ctx}/activity/${activityId}";
            /* $(".valid-form").show();
             $(".acitivityANew").hide();
             $(".header h1").html("活动订单支付");*/
          }
        }
      });
    }
    /*layer.prompt(function(value, index, elem){
     alert(value); //得到value
     layer.close(index);
     });*/
  </script>

</head>
<body >
<a href="javascript:history.go(-1);" class="backNew" id ="backNewid">
  <img src="${ctx}/images/backNew.png"  />
</a>
<div class="acitivityANew">
  <img src="${ctx}/images/backgroundNew.png" />
  <div class="ANeInput">
    <img src="${ctx}/images/yaoqing.png" style="width: 110px;" />
    <input type="number" class="number" placeholder="请输入邀请码" />
    <div class="ANewtrue" onclick="Anew()">确认</div>
  </div>

</div>

</body>
</html>

