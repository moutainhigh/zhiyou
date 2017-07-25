<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- BEGIN JAVASCRIPTS -->
<%--<style type="text/css">--%>
	<%--a {--%>
		<%--cursor: point;--%>
	<%--}--%>
<%--</style>--%>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
  ${sys} <small>管理系统</small>
</h3>
<div class="page-bar">
  <%--<ul class="page-breadcrumb">--%>
    <%--<li><i class="fa fa-home"></i> <a class="ajaxify" data-href="${ctx}/main">首页</a>--%>
  <%--</ul>--%>
  <!-- <div class="page-toolbar">
    <div id="" class="pull-right tooltips btn btn-fit-height grey-salt" data-placement="top" data-original-title="">
      <i class="icon-calendar"></i>&nbsp; <span class="thin uppercase visible-lg-inline-block"></span>&nbsp; <i
        class="fa fa-angle-down"></i>
    </div>
  </div> -->
</div>
<!-- END PAGE HEADER-->

	<%--<div class="row">--%>
      <%--&lt;%&ndash;<div class="col-md-6">&ndash;%&gt;--%>
      <%--&lt;%&ndash;<!-- BEGIN Portlet PORTLET-->&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="portlet light bordered">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="portlet-title">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<div class="caption">&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<i class="icon-clock"></i>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<span class="caption-subject bold uppercase"> 待客服处理</span>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="portlet-body">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<table class="table table-bordered table-hover">&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<tbody>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> 待审核银行卡 </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> ${userBankInfoCount} </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td><c:if test="${userBankInfoCount > 0}"><a data-href="${ctx}/userBankInfo" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> 待审核实名认证 </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> ${userInfoCount} </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td><c:if test="${userInfoCount > 0}"><a data-href="${ctx}/userInfo" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> 待预审核检测报告 </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> ${reportPreCount} </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td><c:if test="${reportPreCount > 0}"><a data-href="${ctx}/report" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> 待审核检测报告 </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> ${reportCount} </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td><c:if test="${reportCount > 0}"><a data-href="${ctx}/report" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> 待平台发货订单 </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td> ${orderPlatformDeliverCount} </td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<td><c:if test="${orderPlatformDeliverCount > 0}"><a data-href="${ctx}/order/platformDeliverList" class="btn green btn-xs"><i class="fa fa-edit"></i>去发货</a></c:if></td>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;</tbody>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</table>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
      <%--&lt;%&ndash;<!-- END Portlet PORTLET-->&ndash;%&gt;--%>
      <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

	  <%--&lt;%&ndash;<div class="col-md-6">&ndash;%&gt;--%>
		<%--&lt;%&ndash;<div class="portlet light bordered">&ndash;%&gt;--%>
          <%--&lt;%&ndash;<div class="portlet-title">&ndash;%&gt;--%>
            	<%--&lt;%&ndash;<div class="caption">&ndash;%&gt;--%>
              	<%--&lt;%&ndash;<i class="icon-users"></i>&ndash;%&gt;--%>
              	<%--&lt;%&ndash;<span class="caption-subject bold uppercase"> 用户统计信息</span>&ndash;%&gt;--%>
            	<%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            	<%--&lt;%&ndash;<div class="actions">&ndash;%&gt;--%>
              	<%--&lt;%&ndash;<a class="btn btn-default btn-sm" data-href="${ctx}/user">&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<i class="fa fa-plus"></i> 查看 </a>&ndash;%&gt;--%>
              <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
          <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
          <%--&lt;%&ndash;<div class="portlet-body">&ndash;%&gt;--%>
              <%--&lt;%&ndash;<table class="table table-bordered table-hover">&ndash;%&gt;--%>
				<%--&lt;%&ndash;<tbody>&ndash;%&gt;--%>
				<%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
  					<%--&lt;%&ndash;<td>服务商总人数</td>&ndash;%&gt;--%>
  					<%--&lt;%&ndash;<td>${agentCount}</td>&ndash;%&gt;--%>
				<%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
				<%--&lt;%&ndash;</tbody>&ndash;%&gt;--%>
			 <%--&lt;%&ndash;</table>&ndash;%&gt;--%>
          <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
      <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

      <%--&lt;%&ndash;<div class="col-md-6">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="portlet light bordered">&ndash;%&gt;--%>
          <%--&lt;%&ndash;<div class="portlet-title">&ndash;%&gt;--%>
              <%--&lt;%&ndash;<div class="caption">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<i class="icon-diamond"></i>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<span class="caption-subject bold uppercase"> 待处理财务相关</span>&ndash;%&gt;--%>
              <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
          <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
          <%--&lt;%&ndash;<div class="portlet-body">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<table class="table table-bordered table-hover">&ndash;%&gt;--%>
              <%--&lt;%&ndash;<tbody>&ndash;%&gt;--%>
              <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td> 待处理提现 </td>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td> ${withdrawCount} </td>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td><c:if test="${withdrawCount > 0}"><a data-href="${ctx}/withdraw" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>&ndash;%&gt;--%>
              <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
              <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td> 待处理充值 </td>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td> ${depositCount} </td>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td><c:if test="${depositCount > 0}"><a data-href="${ctx}/deposit" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>&ndash;%&gt;--%>
              <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
              <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td> 待确认支付 </td>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td> ${paymentCount} </td>&ndash;%&gt;--%>
                  <%--&lt;%&ndash;<td><c:if test="${paymentCount > 0}"><a data-href="${ctx}/payment" class="btn green btn-xs"><i class="fa fa-edit"></i>去确认</a></c:if></td>&ndash;%&gt;--%>
              <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
              <%--&lt;%&ndash;</tbody>&ndash;%&gt;--%>
             <%--&lt;%&ndash;</table>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
          <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
      <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

	<%--</div>--%>
