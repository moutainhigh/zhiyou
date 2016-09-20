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

<title>实名认证</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script type="text/javascript">
  $(function() {

    $('.image-single').imageupload({
      width : 120,
      height : 75,
    });

    //注册验证
    $(".valid-form").validate({
      rules : {
        'realname' : {
          required : true
        },
        'idCardNumber' : {
          required : true
        },
        'image1' : {
          required : true
        },
        'image2' : {
          required : true
        }
      }
    });

    $('.image-view').click(function() {
      var url = $(this).attr('data-src');
      var title = $(this).attr('data-title');
      $.imageview({
        url : url,
        title : title
      });
    });
  });
</script>
</head>
<body>
  <header class="header">
    <h1>实名认证</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article>
    <form action="${ctx}/u/appearance/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-item">
          <label class="list-label">姓名</label>
          <div class="list-text">
            <input type="text" id="realname" name="realname" class="form-input" value="" placeholder="填写真实姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">身份证号</label>
          <div class="list-text">
            <input type="text" id="idCardNumber" name="idCardNumber" class="form-input" value="" placeholder="填写身份证号">
          </div>
        </div>
      </div>
    
      <div class="list-group">
        <div class="list-title">请上传身份证照片</div>
        <div class="list-item">
          <label class="list-label">正面照</label>
          <div class="list-text image-upload">
            <div class="image-item image-single ">
              <input type="hidden" name="image1" id="image1" value="">
              <img src="${stccdn}/image/upload_240_150.png">
              <input type="file">
            </div>
          </div>
          <div class="list-unit">
            <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_1.jpg" data-title="身份证正面"><i class="fa fa-question-circle-o"></i> 示意图</a>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">反面照</label>
          <div class="list-text image-upload">
            <div class="image-item image-single">
              <input type="hidden" name="image2" id="image2" value="">
              <img src="${stccdn}/image/upload_240_150.png">
              <input type="file">
            </div>
          </div>
          <div class="list-unit">
            <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_2.jpg" data-title="身份证反面"><i class="fa fa-question-circle-o"></i> 示意图</a>
          </div>
        </div>
      </div>
    
      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提交">
      </div>
    </form>
    
  </article>
  
</body>
</html>
