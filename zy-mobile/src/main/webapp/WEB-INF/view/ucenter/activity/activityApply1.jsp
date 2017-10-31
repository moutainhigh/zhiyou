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

<title>支付</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
  <script src="${stc}/js/layer/layer.js"></script>
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
  .hide {
    display: none;
  }
  .acitivityANew {display: none;}
  .acitivityANew img{
    display: block;
    width:100%;
  }
  .ANewtrue {
    position: fixed;
    bottom:0;
    left:0;
    width:100%;
    height:48px;
    color: #fff;
    background: #6cb92d;
    font-size: 14px;
    line-height:48px;
    text-align: center;
  }
  .ANeInput {
    padding:10px 15px;
    margin-bottom: 48px;
    border-top:1px solid #eee;
    background: #fff;
  }
  .ANeInput p,.ANeInput input {
    float: left;
    height:30px;
    font-size: 16px;
    line-height: 30px;
  }
  .ANeInput p {
    width:35%;
    color: #333;
  }
  .ANeInput input {
    width:65%;
    color: #555;
    border:none;
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
          number : pass,
          activityApplyId:$("#activityApplyId").val()
        },
        dataType : 'json',
        type : 'POST',
        success : function(result) {
          var code = result.code;
          if(code!=0){
            messageShow("邀请码输入不正确，请重新输入！");
          }else{
            messageShow("报名成功");
            $(".button-left").attr("href","${ctx}/activity/${activityId}");
            $("#fa-angle-left").click();
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

  <header class="header">
    <h1>活动订单支付</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left" id="fa-angle-left"></i></a>
  </header>
   <div class="acitivityANew">
     <img src="http://image.zhi-you.net/image/9cbac3a0-96b5-4e6e-9cb1-40da63cd240f@450h_750w_1e_1c.jpg" />
     <div class="ANeInput clearfloat">
          <p>输入邀请码</p>
          <input type="number" placeholder="请输入邀请码" />
     </div>
     <div class="ANewtrue" onclick="Anew()">确认</div>
   </div>
  <form action="${ctx}/u/activity/activityApply" class="valid-form"  method="post">
    <input type="hidden" name="activityApplyId" id ="activityApplyId" value="${activityApplyId}">

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
