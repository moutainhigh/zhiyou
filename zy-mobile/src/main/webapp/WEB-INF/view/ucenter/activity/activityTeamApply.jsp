<%@ page language="java" pageEncoding="UTF-8"%>
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

<title>票务订单</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />
  <style>
    .header .button-right   {  right:20px;  }
    .ticket_address,.ticket_time {
      font-size: 12px;
      color: #808080;
      line-height: 20px;
    }
    a {display: block;}
    .ticket_innor {  line-height: 25px; font-size:16px;width:190px !important;overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .font-gray {  margin-top:5px;  }
    .ticket-gray {
       margin-top:5px;width:80px !important;border:1px solid #ccc;display: none;
    }
    .order {margin-bottom:15px;}
    /*
    .ticket_button {width:80px;border:1px solid #333;text-align: center;margin-left:20px;margin-top:10px;margin-bottom:10px;font-size:13px;}
    .ticket_play {border:1px solid red;color:red;}
    */
    .ticket_button {
      width:80px;height:30px;text-align: center;margin-left:20px;margin-top:10px;margin-bottom:10px;font-size:13px;background: #f86b3d;
      color: #fff;
    }
    .ticket_play {background: #ccc;}
  </style>
<script>
  var count,hrefAct,hiddenNew,newHref;

  $(function() {
       $('.miui-scroll-nav').scrollableNav();
//        alert($(".hiddenActivity").val());//这个是href
//        alert($(".hiddenNew").val());//订单ID
//        alert($(".ticket_now").attr("href"));
        <%--hrefAct=$(".hiddenActivity").val();//4--%>
        <%--hiddenNew=$(".hiddenNew").val();--%>
        <%--newHref="${ctx}/u/activity/"+hrefAct+"/activityApply";--%>

//        for(var i=0;i<$(".ticket_now").length;i++){
//            count=$(".ticket-gray").eq(i).val();
//           $(".ticket_now").eq(i).attr("href",newHref+"?count="+count+"&id="+hiddenNew);
//        }



  });
  function editSum(obj){
       if($(obj).attr("edit")=="edit"){
           for(var i=0;i<$(".none_grap").length;i++){
             $(".none_grap").eq(i).siblings(".ticket-gray").css("display","block");
             $(".none_grap").eq(i).css("display","none");
           }
           $(".ticket-gray").eq(0).focus();
           $(obj).attr("edit","finish")
           $(obj).text("完成");

       }else {
         for(var i=0;i<$(".ticket-gray").length;i++){
           $(".ticket-gray").eq(i).siblings(".none_grap").css("display","block");
           $(".ticket-gray").eq(i).siblings(".none_grap").text("x"+$(".ticket-gray").eq(i).val());
           $(".ticket-gray").eq(i).css("display","none");

         }
         hrefAct=$(".hiddenActivity").val();//4
         hiddenNew=$(".hiddenNew").val();
         newHref="${ctx}/u/activity/"+hrefAct+"/activityApply";

         for(var i=0;i<$(".ticket_now").length;i++){
           count=$(".ticket-gray").eq(i).val();
           $(".ticket_now").eq(i).attr("href",newHref+"?count="+count+"&id="+hiddenNew);
         }
         $(obj).attr("edit","edit");
         $(obj).text("编辑");
       }

  }
  <%--function newPlay(obj){--%>
     <%--var text=${activity.id};--%>
     <%--var hrefNew=$(obj).parents(".order-info").siblings(".product-info").find(".ticket-gray").val();--%>
     <%--var amount=$(obj).parents(".order-info").siblings(".product-info").find(".amount span").html();--%>

     <%--window.location.href="${ctx}/u/activity/${activity.id}/activityApply?number="+hrefNew+"&aoumt="+amount+"&activityId="+text;--%>
  <%--}--%>
</script>
</head>
<body class="header-fixed footer-fixed">
  <header class="header">
    <h1>订单状态</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <%--<span class="button-right" onclick="editSum(this)" edit="edit">编辑</span>--%>
  </header>

  <article class="order-list">
    <c:if test="${empty activityTeamApplys}">
    <div class="page-empty" style="display: none;">
      <i class="fa fa-file-o"></i>
      <span>空空如也!</span>
    </div>
    </c:if>

    <c:if test="${not empty activityTeamApplys}">
    <c:forEach items="${activityTeamApplys}" var="activityTeamApply" >
      <%--有效订单待支付--%>
      <c:if test="${activityTeamApply.activity.status != '活动已结束' && activityTeamApply.paidStatus == '未支付'}">
        <div class="order bd-t bd-b">
        <div class="product-info pl-15 pr-15">
          <div class="product relative clearfix mt-5">

            <a href="${ctx}/activity/${activityTeamApply.activity.id}" class="product-title">
              <img class="product-image abs-lt" src="${activityTeamApply.activity.imageThumbnail}">
              <div class="ticket_innor">${activityTeamApply.activity.title}</div>
              <div class="ticket_time">${activityTeamApply.activity.startTimeLabel} 开始</div>
              <div class="ticket_address">${activityTeamApply.activity.province} ${activityTeamApply.activity.city} ${activityTeamApply.activity.district}</div>
            </a>
            <div class="product-price abs-rt text-right">
              <div class="fs-15 amount">¥ <span>${activityTeamApply.activity.amountLabel}</span></div>
              <div class="fs-15 font-gray none_grap">x${activityTeamApply.count}</div>
              <input type="number" class="fs-15 font-gray ticket-gray" value="${activityTeamApply.count}" />
            </div>
          </div>
        </div>
        <div class="order_div"></div>
        <div class="order-info pl-15 pr-15 mt-5 bdd-t">
          <div class="flex lh-30">
            <div class="flex-1 font-999 fs-12" style="line-height: 24px !important;"><span>下单时间：<br/>${activityTeamApply.createTimeLabel}</span></div>
            <a href="${ctx}/u/activity/${activityTeamApply.id}/activityTeamApply" class="ticket_button ticket_now">立即支付</a>
            <input type="hidden" value="${activityTeamApply.activity.id}" class="hiddenActivity"/>
            <input type="hidden" value="${activityTeamApply.id}" class="hiddenNew"/>
          </div>
        </div>
      </div>
      </c:if>
    </c:forEach>

      <c:forEach items="${activityTeamApplys}" var="activityTeamApply" >
      <%--有效订单已支付--%>
      <c:if test="${activityTeamApply.activity.status != '活动已结束' && activityTeamApply.paidStatus == '已支付'}">
      <div class="order bd-t bd-b">
        <div class="product-info pl-15 pr-15">
          <div class="product relative clearfix mt-5">
            <a href="${ctx}/activity/${activityTeamApply.activity.id}" class="product-title">
              <img class="product-image abs-lt"  src="${activityTeamApply.activity.imageThumbnail}">
              <div class="ticket_innor">${activityTeamApply.activity.title}</div>
                 <div class="ticket_time">${activityTeamApply.activity.startTimeLabel} 开始</div>
                 <div class="ticket_address">${activityTeamApply.activity.province} ${activityTeamApply.activity.city} ${activityTeamApply.activity.district}</div>
            </a>
            <div class="product-price abs-rt text-right">
              <div class="fs-15">¥ ${activityTeamApply.activity.amountLabel}</div>
              <div class="fs-15 font-gray">x${activityTeamApply.count}</div>

            </div>
          </div>
        </div>
        <div class="order_div"></div>
        <div class="order-info pl-15 pr-15 mt-5 bdd-t">
          <div class="flex lh-30">
            <div class="flex-1 font-999 fs-12" style="line-height: 24px !important;"><span>下单时间：<br/>${activityTeamApply.createTimeLabel}</span></div>
            <a href="${ctx}/u/activity/${activityTeamApply.id}/ticketList"><div class="ticket_button">查看票据</div></a>
            <div class="ticket_button ticket_play">支付成功</div>
          </div>
        </div>
      </div>
      </c:if>
      </c:forEach>

    <c:forEach items="${activityTeamApplys}" var="activityTeamApply" >
    <%--失效订单--%>
    <c:if test="${activityTeamApply.activity.status == '活动已结束'}">
      <div class="order bd-t bd-b">
        <div class="product-info pl-15 pr-15">
          <div class="product relative clearfix mt-5">
            <a href="${ctx}/activity/${activityTeamApply.activity.id}" class="product-title">
              <img class="product-image abs-lt" src="${activityTeamApply.activity.imageThumbnail}">
              <div class="ticket_innor">${activityTeamApply.activity.title}</div>
              <div class="ticket_time">${activityTeamApply.activity.startTimeLabel} 开始</div>
              <div class="ticket_address">${activityTeamApply.activity.province} ${activityTeamApply.activity.city} ${activityTeamApply.activity.district}</div>
            </a>
            <div class="product-price abs-rt text-right">
              <div class="fs-15 amount">¥ <span>${activityTeamApply.activity.amountLabel}</span></div>
              <div class="fs-15 font-gray none_grap">x${activityTeamApply.count}</div>
              <input type="number" class="fs-15 font-gray ticket-gray" value="${activityTeamApply.count}" />
            </div>
          </div>
        </div>
        <div class="order_div"></div>
        <div class="order-info pl-15 pr-15 mt-5 bdd-t">
          <div class="flex lh-30">
            <div class="flex-1 font-999 fs-12" style="line-height: 24px !important;"><span>下单时间：<br/>${activityTeamApply.createTimeLabel}</span></div>
            <c:if test="${activityTeamApply.activity.status == '报名中'}"><div class="ticket_button" ticket_play>报名中</div></c:if>
            <c:if test="${order.orderStatus == '待确认'}"> orange</c:if>
            <c:if test="${activityTeamApply.activity.status == '报名已结束'}"><div class="ticket_button ticket_play">报名已结束</div></c:if>
            <c:if test="${activityTeamApply.activity.status == '进行中'}"><div class="ticket_button ticket_play">进行中</div></c:if>
            <c:if test="${activityTeamApply.activity.status == '活动已结束'}"><div class="ticket_button ticket_play">活动已结束</div></c:if>
          </div>
        </div>
      </div>
    </c:if>
    </c:forEach>

    </c:if>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
