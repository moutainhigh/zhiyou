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

<title>U币积分支付</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<style>
  .hide {
    display: none;
  }
</style>
<script type="text/javascript">
  var amount = ${amount};
  var amount1 = ${amount1};
  var amount2 = ${amount2};
  var htmlPayLink = '<a id="btnSubmit" href="javascript:;" class="btn green btn-block round-2">确认支付</a>';
  var htmlDepositLink = '<a href="${ctx}/u/pay/deposit?payType=1" class="btn btn-block orange round-2">积分余额不足，请先充值</a>';

  $(function() {
  	var useCurrency2 = false;
    $(document).on('click','#btnSubmit',function(){
      $("#form").submit();
    });

    $('#useCurrency2').click(function() {
	    useCurrency2 = !useCurrency2;
    	if(useCurrency2) {
		    $('.amount2-text').addClass('hide');
    		$('.amount2-input').removeClass('hide').focus();

		    buildLink($('#amount2').val());
      } else {
		    $('.amount2-text').removeClass('hide');
		    $('.amount2-input').addClass('hide');
		    buildLink(0);
      }

    });

    $('#amount2').blur(function() {
	    buildLink($(this).val());
    });
  });
  function buildLink(amount2) {
	  if(Number(amount1) + Number(amount2) >= amount) {
		  $('.pay-div').html(htmlPayLink);
	  } else {
		  $('.pay-div').html(htmlDepositLink);
	  }
  }
</script>

</head>
<body>

  <header class="header">
    <h1>U币积分支付</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <c:if test="${amount > amount1}">
    <div class="note note-warning mb-0">
      <p>
        <i class="fa fa-exclamation-circle"></i> 对不起，您账户积分余额不足！
      </p>
    </div>
  </c:if>

  <form action="${ctx}/u/pay/payment" class="valid-form" id="form" method="post">
    <input type="hidden" name="orderId" value="${orderId}">
    <article class="mt-15 mb-15 clearfix">
      <div class="list-title">单据信息</div>
      <div class="list-group">
        <div class="list-item">
          <div class="list-text">标题</div>
          <div class="list-unit">${title}</div>
        </div>
        <div class="list-item">
          <div class="list-text">单据号</div>
          <div class="list-unit">${sn}</div>
        </div>
      </div>

      <div class="list-title">支付信息</div>
      <div class="list-group">
        <div class="list-item">
          <div class="list-text">账户U币余额</div>
          <div class="list-unit">${amount1}元</div>
        </div>
        <div class="list-item">
          <div class="list-text">使用积分余额</div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_useCurrency2" id="useCurrency2Val" value="false">
            <input type="checkbox" id="useCurrency2" name="useCurrency2">
            <label class="i-switch" for="useCurrency2"></label>
          </div>
        </div>
        <div class="list-item amount2-text">
          <div class="list-text">积分余额</div>
          <div class="list-unit" style="color: #d9d9d9;">${amount2}</div>
        </div>
        <div class="list-item  amount2-input hide">
          <div class="list-text">积分余额</div>
          <div class="list-unit"><input type="number" name="amount2" id="amount2" value="${amount2}" max="${amount2}"/></div>
        </div>
        <div class="list-item">
          <div class="list-text">支付金额</div>
          <div class="list-unit">
            <span class="font-orange">${amount}</span>元
          </div>
        </div>
      </div>

      <c:if test="${amount <= amount1}">
      <div class="form-btn ">
        <a id="btnSubmit" href="javascript:;" class="btn green btn-block round-2">确认支付</a>
      </div>
      </c:if>
      <c:if test="${amount > amount1}">
        <div class="form-btn pay-div">
          <a href="${ctx}/u/pay/deposit?payType=1" class="btn btn-block orange round-2">U币余额不足，请先充值</a>
        </div>
      </c:if>
    </article>
  </form>
</body>
</html>
