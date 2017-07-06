<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title>选择旅游路线</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
    <script src="${stc}/js/layer/layer.js"></script>
    <script src="${stccdn}/js/area.js"></script>
    <style>

        .font {
            font-size: 20px;
            color: #fff;
            width: 100%;
            float: left;
            position: relative;
            z-index: 99;
            text-align: center;
        }

        .list-item .list-label {
            width:150px;
        }
    </style>

</head>
<header class="header"><h1>选择旅游路线</h1><a href="${ctx}/tour/findparentInfo"  class="button-left"><i class="fa fa-angle-left"></i></a></header>
<div onclick="TravelDetil(1)" class="opacityAll" style="width:100%;position:relative;">
    <img class="opacityFirst" src="${ctx}/images/Travel1.png" style="display:block;width:100%;" />
    <img src="${ctx}/images/opacity.png" class="opacity" style="display:block;width:100%;z-index:9;" />
    <p class="font">【智优生物为爱前行·洱语花香】</p>
</div>
<div onclick="TravelDetil(2)" class="opacityAll" style="width:100%;position:relative;">
    <img class="opacityFirst" src="${ctx}/images/Travel2.png" style="display:block;width:100%;" />
    <img src="${ctx}/images/opacity.png" class="opacity" style="display:block;width:100%;z-index:9;" />
    <p class="font">【智优生物为爱前行·纵享雨林】</p>
</div>
<div onclick="TravelDetil(3)" class="opacityAll" style="width:100%;position:relative;">
    <img class="opacityFirst" src="${ctx}/images/Travel3.png" style="display:block;width:100%;" />
    <img src="${ctx}/images/opacity.png" class="opacity" style="display:block;width:100%;z-index:9;" />
    <p class="font">【智优生物为爱前行·西蜀问道】</p>
</div>
<div onclick="TravelDetil(4)" class="opacityAll" style="width:100%;position:relative;">
    <img class="opacityFirst" src="${ctx}/images/Travel4.png" style="display:block;width:100%;" />
    <img src="${ctx}/images/opacity.png" class="opacity" style="display:block;width:100%;z-index:9;" />
    <p class="font">【智优生物为爱前行·梦幻时光】</p>
</div>
<script>
    function TravelDetil(num){
        travelT= layer.open({
            type: 2,
            area:['100%', '100%'],
            title: false,
            scrollbar: false,
            closeBtn: 0,
            content: '${ctx}/tour/findTourUserVo'
        });
    }
   var  travelT;

    function cloethis(){
        layer.close(travelT);
    }
    $(function(){
        $(".opacityFirst").load(function(){
            var opacityT=$(".opacityFirst").height();
            $(".opacity").css("margin-top",-opacityT);
            $(".font").css("margin-top",-opacityT/2-20);
        });
    })
</script>
</body>
</html>
