<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
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

<title>${sys}- 授权查询</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/error.css" />
<script type="text/javascript">
  $(function() {
      $('.fa-search').click(function(){
        var codeText = $.trim($('#searchInput').val());  //获取授权编码

        if(codeText != ''){
          $.ajax({  
            url: "${ctx}/check",  
            type: "get",
            dataType: "json",
            data: {'check': codeText}, 
            success: function(data){ 
              //成功 返回data数据
            }  
          }); 
        }else{
          alert('不能为空！');
        }
 
      });
  });
</script>
</head>
<body>
  <header class="header">
    <h1>授权查询</h1>
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <div class="list-group">
    <div class="list-item ">
        <label for="smsCode" class="list-label width-50" style="">ZY</label>
        <div class="list-text">
          <input id="searchInput" name="searchInput" class="form-input" placeholder="授权码" value="" type="text">
        </div>
        <div class="list-unit">
          <i class="fa fa-search btn blue"></i>
        </div>
    </div>
  </div>
  
  <section class="p-15">
    <img class="block-100" src="http://image.zhi-you.net/editor/5d20abcc-c48c-4307-a102-f7a7b2e506e6">
  </section>
  
  <section class="error">
    <i class="error-icon font-gray fa fa-exclamation-triangle"></i>
    <div class="error-info">
      <p>暂无数据</p>
      <p>请检查您输入的授权码是否正确</p>
    </div>
  </section>  
  
  <section class="message-list hide">
    <div class="page-empty">
      <i class="fa fa-bolt"></i> <span>空空如也!</span>
    </div>
  </section>
  
</body>
</html>
 