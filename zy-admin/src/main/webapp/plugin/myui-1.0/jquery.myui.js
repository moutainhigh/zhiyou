/**
 * @description my UI
 * @required jQuery
 * @author Yoki
 * @version 1.0
 */

;
(function() {

  /**
   * loading... 遮盖效果
   */
  $.fn.loading = function(message) {
    var $this = $(this);
    $('<div class="loading-mask"></div>').css({
      display : 'block',
      position : 'absolute',
      width : '100%',
      height : $this.height()
    }).appendTo($this);
    var $msg = $('<div class="loading-mask-msg"></div>').html(message).appendTo($this);
    $msg.css({
      display : 'block',
      position : 'absolute',
      left : ($this.outerWidth(true) - $msg.outerWidth(true)) / 2
    });
  };

  $.loading = function(message) {
    $('<div class="loading-mask"></div>').css({
      display : 'block',
      position : 'fixed',
      width : '100%',
      height : $(window).height()
    }).appendTo('body');
    $('<div class="loading-mask-msg"></div>').html(message).appendTo('body').css({
      display : 'block',
      position : 'fixed',
      left : ($(document.body).outerWidth(true) - 190) / 2,
      top : ($(window).height() - 45) / 2
    });
  };

  $.loaded = function() {
    $('.loading-mask').remove();
    $('.loading-mask-msg').remove();
  };

  /**
   * @author 将form表单元素的值序列化成对象
   * @returns object
   */
  $.fn.serializeObj = function() {
    var o = {}, arrayObj = this.serializeArray();
    $.each(arrayObj, function(index) {
      if (o[this['name']]) {
        o[this['name']] = o[this['name']] + "," + this['value'];
      } else {
        o[this['name']] = this['value'];
      }
    });
    return o;
  };

  /**
   * Form 序列化表单元素数据
   * 
   * @requires jQuery
   * @param 参数bool:
   *          设置为true的话，会把string型"true"和"false"字符串值转化为boolean型。
   * @return object
   */
  $.fn.serializeObject = function(bool) {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
      var val = bool ? ((this.value == 'true' || this.value == 'false') ? this.value == 'true' : this.value) : this.value;
      if (o[this.name]) {
        if (!o[this.name].push) {
          o[this.name] = [ o[this.name] ];
        }
        o[this.name].push(val || '');
      } else {
        o[this.name] = val || '';
      }
    });
    return o;
  };

  var _myui_window_index = 0; // 弹出窗口idx

  var loadContent = function($dialog, options) {
    var $dialogContent = $dialog.find('div.window-content');

    if (options.content) {
      $dialogContent.html(options.content);
      // $.parser.parse($dialog);
      options.onComplete.call(window.top.jQuery, $dialog);
    } else {
      // var iframe = document.createElement('iframe');
      var iframe = $('<iframe src="" frameborder="no" border="0" style="width:100%;height:100%;border:none;overflow: scroll;"></iframe>');
      if (iframe[0].attachEvent) {
        iframe[0].attachEvent('onload', function() {
          options.onComplete.call(window.top.jQuery, $dialog);
        });
      } else {
        iframe[0].onload = function() {
          options.onComplete.call(window.top.jQuery, $dialog);
        };
      }
      $dialogContent.append(iframe);
      iframe[0].src = options.url;
      $dialog.iframe = iframe;
    }

  };

  /**
   * 弹出窗口
   */
  $.window = function(options) {
    options = $.extend({}, $.window.defaults, options || {});

    if (!options.url && !options.content) {
      alert('缺少参数[url或content不能同时为空]!');
      return;
    }

    /* html */
    options.id = options.id || ('_myui_window_' + (_myui_window_index++));
    var html = '<div id="' + options.id + '" class="myui-window">' + '<div class="window-panel shadow">';
    if (options.title) {
      html += '<div class="window-title">' + (options.icon ? '<i class="window-icon icon-' + options.icon + '"></i>' : '') + options.title + '</div>';
    }
    html += '<div class="window-close"></div>';
    html += '<div class="window-content"></div>';

    if (options.button == true) {
      html += '<div class="window-buttonbar">' + '<input type="button" class="window-button ok" value="' + options.ok + '" hidefocus="true" />'
          + '<input type="button" class="window-button cancel" value="' + options.cancel + '" hidefocus="true" />' + '</div>';
    }
    html += '</div></div>'; // window-panel

    var $dialog = $(html);

    $dialog.css({
      width: options.width,
      height: options.height,
      'z-index' : options.zIndex
    }).appendTo($(options.target));

    var $dialogTitle = $dialog.find('.window-title');
    var $buttonClose = $dialog.find('.window-close');
    var $dialogContent = $dialog.find('.window-content');
    var $buttonbar = $dialog.find('.window-buttonbar');
    var $buttonOk = $dialog.find('.ok');
    var $buttonCancel = $dialog.find('.cancel');
    var $overlay = $('<div class="window-mask"></div>');

    $dialogContent.css({
      height : $dialog.height() - $dialogTitle.height() - $buttonbar.height()
    });
    if ($dialog.width() > $(window).width()) { /* fix overflow */
      $dialogContent.css({
        width : $(window).width()
      });
    }
    if ($dialog.height() > $(window).height()) { /* fix overflow */
      $dialogContent.css({
        height : $(window).height() - $dialogTitle.height() - $buttonbar.height()
      });
    }

    $dialog.css({
      'margin-left' : -parseInt($dialog.width() / 2),
      'margin-top' : -parseInt($dialog.height() / 2)
    });

    if (options.modal == true) {
      $overlay.css({
        width : '100%',
        height : '100%' //$(window).height()
      }).appendTo($(options.target)).fadeIn(300);
    }
    function onClose() {
      if ($.isFunction(options.onClose) == 'function') {
        options.onClose.apply($dialog);
      }
      $overlay.remove();
      $dialog.remove();
    }
    $dialog.close = onClose;
    $buttonClose.click(onClose);
    if (options.overlayClose == true) {
      $overlay.click(onClose);
    }
    $buttonOk.click(function() {
      //alert($dialog);
      if ($.isFunction(options.okCallback)) {
        if (options.okCallback.call(window.top.jQuery, $dialog) != false) {
          onClose();
        }
      } else {
        onClose();
      }
    });
    $buttonCancel.click(function() {
      if ($.isFunction(options.cancelCallback)) {
        if (options.cancelCallback.apply(window.top.jQuery, $dialog) != false) {
          onClose();
        }
      } else {
        onClose();
      }
    });

    // title dnd
    var pointX;
    var pointY;

    $dialogTitle.mousedown(function(event) {
      var offset = $(this).offset();
      var dialogWidth = $dialog.width();
      var dialogHeight = $dialog.height();

      if (!window.XMLHttpRequest) {
        pointX = event.clientX - offset.left + 6;
        pointY = event.clientY - offset.top + 6;
      } else {
        pointX = event.pageX - offset.left + 6;
        pointY = event.pageY - offset.top + 6;
      }

      $(document).bind('mousemove', function(event) {
        var top = event.clientY - pointY;
        var left = event.clientX - pointX;
        if (left < 0) {
          left = 0;
        }
        if (left + dialogWidth > $(window).width()) {
          left = $(window).width() - dialogWidth;
        }
        if (top < 0) {
          top = 0;
        }
        if (top + dialogHeight > $(window).height()) {
          top = $(window).height() - dialogHeight;
        }
        $dialog.css({
          'top' : top,
          'left' : left,
          'margin' : 0
        });
      });
      return false;
    });
    $(document).mouseup(function() {
      $(document).unbind('mousemove');
    });

    $dialog.keypress(function(event) {
      if (event.keyCode == 13) {
        if ($.isFunction(options.okCallback)) {
          if (options.okCallback.call(window.top.jQuery, $dialog)!= false) {
            onClose();
          }
        } else {
          onClose();
        }
      }
    });
    if (options.timeout) {
      setTimeout(function() {
        $dialog.animate({
          top : $dialog.position().top - 50,
          opacity : 0
        }, 300, onClose);
      }, options.timeout);
    }
    loadContent($dialog, options);
    $dialog.fadeIn(300);
    $dialog.focus();
    return $dialog;
  };

  $.window.defaults = $.extend({}, {
    url : '',
    content : '',
    title : '',
    icon : null,
    target : 'body',
    width : '50%',
    height : 'auto',
    zIndex : 1000,
    closable : true,
    timeout : 0,
    overlayClose : false,
    modal : true,
    button : true,
    onComplete : function() {
    },
    ok : '确定',
    okCallback : function() {
    },
    cancel : '取消',
    cancelCallback : function() {
    }
  });

  /**
   * 信息提示
   */
  $.message = function(options) {
    if (typeof (options) == 'string') {
      options = {
        content : options
      };
      if (arguments.length > 1) {
        options.type = arguments[1];
      }
      if (arguments.length > 2) {
        options.icon = arguments[2];
      }
    }
    options = $.extend({}, $.message.defaults, options || {});
    if (options.content == null) {
      options.content = '';
    }
    if (options.type == 'alert') {
      options.content = '<div style="padding: 10px;">' + options.content + '</div>';
      $.window(options);
      return;
    }

    var html = '<div class="myui-message"><div class="message-wrap"></div>' + '<div class="message-panel">';
    if (options.title != null) {
      html += '<div class="message-title">' + (options.icon != null ? '<i class="message-icon icon-' + options.icon + '"></i>' : '') + options.title + '</div>';
    }
    html += '<div class="message-close"></div>';
    html += '<div class="message-content"><p>' + options.content + '</p></div>';

    html += '</div></div>';
    var $message = $(html);
    $message.css({/* width: options.width, height: options.height, */
      'bottom' : 0,
      'right' : 0,
      'z-index' : options.zIndex
    }).appendTo($(options.target));
    var $messageTitle = $message.find('.message-title');
    var $messageClose = $message.find(".message-close");
    var $messageContent = $message.find(".message-content");

    $messageTitle.css({
      width : '100%'
    });
    $messageContent.css({
      width : '100%',
      height : options.height - $messageTitle.height()
    });

    var onClose = function() {
      $message.remove();
    };
    $messageClose.click(onClose);

    switch (options.type) {
      case 'fade':
        $message.css({
          width : options.width
        }).fadeIn(600);
        if (options.timeout) {
          setTimeout(function() {
            $message.fadeOut(600);
          }, options.timeout);
        }
        break;
      case 'slide':
        $message.css({
          'bottom' : -$message.height(),
          'right' : 0,
          width : options.width
        }).show();
        $message.animate({
          bottom : 0
        }, 500);
        if (options.timeout) {
          setTimeout(function() {
            $message.animate({
              bottom : -$message.height(),
              opacity : 0
            }, 500, onClose);
          }, options.timeout);
        }
        break;
      case 'show':
        $message.css({
          width : options.width
        }).show(600);
        if (options.timeout) {
          setTimeout(function() {
            $message.hide(600);
          }, options.timeout);
        }
        break;
      default:
        $message.css({
          width : options.width
        }).show();
        if (options.timeout) {
          setTimeout(function() {
            $message.hide();
          }, options.timeout);
        }
        break;
    }
    return $message;
  };

  $.message.defaults = $.extend({}, $.window.defaults, {
    type : 'alert', // alert,slide,fade,show
    title : '提示信息',
    icon : 'info', // info, confirm, error
    content : '',
    target : 'body',
    width : '30%',
    height : '160px',
    zIndex : 1000,
    closable : true,
    timeout : 0,
    overlayClose : true,
    modal : true,
    button : false,
    ok : '确定',
    okCallback : function(message) {
    },
    cancel : '取消',
    cancelCallback : function(message) {
    }
  });

  $.messageAlert = function(message, icon) {
    $.message({
	  type : 'alert',
      icon : icon,
      title : '提示信息',
      content : message,
      button : true
    });
  };
  
  $.messageShow = function(message, icon, type) {
    $.message({
      type : (type ? type : 'slide'),
      title : '提示信息',
      icon: icon,
      content : message,
      timeout : 5000,
      button : false
    });
  };

  $.confirm = function(message, callback) {
    $.message({
      icon : 'info',
      title : '警告!',
      content : message,
      button : true,
      okCallback : callback
    });
  };

  $.confirmHref = function(message, href) {
    $.message({
      icon : 'confirm',
      title : '警告!',
      content : message,
      button : true,
      okCallback : function(r) {
        if (r) {
          location.href = href;
        }
      }
    });
    return false;
  };

  /**
   * Image Scan
   */
  $.imagescan = function(options){
    if(typeof(options) == 'string') {
      options = {url: options};
    }
    options = $.extend({}, $.imagescan.defaults, options || {});
    if (options.url == null) {
      alert('缺少参数[图片URL]!');
      return;
    }
    var scrWidth = $(window).width(), scrHeight = $(window).height();
    var width = options.width > scrWidth - 16 ? scrWidth - 16 : options.width;
    var height = options.height > scrHeight - 16 ? scrHeight - 16 : options.height;
    $image = $('<img src="' + options.url + '" alt="' + options.title + '" style="display:none;" />');
    var str = '<div class="myui-imagescan">'
      + '<div class="imagescan-close"><a onclick="return false;" href="javascript:void(0);"></a></div>'
      + '<div class="imagescan-content"><div class="myui-imagescan-wrapper"><div class="loading"></div></div>';
    if(options.toolbar) {
      str += '<div class="imagescan-bottom imagescan-toolbar">'
          + '<div class="toolbar-mask"></div><div class="image-title">' + options.title + '</div><div class="image-tools">'
          + '<ul><li><a title="放大" href="javascript:void(0)" class="magnify" style="display:none">放大</a></li>'
          + '<li><a title="缩小" href="javascript:void(0)" class="minify" style="display:none">缩小</a></li>'
          + '<li><a title="全屏模式" href="javascript:void(0)" class="fullscreen">全屏</a></li>'
          + '</ul></div></div>';
      str += '<div class="imagescan-top imagescan-toolbar">'
        + '<div class="toolbar-mask"></div><input class="image-url" style="display:none" type="text" readonly="readonly" value="' + options.url + '" />'
        + '</div>';
    }
    str += '</div></div>';
    
    var $imagescan = $(str);
    $imagescan.appendTo($(options.target));
    $imagescanContent = $imagescan.find(".imagescan-content");
    $imageWrapper = $imagescan.find(".myui-imagescan-wrapper");
    $loading = $imagescan.find(".loading");
    $toolbar = $imagescan.find(".imagescan-toolbar");
    $magnify = $toolbar.find(".magnify");
    $minify = $toolbar.find(".minify");
    $fullscreen = $toolbar.find(".fullscreen");
    $thumbnail = $imagescan.find(".imagescan-thumbnail");
    $imagescan.css({'margin-left': - parseInt(width / 2), 'margin-top': - parseInt(height / 2), 'z-index': options.zIndex + 1});
    $imagescanContent.css({'width': width, 'height': height - $thumbnail.height()});
    
    $imagescan.hover(function(){
      $toolbar.show();
    },
    function(){
      $toolbar.fadeOut(300);
    });
    var $overlay = $('<div class="myui-imagescan-mask"></div>').css({'z-index': options.zIndex}).appendTo($(options.target));
    var onClose = function(){
      $imagescan.remove();
      $overlay.remove();
    };
    var $imagescanClose = $imagescan.find(".imagescan-close");
    $imagescanClose.click(onClose);
    if(options.overlayClose == true) {
      $overlay.click(onClose);
    }
    //var $fullscreenClose = $('<div class="myui-imagescan-fullscreen-close"></div>').css({'z-index': options.zIndex + 1}).appendTo($(options.target));
    $fullscreen.click(function(){
      //$fullscreenClose.show();
      $overlay.css('opacity', 1.0).append($imageWrapper);
      //$overlay.unbind();
      var size = getSize($image[0], scrWidth, scrHeight);
      resizeImage($image[0], size.width, size.height, function(){
        $imageWrapper.css({width: size.width, height: size.height, "margin-left": - parseInt(size.width / 2), "margin-top": - parseInt(size.height / 2)});
        $imagescan.hide();
      });
    });
     
    /*$fullscreenClose.click(function(){
      $fullscreenClose.hide();
      $overlay.css('opacity', 0.8);
      $imageWrapper.appendTo($imagescanContent);
      $imagescan.show();
      $overlay.click(onClose);
      var size = getSize($image[0], width, height);
      resizeImage($image[0], size.width, size.height, function(){
        $imageWrapper.css({"margin-left": - parseInt(size.width / 2), "margin-top": - parseInt(size.height / 2)});
      });
    });*/
    //$imagescan.css({"margin-left": - parseInt($imagescan.width() / 2), "margin-top": - parseInt($imagescan.height() / 2), "z-index": options.zIndex});
    var size = getSize($image[0], width, height);
    resizeImage($image[0], size.width, size.height, function(){
      $imageWrapper.css({"margin-left": - parseInt(size.width / 2), "margin-top": - parseInt(size.height / 2)});
      $image.show();
      $loading.hide();
    });
    $imageWrapper.append($image);
    
    function getSize(image, width, height) {
        var w,h;
        var tmpimg = new Image();
        tmpimg.src = image.src;// + "?" + new Date();
        if(tmpimg.width > 0 && tmpimg.height > 0) {
          w = tmpimg.width;
          h = tmpimg.height;
           if (w / h >= width / height) {
            if (w > width) {
              h = (h * width) / w;
              w = width;
            }
          } else {
            if (h > height) {
              w = (w * height) / h;
              h = height;
            }
          }
          image.width = w;
          image.height = h;
        } else {
          tmpimg.onload = function(){
            if (tmpimg.width == 0 || tmpimg.height == 0) {
              return;
            }
            w = tmpimg.width;
            h = tmpimg.height;
            if (w / h >= width / height) {
              if (w > width) {
                h = (h * width) / w;
                w = width;
              }
            } else {
              if (h > height) {
                w = (w * height) / h;
                h = height;
              }
            }
            image.width = w;
            image.height = h;
          };
        }
        return {width: w, height: h};
      };
 
    function resizeImage(image, width, height, callback) {
      image.width = width;
      image.height = height;
      if ($.isFunction(callback)) {
        callback.apply();
      }
    };
  };

  $.imagescan.defaults = $.extend({}, {
    url : '',
    title : '',
    target : 'body',
    width : 750,
    height : 750,
    zIndex : 9900,
    toolbar : true,
    closable : true,
    overlayClose : true
  });
  
  /**
   * Image view
   */
  $.imageview = function(options) {
    if(typeof(options) == 'string') {
      options = {url: options};
    } else if(typeof(options) == 'Array') {
      options = {url: options};
    }
    options = $.extend({}, $.imageview.defaults, options || {});
    if (options.url == null) {
      alert('缺少参数[图片URL]!');
      return;
    }
    
    var str = '<aside class="myui-imageview">'
          +   '<a href="javascript:;" class="btn-close button-right"><i class="fa fa-times-circle"></i></a>';
    if(options.title){
      str +=  '<header class="header">'
          +   '<h2>' + options.title + '</h2>'
          +   '</header>';
    }
    str +=    '<div class="myui-imageview-wrap"><div class="loading"></div></div>';
          +   '</aside>';
    $imageview = $(str);
    $('body').css({'overflow': 'hidden'}).append($imageview);
    $imageWrap = $imageview.find(".myui-imageview-wrap");
    $loading = $imageview.find(".loading");
    
    var onClose = function(){
      $imageview.animate({opacity: 0}, 300, function(){
        $imageview.remove();
      })
      $('body').css({'overflow': 'auto'});
    };
    if(options.shadeClose){
      $imageWrap.click(function(){
        onClose();
        //$imageview.find(".header").slideToggle(300);
      });
    }
    $imageview.find(".btn-close").click(onClose);;
    
    var maxWidth = $(window).width(), maxHeight = $(window).height() - (options.title ? 48 : 0);
    $image = $('<div class="myui-imageview-inner"><img style="display:none" src="' + options.url + '"></div>');
    $image.appendTo($imageWrap);
    $image.find('img').bind('load', function(){
      var $this = $(this);
      w = $this.width();
      h = $this.height();
      if (w / h >= maxWidth / maxHeight) {
        if (w > maxWidth) {
          h = (h * maxWidth) / w;
          w = maxWidth;
        }
      } else {
        if (h > maxHeight) {
          w = (w * maxHeight) / h;
          h = maxHeight;
        }
      }
      $this.width(w).height(h).show();
      $loading.remove();
    });
    
  }
  
  $.imageview.defaults = $.extend({}, {
    url : '',
    title : '',
    shadeClose : true
  });

  /**
   * 表单提示
   */
  $.fn.hint = function(options) {
    var $this = $(this);

    var dataOptions = {};
    var s = $.trim($this.attr("data-options"));
    if (s) {
      var _s = s.substring(0, 1);
      var _e = s.substring(s.length - 1, 1);
      if (_s != "{") {
        s = "{" + s;
      }
      if (_e != "}") {
        s = s + "}";
      }
      dataOptions = (new Function("return " + s))();
    }

    options = $.extend(options, dataOptions);

    var text = options.text;
    var maxlen = options.maxlength;

    $hintbody = $('<span class="hint-body"><span class="hint-arrow"></span>' + text + '</span>');
    $hintbody.appendTo($this.parent());
    $hintbody.hide();

    $this.bind('focus', function() {
      $hintbody.show();
    }).bind('blur', function() {
      $hintbody.hide();
    });

    function countChar(str) {
      var count = 0;
      for (var i = 0; i < str.length; i++) {
        if (str[i].match(/[^\x00-\xff]/ig) != null) // 全角
          count += 2; // 如果是全角，占用两个字节
        else
          count += 1; // 半角占用一个字节
      }
      return count;
    }
    if (maxlen) {
      $hintbody.append('&nbsp;剩余<span class="leftlength">' + maxlen + '</span>个字符.');

      var textCounter = function() {
        var len = countChar($this.val());// 计算字符、字节的长度
        if (len > maxlen)
          $this.val($this.val().substring(0, maxlen));
        $hintbody.find('.leftlength').text(maxlen - countChar($this.val()));
      };
      $hintbody.find('.leftlength').text(maxlen - countChar($this.val()));
      $this.bind('keydown', function() {
        textCounter();
      }).bind('keyup', function() {
        textCounter();
      });
    }
  };

  $.fn.hint.defaults = $.extend({}, {

  });

  $.fn.tabs = function(tabContent, eventName, interval) {
  	eventName = !eventName ? 'click' : eventName;
    var $tabs = $(this);
    var $tabContent = $(tabContent);
    $tabs.each(function() {
      var $this = $(this);
      var index = $tabs.index($this);
      if ($this.hasClass("current")) {
        $tabContent.eq(index).show();
      } else {
        $tabContent.eq(index).hide();
      }
    });
    $tabs.bind(eventName, function() {
      var $this = $(this);
      var index = $tabs.index($this);
      $this.addClass("current").siblings().removeClass("current");
      $tabContent.hide().eq(index).show();
    });

    if (interval) {
    	var showPanel = function(index) {
      	var count = $tabs.size();
        if (index < 0) {
          index = count - 1;
        } else if (index >= count) {
          index = 0;
        }
        $tabs.removeClass("current").eq(index).addClass("current");
        $tabContent.hide().eq(index).show();
      };
      
    	var index = 0;
    	var autoInterval = setInterval(function() {
        showPanel(index++);
      }, interval);
    }

  };
  
  $.fn.placeholder = function (config) {
  	if (('placeholder' in document.createElement('input'))) {
  		return;
  	}
    return this.each(function (n) {
        var that = $(this), pl = that.attr('placeholder');
        if (this.type == 'password') {
            var wrap = that.wrap('<div class="placeholder" style="width:' + that.outerWidth(true) + 'px;height:' + that.outerHeight(true) + 'px"></div>').parent();
            var placeholderText = wrap.append('<span class="placeholder-text" style="line-height:' + that.outerHeight(true) + 'px;left:' + that.css('padding-left') + ';font-size:' + that.css('font-size') + '">' + pl + '</span>').find('span.placeholder-text');
            wrap.click(function () {
                wrap.find('span.placeholder-text').hide();
                that.focus();
            });
            that.blur(function () {
                if (that.val() == '') placeholderText.show();
            });
        } else {
            that.focus(function () {
                if (this.value == pl) that.val('').removeClass('placeholder');
            }).blur(function () {
                if (this.value == '') that.val(pl).addClass('placeholder');
            }).trigger('blur').closest('form').submit(function () {
                if (that.val() == pl) that.val('');
            });
        }
    });
  };
  //扩展方法clearPlaceholderValue：提交数据前执行一下，清空和placeholder值一样的控件内容。防止提交placeholder的值。
  //用于输入控件不在表单中或者使用其他代码进行提交的，不会触发form的submit事件，记得一定要执行此方法
  //是否要执行这个方法，可以判断是否存在此扩展
  //DMEO:if($.fn.clearPlaceholderValue)$('input[placeholder],textarea[placeholder]').clearPlaceholderValue()
  $.fn.clearPlaceholderValue = function () {
    return this.each(function () {
        if (this.value == this.getAttribute('placeholder')) this.value = '';
    });
  }

  $.fn.timer = function(options) {
    if (typeof (options) == 'string') {
      options = {
        beginText : options
      };
      if (arguments.length > 1) {
        options.endText = arguments[1];
      }
    }
    options = $.extend({}, $.fn.timer.defaults, options || {});
    $(this).each(
        function() {
          var $this = $(this);
          var beginTime = $this.attr('data-begin-time');
          if(beginTime){
          	beginTime = strToDate(beginTime);
          }
          var endTime = strToDate($this.attr('data-end-time'));
          var nowTime = new Date().getTime();
          var $timerInterval = function() {
            nowTime = new Date().getTime();
            var bal = endTime - nowTime;
            
            bal_day = Math.floor(bal / (60*60*24*1000));
            bal_hour = Math.floor((bal - (bal_day*60*60*24*1000)) / (60*60*1000));
            bal_minute = Math.floor((bal - (bal_day*60*60*24*1000) - (bal_hour*60*60*1000)) / (60*1000));
            bal_second = Math.floor((bal - (bal_day*60*60*24*1000) - (bal_hour*60*60*1000) - (bal_minute*60*1000)) / 1000);
            var html = '';
            if(bal_day > 0) {
            	html += bal_day + '<em>天</em>';
            }
            html += (bal_hour < 10 ? '0' + bal_hour : bal_hour) + '<em>时</em>';
            html += (bal_minute < 10 ? '0' + bal_minute : bal_minute) + '<em>分</em>';
            html += (bal_second < 10 ? '0' + bal_second : bal_second) + '<em>秒</em>';
            
            $this.html(html);
          };
          if (beginTime && beginTime > nowTime) { // 未开始
          	$this.html(options.beginText);
          } else if (endTime > nowTime) { // 进行中
            $timerInterval();
            setInterval($timerInterval, 1000);
          } else if (endTime < nowTime) { // 已结束
          	$this.html(options.endText);
          	clearInterval($timerInterval);
          }
        });

    // 字符串转换成日期对象函数
    function strToDate(dateStr) {
      var arr = dateStr.split(' ');
      arr[0] = arr[0].split('-');
      arr[1] = arr[1].split(':');
      var theDate = new Date(arr[0][0], (arr[0][1] - 1), arr[0][2], arr[1][0], arr[1][1], arr[1][2]);
      // js的日期对象的月份是从0～11，0代表1月份，11代表12月份，故此第二个参数月份要减1
      return theDate.getTime();
    }
  };
  
  $.fn.timer.defaults = $.extend({}, {
  	beginText : '未开始',
    endText : '已结束'
  });
  
})(jQuery);
