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

    <title>旅游路线详情</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
    <script src="${stc}/js/layer/layer.js"></script>
    <script src="${stccdn}/js/area.js"></script>
    <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
    <style>
        .footer {
            position: fixed;
            bottom:0;
            left:0;
            width:100%;
            height:50px;
            background: #6cb92d;
            z-index: 99;
        }
        .footer div {
            float: left;
            width:50%;
            height:50px;
            text-align: center;
            line-height: 50px;
            color: #fff;
            font-size: 18px;
            border-right: 1px solid #fff;
            box-sizing: border-box;
        }
        /*.layer-anim {
          width:100% !important;
          left:0 !important;
          top:0 !important;
        }*/
        .sumbitBtn {
            width:90%;
            margin-left: 5%;
            margin-right: 5%;
        }
        .list-group {
            margin-bottom: 0;
        }
        .font {
            font-size: 20px;
            color: #fff;
            width: 100%;
            float: left;
            position: relative;
            z-index: 99;
            left: 50%;
            margin-left: -20px;
        }
        .MyApply {
            width:90%;
            margin-left: 5%;
            margin-right: 5%;
            height:40px;
            background:#f18200;
            color: #fff;
            margin-top: 30px;
            margin-bottom: 10px;
            text-align: center;
            line-height: 40px;
            font-size: 18px;
            -webkit-border-radius:10px;
            -moz-border-radius:10px;
            border-radius:10px;
            box-sizing: border-box;
        }
        .list-item .list-label {
            width:150px;
        }
        /*清除浮动代码*/
        .clearfloat:before, .clearfloat:after {
            content:"";
            display:table;
        }
        .clearfloat:after{
            clear:both;
            overflow:hidden;
        }
        .clearfloat{
            zoom:1;
        }
        p {
            color: #f13e00;
            line-height: 20px;
            padding: 5px 15px 5px 15px;
        }
        .TravelFont {
            width:100%;
            background: #f0f0f0;
            -webkit-border-radius:20px 20px 0 0;
            -moz-border-radius:20px 20px 0 0;
            border-radius:20px 20px 0 0;
            margin-top: -60px;
            position: relative;
            z-index:9;
            padding:20px 0 20px 0;
        }
        .TravelFOne {
            width:100%;
            height:30px;
            font-size: 18px;
            color: #f13e00;
            text-align: center;
            line-height: 30px;
        }
        .TravelFOneD {
            padding-left:30px;
            padding-right: 30px;
            color: #f13e00;
            line-height: 30px;
        }
        .TravelDis {
            width:100%;
            padding:0px 20px 10px 20px;
            background: #fff;
        }
        .TravelDisTime {
            float: left;
            border: 1px solid #eee;
            -webkit-border-radius:5px;
            -moz-border-radius:5px;
            border-radius:5px;
            margin-left: 15px;
            padding:5px;
            margin-top: 10px;
            background: #f15b00;
        }
        .changeAll {
            width:100%;height:50px;background:#fff;border-bottom: 1px solid #eee;
        }
        .changeSelect,.changeFont {
            float: left;
            width:50%;
            height:50px;
            border:none;
            text-align: center;
            line-height: 50px;
        }
        .changeSelect {
            height:48px;
        }
        .TravelDisTimecolor,.TravelDisTime p {
            color: #fff;
        }
        img {
            width:100%;
        }
    </style>
</head>
<header class="header">
    <h1>旅游路线详情</h1>
        <a href="#" onclick="history.go(-1)" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>
<img src="${ctx}/images/TravelTop.png" style="width:100%;" />
<div class="TravelFont">
    ${tour.content}
   </div>
<div class="clearfloat" style="padding:10px 15px;background:#fff;">
    <label class="list-label" style="height:30px;line-height:25px;font-size:16px;width:50%;float:left;">出游时间：</label>
    <div class="list-text form-select" style="width:50%;float:left;">
        ${sel}
    </div>
</div>
<div class="TravelDis clearfloat">
    <div class="TravelDisTime"><input type="hidden" name="tourTimeid" value="${tourTimeVo.id}"> <p>${tourTimeVo.beginTimeStr} ${tourTimeVo.weekStr}</p><p class="TravelDisTimecolor">原价:<del>￥${tourTimeVo.fee}</del></p></div>
</div>
<c:choose>
    <c:when test="${tourUser.auditStatus == 1}">
        <div class="MyApply" >审核中</div>
    </c:when>
    <c:when test="${tourUser.auditStatus == 3}">
        <div class="MyApply" >已生效</div>
    </c:when>
    <c:when test="${tourUser.auditStatus == 4}">
        <div class="MyApply" >已完成</div>
    </c:when>
    <c:when test="${tourUser.auditStatus == 5}">
        <div class="MyApply" >审核失败</div>
    </c:when>
</c:choose>

<script>
    $(function(){
        $("img").parent("p").css("padding","0");
    })
//    $(function(){
//        $(document).on("click",".TravelDisTime",function(){
//            $(this).css("background","#f15b00");
//
//            $(this).find("p").css("color","#fff");
//            $(this).find("p.TravelDisTimecolor").css("color","#fff");
//            tourTimeid= $(this).find("input[name='tourTimeid']").val();
//            $(this).siblings(".TravelDisTime").css("background","#fff");
//            $(this).siblings(".TravelDisTime").find("p").css("color","#333");
//
//            $(this).siblings(".TravelDisTime").find("p.TravelDisTimecolor").css("color","#f15b00");
//        });
//    });
</script>
</body>
</html>
