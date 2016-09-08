;
(function() {
  'use strict';
  function Fileupload(element, options) {
    // quit if no root element
    if (!element) {
      return;
    }
    if(!options.width) {
      options.width = 100;
    }
    if(!options.height) {
      options.height = 100;
    }
    if(!options.retain) {
      options.retain = 2;
    }
    element.style.width = options.width + 'px';
    element.style.height = options.height + 'px';

    var fileInput = element.getElementsByTagName('input')[1];
    
    fileInput.onchange = function(evt) {
      fileSelect(fileInput);
    };
    
    var fileSelect = function(obj){
      var file = obj.files[0];
      if(!checkFileType(file)) {
        showError('您选择的文件类型不合法，请重新选择。');
        return;
      }
      startUploadFile(file);
    }

    var checkFileType = function(file) {
      var rFilter = /^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
      return rFilter.test(file.type);
    }
    var checkFileSize = function(file) {
      return !options.maxFileSize || file.size <= sizeToBytes(options.maxFileSize);
    };

    var startUploadFile = function(file) {
      /*var imageObj = element.getElementsByTagName('img')[0];
      if(imageObj){
        imageObj.src = Config.stccdn + '/image/loading_circle.gif';
      }*/
      var state = element.getElementsByClassName('state')[0];
      if(!state){
        var state = document.createElement('em');
        element.appendChild(state);
      }
      state.className = 'state state-loading';
      
      if (!checkFileSize(file)) {
        showError('您选择的文件超过' + options.maxFileSize + '，请重新选择。');
      }
      var vFD = new FormData();
      vFD.append("file", file);
      vFD.append("width", options.width *　options.retain);
      vFD.append("height", options.height * options.retain);
      // create XMLHttpRequest object, adding few event listeners, and POSTing our data
      var oXHR = new XMLHttpRequest();
      oXHR.addEventListener('load', uploadSuccess, false);
      oXHR.addEventListener('error', uploadError, false);
      if (options.progress) {
        oXHR.upload.addEventListener('progress', uploadProgress, false);
      }
      if (options.abort) {
        oXHR.addEventListener('abort', uploadAbort, false);
      }
      oXHR.open('POST', options.url);
      oXHR.send(vFD);
      
    }

    var uploadProgress = function(e) {
      if (options.progress) {
        options.progress.call(element, e);
      }
    }
    var uploadAbort = function(e) {
      if (options.abort) {
        options.abort.call(element, e);
      }
    }
    var uploadError = function(e) {
      alert('图片上传失败，请重试' + '[' + e.target.status + ']');
      if (options.error) {
        options.error.call(element, e);
      }
    }
    var uploadSuccess = function(e) {
      if(e.target.status != 200){
        alert('图片上传失败，请重试' + '[' + e.target.status + ']');
        return;
      }
      var result = JSON.parse(e.target.responseText);
      if(result.code != 0) {
        alert('图片上传失败，请重试' + '[' + result.code + ']');
        return;
      }
      var resultData = result.data;
      //alert(result);
      
      var imageObj = element.getElementsByTagName('img')[0];
      var state = element.getElementsByClassName('state')[0];
      
      if(element.className.indexOf('image-add') != -1) {
        //多图上传
        state.className = 'state state-add';
      } else {
        imageObj.src = resultData.imageThumbnail;
        imageObj.onload = function(){
          element.removeChild(state);
        };
      }
      
      var inputHidden = element.getElementsByTagName('input')[0];
      if(inputHidden){
        inputHidden.value = resultData.image;
      }
      if(options.success) {
        options.success.call(element, resultData);
      }
    };
    var sizeToBytes = function(size) {
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
    };
    var bytesToSize = function(bytes) {
      if (bytes === 0) {
        return '0 B';
      }
      var k = 1000, // or 1024
      units = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ],
      i = Math.floor(Math.log(bytes) / Math.log(k));

      return (bytes / Math.pow(k, i)).toPrecision(3) + '' + units[i];
    };
    var showError = function(message) {
      if((window.jQuery || window.Zepto) && $.message){
        $.message(message, 'error');
      } else {
        alert(message);
      }
    };

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