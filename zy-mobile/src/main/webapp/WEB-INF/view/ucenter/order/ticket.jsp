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
      width:80px;text-align: center;margin-left:20px;margin-top:10px;margin-bottom:10px;font-size:13px;background: #f86b3d;
      color: #fff;
    }
    .ticket_play {background: #ccc;}
  </style>
<script>
  $(function() {
       $('.miui-scroll-nav').scrollableNav();
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

         $(obj).attr("edit","edit");
         $(obj).text("编辑");
       }

  }
</script>
</head>
<body>
<body class="header-fixed footer-fixed">
  <header class="header">
    <h1>订单状态</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <span class="button-right" onclick="editSum(this)" edit="edit">编辑</span>
  </header>
  
  <article class="order-list">
    <!--没有订单时显示!-->
    <div class="page-empty" style="display: none;">
      <i class="fa fa-file-o"></i>
      <span>空空如也!</span>
    </div>
    <!--没有订单时!-->
    <div class="order bd-t bd-b">
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="">
          <a href="#" class="product-title">
               <div class="ticket_innor">深圳经济财富风暴</div>
               <div class="ticket_time">5月29日 09:00 开始</div>
               <div class="ticket_address">广东省 深圳市 罗湖区</div>
          </a>
          <div class="product-price abs-rt text-right">
            <div class="fs-15">¥ 238</div>
            <div class="fs-15 font-gray">x10</div>
          </div>
        </div>
      </div>
      <div class="order_div"></div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1 font-999 fs-12" style="line-height: 50px !important;"><span>下单时间：5月10号 18:30</span></div>
          <div class="ticket_button">查看票据</div>
          <div class="ticket_button ticket_play">支付成功</div>
        </div>
      </div>
    </div>

    <div class="order bd-t bd-b">
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="">
          <a href="#" class="product-title">
            <div class="ticket_innor">深圳经济财富风暴</div>
            <div class="ticket_time">5月29日 09:00 开始</div>
            <div class="ticket_address">广东省 深圳市 罗湖区</div>
          </a>
          <div class="product-price abs-rt text-right">
            <div class="fs-15">¥ 238</div>
            <div class="fs-15 font-gray none_grap">x10</div>
            <input type="text" class="fs-15 font-gray ticket-gray" value="10" />
          </div>
        </div>
      </div>
      <div class="order_div"></div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1 font-999 fs-12" style="line-height: 50px !important;"><span>下单时间：5月10号 18:30</span></div>
          <div class="ticket_button ticket_now">立即支付</div>
        </div>
      </div>
    </div>

    <div class="order bd-t bd-b">
      <div class="product-info pl-15 pr-15">
        <div class="product relative clearfix mt-5">
          <img class="product-image abs-lt" alt="" src="">
          <a href="#" class="product-title">
            <div class="ticket_innor">深圳经济财富风暴</div>
            <div class="ticket_time">5月29日 09:00 开始</div>
            <div class="ticket_address">广东省 深圳市 罗湖区</div>
          </a>
          <div class="product-price abs-rt text-right">
            <div class="fs-15">¥ 238</div>
            <div class="fs-15 font-gray none_grap">x10</div>
            <input type="text" class="fs-15 font-gray ticket-gray" value="10" />
          </div>
        </div>
      </div>
      <div class="order_div"></div>
      <div class="order-info pl-15 pr-15 mt-5 bdd-t">
        <div class="flex lh-30">
          <div class="flex-1 font-999 fs-12" style="line-height: 50px !important;"><span>下单时间：5月10号 18:30</span></div>
          <div class="ticket_button ticket_now">立即支付</div>
        </div>
      </div>
    </div>

  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
