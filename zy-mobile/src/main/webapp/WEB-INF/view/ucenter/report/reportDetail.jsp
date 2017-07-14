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

<title>检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stc}/js/layer/layer.js"></script>
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
      display: none;
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
    .TravelDisTimecolor {
      color: #f15b00;
    }
    #preview img{
      max-width:100%;
      max-height:100%;
      position: absolute;
      top: 50%;
      left:50%;
      -webkit-transform:translate(-50%,-50%);
      transform:translate(-50%,-50%);
    }
    .layer-class {
      -webkit-overflow-scrolling: touch;
      overflow-y: scroll;
    }
    .TravelDisTimecolor {
      color: #f15b00;
    }

  </style>
<script>

  $(function() {
    function change() {
      if ($(".list-unit").html() == "优检一生") {
        $(".footer").hide();
        $(".footer").css("padding-bottom", "50px");
      }
    }
    change();
    $('.image-view').click(function() {
      var images = $(this).find('img');
      if (images.length == 0) {
        var url = $(this).attr('data-src');
        var title = $(this).attr('data-title');
        $.imageview({
          url : url,
          title : title
        });
      } else {
        var title = $(this).attr('data-title');
        var imageUrls = [];
        $.each(images, function(n, image) {
          imageUrls.push($(image).attr('data-src'));
        })
        $.imageview({
          url : imageUrls,
          title : title
        });
      }
    });
    
  });
  
</script>
</head>
<div class="reportDetail">
  <header class="header">
    <h1>检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
    ${report.preConfirmStatus}
    <c:if test="${report.preConfirmStatus == '待审核'}">
    <a href="${ctx}/u/report/edit?id=${report.id}" class="button-right"><i class="fa fa-edit"></i></a>
    </c:if>
  </header>
  
  <article>
    <c:if test="${report.confirmStatus == '待审核'}">
    <div class="note note-warning mb-0">
      <i class="fa fa-clock-o"></i> 审核信息：待审核
    </div>
    </c:if>
    <c:if test="${report.confirmStatus == '未通过'}">
    <div class="note note-danger mb-0">
      <i class="fa fa-close"></i> 审核信息：未通过, ${report.confirmRemark}
    </div>
    </c:if>
    <c:if test="${report.confirmStatus == '已通过'}">
    <div class="note note-success mb-0">
      <i class="fa fa-check"></i> 审核信息：已通过  | 审核时间: ${report.confirmedTimeLabel}
    </div>
    </c:if>
  
    <%-- 结算信息 --%>
    <c:if test="${report.isSettledUp && not empty profits}">
    
    <div class="list-group mt-10">
      <div class="list-title">我的收益</div>
      <c:forEach items="${profits}" var="profit">
      <div class="list-item">
        <i class="list-icon fa fa-cny"></i>
        <div class="list-text">${profit.profitType}</div>
        <div class="list-unit font-orange">${profit.amount}元</div>
      </div>
      </c:forEach>
      <c:forEach items="${transfers}" var="transfer">
      <div class="list-item">
        <i class="list-icon fa fa-cny"></i>
        <div class="list-text">${transfer.transferType}</div>
        <div class="list-unit font-orange">${transfer.amount}元</div>
      </div>
      </c:forEach>
    </div>
    
    </c:if>
    
    <div class="list-group mt-10">
      <div class="list-title">产品信息</div>
      <div class="list-item">
        <label class="list-text lh-36">产品</label>
        <img class="image-40 mr-10" src="${report.product.image1Thumbnail}">
        <div class="list-unit">${report.product.title}</div>
      </div>
    </div>
  
    <div class="list-group">
      <div class="list-title">客户资料</div>
      <div class="list-item">
        <div class="list-text">客户姓名</div>
        <div class="list-unit">${report.realname}</div>
      </div>
      <div class="list-item">
        <div class="list-text">性别</div>
        <div class="list-unit">${report.gender}</div>
      </div>
      <div class="list-item">
        <div class="list-text">年龄</div>
        <div class="list-unit">${report.age}岁</div>
      </div>
      <div class="list-item">
        <div class="list-text">手机号</div>
        <div class="list-unit">${report.phone}</div>
      </div>
      <div class="list-item">
        <div class="list-text">检测时间</div>
        <div class="list-unit">${report.reportedDateLabel}</div>
      </div>
      <div class="list-item">
        <div class="list-text">地区</div>
        <div class="list-unit">${report.province} ${report.city} ${report.district}</div>
      </div>
      <div class="list-item">
        <div class="list-text">职业</div>
        <div class="list-unit">${report.jobName}</div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">检测信息</div>
      <div class="list-item">
        <div class="list-text">检测次数</div>
        <div class="list-unit">第 ${report.times} 次检测</div>
      </div>
      <div class="list-item">
        <div class="list-text">检测结果</div>
        <div class="list-unit">
          <c:choose>
            <c:when test="${report.reportResult == '阴性'}">
            <span class="font-red">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '弱阳性'}">
            <span class="font-orange">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '阳性'}">
            <span class="font-green">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '干扰色'}">
            <span class="font-purple">${report.reportResult}</span>
            </c:when>
          </c:choose>
        </div>
      </div>
      <div class="list-item">
        <div class="list-text list-image image-view" data-title="检测报告图片">
        <c:forEach items="${report.images}" var="image" varStatus="varStatus">
          <img src="${report.imageThumbnails[varStatus.index]}" data-src="${report.imageBigs[varStatus.index]}">
        </c:forEach>
        </div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">客户使用心得</div>
      <div class="list-item">
        <div class="list-text font-777">${report.text}</div>
      </div>
      <div class="footer" >
        <div onclick="insurance('${report.id}')" style="border-right: none;border-right: 1px solid #fff;box-sizing: border-box;">保险申请</div>
        <div onclick="travel('${report.id}')" style="border-right: none;">旅游申请</div>
      </div>
    </div>
  </article>
</div>

  <!--填写旅游申请代码如下!-->
  <div class="policyInfoMain" style="display: none;">
    <header class="header"><h1>填写旅游申请</h1><a href="#" onclick="hideTravel()" class="button-left"><i class="fa fa-angle-left"></i></a></header>
    <article>
      <form class="valid-form" method="post" action="${ctx}/tour/findTourApple">
        <div id="policy" class="list-group">
          <div id="policyInfo">
            <div class="list-item">
              <label class="list-label" for="phone1">推荐人电话：</label>
              <div class="list-text">
                <input type="number" name="phone1" id="phone1" class="form-input" value="${parentPhone}" placeholder="填写推荐人手机号" ${parentPhone!=null?'readonly':''}>
              </div>
            </div>
            <%--<div class="list-text" style="margin-left: 5%;margin-bottom: 20px;margin-top: 20px;">
                <span>填写推荐人手机号：</span><input type="number" style="height: 30px;width:50%;" class="form-input" value="${parentPhone}" ${parentPhone!=null?'readonly':''} id="phone" name="phone" placeholder="填写客户手机号"/>
            </div>--%>
            <div class="form-btn" style="padding-bottom: 50px;">
              <input class="btn orange btn-block round-2" type="button" value="确 认" style="margin-bottom:10px;" onclick="submitTravel()">
              <div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideTravel()">取 消</div>
            </div>
          </div>
        </div>
      </form>
    </article>
  </div>


  <!--选择旅游路线代码如下!-->
  <from class="tourApply" style="display: none;">
    <header class="header"><h1>选择旅游路线</h1><a href="#" onclick="tourApply()"  class="button-left"><i class="fa fa-angle-left"></i></a></header>
    <input type="hidden" name="phone" id="parentPhone" value="${parentPhone}" />
    <input type="hidden" name="reporId" id="reporId" value="${reporId}" />
    <%--<c:forEach items="${tourList}" var="tour">--%>
    <%--<a href="#" onclick="TravelDetil('${tour.id}')" class="opacityAll" style="width:100%;position:relative;">--%>
    <a href="#" onclick="TravelDetil('1')" class="opacityAll" style="width:100%;position:relative;">
      <img class="opacityFirst" src="${tour.image}" style="display:block;width:100%;" />
      <img src="${ctx}/images/opacityTwo.png" class="opacity" style="display:block;width:100%;z-index:9;" />
      <p class="font">【${tour.title}】</p>
    </a>
    <%--</c:forEach>--%>
  </from>
<script>
  var reportId="";
  //保险申请
  function insurance(id){
    reportId=id;
    $.ajax({
      url : '${ctx}/tour/ajaxCheckReport',
      data :{
        'reportId':reportId
      },
      dataType : 'json',
      type : 'POST',
      success : function(result){
        if(result.code == 0){
          travelT= layer.open({
            type: 2,
            area:['100%', '100%'],
            title: false,
            scrollbar: false,
            closeBtn: 0,
            content: '${ctx}/u/report/insuranceInfo?reportId='+ reportId + '',
            skin: 'layer-class'
          });
        } else {
          messageAlert(result.message);
        }
      }
    });
  }
  //旅游申请
  function travel(id) {
    reportId=id;
    $.ajax({
      url : '${ctx}/tour/ajaxCheckTour',
      data :{
        'reportId':reportId
      },
      dataType : 'json',
      type : 'POST',
      success : function(result){
        if(result.code == 0){
          $(".reportDetail").hide();
          $(".policyInfoMain").show();
          <%--travelT= layer.open({--%>
            <%--type: 2,--%>
            <%--area:['100%', '100%'],--%>
            <%--title: false,--%>
            <%--scrollbar: true,--%>
            <%--shadeClose:true,--%>
            <%--closeBtn: 0,--%>
            <%--content: '${ctx}/tour/findparentInfo',--%>
            <%--success: function(layero){--%>
              <%--$(".layui-layer-content").addClass("scroll-wrapper");--%>
<%--//              $(layero).addClass("scroll-wrapper");//苹果 iframe 滚动条失效解决方式--%>
            <%--}--%>
          <%--});--%>
        } else {
          messageAlert(result.message);
        }
      }
    });
  }
  // 取消/返回旅游申请
  function hideTravel(){
      $(".reportDetail").show();
      $(".policyInfoMain").hide();
  }
  //旅游申请确定
  function submitTravel(){
    $(".policyInfoMain").hide();
    $(".tourApply").show();
  }
  //选择旅游路返回
  function tourApply(){
    $(".policyInfoMain").show();
    $(".tourApply").hide();
  }
  //选择旅游路
  function TravelDetil(num){

  }
  /**
   * 获取rteportId
   * @returns {*}
   */
  function getReportId(){
    return reportId;
  }

</script>
</body>
</html>
