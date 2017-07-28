<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script>

    $(function () {

        var grid = new Datatable1();

        grid.init({
            src: $('#dataTable1'),
            onSuccess: function (grid) {
                // execute some code after table records loaded
            },
            onError: function (grid) {
                // execute some code on network or other general error
            },
            dataTable: {
                //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>",
                lengthMenu:[[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
                pageLength: 20, // default record count per page
                order: [], // set first column as a default sort by desc
                ajax: {
                    url: '${ctx}/lesson', // ajax source
                },
                columns: [
                   {
                        data: 'id',
                        title: '<input type="checkbox" name="checkAll" id="checkAll" value=""/>',
                        render: function (data, type, full) {
                                return '<input type="checkbox" name="id" id="id" value="' + full.id +';'+full.title +'"/>';

                        }
                    },
                    {
                        data: 'title',
                        title: '课程名称',
                        orderable: false
                    },
                    {
                        data: 'createLable',
                        title: '创建人',
                        orderable: false
                    },
                    {
                        data: 'createDate',
                        title: '创建时间',
                        orderable: true,
                        render: function (data, type, full) {
                            return full.createDateLable;
                        }
                    }
                ]
            }
        });

    });
    $('#dataTable1').on('click', '#checkAll', function(){
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

    /**
     * 提价选择的课程
     */
    function sellesson() {
        var values ="";
        var names="";
        $('input[name="id"]:checked').each(function(){
            var arry =$(this).val().split(";");
            values=values+arry[0]+",";
            names=names+arry[1]+";";
        });
        if(values==""){
            layer.msg("请选择前期课程");
            return;
        }
        names = names.substring(0,names.length-1);
        values = values.substring(0,values.length-1);
        parent.addSelLesson(names,values);
    }
</script>


<div class="row" style="width: 100%;">
    <div class="col-md-12">
        <!-- BEGIN ALERTS PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-wallet"></i> 课程管理
                </div>
                <div class="actions">
                    <a class="btn btn-circle green"  href="javascript:;" onclick="sellesson()">
                        <i class="fa fa-plus"></i> 确定
                    </a>
                </div>
            </div>
            <div class="portlet-body clearfix">
                <div class="table-container">
                    <div class="table-toolbar">
                        <form id="searchForm" class="filter-form form-inline">
                            <input id="_orderBy1" name="orderBy" type="hidden" value=""/> <input id="_direction1" name="direction" type="hidden" value=""/>
                            <input id="_pageNumber1" name="pageNumber" type="hidden" value="0"/> <input id="_pageSize1" name="pageSize" type="hidden" value="20"/>

                            <div class="form-group">
                                <input type="text" name="titleLK" class="form-control" placeholder="课程名称"/>
                            </div>

                            <div class="form-group">
                                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                       name="createDateGTE" value="" placeholder="创建时间"/>
                            </div>
                            <div class="form-group">
                                <button class="btn blue filter-submit">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                        </form>
                    </div>

                    <table class="table table-striped table-bordered table-hover" id="dataTable1">
                    </table>
                </div>

            </div>
        </div>
    </div>
</div>