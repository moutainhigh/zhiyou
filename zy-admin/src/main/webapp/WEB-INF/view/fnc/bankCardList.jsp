<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  $(function() {
    grid.init({
      src : $('#dataTable'),
      onSuccess : function(grid) {
        // execute some code after table records loaded
      },
      onError : function(grid) {
        // execute some code on network or other general error  
      },
      dataTable : {
        //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>", 
        lengthMenu : [ [ 10, 20, 50, 100, -1 ], [ 10, 20, 50, 100, 'All' ] // change per page values here
        ],
        pageLength : 20, // default record count per page
        order : [], // set first column as a default sort by desc
        ajax : {
          url : '${ctx}/bankCard', // ajax source
        },
        columns : [ {
          data : 'nickname',
          title : '用户信息',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            if (full.user) {
              return '<p><img src="' + full.user.avatarThumbnail + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.user.nickname + '</p><p>手机: ' + full.user.phone + '</p>';
            } else {
              return '-';
            }
          }
        }, {
          data : 'bankName',
          title : '银行名称',
          orderable : false,
          width : '200px',
          render : function(data, type, full) {
            return '银行: ' + data;
          }
        }, {
          data : 'bankBranchName',
          title : '分行名称',
          orderable : false,
          width : '100px'
        }, {
          data : 'cardNumber',
          title : '卡号',
          orderable : false,
          width : '100px'
        }, {
          data : 'realname',
          title : '真实姓名',
          width : '100px'
        }, {
          data : 'appliedTime',
          title : '申请时间',
          width : '100px'
        }, {
          data : 'confirmRemark',
          title : '审核备注',
          width : '100px'
        }, {
          data : 'confirmStatus',
          title : '状态',
          orderable : false,
          width : '80px',
          render : function(data, type, full) {
            var result = '';
            if (data == '待审核') {
              result = '<label class="label label-danger">待审核</label>';
            } else if (data == '已通过') {
              result = '<label class="label label-success">已通过</label>';
            } else if (data == '未通过') {
              result = '<label class="label label-default">未通过</label>';
            }
            return result;
          }

        }, {
          data : 'id',
          title : '操作',
          width : '100px',
          orderable : false,
          render : function(data, type, full) {
            var optionHtml = '';
            <shiro:hasPermission name="user:edit">
            if (full.confirmStatus == '待审核') {
              optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="confirm(' + full.id + ')"><i class="fa fa-edit"></i> 审核 </a>';
            }

            </shiro:hasPermission>
            return optionHtml;
          }
        } ]
      }
    });

  });
  var $confirmDialog;
  function confirm(id) {
    $confirmDialog = $.window({
      content : "<form action='' class='form-horizontal' style='margin-top: 20px;'>" + "<div class='form-body'>" + "<div class='form-group'>"
          + "<label class='control-label col-md-3'>审核状态:</label>" + "<div class='col-md-5'><select name='confirmSelect' class='form-control' style='width: 220px'>"
          + "<option value=''>请选择审核状态</option>" + "<option value='true'>审核通过</option>" + "<option value='false'>审核拒绝</option>" + "</select></div>" + "</div>"
          + "<div class='form-group'	>" + "<label class='control-label col-md-3'>审核信息:</label>"
          + "<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='confirmRemark'></textarea></div>" + "</div>" + "</div>"
          + "<div class='form-actions fluid'>" + "<div class='col-md-offset-3 col-md-9'>" + "<button type='button' class='btn green' onclick='submitBtn(" + id + ")'>"
          + "保存</button>" + "<button type='button' class='btn default' onclick='closeBtn()' style='margin-left: 20px;'>" + "取消</button>" + "</div>" + "</div>" + "</form>",
      title : '审核任务状态',
      width : 420,
      height : 320,
      button : false
    });
  }
  function submitBtn(id) {
    var isSuccess = $('select[name="confirmSelect"]').val();
    var confirmRemark = $('#confirmRemark').val();
    if (isSuccess == '') {
      layer.alert('请选择审核状态');
      return;
    }
    if(isSuccess == 'false' && confirmRemark == '') {
      layer.alert('请输入审核信息');
      return;
    }
    var confirmRemark = $('#confirmRemark').val();
    $.post('${ctx}/bankCard/confirm', {
      id : id,
      isSuccess : isSuccess,
      confirmRemark : confirmRemark
    }, function(result) {
      if(result.code == 0) {
        toastr.success(result.message, '提示信息');
      } else {
        toastr.error(result.message, '提示信息');
      }
      
      $confirmDialog.close();
      grid.getDataTable().ajax.reload(null, false);
    })

  }
  function closeBtn() {
    $confirmDialog.close();
  }
</script>

<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/user">银行卡管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-credit-card"></i><span>银行卡管理 </span>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />
              
              <div class="form-group input-inline">
                <input type="text" name="realnameLK" class="form-control" placeholder="真实姓名" />
              </div>
              
              <div class="form-group">
                <select name=confirmStatusEQ class="form-control">
                  <option value="">-- 审核状态 --</option>
                  <option value="0">待审核</option>
                  <option value="1">已通过</option>
                  <option value="2">未通过</option>
                </select>
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
