<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->

<script>

    /**
     * 选择课程
     */
    var selLesson=null;
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
        if(value.indexOf(",")>0){
            layer.msg("只能选择一个课程，如需多个请多次选择");
        }else{
            $("#lessonameLable").val(name);
            $("#lseeonId").val(value);
            layer.close(selLesson);
        }
    }
    function submitclick() {

         var flage = $('#form').validate({
                rules: {
                    'phone': {
                        required: true
                    }
                }
            });
         if(flage.form()){
             var lseeonId = $("#lseeonId").val();
             if (lseeonId==null||lseeonId==""){
                 layer.msg("请选择课程");
             }else {
                 $.ajax({
                     url: '${ctx}/lesson/user/findUserInfo',
                     data: {
                         phone: $("#phone").val(),
                         lseeonId:lseeonId
                     },
                     dataType: 'JSON',
                     type: 'post',
                     success: function (result) {
                         if (result.code == 0) {
                             layer.confirm('请确认信息是否正确<br/>'+'姓名： '+result.data.name+'<br/>手机号： '+result.data.phone, {
                                 btn: ['确认','取消'] //按钮
                             }, function(){
                                 //调父类的 方法关闭页面
                                 $("#form").submit();
                                 parent.closeaddLessonUser();
                                 layer.closeAll();
                             }, function(){
                             });
                         } else  {
                             layer.msg(result.message);
                         }
                     }
                 });

             }
         }

    }
    function returnParent() {
        if($("#phone").val()==null||$("#phone").val()==""){
            parent.closeaddLessonUser();
        }else{
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
                    <i class="icon-wallet"></i> 用户课程管理
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form id="form" action="" data-action="${ctx}/lesson/user/create" class="form-horizontal" method="post" >
                    <div class="form-body">
                        <div class="alert alert-danger display-hide">
                            <i class="fa fa-exclamation-circle"></i>
                            <button class="close" data-close="alert"></button>
                            <span class="form-errors">您填写的信息有误，请检查。</span>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">用户手机号：<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <input type="number" class="form-control" name="phone" id="phone" value="${phone}" ${phone!=null?'readonly':''}  required placeholder="请填入用户手机号"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">课程:</label>
                            <div class="col-md-5">
                                <input type="text" class="form-control"  id="lessonameLable"  readonly placeholder="请选择课程"/>
                                <input type="hidden" name="lseeonId" id="lseeonId" >
                            </div>
                            <div class="btn" style="width:100px;height: 34px;border:1px solid #e5e5e5;background: #35aa47;color: #fff;" onclick="selectLesson()">请选择 </div>
                        </div>

                    </div>
                    <div class="form-actions fluid">
                        <div class="col-md-offset-3 col-md-9">
                            <button type="button" class="btn green" onclick="submitclick()">
                                <i class="fa fa-save"></i> 保存
                            </button>
                            <button class="btn default"  onclick="returnParent()">
                                <i class="fa fa-chevron-left"></i> 返回
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
