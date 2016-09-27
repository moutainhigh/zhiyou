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
    
    $('.header .button-popmenu').click(function(){
      $('.header-popmenu').toggle(300);
    });
    
    //$.message('你好', 'info', 2);
    
    $('#deliverType0').click(function(){
      $('#logistics').slideUp(300);
    });
    $('#deliverType1').click(function(){
      $('#logistics').slideDown(300);
    });

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

    $('.image-single .image-item').imageupload({
      width : 120,
      height : 75,
    });
    
    $('.image-multi .image-item').imageupload();

    $('.image-multi .image-add').imageupload({
      isMultipart : true
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
      },
      submitHandler : function(form) {
        if($('input[name="image"]').length == 0) {
          messageFlash('请上传图片');
          return;
        }
        form.submit();
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
        btn : ['确定1', '确定2']
      });
    });
    
    $('.btn-dialog > a').eq(2).click(function(){
      $.dialog({
        content : '底部对话框，可以不要标题，点遮罩层消失',
        skin: 'footer',
        btn : ['确定1', '确定2'],
        callback : function(index){
          alert('按钮index=' + index);
          return true;
        }
      });
    });
    
    $('.miui-scroll-nav').scrollableNav();
    
  });
</script>
</head>
<body class="">

  <header class="header">
    <h1>MiUI</h1>
    <a href="${ctx}/" class="button-left"><i class="fa fa-home"></i></a>
    <a href="javascript:;" class="button-right button-popmenu"><i class="fa fa-ellipsis-h"></i></a>
    <nav class="header-popmenu hide">
      <a href="${ctx}/u"><i class="fa fa-user"></i>用户中心</a>
      <a href="${ctx}/superLogin/24"><i class="fa fa-sign-in"></i>一键登录</a>
      <a href="${ctx}/logout"><i class="fa fa-sign-out"></i>退出</a>
    </nav>
  </header>
  
  <nav class="miui-scroll-nav">
    <ul>
      <li>全部</li>
      <li class="current">分类1</li>
      <li>分类2</li>
      <li>分类3</li>
      <li>分类4</li>
      <li>分类5</li>
      <li>分类6</li>
      <li>分类7</li>
      <li>分类8</li>
    </ul>
  </nav>
  
  <article class="mt-10">
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
              <img src="${stccdn}/image/upload_240_150.png">
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
            <div class="image-add" data-limit="6" data-name="image">
              <input type="hidden" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>
      </div>
      
      <div class="list-group">
        <div class="list-title">图片布局</div>
        <div class="list-item">
          <div class="list-text list-image image-view" data-title="我的美照">
            <img src="${stccdn}/image/example/cube.png" data-src="${stccdn}/image/example/cube.png">
            <img src="${stccdn}/image/example/cube.png" data-src="${stccdn}/image/example/cube.png">
          </div>
        </div>
      </div>
      
    </form>
  </article>

  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
