<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<%@ include file="/WEB-INF/view/include/editor.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<style>
	#img{
		cursor: pointer;
	}
</style>
 <script>
 var ue;
 ue = UE.getEditor('editor', {
	  serverUrl: '${ctx}/editor',
		catchRemoteImageEnable: false,
		textarea: 'content',
	    enableAutoSave: false,
	    contextMenu:[],
	    saveInterval: 3600000,
		toolbars:  [[
		   'fullscreen',  
		   'bold', 'italic', 'underline', '|', 
          'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', 
		   'insertorderedlist', 'insertunorderedlist', '|',
		   'simpleupload'
		 ]],
		 'insertorderedlist':{'num':'1,2,3...' },
		 'insertunorderedlist':{'disc' : ''},
		 elementPathEnabled : false,     //去掉元素路径
		 pasteplain: true,   //是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
		 iframeCssUrl: '${ctx}/css/editor/default.css' 
	});
	$(function() {

		$('#createForm').validate({
			rules : {
				title : {
					required : true
				},
				content : {
					required : true
				},
				noticeType : {
					required : true
				}
			},
			messages : {
				title : {
					required : "请输入标题"
				},
				content : {
					required : "请输入内容"
				},
				noticeType : {
					required : "请选择代理类型"
				}
			}
		});
		
		$('#createForm').submit(function(){
			return true;
		});
	   
	});
	 
</script> 
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
	<ul class="page-breadcrumb">
		<li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
		<li><a href="javascript:;" data-href="${ctx}/notice">公告管理</a></li>
	</ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
	<div class="col-md-12">
		<!-- BEGIN VALIDATION STATES-->
		<div class="portlet box green">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-plus-circle"></i><span> 创建公告</span>
				</div>
				<div class="tools">
					<a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
				</div>
			</div>
			<div class="portlet-body form">
				<!-- BEGIN FORM-->
				<form id="createForm" action="" data-action="${ctx}/notice/create" class="form-horizontal" method="post">
					<div class="form-body">
						<div class="alert alert-danger display-hide">
							<i class="fa fa-exclamation-circle"></i>
							<button class="close" data-close="alert"></button>
							<span class="form-errors">您填写的信息有误，请检查。</span>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">标题<span class="required"> * </span></label>
							<div class="col-md-5">
								<div class="input-icon right">
									<i class="fa"></i> <input type="text" class="form-control" name="title" id="title" value="" />
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-3">类型<span class="required"> * </span></label>
							<div class="col-md-5">
								<div class="input-icon right">
									<select name="noticeType" id="noticeType" class="form-control">
					               		<option value="">--请选择公告类型-- </option>
					               		<c:forEach items="${noticeTypes}" var="noticeType" >
					               			<option value="${noticeType}">${noticeType}</option>
					               		</c:forEach>
					                  </select>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-3">内容<span class="required"> * </span>
							</label>
							<div class="col-md-5">
								<div class="input-icon right">
									<script id="editor" type="text/plain" style="width:840px; height:500px;"></script>
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions fluid">
						<div class="col-md-offset-3 col-md-9">
							<button type="submit" class="btn green">
								<i class="fa fa-plus"></i> 保存</button>
							<button class="btn default" data-href="${ctx}/notice">
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
