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

<title>转账汇款</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script type="text/javascript">
  $(function() {
    $('.image-view > img').click(function() {
      var url = $(this).attr('data-src');
      var title = $(this).attr('data-title');
      if (!url) {
        return;
      }
      $.imageview({
        url : url,
        title : title
      });
    });

    $('.image-single .image-item').imageupload({
      width : 120,
      height : 75,
    });
  	
    $(".valid-form").validate({
        rules : {
          'offlineImage' : {
            required : true
          },
          'offlineMemo' : {
            required : true
          }
        }
    });
    
  });
</script>

</head>
<body>

  <header class="header">
    <h1>转账汇款</h1>
    <a href="javascript:history.go(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <div class="note note-warning mb-0">
    <p><i class="fa fa-exclamation-circle"></i> 请将本次交易金额${amount}元汇入以下银行账户</p>
  </div>
  
  <article class="mt-15 mb-15 clearfix">
     <div class="list-group">
        <div class="list-item">
          <div class="list-text">银行</div>
          <div class="list-unit">光大银行上海长宁支行</div>
        </div>
        <div class="list-item">
          <div class="list-text">卡号</div>
          <div class="list-unit">6214 9206 0274 5844</div>
        </div>
        <div class="list-item">
          <div class="list-text">收款人</div>
          <div class="list-unit">戴钟华</div>
        </div>
        <div class="list-item">
          <div class="list-text">转账金额</div>
          <div class="list-unit">${amount}元</div>
        </div>
     </div>
     
     <form action="${ctx}/u/pay/payment" class="valid-form" id="form" method="post">
     <input type="hidden" name="paymentId" value="${paymentId}" >
     <div class="list-group">
        <div class="list-title">上传图片</div>
        <div class="list-item">
          <label class="list-label">上传图片</label>
          <div class="list-text image-upload image-single">
            <div class="image-item">
              <input type="hidden" name="offlineImage" id="offlineImage" value="${offlineImage}">
              <c:if test="${not empty offlineImage}">
              <img src="${offlineImage}">
              </c:if>
              <c:if test="${empty offlineImage}">
              <img src="${stccdn}/image/defaultImage_240_150.png">
              </c:if>
              <input type="file">
            </div>
          </div>
          <div class="list-unit">
            <a href="javascript:;" class="image-view font-blue fs-14" data-src="http://image.mayishike.com/image/1e27766d-62b3-496a-b442-15f512caaff5" data-title="转账汇款截图示例图"><i class="fa fa-question-circle-o"></i> 示意图</a>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">
            <textarea placeholder="请填写汇款账号、汇款人、汇款金额" rows="3" class="form-input" name="offlineMemo">${offlineMemo}</textarea>
          </div>
        </div>
        <p class="bg-white font-red width-100p pl-15 pr-15 pt-10 pb-10">注意事项：</p>
        <p class="bg-white pl-15 pr-15 pb-10">办理银行转帐时，请您务必核对好以上的账户信息和转账金额。</p>
     </div>
     
     <div class="form-btn">
        <input type="submit" value="提交" class="btn orange btn-block round-2" id="btnSubmit">
     </div>
     </form>
  </article>

</body>
</html>
