<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->

<script>


    /**
     * 选择前期课程
     */
    var selLesson;
    function  selectLesson() {
        $.ajax({
            url: '${ctx}/lesson/selectLesson',
            dataType: 'html',
            success: function(data) {
                selLesson = layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['700px', '400px'], //宽高
                    content: data
                });
            }
        })
    }
    function addSelLesson(name,value) {
        var lessonId = $("#lessonId").val();
        if(value.indexOf(lessonId)>0||value==lessonId){
            layer.msg("前期课程 不能包含当前课程");
        }else {
            $("#lessonameLable").val(name);
            $("#parentAllId").val(value);
            layer.close(selLesson);
        }
    }
    function submitclick() {

         var flage = $('#form').validate({
                rules: {
                    'title': {
                        required: true
                    }
                }
            });
         if(flage.form()){
             //调父类的 方法关闭页面
             $("#form").submit();
             parent.closeupdateLesson();
         }

    }
    
</script>


<div class="row" style="width: 100%;height: 100%">
    <div class="col-md-12">
        <!-- BEGIN VALIDATION STATES-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-wallet"></i> 课程管理
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form id="form" action="" data-action="${ctx}/lesson/updateEdit" class="form-horizontal" method="post">
                    <input type="hidden" value="${lessionVo.id}" name ="id" id="lessonId" >
                    <div class="form-body">
                        <div class="alert alert-danger display-hide">
                            <i class="fa fa-exclamation-circle"></i>
                            <button class="close" data-close="alert"></button>
                            <span class="form-errors">您填写的信息有误，请检查。</span>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">课程名称<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <input type="text" class="form-control" name="title" id="title" value="${lessionVo.title}" placeholder="请填入课程名称" readonly/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">前期课程</label>
                            <div class="col-md-5">
                                <input type="text" class="form-control"  id="lessonameLable" value="${lessionVo.lessonAllnameLable}"  readonly placeholder="请选择前期课程"/>
                                <input type="hidden" name="parentAllId" id="parentAllId"  value="${lessionVo.parentAllId}">
                            </div>
                            <div class="btn" style="width:100px;height: 34px;border:1px solid #e5e5e5;background: #35aa47;color: #fff;" onclick="selectLesson()">请选择 </div>
                        </div>

                    </div>
                    <div class="form-actions fluid">
                        <div class="col-md-offset-3 col-md-9">
                            <button type="button" class="btn green" onclick="submitclick()">
                                <i class="fa fa-save"></i> 保存
                            </button>
                            <button class="btn default" data-href="${ctx}/policyCode">
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
