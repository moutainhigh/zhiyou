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
      ignore: ':hidden',
      rules : {
        'useLogistics' : {
          required : true
        },
        'logisticsName' : {
          required : true
        },
        'logisticsSn' : {
          required : true
        }
      },
      submitHandler : function(form) {
        var deliverType = $('input[name="useLogistics"]:checked');
        if(deliverType.length == 0) {
          messageFlash('请选择发货方式');
          return;
        }
        $(form).find(':submit').prop('disabled', true);
        form.submit();
      }
    });
    
    $('#deliverType1').click(function() {
      $('#logistics').slideDown(300);
    });
    
    $('#deliverType0').click(function() {
      $('#logistics').slideUp(300);
    });
    
  });
</script>
</head>
<body>
  <header class="header">
    <h1>订单发货</h1>
    <a href="${ctx}/u/order" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/order/deliver" class="valid-form" method="post">
      <input type="hidden" name="id" value="${orderId}">
      
      <div class="list-group">
        <div class="list-title">请选择发货方式</div>
        <div class="list-item form-radio">
          <label class="list-text" for="deliverType0">面对面发货</label>
          <div class="list-unit">
            <input id="deliverType0" type="radio" name="useLogistics" value="false">
            <label class="i-checked" for="deliverType0"></label>
          </div>
        </div>
        <div class="list-item form-radio">
          <label class="list-text" for="deliverType1">物流发货</label>
          <div class="list-unit">
            <input id="deliverType1" type="radio" name="useLogistics" value="true">
            <label class="i-checked" for="deliverType1"></label>
          </div>
        </div>
      </div>
      
      <div id="logistics" class="list-group hide">
        <div class="list-title">请填写您的物流信息</div>
        <div class="list-item">
          <label class="list-label" for="logisticsName">物流公司名</label>
          <div class="list-text">
            <input type="text" name="logisticsName" class="form-input" value="" placeholder="填写物流公司名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="logisticsSn">物流单号</label>
          <div class="list-text">
            <input type="text" name="logisticsSn" class="form-input" value="" placeholder="填写物流单号">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="logisticsFee">物流费用</label>
          <div class="list-text">
            <input type="text" name="logisticsFee" class="form-input" value="" placeholder="填写物流费用">
          </div>
        </div>
      </div>
      
      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="确认发货">
      </div>
      
    </form>
  </article>

</body>
</html>
