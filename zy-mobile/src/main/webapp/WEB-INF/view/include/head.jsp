<%@ page language="java" pageEncoding="UTF-8"%>
<link rel="shortcut icon" href="${stccdn}/favicon.ico" />
<link rel="stylesheet" href="${stccdn}/css/common.css" />
<link rel="stylesheet" href="${stccdn}/css/components.css" />
<link rel="stylesheet" href="${stccdn}/css/custom.css" />
<link rel="stylesheet" href="${stccdn}/css/icon.css" />
<link rel="stylesheet" href="${stccdn}/plugin/font-awesome-4.6.3/css/font-awesome.min.css" />
<link rel="stylesheet" href="${stccdn}/plugin/mui-1.0/jquery.myui.css" />
<script src="${stccdn}/plugin/jquery-1.11.0/jquery.min.js"></script>
<script src="${stccdn}/plugin/layer-mobile-2.0/layer.js"></script>
<script src="${stccdn}/plugin/mui-1.0/jquery.myui.js"></script>
<script src="${stccdn}/plugin/fastclick-1.0/fastclick.js "></script>
<script src="${stccdn}/js/common.js"></script>
<script src="${stccdn}/js/util.js"></script>
<script>
  $(function() {
    FastClick.attach(document.body);
  });
  var Config = {
    stc : '${stc}',
    ctx : '${ctx}',
    stccdn : '${stccdn}'
  }
  
  window.messageShow = function(message, icon){
    $.message('message', icon || 'info');
  };
  window.messageFlash = function(message, time){
    layer.open({
      content: message,
      skin: 'msg',
      time: time || 2 //自动关闭
    });
  };
  window.messageAlert = function(message, button){
    layer.open({
      content: message,
      btn: button || '确定'
    });
  };
  
</script>
<c:if test='${not empty result}'>
<script>
  $(function() {
    switch ('${result.code}') {
      case '0':
        $.message('${result.message}', 'success');
        break;
      case '500':
        $.message('${result.message}', 'error');
        break;
      default:
        $.message('${result.message}', 'info');
        break;
    }
  });
</script>
</c:if>

