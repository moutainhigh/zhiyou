<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/25
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/view/include/taglib.jsp" %>
<c:set var="nav" value="3"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title>活动详情</title>
    <%@ include file="/WEB-INF/view/include/head.jsp" %>
    <script type="text/javascript">

        $(function () {

        });

    </script>
    <style>
        .activity-image{
            position: relative;overflow: hidden;
            display: block;width: 100%;
        }
        .activity-image img{
            display: block; width: 100%;
        }
        .activity-apply-top{
            /*width: 385px;*/
            width: 100%;
            height: 70px;line-height: 25px;
        }
        .activity-apply-bottom{
            /*width: 385px;*/
            height: 25px;
            width: 100%;
        }
        .activity-apply-bottom  div:nth-child(1){
            float: left;
        }
        .activity-apply-bottom  div:nth-child(2),.activity-apply-bottom  div:nth-child(3){
            float: right;
        }
        .activity-apply-list .list-item::after{
            width: 90%;
        }
        .activity-apply-list .list-item:last-child::after{
            width: 100%;
        }
        .activity-apply-list .list-text{
            right: 28px;
        }
        .activity-information{
            /*width: 385px;*/
            width: 100%;
            height: 112px;
        }
        .activity-img{
            float: left; width: 51px; height: 51px;display: inline-block;
        }
        .activity-content{
            float: left;
            /*width: 285px;*/
            width:74% ;
            display: inline-block;
        }
        .activity-content h5{
            display: inline;
        }
        .activity-hoster{
            font-size: 10px;font-weight:100;text-align: center;
            width: 60px;height: 20px;line-height: 20px;
            background-color: lightblue;
        }
        .activity-attention{
            width: 100%;height: 35px;line-height: 35px;text-align: center;
        }
        .activity-attention a{
            color:royalblue;
        }
        .activity-get{
            height: 100px;width: 100%;
        }
        .activity-enroll{
            /*width: 385px;*/
            width: 100%;
            height: 43px;line-height: 43px;
        }
        .activity-enroll span{
            float: left;
        }
        .activity-enroll a{
            float: right;
        }
        .activity-line{
            /*width: 385px;*/
            width: 90%;
            height: 1px;
        }
        .activity-discuss-line{
            width: 90%;height: 1px;margin-right: 15px;margin-left: 15px;overflow: hidden;
        }
        .activity-discuss span{
            float: left;height: 43px;line-height: 43px;
         }
        .activity-discuss a{
            float: right;height: 43px;line-height: 43px;
        }
        .activity-images{
            width: 100%;
        }
        .activity-images a{
            display: inline;float: left;width: 40px;height: 71px;margin-left: 14px;
        }
        .activity-images a:nth-child(1){
            margin-left: 8px;
        }
        .activity-images img{
            width: 36px;height: 36px;
        }
        .activity-discuss-content{
            /*width: 280px;*/
            width: 72%;
            height: 127px;margin-left: 75px;position: relative;
        }
        .activity-discuss-content img{
            width: 35px;height: 35px;position: absolute;left: 0;top: 0;
            margin: 9px 6px 0 -45px;
        }
        .activity-discuss-content h5{
            float: left;
        }
        .activity-discuss-content span{
            float: right;
        }
        .activity-discuss-line{
            height: 1px;
        }
        /*.activity-discuss-list h5{*/
            /*display: inline;*/
        /*}*/
        .activity-talk{
            height: 36px; line-height: 36px; text-align: center;
            color: #666;
        }
        .activity-event{
            width: 100%;height: 44px;line-height:44px;
            padding-left:15px ;padding-right: 15px;
        }
        .activity-event span{
            float: left;
        }
        .activity-event a{
            float: right;
        }
        .activity-event-line{
            width: 90%;height: 1px;margin-left: 15px;margin-right: 15px;
        }
        .activity-event-content{
            width: 230px;height: 108px;
            padding-top: 16px;padding-bottom: 16px;
            position: relative;
            margin-left: 135px;
        }
        .activity-event-content img{
            display:block;width: 120px;height: 70px;position: absolute;left: -120px;
        }
        .activity-event-content h5{
            height: 39px;
        }
        .activity-event-content em{
            float: left;
        }
        .activity-event-content i{
            float: right;
        }
        .activity-btn{
            width: 100%;height: 50px;line-height:50px;border-bottom: 1px solid #dddddd;
            background-color: #efeff4;text-align: center;
        }
    </style>
</head>
<body>
    <a class="header-back" href=""><i class="fa fa-angle-left"></i></a>
    <article class="activity-wrap">
        <figure class="activity-image">
            <img  src="http://img1.hudongba.com/upload/_oss/userhead/yuantu/201608/2417/1472031915271.jpg">
        </figure>
        <acticle class="activity-apply bg-white">
            <h3 class="activity-apply-top pl-10 pr-10 pt-15 pb-5 bg-white">网赢研习社《创新网络营销总裁班》9-17杭州 免费报名中</h3>
            <div class="activity-apply-bottom pl-10 pr-10 font-gray bg-white">
                <div class="fs-11">8月5日</div>
                <div><i class="fa fa-angellist"></i><span class="fs-17 ml-5">666</span></div>
                <div class="mr-10"><i class="fa fa-angellist"></i><span class="fs-17 ml-5">555</span></div>
            </div>
        </acticle>
        <acticle class="activity-apply-list list-group mb-10">
            <a class="list-item" href="">
                <lable class="list-label fs-14"><i class="fa fa-heart-o mr-10"></i>09-17 09:00 至09-18 17:00</lable>
            </a>
            <a class="list-item" href="">
                <lable class="list-label fs-14"><i class="fa fa-heart-o mr-10"></i>浙江杭州拱墅区登云路518号</lable>
                <span class="list-text"><i class="list-arrow"></i></span>
            </a>
            <a class="list-item" href="">
                <lable class="list-label fs-14"><i class="fa fa-heart-o mr-10"></i>已报名103人/限120人报名</lable>
            </a>
            <a class="list-item" href="">
                <lable class="list-label fs-14"><i class="fa fa-heart-o mr-10"></i><em class="font-orange">免费</em></lable>
                <span class="list-text"><i class="list-arrow"></i></span>
            </a>
        </acticle>
        <div class="activity-information clearfix bg-white mb-10">
            <img class="activity-img bd round ml-15 mt-10" src="http://img.small.hudongba.com/upload/_cos/userhead/yuantu/201507/0713/j41vj1436246395996.jpg@!shop-head">
            <div class="activity-content pl-15 pt-10">
                <h5 class="mr-10">网赢研习社</h5><span class="activity-hoster">主办方</span>
                <div class="left fs-12 font-999 pt-5">网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完.</div>
                <div class="activity-attention flex fs-12 bg-white">
                    <a class="flex-1 bd-r fs-15">+关注</a>
                    <a class="flex-1 fs-15">联系Ta</a>
                </div>
            </div>
        </div>
        <article class="activity-get bg-gray">后台后台</article>
        <article class="activity-enroll  bg-white pl-15 pr-15">
            <span class="bg-white fs-14">已报名(105)</span><a class="bg-white  font-blue fs-12">更多报名></a>
        </article>
        <div class="activity-line  bg-gray ml-15 mr-15"></div>
        <div class="activity-images bg-white fs-12 clearfix pt-15 mb-10">
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/_oss/userhead/yuantu/201608/2819/ve0yh1472383936973.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/userhead/yuantu/201410/1307/2zc721413155130182.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/userhead/yuantu/201410/1307/2zc721413155130182.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/_oss/userhead/yuantu/201608/2819/ve0yh1472383936973.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/_oss/userhead/yuantu/201608/2819/ve0yh1472383936973.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/userhead/yuantu/201410/1307/2zc721413155130182.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
            <a href="" class=""><img class="round" src="http://img.small.hudongba.com/upload/userhead/yuantu/201410/1307/2zc721413155130182.jpg@!user-head"><em class="fs-10">孙自勤</em></a>
        </div>
        <article class="activity-discuss bg-white clearfix pl-15 pr-15">
            <span class="bg-white fs-14">评论(14)</span><a class="bg-white  font-blue fs-12">我要讨论</a>
        </article>
        <div class="activity-discuss-line bg-gray"></div>
        <div class="activity-discuss-list bg-white mb-10">
                <div class="activity-discuss-content">
                    <img class="" src="http://img.small.hudongba.com/upload/_cos/userhead/yuantu/201507/0713/j41vj1436246395996.jpg@!user-head">
                    <h5 class="font-blue pt-5">【主办方】 网赢研习社</h5><span class="fs-12 font-gray pt-10">8月6日</span>
                    <div class="fs-16 font-999 clear-both">回复<b class="fs-16 font-black">某人</b>网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死....</div>
                    <div class="activity-discuss-line bg-gray mt-5"></div>
                </div>
                <div class="activity-discuss-content">
                    <img class="" src="http://img.small.hudongba.com/upload/_cos/userhead/yuantu/201507/0713/j41vj1436246395996.jpg@!user-head">
                    <h5 class="font-blue pt-5">【主办方】 网赢研习社</h5><span class="fs-12 font-gray pt-10">8月6日</span>
                    <div class="fs-16 font-999 clear-both">回复<b class="fs-16 font-black">某人</b>网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死....</div>
                    <div class="activity-discuss-line bg-gray mt-5"></div>
                </div>
                <div class="activity-discuss-content">
                    <img class="" src="http://img.small.hudongba.com/upload/_cos/userhead/yuantu/201507/0713/j41vj1436246395996.jpg@!user-head">
                    <h5 class="font-blue pt-5">【主办方】 网赢研习社</h5><span class="fs-12 font-gray pt-10">8月6日</span>
                    <div class="fs-16 font-999 clear-both">回复<b class="fs-16 font-black">某人</b>网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死....</div>
                    <div class="activity-discuss-line bg-gray mt-5"></div>
                </div>
                <div class="activity-discuss-content">
                    <img class="round" src="http://img.small.hudongba.com/upload/_oss/userhead/yuantu/201608/0607/clhd11470439041332.jpg@!user-head">
                    <h5 class="font-blue pt-5">【主办方】 网赢研习社</h5><span class="fs-12 font-gray pt-10">8月6日</span>
                    <div class="fs-16 font-999 clear-both">回复<b class="fs-16 font-black">某人</b>网赢研习社，以助力传统企业互联网+时代转型致胜为使命，致力于帮助传统企业完成互联网转型的生死生死....</div>
                </div>
                <div class="fs-12 activity-talk">查看更多评论</div>
        </div>
    </article>
    <div class="activity-event fs-12 bg-white">
        <span class="bg-white fs-14">精选活动</span><a class="bg-white  font-blue fs-12">查看更多></a>
    </div>
    <div class="activity-event-list bg-white">
        <div class="activity-event-line bg-gray"></div>
        <div class="activity-event-content">
            <img class="pr-10" src="http://img.small.hudongba.com/upload/_oss/userhead/yuantu/201608/0311/1470196391589.jpg@!info-first-image-new">
            <h5 class="fs-14">无人机亲子飞行体验</h5>
            <p class="fs-12 font-gray">09-03 09:30 开始</p>
            <em class="fs-12 font-gray">徐家汇</em><i class="fs-16 font-orange">￥80</i>
        </div>
        <div class="activity-event-line bg-gray"></div>
        <div class="activity-event-content">
            <img class="pr-10" src="http://img.small.hudongba.com/upload/_oss/userpartyimg/201606/02/31464875198878_party3.jpg@!info-first-image-new">
            <h5 class="fs-14">无人机亲子飞行体验</h5>
            <p class="fs-12 font-gray">09-03 09:30 开始</p>
            <em class="fs-12 font-gray">徐家汇</em><i class="fs-16 font-orange">￥80</i>
        </div>
        <div class="activity-event-line bg-gray"></div>
        <div class="activity-event-content">
            <img class="pr-10" src="http://img.small.hudongba.com/upload/_oss/userhead/yuantu/201608/0311/1470196391589.jpg@!info-first-image-new">
            <h5 class="fs-14">无人机亲子飞行体验</h5>
            <p class="fs-12 font-gray">09-03 09:30 开始</p>
            <em class="fs-12 font-gray">徐家汇</em><i class="fs-16 font-orange">免费</i>
        </div>
        <div class="activity-event-line bg-gray"></div>
        <div class="activity-event-content">
            <img class="pr-10" src="http://img.small.hudongba.com/upload/_oss/userpartyimg/201606/02/31464875198878_party3.jpg@!info-first-image-new">
            <h5 class="fs-14">无人机亲子飞行体验</h5>
            <p class="fs-12 font-gray">09-03 09:30 开始</p>
            <em class="fs-12 font-gray">徐家汇</em><i class="fs-16 font-orange">免费</i>
        </div>
        <div class="activity-event-line bg-gray"></div>
        <div class="activity-event-content">
            <img class="pr-10" src="http://img.small.hudongba.com/upload/_oss/userpartyimg/201606/02/31464875198878_party3.jpg@!info-first-image-new">
            <h5 class="fs-14">无人机亲子飞行体验</h5>
            <p class="fs-12 font-gray">09-03 09:30 开始</p>
            <em class="fs-12 font-gray">徐家汇</em><i class="fs-16 font-orange">免费</i>
        </div>
    </div>
    <div class="activity-btn"><a>返回首页</a>    <a>登录</a></div>

</body>
</html>
