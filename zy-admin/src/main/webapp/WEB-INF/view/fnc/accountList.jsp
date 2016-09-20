<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  $(function () {
    grid.init({
      src: $('#dataTable'),
      onSuccess: function (grid) {
        // execute some code after table records loaded
      },
      onError: function (grid) {
        // execute some code on network or other general error  
      },
      dataTable: {
        //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>", 
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/account', // ajax source
        },
        columns: [
          {
            data: '',
            title: '昵称',
            render: function (data, type, full) {
              if (full.user) {
                return '<img src="' + full.user.avatarThumbnail + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.user.nickname;
              } else {
                return '-';
              }
            }
          },
          {
            data: '',
            title: '手机号',
            render: function (data, type, full) {
              if (full.user) {
                return full.user.phone;
              } else {
                return '-';
              }
            }
          },
          {
            data: 'moneyLabel',
            title: '<i class="fa fa-money"></i> 余额'
          },
          {
            data: '',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '';
              if (full.user) {
                if (full.user.userType != '平台') {
                  <shiro:hasPermission name="account:deposit">
                  optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="deposit(' + full.userId + ')"><i class="fa fa-money"></i> 赠送余额</a>';
                  </shiro:hasPermission>
                }
              }
              return optionHtml;
            }
          }
        ]
      }
    });

  });
  var $addMoneyDialog;
  function deposit(userId) {
    $addMoneyDialog = $.window({
      content: "<form action='' class='form-horizontal' style='margin-top: 20px;'>"
      + "<div class='form-body'>"
      + "<div class='form-group'>"
      + "<label class='control-label col-md-3'>赠送余额:</label>"
      + "<div class='col-md-5'><input type='text' id='amount' class='form-control' value=''/></div>"
      + "</div>"
      + "<div class='form-group'>"
      + "<label class='control-label col-md-3'>备注信息:</label>"
      + "<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='remark'></textarea></div>"
      + "</div>"
      + "</div>"
      + "<div class='form-actions fluid'>"
      + "<div class='col-md-offset-3 col-md-9'>"
      + "<button type='button' class='btn green' onclick='submitBtn(" + userId + ")'>"
      + "保存</button>"
      + "<button type='button' class='btn default' onclick='closeBtn()' style='margin-left: 20px;'>"
      + "取消</button>"
      + "</div>"
      + "</div>"
      + "</form>",
      title: '赠送金额',
      width: 420,
      height: 360,
      button: false
    });
  }
  function submitBtn(userId) {
    var amount = $('#amount').val();
    var remark = $('#remark').val();

    var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");

    if (amount == null || amount == '') {
      alert('请输入赠送金额');
      return;
    }
    if (!reg.test(amount)) {
      alert('请输入正确的赠送金额');
      return;
    }
    if (remark == '' || remark == null) {
      alert('请输入备注信息');
      return;
    }
    $.post('${ctx}/account/deposit', {
      userId: userId,
      amount: amount,
      remark: remark
    }, function (result) {
      if (result.code == 0) {
        toastr.success(result.message, '提示信息');
      } else {
        toastr.error(result.message, '提示信息');
      }
      $addMoneyDialog.close();
      grid.getDataTable().ajax.reload(null, false);
    });

  }
  function closeBtn() {
    $addMoneyDialog.close();
  }
</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/account">资金账户管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-wallet"></i><span>资金账户管理 </span>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号"/>
              </div>
              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
              </div>
              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>
            </form>
          </div>
          <table class="table table-striped table-bordered table-hover" id="dataTable">
          </table>
        </div>
      </div>
      <!-- END ALERTS PORTLET-->
    </div>
  </div>
</div>
