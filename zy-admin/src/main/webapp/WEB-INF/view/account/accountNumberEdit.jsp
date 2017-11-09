<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>
    #img {
        cursor: pointer;
    }
</style>
<script>
    function addsumbit() {
        var falg = $('#form').validate({
            rules: {
                oldPhone: {
                    required: true,
                },
                newPhone: {
                    required: true,
                }
            }
        });
        if(falg.form()){
            var myreg = /^[1][1,2,3,4,5,6,7,8,9,0][0-9]{9}$/;
            var oldPhone = $("#oldPhone").val();
            var newPhone = $("#newPhone").val();
            if (!myreg.test(oldPhone)||!myreg.test(newPhone)) {
                layer.alert("请正确填写手机号", {icon: 0});
            }else {
                if($("#id").val()==null||$("#id").val()==""){
                    $.ajax({
                        url : '${ctx}/accountnumber/addAccountNumber',
                        dataType:"json",
                        type: "post",
                        async: false,
                        data: {
                            newPhone: newPhone,
                            oldPhone: oldPhone
                        },
                        success: function( result ) {
                            if (result.code == 0) {
                                layer.alert("添加成功", {icon: 1});
                                parent.layer.closeAll();
                                parent.refreshData();
                            } else {
                                layer.alert("添加失败", {icon: 0});
                            }
                        }
                    });
                }else{//修改
                    $.ajax({
                        url : '${ctx}/accountnumber/update',
                        dataType:"json",
                        type: "post",
                        async: false,
                        data: {
                            id: $("#id").val(),
                            phone: newPhone
                        },
                        success: function( result ) {
                            if (result.code == 0) {
                                layer.alert("修改成功", {icon: 1});
                                parent.layer.closeAll();
                                parent.refreshData();
                            } else {
                                layer.alert("修改失败", {icon: 0});
                            }
                        }
                    });
                }

            }
        }
        return false;
    }

    function thisclose() {
        parent.layer.closeAll();
        return;
    }
</script>
<!-- END PAGE HEADER-->
<div class="row" style="width: 100%;">
    <div class="col-md-12">
        <!-- BEGIN VALIDATION STATES-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-social-dropbox"></i> 创建账号迁移信息
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form id="form" action="" class="form-horizontal" method="post"  onsubmit="return false;">
                    <input type="hidden" name ="id" value="${accountNumber.id}" id="id"/>
                    <div class="form-body">
                        <div class="alert alert-danger display-hide">
                            <i class="fa fa-exclamation-circle"></i>
                            <button class="close" data-close="alert"></button>
                            <span class="form-errors">您填写的信息有误，请检查。</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">原账号手机号<span class="required"></span></label>
                            <div class="col-md-5">
                                <input type="text" class="form-control" name="oldPhone" id="oldPhone" value="${accountNumber.oldPhone}" ${accountNumber.newPhone==null?"":"readonly"}/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">新账号手机号<span class="required"></span></label>
                            <div class="col-md-5">
                                <input type="text" class="form-control" name="newPhone" id="newPhone" value="${accountNumber.newPhone}" />
                            </div>
                        </div>
                    </div>
                    <div class="form-actions fluid">
                        <div class="col-md-offset-3 col-md-9">
                            <button  class="btn green" onclick="addsumbit()" >
                                <i class="fa fa-save"></i> 保存
                            </button>
                            <button class="btn default" onclick="thisclose()">
                                <i class="fa fa-chevron-left"></i>关闭
                            </button>
                        </div>
                    </div>
                </form>
                <!-- END FORM-->
            </div>
        </div>
        <!-- END VALIDATION STATES-->
    </div>
</div>
