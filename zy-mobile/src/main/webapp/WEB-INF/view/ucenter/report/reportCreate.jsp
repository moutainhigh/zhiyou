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
<%@ include file="/WEB-INF/view/include/fileupload.jsp"%>
<script type="text/javascript">
  $(function() {
    $('.image-add').Fileupload({
      width : 120,
      height : 120,
      url : '${ctx}/image/upload',
      maxFileSize : '4MB',
      progress : function(){
        var $this = $(this);
        $this.find('.state').removeClass('state-add').addClass('state-loading');
      },
      success : function(result) {
        var $this = $(this);
        $this.find('.state').removeClass('state-loading').addClass('state-add');
        var limit = $this.attr('data-limit');
        var images = $this.siblings('.image-item');
        var inputHidden = $this.children('input:hidden');
        var inputName = inputHidden.val('').attr('name');
        inputName = inputName.replace((images.length + 1), '');
        var imageItem = '<div class="image-item">' + '<input type="hidden" name="' + inputName + (images.length + 1) + '" value="' + result + '">'
            + '<img src="' + result + '@200w_200h_1e_1c_jpg">' + '<input type="file">' + '</div>';
        $(imageItem).insertBefore($this);
        $this.children('input:hidden').attr('name', inputName + (images.length + 2));
        if (limit && limit <= images.length + 1) {
          $this.remove();
        }
        $this.siblings('.image-item').eq(images.length).Fileupload({
          width : 120,
          height : 120,
          url : '${ctx}/image/upload',
          maxFileSize : '4MB'
        });
      }
    });

    //验证
    $(".valid-form").validate({
      rules : {
        'name' : {
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
        'image2' : {
          required : true
        },
        'image3' : {
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
    <h1>提交试用报告</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/report/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-title">填写客户资料</div>
        <div class="list-item">
          <label class="list-label" for="name">姓名</label>
          <div class="list-text">
            <input type="text" name="name" class="form-input" value="" placeholder="填写客户姓名">
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
          <label class="list-label">检测时间</label>
          <div class="list-text">
            <input type="text" name="date" class="form-input" value="" placeholder="填写检测时间" onfocus="this.type='date'">
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
        <div class="list-title">您至少需要上传3张检测图片</div>
        <div class="list-item">
          <div class="list-text list-image">
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
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="提 交">
      </div>

    </form>
  </article>

</body>
</html>
