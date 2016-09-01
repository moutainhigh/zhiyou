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

<title>订单发货</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script type="text/javascript">
  $(function() {
    
    //验证
    $(".valid-form").validate({
      rules : {
        'logisticsName' : {
          required : true
        },
        'logisticsNo' : {
          required : true
        }
      }
    });

  });
</script>
</head>
<body class="">
  <header class="header">
    <h1>订单发货</h1>
    <a href="${ctx}/u/order" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/order/deliver" class="valid-form" method="post">
      <input type="hidden" name="orderId" value="${orderId}">
      <div class="form-group mt-10">
        <div class="form-title">请填写您的物流信息</div>
        <div class="form-item">
          <label class="control-label" for="name">物流公司名</label>
          <input type="text" name="logisticsName" class="control-input" value="" placeholder="填写物流公司名">
        </div>
        <div class="form-item">
          <label class="control-label" for="name">物流单号</label>
          <input type="text" name="logisticsNo" class="control-input" value="" placeholder="填写物流单号">
        </div>
      </div>
      
      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="确认发货">
      </div>
    </form>
  </article>

</body>
</html>
