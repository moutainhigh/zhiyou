<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="stc" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8">
<title>${sys} - 管理系统</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<meta http-equiv="Cache-Control" content="no-store" >
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<link rel="shortcut icon" href="favicon.ico">
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${stc}/assets/font/fonts.googleapis.com.css" rel="stylesheet"/>
<link href="${stc}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
<link href="${stc}/assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet"/>
<link href="${stc}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
<link href="${stc}/assets/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet"/>
<!-- END PAGE LEVEL PLUGIN STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="${stc}/assets/css/components.css" rel="stylesheet"/>
<link href="${stc}/assets/css/plugins.css" rel="stylesheet"/>
<link href="${stc}/assets/admin/layout/css/layout.css" rel="stylesheet"/>
<link href="${stc}/assets/admin/layout/css/themes/grey.css" rel="stylesheet" id="style_color"/>
<link href="${stc}/assets/admin/layout/css/custom.css" rel="stylesheet"/>

<link rel="stylesheet" href="${stc}/plugin/myui-1.0/jquery.myui.css" />

<!-- END THEME STYLES -->
<!-- BEGIN JAVASCRIPTS -->
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
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${stc}/assets/plugins/bootbox/bootbox.min.js"></script>
<script src="${stc}/assets/plugins/bootstrap-toastr/toastr.min.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${stc}/assets/scripts/metronic.js"></script>
<script src="${stc}/assets/scripts/ui-options.js"></script>
<script src="${stc}/assets/admin/layout/scripts/layout.js"></script>
<script src="${stc}/plugin/layer-2.2/layer.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
jQuery(document).ready(function() {    
   Metronic.init(); // init metronic core componets
   Layout.init(); // init layout
   
   $('.page-logo .start').click(); // load the content for the dashboard page.
});
</script>
<!-- END JAVASCRIPTS -->
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-sidebar-fixed page-quick-sidebar-over-content">

<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {
    $('#dataForm').validate({
      rules: {
        file: {
          required: true
        }
      },
      messages: {
   	  	name: {
          required : '请选择上传文件'
        }
      }
    });
  });
</script>
<!-- END JAVASCRIPTS -->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box green">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-plus-circle"></i><span> 上传平台物流数据</span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="dataForm" name="dataForm" class="form-horizontal" enctype="multipart/form-data" method="post" action="${ctx}/platformLogistics/handleImport">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
                <label class="col-md-3 control-label" for="file">文件</label>
                <div class="col-md-9">
                    <input id="file" type="file" name="file" class="file" />
                    <p class="help-block"> 平台物流数据表格的文件后缀为：“xls”、“xlsx” </p>
                </div>
            </div>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <shiro:hasPermission name="platformLogistics:import">
              <button type="submit" class="btn green">
                <i class="fa fa-plus"></i> 提交
              </button>
              </shiro:hasPermission>
              <button class="btn default" data-href="${ctx}/platformLogistics">
                <i class="fa fa-arrow-left"></i> 返回
              </button>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
</body>
<!-- END BODY -->
</html>