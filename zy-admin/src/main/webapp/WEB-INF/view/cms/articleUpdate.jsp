<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<%@ include file="/WEB-INF/view/include/editor.jsp"%>
<script type="text/javascript">
//编辑器
	var ue;
	var articleContent = '${article.content}';
	$(function() {
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
		
		ue.addListener("ready", function () {
	        ue.setContent(articleContent);
		});
		
		$("#createForm").validate({
			rules : {
				'title' : {
					required : true,
					maxlength : 30
				},
				'releasedTime' : {
					required : true
				},
				'author' : {
					required : true
				},
				'articleCategoryId' : {
					required : true
				}
			},
			messages : {
				'title' : {
					required : '标题不能为空',
					maxlength : '标题长度不超过40个字符, 20个汉字'
				},
				'releasedTime' : {
					required : '请选择发布时间'
				},
				'author' : {
					required : '请选择作者'
				},
				'articleCategoryId' : {
					required : '请选择类别'
				}
			},
			submitHandler : 
				function(form){
					var content = ue.getContent();
					if(!content) {
						alert('请填写文章详情');
						return false;
					}
					$(form).find(':submit').prop('disabled', true);
					Layout.postForm(form);
				}
		});

		var mutex = (function() { //互斥上传类型
			var m = {};
			return {
				set : function(t, xhr) {
					m.xhr && m.xhr.abort();
					m.type = t;
					xhr && (m.xhr = xhr);
				},
				is : function(t) {
					return m.type === t;
				},
				get : function() {
					return m.type;
				}
			}
		})();

		$('#combatImage').bind('click', function() {
			$('#inputUpload').click();
		});

		$("#inputUpload").bind("change", function(e) {
			if (!this.value)
				return;
			mutex.set("local");
			$('#uploadForm').submit();
		});
		$("#uploadtarget").on("load", function() {
			if (!mutex.is("local"))
				return;
			var txt = $('#uploadtarget').contents().find('body').text();
			var response = $.parseJSON(txt);
			if (!response.imgpath) {
				messagebox.error(response.message);
				return;
			}
			setImage(response.imgpath);
		});
		
	});

	 function setImage(result) {
		  if(result.startWith('http')){
			  $('#combatImage').attr('src', result);
			  $('#image').val(result);
		 }else{
			 alert(result);
		 } 
		  
	  }
	 
	 String.prototype.startWith=function(s){
		if(s==null||s==""||this.length==0||s.length>this.length) 
			return false; 
		if(this.substr(0,s.length)==s) 
			return true; 
		else 
			return false; 
		return true; 
	} 
	
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/article">文章管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box yellow">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-edit"></i><span> 文章编辑 </span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="createForm" action="" data-action="${ctx}/article/update" class="form-horizontal" method="post">
          <input type="hidden" name="id" value="${article.id}"/>
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">类别
              </label>
              <div class="col-md-4">
              	<select name="articleCategoryId" class="form-control">
              		<option value="">-请选择类别-</option>
              		<c:forEach items="${articleCategories}" var="articleCategory">
              			<option value="${articleCategory.id}"<c:if test="${article.articleCategoryId == articleCategory.id}"> selected="selected"</c:if>>${articleCategory.name}</option>
              		</c:forEach>
              	</select>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">标题
              </label>
              <div class="col-md-4">
                  <input type="text" name="title" id="title" class="form-control" maxlength="40"
                      value="${article.title}" placeholder="请输入标题" />
              </div>
            </div>
            
           <div class="form-group">
              <label class="control-label col-md-3">文章摘要</label>
              <div class="col-md-4">
                <textarea name="brief" id="brief" cols="30" rows="5" class="form-control">${article.brief}</textarea>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">文章详情
              </label>
              <div class="col-md-4">
				  <div><script id="editor" type="text/plain" style="width:840px; height:500px;"></script></div>
              </div>
            </div>
            
             <div class="form-group">
              <label class="control-label col-md-3">发布时间
              </label>
              <div class="col-md-4">
				 <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                  	name="releasedTime" value="<fmt:formatDate  pattern="yyyy-MM-dd HH:mm:ss" value="${article.releasedTime }"/>" placeholder="发布时间" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">作者
              </label>
              <div class="col-md-4">
				 <input type="text" name="author" id="author" class="form-control" maxlength="40"
                      value="${article.author}" placeholder="请填写作者" />
              </div>
            </div>
            
          </div>
          
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">保存</button>
              <button class="btn default" data-href="${ctx}/article">
                <i class="fa fa-arrow-left"></i> 返回
              </button>
            </div>
          </div>
        </form>
        
      </div>
    </div>
  </div>
</div>
