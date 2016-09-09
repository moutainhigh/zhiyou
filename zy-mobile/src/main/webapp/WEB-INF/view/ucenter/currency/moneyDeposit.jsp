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
<script type="text/javascript">
  $(function() {
    //验证表单
    $('#orderForm').submit(function() {
      var payNum = $('input:radio[name="money"]:checked').val();
      if (payNum == null) {
        messageFlash("请选择充值的金额！");
        return false;
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
    <form id="orderForm" action="${ctx}/u/pay" method="get">
      <div class="list-group">
        <div class="list-title">请选择充值金额</div>
        <!-- form-radio -->
        <label class="list-item form-radio">
          <div class="list-icon">
            <input type="radio" name="money" value="100">
            <em class="i-checked"></em>
          </div>
          <div class="list-text">本金 100</div>
          <div class="list-unit">支付 ¥ 100.00</div>
        </label>
        <label class="list-item form-radio">
          <div class="list-icon">
            <input type="radio" name="money" value="500">
            <em class="i-checked"></em>
          </div>
          <div class="list-text">本金 500</div>
          <div class="list-unit">支付 ¥ 500.00</div>
        </label>
        <label class="list-item form-radio">
          <div class="list-icon">
            <input type="radio" name="money" value="1000">
            <em class="i-checked"></em>
          </div>
          <div class="list-text">本金 1000</div>
          <div class="list-unit">支付 ¥ 1000.00</div>
        </label>
        <label class="list-item form-radio">
          <div class="list-icon">
            <input type="radio" name="money" value="5000">
            <em class="i-checked"></em>
          </div>
          <div class="list-text">本金 5000</div>
          <div class="list-unit">支付 ¥ 5000.00</div>
        </label>
        <label class="list-item form-radio">
          <div class="list-icon">
            <input type="radio" name="money" value="10000">
            <em class="i-checked"></em>
          </div>
          <div class="list-text">本金 10000</div>
          <div class="list-unit">支付 ¥ 10000.00</div>
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
