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
  </style>
  <script type="text/javascript">
    $(function() {
      $('#userPay').click(function() {
        $('.payer-phone').addClass('hide');
        $('#phone').val('');
      });

      $('#otherPay').click(function() {
        $('.payer-phone').removeClass('hide');
      });

      $('#btnSubmit').click(function() {
        var payChecked = $('#otherPay').is(':checked');
        if (payChecked) {
          var val = $('#phone').val();
          if (!val) {
            messageShow('请输入代付人手机号', 'error', 2);
            return;
          }
        }
        $('.valid-form').submit();
      });
    });

    $(function(){
      test();
    });

    function  test() {
      var flage = ${falge};
      if(flage) {
        $(".valid-form").hide();
        $(".acitivityANew").show();
        $(".header h1").html("填写邀请码");
      }
    }
    function Anew(){
      var pass=$(".ANeInput input").val();
      $.ajax({
        url : '${ctx}/u/activity/ndtInviteNumber',
        data : {
          number : pass
        },
        dataType : 'json',
        type : 'POST',
        success : function(result) {
          var code = result.code;
          if(code!=0){
            messageShow("邀请码输入不正确，请重新输入！");
          }else{
            $(".valid-form").show();
            $(".acitivityANew").hide();
            $(".header h1").html("活动订单支付");
          }
        }
      });
    }

  </script>

</head>
<body >

<%--<header class="header">--%>
<%--<h1>活动订单支付</h1>--%>
<%--<a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>--%>
<%--</header>--%>
<a href="javascript:history.go(-1);" class="backNew">
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

<form action="${ctx}/u/activity/activityApply" class="valid-form"  method="post">
  <input type="hidden" name="activityApplyId" value="${activityApplyId}">

  <article class="mt-15 mb-15 clearfix">
    <div class="list-title">单据信息</div>
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">标题</div>
        <div class="list-unit">${title}</div>
      </div>
    </div>

    <div class="list-title">支付信息</div>
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">支付金额</div>
        <div class="list-unit">
          <span class="font-orange">${amount}</span>元
        </div>
      </div>
    </div>

    <div class="list-title">支付方式</div>
    <div class="list-group">
      <div class="list-item form-radio">
        <label class="list-text" for="userPay">自己支付</label>
        <div class="list-unit">
          <input id="userPay" type="radio" name="userPay" value="true" checked="checked" >
          <label class="i-checked" for="userPay"></label>
        </div>
      </div>
      <%--<div class="list-item form-radio">--%>
      <%--<label class="list-text" for="otherPay">他人代付</label>--%>
      <%--<div class="list-unit">--%>
      <%--<input id="otherPay" type="radio" name="userPay" value="false">--%>
      <%--<label class="i-checked" for="otherPay"></label>--%>
      <%--</div>--%>
      <%--</div>--%>
      <%--<div class="list-item  payer-phone hide">--%>
      <%--<div class="list-text">代付人手机</div>--%>
      <%--<div class="list-unit"><input type="text" name="payerPhone" id="phone" value="" /></div>--%>
      <%--</div>--%>
    </div>


    <div class="form-btn ">
      <a id="btnSubmit" href="javascript:;" class="btn green btn-block round-2">下一步</a>
    </div>

  </article>
</form>
</body>
</html>
