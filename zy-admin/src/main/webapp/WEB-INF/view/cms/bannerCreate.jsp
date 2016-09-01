<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>
  #img {
    cursor: pointer;
  }
</style>
<script>
  $(function () {

    $('#form').validate({
      rules: {
        title: {
          required: true
        },
        image: {
          required: true
        },
        url: {
          required: true,
          url: true
        },
        bannerPosition: {
          required: true
        },
        orderNumber: {
          required: true,
          digits: true
        }
      },
      messages: {
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
    <li><a href="javascript:;" data-href="${ctx}/banner">Banner管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-directions"></i> 创建Banner
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/banner/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">标题<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="title" id="title" value=""/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">位置<span class="required"> * </span></label>
              <div class="col-md-5">
                <select class="form-control" name="bannerPosition">
                  <option value="">-- 请选择 --</option>
                  <c:forEach items="${bannerPositions}" var="bannerPosition">
                    <option value="${bannerPosition}">${bannerPosition} (图片尺寸 长${bannerPosition.width}像素 * 宽${bannerPosition.height}像素)</option>
                  </c:forEach>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">是否在新窗口打开<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="hidden" name="_isOpenBlank" value="false"/>
                <label class="checkbox-inline"><input type="checkbox" name="isOpenBlank" value="true"/> 是否在新窗口打开</label>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">图片<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-image bd" src="${ctx}/image/upload_240_150.jpg">
                <input type="hidden" name="image" value=""/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">图片指向链接<span class="required"> * </span>
              </label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="url" value=""/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">顺序<span class="required"> * </span>
              </label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="orderNumber" value="10"/>
              </div>
            </div>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/banner">
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
