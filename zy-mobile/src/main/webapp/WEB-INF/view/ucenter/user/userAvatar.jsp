<%@ page language="java" pageEncoding="UTF-8"%>
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
<meta name="keywords" content="微信分销" />
<meta name="description" content="上传头像 " />

<title>上传头像</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">

var mutex = (function() { //互斥上传类型
	var m = {};
	return {
		set : function(t, xhr) {
			m.xhr && m.xhr.abort();
			m.type = t;
			xhr && (m.xhr = xhr);
		},
		is : function(t) {
			return m.type === t;
		},
		get : function() {
			return m.type;
		}
	}
})();

$(function() {
	var $avatar = $('.user-avatar .avatar img');
	var setAvator = function(src) {
		$avatar.attr('src', src);
	};
	
	$('#upload').on('change', function(e) {
		if (!this.value)
			return;
		mutex.set('local');
		$avatar.attr('src', '${stccdn}/image/loading_circle.gif');
		$('#uploadForm').submit();
	});
	$('#uploadtarget').on('load', function() {
		if (!mutex.is('local'))
			return;
		var txt = $('#uploadtarget').contents().find('body').text();
		txt = $.parseJSON(txt);
		if (!txt.data) {
			$.messageError('上传的图片无效，请重新上传');
			return;
		}
		setAvator(txt.data);
	});
});
</script>
<style type="text/css">
.user-avatar {
  padding: 30px;
}
.user-avatar .avatar {
  margin: 0 auto;
  display: block; width: 240px; height: 240px;
}
.user-avatar .avatar img {
  width: 100%; height: 100%; z-index: 90;
  background: #fff url('${stccdn}/image/avatar_default.jpg') no-repeat center;
}
.user-avatar .btns {
  position: relative; margin: 30px auto;
  width: 240px;
}
.user-avatar .input-file {
  position: absolute; top: 0; left: 0;
  width: 100%; height: 35px; opacity: 0; z-index: 1;
}
</style>
</head>
<body>

  <header class="header">
    <h1>修改头像</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article class="user-avatar">
    <form id="uploadForm" target="uploadtarget" action="${ctx}/u/avatar" method="post" enctype="multipart/form-data">
      <figure class="avatar">
        <img src="${userAvatarSmall}">
      </figure>
      <div class="btns">
        <input type="button" class="btn btn-block orange round-2 mt-10" value="更改头像">
        <input type="file" class="input-file" id="upload" name="file" value="选择图片" accept="image/*" />
      </div>
    </form>
    <iframe src="about:blank" id="uploadtarget" name="uploadtarget" frameborder="0" style="display: none;"></iframe>
  </article>
   
</body>

</html>
