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
  <style>
    .souquan {
      width: 100%;
      text-align: center;
      position: absolute;
      z-index: 99;
      color: #333;
      font-weight: bold;
      top: 38%;
      left:0;
      display: none;
    }
    .userRank {
      width: 100%;
      position: absolute;
      top: 53%;
      left:0;
      text-align: center;
      font-size: 22px;
      color: #2f71b5;
      z-index: 99;
      display: none;
    }
    #imgWrap {
      position: relative;
    }
    @media (device-height:568px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone5 */
      .souquan {
        font-size: 12px;
      }.userRank {
        font-size: 18px;
       }

    }
  </style>
<script type="text/javascript">
  $(function() {
      $('.fa-search').click(function(){
        var codeText = $.trim($('#searchInput').val());  //获取授权编码

        if(codeText != ''){
          $.ajax({
            url: "${ctx}/code/check",
            type: "post",
            dataType: "json",
            data: {'code': codeText},
            success: function(data){
              if(data.code == 0){
//                console.log(data);
//                console.log(data.data.nickname);
//                console.log(data.data.userRank);
                $('#authorImage').attr('src',"${ctx}/images/333.jpg");
                $(".souquan,.userRank").show();
                $(".souquan span").html("授权码:"+$("#searchInput").val());
                $(".souquan .name").html(data.data.nickname);
                var userRank;
                if(data.data.userRank==="V4"){
                  userRank="特级服务商";
                }else if(data.data.userRank==="V3"){
                  userRank="省级服务商";
                }else if(data.data.userRank==="V2"){
                  userRank="市级服务商";
                }else if(data.data.userRank==="V1"){
                  userRank="VIP服务商";
                }
                $(".userRank b").html(userRank);

                $('.error').hide();
                $('#imgWrap').show();
              }else{
                $.dialog({
                  content : '授权码未找到！',
                  skin: 'message',
                  timeout : 2
                });
                $('.error').show();
                $('#imgWrap').hide();
              }
            }
          });
        }else{
          $.dialog({
            content : '授权码不能为空！',
            skin: 'message',
            timeout : 2
          });
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
        <div class="list-text">
          <input id="searchInput" name="searchInput" class="form-input" placeholder="请输入授权码1111" value="" type="text">
        </div>
        <div class="list-unit">
          <i class="fa fa-search btn blue"></i>
        </div>
    </div>
  </div>
  
  <section id="imgWrap" class="p-15">
    <img id="authorImage" class="block-100" src="">
    <div class="souquan">
      <div style="width:100%;text-align: center;font-size: 18px;" class="name"></div>
      <span></span>
    </div>
    <div class="userRank"><b></b></div>
  </section>
  
  <section class="error hide">
    <i class="error-icon font-gray fa fa-exclamation-triangle"></i>
    <div class="error-info">
      <p>暂无数据</p>
      <p>请检查您输入的授权码是否正确</p>
    </div>
  </section>  
  
 
  
</body>
</html>
 