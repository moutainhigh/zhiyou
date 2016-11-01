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

    var area = new areaInit('province', 'city', 'district', '${report.areaId}');
    
    $('#form').validate({
      rules: {
        'realname': {
          required: true
        },
        'gender': {
          required: true
        },
        'age': {
          required: true
        },
        'areaId': {
          required: true
        },
        'jobId': {
          required: true
        },
        'phone': {
          required: true,
          phone: true
        },
        'reportedDate': {
          required: true
        },
        'reportResult': {
          required: true
        },
        'image': {
          required: true
        },
        'times' : {
          required: true
        },
        'text': {
          required: true
        }
      },
      messages: {
      }
    });

    var uploader = new ss.SimpleUpload({
      button: $.makeArray($('.product-image')),
      url: '${ctx}/image/upload2?width=240&height=150',
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
          $(uploadBtn).attr('src', response.data.imageThumbnail);
          $('#' + $(uploadBtn).data('target')).val(response.data.image);
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
    <li><a href="javascript:;" data-href="${ctx}/report">检测报告</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-volume-1"></i> 创建检测报告
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/report/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">客户姓名<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="realname" id="realname" value="${report.realname}"/>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">性别<span class="required"> * </span></label>
              <div class="col-md-5">
                <select class="form-control" name="gender">
                  <option value="">-- 性别 --</option>
                  <option value="0"<c:if test="${report.gender == '男'}"> selected="selected"</c:if>>男</option>
                  <option value="1"<c:if test="${report.gender == '女'}"> selected="selected"</c:if>>女</option>
                </select>
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
              <label class="control-label col-md-3">职业<span class="required"> * </span></label>
              <div class="col-md-5">
                <select name="jobId" class="form-control">
                  <option value="">请选择</option>
                  <c:forEach items="${jobs}" var="job">
                    <option value="${job.id}"<c:if test="${job.id == report.jobId}"> selected="selected"</c:if>>${job.jobName}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">年龄<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="number" name="age" class="form-control" value="${report.age}" placeholder="填写客户年龄">
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3" for="phone">手机号<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" name="phone" class="form-control" value="${report.phone}" placeholder="填写客户手机号">
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3" for="phone">第几次检测<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="number" name="times" class="form-control" value="${report.times}" placeholder="第几次检测">
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">检测日期<span class="required"> * </span></label>
              <div class="col-md-5">
                <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true})"
                       name="reportedDate" value="${report.reportedDateLabel}" placeholder="检测日期"/>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">检测结果<span class="required"> * </span></label>
              <div class="col-md-5">
                <select name="reportResult" class="form-control">
                  <option value="">-- 检测结果 --</option>
                  <c:forEach items="${reportResults}" var="reportResult">
                    <option value="${reportResult}"<c:if test="${reportResult == report.reportResult}"> selected="selected"</c:if>>${reportResult}</option>
                  </c:forEach>
                </select>
                <p />
                <img data-target="image1" class="product-image bd" src="<c:if test="${not empty report.imageThumbnails[0]}">${report.imageThumbnails[0] }</c:if>
                <c:if test="${empty report.imageThumbnails[0]}">${ctx}/image/upload_240_150.jpg</c:if>">
                <input type="hidden" name="image" id="image1" value="<c:if test='${not empty report.images[0]}'>${report.images[0]}</c:if>" />
                <p />
                <img data-target="image2" class="product-image bd" src="<c:if test="${not empty report.imageThumbnails[1]}">${report.imageThumbnails[1] }</c:if>
                <c:if test="${empty report.imageThumbnails[1]}">${ctx}/image/upload_240_150.jpg</c:if>">
                <input type="hidden" name="image" id="image2" value="<c:if test='${not empty report.images[1]}'>${report.images[1]}</c:if>" />
                
                <img data-target="image3" class="product-image bd" src="<c:if test="${not empty report.imageThumbnails[2]}">${report.imageThumbnails[2] }</c:if>
                <c:if test="${empty report.imageThumbnails[2]}">${ctx}/image/upload_240_150.jpg</c:if>">
                <input type="hidden" name="image" id="image3" value="<c:if test='${not empty report.images[2]}'>${report.images[2]}</c:if>" />  
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3" for="phone">填写产品使用心得<span class="required"> * </span></label>
              <div class="col-md-5">
                 <textarea name="text" class="form-control" rows="3" placeholder="填写产品使用心得">${report.text}</textarea>
              </div>
            </div>
            
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/report">
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
