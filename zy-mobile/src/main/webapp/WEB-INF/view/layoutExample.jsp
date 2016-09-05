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
    $('#deliverType1').click(function() {
      $('#logistics').slideDown(300);
    });
    
    $('#deliverType0').click(function() {
      $('#logistics').slideUp(300);
    });
    
    $('.image-add').Fileupload({
      width : 120,
      height : 120,
      url : '${ctx}/image/upload',
      maxFileSize : '4MB',
      progress : function(){
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
        <div class="list-title">icon-label-text-unit</div>
        <div class="list-item">
          <div class="list-icon"><i class="icon icon-account"></i></div>
          <div class="list-label">我的年龄</div>
          <div class="list-text"><input class="form-input" type="text" value="" placeholder="请输入年龄"></div>
          <div class="list-unit"><em class="badge badge-danger">3</em>岁<i class="list-arrow"></i></div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-item">
          <div class="list-icon"><i class="fa fa-file-o font-red"></i></div>
          <div class="list-text">我的订单</div>
          <div class="list-unit"><em class="badge badge-danger">3</em><i class="list-arrow"></i></div>
        </div>
        <a class="list-item" href="javascript:alert('去填写地址');">
          <div class="list-icon"><i class="fa fa-map-marker font-orange"></i></div>
          <div class="list-text">我的地址</div>
          <div class="list-unit">未填写<i class="list-arrow"></i></div>
        </a>
      </div>
      
      <div class="list-group">
        <div class="list-title">请选择收货地址</div>
        <div class="list-item">
          <div class="list-label">姓名</div>
          <div class="list-text"><input class="form-input" type="text" value="" placeholder="请输入姓名"></div>
        </div>
        <div class="list-item">
          <div class="list-label">地区</div>
          <div class="list-text form-select">
            <select name="province" id="province">
              <option value="">请选择</option>
              <option value="">上海</option>
              <option value="">北京</option>
            </select>
          </div>
          <div class="form-error"><i class="fa fa-exclamation-circle"></i></div>
        </div>
        <div class="list-item">
          <div class="list-text">
            <textarea name="address" class="form-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息"></textarea>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">设为默认地址</div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_isDefault" value="false">
            <input type="checkbox" id="isDefault" name="isDefault" value="true">
            <label class="radius right" for="isDefault"></label>
          </div>
          <div class="form-error"><i class="fa fa-exclamation-circle"></i></div>
        </div>
        <div class="list-item">
          <div class="list-image">
            <div class="image-item">
              <img src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
              <input type="hidden" name="image1" value="">
            </div>
            <div class="image-item">
              <img src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
              <input type="hidden" name="image2" value="">
            </div>
            <div class="image-item">
              <img src="http://image.mayishike.com/image/65d50e00-a2b0-4fc4-bf6b-9c4e8f79bad8@240h_240w_1e_1c.jpg">
              <input type="hidden" name="image3" value="">
            </div>
            <div class="image-add" data-limit="6">
              <input type="hidden" name="image4" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">请选择发货方式</div>
        <label class="list-item form-radio" for="deliverType0">
          <div class="list-text">面对面发货</div>
          <div class="list-unit">
            <input id="deliverType0" type="radio" name="deliverType" value="0">
            <em class="i-checked"></em>
          </div>
        </label>
        <label class="list-item form-radio" for="deliverType1">
          <div class="list-text">物流发货</div>
          <div class="list-unit">
            <input id="deliverType1" type="radio" name="deliverType" value="1">
            <em class="i-checked"></em>
          </div>
        </label>
      </div>
      
      <div id="logistics" class="list-group hide">
        <div class="list-title">请填写您的物流信息</div>
        <div class="list-item">
          <label class="list-label" for="logisticsName">物流公司名</label>
          <input type="text" name="logisticsName" class="form-input" value="" placeholder="填写物流公司名">
        </div>
        <div class="list-item">
          <label class="list-label" for="logisticsNo">物流单号</label>
          <input type="text" name="logisticsNo" class="form-input" value="" placeholder="填写物流单号">
        </div>
      </div>
      
      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="确认发货">
      </div>
      
    </form>
  </article>

</body>
</html>
