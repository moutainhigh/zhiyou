$(function() {
  // var beforeScrollTop = document.body.scrollTop;

});

$.checkLogin = function() {
  $.ajax({
    type : "POST",
    url : Config.ctx + '/checkLogin',
    dataType : "json",
    data : {},
    success : function(result) {
      if (result.status == 2) {
        $.message('请先<a class="font-blue" href="${ctx}/login">登录</a>再评论', 'info', 3000);
      }
    }
  });
};

function bindCode() {
  var layerIndex = layer.open({
    title : '请先绑定您的优惠码:',
    content : '<input type="text" id="code" name="code" class="input width-150 ml-20 mr-20">',
    btn : [ '确认绑定' ],
    shadeClose : false,
    close : true,
    yes : function() {
      if (!$('#code').val()) {
        $.message('请填写您的优惠码', 'error');
        return;
      }
      $.ajax({
        url : Config.ctx + '/bindUser',
        data : {
          code : $('#code').val()
        },
        type : 'POST',
        dataType : 'json',
        success : function() {
          layer.close(layerIndex);
          location.reload();
        },
        error : function() {
          $.message('绑定失败, 请确认您的优惠码', 'error');
        }
      });
    }
  });
}

$(function() {
  // 全局的ajax访问，处理ajax清求时sesion超时
  $(document).ajaxComplete(function(event, XMLHttpRequest, textStatus) {
    /*
     * var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
     * //通过XMLHttpRequest取得响应头，sessionstatus， if(sessionstatus ==
     * "timeout"){//如果超时就处理 ，指定要跳转的页面
     * window.location.replace("${path}/common/login.do"); }
     */
    // alert("XMLHttpRequest.status" + XMLHttpRequest.status);
    switch (XMLHttpRequest.status) {
      case 401:
        alert('您还没有登录, 请先登录');
        break;
      case 403:
        alert('您没有权限执行该操作');
        break;
      case 500:
        alert('操作失败, 请稍后再试');
        break;
      default:
        break;
    }
  });

});

/**
 * Debug print
 * 
 * @param map
 */
function printMap(map) {
  var s = '{';
  for ( var k in map) {
    s += '\n\t' + k + ': \'' + map[k] + '\'';
  }
  s += '\n}';
  alert(s);
}

// 计时
$.fn.countDown = function(a) {
  var b = {
    nowTime : this.attr("data-now") || 0,
    interval : 1000,
    endTime : 60,
    minDigit : true,
    showtime : true,
    callback : null
  }, c = $.extend({}, b, a);
  return this.each(function() {
    var g = null, j = $(this), e = $("<span></span>"), f, h = {}, f = nowTime1 = c.nowTime == 0 ? Math.round(new Date().getTime() / 1000) : Math.round(c.nowTime);
    endTime = f + c.endTime, flag = false;
    function k(l, m) {
      if (!l) {
        return m;
      } else {
        return m < 10 ? m = "0" + m : m;
      }
    }
    (function d() {
      var l = f - nowTime1;
      h.min = k(c.minDigit, Math.floor(l / 60 % 60));
      h.sec = k(c.minDigit, Math.floor(l % 60));
      if (c.showtime) {
        j.html(h.min + ":" + h.sec);
      }
      if (!flag && f > endTime) {
        if (typeof c.callback === "function") {
          flag = true;
          c.callback.call(this);
        }
      }
      f = f + c.interval / 1000;
      g = setTimeout(function() {
        d();
      }, c.interval);
    })();
  });
};
