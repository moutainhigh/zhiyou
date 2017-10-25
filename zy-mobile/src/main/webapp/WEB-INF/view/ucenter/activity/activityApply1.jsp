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
  .hide {
    display: none;
  }
  .acitivityANew  img{
    width:100%;
  }
  .ANewtrue {
    position: fixed;
    bottom:0;
    left:0;
    width:100%;
    height:60px;
    color: #fff;
    background: #6cb92d;
    font-size: 24px;
    line-height:60px;
    text-align: center;
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


  function  test() {
    var flage = ${falge};
    if(flage){

      layer.prompt(function(pass){
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
              alert("验证不通过 停留在这个页面");
            }

          }

        });
      },function () {
        alert("ssss");
      });
  }
    /*layer.prompt(function(value, index, elem){
      alert(value); //得到value
      layer.close(index);
    });*/
  }
</script>

</head>
<body  onload=test()>

  <header class="header">
    <h1>活动订单支付</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
   <div class="acitivityANew">
     <img src="http://image.zhi-you.net/editor/66d43985-0b02-44d0-9b48-794d1ae5f945" />

     <div class="ANewtrue">确认支付</div>
   </div>
  <form action="${ctx}/u/activity/activityApply" class="valid-form"  method="post" style="display: none;">
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
