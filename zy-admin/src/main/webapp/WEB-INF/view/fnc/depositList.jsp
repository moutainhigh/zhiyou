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
          url : '${ctx}/deposit', // ajax source
        },
        columns : [ {
          data : '',
          title : '充值情况',
          orderable : false,
          width : '300px',
          render : function(data, type, full) {
            return 'sn: ' + full.sn + '<br /> 标题：' + full.title + '<br /> 支付类型：' + full.payType + '';
          }
        }, {
          data : 'payType',
          title : '支付类型',
          orderable : false,
          width : '80px'
        }, {
          data : 'depositStatus',
          title : '充值状态',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            var result = '';
            if (data == '待充值') {
              result = '<label class="label label-danger">待充值</label>';
            } else if (data == '充值成功') {
              result = '<label class="label label-success">充值成功</label>';
            } else if (data == '已取消') {
              result = '<label class="label label-default">已取消</label>';
            }
            return result;
          }
        }, {
          data : '',
          title : '用户信息',
          width : '180px',
          orderable : false,
          render : function(data, type, full) {
            if (full.user) {
              return '<img src="' + full.user.avatarThumbnail + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.user.nickname;
            } else {
              return '-';
            }
          }
        }, {
          data : 'currencyType1',
          title : '货币类型',
          width : '100px',
          orderable : false
        }, {
          data : 'amount1',
          title : '本金',
          orderable : true,
          width : '100px'
        }, {
          data : 'currencyType2',
          title : '货币类型',
          width : '100px',
          orderable : false
        }, {
          data : 'amount2',
          title : '试客币',
          orderable : true,
          width : '100px'
        }, {
          data : 'paidTime',
          title : '支付时间',
          orderable : true,
          width : '100px'
        }, {
          data : 'outerSn',
          title : '外部sn',
          orderable : true,
          width : '100px'
        } ]
      }
    });

  });

  <shiro:hasPermission name="deposit:export">
  function depositExport() {
    location.href = '${ctx}/deposit/export?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/deposit">充值管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-login"></i><span>充值管理 </span>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form id="searchForm" class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号" />
              </div>

              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称" />
              </div>

              <div class="form-group">
                <select name="depositStatusEQ" class="form-control">
                  <option value="">-- 充值状态 --</option>
                  <option value="0">待充值</option>
                  <option value="1">充值成功</option>
                  <option value="2">已取消</option>
                </select>
              </div>

              <div class="form-group">
                <select name="payTypeEQ" class="form-control">
                  <option value="">-- 支付类型 --</option>
                  <c:forEach items="${payTypes}" var="payType">
                    <option value="${payType}">${payType}</option>
                  </c:forEach>
                </select>
              </div>

              <div class="form-group">
                <label class="sr-only">支付时间起</label> <input class="Wdate form-control" type="text" id="paidTime"
                  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="paidTimeGTE" value="" placeholder="支付时间起" />
              </div>

              <div class="form-group">
                <label class="sr-only">支付时间止</label> <input class="Wdate form-control" type="text" id="paidTime"
                  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="paidTimeLT" value="" placeholder="支付时间止" />
              </div>

              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>

              <div class="form-group">
                <button class="btn yellow" onClick="depositExport()">
                  <i class="fa fa-file-excel-o"></i> 导出Excel
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
