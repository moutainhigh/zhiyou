<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<%@ include file="/WEB-INF/view/include/editor.jsp"%>

<script type="text/javascript">
    $(function() {

        $('#form').validate({
            rules : {
                isTransfers : {
                    required: true
                },
                carNumber : {
                    required : true
                },
                planTime : {
                    required : true
                }
//                isJoin : {
//                    required : true
//                },
//                amount : {
//                    required : true,
//                    number : true
//
//                }
            },
            messages: {
                amount: {
                    required: '请输入消费金额',
                    number : '只能输入数字'
                },

            },
        });


        //图片上传
        var uploader = new ss.SimpleUpload({
            button: $.makeArray($('.product-image')),
            url: '${ctx}/image/upload',
            name: 'file',
            maxSize: 4096,
            responseType: 'json',
            allowedExtensions: ['jpg', 'jpeg', 'png', 'gif', 'webp'],
            onSubmit: function (filename, extension, uploadBtn, fileSize) {
                $(uploadBtn).data('origin', $(uploadBtn).attr('src'));
                $(uploadBtn).attr('src', 'http://static.thsuan.com/image/loading_image.gif');
            },
            onComplete: function (filename, response, uploadBtn, fileSize) {
                if (response.code == 0) {
                    $(uploadBtn).attr('src', response.data + '@150h_240w_1e_1c.jpg');
                    $('input[name="' + $(uploadBtn).data('target') + '"]').val(response.data);
                } else {
                    $(uploadBtn).attr('src', $(uploadBtn).data('origin'));
                    layer.alert('上传失败' + response.message);
                }
            },
            onError: function (filename, errorType, status, statusText, response, uploadBtn, fileSize) {
                $(uploadBtn).attr('src', $(uploadBtn).data('origin'));
                layer.alert('上传失败' + errorType);
            },
            onSizeError: function (filename, fileSize) {
                layer.alert('图片大小超过4MB限制');
            },
            onExtError: function (filename, extension) {
                layer.alert('图片文件格式错误, 仅限*.jpg, *.jpeg, *.png, *.gif, *.webp');
            }
        });
    });
    $("#auditStatus").attr("disabled",true);
    $("#houseType").attr("disabled",true);
    $("#isAddBed").attr("disabled",true);
    $("#isEffect").attr("disabled",true);
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/tourJoinUser">参游旅客信息管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN VALIDATION STATES-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-speech"></i><span> 编辑参游旅客信息</span>
                </div>
                <div class="tools">
                    <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form id="form" action="" data-action="${ctx}/tourJoinUser/editTourJoinUser" class="form-horizontal" method="post">
                    <div class="form-body">
                        <input type="hidden" name="id" value="${tourUserAdminVo.id}"/>
                        <input type="hidden" name="sequenceId" value="${tourUserAdminVo.sequenceId}"/>
                        <input type="hidden" name="reportId" value="${tourUserAdminVo.reportId}"/>
                        <div class="form-group">
                            <label class="control-label col-md-3">用户<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;" readonly="true" class="form-control" id="userName" name="userName" value="${tourUserAdminVo.userName}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">身份证号<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;" readonly="true" class="form-control" id="idCardNumber" name="idCardNumber" value="${tourUserAdminVo.idCardNumber}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">年龄<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;" readonly="true" class="form-control" id="age" name="age" value="${tourUserAdminVo.age}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">性别<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;" readonly="true" class="form-control" id="gender" name="gender" value="${tourUserAdminVo.gender}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">用户电话<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;" readonly="true" class="form-control" id="userPhone" name="userPhone" value="${tourUserAdminVo.userPhone}" placeholder="请输入手机号" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">线路<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" readonly="true" style="display: block; width: 60%;" class="form-control" id="tourTitle" name="tourTitle" value="${tourUserAdminVo.tourTitle}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">出游时间<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" readonly="true" style="display: block; width: 60%;" class="form-control" id="tourTime" name="tourTime" value="${tourUserAdminVo.tourTime}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">推荐人<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" readonly="true" style="display: block; width: 40%;" class="form-control" id="parentName" name="parentName" value="${tourUserAdminVo.parentName}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">推荐人电话<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" readonly="true" style="display: block; width: 40%;" class="form-control" id="parentPhone" name="parentPhone" value="${tourUserAdminVo.parentPhone}" placeholder="请输入手机号" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">审核状态<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <select style="display: block; width: 40%;" readonly="true" id="auditStatus" class="form-control pull-left">
                                    <option value="1" <c:if test="${tourUserAdminVo.auditStatus == 1}"> selected="selected"</c:if>>审核中</option>
                                    <option value="2" <c:if test="${tourUserAdminVo.auditStatus == 2}"> selected="selected"</c:if>>待补充</option>
                                    <option value="3" <c:if test="${tourUserAdminVo.auditStatus == 3}"> selected="selected"</c:if>>已生效</option>
                                    <option value="4" <c:if test="${tourUserAdminVo.auditStatus == 4}"> selected="selected"</c:if>>已完成</option>
                                    <option value="5" <c:if test="${tourUserAdminVo.auditStatus == 5}"> selected="selected"</c:if>>审核失败</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">房型需求<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <select style="display: block; width: 40%;" readonly="true" class="form-control pull-left" id="houseType" name="houseType">
                                    <option value="1" <c:if test="${tourUserAdminVo.houseType == 1}"> selected="selected"</c:if>>标准间</option>
                                    <option value="2" <c:if test="${tourUserAdminVo.houseType == 2}"> selected="selected"</c:if>>三人间</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">是否加床<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <select style="display: block; width: 40%;" readonly="true" class="form-control pull-left" id="isAddBed" name="isAddBed">
                                    <option value="0" <c:if test="${tourUserAdminVo.isAddBed == 0}"> selected="selected"</c:if>>否</option>
                                    <option value="1" <c:if test="${tourUserAdminVo.isAddBed == 1}"> selected="selected"</c:if>>是</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">是否接机/车<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <select style="display: block; width: 40%;" class="form-control pull-left" id="isTransfers" name="isTransfers">
                                    <option value="0" <c:if test="${tourUserAdminVo.isTransfers == 0}"> selected="selected"</c:if>>否</option>
                                    <option value="1" <c:if test="${tourUserAdminVo.isTransfers == 1}"> selected="selected"</c:if>>是</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">班号<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;"  class="form-control" id="carNumber" name="carNumber" value="${tourUserAdminVo.carNumber}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">票务照片<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <img data-target="image" class="product-image bd" src="<c:if test='${not empty tourUserAdminVo.imageThumbnail}'>${tourUserAdminVo.imageThumbnail}</c:if><c:if test='${empty tourUserAdminVo.imageThumbnail}'>${ctx}/image/upload_240_150.jpg</c:if>">
                                <input type="hidden" name="image" value="${tourUserAdminVo.imageThumbnail}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">预计到达时间<span class="required"> * </span>
                            </label>
                            <div class="col-md-4">
                                <input  style="display: block; width: 50%;" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="planTime" value="${tourUserAdminVo.planTimeLabel}" placeholder="预计到达时间" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">用户备注<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <textarea type="text" readonly="true" style="display: block; width: 60%;" class="form-control">${tourUserAdminVo.userRemark}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">审核备注<span class="required"> * </span>
                            </label>
                            <div class="col-md-5">
                                <textarea type="text" readonly="true" style="display: block; width: 60%;" class="form-control">${tourUserAdminVo.revieweRemark}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">是否参游</label>
                            <div class="col-md-5">
                                <select style="display: block; width: 40%;" class="form-control pull-left" id="isJoin" name="isJoin">
                                    <option value="0" <c:if test="${tourUserAdminVo.isJoin == 0}"> selected="selected"</c:if>>否</option>
                                    <option value="1" <c:if test="${tourUserAdminVo.isJoin == 1}"> selected="selected"</c:if>>是</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">消费金额(元)
                            </label>
                            <div class="col-md-5">
                                <input type="text" style="display: block; width: 40%;" class="form-control" id="amount" name="amount" value="${tourUserAdminVo.amount}"/>
                            </div>
                        </div>
                        <%--<div class="form-group">--%>
                            <%--<label class="control-label col-md-3">是否有效<span class="required"> * </span></label>--%>
                            <%--<div class="col-md-5">--%>
                                <%--<select style="display: block; width: 40%;" readonly="true" class="form-control pull-left" id="isEffect" name="isJoin">--%>
                                    <%--<option value="0" <c:if test="${tourUserAdminVo.isEffect == 0}"> selected="selected"</c:if>>否</option>--%>
                                    <%--<option value="1" <c:if test="${tourUserAdminVo.isEffect == 1}"> selected="selected"</c:if>>是</option>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    </div>
                    <div class="form-actions fluid">
                        <div class="col-md-offset-3 col-md-9">
                            <button type="submit" class="btn green">
                                <i class="fa fa-save"></i> 保存
                            </button>
                            <button class="btn default" data-href="${ctx}/tourJoinUser">
                                <i class="fa fa-chevron-left"></i> 返回
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

