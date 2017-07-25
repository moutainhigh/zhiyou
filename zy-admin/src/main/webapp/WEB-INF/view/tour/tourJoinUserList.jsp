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
                <label class="control-label col-md-2">是否参游<span class="required"> * </span></label>
                <div class="col-md-5">
                    <select name="isJoin" class="form-control">
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">消费金额(元)
                </label>
                <div class="col-md-5">
                    <input type="text" class="form-control" name="amount"/>
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

<script id="refundAmountTmpl" type="text/x-handlebars-template">
    <form id="refundAmountForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">金额(元)<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" class="form-control" name="refundAmount"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="refundAmountSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="refundAmountCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>

<script id="guaranteeAmountTmpl" type="text/x-handlebars-template">
    <form id="guaranteeAmountForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">金额(元)<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" class="form-control" name="guaranteeAmount"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="guaranteeAmountSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="guaranteeAmountCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>

<script id="surchargeTmpl" type="text/x-handlebars-template">
    <form id="surchargeForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">金额(元)<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" class="form-control" name="surcharge"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="surchargeSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="surchargeCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<script>
    var grid = new Datatable();

    var tourUserTemplate = Handlebars.compile($('#confirmTmpl').html());
    $('#dataTable').on('click', '.report-confirm', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = tourUserTemplate(data);
        var tourUserIndex = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['500px', '300px'], //宽高
            content: html
        });

        $form = $('#confirmForm' + id);
        $form.validate({
            rules: {
                isJoin: {
                    required: true
                },
                amount: {
                    number: true
                }
            },
            messages: {}
        });

        $('#reportConfirmSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourUser/addInfo';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(index);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('补充信息失败,原因' + data.message);
                    }
                });
            }
        })

        $('#reportConfirmCancel' + id).bind('click', function () {
            layer.close(tourUserIndex);
        })

    });

    var guaranteeAmountTemplate = Handlebars.compile($('#guaranteeAmountTmpl').html());
    $('#dataTable').on('click', '.guaranteeAmount', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = guaranteeAmountTemplate(data);
        var index = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            content: html
        });

        $form = $('#guaranteeAmountForm' + id);
        $form.validate({
            rules: {
                guaranteeAmount: {
                    number: true
                }
            },
            messages: {}
        });

        $('#guaranteeAmountSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourJoinUser/amount';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(index);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('补充信息失败,原因' + data.message);
                    }
                });
            }
        })

        $('#guaranteeAmountCancel' + id).bind('click', function () {
            layer.close(index);
        })

    });


    var surchargeTemplate = Handlebars.compile($('#surchargeTmpl').html());
    $('#dataTable').on('click', '.surcharge', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = surchargeTemplate(data);
        var index = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            content: html
        });

        $form = $('#surchargeForm' + id);
        $form.validate({
            rules: {
                surcharge: {
                    number: true
                }
            },
            messages: {}
        });

        $('#surchargeSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourJoinUser/amount';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(index);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('补充信息失败,原因' + data.message);
                    }
                });
            }
        })

        $('#surchargeCancel' + id).bind('click', function () {
            layer.close(index);
        })

    });


    var template = Handlebars.compile($('#refundAmountTmpl').html());
    $('#dataTable').on('click', '.refundAmount', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = template(data);
        var index = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            content: html
        });

        $form = $('#refundAmountForm' + id);
        $form.validate({
            rules: {
                refundAmount: {
                    number: true
                }
            },
            messages: {}
        });

        $('#refundAmountSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourJoinUser/amount';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(index);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('补充信息失败,原因' + data.message);
                    }
                });
            }
        })

        $('#refundAmountCancel' + id).bind('click', function () {
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
                    url: '${ctx}/tourJoinUser', // ajax source
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
                        data: 'idCardNumber',
                        title: '身份证',
                        orderable: false,
                    },
                    {
                        data: 'age',
                        title: '年龄',
                        orderable: false,
                    },
                    {
                        data: 'gender',
                        title: '性别',
                        orderable: false
                    },
                    {
                        data: 'userPhone',
                        title: '用户电话',
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
                        data: 'parentPhone',
                        title: '推荐人电话',
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
                        title: '是否加床',
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
                        data: 'isTransfers',
                        title: '是否接机/车',
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
                        data: 'carNumber',
                        title: '班号',
                        orderable: false
                    },
                    {
                        data: 'planTimeLabel',
                        title: '预计到达时间',
                        orderable: false
                    },
                    {
                        data: 'userRemark',
                        title: '用户备注',
                        orderable: false
                    },
                    {
                        data: 'updateName',
                        title: '审核员',
                        orderable: false
                    },
                    {
                        data: 'revieweRemark',
                        title: '审核备注',
                        orderable: false
                    },
                    {
                        data: 'isJoin',
                        title: '是否参游',
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
                        data: 'guaranteeAmount',
                        title: '保障金额(元)',
                        orderable: false
                    },
                    {
                        data: 'refundAmount',
                        title: '退回保障金额(元)',
                        orderable: false
                    },
                    {
                        data: 'surcharge',
                        title: '附加费(元)',
                        orderable: false
                    },
                    {
                        data: 'amount',
                        title: '消费金额(元)',
                        orderable: false
                    },
                    {
                        data: 'isEffect',
                        title: '是否有效',
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
                        data: 'id',
                        title: '操作',
                        orderable: false,
                        render: function (data, type, full) {
                        <shiro:hasPermission name="tourJoinUser:edit">
                            var optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/tourJoinUser/update/' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
                            if (full.auditStatus == 3){
                                optionHtml += '<a class="btn btn-xs default yellow-stripe report-confirm" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 旅客信息补充 </a>';
                            }
                            optionHtml += '<a class="btn btn-xs default yellow-stripe guaranteeAmount" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 保障金额 </a>';
                            optionHtml += '<a class="btn btn-xs default yellow-stripe refundAmount" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 退回保障金额 </a>';
                            optionHtml += '<a class="btn btn-xs default yellow-stripe surcharge" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 附加费 </a>';
                        </shiro:hasPermission>
                            return optionHtml;
                        }
                    }]
            }
        });

    });
    <shiro:hasPermission name="tourJoinUser:edit">

    <shiro:hasPermission name="tourJoinUser:export">
    function tourJoinUserExport() {
        location.href = '${ctx}/tourJoinUser/tourJoinUserExport?' + $('#searchForm').serialize();
    }
    </shiro:hasPermission>

    </shiro:hasPermission>
</script>
<!-- END JAVASCRIPTS -->
<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/touJoinrUser">参游旅客信息管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open"></i> 参游旅客信息管理
                </div>
            </div>
            <div class="portlet-body clearfix">
                <div class="table-container">
                    <div class="table-toolbar">
                        <form class="filter-form form-inline" id="searchForm">
                            <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                            <input id="_direction" name="direction" type="hidden" value=""/>
                            <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                            <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

                            <div class="form-group">
                                <input type="text" name="sequenceId" class="form-control" placeholder="旅行申请单号"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="tourTitle" class="form-control" placeholder="旅行线路"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="userName" class="form-control" placeholder="用户名"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="userPhone" class="form-control" placeholder="用户电话"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="parentName" class="form-control" placeholder="推荐人"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="parentPhone" class="form-control" placeholder="推荐人电话"/>
                            </div>
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="beginTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="beginTime" value="" placeholder="出游开始时间"/>
                            </div>
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="endTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="endTime" value="" placeholder="出游结束时间"/>
                            </div>
                            <div class="form-group">
                                <select name="isTransfers" class="form-control">
                                    <option value="">-- 是否接机/车 --</option>
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="planStartTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="planStartTime" value="" placeholder="计划到达起始时间"/>
                            </div>
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="planEndTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="planEndTime" value="" placeholder="计划到达结束时间"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="guaranteeAmount" class="form-control" placeholder="保障金额"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="refundAmount" class="form-control" placeholder="退回保障金额"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="surcharge" class="form-control" placeholder="附加费"/>
                            </div>
                            <div class="form-group">
                                <select name="isJoin" class="form-control">
                                    <option value="">-- 是否参游 --</option>
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select name="auditStatus" class="form-control">
                                    <option value="">-- 审核状态 --</option>
                                    <%--<option value="1">审核中</option>--%>
                                    <%--<option value="2">待补充</option>--%>
                                    <option value="3">已生效</option>
                                    <option value="4">已完成</option>
                                    <%--<option value="5">审核失败</option>--%>
                                </select>
                            </div>
                            <%--<div class="form-group">--%>
                                <%--<select name="isEffect" class="form-control">--%>
                                    <%--<option value="">-- 是否有效 --</option>--%>
                                    <%--<option value="1">是</option>--%>
                                    <%--<option value="0">否</option>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                            <div class="form-group">
                                <button class="btn blue filter-submit">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                            <shiro:hasPermission name="tourJoinUser:export">
                                <div class="form-group">
                                    <button type="button" class="btn yellow" onClick="tourJoinUserExport()">
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
