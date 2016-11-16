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

<title>添加保单信息</title>
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
      },
      submitHandler : function(form) {
        var reportId = $('#reportId').val();
        if(!reportId) {
          messageAlert('请选择检测报告');
          return;
        }
        $(form).find(':submit').prop('disabled', true);
        form.submit();
      }
    });
    
    //选择检测报告
  	$('.report-info').click(function(){
  	  showReportList();
  	});
    
    $('body').on('click', '.report', function() {
      var $this = $(this);
      var report = {};
      report.id = $this.attr('data-id');
      report.realname = $this.attr('data-realname');
      report.gender = $this.attr('data-gender');
      report.phone = $this.attr('data-phone');
      setReport(report);
    });
  });
  
  function showReportList() {
    var html = document.getElementById('reportListTpl').innerHTML;
    pushDialog(html);
  }
	
  function hideReportList() {
    pullDialog('#reportList', function(){
      $('#reportList').remove();
    });
  }
  
  function setReport(report) {
    hideReportList();
    $('#reportId').val(report.id);
    $('#realname').text(report.realname);
    $('#gender').text(report.gender);
    $('#phone').text(report.phone);
    $('.report-info .list-unit').html('<span>报告编号：' + report.id + '</span> (<span>' + report.realname + '</span>)');
  }
  
</script>
<script id="reportListTpl" type="text/html">
  <aside id="reportList" class="abs-lt size-100p bg-gray zindex-1000">
    <header class="header">
      <h1>选择检测报告</h1>
      <a href="javascript:hideReportList();" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
    <div class="list-group">
      <c:forEach items="${reports}" var="report">
      <div class="list-item report" data-id="${report.id}" data-realname="${report.realname}" data-gender="${report.gender}" data-phone="${report.phone}">
        <div class="list-text">${report.realname} (编号: ${report.id})</div>
        <div class="list-unit fs-14">检测时间: ${report.reportedDateLabel}</div>
      </div>
      </c:forEach>
    </div>
  </aside>
</script>
</head>
<body>
  <header class="header">
    <h1>添加保单信息</h1>
    <a href="${ctx}/u/policy" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/policy/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-title">选择检测报告</div>
        <div class="list-item report-info">
          <label class="list-text lh-36">请选择</label>
          <div class="list-unit"></div>
          <i class="list-arrow"></i>
          <input id="reportId" type="hidden" name="reportId" value="">
        </div>
      </div>
      <div class="list-group">
        <div class="list-title">客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div id="realname" class="list-text"></div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div id="gender" class="list-text"></div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div id="phone" class="list-text"></div>
        </div>
        <div class="list-item">
          <label class="list-label" for="idCardNumber">身份证号</label>
          <div class="list-text">
            <input type="text" name="idCardNumber" class="form-input" value="" placeholder="填写身份证号">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">生日</label>
          <div class="list-text">
            <input type="date" name="birthday" class="form-input" value="" placeholder="填写生日  1900-01-01">
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
