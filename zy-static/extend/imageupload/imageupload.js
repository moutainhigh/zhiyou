;
(function() {
  'use strict';
  function imageupload(element, options) {
    // quit if no root element
    if (!element) {
      return;
    }
    if((typeof options) == 'undefined') {
      options = {};
    }
    if((typeof options.url) == 'undefined') {
      options.url = this.defaults.url;
    }
    if((typeof options.width) == 'undefined') {
      options.width = this.defaults.width;
    }
    if((typeof options.height) == 'undefined') {
      options.height = this.defaults.height;
    }
    if((typeof options.retain) == 'undefined') {
      options.retain = this.defaults.retain;
    }
    if((typeof options.maxFileSize) == 'undefined') {
      options.maxFileSize = this.defaults.maxFileSize;
    }
    
    element.style.width = options.width + 'px';
    element.style.height = options.height + 'px';
    
    var fileInput = element.getElementsByTagName('input')[1];
    
    fileInput.onchange = function(evt) {
      fileSelect(fileInput);
    };
    
    var quality = options.quality ? options.quality : 0.5;
    var fileOrigin, fileData;
    var needCompress = quality < 1;
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
      //needCompress = false;
    }
    
    var fileSelect = function(obj){
      fileOrigin = obj.files[0];
      
      loginfo('Origin File type : ' + fileOrigin.type);
      loginfo('Origin File size : ' + fileOrigin.size + 'Byte');
      
      if(!checkFileType(fileOrigin)) {
        showError('您选择的文件类型不合法，请重新选择。');
        return;
      }
      
      if(needCompress){
        compressFile(fileOrigin)
      } else {
        fileData = fileOrigin;
        startUploadFile();
      }
    }
    
    /** compress method */
    var compressFile = function(file) {
      if (typeof FileReader === 'undefined') {
        console('Your browser does not support FileReader...');
        fileData = file;
        startUploadFile();
        return;
      }
      var image = new Image();
      var reader = new FileReader();
      reader.onload = function() {
        var url = reader.result;
        image.src = url;
      };
      var canvas = document.createElement("canvas"),
      ctx = canvas.getContext('2d');
      image.onload = function() {
        var w = image.naturalWidth,
            h = image.naturalHeight;
        canvas.width = w;
        canvas.height = h;
        ctx.drawImage(image, 0, 0, w, h, 0, 0, w, h);
        
        var format = "image/jpeg";
        var base64 = canvas.toDataURL(format, quality);
        var code = window.atob(base64.split(',')[1]);

        var aBuffer = new window.ArrayBuffer(code.length);
        var uBuffer = new window.Uint8Array(aBuffer);
        for(var i = 0; i < code.length; i++){
          uBuffer[i] = code.charCodeAt(i) & 0xff ;
        }
        fileData = new Blob([uBuffer], {
          type: format
        });
        
        loginfo('Compressed File size : ' + fileData.size + 'Byte');
        startUploadFile();
      };
      reader.readAsDataURL(file);
    }
    
    /** file type check method */
    var checkFileType = function(file) {
      var rFilter = /^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
      return rFilter.test(file.type);
    }
    
    /** real upload method */
    var startUploadFile = function() {
      /*
       * var imageObj = element.getElementsByTagName('img')[0]; if(imageObj){
       * imageObj.src = Config.stccdn + '/image/loading_circle.gif'; }
       */
      var state = element.getElementsByClassName('state')[0];
      if(!state){
        var state = document.createElement('em');
        element.appendChild(state);
      }
      state.className = 'state state-loading';
      
      if (options.maxFileSize && fileData.size > sizeToBytes(options.maxFileSize)) {
        showError('您选择的文件超过' + options.maxFileSize + '，请重新选择。');
        if(element.className.indexOf('image-add') != -1) {
          state.className = 'state state-add';
        } else {
          element.removeChild(state);
        }
      }
      
      // get form data for POSTing
      var vFD = new FormData();
      vFD.append("file", fileData);
      vFD.append("width", options.width *　options.retain);
      vFD.append("height", options.height * options.retain);
      // create XMLHttpRequest object, adding few event listeners, and POSTing
      // our data
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
    };
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
      var state = element.getElementsByClassName('state')[0];
      if(element.className.indexOf('image-add') != -1) {
        state.className = 'state state-add';
      } else {
        element.removeChild(state);
      }
      if (options.error) {
        options.error.call(element, e);
      }
    }
    
    /** upload success callback */
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
      // alert(result);
      
      var imageObj = element.getElementsByTagName('img')[0];
      var state = element.getElementsByClassName('state')[0];
      
      if(element.className.indexOf('image-add') != -1) {
        // 多图上传
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
      var k = 1024,
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
    var loginfo = function(message){
      console.info(info);
      //alert(message);
    }
  }

  window.imageupload = imageupload;

  if (window.jQuery || window.Zepto) {
    (function($) {
      $.fn.imageupload = function(params) {
        return this.each(function() {
          $(this).data('imageupload', new imageupload($(this)[0], params));
        });
      };
      $.fn.imageupload.setDefaults = function(defaults) {
        window.imageupload.prototype.defaults = defaults;
      };
    })(window.jQuery || window.Zepto)
  }

}());