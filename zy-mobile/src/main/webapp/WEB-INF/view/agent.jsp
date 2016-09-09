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

<title>成为代理</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<style>
.form-radio.current {
  background-color: #f1f1f1;
}
.form-radio img {
  opacity: .7;
}
.form-radio.current img {
  opacity: 1;
}
.form-radio.current h2 {
  color: #222 !important;
}
.form-radio.current div {
  font-weight: bold;
}
</style>

<script>
  $(function() {
    
    $('.form-radio img, .form-radio .list-text').click(function(){
      $('[name="agentLevel"]').removeAttr('checked');
      $(this).parent().find('[name="agentLevel"]').click().attr('checked', 'checked');
    });
    
	$('[name="agentLevel"]').click(function(){
	  var item = $(this).parents('.form-radio');
	  item.addClass('current').siblings().removeClass('current');
	  var quantity = item.find('.quantity').text();
	  $('[name="quantity"]').val(quantity);
	});
	
	$('#btnSubmit').click(function(){
	  if(!$('[name="quantity"]').val()) {
	    messageFlash('请选择代理套餐');
	    return false;
	  }
	  var phone = $('#phone').val();
	  if(!phone) {
	    messageFlash('请填写上级手机号');
	    return false;
	  }
	  $.ajax({
	    url: '${ctx}/u/agent/checkPhone',
	    data: {
	      phone: phone,
	    },
	    type: 'POST',
	    dataType: 'JSON',
	    success: function(result){
	      if(result.code == 0) {
	        $('[name="parentId"]').val(result.message);
	        $('#form').submit();
	      } else {
	        messageShow(result.message, 'error');
	      }
	    }
	  });
	  return false;
	});
	
  });
</script>

</head>
<body>

  <header class="header">
    <h1>成为代理</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a class="button-right" href="${ctx}/help"><i class="fa fa-question-circle"></i></a>
  </header>
  
  <div class="note note-warning mb-0">
    <p>
      <i class="fa fa-exclamation-circle"></i>购买下列套餐产品即可成为相应代理。
    </p>
  </div>
  
  <article class="mb-15 clearfix">
    <form id="form" action="${ctx}/u/order/create" method="get">
      <input type="hidden" name="productId" value="${product.id}">
      <input type="hidden" name="parentId" value="${inviter.id}">
      <input type="hidden" name="quantity" value="">
      
      <div class="list-group">
        <!-- form-radio -->
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel1" type="radio" name="agentLevel" value="0">
            <label class="i-checked" for="agentLevel1"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <div class="list-text">
            <h2 class="font-777 fs-15 lh-24">${product.title}代理套餐 <span class="quantity">${quantity1}</span>盒</h2>
            <div class="lh-30"><label class="label red">一级代理</label></div>
            <div class="font-orange lh-24">¥ ${amount1}</div>
          </div>
        </div>
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel2" type="radio" name="agentLevel" value="1">
            <label class="i-checked" for="agentLevel2"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <div class="list-text">
            <h2 class="font-777 fs-15 lh-24">${product.title}代理套餐 <span class="quantity">${quantity2}</span>盒</h2>
            <div class="lh-30"><label class="label yellow">二级代理</label></div>
            <div class="font-orange lh-24">¥ ${amount2}</div>
          </div>
        </div>
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel3" type="radio" name="agentLevel" value="2">
            <label class="i-checked" for="agentLevel3"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <div class="list-text">
            <h2 class="font-777 fs-15 lh-24">${product.title}代理套餐 <span class="quantity">${quantity3}</span>盒</h2>
            <div class="lh-30"><label class="label green">三级代理</label></div>
            <div class="font-orange lh-24">¥ ${amount3}</div>
          </div>
        </div>
        
      </div>
      
      <div class="list-group">
        <div class="list-item">
          <label class="list-label" for="name">上级手机号</label>
          <div class="list-text">
            <input id="phone" name="phone" class="form-input" type="tel" value="${inviter.phone}" placeholder="输入上级代理手机号">
          </div>
        </div>
      </div>
      
      <div class="form-btn">
        <a id="btnSubmit" class="btn orange btn-block" href="javascript:;">去下单</a>
      </div>
      
    </form>
  </article>

</body>
</html>
