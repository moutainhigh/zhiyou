;
(function() {
  'use strict';
  function Fileupload(element, options) {
    // quit if no root element
    if (!element) {
      return;
    }
    element.style.width = options.width + 'px';
    element.style.height = options.height + 'px';

    var fileInput = element.getElementsByTagName('input')[1];
    
    fileInput.onchange = function(evt) {
      var file = fileInput.files[0];
      var checked = checkFile(file);
      if (checked) {
        startUploadFile(file);
      }
    }

    function sizeToBytes(size) {
      if (!size) {
        return '0';
      }
      var units = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ];
      for ( var i in units) {
        var regexp = new RegExp('(\\d+)' + units[i] + '$');
        if (regexp.test(size)) {
          var num = size.replace(regexp, '$1');
          return num * Math.pow(1024, i);
        }
      }
      return 0;
    }

    function bytesToSize(bytes) {
      if (bytes === 0) {
        return '0 B';
      }
      var k = 1000, // or 1024
      units = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ],
      i = Math.floor(Math.log(bytes) / Math.log(k));

      return (bytes / Math.pow(k, i)).toPrecision(3) + '' + units[i];
    }
    
    function showError(message) {
      if((window.jQuery || window.Zepto) && $.message){
        $.message(message, 'error');
      } else {
        alert(message);
      }
    }

    function checkFile(file) {
      var rFilter = /^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
      if (!rFilter.test(file.type)) {
        showError('您选择的文件类型不合法，请重新选择。');
        return false;
      }
      // little test for filesize
      if (options.maxFileSize > 0 && file.size > sizeToBytes(options.maxFileSize)) {
        showError('您选择的文件超过' + options.maxFileSize + '，请重新选择。');
        return false;
      }
      return true;
    }

    function startUploadFile(file) {
      var image = element.getElementsByTagName('img')[0];
      if(image){
        image.src = Config.stccdn + '/image/loading_circle.gif';
      }
      // get form data for POSTing
      var vFD = new FormData();
      vFD.append("file", file);
      // create XMLHttpRequest object, adding few event listeners, and POSTing our data
      var oXHR = new XMLHttpRequest();
      oXHR.addEventListener('load', uploadSuccess, false);
      if (options.progress) {
        oXHR.upload.addEventListener('progress', options.progress, false);
      }
      if (options.error) {
        oXHR.addEventListener('error', options.error, false);
      }
      if (options.abort) {
        oXHR.addEventListener('abort', options.abort, false);
      }
      oXHR.open('POST', options.url);
      oXHR.send(vFD);
    }

    function uploadSuccess(e) {
      if(e.target.status != 200){
        alert('图片上传失败，请重试' + '[' + e.target.status + ']');
        return;
      }
      var result = JSON.parse(e.target.responseText);
      if(result.code != 0) {
        alert('图片上传失败，请重试' + '[' + result.code + ']');
        return;
      }
      var imageUrl = result.data;
      // alert(imageUrl);
      
      var image = element.getElementsByTagName('img')[0];
      var inputHidden = element.getElementsByTagName('input')[0];
      if(image){
        image.src = imageUrl;
      }
      if(inputHidden){
        inputHidden.value = imageUrl;
      }
      if(options.success) {
        options.success.call(element, imageUrl);
      }
    }

  }

  window.Fileupload = Fileupload;

  if (window.jQuery || window.Zepto) {
    (function($) {
      $.fn.Fileupload = function(params) {
        return this.each(function() {
          $(this).data('Fileupload', new Fileupload($(this)[0], params));
        });
      }
    })(window.jQuery || window.Zepto)
  }

}());