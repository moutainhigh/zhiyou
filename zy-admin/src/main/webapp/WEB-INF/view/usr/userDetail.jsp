<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<%@ include file="/WEB-INF/view/include/editor.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>

</style>
<script>
  $(function () {

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
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-user"></i> 查看用户
        </div>
        <div class="actions">
          <a class="btn btn-circle default" data-href="${ctx}/user">
            <i class="fa fa-chevron-left"></i> 返回
          </a>
        </div>
      </div>
      <div class="portlet-body form">
        <form class="form-horizontal">
          <div class="form-body">

            <h4 class="form-section">基本信息:</h4>
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">昵称:</label>
                  <div class="col-md-9">
                    <p class="form-control-static"><img src="${user.avatarThumbnail}" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"> ${user.nickname}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">手机号:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.phone}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">用户等级:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userRankLabel}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">是否冻结:</label>
                  <div class="col-md-9">
                    <p class="form-control-static"><c:if test="${user.isFrozen}">已冻结</c:if></p>
                  </div>
                </div>
              </div>
            </div>
            
            <c:if test="${not empty user.parent}">
            <h4 class="form-section">上级信息:</h4>
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">昵称:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">
                      <img src="${user.parent.avatarThumbnail}" width="30" height="30" style="border-radius: 40px !important; margin-right:5px">
                      ${user.parent.nickname}
                    </p>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">手机号:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.parent.phone}</p>
                  </div>
                </div>
              </div>
            </div>
          
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">等级:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.parent.userRankLabel}</p>
                  </div>
                </div>
              </div>
            </div>
           </c:if>
          </div>

        </form>

        <ul class="nav nav-tabs">
          <li class="active"><a href="#chart" data-toggle="tab" aria-expanded="false"> 层级信息 </a></li>
          <li class=""><a href="#userInfo" data-toggle="tab" aria-expanded="false"> 用户认证信息 </a></li>
          <li class=""><a href="#userUpgrade" data-toggle="tab" aria-expanded="false"> 用户升级信息 <span class="badge badge-primary"> ${user.userUpgrades.size()} </span></a></li>
        </ul>

        <div class="tab-content">

          <div class="tab-pane fade active in" id="chart">
            <div id="userChart" style="height:800px;">
                    
            </div>
          </div>
          <script>
            $(function(){
              var myChart = echarts.init(document.getElementById('userChart'));
              // 指定图表的配置项和数据
              myChart.showLoading();
              $.post('${ctx}/user/ajaxChart/team',{userId : '${user.id}'}, function (result) {
                  myChart.hideLoading();
                
                  option = {
                          legend: {
                              data: result.legend
                          },
                          series: [{
                              type: 'graph',
                              layout: 'force',
                              force: {
                                repulsion: 1500,
                                edgeLength: 150
                              },
                              animation: false,
                              label: {
                                  normal: {
                                    show: true,
                                    position: 'right',
                                      formatter: function(v) {
                                        return v.data.value;
                                      }
                                  }
                              },
                              itemStyle: {
                                normal: {
                                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                                  shadowBlur: 10
                                }
                              },
                              draggable: true,
                              data: result.nodes,
                              categories: result.categories,
                              links: result.links
                          }]
                      };
              
                  myChart.setOption(option);
              })
              });
            </script>
          <div class="tab-pane fade in" id="userInfo">
            <table class="table table-hover table-bordered">
              <thead>
              <tr>
                <th>真实姓名</th>
                <th>身份证号</th>
                <th>图片1</th>
                <th>图片2</th>
                <th>审核状态</th>
                <th>审核备注</th>
                <th>申请时间</th>
                <th>审核通过时间</th>
                <th>性别</th>
                <th>生日</th>
                <th>职业</th>
                <th>所在地</th>
                <th>标签</th>
              </tr>
              </thead>
              <tbody>
              <c:if test="${empty user.userInfo}">
                <tr>
                  <td colspan="18">暂无数据</td>
                </tr>
              </c:if>
              <c:if test="${not empty user.userInfo}">
                <tr>
                  <td>${user.userInfo.realname}</td>
                  <td>${user.userInfo.idCardNumber}</td>
                  <td><c:if test="${not empty user.userInfo.image1Thumbnail}"><img class="imageview" data-url="${user.userInfo.image1}" src="${user.userInfo.image1Thumbnail}"/></c:if></td>
                  <td><c:if test="${not empty user.userInfo.image2Thumbnail}"><img class="imageview" data-url="${user.userInfo.image2}" src="${user.userInfo.image2Thumbnail}"/></c:if></td>
                  <td><label class="label label-${user.userInfo.confirmStatusStyle}">${user.userInfo.confirmStatus}</label></td>
                  <td>${user.userInfo.confirmRemark}</td>
                  <td>${user.userInfo.appliedTimeLabel}</td>
                  <td>${user.userInfo.confirmedTimeLabel}</td>
                  <td>${user.userInfo.gender}</td>
                  <td>${user.userInfo.birthdayLabel}</td>
                  <td>${user.userInfo.jobName}</td>
                  <td>${user.userInfo.province} - ${user.userInfo.city} - ${user.userInfo.district}</td>
                  <td><c:forEach items="${user.userInfo.tagNames}" var="tagName">${tagName}</c:forEach></td>
                </tr>
              </c:if>
              </tbody>
            </table>
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
