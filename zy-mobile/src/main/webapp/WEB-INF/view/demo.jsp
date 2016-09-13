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

<title>MiUI</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
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

    $('.image-single .image-item').imageupload({
      width : 120,
      height : 75,
    });
    
    $('.image-multi .image-item').imageupload();

    $('.image-multi .image-add').imageupload({
      width : 100,
      height : 100,
      url : '${ctx}/image/upload',
      maxFileSize : '4MB',
      success : function(result) {
        var $this = $(this);
        var limit = $this.attr('data-limit');
        var imageItems = $this.siblings('.image-item');
        var inputHidden = $this.children('input:hidden');
        var inputName = inputHidden.val('').attr('name');
        inputName = inputName.replace((imageItems.length + 1), '');
        var image = result.image;
        var imageThumbnail = result.imageThumbnail;
        var imageItem = '<div class="image-item">' + '<input type="hidden" name="' + inputName + (imageItems.length + 1) + '" value="' + image + '">'
            + '<img src="' + imageThumbnail + '">' + '<input type="file">' + '</div>';
        $(imageItem).insertBefore($this);
        $this.children('input:hidden').attr('name', inputName + (imageItems.length + 2));
        if (limit && limit <= imageItems.length + 1) {
          $this.remove();
        }
        $this.siblings('.image-item').eq(imageItems.length).imageupload({
          width : 100,
          height : 100,
          url : '${ctx}/image/upload',
          maxFileSize : '4MB'
        });
      }
    });
    
    $('.valid-form').validate({
      rules : {
        'name' : {
          required : true
        },
        'province' : {
          required : true
        },
        'captcha' : {
          required : true
        },
        'address' : {
          required : true
        },
        'simage' : {
          required : true
        }
      }
    });
    
    $('.btn-dialog > a').eq(0).click(function(){
      $.dialog({
        content : '提示信息，2秒后消失',
        skin: 'message',
        timeout : 2
      });
    });
    
    $('.btn-dialog > a').eq(1).click(function(){
      $.dialog({
        content : '中间显示对话框，可自定义按钮',
        skin: 'center',
        btn : ['确定1', '确定2'],
        btnCancel: true
      });
    });
    
    $('.btn-dialog > a').eq(2).click(function(){
      $.dialog({
        content : '底部对话框，可以不要标题，点遮罩层消失',
        skin: 'footer',
        btn : ['确定1', '确定2'],
        btnCancel: true
      });
    });
  
  });
</script>
</head>
<body class="">
  <header class="header">
    <h1>MiUI</h1>
    <a href="${ctx}/" class="button-left"><i class="fa fa-home"></i></a>
    <a href="${ctx}/superLogin/11" class="button-right"><i class="fa fa-user"></i></a>
  </header>
  
  <article>
    <div class="list-title">信息提示</div>
    <div class="btn-dialog form-btn flex">
      <a class="flex-1 btn red round-2">提示信息</a>
      <a class="flex-1 btn blue round-2 ml-5">询问框</a>
      <a class="flex-1 btn purple round-2 ml-5">底部对话框</a>
    </div>
      
    <form action="${ctx}/u" class="valid-form" method="get">
      <div class="list-group">
        <div class="list-title">列表布局</div>
        <!-- icon-label-text-unit -->
        <div class="list-item">
          <i class="list-icon fa fa-user"></i>
          <div class="list-text">版本</div>
          <div class="list-unit">v 1.0</div>
        </div>
        <div class="list-item">
          <i class="list-icon fa fa-file-o"></i>
          <div class="list-text">用户数</div>
          <div class="list-unit"><em class="badge red">99+</em></div>
        </div>
        <a class="list-item" href="javascript:messageFlash('为移动web开发量身定制..');">
          <i class="list-icon fa fa-map-marker font-orange"></i>
          <div class="list-text">兼容性</div>
          <div class="list-unit">还不错</div>
          <i class="list-arrow"></i>
        </a>
      </div>
      
      <div class="form-message note note-warning hide">
        <p>输入信息有误，请先更正。</p>
      </div>
      
      <div class="list-group">
        <div class="list-title"><span class="font-red mr-10">validate 表单</span>完善您的资料</div>
        <div class="list-item">
          <label class="list-label" for="name">姓名</label>
          <div class="list-text"><input id="name" name="name" class="form-input" type="text" value="" placeholder="请输入姓名"></div>
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
            <input type="text" id="captcha" name="captcha" class="form-input" placeholder="图形验证码" value="">
          </div>
          <div class="list-unit">
            <img id="captchaImage" src="${ctx}/captcha">
          </div>
        </div>
        <div class="list-item phone-captcha">
          <label for="smsCode" class="list-label">手机验证码</label>
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
        </div>
        <!-- form-input/textarea -->
        <div class="list-item">
          <div class="list-text">
            <textarea name="address" class="form-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息"></textarea>
          </div>
        </div>
        <!-- form-switch -->
        <div class="list-item">
          <div class="list-text">是否接受更新提醒<small>switch开关</small></div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_isDefault" value="false">
            <input type="checkbox" id="isDefault" name="isDefault" value="true">
            <label class="i-switch" for="isDefault"></label>
          </div>
        </div>
      </div>
      
      <div class="form-btn">
        <button type="submit" class="btn orange btn-block round-2"><i class="fa fa-check"></i> 提 交</button>
      </div>
      
      <div class="list-group">
        <div class="list-title">checkbox 复选</div>
        <!-- form-checkbox -->
        <div class="list-item form-checkbox">
          <div class="list-icon">
            <input type="checkbox" id="checkbox0" name="checkname" value="0" checked="checked">
            <label class="i-checked" for="checkbox0"></label>
          </div>
          <label class="list-text" for="checkbox0">使用HTML5</label>
        </div>
        <div class="list-item form-checkbox">
          <div class="list-icon">
            <input type="checkbox" id="checkbox1" name="checkname" value="1" checked="checked">
            <label class="i-checked" for="checkbox1"></label>
          </div>
          <label class="list-text" for="checkbox1">使用CSS3</label>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">radio 单选</div>
        <!-- form-radio -->
        <div class="list-item form-radio">
          <label class="list-text" for="deliverType0">不使用物流</label>
          <div class="list-unit">
            <input id="deliverType0" type="radio" name="deliverType" value="0" checked="checked">
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
          <div class="list-text image-upload image-single">
            <div class="image-item">
              <input type="hidden" name="simage" id="simage" value="">
              <img src="${stccdn}/image/defaultImage_240_150.png">
              <input type="file">
            </div>
          </div>
          <div class="list-unit">
            <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/cube.png" data-title="淘宝截图示例图"><i class="fa fa-question-circle-o"></i> 示例图</a>
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title"><span class="font-red">多图</span>上传插件</div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <div class="image-item">
              <img src="${stccdn}/image/example/cube.png">
              <input type="hidden" name="image1" value="">
              <input type="file">
            </div>
            <div class="image-item">
              <img src="${stccdn}/image/example/cube.png">
              <input type="hidden" name="image2" value="">
              <input type="file">
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
            <img class="image-view" data-title="照片名字" data-src="${stccdn}/image/example/cube.png" src="${stccdn}/image/example/cube.png">
            <img class="image-view" data-title="我的美照" data-src="${stccdn}/image/example/cube.png" src="${stccdn}/image/example/cube.png">
          </div>
        </div>
      </div>
      
    </form>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
