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
<style>
  .hide {
    display: none;
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
     var payTypeVal = $('input:radio[name="payType"]:checked').val();
     if (payTypeVal == null && !payChecked) {
      messageShow('请选择支付类型', 'error', 2);
      return;
    }

    if (payTypeVal == '0' && !payChecked) {
      $.dialog({
        content : '您确定积分支付？',
        callback : function(index) {
          if (index == 1) {
            $('#form').submit();
          }
        }
      });
      return;
    }
     $('#form').submit();
   });
 });
</script>

</head>
<body>

  <header class="header">
    <h1></h1>活动订单支付</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <form action="${ctx}/u/pay/activityApply/payment" class="valid-form" id="form" method="post">
    <input type="hidden" name="activityId" value="${activityId}">
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

      <div class="list-title">支付类型</div>
      <div class="list-group">
        <div class="list-item form-radio">
          <label class="list-text" for="shengpay">盛付通支付</label>
          <div class="list-unit">
            <input id="shengpay" type="radio" name="payType" value="6" >
            <label class="i-checked" for="shengpay"></label>
          </div>
        </div>
        <div class="list-item form-radio">
          <label class="list-text" for="otherPay">积分支付</label>
          <div class="list-unit">
            <input id="pointPay" type="radio" name="payType" value="0">
            <label class="i-checked" for="pointPay"></label>
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
        <div class="list-item form-radio">
          <label class="list-text" for="otherPay">他人代付</label>
          <div class="list-unit">
            <input id="otherPay" type="radio" name="userPay" value="false">
            <label class="i-checked" for="otherPay"></label>
          </div>
        </div>
        <div class="list-item  payer-phone hide">
          <div class="list-text">代付人手机</div>
          <div class="list-unit"><input type="text" name="payerPhone" id="phone" value="" /></div>
        </div>
      </div>


      <div class="form-btn ">
        <a id="btnSubmit" href="javascript:;" class="btn green btn-block round-2">确认支付</a>
      </div>

    </article>
  </form>
</body>
</html>
