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

  <title>上传检测报告</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <%@ include file="/WEB-INF/view/include/validate.jsp"%>
  <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
  <script src="${stc}/js/layer/layer.js"></script>
  <script src="${stccdn}/js/area.js"></script>
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
  </style>
  <script type="text/javascript">
    $(function() {
      var area = new areaInit('province', 'city', 'district', '${report.areaId}');

      //选择产品
      showProductList();

      $('.image-multi .image-add').imageupload({
        isMultipart : true
      });

      //验证
      $('.valid-form').validate({
        ignore: ':hidden',
        rules : {
          'realname' : {
            required : true
          },
          'gender' : {
            required : true
          },
          'age' : {
            required : true
          },
          'phone' : {
            required : true,
            phone : true
          },
          'times' : {
            required : true,
            digits : true,
            max: 999
          },
          'reportedDate' : {
            required : true
          },
          'areaId' : {
            required : true
          },
          'jobId' : {
            required : true
          },
          'reportResult' : {
            required : true
          },
          'text' : {
            required : true
          },
          'code' : {
            required : true
          },
          'image1' : {
            required : true
          },
          'idCardNumber' : {
            required : true
          },
          'birthday' : {
            required : true
          }
        },
        submitHandler : function(form) {
          if($('input[name="image"]').length < 1) {
            messageFlash('请至少上传一张图片');
            return;
          }
          var productId = $('#productId').val();
          if(!productId) {
            messageAlert('请选择产品');
            return;
          }
          form.submit();
        }
      });

      //选择产品
      $('.product-info').click(function(){
        showProductList();
      });

      $('body').on('click', '.product', function() {
        var $this = $(this);
        var product = {};
        product.id = $this.attr('data-id');
        product.title = $this.attr('data-title');
        product.image = $this.attr('data-image');
        setProduct(product);
        if(product.id == 2) {
          $('#policy').show();
        } else {
          $('#policy').hide();
          $('#hasPolicy').removeAttr('checked');
        }
      });

      //是否选择保单
      $('#hasPolicy').click(function() {
        var checked = $(this).is(':checked');
        //messageFlash(checked);
        if(checked) {
          $('#policyInfo').slideDown(300);
        } else {
          $('#policyInfo').slideUp(300);
        }
      });

      $('.image-single').imageupload({
        width : 120,
        height : 75,
      });

      $('.image-view').click(function() {
        var url = $(this).attr('data-src');
        var title = $(this).attr('data-title');
        $.imageview({
          url : url,
          title : title
        });
      });

    });

    function showProductList() {
      var html = document.getElementById('productListTpl').innerHTML;
      pushDialog(html);
    }

    function hideProductList() {
      pullDialog('#productList', function(){
        $('#productList').remove();
      });
    }

    function setProduct(product) {
      hideProductList();
      $('#productId').val(product.id);
      $('.product-info .list-unit').html('<span>' + product.title + '</span>');
      $('.product-info .list-icon').removeClass('list-icon').html('<img class="image-40" src="' + product.image + '">');
    }

  </script>
  <script id="productListTpl" type="text/html">
    <aside id="productList" class="abs-lt size-100p bg-gray zindex-1000">
      <header class="header">
        <h1>选择商品</h1>
        <a href="javascript:hideProductList();" class="button-left"><i class="fa fa-angle-left"></i></a>
      </header>
      <div class="list-group">
        <c:forEach items="${products}" var="product">
          <div class="list-item product" data-id="${product.id}" data-title="${product.title}" data-image="${product.image1Thumbnail}">
            <img class="image-60 mr-10" src="${product.image1Thumbnail}">
            <div class="list-text">${product.title}</div>
          </div>
        </c:forEach>
      </div>
    </aside>
  </script>
</head>
<div class="main">
  <header class="header">
    <h1>上传检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <article>
    <form action="${ctx}/u/report/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-title">请选择产品</div>
        <div class="list-item product-info">
          <label class="list-text lh-36">选择产品</label>
          <div class="list-icon"></div>
          <div class="list-unit"></div>
          <i class="list-arrow"></i>
          <input id="productId" type="hidden" name="productId" value="">
        </div>
      </div>

      <div class="list-group mt-10">
        <div class="list-title">填写客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div class="list-text">
            <input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写客户姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="男"<c:if test="${report.gender eq '男'}"> selected="selected"</c:if>>男</option>
              <option value="女"<c:if test="${report.gender eq '女'}"> selected="selected"</c:if>>女</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">地区</label>
          <div class="list-text">
            <div class="flex">
              <div class="form-select flex-1">
                <select name="" id="province">
                  <option value="">省</option>
                </select>
              </div>
              <div class="form-select flex-1">
                <select name="" id="city">
                  <option value="">市</option>
                </select>
              </div>
              <div class="form-select flex-1">
                <select name="areaId" id="district">
                  <option value="">区</option>
                </select>
              </div>
            </div>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">职业</label>
          <div class="list-text form-select">
            <select name="jobId">
              <option value="">请选择</option>
              <c:forEach items="${jobs}" var="job">
                <option value="${job.id}"${job.id eq report.jobId ? ' selected="selected"' : ''}>${job.jobName}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="age">年龄</label>
          <div class="list-text">
            <input type="number" name="age" id="age" class="form-input" value="${report.age}" placeholder="填写客户年龄">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div class="list-text">
            <input type="number" name="phone" id="phone" class="form-input" value="${report.phone}" placeholder="填写客户手机号">
          </div>
        </div>
      </div>

      <div class="list-group">
        <div class="list-title">检测信息，至少需要上传1张检测图片</div>
        <div class="list-item">
          <label class="list-label" for="times">检测次数</label>
          <div class="list-text">
            <input type="number" name="times" id="times" class="form-input" value="${report.times}" placeholder="第几次检测">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="date">检测日期</label>
          <div class="list-text">
            <input type="text" id="date" name="reportedDate" class="form-input" value="" placeholder="填写检测时间 2001-01-01" onfocus="this.type='date'">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">检测结果</label>
          <div class="list-text form-select">
            <select name="reportResult">
              <option value="">请选择</option>
              <option value="阴性"<c:if test="${report.reportResult eq '阴性'}"> selected="selected"</c:if>>阴性</option>
              <option value="弱阳性"<c:if test="${report.reportResult eq '弱阳性'}"> selected="selected"</c:if>>弱阳性</option>
              <option value="阳性"<c:if test="${report.reportResult eq '阳性'}"> selected="selected"</c:if>>阳性</option>
              <option value="干扰色"<c:if test="${report.reportResult eq '干扰色'}"> selected="selected"</c:if>>干扰色</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <div class="image-add" data-limit="6" data-name="image">
              <input type="hidden" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>
      </div>
      <div class="list-group">
        <div class="list-title">填写产品使用心得</div>
        <div class="list-item">
          <div class="list-text">
            <textarea name="text" class="form-input" rows="3" placeholder="填写产品使用心得">${report.text}</textarea>
          </div>
        </div>
      </div>
      <div class="form-btn" style="padding-bottom: 50px;">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>
    </form>
  </article>
  <div class="footer" >
    <div onclick="insurance()">保险申请</div>
    <div onclick="travel()" style="border-right: none;">旅游申请</div>
  </div>
</div>

<script>

  //保险申请
  var insuranceT,travelT,line,MyApply;
  function insurance(){
    insuranceT=layer.open({
      type: 1,
      title: false,
      closeBtn: 0,
      scrollbar: false,
      area:['100%', '100%'],
      skin: 'yourclass',
      content:'<header class="header"><h1>填写保险申请</h1><a href="#" onclick="hideBtn()" class="button-left"><i class="fa fa-angle-left"></i></a></header><div id="policy" class="list-group"><div id="policyInfo">'
            +'<div class="list-item bd-t-0"><label class="list-label" for="code">产品编码</label><div class="list-text"><input type="text" name="code" id="code" class="form-input" value="" placeholder="填写产品编码">'
            +'</div></div><div class="list-item"><label class="list-label" for="idCardNumber">身份证号</label><div class="list-text"><input type="text" name="idCardNumber" id="idCardNumber" class="form-input" value="${policy.idCardNumber}" placeholder="填写身份证号">'
            +'</div></div><div class="list-item"><label class="list-label">正面照</label><div class="list-text image-upload"><div class="image-item image-single "><input type="hidden" name="image1" id="image1" value=""><div id="preview"><img src="${stccdn}/image/upload_240_150.png" class="previewImage" style="width:100px;height:100px;"></div>'
            +'<input type="file" onchange="fileChange(this)" style="z-index:999;"/></div></div><div class="list-unit"><a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_1.jpg" data-title="身份证正面"><i class="fa fa-question-circle-o"></i> 示意图</a>'
            +'</div></div><label style="margin-left: 20px; color:red;">(非必填)上传身份证图片可方便核验您的身份证号码的准确性</label><div class="list-item"><label class="list-label">生日</label><div class="list-text"><input type="date" name="birthday" class="form-input" value="" placeholder="填写生日  1900-01-01"></div></div></div></div>'
            +'<div class="form-btn" style="padding-bottom: 50px;"><input class="btn orange btn-block round-2" type="submit" value="申 请" style="margin-bottom:10px;" onclick="submitBtn()"><div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideBtn()">取 消</div></div>'
  });
    $(".main").hide();
  }
  function fileChange(obj){
    var file = obj.files[0];
    preview(file);
  }
  function preview(file) {
    var img = new Image(), url = img.src = URL.createObjectURL(file)
    var $img = $(img)
    img.onload = function() {
      URL.revokeObjectURL(url);
      $('#preview .previewImage').remove();
      $('#preview').empty().append($img);
    }
  }
  //提交保险申请
  function submitBtn(){

     layer.close(insuranceT);
     $(".main").show();
  }
  //取消保险申请
  function hideBtn(){
    layer.close(insuranceT);
    $(".main").show();
  }
  //旅游申请
  function travel(){
    travelT= layer.open({
      type: 2,
      area:['100%', '100%'],
      title: false,
      scrollbar: false,
      closeBtn: 0,
      content: '${ctx}/tour/findparentInfo'
    });

    /*travelT=layer.open({
      type: 1,
      title: false,
      closeBtn: 0,
      shadeClose: true,
      skin: 'yourclass',
      content: '<header class="header"><h1>填写旅游申请</h1><a href="#" onclick="hideTravel()" class="button-left"><i class="fa fa-angle-left"></i></a></header><div id="policy" class="list-group"><div id="policyInfo">'
               +'<div style="margin-left: 5%;margin-bottom: 20px;margin-top: 20px;"><span>填写推荐人手机号：</span><input type="number" style="height: 30px;width:50%;" /></div>'
               +'<div class="form-btn" style="padding-bottom: 50px;"><input class="btn orange btn-block round-2" type="submit" value="确 认" style="margin-bottom:10px;" onclick="submitTravel()"><div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideTravel()">取 消</div></div>'
    });
    $(".main").hide();*/
  }
  //确认旅游申请
  function submitTravel(){
    layer.close(travelT);
    line=layer.open({
      type: 1,
      title: false,
      closeBtn: 0,
      shadeClose: true,
      skin: 'yourclass',
      content:'${ctx}/tour/findTourUserVo'
    });
  }

  //取消旅游申请
  function hideTravel(){
    layer.close(travelT);
    $(".main").show();
  }
  function hideLine(){
    layer.close(line);
    travel();
  }
  <%--//点击线路弹出旅游详情--%>
  <%--var traveldetil="";--%>
  <%--var numBack=0;--%>
  <%--function TravelDetil(num){--%>
    <%--layer.close(line);--%>
    <%--if(traveldetil!=""){--%>
      <%--layer.close(traveldetil);--%>
    <%--}--%>
    <%--switch(num)--%>
    <%--{--%>
      <%--case 1:--%>
        <%--numBack=1;--%>
        <%--traveldetil=layer.open({--%>
          <%--type: 1,--%>
          <%--title: false,--%>
          <%--closeBtn: 0,--%>
          <%--shadeClose: true,--%>
          <%--skin: 'yourclass',--%>
          <%--content: '<header class="header"><h1>旅游路线详情</h1><a href="#" onclick="hideDetil()" class="button-left"><i class="fa fa-angle-left"></i></a></header>'--%>
          <%--+'<img src="${ctx}/images/TravelTop.png" style="width:100%;" /><div class="TravelFont"><div class="TravelFOne">预定须知</div><div class="TravelFOneD">旅游信息有几个限制条件：1、户籍所在地的限制，目前对四川、重庆不参与四川境内游活动，云南籍不参与云南境内游活动；2、年龄限制，年龄在28-58岁旅游费用全免，27岁以下与59岁至65岁者另加960，儿童另加960元且门票住宿自理。66岁以上老人及残疾人也不享受本活动。</div>'--%>
          <%--+'<div class="TravelFOne" style="margin-top:10px;">产品特色</div><div class="TravelFOneD">旅游信息有几个限制条件：1、户籍所在地的限制，目前对四川、重庆不参与四川境内游活动，云南籍不参与云南境内游活动；2、年龄限制，年龄在28-58岁旅游费用全免。</div></div>'--%>
          <%--+'<img src="${ctx}/images/TravelDetilT.png" style="width:100%;"/>'--%>
          <%--+'<div class="clearfloat" style="padding:10px 15px;background:#fff;"><label class="list-label" style="height:30px;line-height:25px;font-size:16px;width:50%;float:left;">请选择出游时间：</label><div class="list-text form-select" style="width:50%;float:left;"><select name="jobId" onchange="selectValue(this)"><option value="0">请选择</option><option value="1">一月</option><option value="2">二月</option></select></div></div>'--%>
          <%--+'<div class="TravelDis clearfloat"><div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div><div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div><div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div><div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div><div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div></div>'--%>
          <%--+'<div class="MyApply" onclick="MyApplyFun()">我要报名</div>'--%>
        <%--});--%>
        <%--break;--%>
      <%--case 2:--%>
        <%--numBack=2;--%>
        <%--traveldetil=layer.open({--%>
          <%--type: 1,--%>
          <%--title: false,--%>
          <%--closeBtn: 0,--%>
          <%--shadeClose: true,--%>
          <%--skin: 'yourclass',--%>
          <%--content: ''--%>
        <%--});--%>
        <%--break;--%>
      <%--case 3:--%>
        <%--break;--%>

      <%--case 4:--%>
        <%--break;--%>
    <%--}--%>
  <%--}--%>

  <%--//返回选择路线图页面，关闭路线图详情--%>
  <%--function hideDetil(){--%>
    <%--layer.close(traveldetil);--%>
    <%--submitTravel();--%>
  <%--}--%>
  <%--//点击我要报名--%>
  <%--function MyApplyFun(){--%>
    <%--layer.close(traveldetil);--%>
    <%--MyApply=layer.open({--%>
      <%--type: 1,--%>
      <%--title: false,--%>
      <%--closeBtn: 0,--%>
      <%--shadeClose: true,--%>
      <%--skin: 'yourclass',--%>
      <%--content: '<header class="header"><h1>旅游报名申请表单</h1><a href="#" onclick="hideApply()" class="button-left"><i class="fa fa-angle-left"></i></a></header>'--%>
                <%--+'<div class="list-group mt-10"><div class="list-title">旅游报名申请表单</div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">旅游路线</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写旅游路线"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">姓名</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写姓名"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">身份证号</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写身份证号"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">性别</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写性别"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">年龄</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写年龄"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">户籍城市</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写户籍城市"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">手机号码</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写手机号码"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">推荐人姓名</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写推荐人姓名"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">推荐人手机号</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写推荐人手机号"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">出游日期</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写出游日期"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">房型需求</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写房型需求"></div></div>'--%>
                <%--+'<div class="list-item"><label class="list-label" for="realname">特殊需求</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写特殊需求"></div></div>'--%>
                <%--+'<div class="MyApply" onclick="applyClick()">申请</div></div>'--%>
    <%--});--%>
  <%--}--%>
  <%--//点击报名申请表单中的返回--%>
  <%--function hideApply(){--%>
    <%--layer.close(MyApply);--%>
    <%--TravelDetil(numBack);--%>
  <%--}--%>
  <%--//点击申请按钮--%>
  <%--function applyClick(){--%>
    <%--layer.close(MyApply);--%>
    <%--$(".main").show();--%>
  <%--}--%>
</script>
</body>
</html>
