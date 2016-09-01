<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<%@ include file="/WEB-INF/view/include/editor.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>
  #img {
    cursor: pointer;
  }
</style>
<script>

  var ue;
  var content = '';

  $(function () {

    var area = new areaInit('province', 'city', 'district');

    ue = UE.getEditor('editor', {
      serverUrl: '${ctx}/editor',
      catchRemoteImageEnable: false,
      textarea: 'detail',
      enableAutoSave: false,
      contextMenu: [],
      saveInterval: 3600000,
      toolbars: [[
        'bold', 'italic', 'underline', '|',
        'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
        'insertorderedlist', 'insertunorderedlist', 'link', '|',
        'simpleupload', 'insertimage'
      ]],
      'insertorderedlist': {'num': '1,2,3...'},
      'insertunorderedlist': {'disc': ''},
      elementPathEnabled: false,     //去掉元素路径
      pasteplain: true,   //是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
      iframeCssUrl: '${ctx}/css/editor/default.css'
    });

    ue.addListener("ready", function () {
      ue.setContent(content);
    });

    $('#form').validate({
      rules: {
        title: {
          required: true,
          maxlength: 60
        },
        areaId: {
          required: true
        },
        address: {
          required: true,
          maxlength: 100
        },
        image: {
          required: true
        },
        detail: {
          required: true,
        },
        startTime: {
          required: true,
        },
        endTime: {
          required: true
        },
        applyDeadline: {
          required: true
        }
      },
      messages: {},
      submitHandler: function (form) {
        var content = ue.getContent();
        if (!content) {
          layer.alert('请填写活动详情')
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
    <li><a href="javascript:;" data-href="${ctx}/activity">活动管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 创建活动
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/activity/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">标题<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="title" value=""/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">图片<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-image bd" src="${ctx}/image/upload_240_150.jpg">
                <input type="hidden" name="image" value=""/>
                <p class="help-block">图片尺寸 750*450</p>
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
              <label class="control-label col-md-3">地址<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="address" value=""/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">开始时间<span class="required"> * </span></label>
              <div class="col-md-5">
                <div class="input-icon input-medium right">
                  <i class="fa fa-calendar"></i>
                  <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                         name="startTime" value="" placeholder="开始时间"/>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">结束时间<span class="required"> * </span></label>
              <div class="col-md-5">
                <div class="input-icon input-medium right">
                  <i class="fa fa-calendar"></i>
                  <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                         name="endTime" value="" placeholder="结束时间"/>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">报名截止时间<span class="required"> * </span></label>
              <div class="col-md-5">
                <div class="input-icon input-medium right">
                  <i class="fa fa-calendar"></i>
                  <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                         name="applyDeadline" value="" placeholder="报名截止时间"/>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">活动详情<span class="required"> * </span></label>
              <div class="col-md-5">
                <div class="input-icon right">
                  <script id="editor" type="text/plain" style="width:840px; height:500px;"></script>
                </div>
              </div>
            </div>

          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/activity">
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
