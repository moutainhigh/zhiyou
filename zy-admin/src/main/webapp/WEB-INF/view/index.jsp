<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/view/include/taglib.jsp" %>
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
  <meta http-equiv="Cache-Control" content="no-store">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
  <link rel="shortcut icon" href="favicon.ico">
  <!-- BEGIN GLOBAL MANDATORY STYLES -->
  <link href="${stc}/assets/font/fonts.googleapis.com.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
  <link href="${stc}/assets/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/gritter/css/jquery.gritter.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/bootstrap-datepicker/css/datepicker3.css" rel="stylesheet">
  <link href="${stc}/assets/plugins/bootstrap-datetimepicker/css/datetimepicker.css" rel="stylesheet">
  <link href="${stc}/assets/plugins/fullcalendar/fullcalendar/fullcalendar.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet"/>
  <!-- END PAGE LEVEL PLUGIN STYLES -->
  <!-- BEGIN PAGE STYLES -->
  <link href="${stc}/assets/admin/pages/css/tasks.css" rel="stylesheet"/>
  <link href="${stc}/assets/plugins/select2/select2.css" rel="stylesheet"/>
  <!-- END PAGE STYLES -->
  <!-- BEGIN THEME STYLES -->
  <link href="${stc}/assets/css/components.css" rel="stylesheet"/>
  <link href="${stc}/assets/css/plugins.css" rel="stylesheet"/>
  <link href="${stc}/assets/admin/layout/css/layout.css" rel="stylesheet"/>
  <link href="${stc}/assets/admin/layout/css/themes/grey.css" rel="stylesheet" id="style_color"/>
  <link href="${stc}/assets/admin/layout/css/custom.css" rel="stylesheet"/>

  <link href="${stc}/css/icon.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${stc}/plugin/editor/kindeditor-4.1.7/themes/default/default.css"/>
  <link rel="stylesheet" href="${stc}/plugin/myui-1.0/jquery.myui.css"/>

  <!-- END THEME STYLES -->
  <!-- BEGIN JAVASCRIPTS -->
  <!-- BEGIN CORE PLUGINS -->
  <!--[if lt IE 9]>
  <script src="${stc}/assets/plugins/respond.min.js"></script>
  <script src="${stc}/assets/plugins/excanvas.min.js"></script>
  <![endif]-->
  <script src="${stc}/plugin/echarts/echarts.min.js"></script>
  <script src="${stc}/assets/plugins/jquery-1.11.0.min.js"></script>
  <script src="${stc}/assets/plugins/jquery-migrate-1.2.1.min.js"></script>
  <script src="${stc}/plugin/simpleAjaxUploader-2.5.5/SimpleAjaxUploader.min.js"></script>
  <!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
  <script src="${stc}/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js"></script>
  <script src="${stc}/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
  <script src="${stc}/assets/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"></script>
  <script src="${stc}/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js"></script>
  <script src="${stc}/assets/plugins/jquery.blockui.min.js"></script>
  <script src="${stc}/assets/plugins/jquery.cokie.min.js"></script>
  <script src="${stc}/assets/plugins/uniform/jquery.uniform.min.js"></script>
  <script src="${stc}/assets/plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
  <!-- END CORE PLUGINS -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <script src="${stc}/assets/plugins/bootbox/bootbox.min.js"></script>
  <script src="${stc}/assets/plugins/bootstrap-toastr/toastr.min.js"></script>
  <script src="${stc}/assets/plugins/select2/select2.min.js"></script>
  <script src="${stc}/assets/plugins/datatables/media/js/jquery.dataTables.min.js"></script>
  <script src="${stc}/assets/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js"></script>
  <script src="${stc}/assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
  <script src="${stc}/assets/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
  <!-- END PAGE LEVEL PLUGINS -->
  <!-- BEGIN PAGE LEVEL SCRIPTS -->
  <script src="${stc}/assets/scripts/metronic.js"></script>
  <script src="${stc}/assets/scripts/ui-options.js"></script>
  <script src="${stc}/assets/scripts/datatable.js?v=20160408"></script>
  <script src="${stc}/assets/admin/layout/scripts/layout.js"></script>
  <script src="${stc}/plugin/myui-1.0/jquery.myui.js"></script>
  <script src="${stc}/plugin/layer-2.2/layer.js"></script>
  <script src="${ctx}/plugin/My97DatePicker/WdatePicker.js"></script>
  <script src="${ctx}/js/area.js"></script>
  <%@ include file="/WEB-INF/view/include/form.jsp" %>

  <!-- END PAGE LEVEL SCRIPTS -->
  <script>

    window.addEventListener('message', function (event) {
      var loc = event.data;
      if (loc && loc.module == 'locationPicker') {
        if (typeof onLocation === 'function') {
          onLocation.call(null, loc);
        }
      }
    }, false);

    jQuery(document).ready(function () {
      Metronic.init(); // init metronic core componets
      Layout.init(); // init layout

      $('.page-logo .start').click(); // load the content for the dashboard page.
    });
  </script>
  <!-- END JAVASCRIPTS -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-sidebar-fixed page-quick-sidebar-over-content">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
  <!-- BEGIN HEADER INNER -->
  <div class="page-header-inner">
    <!-- BEGIN LOGO -->
    <div class="page-logo">
      <a class="start" href="javascript:;" data-href="${ctx}/main">
        <img src="${stc}/image/logo_zy_small.png" width="120" height="40" class="logo-default"/>
      </a>
      <div class="menu-toggler sidebar-toggler">
        <!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
      </div>
    </div>
    <!-- END LOGO -->
    <!-- BEGIN RESPONSIVE MENU TOGGLER -->
    <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
    </a>
    <!-- END RESPONSIVE MENU TOGGLER -->

    <!-- BEGIN TOP NAVIGATION MENU -->
    <div class="top-menu">
      <ul class="nav navbar-nav pull-right">

        <!-- BEGIN USER LOGIN DROPDOWN -->
        <li class="dropdown dropdown-user"><a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
                                              data-close-others="true"> <img alt="" class="img-circle hide1" src="${user.avatarThumbnail}"/>
          <span class="username username-hide-on-mobile"> ${user.nickname} </span> <i class="fa fa-angle-down"></i>
        </a>
          <ul class="dropdown-menu">
            <li><a data-href="${ctx}/modifyPassword"> <i class="fa fa-key"></i> 修改密码</a></li>
            <li><a href="${ctx}/logout"> <i class="fa fa-arrow-circle-left"></i> 退出系统</a></li>
          </ul>
        </li>
        <!-- END USER LOGIN DROPDOWN -->
        <!-- BEGIN QUICK SIDEBAR TOGGLER -->
        <%--	<li class="dropdown dropdown-logout tooltips" data-original-title="退出系统" data-container="body" data-placement="bottom" data-html="true">
            <a href="${ctx}/logout" class="dropdown-toggle"> <i class="icon-logout"></i></a>
          </li>--%>
        <!-- END QUICK SIDEBAR TOGGLER -->
      </ul>
    </div>
    <!-- END TOP NAVIGATION MENU -->
  </div>
  <!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
  <!-- BEGIN SIDEBAR -->
  <div class="page-sidebar-wrapper">
    <div class="page-sidebar navbar-collapse collapse">
      <!-- BEGIN SIDEBAR MENU -->
      <ul class="page-sidebar-menu " data-auto-scroll="true" data-slide-speed="200">
        <li class="sidebar-search-wrapper">
          <!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
          <form class="sidebar-search sidebar-search-bordered" action="" method="POST">
            <a href="javascript:;" class="remove">
              <i class="icon-close"></i>
            </a>
            <div class="input-group">
              <input type="text" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
              <a href="javascript:;" class="btn submit"><i class="icon-magnifier"></i></a>
              </span>
            </div>
          </form>
          <!-- END RESPONSIVE QUICK SEARCH FORM -->
        </li>

        <li class="start active open">
          <a href="javascript:;">
            <i class="icon-users"></i>
            <span class="title">用户中心</span>
            <span class="selected"></span>
            <span class="arrow"></span>
          </a>
          <ul class="sub-menu">
            <shiro:hasPermission name="user:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/user"><i class="icon-user"></i> 用户信息<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="appearance:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/appearance"><i class="icon-user-female"></i> 实名认证管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="address:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/address"><i class="icon-home"></i> 收货地址管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="portrait:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/portrait"><i class="icon-graduation"></i> 用户画像管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="tag:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/tag"><i class="icon-tag"></i> 标签库管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>

          </ul>
        </li>

        <li>
          <a href="javascript:;">
            <i class="icon-basket"></i>
            <span class="title">下单管理</span>
            <span class="selected"></span>
            <span class="arrow "></span>
          </a>
          <ul class="sub-menu">
            <shiro:hasPermission name="product:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/product"><i class="icon-game-controller"></i> 商品管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <%--<shiro:hasPermission name="gift:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/gift"><i class="icon-game-controller"></i> 礼品管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>--%>
            <shiro:hasPermission name="agent:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/agent"><i class="icon-user"></i> 代理信息<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="order:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/order"><i class="icon-docs"></i> 订单管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
          </ul>
        </li>

        <li>
          <a href="javascript:;">
            <i class="icon-globe"></i>
            <span class="title">活动管理</span>
            <span class="selected"></span>
            <span class="arrow "></span>
          </a>
          <ul class="sub-menu">
            <shiro:hasPermission name="activity:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/activity"><i class="icon-social-dropbox"></i> 活动管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="report:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/report"><i class="icon-volume-1"></i> 检测报告<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
          </ul>
        </li>

        <li>
          <a href="javascript:;">
            <i class="icon-diamond"></i>
            <span class="title">财务管理</span>
            <span class="selected"></span>
            <span class="arrow "></span>
          </a>
          <ul class="sub-menu">
            <shiro:hasPermission name="account:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/account"><i class="icon-wallet"></i> 资金账户管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="deposit:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/deposit"><i class="icon-login"></i> 充值管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="withdraw:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/withdraw"><i class="icon-logout"></i> 提现管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="profit:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/profit"><i class="icon-present"></i> 收益管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="accountLog:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/accountLog"><i class="icon-calculator"></i> 流水管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="bankCard:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/bankCard"><i class="icon-credit-card"></i> 银行卡信息<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
          </ul>
        </li>

        <li>
          <a href="javascript:;">
            <i class="icon-screen-desktop"></i>
            <span class="title">内容管理</span>
            <span class="selected"></span>
            <span class="arrow "></span>
          </a>
          <ul class="sub-menu">
            <shiro:hasPermission name="banner:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/banner"><i class="icon-directions"></i> Banner管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="notice:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/notice"><i class="icon-volume-1"></i> 公告管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="article:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/article"><i class="icon-book-open"></i> 文章管理<span class="badge badge-danger"></span></a>
              </li>
              <li>
                <a href="javascript:;" data-href="${ctx}/articleCategory"><i class="icon-grid"></i> 文章类别管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="help:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/helpCategory"><i class="icon-speech"></i> 帮助管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
          </ul>
        </li>

        <li>
          <a href="javascript:;">
            <i class="icon-settings"></i>
            <span class="title">系统管理</span>
            <span class="selected"></span>
            <span class="arrow open"></span>
          </a>
          <ul class="sub-menu">
            <shiro:hasPermission name="admin:*">
              <li>
                <a href="javascript:;" data-href="${ctx}/admin"><i class="icon-user"></i> 管理员管理<span class="badge badge-roundless badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:*">
              <li>
                <a href="javascript:;" data-href="${ctx}/role"><i class="icon-key"></i> 角色管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="site:*">
              <li>
                <a href="javascript:;" data-href="${ctx}/site"><i class="icon-clock"></i> 站点管理</a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="message:view">
              <li>
                <a href="javascript:;" data-href="${ctx}/message"><i class="icon-bulb"></i> 消息管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="setting:*">
              <li>
                <a href="javascript:;" data-href="${ctx}/setting/edit"><i class="icon-speedometer"></i> 系统设置<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="area:*">
              <li>
                <a href="javascript:;" data-href="${ctx}/area"><i class="icon-bar-chart"></i> 区域管理<span class="badge badge-danger"></span></a>
              </li>
            </shiro:hasPermission>
            <li>
              <a href="javascript:;" data-href="${ctx}/dev"><i class="icon-briefcase"></i> 开发者工具<span class="badge badge-danger"></span></a>
            </li>
          </ul>
        </li>
      </ul>
      <!-- END SIDEBAR MENU -->
    </div>
  </div>
  <!-- END SIDEBAR -->
  <!-- BEGIN CONTENT -->
  <div class="page-content-wrapper">
    <div class="page-content">

      <div class="page-content-body">
        <!-- HERE WILL BE LOADED AN AJAX CONTENT -->
        正在加载中...
      </div>

    </div>
  </div>
  <!-- END CONTENT -->
</div>
<!-- END CONTAINER -->

<!-- BEGIN FOOTER -->
<div class="page-footer">
  <div class="page-footer-inner">
    2016 &copy; ${sys} 版权所有.
  </div>
  <div class="scroll-to-top">
    <i class="icon-arrow-up"></i>
  </div>
</div>
<!-- END FOOTER -->

</body>
<!-- END BODY -->
</html>
