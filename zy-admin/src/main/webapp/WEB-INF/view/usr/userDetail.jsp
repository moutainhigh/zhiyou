<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>

</style>
<script>
  $(function () {
  	var setting = {
  	    data: {
				simpleData: {
					enable: true
				}
			},
        callback: {
          onClick: function(event, treeId, treeNode) {
            $.ajax({
              url: '${ctx}/user/detail?isPure=true&id=' + treeNode.id,
              dataType: 'html',
              success: function(data) {
                layer.open({
                  type: 1,
                  skin: 'layui-layer-rim', //加上边框
                  area: ['960px', '640px'], //宽高
                  content: data
                });
              }
            })
  
          }
        }
    };
  	var json = ${json};
  	
    $.fn.zTree.init($('#${treeUuid}'), setting, json);

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<c:if test="${not isPure}">
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/user">用户管理</a></li>
  </ul>
</div>
</c:if>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered" style="height: 800px;">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-user"></i> 查看用户
        </div>
        <c:if test="${not isPure}">
        <div class="actions">
          <a class="btn btn-circle default" data-href="${ctx}/user">
            <i class="fa fa-chevron-left"></i> 返回
          </a>
        </div>
        </c:if>
      </div>
      <div class="portlet-body form">
        <form class="form-horizontal">
          <div class="form-body">

            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">昵称:</label>
                  <div class="col-md-9">
                    <p class="form-control-static"><img src="${user.avatarThumbnail}" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"> ${user.nickname}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">手机号:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.phone}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">用户等级:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userRankLabel}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">真实姓名:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userInfo.realname}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">性别:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userInfo.gender}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">生日:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userInfo.birthdayLabel}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">职业:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userInfo.jobName}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">所在地:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userInfo.province} - ${user.userInfo.city} - ${user.userInfo.district}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label class="control-label col-md-3">是否冻结:</label>
                  <div class="col-md-9">
                    <p class="form-control-static"><c:if test="${user.isFrozen}"><label class="label label-danger">已冻结</label></c:if><c:if test="${!user.isFrozen}">-</c:if></p>
                  </div>
                </div>
              </div>
            </div>
            
          </div>

        </form>

        <ul class="nav nav-tabs">
          <li class="active"><a href="#chart" data-toggle="tab" aria-expanded="false"> 层级信息 </a></li>
          <li class=""><a href="#userUpgrade" data-toggle="tab" aria-expanded="false"> 用户升级信息 <span class="badge badge-primary"> ${user.userUpgrades.size()} </span></a></li>
        </ul>

        <div class="tab-content">

          <div class="tab-pane fade active in" id="chart">
            <div class="col-md-10 ztree" id="${treeUuid}">

            </div>
          </div>
          
          <div class="tab-pane fade" id="userUpgrade">
            <table class="table table-hover table-bordered">
              <thead>
              <tr>
                <th>升级前的等级</th>
                <th>升级后的等级</th>
                <th>升级时间</th>
              </tr>
              </thead>
              <tbody>
              <c:if test="${empty user.userUpgrades}">
                <tr>
                  <td colspan="18">暂无数据</td>
                </tr>
              </c:if>
              <c:if test="${not empty user.userUpgrades}">
               <c:forEach items="${user.userUpgrades}" var="userUpgrade">
                <tr>
                  <td>${userUpgrade.fromUserRankLabel}</td>
                  <td>${userUpgrade.toUserRankLabel}</td>
                  <td>${userUpgrade.upgradedTimeLabel}</td>
                </tr>
                </c:forEach>
              </c:if>
              </tbody>
            </table>
          </div>
        </div> 
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
