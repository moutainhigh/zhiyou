<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="stc" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>${sys} - 用户登录</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<link rel="shortcut icon" href="favicon.ico"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${stc}/assets/font/fonts.googleapis.com.css" rel="stylesheet"/>
<link href="${stc}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
<link href="${stc}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
<link href="${stc}/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="${stc}/assets/plugins/select2/select2.css" rel="stylesheet"/>
<link href="${stc}/assets/admin/pages/css/login-soft.css" rel="stylesheet"/>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME STYLES -->
<link href="${stc}/assets/css/components.css" rel="stylesheet"/>
<link href="${stc}/assets/css/plugins.css" rel="stylesheet"/>
<!-- END THEME STYLES -->
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body class="login">
<!-- BEGIN LOGO -->
<div class="logo">
  <a href="${ctx}/">
    <img src="${stc}/image/logo_zy.png" width="180" height="60"/>
  </a>
</div>
<!-- END LOGO -->
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGIN -->
<div class="content">
  <!-- BEGIN LOGIN FORM -->
  <form id="loginForm" class="login-form" action="${ctx}/login" method="post">
    <h3 class="form-title">请您登录</h3>
    <div class="alert alert-danger display-hide">
      <button class="close" data-close="alert"></button>
      <i class="fa fa-exclamation-circle"></i><span class="form-errors">请输入用户名和密码.</span>
    </div>
    <div class="form-group">
      <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
      <label class="control-label visible-ie8 visible-ie9">用户名</label>
      <div class="input-icon">
        <i class="fa fa-user"></i>
        <input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="用户名" name="username" value="${username}"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label visible-ie8 visible-ie9">密　码</label>
      <div class="input-icon">
        <i class="fa fa-lock"></i>
        <input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="密码" name="password"/>
      </div>
    </div>
    <c:if test="${hasCaptcha}">
    <div class="form-group input-group form-captcha">
      <label class="control-label visible-ie8 visible-ie9">验证码</label>
      <div class="input-icon">
        <i class="fa fa-font"></i>
        <input class="form-control input-inline captcha placeholder-no-fix" type="text" autocomplete="off" placeholder="验证码" id="captcha" name="captcha" maxlength="5" />
        <span class="input-group-addon">
          <img src="${ctx}/captcha" class="captcha-image" />
          <a href="javascript:void(0);" class="captcha-refresh" title="看不清,换一张">看不清</a>
        </span>
      </div>
    </div>
    </c:if>
    <div class="form-actions">
      <label class="checkbox">
      <input type="checkbox" name="remember" value="1"/> 记住我 </label>
      <button id="loginButton" type="submit" class="btn red pull-right">
      登录 <i class="m-icon-swapright m-icon-white"></i>
      </button>
    </div>
    <%--
    <div class="login-options">
      <h4>Or login with</h4>
      <ul class="social-icons">
        <li>
          <a class="facebook" data-original-title="facebook" href="#"></a>
        </li>
        <li>
          <a class="twitter" data-original-title="Twitter" href="#"></a>
        </li>
        <li>
          <a class="googleplus" data-original-title="Goole Plus" href="#"></a>
        </li>
        <li>
          <a class="linkedin" data-original-title="Linkedin" href="#"></a>
        </li>
      </ul>
    </div>
    <div class="forget-password">
      <h4>忘记密码 ?</h4>
      <p>
         <a href="javascript:;" id="forget-password">点击这里</a>重置密码
      </p>
    </div>
    <div class="create-account">
      <p>
         Don't have an account yet ?&nbsp; <a href="javascript:;" id="register-btn">
        Create an account </a>
      </p>
    </div>
     --%>
  </form>
  <!-- END LOGIN FORM -->
  
</div>
<!-- END LOGIN -->
<!-- BEGIN COPYRIGHT -->
<div class="copyright">
   ${sys} &copy; 2016 版权所有
</div>
<!-- END COPYRIGHT -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="${stc}/assets/plugins/respond.min.js"></script>
<script src="${stc}/assets/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="${stc}/assets/plugins/jquery-1.11.0.min.js"></script>
<script src="${stc}/assets/plugins/jquery-migrate-1.2.1.min.js"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="${stc}/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js"></script>
<script src="${stc}/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="${stc}/assets/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"></script>
<script src="${stc}/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${stc}/assets/plugins/jquery.blockui.min.js"></script>
<script src="${stc}/assets/plugins/jquery.cokie.min.js"></script>
<script src="${stc}/assets/plugins/uniform/jquery.uniform.min.js"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${stc}/assets/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${stc}/assets/plugins/backstretch/jquery.backstretch.min.js"></script>
<script src="${stc}/assets/plugins/select2/select2.min.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${stc}/assets/scripts/metronic.js"></script>
<script src="${stc}/assets/admin/pages/scripts/login.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
jQuery(document).ready(function() {     
  Metronic.init(); // init metronic core components
  
  if($('#menus').length > 0) {
    location.href = '${ctx}/login';
  }
  
  // 刷新验证码图片
  var refreshCaptcha = function() {
    $(".captcha-image").attr("src", '${ctx}/captcha?' + new Date().getTime());
  };
  $(".captcha-image").click(refreshCaptcha);
  $(".captcha-refresh").click(refreshCaptcha);
  
  <c:if test="${not empty failureInfo}">
  $('.alert-danger', $('.login-form')).show().find('.form-errors').text('${failureInfo}');
  </c:if>
  
  $.backstretch([
                 "${stc}/assets/admin/pages/media/bg/1.jpg",
                 "${stc}/assets/admin/pages/media/bg/2.jpg",
                 "${stc}/assets/admin/pages/media/bg/3.jpg",
                 "${stc}/assets/admin/pages/media/bg/4.jpg"
                 ], {
	    fade: 1000,
	    duration: 8000
    }
  );
  
  $('.login-form').validate({
    errorElement: 'span', //default input error message container
    errorClass: 'help-block', // default input error message class
    focusInvalid: false, // do not focus the last invalid input
    rules: {
        username: {
            required: true
        },
        password: {
            required: true
        },
        captcha : {
          required : true
        },
        remember: {
            required: false
        }
    },
    messages: {
        username: {
            required: "请输入用户名."
        },
        password: {
            required: "请输入密码."
        },
        captcha : {
          required : "请输入验证码."
        }
    },
    invalidHandler: function (event, validator) { //display error alert on form submit   
        $('.alert-danger', $('.login-form')).show();
    },
    highlight: function (element) { // hightlight error inputs
        $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
    },
    success: function (label) {
        label.closest('.form-group').removeClass('has-error');
        label.remove();
    },
    errorPlacement: function (error, element) {
        error.insertAfter(element.closest('.input-icon'));
    },
    submitHandler: function (form) {
        form.submit(); // form validation success, call ajax form submit
    }
  });
  $('.login-form input').keypress(function (e) {
      if (e.which == 13) {
          if ($('.login-form').validate().form()) {
              $('.login-form').submit(); //form validation success, call ajax form submit
          }
          return false;
      }
  });
});
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>