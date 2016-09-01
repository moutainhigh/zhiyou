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
          lengthMenu: [
                         [10, 20, 50, 100, -1],
                         [10, 20, 50, 100, 'All'] // change per page values here
                         ],
          pageLength: 20, // default record count per page
          order: [], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/product', // ajax source
          },
          columns : [
              {
  				data : '',
  				title : '商品',
  				orderable : false,
  				width : '120px',
                render : function(data, type, full) {
                    return '<img src="'+ full.image1 +'" style="width: 80px; height: 80px;">' + full.title + '';
                  }
  			  },
              {
                  data : 'price',
                  title: '价格',
                  width: '120px'
              },
              {
                  data : 'marketPrice',
                  title: '原价',
                  width: '120px'
              },
              {
                  data : 'skuCode',
                  title: '原价',
                  width: '120px'
              },
              {
                  data : 'stockQuantity',
                  title: '库存数量',
                  width: '120px'
              },
              {
                  data : 'lockedCount',
                  title: '锁定数量',
                  width: '120px'
              },
              {
                  data : 'isOn',
                  title: '是否上架',
                  width: '120px',
                  render : function(data, type, full) {
                      return data?'已上架':'';
                    }
              },
              {
                  data : '',
                  title: '操作',
                  width: '240px',
                  orderable : false,
                  render : function(data, type, full) {
                  	var optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/product/update/' + full.id + '"><i class="fa  fa-mail-forward"></i> 编辑 </a>';
                  	if(full.isOn){
	                	optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="down(' + full.id + ')"><i class="fa fa-fighter-jet"></i> 下架</a>';
                	}else {
                		optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="on(' + full.id + ')"><i class="fa icon-plane"></i> 上架</a>';
                	}
                  	return optionHtml;
                  }
                } ]
        }
      });

});

function on(id) {
	operation(id, true);
}
function down(id) {
	operation(id, false);
}
function operation(id, isOn) {
	$.post('${ctx}/product/on',{id : id, isOn : isOn}, function(result) {
		toastr.success("操作成功", '提示信息');
		grid.getDataTable().ajax.reload(null, false);
	})
}

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
          <i class="fa fa-dropbox"></i> 商品管理
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <div class="btn-group">
               <button id="" class="btn green" data-href="${ctx}/product/create">
                新增 <i class="fa fa-plus"></i>
              </button>
            </div>
            <!-- <div class="btn-group pull-right">
              <button class="btn dropdown-toggle" data-toggle="dropdown">
                工具 <i class="fa fa-angle-down"></i>
              </button>
              <ul class="dropdown-menu pull-right">
                <li><a href="#"> 打印 </a></li>
                <li><a href="#"> 导出Excel </a></li>
              </ul>
            </div> -->
          </div>

          <div class="row">
            <div class="col-md-3 table-actions">
              <span class="table-row-checked"></span>
            </div>
            <div class="col-md-9">
              <form class="filter-form pull-right">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
                
                <div class="form-group input-inline">
                  <label class="sr-only"> 商品名： </label>
                  <input type="text" name="titleLK" maxlength="50" class="form-control" placeholder="商品名" />
                </div>
                
                <div class="form-group input-inline">
                  <label class="sr-only"> 是否上架： </label>
                  <select name="isOnEQ" class="form-control">
                  	<option value="">--请选择是否上架--</option>
                  	<option value="1">是</option>
                  	<option value="0">否</option>
                  </select>
                </div>
                
                <div class="form-group input-inline">
                <button class="btn purple filter-submit">
                  <i class="fa fa-check"></i> 查询
                </button>
                </div>
              </form>
            </div>
          </div>
          <table class="table table-striped table-bordered table-hover" id="dataTable">
          </table>
        </div>
      </div>
      <!-- END ALERTS PORTLET-->
    </div>
  </div>
</div>