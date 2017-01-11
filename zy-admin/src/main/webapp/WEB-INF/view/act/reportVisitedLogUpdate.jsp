<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function () {
  	$('.btn-submit').click(function() {
		  $.post('${ctx}/reportVisitedLog/update', $('#form').serialize(), function(data) {
			  if(data.code == 0) {
				  parent.grid.getDataTable().ajax.reload(null, false);
				  layer.closeAll();
			  } else {
				  layer.alert(data.message);
			  }
		  });
    });

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<%--<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/reportVisitedLog">回访记录</a></li>
  </ul>
</div>--%>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-volume-1"></i> 填写回访记录
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/reportVisitedLog/update" class="form-horizontal" method="post">
          <input type="hidden" name="reportId" value="${report.id}"/>
          <input type="hidden" name="id" value="${reportVisitedLog.id}"/>
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>

            <c:if test="${not empty reportVisitedLog.customerServiceName1}">
              <div class="form-group">
                <label class="control-label col-md-3">一访客服</label>
                <div class="col-md-5">
                  <div class="form-control-static">客服: ${reportVisitedLog.customerServiceName1} | 回访时间: ${reportVisitedLog.visitedTime1Label} | 回访结果: ${reportVisitedLog.visitedStatus1}</div>
                </div>
              </div>
            </c:if>
            <c:if test="${not empty reportVisitedLog.customerServiceName2}">
              <div class="form-group">
                <label class="control-label col-md-3">二访客服</label>
                <div class="col-md-5">
                  <div class="form-control-static">客服: ${reportVisitedLog.customerServiceName2} | 回访时间: ${reportVisitedLog.visitedTime2Label} | 回访结果: ${reportVisitedLog.visitedStatus2}</div>
                </div>
              </div>
            </c:if>
            <c:if test="${not empty reportVisitedLog.customerServiceName3}">
              <div class="form-group">
                <label class="control-label col-md-3">三访客服</label>
                <div class="col-md-5">
                  <div class="form-control-static">客服: ${reportVisitedLog.customerServiceName3} | 回访时间: ${reportVisitedLog.visitedTime3Label} | 回访结果: ${reportVisitedLog.visitedStatus3}</div>
                </div>
              </div>
            </c:if>

            <div class="form-section">
              <div class="form-group">
                <label class="control-label col-md-3">检测报告</label>
                <div class="col-md-5">
                  <div class="form-control-static">
                    ${report.realname} ${report.age} ${report.gender} ${report.jobName} | ${report.province} ${report.city} ${report.district} | ${report.reportedDateLabel}(检测时间)
                  </div>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">与检测人关系</label>
              <div class="col-md-5">
                <div class="radio-list">
                  <c:forEach items="${map['guanxi']}" var="label" varStatus="varStatus">
                  <label style="float: left;">
                  <input type="radio" value="${label}" name="relationship"<c:if test="${reportVisitedLog.relationship == label}"> checked</c:if>> ${label}
                  </label>
                  </c:forEach>
                </div>
                <input type="text" name="relationshipText" value="${reportVisitedLog.relationshipText}" class="form-control" placeholder="其他"/>
              </div>
            </div>

            <div class="form-section">
              <div class="form-group">
                <label class="control-label col-md-3">作息时间</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['zuoxi']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="restTimeLabel"<c:if test="${reportVisitedLog.restTimeLabel == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="restTimeText" value="${reportVisitedLog.restTimeText }" class="form-control" placeholder="其他"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">睡眠质量</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['shuimian']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="sleepQuality"<c:if test="${reportVisitedLog.sleepQuality == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="sleepQualityText" value="${reportVisitedLog.sleepQualityText}" class="form-control" placeholder="其他"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">饮酒</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['yinjiu']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="drink"<c:if test="${reportVisitedLog.drink == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">抽烟</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['chouyan']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="smoke"<c:if test="${reportVisitedLog.smoke == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">锻炼身体</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['duanlian']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="exercise"<c:if test="${reportVisitedLog.exercise == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="exerciseText" class="form-control" value="${reportVisitedLog.exerciseText}" placeholder="是或其他"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">兴趣爱好</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['xingqu']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="hobby"<c:if test="${reportVisitedLog.hobby == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="hobbyText" value="${reportVisitedLog.hobbyText}" class="form-control" placeholder="兴趣爱好"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">生活习惯备注</label>
                <div class="col-md-5">
                  <textarea type="text" class="form-control" name="remark1" >${reportVisitedLog.remark1}</textarea>
                </div>
              </div>
            </div>

            <div class="form-section">
              <div class="form-group">
                <label class="control-label col-md-3">检测原因</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['yuanyin']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="cause"<c:if test="${reportVisitedLog.cause == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="causeText" value="${reportVisitedLog.causeText}" class="form-control" placeholder="其他"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">健康状况</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['jiankang']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="health"<c:if test="${reportVisitedLog.health == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">当前病状</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['bingzhuang']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="sickness"<c:if test="${reportVisitedLog.sickness == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">家族遗传史</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['bingshi']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="familyHistory"<c:if test="${reportVisitedLog.familyHistory == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">保健品</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['baojianpin']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="healthProduct"<c:if test="${reportVisitedLog.healthProduct == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="healthProductText" value="${reportVisitedLog.healthProductText}" class="form-control" placeholder="保健品名称"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">月平均消费</label>
                <div class="col-md-5">
                  <div class="radio-list">
                    <c:forEach items="${map['yuexiaofei']}" var="label" varStatus="varStatus">
                      <label style="float: left;">
                      <input type="radio" value="${label}" name="monthlyCost"<c:if test="${reportVisitedLog.monthlyCost == label}"> checked</c:if>> ${label}
                      </label>
                    </c:forEach>
                  </div>
                  <input type="text" name="monthlyCostText" value="${reportVisitedLog.monthlyCostText}" class="form-control" placeholder="其他"/>
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">产品名称</label>
                <div class="col-md-5">
                  <input type="text" class="form-control" name="productName" value="${reportVisitedLog.productName}" />
                </div>
              </div>

              <div class="form-group">
                <label class="control-label col-md-3">健康状况备注</label>
                <div class="col-md-5">
                  <textarea type="text" class="form-control" name="remark2" >${reportVisitedLog.remark2}</textarea>
                </div>
              </div>

            </div>

            <div class="form-group">
              <label class="control-label col-md-3">产品分享</label>
              <div class="col-md-5">
                <div class="radio-list">
                  <c:forEach items="${map['fenxiang']}" var="label" varStatus="varStatus">
                    <label style="float: left;">
                    <input type="radio" value="${label}" name="productSharing"<c:if test="${reportVisitedLog.productSharing == label}"> checked</c:if>> ${label}
                    </label>
                  </c:forEach>
                </div>
                <input type="text" name="productSharingText" value="${reportVisitedLog.productSharingText}" class="form-control" placeholder="其他"/>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">客转代意向客户</label>
              <div class="col-md-5">
                <div class="radio-list">
                  <c:forEach items="${map['zuodaili']}" var="label" varStatus="varStatus">
                    <label style="float: left;">
                    <input type="radio" value="${label}" name="toAgent"<c:if test="${reportVisitedLog.toAgent == label}"> checked</c:if>> ${label}
                    </label>
                  </c:forEach>
                </div>
              </div>
            </div>

            <div class="form-section">

              <div class="form-group">
                <label class="control-label col-md-3">检测干扰因素</label>
                <div class="col-md-5">
                  <input type="text" value="${reportVisitedLog.interferingFactors}" class="form-control" name="interferingFactors" />
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-md-3">备注</label>
                <div class="col-md-5">
                  <textarea type="text" class="form-control" name="remark3" >${reportVisitedLog.remark3}</textarea>
                </div>
              </div>

            </div>

            <div class="form-group">
              <label class="control-label col-md-3">回访记录</label>
              <div class="col-md-5">
                <textarea type="text" class="form-control" name="visitedInfo" >${reportVisitedLog.visitedInfo}</textarea>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">沟通方式</label>
              <div class="col-md-5">
                <div class="radio-list">
                  <c:forEach items="${map['fangshi']}" var="label" varStatus="varStatus">
                    <label style="float: left;">
                    <input type="radio" value="${label}" name="contactWay"<c:if test="${reportVisitedLog.contactWay == label}"> checked</c:if>> ${label}
                    </label>
                  </c:forEach>
                </div>
                <input type="text" name="contactWayText" value="${reportVisitedLog.contactWayText}"  class="form-control" placeholder="微信号或其他"/>
              </div>
            </div>

            <c:if test="${empty reportVisitedLog.customerServiceName2 or empty reportVisitedLog.customerServiceName3}">
              <div class="form-group">
                <label class="control-label col-md-3">时间<span class="required"> * </span></label>
                <div class="col-md-5">
                  <input class="form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                         name="<c:if test="${empty reportVisitedLog.visitedTime2}">visitedTime2</c:if><c:if test="${not empty reportVisitedLog.visitedTime2 and empty reportVisitedLog.visitedTime3}">visitedTime3</c:if>" value="" placeholder="回访时间"/>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-md-3">回访结果<span class="required"> * </span></label>
                <div class="col-md-5">
                  <select class="form-control" name="<c:if test="${empty reportVisitedLog.visitedStatus2}">visitedStatus2</c:if><c:if test="${not empty reportVisitedLog.visitedStatus2 and empty reportVisitedLog.visitedStatus3}">visitedStatus3</c:if>">
                    <option value="">-- 回访结果  --</option>
                    <c:forEach items="${visitedStatusList}" var="label" varStatus="varStatus">
                      <option value="${label}">${label}</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
            </c:if>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="button" class="btn green btn-submit">
                <i class="fa fa-save"></i> 保存
              </button>
              <%--<button class="btn default" data-href="${ctx}/reportVisitedLog">
                <i class="fa fa-chevron-left"></i> 返回
              </button>--%>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
