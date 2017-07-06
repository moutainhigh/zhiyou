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

    <title>旅游申请</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
    <script src="${stccdn}/js/area.js"></script>
    <style>
        .tourNum {
            float: left;
            width:25%;
            height:40px;
            background: #fff;
            text-align: center;
            line-height: 40px;
            font-size: 16px;
            box-sizing: border-box;
        }
        .on {
            border-bottom: 2px solid #6cb92f;
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
        .tourArticle {
            border-top:1px solid #ccc;
            padding:10px 10px 20px 20px;
            background: #fff;
            color: #333;
            font-size: 16px;
        }
        .tourImageT {
            padding:0px 10px 20px 20px;
            background: #fff;
        }
        .tourImageT img {
            float:left;width: 160px;
        }
        .startTime {
            float:left;
            margin-left: 10px;
            font-size: 12px;
            color: #333;
            margin-left: 10px;
            margin-top: 15px;
        }
        a {display: block;}

        .startState {
            float: left;
            width:100px;
            height:35px;
            border:1px solid #6cb92f;
            margin-left: 10px;
            margin-top: 15px;
            text-align: center;
            line-height: 35px;
            color: #6cb92f;
            -webkit-border-radius:5px;
            -moz-border-radius:5px;
            border-radius:5px;
            font-size: 18px;
        }
        .tourImageTFont {
            float: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-left: 10px;
        }
        .startStateFile {
            color: #cccccc;
            border:1px solid #cccccc;
        }
        @media (device-height:568px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone5 */

        }
        @media (device-height:667px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone6 */

        }
    </style>
</head>
<div class="main">
    <header class="header">
        <h1>旅游申请</h1>
        <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
    <%--<div class="tourAll clearfloat">--%>
    <%--<div class="tourNum">审核中</div>--%>
    <%--<div class="tourNum on">待补充</div>--%>
    <%--<div class="tourNum">可编辑</div>--%>
    <%--<div class="tourNum">已参加</div>--%>
    <%--</div>--%>
    <article>
            <a href="${ctx}/tour/addInfo" class="tourArticleAll">
                 <p class="tourArticle"></p>
                 <div class="tourImageT clearfloat">
                     <img src="${ctx}/images/tourImageT1.png" />
                     <p class="tourImageTFont"><b>【昆明】大理丽江健康之旅</b></p>
                     <p class="startTime">出发时间：2017-02-07</p>
                     <%--&lt;%&ndash;&lt;%&ndash;<p class="startState">审核中</p>   //跳到旅游详情页面，并且把我要报名按钮改成审核中--%>
                     <%--<p class="startState">待补充</p>   跳到<a href="${ctx}/tour/addInfo" class="tourArticleAll">--%>
                     <%--<p class="startState">可生效</p>  //跳到旅游详情页面，并且把我要报名按钮改成可生效--%>
                     <%--<p class="startState">已完成</p>   //跳到旅游详情页面，并且把我要报名按钮改成已完成--%>
                     <p class="startState startStateFile">审核失败</p>
                     <%--<div class="tourFont">青岛旅游，说白了就是海和崂山 如今的崂山分为七大风景区，但 是对于外来游客来讲，真正值如今的崂山分为七大风景区</div>--%>
                 </div>
            </a>

    </article>

</div>
<script>
$(function(){
    $(".tourNum").click(function(){
        $(this).addClass("on");
        $(this).siblings(".tourNum").removeClass("on");
    });
})
</script>
</body>
</html>
