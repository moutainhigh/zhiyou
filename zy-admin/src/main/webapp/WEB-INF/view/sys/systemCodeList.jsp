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
          url: '${ctx}/systemCode', // ajax source
        },
        columns: [
	        {
		        data: 'systemType',
		        title: '系统类型码',
		        orderable: false
	        },
	        {
		        data: 'systemName',
		        title: '名称',
		        orderable: false

	        },
	        {
		        data: 'systemValue',
		        title: '默认值',
		        orderable: false

	        },
	        {
		        data: 'systemDesc',
		        title: '描述',
		        orderable: false

	        },
            {
                data: 'createDate',
                title: '创建时间',
                render: function(data, type, full) {
                    return full.createTimeLabel;
                }
            },
            {
                data: '',
                title: '创建人',
                orderable: false,
                render: function(data, type, full) {
                    return formatUser(full.createUser);
                }
            },
            {
                data: 'updateDate',
                title: '修改时间',
                render: function(data, type, full) {
                    return full.updateTimeLabel;
                }
            },
            {
                data: '',
                title: '修改人',
                orderable: false,
                render: function(data, type, full) {
                    return formatUser(full.updateUser);
                }
            },
            {
                data: 'id',
                title: '操作',
                width: '200px',
                orderable: false,
                render: function (data, type, full) {
                    var optionHtml = '';
                    <shiro:hasPermission name="tourSetting:edit">
                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/systemCode/editSystemCode/' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
                    optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="deleteAjax(' + full.id + ')"><i class="fa fa-trash-o"></i> 删除 </a>';
                    </shiro:hasPermission>
                    return optionHtml;
                }
            }
        ]
      }
    });
  });

  function deleteAjax(id) {
      layer.confirm('删除不可恢复，您确认删除此系统默认值吗?', {
          btn: ['删除','取消'] //按钮
      }, function(){
          $.post('${ctx}/systemCode/deleteSystemCode', {id: id}, function (result) {
              grid.getDataTable().ajax.reload(null, false);
          });
          layer.msg('删除成功！');
      }, function(){

      });
  }

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/systemCode">系统默认值管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 系统默认值管理
        </div>
        <shiro:hasPermission name="systemCode:edit">
          <div class="actions">
              <a class="btn btn-circle green" data-href="${ctx}/systemCode/createSystemCode">
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
              <input id="systemFlag" name="systemFlag" type="hidden" value="0"/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="systemTypeLK" class="form-control" placeholder="系统类型码"/>
              </div>

              <div class="form-group">
                <input type="text" name="systemNameLK" class="form-control" placeholder="名字"/>
              </div>


              <div class="form-group">
                <input type="text" name="createNicknameLK" class="form-control" placeholder="创建人昵称"/>
              </div>

              <div class="form-group">
                 <input type="text" name="createPhoneEQ" class="form-control" placeholder="创建人手机号"/>
              </div>

              <div class="form-group">
                   <input type="text" name="updateNicknameLK" class="form-control" placeholder="修改人昵称"/>
              </div>

              <div class="form-group">
                <input type="text" name="updatePhoneEQ" class="form-control" placeholder="修改人手机号"/>
              </div>

                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text"
                           onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createTimeGTE" value="" placeholder="创建时间起"/>
                </div>
                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                           name="createTimeLT" value="" placeholder="创建时间止"/>
                </div>

                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text"
                           onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="updateTimeGTE" value="" placeholder="修改时间起"/>
                </div>
                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                           name="updateTimeLT" value="" placeholder="修改时间止"/>
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