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

<script id="firstVisitStatusTmpl" type="text/x-handlebars-template">
    <form id="firstVisitStatusForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">回访结果<span class="required"> * </span></label>
                <div class="col-md-5">
                    <select name="firstVisitStatus" class="form-control">
                        <option value="2">通过</option>
                        <option value="0">拒绝</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">回访备注
                </label>
                <div class="col-md-5" style="width: 300px">
                    <textarea type="text" class="form-control" name="visitRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="firstVisitStatusSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="firstVisitStatusCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>

<script id="thirdVisitStatusTmpl" type="text/x-handlebars-template">
    <form id="thirdVisitStatusForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">回访结果<span class="required"> * </span></label>
                <div class="col-md-5">
                    <select name="thirdVisitStatus" class="form-control">
                        <option value="2">通过</option>
                        <option value="0">拒绝</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">回访备注
                </label>
                <div class="col-md-5" style="width: 300px">
                    <textarea type="text" class="form-control" name="visitRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="thirdVisitStatusSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="thirdVisitStatusCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>

<script id="secondVisitStatusTmpl" type="text/x-handlebars-template">
    <form id="secondVisitStatusForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 100%; margin: 20px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">回访结果<span class="required"> * </span></label>
                <div class="col-md-5">
                    <select name="secondVisitStatus" class="form-control">
                        <option value="2">通过</option>
                        <option value="0">拒绝</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">回访备注
                </label>
                <div class="col-md-5" style="width: 300px">
                    <textarea type="text" class="form-control" name="visitRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="secondVisitStatusSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="secondVisitStatusCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>


<script id="visitUserTmpl" type="text/x-handlebars-template">
    <form id="visitUserForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">客服手机<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="phone" value="{{phone}}" class="form-control" placeholder="请输入客服手机"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="visitUserSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="visitUserCancel{{id}}" class="btn default" >
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>


<script id="visitUserListTmpl" type="text/x-handlebars-template">
    <form id="visitUserListForm" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="ids" value="{{ids}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">客服手机<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="phone" value="{{phone}}" class="form-control" placeholder="请输入客服手机"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="visitUserListSubmit" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="visitUserListCancel" class="btn default" data-href="">
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

    var firstVisitStatusTemplate = Handlebars.compile($('#firstVisitStatusTmpl').html());
    $('#dataTable').on('click', '.firstVisitStatus', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = firstVisitStatusTemplate(data);
        var fIndex = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['500px', '300px'], //宽高
            content: html
        });

        $form = $('#firstVisitStatusForm' + id);
        $form.validate({
            rules: {
                firstVisitStatus: {
                    required: true
                },
                revieweRemark: {}
            },
            messages: {}
        });

        $('#firstVisitStatusSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourUser/updateVisitStatus';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(fIndex);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('审核失败,原因' + data.message);
                    }
                });
            }
        })

        $('#firstVisitStatusCancel' + id).bind('click', function () {
            layer.close(fIndex);
        })

    });

    var secondVisitStatusTemplate = Handlebars.compile($('#secondVisitStatusTmpl').html());
    $('#dataTable').on('click', '.secondVisitStatus', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = secondVisitStatusTemplate(data);
        var sIndex = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['500px', '300px'], //宽高
            content: html
        });

        $form = $('#secondVisitStatusForm' + id);
        $form.validate({
            rules: {
                secondVisitStatus: {
                    required: true
                },
                revieweRemark: {}
            },
            messages: {}
        });

        $('#secondVisitStatusSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourUser/updateVisitStatus';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.close(sIndex);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert('审核失败,原因' + data.message);
                    }
                });
            }
        })

        $('#secondVisitStatusCancel' + id).bind('click', function () {
            layer.close(sIndex);
        })

    });

    var thirdVisitStatusTemplate = Handlebars.compile($('#thirdVisitStatusTmpl').html());
    $('#dataTable').on('click', '.thirdVisitStatus', function () {
        var id = $(this).data('id');
        var data = {
            id: id
        };
        var html = thirdVisitStatusTemplate(data);
        var index = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['500px', '300px'], //宽高
            content: html
        });

        $form = $('#thirdVisitStatusForm' + id);
        $form.validate({
            rules: {
                thirdVisitStatus: {
                    required: true
                },
                revieweRemark: {}
            },
            messages: {}
        });

        $('#thirdVisitStatusSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourUser/updateVisitStatus';
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

        $('#thirdVisitStatusCancel' + id).bind('click', function () {
            layer.close(index);
        })

    });



    var visitUserTemplate = Handlebars.compile($('#visitUserTmpl').html());
    $('#dataTable').on('click', '.visit-user', function () {
        var id = $(this).data('id');
        var phone = $(this).data('phone');
        var data = {
            id: id,
            phone: phone
        };
        var html = visitUserTemplate(data);
        var index = layer.open({
            type: 1,
            //skin: 'layui-layer-rim', //加上边框
            area: ['450px', '300px'], //宽高
            content: html
        });

        $form = $('#visitUserForm' + id);
        $form.validate({
            rules: {
                'phone': {
                    required: true
                }
            },
            messages: {}
        });

        $('#visitUserSubmit' + id).bind('click', function () {
            var result = $form.validate().form();
            if (result) {
                var url = '${ctx}/tourUser/visitUser';
                $.post(url, $form.serialize(), function (data) {
                    if (data.code === 0) {
                        layer.alert('操作成功');
                        layer.close(index);
                        grid.getDataTable().ajax.reload(null, false);
                    } else {
                        layer.alert(data.message);
                    }
                });
            }
        })

        $('#visitUserCancel' + id).bind('click', function () {
            layer.close(index);
        })

    });

    $('#dataTable').on('click', '#checkAll', function(){
                var isChecked = $(this).attr("checked");
                if(isChecked == 'checked'){
                    $("input[name='id']").each(function() {
                        var disableAttr = $(this).attr("disabled");
                        if(disableAttr == undefined){
                            $(this).parent().addClass("checked");
                            $(this).attr("checked",'true');
                        }
                    });
                } else {
                    $("input[name='id']").each(function() {
                        var disableAttr = $(this).attr("disabled");
                        if(disableAttr == undefined){
                            $(this).parent().removeClass("checked");
                            $(this).removeAttr("checked");
                        }
                    });
                }
            }
    );

    <shiro:hasPermission name="tourUser:visitUser">
        var modifyTemplate = Handlebars.compile($('#visitUserListTmpl').html());
        $('.table-toolbar').on('click', '#visitUserListBtn',function(){
                var ids = '';
                var phone = $(this).data('phone');
                $("input[name='id']").each(function() {
                    var isChecked = $(this).attr("checked");
                    if(isChecked == 'checked'){
                        ids += $(this).val() + ',';
                    }
                })
                if(ids.length > 0){
                    var data = {
                    ids: ids,
                    phone: phone
                };
                    var html = modifyTemplate(data);
                    var index = layer.open({
                        type: 1,
                        //skin: 'layui-layer-rim', //加上边框
                        area: ['450px', '300px'], //宽高
                        content: html
                });

                $form = $('#visitUserListForm');
                $form.validate({
                    rules: {
                        'phone': {
                            required: true
                        }
                    },
                    messages: {}
                });

                $('#visitUserListSubmit').bind('click', function () {
                        var result = $form.validate().form();
                        if (result) {
                            var url = '${ctx}/tourUser/visitUserList';
                        $.post(url, $form.serialize(), function (data) {
                            if (data.code === 0) {
                                layer.alert('操作成功');
                                layer.close(index);
                                grid.getDataTable().ajax.reload(null, false);
                        } else {
                                layer.alert('操作失败:' + data.message);
                            }
                        });
                    }
                                layer.close(index);
                })

                $('#visitUserListCancel').bind('click', function () {
                        layer.close(index);
                })

                } else {
                        layer.alert("请至少选择一条记录！", {icon: 2});
                    }
                });
        </shiro:hasPermission>





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
                        data: 'id',
                        title: '<input type="checkbox" name="checkAll" id="checkAll" value=""/>',
                        render: function (data, type, full) {
                            if(full.auditStatus == 1 && full.visitUserId == null){
                                return '<input type="checkbox" name="id" id="id" value="' + data + '"/>';
                            } else {
                                return '<input type="checkbox" name="id" disabled="disabled" id="id" value="' + data + '"/>';
                            }
                        }
                    },
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
                            if (data == 0){
                                return '<label class="label label-primary">我要报名</label>';
                            }else if(data == 1){
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
                        data: 'firstVisitStatus',
                        title: '初访状态',
                        orderable: false,
                        render: function (data, type, full) {
                            if (data == 0){
                                return '<label class="label label-default">回访失败</label>';
                            }else if(data == 1){
                                return '<label class="label label-danger">回访中</label>';
                            }else if(data == 2){
                                return '<label class="label label-success">回访成功</label>';
                            }
                        }
                    },             {
                        data: 'secondVisitStatus',
                        title: '二访状态',
                        orderable: false,
                        render: function (data, type, full) {
                            if (data == 0){
                                return '<label class="label label-default">回访失败</label>';
                            }else if(data == 1){
                                return '<label class="label label-danger">回访中</label>';
                            }else if(data == 2){
                                return '<label class="label label-success">回访成功</label>';
                            }
                        }
                    },             {
                        data: 'thirdVisitStatus',
                        title: '三访状态',
                        orderable: false,
                        render: function (data, type, full) {
                            if (data == 0){
                                return '<label class="label label-default">回访失败</label>';
                            }else if(data == 1){
                                return '<label class="label label-danger">回访中</label>';
                            }else if(data == 2){
                                return '<label class="label label-success">回访成功</label>';
                            }
                        }
                    },
                    {
                        data: 'visitRemark',
                        title: '回访备注',
                        orderable: false
                    },
                    {
                        data: 'visitTime',
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
                            }else {
                                return ' ';
                            }
                        }
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
                            var optionHtml = '';
                            <shiro:hasPermission name="tourUser:edit">
                            optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/tourUser/update/' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
                            if (full.auditStatus == 1){
                                <shiro:hasPermission name="tourUser:visitUser">
                                if(full.visitUserId == null) {
                                    optionHtml += '<a class="btn btn-xs default blue-stripe visit-user" data-id="' + full.id + '" href="javascript:;" ><i class="fa fa-edit"></i> 分配客服</a>';
                                }
                                </shiro:hasPermission>
                                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="resetAjax(' + full.id + ')"><i class="fa fa-edit"></i> 重置 </a>';
                                optionHtml += '<a class="btn btn-xs default yellow-stripe report-confirm" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 审核 </a>';
                                if (full.firstVisitStatus == 1){
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe firstVisitStatus" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 初访 </a>';
                                }
                                if (full.firstVisitStatus == 0 && full.secondVisitStatus == 1){
                                    optionHtml += '<a class="btn btn-xs default green-stripe secondVisitStatus" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 二访 </a>';
                                }
                                if (full.secondVisitStatus == 0 && full.thirdVisitStatus == 1){
                                    optionHtml += '<a class="btn btn-xs default green-stripe thirdVisitStatus" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 三访 </a>';
                                }
                            }
                            </shiro:hasPermission>
                            return optionHtml;
                        }
                    }]
            }
        });

    });
    <shiro:hasPermission name="tourUser:edit">

    function resetAjax(id) {
        layer.confirm('您确认重置吗?', {
            btn: ['重置','取消'] //按钮
        }, function(){
            $.post('${ctx}/tourUser/reset', {id: id}, function (result) {
                layer.msg('重置成功！');
                grid.getDataTable().ajax.reload(null, false);
            });
        }, function(){

        });
    }

    <shiro:hasPermission name="tourUser:export">
    function tourUserExport() {
        location.href = '${ctx}/tourUser/tourUserExport?' + $('#searchForm').serialize();
    }
    </shiro:hasPermission>

    </shiro:hasPermission>
</script>
<!-- END JAVASCRIPTS -->
<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/tourUser">游客信息管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open"></i> 游客信息管理
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
                                <input type="text" name="reportId" class="form-control" placeholder="检测报告编号"/>
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
                                <select name="auditStatus" class="form-control">
                                    <option value="">-- 审核状态 --</option>
                                    <option value="1">审核中</option>
                                    <option value="2">待补充</option>
                                    <option value="3">已生效</option>
                                    <option value="4">已完成</option>
                                    <option value="5">审核失败</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select name="firstVisitStatus" class="form-control">
                                    <option value="">-- 初访状态 --</option>
                                    <option value="0">回访失败</option>
                                    <option value="1">回访中</option>
                                    <option value="2">回访成功</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select name="secondVisitStatus" class="form-control">
                                    <option value="">-- 二访状态 --</option>
                                    <option value="0">回访失败</option>
                                    <option value="1">回访中</option>
                                    <option value="2">回访成功</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select name="thirdVisitStatus" class="form-control">
                                    <option value="">-- 三访状态 --</option>
                                    <option value="0">回访失败</option>
                                    <option value="1">回访中</option>
                                    <option value="2">回访成功</option>
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
                            <shiro:hasPermission name="tourUser:export">
                                <div class="form-group">
                                    <button type="button" class="btn yellow" onClick="tourUserExport()">
                                        <i class="fa fa-file-excel-o"></i> 导出Excel
                                    </button>
                                </div>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="tourUser:visitUser">
                                <div class="form-group">
                                    <button type="button" class="btn green" id="visitUserListBtn">
                                        <i class="fa fa-send-o"></i> 批量分配
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
