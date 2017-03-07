<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>修改检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script src="${stccdn}/js/area.js"></script>
<script type="text/javascript">
  $(function() {
    var area = new areaInit('province', 'city', 'district', '${report.areaId}');
    
    $('.image-multi .image-item').imageupload();
    
    $('.image-multi .image-add').imageupload({
      isMultipart : true
    });

    //验证
    $('.valid-form').validate({
      rules : {
        'realname' : {
          required : true
        },
        'gender' : {
          required : true
        },
        'age' : {
          required : true
        },
        'phone' : {
          required : true,
          phone : true
        },
        'times' : {
          required : true,
          digits : true
        },
        'reportedDate' : {
          required : true
        },
        'areaId' : {
          required : true
        },
        'jobId' : {
          required : true
        },
        'reportResult' : {
          required : true
        },
        'text' : {
          required : true
        }
      },
	    submitHandler : function(form) {
        if($('input[name="image"]').length < 1) {
          messageFlash('请至少上传一张图片');
          return;
        }
        form.submit();
      }
    });

  });
</script>
</head>
<body>
  <header class="header">
    <h1>修改检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <form action="${ctx}/u/report/edit" class="valid-form" method="post">
      <input type="hidden" name="id" value="${report.id}"/>
      <div class="list-group mt-10">
        <div class="list-title">产品信息</div>
        <div class="list-item">
          <label class="list-text lh-36">产品</label>
          <img class="image-40 mr-10" src="${report.product.image1Thumbnail}">
          <div class="list-unit">${report.product.title}</div>
          <input type="hidden" name="productId" value="${report.productId}"/>
        </div>
      </div>
      <div class="list-group mt-10">
        <div class="list-title">填写客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div class="list-text">
            <input type="text" name="realname" class="form-input" value="${report.realname}" placeholder="填写客户姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="男"<c:if test="${report.gender eq '男'}"> selected</c:if>>男</option>
              <option value="女"<c:if test="${report.gender eq '女'}"> selected</c:if>>女</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">地区</label>
          <div class="list-text">
            <div class="flex">
              <div class="form-select flex-1">
                <select name="" id="province">
                  <option value="">省</option>
                </select>
              </div>
              <div class="form-select flex-1">
                <select name="" id="city">
                  <option value="">市</option>
                </select>
              </div>
              <div class="form-select flex-1">
                <select name="areaId" id="district">
                  <option value="">区</option>
                </select>
              </div>
            </div>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">职业</label>
          <div class="list-text form-select">
            <select name="jobId">
              <option value="">请选择</option>
              <c:forEach items="${jobs}" var="job">
                <option value="${job.id}" <c:if test="${report.jobId eq job.id}"> selected </c:if>>${job.jobName}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="age">年龄</label>
          <div class="list-text">
            <input type="number" name="age" class="form-input" value="${report.age}" placeholder="填写客户年龄">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div class="list-text">
            <input type="number" name="phone" class="form-input" value="${report.phone}" placeholder="填写客户手机号">
          </div>
        </div>
      </div>

      <div class="list-group">
        <div class="list-title">检测信息，至少需要上传1张检测图片</div>
        <div class="list-item">
          <label class="list-label" for="times">检测次数</label>
          <div class="list-text">
            <input type="number" name="times" class="form-input" value="${report.times}" placeholder="第几次检测">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="date">检测日期</label>
          <div class="list-text">
            <input type="text" id="date" name="reportedDate" class="form-input" value="${report.reportedDateLabel}" placeholder="填写检测时间 2001-01-01" onfocus="this.type='date'">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">检测结果</label>
          <div class="list-text form-select">
            <select name="reportResult">
              <option value="">请选择</option>
              <option value="阴性"<c:if test="${report.reportResult eq '阴性'}"> selected</c:if>>阴性</option>
              <option value="弱阳性"<c:if test="${report.reportResult eq '弱阳性'}"> selected</c:if>>弱阳性</option>
              <option value="阳性"<c:if test="${report.reportResult eq '阳性'}"> selected</c:if>>阳性</option>
              <option value="干扰色"<c:if test="${report.reportResult eq '干扰色'}"> selected</c:if>>干扰色</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <c:forEach items="${report.images}" var="image" varStatus="varStatus">
            <div class="image-item">
              <input type="hidden" name="image" value="${image}">
              <img src="${report.imageThumbnails[varStatus.index]}">
              <input type="file">
            </div>
            </c:forEach>
            
            <c:if test="${fn:length(report.images) < 6}">
            <div class="image-add" data-limit="6" data-name="image">
              <input type="hidden" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
            </c:if>
          </div>
        </div>

      </div>
      <div class="list-group">
        <div class="list-title">填写产品使用心得</div>
        <div class="list-item">
          <div class="list-text">
            <textarea name="text" class="form-input" rows="3" placeholder="填写产品使用心得">${report.text}</textarea>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>

    </form>
  </article>

</body>
</html>
