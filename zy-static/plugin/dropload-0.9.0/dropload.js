/**
 * dropload 西门(http://ons.me/526.html) 0.9.0(160215)
 */

;
(function($) {
  'use strict';
  var win = window;
  var doc = document;
  var $win = $(win);
  var $doc = $(doc);
  $.fn.dropload = function(options) {
    return new DropLoad(this, options);
  };
  var DropLoad = function(element, options) {
    var dl = this;
    dl.$element = element;
    // 上方是否插入DOM
    dl.upInsertDOM = false;
    // loading状态
    dl.loading = false;
    // 是否锁定
    dl.isLockUp = false;
    dl.isLockDown = true;
    // 是否有数据
    dl.hasMoreData = true;
    dl._scrollTop = 0;
    dl._threshold = 0;
    
    dl.init(options);
  };

  // 初始化
  DropLoad.prototype.init = function(options) {
    var dl = this;
    dl.options = $.extend(true, {}, {
      scrollArea : dl.$element, // 滑动区域
      domUp : {
        domClass : 'dropload-up',
        domRefresh : '<div class="dropload-refresh">↓ 下拉刷新</div>',
        domUpdate : '<div class="dropload-update">↑ 释放更新</div>',
        domLoad : '<div class="dropload-load"><span class="i-loading"></span>加载中...</div>'
      },
      domDown : {
        domClass : 'dropload-down',
        domRefresh : '<div class="dropload-refresh">↑ 上拉加载更多</div>',
        domLoad : '<div class="dropload-load"><span class="i-loading"></span>加载中...</div>',
        domNoData : '<div class="dropload-noData">没有更多数据了</div>'
      },
      autoLoad : false, // 自动加载
      distance : 50, // 拉动距离
      threshold : '', // 提前加载距离
      loadUpFn : '', // 上方function
      loadDownFn : '' // 下方function
    }, options);

    // 如果加载下方，事先在下方插入DOM
    if (dl.options.loadDownFn != '') {
      dl.$element.append('<div class="' + dl.options.domDown.domClass + '">' + dl.options.domDown.domRefresh + '</div>');
      dl.$domDown = $('.' + dl.options.domDown.domClass);
    }

    // 计算提前加载距离
    if (!!dl.$domDown && dl.options.threshold === '') {
      // 默认滑到加载区2/3处时加载
      dl._threshold = Math.floor(dl.$domDown.height() * 1 / 3);
    } else {
      dl._threshold = dl.options.threshold;
    }

    // 判断滚动区域
    if (dl.options.scrollArea == win) {
      dl.$scrollArea = $win;
      // 获取文档高度
      dl._scrollContentHeight = $doc.height();
      // 获取win显示区高度 —— 这里有坑
      dl._scrollWindowHeight = doc.documentElement.clientHeight;
    } else {
      dl.$scrollArea = dl.options.scrollArea;
      dl._scrollContentHeight = dl.$element[0].scrollHeight;
      dl._scrollWindowHeight = dl.$element.height();
    }
    fnAutoLoad(dl);

    // 窗口调整
    $win.on('resize', function() {
      if (dl.options.scrollArea == win) {
        // 重新获取win显示区高度
        dl._scrollWindowHeight = win.innerHeight;
      } else {
        dl._scrollWindowHeight = dl.$element.height();
      }
    });

    // 绑定触摸
    dl.$element.on('touchstart', function(e) {
      if (!dl.loading) {
        fnTouches(e);
        fnTouchstart(e, dl);
      }
    });
    dl.$element.on('touchmove', function(e) {
      if (!dl.loading) {
        fnTouches(e, dl);
        fnTouchmove(e, dl);
      }
    });
    dl.$element.on('touchend', function() {
      if (!dl.loading) {
        fnTouchend(dl);
      }
    });

    // 加载下方
    dl.$scrollArea.on('scroll', function() {
      dl._scrollTop = dl.$scrollArea.scrollTop();

      // 滚动页面触发加载数据
      if (dl.options.loadDownFn != '' && !dl.loading && !dl.isLockDown && (dl._scrollContentHeight - dl._threshold) <= (dl._scrollWindowHeight + dl._scrollTop)) {
        loadDown(dl);
      }
    });
  };

  // touches
  function fnTouches(e) {
    if (!e.touches) {
      e.touches = e.originalEvent.touches;
    }
  }

  // touchstart
  function fnTouchstart(e, dl) {
    dl._startY = e.touches[0].pageY;
    // 记住触摸时的scrolltop值
    dl.touchScrollTop = dl.$scrollArea.scrollTop();
  }

  // touchmove
  function fnTouchmove(e, dl) {
    dl._curY = e.touches[0].pageY;
    dl._moveY = dl._curY - dl._startY;

    if (dl._moveY > 0) {
      dl.direction = 'down';
    } else if (dl._moveY < 0) {
      dl.direction = 'up';
    }

    var _absMoveY = Math.abs(dl._moveY);

    // 加载上方
    if (dl.options.loadUpFn != '' && dl.touchScrollTop <= 0 && dl.direction == 'down' && !dl.isLockUp) {
      e.preventDefault();

      dl.$domUp = $('.' + dl.options.domUp.domClass);
      // 如果加载区没有DOM
      if (!dl.upInsertDOM) {
        dl.$element.prepend('<div class="' + dl.options.domUp.domClass + '"></div>');
        dl.upInsertDOM = true;
      }

      fnTransition(dl.$domUp, 0);

      // 下拉
      if (_absMoveY <= dl.options.distance) {
        dl._offsetY = _absMoveY;
        // todo：move时会不断清空、增加dom，有可能影响性能，下同
        dl.$domUp.html(dl.options.domUp.domRefresh);
        // 指定距离 < 下拉距离 < 指定距离*2
      } else if (_absMoveY > dl.options.distance && _absMoveY <= dl.options.distance * 2) {
        dl._offsetY = dl.options.distance + (_absMoveY - dl.options.distance) * 0.5;
        dl.$domUp.html(dl.options.domUp.domUpdate);
        // 下拉距离 > 指定距离*2
      } else {
        dl._offsetY = dl.options.distance + dl.options.distance * 0.5 + (_absMoveY - dl.options.distance * 2) * 0.2;
      }

      dl.$domUp.css({
        'height' : dl._offsetY
      });
    }
  }

  // touchend
  function fnTouchend(dl) {
    var _absMoveY = Math.abs(dl._moveY);
    if (dl.options.loadUpFn != '' && dl.touchScrollTop <= 0 && dl.direction == 'down' && !dl.isLockUp) {
      fnTransition(dl.$domUp, 300);

      if (_absMoveY > dl.options.distance) {
        dl.$domUp.css({
          'height' : dl.$domUp.children().height()
        });
        dl.$domUp.html(dl.options.domUp.domLoad);
        dl.loading = true;
        dl.options.loadUpFn(dl);
      } else {
        dl.$domUp.css({
          'height' : '0'
        }).on('webkitTransitionEnd mozTransitionEnd transitionend', function() {
          dl.upInsertDOM = false;
          $(this).remove();
        });
      }
      dl._moveY = 0;
    }
  }

  // 如果文档高度不大于窗口高度，数据较少，自动加载下方数据
  function fnAutoLoad(dl) {
    if (dl.options.autoLoad) {
      if ((dl._scrollContentHeight - dl._threshold) <= dl._scrollWindowHeight) {
        loadDown(dl);
      }
    }
  }

  // 重新获取文档高度
  function fnRecoverContentHeight(dl) {
    if (dl.options.scrollArea == win) {
      dl._scrollContentHeight = $doc.height();
    } else {
      dl._scrollContentHeight = dl.$element[0].scrollHeight;
    }
  }

  // 加载下方
  function loadDown(dl) {
    dl.direction = 'up';
    dl.$domDown.html(dl.options.domDown.domLoad);
    dl.loading = true;
    dl.options.loadDownFn(dl);
  }

  // 锁定
  DropLoad.prototype.lock = function(direction) {
    var dl = this;
    // 如果不指定方向
    if (direction === undefined) {
      // 如果操作方向向上
      if (dl.direction == 'up') {
        dl.isLockDown = true;
        // 如果操作方向向下
      } else if (dl.direction == 'down') {
        dl.isLockUp = true;
      } else {
        dl.isLockUp = true;
        dl.isLockDown = true;
      }
      // 如果指定锁上方
    } else if (direction == 'up') {
      dl.isLockUp = true;
      // 如果指定锁下方
    } else if (direction == 'down') {
      dl.isLockDown = true;
      // 为了解决DEMO5中tab效果bug，因为滑动到下面，再滑上去点tab，direction=down，所以有bug
      //dl.direction = 'up';
    }
  };

  // 解锁
  DropLoad.prototype.unlock = function() {
    var dl = this;
    // 简单粗暴解锁
    dl.isLockUp = false;
    dl.isLockDown = false;
    // 为了解决DEMO5中tab效果bug，因为滑动到下面，再滑上去点tab，direction=down，所以有bug
    //dl.direction = 'up';
  };

  // 无数据
  DropLoad.prototype.hasMore = function(flag) {
    var dl = this;
    if (flag === undefined || flag == true) {
      dl.hasMoreData = true;
    } else if (flag == false) {
      dl.hasMoreData = false;
    }
  };

  // 重置
  DropLoad.prototype.resetload = function() {
    var dl = this;
    console.info(dl.direction);
    console.info(dl.upInsertDOM);
    if (dl.direction == 'down' && dl.upInsertDOM) {
      dl.$domUp.css({
        'height' : '0'
      }).on('webkitTransitionEnd mozTransitionEnd transitionend', function() {
        dl.loading = false;
        dl.upInsertDOM = false;
        $(this).remove();
        fnRecoverContentHeight(dl);
      });
    } else if (dl.direction == 'up') {
      dl.loading = false;
      // 如果有数据
      if (dl.hasMoreData) {
        // 加载区修改样式
        dl.$domDown.html(dl.options.domDown.domRefresh);
        fnRecoverContentHeight(dl);
        fnAutoLoad(dl);
      } else {
        // 如果没数据
        dl.$domDown.html(dl.options.domDown.domNoData);
      }
    }
  };

  // css过渡
  function fnTransition(dom, num) {
    dom.css({
      '-webkit-transition' : 'all ' + num + 'ms',
      'transition' : 'all ' + num + 'ms'
    });
  }
})(window.Zepto || window.jQuery);