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
<meta name="keywords" content="微信分销" />
<meta name="description" content="修改银行卡" />

<title>修改银行卡</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script src="${stccdn}/js/area.js"></script>
<script>
	$(function() {

		$('.valid-form').validate({
			rules : {
				'accountName' : {
					required : true
				},
				'accountNo' : {
					required : true
				}
			},
			messages : {
				'accountName' : {
					required : '请输入开户姓名'
				},
				'accountNo' : {
					required : '请输入银行卡号'
				}
			}
		});
		
	});
</script>
</head>
<body>
  <header class="header">
    <h1>修改银行卡</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <article class="bank-create">
    <form id="bankForm" class="valid-form" action="${ctx}/u/bankCard/edit" method="post">
      <div class="form-message note note-warning mb-0 hide">
        <p>输入信息有误，请先更正。</p>
      </div>
      <div class="form-group">
        <div class="form-item">
          <label class="control-label">开户银行</label>
          <div class="form-select">
            <select name="bankName" id="bankName">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="form-item">
          <label class="control-label" for="accountName">开户姓名</label>
          <input type="text" id="accountName" name="accountName" class="control-input" value="${bank.accountName}" placeholder="填写开户姓名">
        </div>
        <div class="form-item">
          <label class="control-label" for="accountNo">银行卡号</label>
          <input type="text" id="accountNo" name="accountNo" class="control-input" value="${bank.accountNo}" placeholder="填写银行卡号">
        </div>
        <div class="form-item">
          <label class="control-label" for="bankBranchName">开户支行名</label>
          <input type="text" id="bankBranchName" name="bankBranchName" class="control-input" value="${bank.bankBranchName}" placeholder="填写开户支行名称">
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="保 存">
      </div>
    </form>
  </article>
</body>
</html>
