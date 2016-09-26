<%@ page language="java" pageEncoding="UTF-8"%>
<script src="${stccdn}/extend/imageupload/imageupload.js"></script>
<link rel="stylesheet" href="${stccdn}/extend/imageupload/imageupload.css" />
<script>
  $.fn.imageupload.setDefaults({
    url : '${ctx}/image/upload',
    width : 100,
    height : 100,
    retain : 2,
    maxFileSize : '6MB',
    isMultipart : false,
    success : function(result){
      var $this = $(this);
      if($this.hasClass('image-add')){
        var limit = $this.attr('data-limit');
        var name = $this.attr('data-name');
        var imageItem = '<div class="image-item">' 
                      + '<input type="hidden" name="' + name + '" value="' + result.image + '">'
                      + '<img src="' + result.imageThumbnail + '">' 
                      + '<input type="file">' 
                      + '</div>';
        $(imageItem).insertBefore($this);
        var imageItems = $this.siblings('.image-item');
        if (limit && limit <= imageItems.length) {
          $this.remove();
        }
        imageItems.last().imageupload();
      }
    }
  });
</script>