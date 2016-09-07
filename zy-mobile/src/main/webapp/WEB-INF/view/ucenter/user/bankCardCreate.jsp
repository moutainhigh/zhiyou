<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
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
<meta name="keywords" content="微信分销" />
<meta name="description" content="新增银行卡" />

<title>新增银行卡</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script src="${stccdn}/js/area.js"></script>
<script>
  $(function() {
    
    $('.valid-form').validate({
      rules : {
        'bankId' : {
          required : true
        },
        'bankName' : {
          required : true
        },
        'realname' : {
          required : true
        },
        'cardNumber' : {
          required : true
        },
        'bankBranchName' : {
          required : true
        }
      },
      messages : {
        'bankId' : {
          required : '请选择开户银行'
        },
        'bankName' : {
          required : '请选择开户银行'
        },
        'realname' : {
          required : '请输入开户姓名'
        },
        'cardNumber' : {
          required : '请输入银行卡号'
        },
        'bankBranchName' : {
          required : '请输入开户支行名'
        }
      }
    });
    
    //选择银行
  	$('.bank-info').click(function(){
  	  showBankList();
  	});
    
    $('body').on('click', '.bank', function() {
      var $this = $(this);
      var bank = {};
      bank.id = $this.attr('data-id');
      bank.name = $this.attr('data-name');
      bank.code = $this.attr('data-code');
      setBank(bank);
    });
    
  });
  
  function showBankList() {
    if ($('#bankList').length == 0) {
      var bankList = document.getElementById('bankListTpl').innerHTML;
      $(bankList).appendTo('body');
    }
    $('body').addClass('o-hidden');
    $('#bankList').show().animate({
      'left' : 0
    }, 300, function() {
    });
  }
	
  function hideBankList() {
    $('#bankList').animate({
      'left' : '100%'
    }, 300, function() {
      $('body').removeClass('o-hidden');
      $('#bankList').hide();
    });
  }
  
  function setBank(bank) {
    hideBankList();
    $('[name="bankId"]').val(bank.id);
    $('[name="bankName"]').val(bank.name);
    $('.bank-name').html('<i class="icon icon-bank-' + bank.code + ' mr-10"></i><span>' + bank.name + '</span>');
  }
</script>
<script id="bankListTpl" type="text/html">
  <aside id="bankList" class="bank-list header-fixed abs-lt size-100p bg-white z-1000" style="left: 100%; display: none;">
    <header class="header">
      <h1>选择银行</h1>
      <a href="javascript:hideBankList();" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
    <div class="list-group">
      <c:forEach items="${banks}" var="bank">
      <div class="list-item bank" data-id="${bank.id}" data-name="${bank.name}" data-code="${bank.code}">
        <i class="icon icon-2x icon-bank-${bank.code} mr-15"></i>
        <div class="list-text">${bank.name}</div>
      </div>
      </c:forEach>
    </div>
  </aside>
</script>
</head>
<body>
  <header class="header">
    <h1>新增银行卡</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <article class="bank-card-create">
    <form id="bankCardForm" class="valid-form" action="${ctx}/u/bankCard/create" method="post">
      <div class="form-message note note-warning mb-0 hide">
        <p>输入信息有误，请先更正。</p>
      </div>
      <div class="list-group">
        <div class="list-item bank-info">
          <label class="list-label">开户银行</label>
          <div class="list-text">
            <span class="bank-name"><span class="font-777">请选择</span></span>
            <input type="hidden" name="bankId" value="">
            <input type="hidden" name="bankName" value="">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="accountName">开户姓名</label>
          <div class="list-text">
            <input type="text" id="realname" name="realname" class="form-input" value="" placeholder="填写开户姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="accountNo">银行卡号</label>
          <div class="list-text">
            <input type="text" id="cardNumber" name="cardNumber" class="form-input" value="" placeholder="填写银行卡号">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="bankBranchName">开户支行名</label>
          <div class="list-text">
            <input type="text" id="bankBranchName" name="bankBranchName" class="form-input" value="" placeholder="填写开户支行名称">
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">设为默认银行卡</div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_isDefault" value="false">
            <input type="checkbox" id="isDefault" name="isDefault" value="true">
            <label class="i-switch" for="isDefault"></label>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="保 存">
      </div>
    </form>
  </article>

</body>
</html>
