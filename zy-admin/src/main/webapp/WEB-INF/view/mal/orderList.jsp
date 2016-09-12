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
          url : '${ctx}/order', // ajax source
        },
        columns : [ {
          data : 'sn',
          title : '订单',
          orderable : false,
          render : function(data, type, full) {
            return 'sn: ' + full.sn + '<br /> 标题：' + full.title;
          }
        }, {
          data : 'orderStatus',
          title : '订单状态',
          orderable : false,
          render : function(data, type, full) {
            var result = '';
            if (data == '待支付') {
              result = '<label class="label label-danger">待支付</label>';
            } else if (data == '已支付') {
              result = '<label class="label label-success">已支付</label>';
            } else if (data == '订单取消') {
              result = '<label class="label label-default">订单取消</label>';
            } else if (data == '已完成') {
              result = '<label class="label label-default">已完成</label>';
            }
            return result;
          }
        }, {
          data : '',
          title : '用户信息',
          orderable : false,
          render : function(data, type, full) {
            return '<p>昵称: ' + full.user.nickname + '</p><p>手机号: ' + full.user.phone + '</p><p>等级: ' + full.user.userRankLabel + '</p>';
          }
        }, {
          data : '',
          title : '卖家信息',
          orderable : false,
          render : function(data, type, full) {
            return '<p>昵称: ' + full.seller.nickname + '</p><p>手机号: ' + full.seller.phone + '</p><p>等级: ' + full.user.userRankLabel + '</p>';
          }
        }, {
          data : '',
          title : '图片',
          orderable : false,
          render : function(data, type, full) {
            return '<img style="width:180px;height:80px;"  src="' + full.imageThumbnail + '"/>';
          }
        }, {
          data : 'quantity',
          title : '数量',
          orderable : false
        }, {
          data : 'totalMoney',
          title : '交易金额',
          orderable : false,
          render : function(data, type, full) {
            return '<p>单价' + full.price + '</p><p>优惠金额: ' + full.discountFee.toFixed(2) + '</p><p>总金额：' + full.amount.toFixed(2) + '</p>';
          }
        }, {
          data : 'createdTimeLabel',
          title : '下单时间',
          orderable : false
        }, {
          data : 'paidTimeLabel',
          title : '支付时间',
          orderable : false
        }, {
          data : 'remark',
          title : '备注',
          orderable : false
        }, {
          data : 'refund',
          title : '退款金额',
          orderable : true
        }, {
          data : 'refundedTimeLabel',
          title : '退款时间',
          orderable : false
        }, {
          data : 'refundRemark',
          title : '退款备注',
          orderable : false
        }, {
          data : 'id',
          title : '操作',
          orderable : false,
          render : function(data, type, full) {
            return '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/order/detail?id=' + data + '"><i class="fa fa-search"></i> 查看 </a>';
          }
        } ]
      }
    });

  });
</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/order">订单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-docs"></i><span>订单管理 </span>
        </div>
        <div class="tools"></div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号" />
              </div>

              <div class="form-group input-inline">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称" />
              </div>

              <div class="form-group input-inline">
                <input type="text" name=refIdEQ class="form-control" placeholder="订单号" />
              </div>
              <div class="form-group input-inline">
                <select name="orderStatusEQ" class="form-control">
                  <option value="">-- 订单状态 --</option>
                  <option label="0">待支付</option>
                  <option label="1">已支付</option>
                  <option label="2">已发货</option>
                  <option label="3">已完成</option>
                  <option label="4">已退款</option>
                  <option label="5">已取消</option>
                </select>
              </div>
              <div class="form-group input-inline">
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
