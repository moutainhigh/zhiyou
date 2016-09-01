<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<%@ include file="/WEB-INF/view/include/form.jsp" %>
<%@ include file="/WEB-INF/view/include/editor.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
	var ue;
	var detail = '${detail}';
	$(function () {

		ue = UE.getEditor('editor', {
			serverUrl: '${ctx}/editor',
			catchRemoteImageEnable: false,
			textarea: 'detail',
			enableAutoSave: false,
			contextMenu: [],
			saveInterval: 3600000,
			toolbars: [[
				'fullscreen',
				'bold', 'italic', 'underline', '|',
				'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify',
				'insertorderedlist', 'insertunorderedlist', '|',
				'simpleupload', 'insertimage'
			]],
			'insertorderedlist': {'num': '1,2,3...'},
			'insertunorderedlist': {'disc': ''},
			elementPathEnabled: false,     //去掉元素路径
			pasteplain: true,   //是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
			iframeCssUrl: '${ctx}/css/editor/default.css'
		});

		ue.addListener("ready", function () {
			ue.setContent(detail);
		});

		var mutex = (function () { //互斥上传类型
			var m = {};
			return {
				set: function (t, xhr) {
					m.xhr && m.xhr.abort();
					m.type = t;
					xhr && (m.xhr = xhr);
				},
				is: function (t) {
					return m.type === t;
				},
				get: function () {
					return m.type;
				}
			}
		})();

		$('#createForm').validate({
			rules: {
				'title': {
					required: true
				},
				'price': {
					required: true
				},
				'image1': {
					required: true
				}
			},
			messages: {
				'title': {
					required: '请输入名称'
				},
				'price': {
					required: '请输入价格'
				},
				'image1': {
					required: '请上传商品主图'
				}
			},
			submitHandler: function (form) {
				var content = ue.getContent();
				if (!content) {
					alert('请填写详情');
					return false;
				}
				$(form).find(':submit').prop('disabled', true);
				Layout.postForm(form);
			}
		});

		var uploader = new ss.SimpleUpload({
			button: $.makeArray($('.product-image')),
			url: '${ctx}/image/upload',
			name: 'file',
			maxSize: 4096,
			responseType: 'json',
			allowedExtensions: ['jpg', 'jpeg', 'png', 'gif', 'webp'],
			onSubmit: function (filename, extension, uploadBtn, fileSize) {
				$(uploadBtn).data('origin', $(uploadBtn).attr('src'));
				$(uploadBtn).attr('src', '${stccdnThsuan}/image/loading_image.gif');
			},
			onComplete: function (filename, response, uploadBtn, fileSize) {
				if (response.code == 0) {
					$(uploadBtn).attr('src', response.data);
					$('input[name="' + $(uploadBtn).data('target') + '"]').val(response.data);
				} else {
					$(uploadBtn).attr('src', $(uploadBtn).data('origin'));
					layer.alert('上传失败' + response.message);
				}
			},
			onError: function (filename, errorType, status, statusText, response, uploadBtn, fileSize) {
				$(uploadBtn).attr('src', $(uploadBtn).data('origin'));
				layer.alert('上传失败' + errorType);
			},
			onSizeError: function (filename, fileSize) {
				layer.alert('图片大小超过4MB限制');
			},
			onExtError: function (filename, extension) {
				layer.alert('图片文件格式错误, 仅限*.jpg, *.jpeg, *.png, *.gif, *.webp');
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
		<div class="portlet box yellow">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-edit"></i><span> 编辑商品</span>
				</div>
				<div class="tools">
					<a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
				</div>
			</div>
			<div class="portlet-body form">

				<!-- BEGIN FORM-->
				<form id="createForm" action="" data-action="${ctx}/product/update" class="form-horizontal" method="post">
					<input type="hidden" name="id" value="${product.id}"/>
					<div class="form-body">
						<div class="alert alert-danger display-hide">
							<i class="fa fa-exclamation-circle"></i>
							<button class="close" data-close="alert"></button>
							<span class="form-errors">您填写的信息有误，请检查。</span>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">商品名<span class="required"> * </span>
							</label>
							<div class="col-md-4">
								<input type="text" id="name" class="form-control" name="title" value="${product.title}" placeholder="商品名"/>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">价格<span class="required"> * </span>
							</label>
							<div class="col-md-4">
								<input type="text" id="price" class="form-control" name="price" value="${product.price}" placeholder="价格"/>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">原价<span class="required"> * </span>
							</label>
							<div class="col-md-4">
								<input type="text" id="price" class="form-control" name="marketPrice" value="${product.marketPrice}" placeholder="原价"/>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">sku编码
							</label>
							<div class="col-md-4">
								<input type="text" id="skuCode" class="form-control" name="skuCode" value="${product.skuCode}" placeholder="sku编码"/>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">库存数量
							</label>
							<div class="col-md-4">
								<input type="text" id="skuCode" class="form-control" name="stockQuantity" value="${product.stockQuantity}" placeholder="库存数量"/>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">锁定数量
							</label>
							<div class="col-md-4">
								<input type="text" id="skuCode" class="form-control" name="lockedCount" value="${product.lockedCount}" placeholder="锁定数量"/>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">商品图片<span class="required"> * </span></label>
							<div class="col-md-5">
								<div class="input-icon right">
									<img data-target="image1" class="product-image image-80 bd" alt="商品主图 点击选择"
									     src="<c:if test="${not empty product.image1 }">${product.image1 }</c:if><c:if test="${empty product.image1 }">${ctx}/image/image_default_80x80.png</c:if>">
									<input type="hidden" name="image1" value="${product.image1}"/>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">图片</label>
							<div class="col-md-5">
								<div class="input-icon right">
									<img data-target="image2" class="product-image image-80 bd" alt="商品主图 点击选择"
									     src="<c:if test='${not empty product.image2 }'>${product.image2}</c:if><c:if test='${empty product.image2 }'>${ctx}/image/image_default_80x80.png</c:if> ">
									<input type="hidden" name="image2" value="${product.image2}"/>
									<img data-target="image3" class="product-image image-80 bd" alt="商品主图 点击选择"
									     src="<c:if test='${not empty product.image3 }'>${product.image3}</c:if><c:if test='${empty product.image3 }'>${ctx}/image/image_default_80x80.png</c:if>">
									<input type="hidden" name="image3" value="${product.image3}"/>
									<img data-target="image4" class="product-image image-80 bd" alt="商品主图 点击选择"
									     src="<c:if test='${not empty product.image4 }'>${product.image4}</c:if><c:if test='${empty product.image4 }'>${ctx}/image/image_default_80x80.png</c:if> ">
									<input type="hidden" name="image4" value="${product.image4}"/>
									<img data-target="image5" class="product-image image-80 bd" alt="商品主图 点击选择"
									     src="<c:if test='${not empty product.image5 }'>${product.image5}</c:if><c:if test='${empty product.image5 }'>${ctx}/image/image_default_80x80.png</c:if> ">
									<input type="hidden" name="image5" value="${product.image5}"/>
									<img data-target="image6" class="product-image image-80 bd" alt="商品主图 点击选择"
									     src="<c:if test='${not empty product.image6 }'>${product.image6}</c:if><c:if test='${empty product.image6 }'>${ctx}/image/image_default_80x80.png</c:if> ">
									<input type="hidden" name="image6" value="${product.image6}"/>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-3">商品详情<span class="required"> * </span>
							</label>
							<div class="col-md-4">
								<div>
									<script id="editor" type="text/plain" style="width:840px; height:500px;"></script>
								</div>
							</div>
						</div>

					</div>
					<div class="form-actions fluid">
						<div class="col-md-offset-3 col-md-9">
							<button type="submit" class="btn green">保存</button>
							<button class="btn default" data-href="${ctx}/product">
								<i class="fa fa-arrow-left"></i> 返回
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
