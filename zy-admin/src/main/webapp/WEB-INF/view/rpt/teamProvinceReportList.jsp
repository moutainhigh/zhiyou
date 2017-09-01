<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
    var grid = new Datatable();

    $(function () {

        grid.init({
            src: $('#dataTable'),
            onSuccess: function (grid) {
            },
            onError: function (grid) {
            },
            dataTable: {
                //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>",
//                lengthMenu: [
//                    [10, 20, 50, 100, -1],
//                    [10, 20, 50, 100, '全部']
//                ],

                order: [], // set first column as a default sort by desc
                ajax: {
                    url: "${ctx}/report/teamProvinceReport",
                },
                columns: [
                    {
                        data: 'province',
                        title: '省份',
                        orderable: false,
                    },
                    {
                        data: 'v4Number',
                        title: '特级服务商人数',
                        orderable: true,
                    },
                    {
                        data: 'newv4',
                        title: '新增特级人数',
                        orderable: false,
                    },
                    {
                        data: 'v3Number',
                        title: '省级服务商人数',
                        orderable: true,
                    },
                    {
                        data: 'newv3',
                        title: '新增省级人数',
                        orderable: false,
                    },
                    {
                        data: 'v4ActiveNumber',
                        title: '特级服务商活跃人数',
                        orderable: true,
                    },
                    {
                        data: 'v4ActiveRank',
                        title: '特级服务商活跃度排名',
                        orderable: true,
                    },
                    {
                        data: 'v4ActiveRate',
                        title: '特级服务商活跃度',
                        orderable: true,
                        render: function (data, type, full) {
                            var f = Math.round(data*100)/100;
                            var s = f.toString();
                            var rs = s.indexOf('.');
                            if (rs < 0) {
                                rs = s.length;
                                s += '.';
                            }
                            while (s.length <= rs + 2) {
                                s += '0';
                            }
                            return s +"%";
                        }
                    }
                ]
            }
        });

    });
 function filtersubmit() {
     var year =  $('#yearEQ').val();
     var month =  $('#monthEQ').val();
     if ((year!=""||year!=null)||(month!=""||month!=null)){
         if (!((year!=""&&year!=null)&&(month!=""&&month!=null))){
             if(year==""||year==null){
                 layer.alert('请选择年份');
                 return false;
             }
             if(month==""||month==null){
                 layer.alert('请选择月份');
                 return false;
             }
         }else {
             $('#team').text("省份服务商活跃度("+year+"/"+month+")");
         }
     }

 }

    <shiro:hasPermission name="teamProvinceReport:export">
    function reportExport() {
        location.href = '${ctx}/report/teamProvinceReport/export?' + $('#searchForm').serialize();
    }
    </shiro:hasPermission>

    function  changemonth(obj) {
        var thisYear = ${year};
        var thisMonth = ${month};
        var sel = $('#monthEQ');
        var opt="";
        if(obj!=thisYear){
            for(var i =1;i<=12;i++){
                if(i==thisMonth){
                    opt = opt + "<option value='" + i + "' selected >" + i + "月</option>";
                }else {
                    opt = opt + "<option value='" + i + "' >" + i + "月</option>";
                }

            }
        }else{
            for(var i =1;i<=thisMonth;i++){
                if(i==thisMonth){
                    opt = opt + "<option value='" + i + "' selected >" + i + "月</option>";
                }else {
                    opt = opt + "<option value='" + i + "' >" + i + "月</option>";
                }

            }
        }
        sel.html("");
        sel.html(opt)
    }

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/report/teamProvinceReport" id="team">省份服务商活跃度(${year}/${month})</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-body clearfix">
                <div class="table-container">
                    <div class="table-toolbar">
                        <form class="filter-form form-inline" id="searchForm">
                            <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                            <input id="_direction" name="direction" type="hidden" value=""/>
                            <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                            <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

                            <div class="form-group">
                                <select name="yearEQ" class="form-control" id="yearEQ" onchange="changemonth(this.value)">
                                    <option value="">-- 选择年份--</option>
                                    <c:forEach var="i" begin="2015" end="${year}">
                                        <option value="${i}" ${i == year?'selected':''} >${i}年</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <select name="monthEQ" class="form-control" id="monthEQ">
                                    <option value="">-- 选择月份--</option>
                                    <c:forEach var="i" begin="1" end="${month}">
                                        <option value="${i}" ${i == month?'selected':''} >${i}月</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <select name="provinceEQ" class="form-control">
                                    <option value="">-- 选择省份--</option>
                                    <c:forEach items="${areas}" var="area" >
                                      <option value="${area.name}"> ${area.name} </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group input-inline">
                                <button class="btn blue filter-submit" onclick="filtersubmit()">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                            <shiro:hasPermission name="teamProvinceReport:export">
                                <div class="form-group">
                                    <button type="button" class="btn yellow" onClick="reportExport()">
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
