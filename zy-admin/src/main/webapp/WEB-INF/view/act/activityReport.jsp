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
                    url: '${ctx}/activity/activityReportList', // ajax source
                },
                columns: [
                    {
                        data: 'id',
                        title: '序号',
                        orderable: false,
                        render: function(data, type, full) {
                            if(type=="display"){
                                $("#notPayNum").text(full.notPayNum);
                                $("#payNum").text(full.payNum);
                                $("#signNum").text(full.signNum);
                            }
                            return full.id;
                        }
                    },
                    {
                        data: 'title',
                        title: '活动名称',
                        orderable: false
                    },
                    {
                        data: 'starDate',
                        title: '活动开始时间',
                        orderable: false
                    },
                    {
                        data: 'address',
                        title: '活动地点',
                        orderable: false
                    },
                    {
                        data: 'nickname',
                        title: '昵称',
                        orderable: false
                    },
                    {
                        data: 'realname',
                        title: '姓名',
                        orderable: false
                    },
                    {
                        data: 'phone',
                        title: '手机号',
                        orderable: false
                    },
                    {
                        data: 'userRankLable',
                                title: '等级',
                            orderable: false
                    },
                    {
                        data: 'parentName',
                        title: '邀请人姓名',
                        orderable: false
                    },
                ]
            }
        });

    });

</script>
<script>
    function changeInitGlagDate(){
        $("#initFalg").val("1");
        $("#notPayNum").text("");
        $("#payNum").text("");
        $("#signNum").text("");
        $("#notPayNum").text(0);
        $("#payNum").text(0);
        $("#signNum").text(0);
    }
    function addDate(flag) {
        $("#activityApplyStatus").val(flag);
        var flag =  $("#initFalg").val();
        $("#notPayNum").text("0");
        $("#payNum").text("0");
        $("#signNum").text("0");
        if(0!=flag){
            $("#filter-submit").click();
        }
    }
    <shiro:hasPermission name="activityReport:export">
    function policyCodeExport() {
        location.href = '${ctx}/activity/export?' + $('#searchForm').serialize();
    }
    </shiro:hasPermission>
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/activity/activityReport">活动报表管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-bar-chart"></i> 活动报表管理
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
                            <input  name="initFalg" id="initFalg" type="hidden" value="0"/>
                            <input  name="activityApplyStatus" id="activityApplyStatus" type="hidden" value="0"/>

                            <div class="form-group">
                                <input type="text" name="activityName" class="form-control" placeholder="活动名称"/>
                            </div>

                            <div class="form-group input-inline">
                                <input type="text" name="activityTime" class="Wdate form-control"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" placeholder="活动时间"/>
                            </div>

                            <div class="form-group">
                                <input type="text" name="activityAddress" class="form-control" placeholder="活动地点"/>
                            </div>

                            <div class="form-group">
                                <input type="text" name="userName" class="form-control" placeholder="姓名"/>
                            </div>

                            <div class="form-group">
                                <input type="text" name="userPhone" class="form-control" placeholder="手机号"/>
                            </div>

                            <div class="form-group">
                                <select name="userLevel" class="form-control">
                                    <option value="">-- 用户等级 --</option>
                                    <c:forEach items="${userRankMap}" var="userRankMap">
                                        <option value="${userRankMap.key}">${userRankMap.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <button class="btn blue filter-submit" onclick="changeInitGlagDate()" id="filter-submit">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                            <shiro:hasPermission name="activityReport:export">
                                <div class="form-group">
                                    <button type="button" class="btn yellow" onClick="policyCodeExport()">
                                        <i class="fa fa-file-excel-o"></i> 导出Excel
                                    </button>
                                </div>
                            </shiro:hasPermission>
                        </form>
                    </div>
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="javascript:void(0)" onclick="addDate('0')" data-toggle="tab" aria-expanded="false"> 未支付 <span class="badge badge-primary" id="notPayNum">0</span></a>
                        </li>
                        <li class="">
                            <a href="javascript:void(0)" onclick="addDate('1')"data-toggle="tab" aria-expanded="false"> 已支付 <span class="badge badge-primary" id="payNum">0</span></a>
                        </li>
                        <li class="">
                            <a href="javascript:void(0)" onclick="addDate('2')" data-toggle="tab" aria-expanded="false"> 已签到 <span class="badge badge-primary" id="signNum">0</span></a>
                        </li>
                    </ul>
                    <table class="table table-striped table-bordered table-hover" id="dataTable">
                    </table>
                </div>
            </div>
            <!-- END ALERTS PORTLET-->
        </div>
    </div>
</div>