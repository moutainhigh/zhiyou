<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>


  $(function () {

    var grid = new Datatable();

    $('#statusTab li').bind('click', function () {
      $this = $(this);
      $('input[name="orderStatusEQ"]').val($this.data('order-status'));
      $('input[name="isPlatformDeliverEQ"]').val($this.data('is-platform-deliver'));
      grid.getDataTable().ajax.reload(null, false);
    });

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
          url: '${ctx}/order', // ajax source
        },
        columns: [
          {
            data: 'sn',
            title: '订单',
            orderable: false,
            render: function (data, type, full) {
              return 'sn: ' + full.sn + '<br /> 标题：' + full.title;
            }
          },
          {
            data: 'orderStatus',
            title: '订单状态',
            orderable: false,
            render: function (data, type, full) {
              return '<label class="label label-' + full.orderStatusStyle + '">' + data + '</label>';
            }
          },
          {
            data: '',
            title: '买家信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.user);
            }
          },
          {
            data: '',
            title: '卖家信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.seller);
            }
          },
          {
            data: 'price',
            title: '单价',
            orderable: false
          },
          {
            data: 'quantity',
            title: '数量',
            orderable: false
          },
          {
            data: 'totalMoney',
            title: '总金额',
            orderable: false,
            render: function (data, type, full) {
              return full.amount.toFixed(2);
            }
          },
          {
            data: 'createdTimeLabel',
            title: '下单时间',
            orderable: false
          },
          {
            data: 'buyerMemo',
            title: '买家留言',
            orderable: false
          },
          {
            data: 'paidTimeLabel',
            title: '支付时间',
            orderable: false
          },

          {
            data: 'remark',
            title: '备注',
            orderable: false
          },
          {
            data: 'refund',
            title: '退款金额',
            orderable: true
          },
          {
            data: 'refundedTimeLabel',
            title: '退款时间',
            orderable: false
          },
          {
            data: 'refundRemark',
            title: '退款备注',
            orderable: false
          }, {
            data: 'id',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '<a class="btn btn-xs default blue-stripe" href="javascript:;" data-href="${ctx}/order/detail?id=' + data + '"><i class="fa fa-search"></i> 查看 </a>';
              <shiro:hasPermission name="order:deliver">
              if (full.orderStatus == '已支付' && full.isPlatformDeliver) {
                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/order/deliver?id=' + data + '"><i class="fa fa-car"></i> 发货 </a>';
              }
              </shiro:hasPermission>
              return optionHtml;
            }
          }]
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
              <ul id="statusTab" class="nav nav-tabs">
                <li class="active"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 全部</a></li>
                <li class="" data-order-status="0"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 待付款</a></li>
                <li class="" data-order-status="1"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已支付</a></li>
                <li class="" data-order-status="2"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已发货</a></li>
                <li class="" data-order-status="3"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已完成</a></li>
                <li class="" data-order-status="4"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已退款</a></li>
                <li class="" data-order-status="5"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已取消</a></li>
              </ul>
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
              <input type="hidden" name="orderStatusEQ" value=""/>
              <input type="hidden" name="isPlatformDeliverEQ" value=""/>

              <div class="form-group">
                <input type="text" name="userPhoneEQ" class="form-control" placeholder="用户手机号"/>
              </div>

              <div class="form-group input-inline">
                <input type="text" name="userNicknameLK" class="form-control" placeholder="用户昵称"/>
              </div>

              <div class="form-group input-inline">
                <input type="text" name="snLK" class="form-control" placeholder="订单号"/>
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
