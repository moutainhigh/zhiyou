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
    $('#amount').bind('input propertychange', function() {
      var textValue = $.trim($(this).val());
      var value = textValue
      if (textValue.indexOf('.') == 0) {
        value = $(this).val().replace(/^\.(\d)(\d).*$/, '0.$1$2');
      } else {
        value = $(this).val().replace(/^(\d+)\.(\d)(\d).*$/, '$1.$2$3');
      }

      if (textValue == '') {
        $('.form-btn button').removeClass('orange').addClass('disabled').attr('href', 'button');
      } else {
        var accountBalance = Number($('#accountBalance').text());
        if (textValue > accountBalance) {
          value = accountBalance;
        }
        $('.form-btn button').addClass('orange').removeClass('disabled').attr('type', 'submit');
      }
      $(this).val(value);
      var realAmount = Number(value) * (1 - withdrawFeeRate);
      $('#realAmount').text(realAmount.toFixed(2));
    });
    
    //选择银行卡
  	$('.bank-card-info').click(function(){
  	  showBankCardList();
  	});
    
    $('body').on('click', '.bank-card', function() {
      var $this = $(this);
      var bankCard = {};
      bankCard.id = $this.attr('data-id');
      bankCard.bankName = $this.attr('data-bank-name');
      bankCard.bankCode = $this.attr('data-bank-code');
      bankCard.cardNumber = $this.attr('data-card-number');
      setBankCard(bankCard);
    });
  });
  
  function showBankCardList() {
    if ($('#bankCardList').length == 0) {
      var bankCardList = document.getElementById('bankCardListTpl').innerHTML;
      $(bankCardList).appendTo('body');
    }
    $('body').addClass('o-hidden');
    $('#bankCardList').show().animate({
      'left' : 0
    }, 300, function() {
    });
  }
	
  function hideBankCardList() {
    $('#bankCardList').animate({
      'left' : '100%'
    }, 300, function() {
      $('body').removeClass('o-hidden');
      $('#bankCardList').hide();
    });
  }
  
  function setBankCard(bankCard) {
    hideBankCardList();
    $('[name="bankCardId"]').val(bankCard.id);
    $('.bank-card-info').html('<i class="icon icon-bank-' + bankCard.bankCode + ' mr-10"></i><span>' + bankCard.bankName + '</span>(<span>' + bankCard.cardNumber + '</span>)');
  }
  
</script>
<script id="bankCardListTpl" type="text/html">
  <aside id="bankCardList" class="header-fixed abs-lt size-100p bg-white zindex-1000" style="left: 100%; display: none;">
    <header class="header">
      <h1>选择银行卡</h1>
      <a href="javascript:hideBankCardList();" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
    <div class="list-group">
      <c:forEach items="${bankCards}" var="bankCard">
      <div class="list-item bank-card" data-id="${bankCard.id}" data-card-number="${bankCard.cardNumberLabel}" data-bank-name="${bankCard.bankName}" data-bank-code="${bankCard.bankCode}">
        <i class="icon icon-2x icon-bank-${bankCard.bankCode} mr-15"></i>
        <div class="list-text">${bankCard.bankName}</div>
        <div class="list-unit fs-16">尾号 ${bankCard.cardNumberLabel}</div>
      </div>
      </c:forEach>
    </div>
  </aside>
</script>
</head>

<body>
  <header class="header">
    <h1>${currencyType.alias}提现</h1>
    <a href="${ctx}/u/money?currencyType=${currencyType}" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form id="form" method="post" action="${ctx}/u/money/withdraw">
      <input type="hidden" name="currencyType" value="${currencyType}"/>
      <div class="list-group mt-20">
        <div class="list-item">
          <label class="list-text lh-36">提现到</label>
          <div class="list-unit bank-card-info">
            <i class="icon icon-bank-${defaultBankCard.bankCode}"></i> ${defaultBankCard.bankName}（${defaultBankCard.cardNumberLabel}）
          </div>
          <i class="list-arrow"></i>
          <input type="hidden" name="bankCardId" value="${defaultBankCard.id}">
        </div>
        <div class="list-item">
          <label for="amount" class="list-label lh-48 fs-18">金额：</label>
          <div class="list-text">
            <input type="number" class="form-input lh-48 fs-18" id="amount" name="amount" placeholder="输入提现金额" value="">
            <div class="form-help pt-5 bdd-t text-right">可提现金额 <em id="accountBalance" class="font-orange fs-16 bold">${amount}</em> 元</div>
          </div>
        </div>
      </div>
      <div class="list-title lh-30">
        <p><i class="fa fa-exclamation-circle"></i> 预计${date}前到账</p>
        <p><i class="fa fa-exclamation-circle"></i> 预计到账金额：<em id="realAmount" class="font-orange bold">0.00</em> 元</p>
      </div>
      <div class="form-btn mt-15">
        <button type="button" class="btn btn-block disabled round-2">确认提现</button>
      </div>
    </form>
  </article>

  <a href="${ctx}/help/money" class="abs-lb mb-10 width-100p font-999 fs-12 text-center"><i class="fa fa-question-circle-o"></i> 提现说明</a>

</body>

</html>
