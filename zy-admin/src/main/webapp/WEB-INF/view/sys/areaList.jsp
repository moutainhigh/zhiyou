<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>

<style>
	.col-md-6 {
		width: 320px;
	}
</style>

<div class="page-bar">
	<ul class="page-breadcrumb">
		<li><i class="fa fa-home"></i> <a href="javascript:;"
			data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
		<li><a href="javascript:;" data-href="${ctx}/area">区域管理</a></li>
	</ul>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa"></i> 区域管理
				</div>
			</div>
			<div class="portlet-body clearfix">
				<div class="tabbable tabbable-custom">
					<div class="tab-pane active" >
						<div class="form-body">
							<c:if test="${empty areas}">
								<label class="control-label col-md-5"> 
								<button class="btn default" data-href="${ctx}/area">
					                <i class="fa"></i>暂无数据, 返回
					              </button>
					            </label>
							</c:if>
							<c:if test="${not empty areas}">
							<c:forEach items="${areas}" var="area" varStatus="varStatus">
							<c:if test="${varStatus.index % 4 == 0}">
							<div class="row">
							</c:if>
								<div class="col-md-3 col-sm-6">
									<div class="form-group">
										<label class="control-label col-md-5">${area.name}</label>
										<a href="javascript:;" data-href="${ctx}/area/update/${area.id}">[编辑]</a>
										<c:if test="${area.areaType != '区'}">
										<a href="javascript:;" data-href="${ctx}/area?parentIdEQ=${area.id}">[查看子节点]</a>
										<a href="javascript:;" data-href="${ctx}/area/create?parentId=${area.id}">[新增子节点]	</a>
										</c:if>
									</div>
								</div>
							<c:if test="${varStatus.index % 4 == 3}">
							</div>
							</c:if>
							</c:forEach>
							</c:if>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


