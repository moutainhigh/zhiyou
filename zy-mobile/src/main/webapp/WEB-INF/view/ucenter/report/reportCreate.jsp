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

<title>上传检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script src="${stccdn}/js/area.js"></script>
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
        <c:if test="${userRank == 'V0' && empty parent}">
        var parentPhone = $('#parentPhone').val();
        if(!parentPhone) {
          messageFlash('请填写上级手机号');
          return;
        }
        $.ajax({
          url: '${ctx}/u/checkPhone',
          data: {
            phone: parentPhone
          },
          type: 'POST',
          dataType: 'JSON',
          success: function(result){
            if(result.code == 0) {
              $('[name="parentId"]').val(result.message);
              form.submit();
            } else {
              messageAlert(result.message);
            }
          }
        });
        return;
        </c:if>
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
<body>
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
            <input type="text" name="realname" class="form-input" value="${report.realname}" placeholder="填写客户姓名">
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
            <input type="number" name="age" class="form-input" value="${report.age}" placeholder="填写客户年龄">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div class="list-text">
            <input type="number" name="phone" class="form-input" value="${report.phone}" placeholder="填写客户手机号">
          </div>
        </div>
        <c:if test="${userRank == 'V0' && empty parent}">
          <div class="list-item">
            <label class="list-label">上级手机号</label>
            <div class="list-text">
              <input id="parentPhone" name="parentPhone" class="form-input" type="tel" value="${inviter.phone}" placeholder="输入上级服务商手机号">
              <input type="hidden" name="parentId" value="${inviter.id}">
            </div>
          </div>
        </c:if>
      </div>

      <div class="list-group">
        <div class="list-title">检测信息，至少需要上传1张检测图片</div>
        <div class="list-item">
          <label class="list-label" for="times">检测次数</label>
          <div class="list-text">
            <input type="number" name="times" class="form-input" value="${report.times}" placeholder="第几次检测">
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
      
      <div id="policy" class="list-group" style="display: none;">
        <div class="list-title">保险</div>
        <div class="list-item">
          <div class="list-text">是否申请保险</div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_hasPolicy" value="false">
            <input type="checkbox" id="hasPolicy" name="hasPolicy">
            <label class="i-switch" for="hasPolicy"></label>
          </div>
        </div>
        <div id="policyInfo" class="hide">
          <div class="list-item bd-t-0">
            <label class="list-label" for="code">产品编码</label>
            <div class="list-text">
              <input type="text" name="code" class="form-input" value="" placeholder="填写产品编码">
            </div>
          </div>
          <div class="list-item">
            <label class="list-label" for="idCardNumber">身份证号</label>
            <div class="list-text">
              <input type="text" name="idCardNumber" class="form-input" value="${policy.idCardNumber}" placeholder="填写身份证号">
            </div>
          </div>
          <div class="list-item">
            <label class="list-label">生日</label>
            <div class="list-text">
              <input type="date" name="birthday" class="form-input" value="" placeholder="填写生日  1900-01-01">
            </div>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>

    </form>
  </article>

</body>
</html>
