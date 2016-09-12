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

<title>余额支付</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/activity.css">
<script type="text/javascript">
  $(function() {
	  $("#btnSubmit").click(function(){
		  	
      });
  });
</script>

</head>
<body>

  <header class="header">
    <h1>余额支付</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <div class="note note-warning mb-0">
    <p><i class="fa fa-exclamation-circle"></i> 对不起，您账户余额不足！</p>
  </div>
  
  <article class="mt-15 mb-15 clearfix">
     <div class="list-title">订单信息</div>
     <div class="list-group">
       <div class="list-item">
          <div class="activity">
            <figure class="abs-lt image-wrap">
              <img src="http://image.zhi-you.net/image/aa7af759-93b8-436b-bbe2-ece54163c0e3@160h_240w_1e_1c.jpg">
            </figure>
            <h2>智优生物上海总部宣讲会</h2>
            <div class="font-999 fs-12 lh-20">数量：2件 </div>
            <div class="font-999 fs-12 lh-20">付款金额：99元</div>
          </div>
       </div>
     </div>  
     
     <div class="list-title">账户信息</div> 
     <div class="list-group">
       <div class="list-item">
         <div class="list-text">账户余额</div>
         <div class="list-unit">999元</div>
       </div>
       <div class="list-item">
         <div class="list-text">支付金额</div>
         <div class="list-unit">99元</div>
       </div>
     </div>
     
     <div class="form-btn">
        <div id="btnSubmit" class="btn orange btn-block round-2">确认支付</div>
     </div>
  </article>  

</body>
</html>
