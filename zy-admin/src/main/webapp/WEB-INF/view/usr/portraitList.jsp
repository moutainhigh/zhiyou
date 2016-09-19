<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<style>
  .mr-5 { 
    margin-right: 5px !important; 
  }
  .mb-5 { 
    margin-bottom: 5px !important; 
  }
  .inline-block {
    display: inline-block;
  }
</style>
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
          url : '${ctx}/portrait', // ajax source
        },
        columns : [ {
          data : '',
          title : '用户信息',
          orderable : false,
          render : function(data, type, full) {
            return '<p><img src="' + full.user.avatarThumbnail + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.user.nickname + '</p><p>手机: ' + full.user.phone + '</p>';
          }
        }, {
          data : 'gender',
          title : '性别'
        }, {
          data : 'birthdayLabel',
          title : '出生年月',
          width : '120px',
          orderable : false,
        }, {
          data : 'tagNames',
          title : '标签',
          orderable : false,
          render : function(data, type, full) {
            var html = '';
            for (var i = 0; i < data.length; i++) {
              if (i % 5 == 0) {
                html += '<em class="label mb-5 mr-5 inline-block label-success">' + data[i] + '</em>';
              } else if (i % 5 == 1) {
                html += '<em class="label mb-5 mr-5 inline-block label-danger">' + data[i] + '</em>';
              } else if (i % 5 == 2) {
                html += '<em class="label mb-5 mr-5 inline-block label-info">' + data[i] + '</em>';
              } else if (i % 5 == 3) {
                html += '<em class="label mb-5 mr-5 inline-block label-warning">' + data[i] + '</em>';
              } else if (i % 5 == 4) {
                html += '<em class="label mb-5 mr-5 inline-block label-primary">' + data[i] + '</em>';
              }
            }
            return html;
          }
        }, {
          data : '',
          title : '所在地',
          orderable : false,
          render : function(data, type, full) {
            return full.province + '-' + full.city + '-' + full.district;
          }
        }, {
          data : 'jobName',
          title : '职业',
          width : '100px',
          orderable : false
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
    <li><a href="javascript:;" data-href="${ctx}/portrait">完善资料管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-graduation"></i><span>完善资料管理 </span>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />

              <div class="form-group">
                <input type="text" name="userPhoneEQ" class="form-control" placeholder="手机号" />
              </div>

              <div class="form-group">
                <input type="text" name="userNicknameLK" class="form-control" placeholder="昵称" />
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
