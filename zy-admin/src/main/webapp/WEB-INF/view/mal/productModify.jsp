<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<%@ include file="/WEB-INF/view/include/editor.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  
  $(function() {

    $('#form').validate({
      rules : {
        'productPriceType' : {
          required : true
        }
      },
      messages : {},
      submitHandler: function (form) {
        var productPriceType = $('#productPriceType').find("option:selected").val();
        if(productPriceType == 0 ) {
          if(!$('#price').val()) {
            layer.alert('请填写价格');
            return false;
          }
        } else if(productPriceType == 2 ) {
          if(!$('#priceScript').val()) {
            layer.alert('请填写价格脚本');
            return false;
          }
        }
        $(form).find(':submit').prop('disabled', true);
        Layout.postForm(form);
      }
    });
    
    $('#productPriceType').change(function() {
      $('#priceDiv').addClass('hide');
      $('#priceScriptDiv').addClass('hide');
      
      var val = $(this).val();
      if(val == 0) {
        $('#priceDiv').removeClass('hide');
      } else if(val == 2) {
        $('#priceScriptDiv').removeClass('hide');
      }
    });
    
  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/product">商品管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-present"></i><span> 商品价格编辑</span>
        </div>
      </div>
      <div class="portlet-body form">

        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/product/modifyPrice" class="form-horizontal" method="post">
          <input type="hidden" name="id" value="${product.id}" />
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">商品名
              </label>
              <div class="col-md-4">
                <div class="form-control-static">
                  ${product.title}
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">商品价格类型<span class="required"> * </span>
              </label>
              <div class="col-md-4">
                <select name="productPriceType" id="productPriceType" class="form-control">
                  <option value="">-- 商品价格类型 --</option>
                  <option value="0"<c:if test="${product.productPriceType == '一般价格'}"> selected="selected"</c:if>>一般价格</option>
                  <option value="2"<c:if test="${product.productPriceType == '脚本价格'}"> selected="selected"</c:if>>脚本价格</option>
                </select>
              </div>
            </div>

            <div class="form-group <c:if test="${empty product.productPriceType or product.productPriceType == '脚本价格'}"> hide</c:if>" id="priceDiv">
              <label class="control-label col-md-3">价格
              </label>
              <div class="col-md-4">
                  <input type="text" id="price" class="form-control" name="price" value="${product.price}" placeholder="价格" />
              </div>
            </div>

            <div class="form-group<c:if test="${empty product.productPriceType or product.productPriceType == '一般价格'}"> hide</c:if>" id="priceScriptDiv">
              <label class="control-label col-md-3">价格脚本<span class="required"> * </span>
              </label>
              <div class="col-md-4">
                <textarea rows="26" cols="10" class="form-control" name="priceScript" id="priceScript">${product.priceScript}</textarea>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">原价
              </label>
              <div class="col-md-4">
                <div class="form-control-static">
                  ${product.marketPrice}
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">sku编码 </label>
              <div class="col-md-4">
                <div class="form-control-static">
                  ${product.skuCode}
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">商品图片</label>
              <div class="col-md-5">
                <div class="input-icon right">
                  <img data-target="image1" class="product-image bd" alt="商品主图 点击选择"
                    src="<c:if test="${not empty product.image1 }">${product.image1Big}</c:if><c:if test="${empty product.image1 }">${ctx}/image/upload_240_150.jpg</c:if>">
                  <input type="hidden" name="image1" value="${product.image1Big}" />
                </div>
              </div>
            </div>

          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/product">
                <i class="fa fa-chevron-left"></i> 返回
              </button>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
