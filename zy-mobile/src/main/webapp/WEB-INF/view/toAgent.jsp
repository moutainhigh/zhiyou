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
<link href="${stccdn}/css/product.css" rel="stylesheet" />

<script>
  $(function() {

  });
</script>

</head>
<body class="product-list">

  <header class="header">
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a> <a class="button-right" href="${ctx}/help"><i class="fa fa-question-circle"></i></a>
    <h1>成为代理</h1>
  </header>
  
  <div class="note note-warning mb-0">
    <p>
      <i class="fa fa-exclamation-circle"></i>购买下列套餐产品即可成为相应代理。
    </p>
  </div>
  
  <article class="mb-15 clearfix">
    <form id="form" action="${ctx}/order/create" method="get">
      <input type="hidden" name="productId" value="${product.id}">
      <div class="list-group">
        <!-- form-radio -->
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel0" type="radio" name="agentLevel" value="0">
            <label class="i-checked" for="agentLevel0"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <label class="list-text" for="agentLevel0">
            <h2 class="fs-15 lh-24">智优生物-优检一生代理套餐 300盒</h2>
            <div class="lh-30"><label class="label red">特级代理</label></div>
            <div class="font-orange lh-24">¥ 32400.00</div>
          </label>
        </div>
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel1" type="radio" name="agentLevel" value="1">
            <label class="i-checked" for="agentLevel1"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <label class="list-text" for="agentLevel1">
            <h2 class="fs-15 lh-24">智优生物-优检一生代理套餐 100盒</h2>
            <div class="lh-30"><label class="label yellow">一级代理</label></div>
            <div class="font-orange lh-24">¥ 10800.00</div>
          </label>
        </div>
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel2" type="radio" name="agentLevel" value="2">
            <label class="i-checked" for="agentLevel2"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <label class="list-text" for="agentLevel2">
            <h2 class="fs-15 lh-24">智优生物-优检一生代理套餐 20盒</h2>
            <div class="lh-30"><label class="label green">二级代理</label></div>
            <div class="font-orange lh-24">¥ 2560.00</div>
          </label>
        </div>
        <div class="list-item form-radio">
          <div class="list-icon">
            <input id="agentLevel3" type="radio" name="agentLevel" value="3">
            <label class="i-checked" for="agentLevel3"></label>
          </div>
          <img class="image-80 block mr-10" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
          <label class="list-text" for="agentLevel3">
            <h2 class="fs-15 lh-24">智优生物-优检一生代理套餐 5盒</h2>
            <div class="lh-30"><label class="label blue">三级代理</label></div>
            <div class="font-orange lh-24">¥ 840.00</div>
          </label>
        </div>
        
      </div>
      
      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="去下单">
      </div>
      
    </form>
  </article>

</body>
</html>
