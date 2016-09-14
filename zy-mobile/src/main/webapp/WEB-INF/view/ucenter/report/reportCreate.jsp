<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>上传检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script src="${stccdn}/js/area.js"></script>
<script type="text/javascript">
  $(function() {
    var area = new areaInit('province', 'city', 'district');
    
    $('.image-multi .image-add').imageupload({
      width : 100,
      height : 100,
      url : '${ctx}/image/upload',
      maxFileSize : '4MB',
      progress : function() {
      },
      success : function(result) {
        var $this = $(this);
        var limit = $this.attr('data-limit');
        var imageItems = $this.siblings('.image-item');
        var inputHidden = $this.children('input:hidden');
        var inputName = inputHidden.val('').attr('name');
        inputName = inputName.replace((imageItems.length + 1), '');
        var image = result.image;
        var imageThumbnail = result.imageThumbnail;
        var imageItem = '<div class="image-item">' + '<input type="hidden" name="' + inputName + (imageItems.length + 1) + '" value="' + image + '">'
            + '<img src="' + imageThumbnail + '">' + '<input type="file">' + '</div>';
        $(imageItem).insertBefore($this);
        $this.children('input:hidden').attr('name', inputName + (imageItems.length + 2));
        if (limit && limit <= imageItems.length + 1) {
          $this.remove();
        }
        $this.siblings('.image-item').eq(imageItems.length).imageupload({
          width : 100,
          height : 100,
          url : '${ctx}/image/upload',
          maxFileSize : '4MB'
        });
      }
    });

    //验证
    $(".valid-form").validate({
      rules : {
        'realname' : {
          required : true
        },
        'gender' : {
          required : true
        },
        'age' : {
          required : true
        },
        'phone' : {
          required : true
        },
        'areaId' : {
          required : true
        },
        'jobId' : {
          required : true
        },
        'reportResult' : {
          required : true
        },
        'image1' : {
          required : true
        },
        'text' : {
          required : true
        }
      }
    });

  });
</script>
</head>
<body>
  <header class="header">
    <h1>上传检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/report/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-title">填写客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div class="list-text">
            <input type="text" name="realname" class="form-input" value="" placeholder="填写客户姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="0">男</option>
              <option value="1">女</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="age">年龄</label>
          <div class="list-text">
            <input type="number" name="age" class="form-input" value="" placeholder="填写客户年龄">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div class="list-text">
            <input type="number" name="phone" class="form-input" value="" placeholder="填写客户手机号">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">地区</label>
          <div class="list-text">
            <div class="form-select pb-10 bd-b">
              <select name="" id="province">
                <option value="">选择省</option>
              </select>
            </div>
            <div class="form-select mt-10 pb-10 bd-b">
              <select name="" id="city">
                <option value="">选择市</option>
              </select>
            </div>
            <div class="form-select mt-10">
              <select name="areaId" id="district">
                <option value="">选择区</option>
              </select>
            </div>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">职业</label>
          <div class="list-text form-select">
            <select name="jobId">
              <option value="">请选择</option>
              <c:forEach items="${jobs}" var="job">
                <option value="${job.id}">${job.jobName}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">检测结果</label>
          <div class="list-text form-select">
            <select name="reportResult">
              <option value="">请选择</option>
              <option value="阴性">阴性</option>
              <option value="弱阳性">弱阳性</option>
              <option value="阳性">阳性</option>
              <option value="干扰色">干扰色</option>
            </select>
          </div>
        </div>
      </div>

      <div class="list-group">
        <div class="list-title">您至少需要上传1张检测图片</div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <div class="image-add" data-limit="6">
              <input type="hidden" name="image1" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>

      </div>
      <div class="list-group">
        <div class="list-title">填写产品使用心得</div>
        <div class="list-item">
          <div class="list-text">
            <textarea name="text" class="form-input" rows="3" placeholder="填写产品使用心得"></textarea>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <c:if test="${userRank == 'V0'}">
        <input class="btn default btn-block round-2" type="button" disabled="disabled" value="成为代理才能提交检测报告">
        </c:if>
        <c:if test="${userRank != 'V0'}">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
        </c:if>
      </div>

    </form>
  </article>

</body>
</html>
