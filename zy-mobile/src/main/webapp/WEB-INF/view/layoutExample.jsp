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

<title>list-group布局</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/fileupload.jsp"%>
<script type="text/javascript">
  $(function() {
    $('#deliverType0').click(function(){
        $('#logistics').slideUp(300);
    });
    $('#deliverType1').click(function(){
      $('#logistics').slideDown(300);
  });

    $('.image-view').click(function() {
      var url = $(this).attr('data-src');
      var title = $(this).attr('data-title');
      if (!url) {
        return;
      }
      $.imageview({
        url : url,
        title : title
      });
    });

    $('.image-single-upload').Fileupload({
      width : 120,
      height : 75,
      url : '${ctx}/image/upload',
      maxFileSize : '4MB'
    });

    $('.image-add').Fileupload({
      width : 120,
      height : 120,
      url : '${ctx}/image/upload',
      maxFileSize : '4MB',
      progress : function() {
        var $this = $(this);
        $this.find('.state').removeClass('state-add').addClass('state-loading');
      },
      success : function(result) {
        var $this = $(this);
        $this.find('.state').removeClass('state-loading').addClass('state-add');
        var limit = $this.attr('data-limit');
        var images = $this.siblings('.image-item');
        var inputHidden = $this.children('input:hidden');
        var inputName = inputHidden.val('').attr('name');
        inputName = inputName.replace((images.length + 1), '');
        var imageItem = '<div class="image-item">' + '<input type="hidden" name="' + inputName + (images.length + 1) + '" value="' + result + '">'
            + '<img src="' + result + '@200w_200h_1e_1c_jpg">' + '<input type="file">' + '</div>';
        $(imageItem).insertBefore($this);
        $this.children('input:hidden').attr('name', inputName + (images.length + 2));
        if (limit && limit <= images.length + 1) {
          $this.remove();
        }
        $this.siblings('.image-item').eq(images.length).Fileupload({
          width : 120,
          height : 120,
          url : '${ctx}/image/upload',
          maxFileSize : '4MB'
        });
      }
    });
  });
</script>
</head>
<body class="">
  <header class="header">
    <h1>List 布局</h1>
    <a href="${ctx}/u/order" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/order/deliver" class="valid-form" method="post">
      <input type="hidden" name="orderId" value="${orderId}">
      <div class="list-group">
        <div class="list-title">列表布局</div>
        <!-- icon-label-text-unit -->
        <div class="list-item">
          <i class="list-icon icon icon-account"></i>
          <div class="list-text">昵称</div>
          <div class="list-unit">哆来嘧</div>
        </div>
        <div class="list-item">
          <i class="list-icon fa fa-file-o font-red"></i>
          <div class="list-text">订单数</div>
          <div class="list-unit"><em class="badge badge-danger">3</em></div>
          <i class="list-arrow"></i>
        </div>
        <a class="list-item" href="javascript:alert('去填写地址');">
          <i class="list-icon fa fa-map-marker font-orange"></i>
          <div class="list-text">收货地址</div>
          <div class="list-unit">未填写</div>
          <i class="list-arrow"></i>
        </a>
      </div>
      
      <div class="list-group">
        <div class="list-title"><span class="font-red mr-10">表单</span>请选择收货地址</div>
        <div class="list-item">
          <label class="list-label" for="name">姓名</label>
          <div class="list-text"><input id="name" name="name" class="form-input" type="text" value="" placeholder="请输入姓名"></div>
          <div class="form-error"><i class="fa fa-exclamation-circle"></i></div>
        </div>
        <div class="list-item">
          <label class="list-label" for="age">年龄</label>
          <div class="list-text form-quantity">
            <i class="quantity-min fa fa-minus disabled"></i>
            <input id="age" name="age" class="form-input" type="number" value="" placeholder="请输入年龄">
            <i class="quantity-add fa fa-plus"></i>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="level">等级</label>
          <div class="list-text form-range">
            <input type="range" id="level" name="level" min="0" max="5" value="2" step="1" onchange="$('#levelVal').text($(this).val());">
          </div>
          <div class="list-unit fs-16 font-333">
            <output id="levelVal">2</output> 级
          </div>
        </div>
        <div class="list-item img-captcha">
          <label for="captcha" class="list-label">图形码</label>
          <div class="list-text">
            <input type="text" id="captcha" name="captcha" class="form-input" placeholder="图形验证码">
          </div>
          <div class="list-unit">
            <img id="captchaImage" src="${ctx}/captcha">
          </div>
        </div>
        <div class="list-item phone-captcha">
          <label for="smsCode" class="list-label" style="z-index: 9">手机验证码</label>
          <div class="list-text">
            <input type="text" id="smsCode" name="smsCode" class="form-input" placeholder="手机验证码" value="">
          </div>
          <div class="list-unit">
            <a id="btnSend" class="btn blue btn-sm">发送验证码</a>
          </div>
        </div>
        <!-- form-select -->
        <div class="list-item">
          <label class="list-label" for="province">地区</label>
          <div class="list-text form-select">
            <select name="province" id="province">
              <option value="">请选择</option>
              <option value="">上海</option>
              <option value="">北京</option>
            </select>
          </div>
          <div class="form-error"><i class="fa fa-exclamation-circle"></i></div>
        </div>
        <!-- form-input/textarea -->
        <div class="list-item">
          <div class="list-text">
            <textarea name="address" class="form-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息"></textarea>
          </div>
        </div>
        <!-- form-switch -->
        <div class="list-item">
          <div class="list-text">设为默认地址</div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_isDefault" value="false">
            <input type="checkbox" id="isDefault" name="isDefault" value="true">
            <label class="i-switch" for="isDefault"></label>
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">checkbox 复选</div>
        <!-- form-checkbox -->
        <div class="list-item form-checkbox">
          <div class="list-icon">
            <input type="checkbox" id="checkbox0" name="checkname" value="0">
            <label class="i-checked" for="checkbox0"></label>
          </div>
          <label class="list-text" for="checkbox0">使用平台礼品</label>
        </div>
        <div class="list-item form-checkbox">
          <div class="list-icon">
            <input type="checkbox" id="checkbox1" name="checkname" value="1">
            <label class="i-checked" for="checkbox1"></label>
          </div>
          <label class="list-text" for="checkbox1">使用平台物流</label>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">radio 单选</div>
        <!-- form-radio -->
        <div class="list-item form-radio">
          <label class="list-text" for="deliverType0">面对面发货</label>
          <div class="list-unit">
            <input id="deliverType0" type="radio" name="deliverType" value="0">
            <label class="i-checked" for="deliverType0"></label>
          </div>
        </div>
        <div class="list-item form-radio">
          <label class="list-text" for="deliverType1">物流发货</label>
          <div class="list-unit">
            <input id="deliverType1" type="radio" name="deliverType" value="1">
            <label class="i-checked" for="deliverType1"></label>
          </div>
        </div>
      </div>
      
      <div id="logistics" class="list-group hide">
        <div class="list-title">请填写您的物流信息</div>
        <div class="list-item">
          <label class="list-label" for="logisticsNo">物流单号</label>
          <div class="list-text">
            <input type="text" name="logisticsNo" class="form-input" value="" placeholder="填写物流单号">
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">图片上传插件</div>
        <div class="list-item">
          <label class="list-label">单个图片</label>
          <div class="list-text list-image">
            <div class="image-item image-single-upload bd">
              <input type="hidden" name="infoImage2" id="infoImage2" value="">
              <img src="${stccdn}/image/defaultImage_240_150.png">
              <input type="file">
            </div>
          </div>
          <div class="list-unit">
            <a href="javascript:;" class="image-view font-blue fs-14" data-src="http://image.mayishike.com/image/1e27766d-62b3-496a-b442-15f512caaff5" data-title="淘宝截图示例图"><i class="fa fa-question-circle-o"></i> 示意图</a>
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title"><span class="font-red">多图</span>上传插件</div>
        <div class="list-item">
          <div class="list-text list-image">
            <div class="image-item">
              <img src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
              <input type="hidden" name="image1" value="">
            </div>
            <div class="image-item">
              <img src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
              <input type="hidden" name="image2" value="">
            </div>
            <div class="image-add" data-limit="6">
              <input type="hidden" name="image3" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">图片布局</div>
        <div class="list-item">
          <div class="list-text list-image">
            <div class="image-item">
              <img class="image-view" data-title="照片名字" data-src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@640h_640w_1e_1c.jpg" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
            </div>
            <div class="image-item">
              <img class="image-view" data-title="我的美照" data-src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@640h_640w_1e_1c.jpg" src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
            </div>
          </div>
        </div>
      </div>
      
      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="确认发货">
      </div>
      
    </form>
  </article>

</body>
</html>
