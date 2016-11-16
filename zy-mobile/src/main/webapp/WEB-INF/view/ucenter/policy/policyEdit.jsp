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

<title>修改保单信息</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script type="text/javascript">
  $(function() {

    //验证
    $('.valid-form').validate({
      rules : {
        'idCardNumber' : {
          required : true
        },
        'birthday' : {
          required : true
        }
      }
    });
  });
  
</script>
</head>
<body>
  <header class="header">
    <h1>修改保单信息</h1>
    <a href="${ctx}/u/policy" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/policy/edit" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-item report-info">
          <label class="list-text lh-36">检测报告ID</label>
          <div class="list-unit">${policy.reportId}</div>
        </div>
        <div class="list-title">客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div id="realname" class="list-text">${policy.realname}</div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div id="gender" class="list-text">${policy.gender}</div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div id="phone" class="list-text">${policy.phone}</div>
        </div>
        <div class="list-item">
          <label class="list-label" for="idCardNumber">身份证号</label>
          <div class="list-text">
            <input type="text" name="idCardNumber" class="form-input" value="${policy.idCardNumber}" placeholder="填写身份证号">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">生日</label>
          <div class="list-text">
            <input type="date" name="birthday" class="form-input" value="${policy.birthday}" placeholder="填写生日  1900-01-01">
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>

    </form>
  </article>

</body>
</html>
