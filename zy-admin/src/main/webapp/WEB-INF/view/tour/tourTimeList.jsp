<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script>
    var flage = false;
    var grid = new Datatable();
    var urlPc = '${rulPc}';
    $(function () {
        grid.init({
            src: $('#dataTable'),
            onSuccess: function (grid) {
            },
            onError: function (grid) {
            },
            dataTable: {
                //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>",
                lengthMenu : [ [ 10, 20, 50, 100 ], [ 10, 20, 50, 100 ] ],// change per page values here
                pageLength: 20, // default record count per page
                order: [
                ], // set first column as a default sort by desc
                ajax: {
                    url: '${ctx}/tour/findTourTime', // ajax source
                },
                columns: [
                    {
                        data: 'createby',
                        title: '创建人',
                        orderable: false
                    },
                    {
                        data: 'beginTime',
                        title: '开始时间',
                        render: function (data, type, full) {
                            return full.begintimeLible;
                        }
                    },
                    {
                        data: 'endTime',
                        title: '结束时间',
                        render: function (data, type, full) {
                            return full.endtimeLible;
                        }
                    },
                    {
                        data: 'fee',
                        title: '费用',
                        orderable: true
                    },
                    {
                        data: 'createdDate',
                        title: '发布时间',
                        orderable: true,
                        render: function (data, type, full) {
                            return full.createdTimeLible;
                        }

                    },
                    {
                        data: 'starAddress',
                        title: '集合地点',
                        orderable: false,
                        render: function (data, type, full) {
                            return '<p>' + full.province + '-' + full.city + '-' + full.district + '</p>';
                        }
                    },
                    {
                        data: 'isreleased',
                        title: '是否发布',
                        orderable: false,
                        render: function (data, type, full) {
                            if (data) {
                                flage=true;
                                return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 已发布 </span>';
                            }
                            return '';
                        }
                    },
                    {
                        data: 'id',
                        title: '操作',
                        orderable: false,
                        render: function (data, type, full) {
                            var optionHtml = '';
                            <shiro:hasPermission name="tour:edit">
                           /* optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/tour/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';*/
                            if (full.isreleased==1) {
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
    <shiro:hasPermission name="article:edit">
    function release(id) {
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
        releaseAjax(id, 0);
    }
    function release(id) {
        releaseAjax(id, 1);
    }
    function releaseAjax(id, isreleased) {
         $.post('${ctx}/tour/release', {tourTimeId: id, isreleased: isreleased}, function (result) {
          grid.getDataTable().ajax.reload(null, false);
          flage = false;
         });
     }
    function deleteAjax(id) {
        layer.confirm('您确认删除此旅游路线信息嘛?', {
            btn: ['删除','取消'] //按钮
        }, function(){
            $.post('${ctx}/tour/release', {tourTimeId: id,delFlage:1}, function (result) {
                layer.msg(result.message);
                grid.getDataTable().ajax.reload(null, false);
                flage = false;
            });

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

    function addTourTime(id) {
        $.ajax({
            url: '${ctx}/tour/createTourTime?tourId='+ id,
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
    }
    
    function refreshData() {
        $(".filter-submit").click();
    }
    function updateTour (tourId) {
        if(flage){
            layer.confirm('确认启用本条旅游信息？', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.post('${ctx}/tour/ajaxupdate', {id:tourId}, function (result) {
                    layer.msg(result.message);
                    $("#tourhref").click();
                });

            }, function(){
            });
        }else{
            layer.msg("没有发团时间或者没有启用的发团时间，请确认后再提交！");
        }
    }
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a id="tourhref" href="javascript:;" data-href="${ctx}/tour">旅游信息管理</a><i class="fa fa-angle-right"></i></li>
        <li><a  href="javascript:;">旅游线路管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open"></i> <span style="color: red">  ${tour.title}</span>
                </div>
            <shiro:hasPermission name="tour:edit">

                <div class="actions">
                    <c:if test="${flage ==1}">
                        <a class="btn btn-circle green" href="javascript:;"  onclick="updateTour('${tour.id}')">
                            <i class="fa fa-check font-green"></i>发布旅游信息
                        </a>
                        &nbsp;  &nbsp;
                 </c:if>
                    <a class="btn btn-circle green" href="javascript:;"  onclick="addTourTime('${tour.id}')">
                        <i class="fa fa-plus"></i> 新增行程
                    </a>

                </div>
            </shiro:hasPermission>
            </div>
            <div class="portlet-body clearfix">
                <div class="table-container">
                    <div class="table-toolbar">
                        <form class="filter-form form-inline">
                            <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                            <input id="_direction" name="direction" type="hidden" value=""/>
                            <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                            <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
                            <input id="tourId" name="tourId" type="hidden" value="${tour.id}"/>
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="begintime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="begintime" value="" placeholder="开始时间"/>
                            </div>
                            <div class="form-group input-inline">
                                <input class="Wdate form-control" type="text" id="endtime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="endtime" value="" placeholder="结束时间"/>
                            </div>
                            <div class="form-group">
                                <select name="isreleased" class="form-control">
                                    <option value="">-- 是否发布 --</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <button class="btn blue filter-submit">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
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
