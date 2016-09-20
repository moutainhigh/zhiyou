<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();
  var urlPc = '${rulPc}';
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
        order: [
        ], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/article', // ajax source
        },
        columns: [
          {
            data: 'imageBig',
            title: '主图',
            orderable: false,
            render: function (data, type, full) {
              return '<a target="_blank" href="' + data + '"><img style="width:180px;height:80px;"  src="' + full.imageThumbnail + '"/></a>';
            }
          },
          {
            data: 'title',
            title: '标题',
            orderable: false
          },
          {
            data: 'visitCount',
            title: '访问数',
            orderable: false
          },
          {
            data: 'author',
            title: '作者',
            orderable: false
          },
          {
            data: 'isReleased',
            title: '是否发布',
            orderable: false,
            render: function (data, type, full) {
              if (data) {
                return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 已发布 </span>';
              }
              return '';
            }
          },
          {
            data: 'id',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '';
              <shiro:hasPermission name="article:edit">
              optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/article/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
              </shiro:hasPermission>
              <shiro:hasPermission name="article:edit">
              if (full.isReleased) {
                optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" onclick="unrelease(' + full.id + ')"><i class="fa fa-times X"></i> 取消发布 </a>';
              } else {
                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="release(' + full.id + ')"><i class="fa fa-check"></i> 发布 </a>';
              }
              </shiro:hasPermission>
              return optionHtml;
            }
          }]
      }
    });

  });
  <shiro:hasPermission name="article:edit">
  function release(id) {
    releaseAjax(id, true);
  }
  function unrelease(id) {
    releaseAjax(id, false);
  }
  function releaseAjax(id, isRelease) {
    $.post('${ctx}/article/release', {id: id, isRelease: isRelease}, function (result) {
      //grid.getDataTable().ajax.reload();
      grid.getDataTable().ajax.reload(null, false);
    });
  }
  </shiro:hasPermission>
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/article">新闻管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-book-open"></i> 新闻管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/article/create">
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
                  <select name="isReleasedEQ" class="form-control">
                    <option value="">--请选择是否发布--</option>
                    <option value="true">是</option>
                    <option value="false">否</option>
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