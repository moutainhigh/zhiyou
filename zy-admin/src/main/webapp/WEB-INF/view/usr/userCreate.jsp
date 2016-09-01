<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {
    $('#updateForm').validate({
      rules : {
    	  'nickname' : {
				required : true,
				rangelength : [ 2, 12 ]
			},
			'phone' : {
				required : true,
				mobile : true,
				remote : {
					url : '${ctx}/user/checkPhone',
					type : 'post',
					data : {
						phone : function() {
							return $("#phone").val();
						}
					}
				}
			},
			'password' : {
				minlength : 6
			},
			'passwordSure' : {
				equalTo : "#password"
			},
			'qq' : {
				required : true,
				min: 10000,
				digits : true,
			}
      },
      messages : {
	   	    'nickname' : {
				required : '用户昵称不能为空',
				rangelength : '昵称长度必须大于2且小于12个字符'
			},
			'phone' : {
				required : '手机号码不能为空',
				mobile : '不是有效的手机号码',
				remote : '该手机号已经注册过',
			},
	        'password' : {
	          minlength : '密码不得少于6个字符'
	        },
	        'passwordSure' : {
				equalTo : '两次输入的密码不一致'
			},
			'qq' : {
				required : '请输入qq号',
				min: '请输入5-11位之间的qq号',
				digits : '请输入5-11位之间的qq号'
			}
      	}
    });
    
  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/user">用户管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box yellow">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-edit"></i><span> 新增用户 </span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="updateForm" action="" data-action="${ctx}/user/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">用户类型
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <select name="userType" class="form-control">
                  	<option value="商家">商家</option>
                  	<option value="试客">试客</option>
                  	<option value="平台">平台</option>
                  </select>
                </div>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">手机号
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <i class="fa fa-phone"></i> 
                  <input type="text" id="phone" class="form-control" name="phone" value="" placeholder="请输入手机号"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">昵称
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <i class="fa fa-user"></i>
                  <input type="text" id="nickname" class="form-control" name="nickname" value="" placeholder="请输入昵称"/>
                </div>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">qq
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <i class="fa fa-qq"></i>
                  <input type="text" id="qq" class="form-control" name="qq" value="" placeholder="请输入qq"/>
                </div>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">密码
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <i class="fa fa-lock"></i> 
                  <input type="password" id="password" class="form-control" name="password" value="" placeholder="请输入密码"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">确认密码
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <i class="fa fa-lock"></i> 
                  <input type="password" class="form-control" name="passwordSure" value="" placeholder="请再次输入密码"/>
                </div>
              </div>
            </div>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">保存</button>
              <button class="btn default" data-href="${ctx}/user">
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
