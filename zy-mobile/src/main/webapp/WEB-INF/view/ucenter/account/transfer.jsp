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

<title>转账</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script type="text/javascript">
  $(function() {
    //验证
    $('.valid-form').validate({
      rules : {
        'amount' : {
          required : true
        },
        'toUserPhone' : {
          required : true,
          phone : true
        }
      }
    });
  });
  
</script>
</head>

<body>
  <header class="header">
    <h1>转账</h1>
    <a href="${ctx}/u/money?currencyType=0" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form id="form" method="post" action="${ctx}/u/account/transfer/create" class="valid-form">
      <div class="list-group mt-20">
        <div class="list-item">
          <label for="toUserPhone" class="list-label">转入用户手机：</label>
          <div class="list-text">
            <input type="number" class="form-input" id="toUserPhone" name="toUserPhone"  placeholder="输入转入用户手机" value="">
          </div>
        </div>
        <div class="list-item">
          <label for="amount" class="list-label lh-48 fs-18">转账金额：</label>
          <div class="list-text">
            <input type="number" class="form-input lh-48 fs-18" id="amount" name="amount" max="${amount}" min="0" placeholder="输入转账金额" value="">
            <div class="form-help pt-5 bdd-t text-right">最大转账金额 <em id="accountBalance" class="font-orange fs-16 bold">${amount}</em> 元</div>
          </div>
        </div>
      </div>
      <div class="list-title lh-30">
        <p><i class="fa fa-exclamation-circle"></i> 确认转账，资金即刻到对方账户，请慎重操作</p>
      </div>
      <div class="form-btn mt-15">
        <button type="submit" class="btn green btn-block round-2">确认转账</button>
      </div>
    </form>
  </article>

</body>

</html>
