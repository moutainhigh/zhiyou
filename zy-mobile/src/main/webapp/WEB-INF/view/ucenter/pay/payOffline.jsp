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

    $('.image-multi .image-add').imageupload({
      isMultipart : true
    });

    $('.valid-form').validate({
      rules : {
        'offlineImage' : {
          required : true
        },
        'offlineMemo' : {
          required : true
        }
      },
      submitHandler : function(form) {
        if($('input[name="offlineImage"]').length < 1) {
          messageFlash('请至少上传一张图片');
          return;
        }
        form.submit();
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
    <p>
      <i class="fa fa-exclamation-circle"></i> 请将本次交易金额<span class="font-orange">${amount}元</span>汇入以下银行账户，并将<span class="font-orange">汇款凭证</span>拍照或截图上传。
    </p>
  </div>

  <article class="mt-15 mb-15 clearfix">
    <div class="list-title">单据信息</div>
    <div class="list-group">
     <div class="list-item">
        <div class="list-text">标题</div>
        <div class="list-unit">${title}</div>
     </div>
     <div class="list-item">
        <div class="list-text">单据号</div>
        <div class="list-unit">${sn}</div>
     </div>
     <div class="list-item">
        <div class="list-text">转账金额</div>
        <div class="list-unit">${amount}元</div>
      </div>
    </div> 
    <%-- <div class="list-title">收款银行账户</div>
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
    </div> --%>
    
    <form action="${ctx}/u/pay/${payType == '银行汇款' ? 'order' : 'payment'}" class="valid-form" id="form" method="post">
      <input type="hidden" name="refId" value="${refId}">
      <div class="list-group">
        <div class="list-title">上传汇款凭证</div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <div class="image-add" data-limit="6" data-name="offlineImage">
              <input type="hidden" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">
            <textarea placeholder="请填写汇款账号、汇款人、汇款金额" rows="3" class="form-input" name="offlineMemo">${offlineMemo}</textarea>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">
            <p class="font-red fs-14">注意事项：</p>
            <p class="mt-5 fs-14">办理银行转帐时，请您务必核对好以上的收款银行账户和转账金额。</p>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input type="submit" value="提交" class="btn orange btn-block round-2" id="btnSubmit">
      </div>
    </form>
  </article>

</body>
</html>
