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
            border-bottom: 2px solid #ff7f00;
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
            padding:20px 10px 20px 20px;
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
            border:1px solid #ff7f00;
            -webkit-border-radius:15px;
            -moz-border-radius:15px;
            border-radius:15px;
            padding:5px 10px 5px 10px;
            margin-left: 10px;
            font-size: 12px;
            color: #ff7f00;
        }
        a {display: block;}
        .tourFont {
            float: left;
            width:200px;
            margin-left: 10px;
            margin-top: 15px;
            overflow : hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 4;    /* 数值代表显示几行 */
            -webkit-box-orient: vertical;
        }
        .startState {
            margin-top: 20px;
        }
        @media (device-height:568px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone5 */
            .tourFont {
                float: left;
                width:150px;
                margin-left: 10px;
                margin-top: 10px;
                overflow : hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 3;    /* 数值代表显示几行 */
                -webkit-box-orient: vertical;
            }
            .tourImageT img {
                width:130px;
            }
        }
        @media (device-height:667px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone6 */
            .tourFont {
                float: left;
                width:170px;
                margin-left: 10px;
                margin-top: 15px;
            }

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
                 <p class="tourArticle"><b>【青岛】有山有水好风光——青岛三日游</b></p>
                 <div class="tourImageT clearfloat">
                     <img src="${ctx}/images/tourImageT1.png" />
                     <p class="startTime">出发时间：2017-02-07</p>
                     <p class="startTime startState">审核中</p>
                     <%--<p class="startTime">待补充</p>--%>
                     <%--<p class="startTime">可编辑</p>--%>
                     <%--<p class="startTime">已参加</p>--%>
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
