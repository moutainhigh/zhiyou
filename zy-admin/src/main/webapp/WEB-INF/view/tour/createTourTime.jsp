<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>
    #img {
        cursor: pointer;
    }
</style>
<script>
    $(function (){
        var area = new areaInit('province', 'city', 'district');
    });
 function addsumbit() {
    var falg = $('#form').validate({
        rules: {
            begintime: {
                required: true,
            },
            endtime: {
                required: true,
            },
            province: {
                required: true,
            },
            city: {
                required: true,
            },
            areaId: {
                required: true,
            },
            address: {
                required: true,
            },
            fee: {
                required: true,
                number: true,
            }
        },
         messages: {
             fee: {
                required: '请输入费用',
                number : '只能输入数字',
            }
        }
    });
     if(falg.form()){
     $.ajax({
         url : '${ctx}/tour/ajaxCreateTourTime',
         dataType:"json",
         type: "post",
         async: false,
         data: $('#form').serialize(),
         success: function( result ) {
             if (result.code == 0) {
                 layer.alert("添加成功", {icon: 1});
                 parent.layer.closeAll();
                 parent.refreshData();
                 /*    grid.getDataTable().ajax.reload(null, false);*/
             } else {
                 layer.alert("添加失败", {icon: 0});
             }
         }
     });
     }

 }

    function thisclose() {
        alert("ewwew");
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
                    <i class="icon-social-dropbox"></i> 创建旅游信息
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form id="form" action="" data-action="${ctx}/tour/createTourTime" class="form-horizontal" method="post">
                    <div class="form-body">
                        <input type="hidden" name ="tourId" value="${tourId}"/>
                        <div class="alert alert-danger display-hide">
                            <i class="fa fa-exclamation-circle"></i>
                            <button class="close" data-close="alert"></button>
                            <span class="form-errors">您填写的信息有误，请检查。</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">开始时间<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <div class="input-icon input-medium right">
                                    <i class="fa fa-calendar"></i>
                                    <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                           name="begintime" value="" placeholder="开始时间"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">结束时间<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <div class="input-icon input-medium right">
                                    <i class="fa fa-calendar"></i>
                                    <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                           name="endtime" value="" placeholder="结束时间"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">费用<span class="required"></span></label>
                            <div class="col-md-5">
                                <input type="text" class="form-control" name="fee" value="0"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">是否有效<span class="required"> </span></label>
                            <div class="col-md-5">
                                <select style="display: block; width: 40%;" class="form-control pull-left" id="isreleased" name="isreleased">
                                    <option value="1"> --是-- </option>
                                    <option value="0"> --否-- </option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">省市区<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <select style="display: block; width: 32%;" class="form-control pull-left" id="province" name="province">
                                    <option value="">-- 请选择省 --</option>
                                </select>
                                <select style="display: block;width: 32%; margin-left: 2%" class="form-control pull-left" id="city" name="city">
                                    <option value="">-- 请选择市 --</option>
                                </select>
                                <select style="display: block;width: 32%; margin-left: 2%" class="form-control pull-left" id="district" name="areaId">
                                    <option value="">-- 请选择区 --</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">详细地址<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <input type="text" class="form-control pull-left" name="address" value=""/>
                            </div>
                        </div>
                    </div>
                    <div class="form-actions fluid">
                        <div class="col-md-offset-3 col-md-9">
                            <button type="submit" class="btn green" onclick="addsumbit()" >
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
