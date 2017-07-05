<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<%@ include file="/WEB-INF/view/include/editor.jsp"%>
<style>
    #img {
        cursor: pointer;
    }
</style>
<script type="text/javascript">
    //编辑器
    var ue;
    articleContent = '${tour.content}';
    $(function() {
        ue = UE.getEditor('editor', {
            serverUrl: '${ctx}/editor',
            catchRemoteImageEnable: false,
            textarea: 'content',
            enableAutoSave: false,
            contextMenu:[],
            saveInterval: 3600000,
            toolbars:  [[
                'fullscreen', 'source', '|', 'undo', 'redo', '|',
                'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
                'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
                'directionalityltr', 'directionalityrtl', 'indent', '|',
                'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
                'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
                'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertvideo', 'music', 'attachment', 'map', 'gmap', 'insertframe', 'insertcode', 'webapp', 'pagebreak', 'template', 'background', '|',
                'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
                'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
                'print', 'preview', 'searchreplace', 'help', 'drafts'
            ]],
            'insertorderedlist':{'num':'1,2,3...' },
            'insertunorderedlist':{'disc' : ''},
            elementPathEnabled : false,     //去掉元素路径
            pasteplain: true,   //是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
            initialFrameWidth: 750,
            iframeCssUrl: '${ctx}/css/editor/default.css'
        });
        ue.addListener("ready", function () {
            ue.setContent(articleContent);
        });
        $("#createForm").validate({
            rules : {
                'title' : {
                    required : true,
                    maxlength : 100
                },
                'image' : {
                    required : true,
                },
                'brief' : {
                    required : true,
                },
                'days' : {
                    required : true,
                    number: true,
                }
            },
            messages: {
                days: {
                    required: '请输入天数',
                    number: '只能输入数字',
                }
            },
            submitHandler :
                    function(form){
                        var content = ue.getContent();
                        if(!content) {
                            layer.alert('请填写旅游详情');
                            return false;
                        }
                        $(form).find(':submit').prop('disabled', true);
                        Layout.postForm(form);
                    }
        });

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

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/tour">旅游管理</a></li>
    </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN VALIDATION STATES-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open"></i><span> 创建旅游信息 </span>
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form id="createForm" action="" data-action="${ctx}/tour/create" class="form-horizontal" method="post">
                    <div class="form-body">
                        <div class="alert alert-danger display-hide">
                            <i class="fa fa-exclamation-circle"></i>
                            <button class="close" data-close="alert"></button>
                            <span class="form-errors">您填写的信息有误，请检查。</span>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">标题<span class="required"> * </span>
                            </label>
                            <div class="col-md-4">
                                <input type="text" name="title" id="title" class="form-control"
                                       value="${tour.title}" placeholder="请输入标题" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">旅游活动天数<span class="required"> * </span>
                            </label>
                            <div class="col-md-4">
                                <input type="text" name="days" id="days" class="form-control"
                                       value="${tour.days}"  placeholder="请输天数" />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">主图<span class="required"> * </span></label>
                            <div class="col-md-5">
                                <img data-target="image" class="product-image bd" src="<c:if test='${not empty tour.image}'>${tour.image}</c:if><c:if test='${empty article.image}'>${ctx}/image/upload_240_150.jpg</c:if>">
                                <input type="hidden" name="image" value="${tour.image}"/>
                                <p class="help-block">图片尺寸 750*450</p>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">旅游信息摘要<span class="required"> * </span></label>
                            <div class="col-md-4">
                                <textarea name="brief" id="brief" cols="30" rows="5" class="form-control">${tour.brief}</textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3">旅游信息详情<span class="required"> * </span>
                            </label>
                            <div class="col-md-4">
                                <div><script id="editor" type="text/plain" style="width:840px; height:500px;"></script></div>
                            </div>
                        </div>

                    </div>

                    <div class="form-actions fluid">
                        <div class="col-md-offset-3 col-md-9">
                            <button type="submit" class="btn green">保存</button>
                            <button class="btn default" data-href="${ctx}/tour">
                                <i class="fa fa-arrow-left"></i> 返回
                            </button>
                        </div>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>
