<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<script>
 $(function () {
    var area = new areaInit('province', 'city', 'district', '${areaId}');
 });
 
 <shiro:hasPermission name="orderReport:export">
 function reportExport() {
   location.href = '${ctx}/report/userUpgrade/export?' + $('#searchForm').serialize();
 }
 </shiro:hasPermission>
</script>
<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report/userUpgrade">服务商数量统计报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>服务商数量统计报表</span>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form data-action="${ctx}/report/userUpgrade" class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <select class="form-control pull-left" id="province" name="provinceIdEQ">
                  <option value="">-- 请选择省 --</option>
                </select>
                <select class="form-control pull-left" id="city" name="cityIdEQ">
                  <option value="">-- 请选择市 --</option>
                </select>
                <select class="form-control pull-left" id="district" name="districtIdEQ">
                  <option value="">-- 请选择区 --</option>
                </select>
              </div>
              
              <div class="form-group">
                <select name="rootRootNameLK" class="form-control">
                  <option value="">-- 系统--</option>
                  <c:forEach items="${rootNames}" var="rootName">
                  <option value="${rootName}" <c:if test="${rootName == rootRootName}"> selected="selected"</c:if>>${rootName}</option>
                  </c:forEach>
                </select>
              </div>
              
              <div class="form-group input-inline">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>
              <shiro:hasPermission name="userUpgradeReport:export">
                <div class="form-group">
                  <button type="button" class="btn yellow" onClick="reportExport()">
                    <i class="fa fa-file-excel-o"></i> 导出Excel
                  </button>
                </div>
              </shiro:hasPermission>
            </form>
          </div>
          <div class="table-scrollable">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th> # </th>
                        <c:forEach items="${timeLabels}" var="timeLabel">
                        <th>${timeLabel}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td> 服务商总量 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].count} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 当月新增服务商 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].growthCount} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 环比增长率（总） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].growthRateLabel} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 特级服务商总量） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v4Count} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 当月新增特级 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v4GrowthCount} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 环比增长率（特级） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v4GrowthRateLabel} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 特级转化率（本月新增特级/上月省级数量） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v4ConversionRateLabel} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 省级服务商总量 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v3Count} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 当月新增省级</td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v3GrowthCount} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 环比增长率（省级） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v3GrowthRateLabel} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 市级服务商总量 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v2Count} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 当月新增市级 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v2GrowthCount} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 环比增长率（市级） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v2GrowthRateLabel} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> VIP服务商总量 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v1Count} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 当月新增VIP服务商 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v1GrowthCount} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 环比增长率（VIP服务商） </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v1GrowthRateLabel} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 意向服务商总量 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v0Count} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 当月转化量 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v0ConversionCount} </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td> 转化率 </td>
                        <c:forEach items="${data}" varStatus="varStatus">
                        <td> ${data[varStatus.index].v0ConversionRateLabel} </td>
                        </c:forEach>
                    </tr>
                </tbody>
            </table>
        </div>
        </div>
      </div>
      <!-- END ALERTS PORTLET-->
    </div>
  </div>
</div>
