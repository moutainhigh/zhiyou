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

<title>余额充值</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script type="text/javascript">
  $(function() {
    
    $(".valid-form").validate({
      ignore: ':hidden',
      rules : {
        'payType' : {
          required : true
        },
        'amount' : {
          required : true
        }
      },
      submitHandler : function(form) {
        var payType = $('input[name="payType"]:checked');
        if(payType.length == 0) {
          messageFlash('请选择支付方式');
          return;
        }
        $(form).find(':submit').prop('disabled', true);
        form.submit();
      }
    });
  });
</script>
</head>

<body>

  <header class="header">
    <h1>余额充值</h1>
    <a href="${ctx}/u/money" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="account-wrap clearfix">
    <form id="form" action="${ctx}/u/pay" class="valid-form" method="post">
      <div class="list-group">
        <div class="list-title">请输入充值金额</div>
        <div class="list-item">
          <label for="amount" class="list-label lh-48 fs-18">金额：</label>
          <div class="list-text">
            <input type="number" class="form-input lh-48 fs-18" name="amount" placeholder="输入充值金额" value="">
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">支付方式</div>
        <!-- form-radio -->
        <%-- 
        <label class="list-item form-radio" for="payType0">
          <div class="list-text">在线支付</div>
          <div class="list-unit">
            <input id="payType0" type="radio" name="payType" value="0">
            <em class="i-checked"></em>
          </div>
        </label>
        --%>
        <label class="list-item form-radio" for="payType1">
          <div class="list-text">银行汇款</div>
          <div class="list-unit">
            <input id="payType1" type="radio" name="payType" value="1" checked="checked">
            <em class="i-checked"></em>
          </div>
        </label>
      </div>
      
      <div class="form-btn">
        <input type="submit" class="btn orange btn-block round-2" value="下一步">
      </div>
    </form>
  </article>
  
  <a href="${ctx}/help/money" class="abs-lb mb-10 width-100p font-999 fs-12 text-center"><i class="fa fa-question-circle-o"></i> 余额问题</a>
</body>
</html>
