<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>

</style>
<script>
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN VALIDATION STATES-->
        <div class="portlet light bordered" style="height: 800px;">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-user"></i> 查看用户
                </div>
            </div>
            <div class="portlet-body form">
                <form class="form-horizontal">
                    <div class="form-body">

                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-md-3">姓名:</label>
                                    <div class="col-md-9">
                                        <p class="form-control-static">${user.userInfo.realname}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-md-3">身份证号:</label>
                                    <div class="col-md-9">
                                        <p class="form-control-static">${user.userInfo.idCardNumber}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-md-3">性别:</label>
                                    <div class="col-md-9">
                                        <p class="form-control-static">${user.userInfo.gender}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-md-3">生日:</label>
                                    <div class="col-md-9">
                                        <p class="form-control-static">${user.userInfo.birthdayLabel}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-md-3">户籍城市:</label>
                                    <div class="col-md-9">
                                        <p class="form-control-static">${user.userInfo.province} - ${user.userInfo.city} - ${user.userInfo.district}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                </form>
            </div>
        </div>
        <!-- END VALIDATION STATES-->
    </div>
</div>
