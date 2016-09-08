<%@ page language="java" pageEncoding="UTF-8"%>
<script src="${stccdn}/extend/imageupload/imageupload.wx.js"></script>
<link rel="stylesheet" href="${stccdn}/extend/imageupload/imageupload.css" />
<script>
  $.fn.imageupload.setDefaults({
    url : '${ctx}/image/upload',
    width : 100,
    height : 100,
    retain : 2,
    maxFileSize : '4MB'
  });
</script>