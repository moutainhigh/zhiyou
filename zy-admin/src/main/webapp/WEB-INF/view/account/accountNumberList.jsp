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
                lengthMenu : [ [ 10, 20, 50, 100 ], [ 10, 20, 50, 100 ] ],// change per page values here
                pageLength: 20, // default record count per page
                order: [
                ],
                ajax: {
                    url: '${ctx}/accountnumber', // ajax source
                },
                columns: [
                    {
                        data: 'id',
                        title: '编号',
                        orderable: true
                    },
                    {
                        data: 'oldName',
                        title: '原用户名',
                        orderable: false
                    },
                    {
                        data: 'oldPhone',
                        title: '原账号',
                        orderable: false
                    },
                    {
                        data: 'newPhone',
                        title: '新账号',
                        orderable: false
                    },
                    {
                        data: 'flage',
                        title: '是否已使用',
                        orderable: false,
                        render: function (data, type, full) {
                            if (data==1) {
                                return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 已使用 </span>';
                            }
                            return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 未使用 </span>';
                        }
                    },
                    {
                        data: 'createDate',
                        title: '发布时间',
                        orderable: false,
                        render: function (data, type, full) {
                            return full.createDateLable;
                        }
                    },
                    {
                        data: 'createName',
                        title: '创建人姓名',
                        orderable: false
                    },
                    {
                        data: 'id',
                        title: '操作',
                        orderable: false,
                        render: function (data, type, full) {
                            var optionHtml = '';
                            <shiro:hasPermission name="accountNumber:edit">
                            if (full.flage==0) {
                               optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;"  onclick="toupdateAjax(' + full.id + ')"><i class="fa fa-edit"></i> 编辑 </a>';
                            }
                            optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="deleteAjax(' + full.id + ')"><i class="fa fa-trash-o"></i> 删除 </a>';
                            </shiro:hasPermission>
                            return optionHtml;
                        }
                    }]
            }
        });

    });
    <shiro:hasPermission name="accountNumber:edit">

    function toupdateAjax(id) {

        $.ajax({
            url: '${ctx}/accountnumber/toupdate?id='+id,
            dataType: 'html',
            success: function(data) {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['800px', '500px'], //宽高
                    content: data
                });
            }
        })
    }

    function deleteAjax(id) {
        layer.confirm('您确认删除此旅游信息嘛?', {
            btn: ['删除','取消'] //按钮
        }, function(){
            $.post('${ctx}/accountnumber/delete', {id: id,isReleased:false}, function (result) {
                layer.msg('删除成功！');
                grid.getDataTable().ajax.reload(null, false);
            });
        }, function(){

        });
    }
    </shiro:hasPermission>


    function addAccountNumber(id) {
        $.ajax({
            url: '${ctx}/accountnumber/toCreate',
            dataType: 'html',
            success: function(data) {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                   area: ['800px', '500px'], //宽高
                    content: data
                });
            }
        })
    }
    function refreshData() {
        $(".filter-submit").click();
    }
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/accountNumber">账号迁移管理管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open"></i> 账号迁移管理管理
                </div>
                <shiro:hasPermission name="accountNumber:edit">
                    <div class="actions">
                        <a class="btn btn-circle green" href="javascript:;"  onclick="addAccountNumber()">
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
                                <input type="text" name="oldPhoneEQ" class="form-control" placeholder="原账号手机号"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="newPhoneEQ" class="form-control" placeholder="新账号手机号"/>
                            </div>
                           <%-- <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="createDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="createDate" value="" placeholder="创建时间"/>
                            </div>--%>
                            <div class="form-group">
                                <select name="flageEQ" class="form-control">
                                    <option value="">-- 是否使用 --</option>
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
