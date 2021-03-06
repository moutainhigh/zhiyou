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
        lengthMenu : [ [ 10, 20, 50, 100 ], [ 10, 20, 50, 100 ] ],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/banner', // ajax source
        },
        columns: [
          {
            data: 'image',
            title: '主图',
            orderable: false,
            width: '160px',
            render: function (data, type, full) {
              return '<a target="_blank" href="' + data + '"><img style="width:160px;height:100px;"  src="' + full.imageThumbnail + '"/></a>';
            }
          },
          {
            data: 'title',
            title: '标题',
            width: '40px'
          },
          {
            data: 'url',
            title: '链接',
            width: '100px'
          },
          {
            data: 'bannerPosition',
            title: '位置',
            width: '60px'
          },
          {
            data: 'isReleased',
            title: '是否上架',
            width: '30px',
            render: function (data, type, full) {
              if (data) {
                return '<i class="fa fa-check font-green"></i>';
              }
              return '';
            }
          },
          {
            data: 'orderNumber',
            title: '顺序',
            width: '20px'
          },
          {
            data: 'id',
            title: '操作',
            width: '120px',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '';
              optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/banner/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
              if (full.isReleased) {
                optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/banner/release?id=' + data + '&isReleased=false"><i class="fa fa-times"></i> 下架 </a>';
              } else {
                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/banner/release?id=' + data + '&isReleased=true"><i class="fa fa-check"></i> 上架 </a>';
              }
              optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/banner/delete?id=' + data + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/banner">Banner管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-directions"></i> Banner管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/banner/create">
            <i class="fa fa-plus"></i> 新增
          </a>
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
                <input type="text" name="titleLK" class="form-control" placeholder="标题"/>
              </div>
              <div class="form-group">
                <select name="bannerPositionEQ" class="form-control">
                  <option value="">-- Banner 位置--</option>
                  <c:forEach items="${bannerPositions}" var="bannerPosition">
                    <option value="${bannerPosition.ordinal()}">${bannerPosition}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="form-group">
                <select name="isReleasedEQ" class="form-control">
                  <option value="">-- 是否上架 --</option>
                  <option value="true">是</option>
                  <option value="false">否</option>
                </select>
              </div>

              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>

             <%-- <div class="form-group">
                <div class="btn-group">
                  <button class="btn green" data-href="${ctx}/banner/create">
                    <i class="fa fa-plus"></i> 新增
                  </button>
                </div>
              </div>--%>

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