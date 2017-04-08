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
          required: true,
          maxlength: 20
        },
        description: {
          required: true,
          maxlength: 50
        },
        url: {
          required: true,
          url: true
        },
        type: {
          required: true
        },
        authority: {
          required: true
        }
      },
      messages: {
      }
    });
    var bbb = ${matter.type};
    if (bbb == 1 ) {
      $(".video").removeClass("hidden");
      $(".image").addClass("hidden");
      $(".word").addClass("hidden");
      $(".audio").addClass("hidden");
      //图片
    } else if (bbb == 2) {
      $(".image").removeClass("hidden");
      $(".video").addClass("hidden");
      $(".word").addClass("hidden");
      $(".audio").addClass("hidden");
      //文档
    } else if (bbb == 3) {
      $(".word").removeClass("hidden");
      $(".video").addClass("hidden");
      $(".image").addClass("hidden");
      $(".audio").addClass("hidden");
      //音频
    } else if (bbb == 4) {
      $(".audio").removeClass("hidden");
      $(".video").addClass("hidden");
      $(".image").addClass("hidden");
      $(".word").addClass("hidden");
    }

    $(".type").on("change", function() {

      $("#matterUrl").val("");
      //类型
      var type = $(".type").val();
      //视频
      if (type == 1 ) {
        $(".video").removeClass("hidden");
        $(".image").addClass("hidden");
        $(".word").addClass("hidden");
        $(".audio").addClass("hidden");
        $(".video .product-video").css("display","block");
        $(".video .product-video").attr("src","${ctx}/image/upload_video.png");
        $(".video .col-md-5 .col-md-3").css("display","none");
        //图片
      } else if (type == 2) {
        $(".image").removeClass("hidden");
        $(".video").addClass("hidden");
        $(".word").addClass("hidden");
        $(".audio").addClass("hidden");
        $(".image .product-image").css("display","block");
        $(".image .product-image").attr("src","${ctx}/image/upload_240_150.jpg");
        $(".image .col-md-5 .col-md-3").css("display","none");
        //文档
      } else if (type == 3) {
        $(".word").removeClass("hidden");
        $(".video").addClass("hidden");
        $(".image").addClass("hidden");
        $(".audio").addClass("hidden");
        $(".word .product-document").css("display","block");
        $(".word .product-document").attr("src","${ctx}/image/upload_word.png");
        $(".word .col-md-5 .col-md-3").css("display","none");
        音频
      } else if (type == 4) {
        $(".audio").removeClass("hidden");
        $(".video").addClass("hidden");
        $(".image").addClass("hidden");
        $(".word").addClass("hidden");
        $(".audio .product-voice").css("display","block");
        $(".audio .product-voice").attr("src","${ctx}/image/upload_audio.png");
        $(".audio .col-md-5 .col-md-3").css("display","none");
      }
    });
    //图片上传
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
          $(uploadBtn).attr('src', response.data+ '@150h_240w_1e_1c.jpg' );
          $('#matterUrl').val(response.data);
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
    //视频上传
    var uploader = new ss.SimpleUpload({
      button: $.makeArray($('.product-video')),
      url: '${ctx}/video/upload',
      name: 'file',
      maxSize: 10240,
      responseType: 'json',
      //阿里云支持的视频文件格式
      //allowedExtensions: ['3GP','AVI','FLV','MP4','M3U8','MPG','ASF','WMV','MKV','MOV','TS','WebM'],
      allowedExtensions: ['MP4'],
      onSubmit: function (filename, extension, uploadBtn, fileSize) {
        $(uploadBtn).data('origin', $(uploadBtn).attr('src'));
        $(uploadBtn).attr('src', 'http://static.thsuan.com/image/loading_image.gif');
      },
      onComplete: function (filename, response, uploadBtn, fileSize) {
        if (response.code == 0) {
          $(uploadBtn).attr('src', response.data );
          $('#matterUrl').val(response.data);

          $('.product-video').css("display","none");
          $('#videohuan').css("display","block");
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
        layer.alert('视频大小超过10MB限制');
      },
      onExtError: function (filename, extension) {
        layer.alert('视频文件格式错误, 仅限*.MP4');
      }
    });

    //文档上传
    var uploader = new ss.SimpleUpload({
      button: $.makeArray($('.product-document')),
      url: '${ctx}/document/upload',
      name: 'file',
      maxSize: 10240,
      responseType: 'json',
      //rar|zip|pdf|pdfx|txt|csv|xls|xlsx|doc|docx|RAR|ZIP|PDF|PDFX|TXT|CSV|XLS|XLSX|DOC|DOCX
      //allowedExtensions: ['rar','pdf','pdfx','txt','csv','xls','xlsx','doc','docx'],
      allowedExtensions: ['pdf','pdfx'],
      onSubmit: function (filename, extension, uploadBtn, fileSize) {
        $(uploadBtn).data('origin', $(uploadBtn).attr('src'));
        $(uploadBtn).attr('src', 'http://static.thsuan.com/image/loading_image.gif');
      },
      onComplete: function (filename, response, uploadBtn, fileSize) {
        if (response.code == 0) {
          $(uploadBtn).attr('src', response.data +'@150h_240w_1e_1c.jpg' );
          $('#matterUrl').val(response.data);

          $('.product-document').css("display","none");
          $('#documenthuan').css("display","block");
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
        layer.alert('文档大小超过10MB限制');
      },
      onExtError: function (filename, extension) {
        layer.alert('文档文件格式错误, 仅限 *.pdf, *.pdfx ');
      }
    });
    //音频上传
    var uploader = new ss.SimpleUpload({
      button: $.makeArray($('.product-voice')),
      url: '${ctx}/audio/upload',
      name: 'file',
      maxSize: 10240,
      responseType: 'json',
      allowedExtensions: ['mp3'],
      onSubmit: function (filename, extension, uploadBtn, fileSize) {
        $(uploadBtn).data('origin', $(uploadBtn).attr('src'));
        $(uploadBtn).attr('src', 'http://static.thsuan.com/image/loading_image.gif');
      },
      onComplete: function (filename, response, uploadBtn, fileSize) {

        if (response.code == 0) {
          $(uploadBtn).attr('src', response.data );
         // console.alert(response.data);
          $('#matterUrl').val(response.data);

          $('.product-voice').css("display","none");
          $('#audiohuan').css("display","block");
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
        layer.alert('音频大小超过10MB限制');
      },
      onExtError: function (filename, extension) {
        layer.alert('音频文件格式错误, 仅限*.mp3');
      }
    });
  });

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/matter">资源管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-arrow-down"></i> 编辑资源
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/matter/update" class="form-horizontal" method="post">
          <input type="hidden" name="id" value="${matter.id}"/>
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">标题<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="title" id="title" value="${matter.title}"/>
                <input type="hidden"  name="id" id="matterId" value="${matter.id}"/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">简述<span class="required"> * </span></label>
              <div class="col-md-5">
                <textarea type="text" name="description" class="form-control">${matter.description}</textarea>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">权限<span class="required"> * </span></label>
              <div class="col-md-5">
                <select class="form-control" name="authority">
                  <option value="">-- 请选择 --</option>
                  <option value="0"<c:if test="${matter.authority == 0}"> selected="selected"</c:if>>无权限</option>
                  <option value="1"<c:if test="${matter.authority == 1}"> selected="selected"</c:if>>VIP</option>
                  <option value="2"<c:if test="${matter.authority == 2}"> selected="selected"</c:if>>市级服务商</option>
                  <option value="3"<c:if test="${matter.authority == 3}"> selected="selected"</c:if>>省级服务商</option>
                  <option value="4"<c:if test="${matter.authority == 4}"> selected="selected"</c:if>>特级服务商</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">文件类型<span class="required"> * </span></label>
              <div class="col-md-5">
                <select class="form-control  type" name="type">
                  <option value="">-- 请选择 --</option>
                  <option value="1"<c:if test="${matter.type == 1}"> selected="selected"</c:if>>视频</option>
                  <option value="2"<c:if test="${matter.type == 2}"> selected="selected"</c:if>>图片</option>
                  <%--<option value="3"<c:if test="${matter.type == 3}"> selected="selected"</c:if>>文档</option>--%>
                  <option value="4"<c:if test="${matter.type == 4}"> selected="selected"</c:if>>音频</option>
                </select>
              </div>
            </div>
            <div class="form-group  video  hidden">
              <label class="control-label col-md-3">视频<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-video bd" src="${ctx}/image/upload_video.png">
                <img class="control-label col-md-3" src="${ctx}/image/huancun.png" id="videohuan" style="display: none;width: 200px;">
              </div>
            </div>
            <div class="form-group  image  hidden">
              <label class="control-label col-md-3">图片<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-image bd"
                     src="<c:if test='${not empty matter.url && matter.type == 2 }'>${matter.url}</c:if>
                     <c:if test='${empty matter.url || matter.type != 2}'>${ctx}/image/upload_240_150.jpg</c:if>">
              </div>
            </div>
            <div class="form-group  word  hidden">
              <label class="control-label col-md-3">文档<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-document bd" src="${ctx}/image/upload_word.png">
                <img class="control-label col-md-3" src="${ctx}/image/huancun.png" id="documenthuan" style="display: none;width: 200px;">
              </div>
            </div>
            <div class="form-group  audio  hidden">
              <label class="control-label col-md-3">音频<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-voice bd" src="${ctx}/image/upload_audio.png">
                <img class="control-label col-md-3" src="${ctx}/image/huancun.png" id="audiohuan" style="display: none;width: 200px;">
              </div>
            </div>
            <input id="matterUrl" type="hidden" name="url" value="${matter.url}"/>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/matter">
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
