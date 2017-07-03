<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script id="confirmTmpl" type="text/x-handlebars-template">
    <form id="confirmForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">审核结果<span class="required"> * </span></label>
                <div class="col-md-5">
                    <select name="isSuccess" class="form-control">
                        <option value="true">通过</option>
                        <option value="false">拒绝</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">审核备注
                </label>
                <div class="col-md-5" style="width: 300px">
                    <textarea type="text" class="form-control" name="revieweRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="reportConfirmSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="reportConfirmCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<script>
    var grid = new Datatable();

    var template = Handlebars.compile($('#confirmTmpl').html());
    $('#dataTable').on('click', '.report-confirm', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = template(data);
        var index = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['500px', '300px'], //宽高
            content: html
        });

        $form = $('#confirmForm' + id);
        $form.validate({
            rules: {
                isSuccess: {
                    required: true
                },
                revieweRemark: {}
            },
            messages: {}
        });

        $('#reportConfirmSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourUser/updateAuditStatus';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(index);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('审核失败,原因' + data.message);
                    }
                });
            }
        })

        $('#reportConfirmCancel' + id).bind('click', function () {
            layer.close(index);
        })

    });

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
                    url: '${ctx}/tourUser', // ajax source
                },
                columns: [
                    {
                        data: 'imageThumbnail',
                        title: '票务照片',
                        orderable: false,
                        render: function (data, type, full) {
                            return '<a target="_blank" href="' + data + '"><img style="width:160px;height:80px;"  src="' +data+ '"/></a>';
                        }
                    },
                    {
                        data: 'sequenceId',
                        title: '旅行申请单号',
                        orderable: false
                    },
                    {
                        data: 'reportId',
                        title: '检测报告编号',
                        orderable: false
                    },
                    {
                        data: 'userName',
                        title: '用户',
                        orderable: false,
                    },
                    {
                        data: 'tourTitle',
                        title: '线路',
                        orderable: false
                    },
                    {
                        data: 'tourTime',
                        title: '出游时间',
                        orderable: false
                    },
                    {
                        data: 'parentName',
                        title: '推荐人',
                        orderable: false
                    },
                    {
                        data: 'auditStatus',
                        title: '审核状态',
                        orderable: false,
                        render: function (data, type, full) {
                            if(data == 1){
                                return '<label class="label label-danger">审核中</label>';
                            }else if(data == 2){
                                return '<label class="label label-warning">待补充</label>';
                            }else if(data == 3){
                                return '<label class="label label-info">已生效</label>';
                            }else if(data == 4){
                                return '<label class="label label-success">已完成</label>';
                            }else if(data == 5){
                                return '<label class="label label-default">审核失败</label>';
                            }
                        }
                    },
                    {
                        data: 'updateDateLabel',
                        title: '状态时间',
                        orderable: false
                    },
                    {
                        data: 'houseType',
                        title: '房型需求',
                        orderable: false,
                        render: function (data, type, full) {
                            if(data == 1){
                                return '标准间';
                            }else if(data == 2){
                                return '三人间';
                            }
                        }
                    },
                    {
                        data: 'isAddBed',
                        title: '房型需求',
                        orderable: false,
                        render: function (data, type, full) {
                            if(data == 0){
                                return '否';
                            }else if(data == 1){
                                return '是';
                            }
                        }
                    },
                    {
                        data: 'updateName',
                        title: '审核员',
                        orderable: false
                    },
                    {
                        data: 'userRemark',
                        title: '用户备注',
                        orderable: false
                    },
                    {
                        data: 'revieweRemark',
                        title: '审核备注',
                        orderable: false
                    },
                    {
                        data: 'id',
                        title: '操作',
                        orderable: false,
                        render: function (data, type, full) {
                            var optionHtml = '';
                            <shiro:hasPermission name="tourUser:edit">
                            if (full.auditStatus == 1){
                                optionHtml += '<a class="btn btn-xs default yellow-stripe report-confirm" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 审核 </a>';
                            }
                            if (full.isReleased) {
                                optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" onclick="unrelease(' + full.id + ')"><i class="fa fa-times X"></i> 取消发布 </a>';
                            } else {
                                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="release(' + full.id + ')"><i class="fa fa-check"></i> 发布 </a>';
                            }
                            optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="deleteAjax(' + full.id + ')"><i class="fa fa-trash-o"></i> 删除 </a>';
                            </shiro:hasPermission>
                            return optionHtml;
                        }
                    }]
            }
        });

    });
    <shiro:hasPermission name="tourUser:edit">
    function release(id) {
        alert("测试")
        $.ajax({
            url: '${ctx}/tour/findTourTime?tourId='+ id,
            dataType: 'html',
            success: function(data) {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['960px', '640px'], //宽高
                    content: data
                });
            }
        })
        //releaseAjax(id, true);
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
    function deleteAjax(id) {
        layer.confirm('您确认删除此旅游信息嘛?', {
            btn: ['删除','取消'] //按钮
        }, function(){
            $.post('${ctx}/article/delete', {id: id}, function (result) {
                grid.getDataTable().ajax.reload(null, false);
            });
            layer.msg('删除成功！');
        }, function(){

        });
    }
    </shiro:hasPermission>
    function qrCode(id) {
        layer.open({
            area: ['300', '400'],
            content:'<div style="width: 240px; height: 240px;"><img src="${ctx}/article/detailQrCode?id='+ id +'" ></div>'
        });
    }
</script>
<!-- END JAVASCRIPTS -->
<script type="text/javascript">
    <shiro:hasPermission name="tourUser:export">
    function tourUserExport() {
        location.href = '${ctx}/tourUser/tourUserExport?' + $('#searchForm').serialize();
    }
    </shiro:hasPermission>
</script>
<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/tourUser">旅客信息管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open"></i> 旅客信息管理
                </div>
            <%--<shiro:hasPermission name="tour:edit">--%>
                <%--<div class="actions">--%>
                    <%--<a class="btn btn-circle green" data-href="${ctx}/tour/create">--%>
                        <%--<i class="fa fa-plus"></i> 新增--%>
                    <%--</a>--%>
                <%--</div>--%>
            <%--</shiro:hasPermission>--%>
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
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="releasedTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="releasedTimeLT" value="" placeholder="发布时间止"/>
                            </div>
                            <div class="form-group">
                                <select name="isReleasedEQ" class="form-control">
                                    <option value="">-- 是否发布 --</option>
                                    <option value="true">是</option>
                                    <option value="false">否</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <button class="btn blue filter-submit">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                            <shiro:hasPermission name="tourUser:export">
                                <div class="form-group">
                                    <button class="btn yellow" onClick="tourUserExport()">
                                        <i class="fa fa-file-excel-o"></i> 导出Excel
                                    </button>
                                </div>
                            </shiro:hasPermission>
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
