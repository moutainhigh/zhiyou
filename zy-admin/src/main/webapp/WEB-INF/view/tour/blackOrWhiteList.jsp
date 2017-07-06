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
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/tour/blackOrWhite', // ajax source
        },
        columns: [
	        {
		        data: '',
		        title: '昵称',
		        orderable: false,
            render: function(data, type, full) {
              return full.user.nickname;
            }
	        },
	        {
		        data: '',
		        title: '手机号',
		        orderable: false,
            render: function(data, type, full) {
              return full.user.phone;
            }
	        },
	        {
		        data: '',
		        title: '等级',
                orderable: false,
            render: function(data, type, full) {
              return full.user.userRankLabel;
            }
	        },
	        {
		        data: 'type',
		        title: '类型',
                render: function(data, type, full) {
                    if (data == 1) {
                        return '<label class="label label-danger">黑名单</label>';
                    } else if(data == 2) {
                        return '<label class="label label-default">白名单</label>';
                    }else{
                        return "";
                    }
                }
	        },
            {
                data: 'number',
                title: '限制人数',
            },

            {
                data: 'id',
                title: '操作',
                width: '200px',
                orderable: false,
                render: function (data, type, full) {
                    var optionHtml = '';
                    <shiro:hasPermission name="tourSetting:edit">
                        optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/tour/editBlackWhite/' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
                        optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/tour/deleteBlackWhite/' + data + '" data-confirm="您确定要撤销该用户的黑/白名单设置吗？"><i class="fa fa-trash-o"></i> 撤销 </a>';
                    </shiro:hasPermission>
                    return optionHtml;
                }
            }

        ]
      }
    });

  });

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/tour/blackOrWhite">黑白名单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 黑白名单管理
        </div>
        <shiro:hasPermission name="tourSetting:edit">
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/tour/createBlackWhite">
              <i class="fa fa-plus"></i> 新增
          </a>
        </div>
        </shiro:hasPermission>
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
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
              </div>

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号"/>
              </div>

              <div class="form-group">
                <input type="number" name="numberGTE" class="form-control" placeholder="限制人数下限"/>
              </div>

              <div class="form-group">
                <input type="number" name="numberLT" class="form-control" placeholder="限制人数上限"/>
              </div>

              <div class="form-group">
                <select name="type" class="form-control">
                  <option value="">-- 黑/白名单 --</option>
                  <option value="1">黑名单</option>
                  <option value="2">白名单</option>
                </select>
              </div>

                <div class="form-group">
                    <select name="userRankEQ" class="form-control">
                        <option value="">-- 用户等级 --</option>
                        <c:forEach items="${userRankMap}" var="userRankMap">
                            <option value="${userRankMap.key}">${userRankMap.value}</option>
                        </c:forEach>
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