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
          url : '${ctx}/portrait', // ajax source
        },
        columns : [ {
          data : '',
          title : '用户信息',
          width : '140px',
          render : function(data, type, full) {
            return '昵称: ' + full.userNickname + '<br /> 手机号：' + full.userPhone;
          }
        }, {
          data : 'gender',
          title : '性别',
          width : '60px'
        }, {
          data : 'birthday',
          title : '出生年月',
          width : '120px'
        }, {
          data : '',
          title : '所在地',
          width : '120px',
          render : function(data, type, full) {
            return full.province + '-' + full.city + '-' + full.district;
          }
        }, {
          data : '',
          title : '家乡所在地',
          width : '120px',
          render : function(data, type, full) {
            return full.homeProvince + '-' + full.homeCity + '-' + full.homeDistrict;
          }
        }, {
          data : 'job',
          title : '职业',
          width : '80px'

        }, {
          data : 'consumptionLevel',
          title : '收入水平',
          width : '100px'
        }, {
          data : 'appearanceScore',
          title : '颜值评分',
          width : '100px'
        } ]
      }
    });

  });
</script>

<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/portrait">用户画像管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-graduation"></i><span>用户画像管理 </span>
        </div>
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
              
              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称" />
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
