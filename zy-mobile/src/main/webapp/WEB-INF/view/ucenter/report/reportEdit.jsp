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

<title>修改检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script type="text/javascript">
  $(function() {
    $('.image-multi .image-item').imageupload();
    
    $('.image-multi .image-add').imageupload({
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
        $this.siblings('.image-item').eq(imageItems.length).imageupload();
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
        'date' : {
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
<body class="">
  <header class="header">
    <h1>修改检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/report/edit" class="valid-form" method="post">
      <input type="hidden" name="id" value="${report.id}"/>
      <div class="list-group mt-10">
        <div class="list-title">填写客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div class="list-text">
            <input type="text" name="realname" class="form-input" value="${report.realname}" placeholder="填写客户姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="0"<c:if test="${report.gender eq '男'}"> selected</c:if>>男</option>
              <option value="1"<c:if test="${report.gender eq '女'}"> selected</c:if>>女</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="age">年龄</label>
          <div class="list-text">
            <input type="number" name="age" class="form-input" value="${report.age}" placeholder="填写客户年龄">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="date">检测时间</label>
          <div class="list-text">
            <input type="text" id="date" name="date" class="form-input" value="${report.dateLabel}" placeholder="填写检测时间 2001-01-01" onfocus="this.type='date'">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">检测结果</label>
          <div class="list-text form-select">
            <select name="reportResult">
              <option value="">请选择</option>
              <option value="阴性"<c:if test="${report.reportResult eq '阴性'}"> selected</c:if>>阴性</option>
              <option value="弱阳性"<c:if test="${report.reportResult eq '弱阳性'}"> selected</c:if>>弱阳性</option>
              <option value="阳性"<c:if test="${report.reportResult eq '阳性'}"> selected</c:if>>阳性</option>
              <option value="干扰色"<c:if test="${report.reportResult eq '干扰色'}"> selected</c:if>>干扰色</option>
            </select>
          </div>
        </div>
      </div>

      <div class="list-group">
        <div class="list-title">您至少需要上传1张检测图片</div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <div class="image-item">
              <input type="hidden" name="image1" value="${report.image1}">
              <img src="${report.image1Thumbnail}">
              <input type="file">
            </div>
            <c:if test="${not empty report.image2}">
            <div class="image-item">
              <input type="hidden" name="image2" value="${report.image2}">
              <img src="${report.image2Thumbnail}">
              <input type="file">
            </div>
            </c:if>
            <c:if test="${not empty report.image3}">
            <div class="image-item">
              <input type="hidden" name="image3" value="${report.image3}">
              <img src="${report.image3Thumbnail}">
              <input type="file">
            </div>
            </c:if>
            <c:if test="${not empty report.image4}">
            <div class="image-item">
              <input type="hidden" name="image4" value="${report.image4}">
              <img src="${report.image4Thumbnail}">
              <input type="file">
            </div>
            </c:if>
            <c:if test="${not empty report.image5}">
            <div class="image-item">
              <input type="hidden" name="image5" value="${report.image5}">
              <img src="${report.image5Thumbnail}">
              <input type="file">
            </div>
            </c:if>
            <c:if test="${not empty report.image6}">
            <div class="image-item">
              <input type="hidden" name="image6" value="${report.image6}">
              <img src="${report.image6Thumbnail}">
              <input type="file">
            </div>
            </c:if>
            <c:if test="${empty report.image6}">
            <div class="image-add" data-limit="6">
              <input type="hidden" name="image" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
            </c:if>
          </div>
        </div>

      </div>
      <div class="list-group">
        <div class="list-title">填写产品使用心得</div>
        <div class="list-item">
          <div class="list-text">
            <textarea name="text" class="form-input" rows="3" placeholder="填写产品使用心得">${report.text}</textarea>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>

    </form>
  </article>

</body>
</html>
