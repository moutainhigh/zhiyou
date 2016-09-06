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
          url : '${ctx}/product', // ajax source
        },
        columns : [
            {
              data : '',
              title : '商品',
              orderable : false,
              render : function(data, type, full) {
                return '<img src="'+ full.image1 +'" style="width: 80px; height: 80px;">' + full.title + '';
              }
            },
            {
              data : 'productPriceType',
              title : '商品价格类型',
              orderable : false
            },
            {
              data : 'price',
              title : '价格',
              orderable : false
            },
            {
              data : 'marketPrice',
              title : '原价',
              orderable : false
            },
            {
              data : 'skuCode',
              title : '商品编码',
              orderable : false
            },
            {
              data : 'isOn',
              title : '是否上架',
              orderable : false,
              render: function (data, type, full) {
                if (data) {
                  return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 已上架 </span>';
                }
                return '';
              }
            },
            {
              data : 'id',
              title : '操作',
              orderable : false,
              render : function(data, type, full) {
                var optionHtml
                <shiro:hasPermission name="report:confirm">
                optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/product/update?id=' + data + '"><i class="fa  fa-edit"></i> 编辑 </a>';
                optionHtml += '<a class="btn btn-xs default blue-stripe" href="javascript:;" data-href="${ctx}/product/modifyPrice?id=' + data + '"><i class="fa  fa-edit"></i> 编辑价格 </a>';
                  if (full.isOn) {
                    optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/product/on?id=' + data + '&isOn=false"><i class="fa fa-times"></i> 下架 </a>';
                  } else {
                    optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/product/on?id=' + data + '&isOn=true"><i class="fa fa-check"></i> 上架 </a>';
                  }
                </shiro:hasPermission>
                return optionHtml;
              }
            }]
      }
    });

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/product">商品管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-present"></i> 商品管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/product/create">
            <i class="fa fa-plus"></i> 新增
          </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />

              <div class="form-group">
                <input type="text" name="titleLK" class="form-control" placeholder="商品名" />
              </div>

              <div class="form-group">
                <select name="isOnEQ" class="form-control">
                  <option value="">-- 是否上架 --</option>
                  <option value="1">是</option>
                  <option value="0">否</option>
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