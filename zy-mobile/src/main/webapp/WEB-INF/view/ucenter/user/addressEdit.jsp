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
<meta name="keywords" content="微信分销" />
<meta name="description" content="修改收货地址" />

<title>修改收货地址</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script src="${stccdn}/js/area.js"></script>
<script>
  var areaId = '${address.areaId}';
  $(function() {
    var area = new areaInit('province', 'city', 'district', areaId/* , defaultAreaId */);

    $('.valid-form').validate({
      rules : {
        'realname' : {
          required : true
        },
        'phone' : {
          required : true,
          phone : true
        },
        'province' : {
          required : true
        },
        'city' : {
          required : true
        },
        'areaId' : {
          required : true
        },
        'address' : {
          required : true
        }
      },
      messages : {
        'realname' : {
          required : '请输入收货人姓名'
        },
        'phone' : {
          required : '请输入收货人手机号',
          phone : '请输入正确的手机号'
        },
        'province' : {
          required : '请选择省'
        },
        'city' : {
          required : '请选择市'
        },
        'areaId' : {
          required : '请选择区'
        },
        'address' : {
          required : '请输入详细地址'
        }
      }
    });

    $('#btnDelete').click(function() {
      if (window.confirm('您确认要删除该收货地址吗?') == 1) {
        location.href = '${ctx}/u/address/delete/${address.id}';
      }
    });

    $('.form-switch label').click(function() {
      var checked = $(this).prev().is(':checked');
      $('#isDefault').val(!checked);
    });
  });
</script>
</head>
<body class="">
  <header class="header">
    <h1>修改收货地址</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="javascript:;" id="btnDelete" class="button-right"><i class="fa fa-trash"></i></a>
  </header>
  <article class="address-edit">
    <form id="addressForm" class="valid-form" action="${ctx}/u/address/edit" method="post">
      <input type="hidden" name="id" value="${address.id}">
      <div class="form-message note note-warning mb-0 hide">
        <p>输入信息有误，请先更正。</p>
      </div>
      <div class="form-group">
        <div class="form-item">
          <label class="control-label" for="realname">收件人</label>
          <input type="text" id="realname" name="realname" class="control-input" value="${address.realname}"
            placeholder="填写收件人姓名">
        </div>
        <div class="form-item">
          <label class="control-label" for="phone">手机号码</label>
          <input type="tel" id="phone" name="phone" class="control-input" value="${address.phone}" placeholder="填写收件人手机号码">
        </div>
        <div class="form-item">
          <label class="control-label">省份</label>
          <div class="form-select">
            <select name="province" id="province">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="form-item">
          <label class="control-label">城市</label>
          <div class="form-select">
            <select name="city" id="city">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="form-item">
          <label class="control-label">地区</label>
          <div class="form-select">
            <select name="areaId" id="district">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="form-item input-cell">
          <textarea name="address" class="control-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息">${address.address}</textarea>
        </div>
        <div class="form-item form-switch">
          <input type="hidden" name="_isDefault" value="false">
          <input type="checkbox" id="isDefault" name="isDefault" value="true" <c:if test="${address.isDefault}"> checked="checked"</c:if>>
          <label class="radius right" for="isDefault"></label>
          <p class="form-switch-text">设为默认地址</p>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn orange btn-block" type="submit" value="保 存">
      </div>
    </form>
  </article>

</body>
</html>
