<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<%--<script id="modifyLastloginTimeTmpl" type="text/x-handlebars-template">--%>
<%--<form id="modifylastloginTimeForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">--%>
<%--<input type="hidden" name="userId" value="{{id}}"/>--%>
<%--<div class="form-body">--%>
<%--<div class="alert alert-danger display-hide">--%>
<%--<i class="fa fa-exclamation-circle"></i>--%>
<%--<button class="close" data-close="alert"></button>--%>
<%--<span class="form-errors">您填写的信息有误，请检查。</span>--%>
<%--</div>--%>
<%--<div class="form-group">--%>
<%--<label class="control-label col-md-2">昵称<span class="required"> * </span></label>--%>
<%--<div class="col-md-5">--%>
<%--<input type="text" name="nickname" readonly="true" value="{{nickname}}" class="form-control" placeholder="请输入昵称"/>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="form-group">--%>
<%--<label class="control-label col-md-2">最后一次登录时间<span class="required"> * </span></label>--%>
<%--<div class="col-md-5">--%>
<%--<input class="form-control" type="text" id="lastloginTime"--%>
<%--&lt;%&ndash;<input type="text" name="lastloginTime" readonly="true" value="{{lastloginTime}}" class="form-control" placeholder="请输入昵称"/>&ndash;%&gt;--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="form-actions fluid">--%>
<%--<div class="col-md-offset-3 col-md-9">--%>
<%--<button id="modifyLastloginTimeSubmit{{id}}" type="button" class="btn green">--%>
<%--<i class="fa fa-save"></i> 保存--%>
<%--</button>--%>
<%--<button id="modifyLastloginTimeCancel{{id}}" class="btn default" data-href="">--%>
<%--<i class="fa fa-chevron-left"></i> 返回--%>
<%--</button>--%>
<%--</div>--%>
<%--</div>--%>
<%--</form>--%>
<%--</script>--%>
<script id="modifyValidTimeTmpl" type="text/x-handlebars-template">
    <form id="modifyForm2" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="ids" value="{{ids}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class='control-label col-md-3'>设置大区:</label>
                <div class='col-md-5'>
                    <select class='form-control' id='largeArea2' name="largeArea2">
                        <option value=''>-- 大区类型 --</option>
                        <c:forEach items='${largeAreas}' var='largeArea'><option value='${largeArea.systemValue}'>${largeArea.systemName}</option></c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class='control-label col-md-3'>备注信息:</label>
                <div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='remark3' name="remark3"></textarea></div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="modifySubmit2" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="modifyCancel2" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<script id="modifyImpl" type="text/x-handlebars-template">
    <form id="modifyForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="userId" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">昵称<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="nickname" value="{{nickname}}" class="form-control" placeholder="请输入昵称"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">手机<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="phone" value="{{phone}}" class="form-control" placeholder="请输入手机号"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="modifySubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="modifyCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<script id="modifyPasswordImpl" type="text/x-handlebars-template">
    <form id="modifyPasswordForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="userId" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">密码<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="password" name="newPassword" id="newPassword" value="" class="form-control" placeholder="请输入密码"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">确认密码<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="password" name="newPasswordSure" value="" class="form-control" placeholder="请确认密码"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="modifyPasswordSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="modifyPasswordCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<script id="modifyParentImpl" type="text/x-handlebars-template">
    <form id="modifyParentForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="userId" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">邀请人手机<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="parentPhone" value="" class="form-control" placeholder="请输入邀请人手机号"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2">备注<span class="required"> * </span></label>
                <div class="col-md-5">
                    <textarea name="remark" class="form-control"></textarea>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="modifyParentSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="modifyParentCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<script>
    var grid = new Datatable();

    $(function () {


        $('#dataTable').on('click', '.user-detail', function() {
            var id = $(this).data('id');
            $.ajax({
                url: '${ctx}/user/detail?id=' + id + '&isPure=true',
                dataType: 'html',
                success: function(data) {
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['1080px', '720px'], //宽高
                        content: data
                    });
                }
            });
        });

        var modifyTemplate = Handlebars.compile($('#modifyImpl').html());
        $('#dataTable').on('click', '.modify-info', function () {
            var id = $(this).data('id');
            var nickname = $(this).data('nickname');
            var phone = $(this).data('phone');
            var data = {
                id: id,
                nickname: nickname,
                phone: phone
            };
            var html = modifyTemplate(data);
            var index = layer.open({
                type: 1,
                //skin: 'layui-layer-rim', //加上边框
                area: ['600px', '360px'], //宽高
                content: html
            });

            var $form = $('#modifyForm' + id);
            $form.validate({
                rules: {
                    'nickname': {
                        required: true
                    },
                    'phone': {
                        required: true
                    }
                },
                messages: {}
            });

            $('#modifySubmit' + id).bind('click', function () {
                var result = $form.validate().form();
                if (result) {
                    var url = '${ctx}/user/modify';
                    $.post(url, $form.serialize(), function (data) {
                        if (data.code === 0) {
                            layer.alert('操作成功');
                            layer.close(index);
                            grid.getDataTable().ajax.reload(null, false);
                        } else {
                            layer.alert('操作失败,原因' + data.message);
                        }
                    });
                }
            })

            $('#modifyCancel' + id).bind('click', function () {
                layer.close(index);
            })

        });


        var modifyPasswordTemplate = Handlebars.compile($('#modifyPasswordImpl').html());
        $('#dataTable').on('click', '.user-modify-password', function () {
            var id = $(this).data('id');
            var data = {
                id: id
            };
            var html = modifyPasswordTemplate(data);
            var index = layer.open({
                title: '修改密码',
                type: 1,
                //skin: 'layui-layer-rim', //加上边框
                area: ['600px', '360px'], //宽高
                content: html
            });

            var $form = $('#modifyPasswordForm' + id);
            $form.validate({
                rules: {
                    'newPassword': {
                        required: true
                    },
                    'newPasswordSure': {
                        equalTo : "#newPassword"
                    }
                },
                messages: {}
            });

            $('#modifyPasswordSubmit' + id).bind('click', function () {
                var result = $form.validate().form();
                if (result) {
                    var url = '${ctx}/user/update';
                    $.post(url, $form.serialize(), function (data) {
                        if (data.code === 0) {
                            layer.alert('操作成功');
                            layer.close(index);
                            grid.getDataTable().ajax.reload(null, false);
                        } else {
                            layer.alert('操作失败,原因' + data.message);
                        }
                    });
                }
            })

            $('#modifyPasswordCancel' + id).bind('click', function () {
                layer.close(index);
            })

        });

        var modifyParentTemplate = Handlebars.compile($('#modifyParentImpl').html());
        $('#dataTable').on('click', '.user-modify-parent', function () {
            var id = $(this).data('id');
            var data = {
                id: id
            };
            var html = modifyParentTemplate(data);
            var index = layer.open({
                title: '修改邀请人',
                type: 1,
                //skin: 'layui-layer-rim', //加上边框
                area: ['600px', '360px'], //宽高
                content: html
            });

            var $form = $('#modifyParentForm' + id);
            $form.validate({
                rules: {
                    'parentPhone': {
                        required: true
                    },
                    'remark': {
                        required: true
                    }
                },
                messages: {}
            });

            $('#modifyParentSubmit' + id).bind('click', function () {
                var result = $form.validate().form();
                if (result) {
                    var url = '${ctx}/user/modifyParent';
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

            $('#modifyParentCancel' + id).bind('click', function () {
                layer.close(index);
            })

        });

        var template = Handlebars.compile($('#setBossTmpl').html());
        $('#dataTable').on('click', '.set-boss', function () {
            var id = $(this).data('id');
            var data = {
                id: id
            };
            var html = template(data);
            var index = layer.open({
                type: 1,
                //skin: 'layui-layer-rim', //加上边框
                area: ['600px', '300px'], //宽高
                content: html
            });

            var $form = $('#setBossForm' + id);
            $form.validate({
                rules: {
                    bossName: {
                        required: true
                    }
                },
                messages: {}
            });

            $('#setBossSubmit' + id).bind('click', function () {
                var result = $form.validate().form();
                if (result) {
                    var url = '${ctx}/user/setBoss';
                    $.post(url, $form.serialize(), function (data) {
                        if (data.code === 0) {
                            layer.close(index);
                            grid.getDataTable().ajax.reload(null, false);
                        } else {
                            layer.alert('操作失败,原因' + data.message);
                        }
                    });
                }
            })

            $('#setBossCancel' + id).bind('click', function () {
                layer.close(index);
            })

        });

        var joinTeamTemplate = Handlebars.compile($('#joinTeamTmpl').html());
        $('#dataTable').on('click', '.join-team', function () {
            var id = $(this).data('id');
            var data = {
                id: id
            };
            var html = joinTeamTemplate(data);
            var index = layer.open({
                type: 1,
                //skin: 'layui-layer-rim', //加上边框
                area: ['600px', '300px'], //宽高
                content: html
            });

            var $form = $('#joinTeamForm' + id);
            $form.validate({
                rules: {
                    bossPhone: {
                        required: true
                    }
                },
                messages: {}
            });

            $('#joinTeamSubmit' + id).bind('click', function () {
                var result = $form.validate().form();
                if (result) {
                    var url = '${ctx}/user/boss/joinTeam';
                    $.post(url, $form.serialize(), function (data) {
                        if (data.code === 0) {
                            layer.close(index);
                            grid.getDataTable().ajax.reload(null, false);
                        } else {
                            layer.alert('操作失败,原因' + data.message);
                        }
                    });
                }
            })

            $('#joinTeamCancel' + id).bind('click', function () {
                layer.close(index);
            })

        });

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
                    url: '${ctx}/user', // ajax source
                },
                columns: [
                    {
                        data: 'id',
                        title: '<input type="checkbox" name="checkAll" id="checkAll" value=""/>',
                        render: function (data, type, full) {
                                return '<input type="checkbox" name="id" id="id" value="' + data + '"/>';
                        }
                    },
                    {
                        data: '',
                        title: '用户信息',
                        render: function (data, type, full) {
                            return '<p><img src="' + full.avatarThumbnail + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.nickname + '</p>'
                                    + '<p>手机：' + full.phone + '</p><p>用户等级：' + full.userRankLabel + '</p>';
                        }
                    },
                    {
                        data: 'userType',
                        title: '用户类型'
                    },
                    {
                        data: '',
                        title: '管理层职位',
                        orderable: false,
                        render: function(data, type, full) {
                            var html = '';
                            if (full.isDirector) {
                                html += '<p>董事</p>';
                            }
                            if (full.isHonorDirector) {
                                html += '<p>荣誉董事</p>';
                            }
                            if (full.isShareholder) {
                                html += '<p>股东</p>';
                            }
                            if (full.isBoss) {
                                html += '<p>总经理</p>'
                            }
                            return html;
                        }
                    },
                    {
                        data: '',
                        title: '总经理信息',
                        orderable: false,
                        render: function (data, type, full) {
                            if (full.boss) {
                                return formatUser(full.boss) + '<p>' + full.boss.bossName + '</p>';
                            }
                            return '';
                        }
                    },
                    {
                        data: 'largearea',
                        title: '所属大区',
                        render: function (data, type, full) {
                            return full.largeareaLabel;
                        }
                    },
                    {
                        data: 'registerTime',
                        title: '注册时间',
                        orderable: false
                    },
                    {
                        data: 'lastloginTime',
                        title: '最后一次登录时间',
                        // orderable: false
                    },
                    {
                        data: '',
                        title: '推荐人',
                        orderable: false,
                        render: function (data, type, full) {
                            return formatUser(full.parent);
                        }
                    },
                    {
                        data: 'isFrozen',
                        title: '是否冻结',
                        orderable: false,
                        render: function (data, type, full) {
                            if (full.isFrozen) {
                                return '<label class="badge badge-danger">已冻结</label>';
                            } else {
                                return '';
                            }
                        }
                    },
                    {
                        data: 'isDeleted',
                        title: '是否删除',
                        orderable: false,
                        render: function (data, type, full) {
                            if (full.isDeleted) {
                                return '<label class="badge badge-danger">已删除</label>';
                            } else {
                                return '';
                            }
                        }
                    },
                    {
                        data: 'isToV4',
                        title: '直升特级',
                        orderable: false,
                        render: function (data, type, full) {
                            if (full.isToV4) {
                                return '<label class="badge badge-info">直升特级</label>';
                            } else {
                                return '';
                            }
                        }
                    },
                    {
                        data: 'id',
                        title: '操作',
                        width: '20%',
                        orderable: false,
                        render: function (data, type, full) {
                            var optionHtml = '';
                            if (full.userType != '平台') {
                                optionHtml = '<a class="btn btn-xs default blue-stripe user-detail" href="javascript:;" data-id="' + data + '"><i class="fa fa-search"></i> 查看 </a>';
                                <shiro:hasPermission name="user:edit">
                                optionHtml += '<a class="btn btn-xs default yellow-stripe user-modify-password" href="javascript:;" data-id="' + data + '"><i class="fa fa-edit"></i> 修改密码 </a>';
                                optionHtml += '<a class="btn btn-xs default yellow-stripe modify-info" href="javascript:;" data-id="' + full.id + '" data-nickname="' + full.nickname + '" data-phone="' + full.phone + '"><i class="fa fa-edit"></i> 修改手机昵称 </a>';
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:modifyParent">
                                optionHtml += '<a class="btn btn-xs default green-stripe user-modify-parent" href="javascript:;" data-id="' + data + '"><i class="fa fa-edit"></i> 修改邀请人</a>';
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:addVip">
                                optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="addVip(' + full.id + ')"><i class="fa fa-user"></i> 修改等级 </a>';
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:freeze">
                                if (full.isFrozen) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/unFreeze/' + data + '" data-confirm="您确定要解冻用户[' + full.nickname + ']？"><i class="fa fa-smile-o"></i> 解冻 </a>';
                                } else {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/freeze/' + data + '" data-confirm="您确定要冻结用户[' + full.nickname + ']？"><i class="fa fa-meh-o"></i> 冻结 </a>';
                                }
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:setBoss">
                                if (full.isBoss == null || full.isBoss == false) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe set-boss" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-male"></i> 设置总经理 </a>';
                                    optionHtml += '<a class="btn btn-xs default blue-stripe join-team" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-child"></i> 加入总经理团队 </a>';
                                } else if (full.isBoss) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setBoss?id=' + full.id + '" data-confirm="您确定要取消总经理团队？"><i class="fa fa-edit"></i> 取消总经理 </a>';
                                }
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:setIsDeleted">
                                if (full.isDeleted == null || full.isDeleted == false) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setIsDeleted?id=' + full.id + '" data-confirm="您确定要删除[' + full.nickname + ']吗？"><i class="fa fa-times"></i> 删除（开除） </a>';
                                }
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:setIsPresident">
                                if (full.isPresident) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setIsPresident?id=' + full.id + '" data-confirm="您确定要取消[' + full.nickname + ']的大区总裁吗？"><i class="fa fa-smile-o"></i> 取消大区总裁 </a>';
                                }
                                if (!full.isPresident) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setIsPresident?id=' + full.id + '&flag=0" data-confirm="您确定要设置[' + full.nickname + ']为大区总裁吗？"><i class="fa fa-smile-o"></i> 成为大区总裁 </a>';
                                }
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:setIsToV4">
                                if (full.isToV4 == null || full.isToV4 == false) {
                                    optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setIsToV4?id=' + full.id + '" data-confirm="您确定要设置直升特级[' + full.nickname + ']吗？"><i class="fa fa-male"></i>直升特级</a>';
                                }
                                </shiro:hasPermission>
                                if(full.userRank == 'V4') {
                                    <shiro:hasPermission name="user:setDirector">
                                    if(!full.isDirector) {
                                        optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setDirector?id=' + full.id + '" data-confirm="您确定要升级[' + full.nickname + ']为董事？"><i class="fa fa-smile-o"></i> 成为董事 </a>';
                                    }
                                    if (!full.isHonorDirector) {
                                        optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/isHonorDirector?id=' + full.id + '" data-confirm="您确定升级[' + full.nickname + ']为荣誉董事？"><i class="fa fa-smile-o"></i> 成为荣誉董事 </a>';
                                    }
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="user:setShareholder">
                                    if(!full.isShareholder) {
                                        optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/user/setShareholder?id=' + full.id + '" data-confirm="您确定要升级[' + full.nickname + ']为股东？"><i class="fa fa-smile-o"></i> 成为股东 </a>';
                                    }
                                    </shiro:hasPermission>
                                }
                                <shiro:hasPermission name="user:setShareholder">

                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:edit">
                                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" onclick="editLastLoginTimeAjax(' + full.id + ')"><i class="fa fa-trash-o"></i> 重置登录时间 </a>';
                                </shiro:hasPermission>
                                <shiro:hasPermission name="user:setLargeArea">
                                optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="addLargeArea(' + full.id + ')"><i class="fa fa-user"></i> 设置大区 </a>';
                                </shiro:hasPermission>
                            }
                            return optionHtml;
                        }
                    }]
            }
        });

        <shiro:hasPermission name="user:setLargeArea">
        var modifyTemplate = Handlebars.compile($('#modifyValidTimeTmpl').html());
        $('.table-toolbar').on('click', '#modifyValidTimeBtn',function(){
            var ids = '';
            $("input[name='id']").each(function() {
                var isChecked = $(this).attr("checked");
                if(isChecked == 'checked'){
                    ids += $(this).val() + ',';
                }
            })
            if(ids.length > 0){
                var data = {
                    ids: ids
                };
                var html = modifyTemplate(data);
                var index = layer.open({
                    type: 1,
                    //skin: 'layui-layer-rim', //加上边框
                    area: ['600px', '360px'], //宽高
                    content: html
                });

                $form = $('#modifyForm2');
                $form.validate({
                    rules: {
                        'largeArea2': {
                            required: true
                        }
                    },
                    messages: {}
                });

                $('#modifySubmit2').bind('click', function () {
                    var result = $form.validate().form();
                    if (result) {
                        var url = '${ctx}/user/batchSetLargeArea';
                        $.post(url, $('#modifyForm2').serialize(), function (data) {
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

                $('#modifyCancel2').bind('click', function () {
                    layer.close(index);
                })

            } else {
                layer.alert("请至少选择一条记录！", {icon: 2});
            }
        });
        </shiro:hasPermission>

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

    <shiro:hasPermission name="user:setIsPresident">
    $('.table-toolbar').on('click', '#grantBtn',function(){
        layer.confirm('您确认要批量设置大区总裁吗？', {
            btn: ['设置','取消']
        }, function(){
            var ids = '';
            $("input[name='id']").each(function() {
                var isChecked = $(this).attr("checked");
                if(isChecked == 'checked'){
                    ids += $(this).val() + ',';
                }
            })
            if(ids.length > 0){
                $.ajax({
                    url : '${ctx}/user/batchSetPresident',
                    dataType:"json",
                    type: "post",
                    data : {
                        ids : ids
                    },
                    success: function( result ) {
                        layer.alert(result.message, {icon: 1});
                        grid.getDataTable().ajax.reload(null, false);
                    }
                });

            } else {
                layer.alert("请至少选择一条记录！", {icon: 2});
            }

        }, function(){

        });
    });

    $('.table-toolbar').on('click', '#cancelBtn',function(){
        layer.confirm('您确认要批量取消大区总裁吗？', {
            btn: ['确认','取消']
        }, function(){
            var ids = '';
            $("input[name='id']").each(function() {
                var isChecked = $(this).attr("checked");
                if(isChecked == 'checked'){
                    ids += $(this).val() + ',';
                }
            })
            if(ids.length > 0){
                $.ajax({
                    url : '${ctx}/user/batchCancelPresident',
                    dataType:"json",
                    type: "post",
                    data : {
                        ids : ids
                    },
                    success: function( result ) {
                        layer.alert(result.message, {icon: 1});
                        grid.getDataTable().ajax.reload(null, false);
                    }
                });

            } else {
                layer.alert("请至少选择一条记录！", {icon: 2});
            }

        }, function(){

        });
    });
    </shiro:hasPermission>

    var $addVipDialog;
    function addVip(id) {
        $addVipDialog = $.window({
            content: "<form action='' class='form-horizontal' style='margin-top: 20px;'>" + "<div class='form-body'>" + "<div class='form-group'>"
            + "<label class='control-label col-md-3'>加VIP等级:</label>" + "<div class='col-md-5'>"
            + "<select class='form-control' id='userRank'><option value=''>-- 用户等级 --</option><option value='V1'>VIP服务商</option><option value='V2'>市级服务商</option><option value='V3'>省级服务商</option><option value='V4'>特级服务商</option></select></div></div>"
            + "<div class='form-group'>" + "<label class='control-label col-md-3'>备注信息:</label>"
            + "<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='remark'></textarea></div>" + "</div>" + "</div>"
            + "<div class='form-actions fluid'>" + "<div class='col-md-offset-3 col-md-9'>" + "<button type='button' class='btn green' onclick='submitBtn(" + id + ")'>"
            + "保存</button>" + "<button type='button' class='btn default' onclick='closeBtn()' style='margin-left: 20px;'>" + "取消</button>" + "</div>" + "</div>" + "</form>",
            title: '加VIP',
            width: 420,
            height: 320,
            button: false
        });
    }
    function submitBtn(id) {
        var remark = $('#remark').val();
        var userRank = $('#userRank').find("option:selected").val();
        if (remark == '' || remark == null) {
            alert('请输入备注信息');
            return;
        }
        $.post('${ctx}/user/addVip', {
            id: id,
            userRank: userRank,
            remark: remark
        }, function (result) {
            toastr.success(result.message, '提示信息');
            $addVipDialog.close();
            grid.getDataTable().ajax.reload(null, false);
        })

    }
    function closeBtn() {
        $addVipDialog.close();
    }
    var $addLargeAreaDialog;
    function addLargeArea(id) {
        $addLargeAreaDialog = $.window({
            content: "<form action='' class='form-horizontal' style='margin-top: 20px;'>" + "<div class='form-body'>" + "<div class='form-group'>"
            + "<label class='control-label col-md-3'>设置大区:</label>" + "<div class='col-md-5'>"
            + "<select class='form-control' id='largeArea'><option value=''>-- 大区类型 --</option>" +
            "<c:forEach items='${largeAreas}' var='largeArea'><option value='${largeArea.systemValue}'>${largeArea.systemName}</option></c:forEach></select>"
            + "<div class='form-group'>" + "<label class='control-label col-md-3'>备注信息:</label>"
            + "<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='remark2'></textarea></div>" + "</div>" + "</div>"
            + "<div class='form-actions fluid'>" + "<div class='col-md-offset-3 col-md-9'>" + "<button type='button' class='btn green' onclick='submitLargeAreaBtn(" + id + ")'>"
            + "保存</button>" + "<button type='button' class='btn default' onclick='closeLargeAreaBtn()' style='margin-left: 20px;'>" + "取消</button>" + "</div>" + "</div>" + "</form>",
            title: '设置大区',
            width: 420,
            height: 320,
            button: false
        });
    }
    function submitLargeAreaBtn(id) {
        var remark2 = $('#remark2').val();
        var largeArea = $('#largeArea').find("option:selected").val();
        $.post('${ctx}/user/setLargeArea', {
            id: id,
            largeArea: largeArea,
            remark2: remark2
        }, function (result) {
            toastr.success(result.message, '提示信息');
            $addLargeAreaDialog.close();
            grid.getDataTable().ajax.reload(null, false);
        })

    }
    function closeLargeAreaBtn() {
        $addLargeAreaDialog.close();
    }

    function editLastLoginTimeAjax(id) {
        layer.confirm('您确认要重置该用户的登录时间吗?', {
            btn: ['重置','取消'] //按钮
        }, function(){
            $.post('${ctx}/user/editLastLoginTime/', {id: id}, function (result) {
                grid.getDataTable().ajax.reload(null, false);
            });
            layer.msg('重置成功！');
        }, function(){

        });
    }
</script>
<script type="text/javascript">
    function exportUsers() {
        location.href = '${ctx}/user/export?' + $('#searchForm').serialize();
    }
</script>

<script id="setBossTmpl" type="text/x-handlebars-template">
    <form id="setBossForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-3">总经理团队名称<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="bossName" value="" class="form-control" placeholder="总经理团队名称"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="setBossSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="setBossCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>

<script id="joinTeamTmpl" type="text/x-handlebars-template">
    <form id="joinTeamForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
        <input type="hidden" name="id" value="{{id}}"/>
        <div class="form-body">
            <div class="alert alert-danger display-hide">
                <i class="fa fa-exclamation-circle"></i>
                <button class="close" data-close="alert"></button>
                <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
                <label class="control-label col-md-3">总经理手机号<span class="required"> * </span></label>
                <div class="col-md-5">
                    <input type="text" name="bossPhone" value="" class="form-control" placeholder="总经理手机号"/>
                </div>
            </div>
        </div>
        <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button id="joinTeamSubmit{{id}}" type="button" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
                <button id="joinTeamCancel{{id}}" class="btn default" data-href="">
                    <i class="fa fa-chevron-left"></i> 返回
                </button>
            </div>
        </div>
    </form>
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/user">用户管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-user"></i><span>用户管理 </span>
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
                                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="phoneEQ" class="form-control" placeholder="手机"/>
                            </div>
                            <div class="form-group">
                                <input class="Wdate form-control" type="text" id="beginDate"
                                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="registerTimeGTE" value="" placeholder="注册时间起"/>
                            </div>
                            <div class="form-group">
                                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="registerTimeLT" value="" placeholder="注册时间止"/>
                            </div>
                            <div class="form-group">
                                <input type="text" name="inviterNicknameLK" class="form-control" placeholder="邀请人昵称"/>
                            </div>

                            <div class="form-group">
                                <select name="isFrozenEQ" class="form-control">
                                    <option value="">-- 是否冻结 --</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <select name="isBossEQ" class="form-control">
                                    <option value="">-- 是否总经理 --</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
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

                            <div class="form-group input-inline">
                                <button class="btn blue filter-submit">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>

                            <shiro:hasPermission name="user:setIsPresident">
                                <div class="form-group">
                                    <button type="button" class="btn green" id="grantBtn">
                                        <i class="fa fa-send-o"></i> 批量设置大区总裁
                                    </button>
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn cancel" id="cancelBtn">
                                        <i class="fa fa-times"></i> 批量取消大区总裁
                                    </button>
                                </div>
                            </shiro:hasPermission>

                            <shiro:hasPermission name="user:setLargeArea">
                                <div class="form-group">
                                    <button type="button" class="btn green" id="modifyValidTimeBtn">
                                        <i class="fa fa-send-o"></i> 批量设置大区
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
