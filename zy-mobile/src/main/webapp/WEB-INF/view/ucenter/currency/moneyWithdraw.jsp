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

<title>本金提现</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
$(function() {
	var withdrawFeeRate = ${withdrawFeeRate};
	$('#amount').bind('input propertychange', function () {
		var textValue = $.trim($(this).val());
		var value = textValue
		if(textValue.indexOf('.') == 0) {
			value = $(this).val().replace(/^\.(\d)(\d).*$/,'0.$1$2');
		} else {
			value = $(this).val().replace(/^(\d+)\.(\d)(\d).*$/,'$1.$2$3');
		}
		
		if(textValue == '') {
			$('.form-btn button').removeClass('orange').addClass('disabled').attr('href','button');
		} else {
			var accountBalance = Number($('#accountBalance').text());
			if(textValue > accountBalance) {
				value = accountBalance;
			}
			$('.form-btn button').addClass('orange').removeClass('disabled').attr('type','submit');
		}
		$(this).val(value);
		var realAmount = Number(value) * (1 - withdrawFeeRate);
		$('#realAmount').text(realAmount.toFixed(2));
	});
});
</script>
</head>
 
<body class="header-fixed">

  <header class="header">
    <h1>本金提现</h1>
    <a href="${ctx}/u/money" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="form-group mt-20 clearfix">
      
      <form id="formMoney" method="post" action="${ctx}/u/money/withdraw" >
      
        <div class="form-group">
          <div class="form-item">
            <label class="control-label lh-36">提现到</label>
            <%-- <div class="form-control-static text-right font-555 lh-36"><i class="icon icon-bank-gongshang"></i> 工商银行（6321）</div> --%>
            <div class="form-control-static text-right font-555 lh-36"><i class="icon icon-weixin"></i> 微信零钱</div>
          </div>
          </div>
        <div class="form-group">
          <div class="form-item">
            <label for="holder" class="control-label lh-48 fs-18">金额：</label>
            <input type="number" class="control-input lh-48 fs-18" id="amount" name="amount" placeholder="输入提现金额" value="">
            <div class="help-block">可提现金额 <em id="accountBalance" class="font-orange fs-16 bold">${amount}</em> 元</div>
          </div>
          <div class="help-block lh-30">
            <p> <i class="fa fa-exclamation-circle"></i> 预计${date}前到账</p>
            <p> <i class="fa fa-exclamation-circle"></i> 预计到账金额：<em id="realAmount" class="font-orange bold">0.00</em> 元</p>
          </div>
        </div>
        
        <div class="form-btn">
          <button type="button" class="btn btn-block disabled round-4">确认提现</button>
        </div>
        
      </form>
  </article>
  
  <a href="${ctx}/help/money" class="abs-lb mb-10 width-100p font-999 fs-12 text-center"><i class="fa fa-question-circle-o"></i> 提现说明</a>

</body>
   
</html>
