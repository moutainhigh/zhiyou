<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/view/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
    <meta content="no-cache" http-equiv="pragma" />
    <meta content="no-cache, must-revalidate" http-equiv="Cache-Control" />
    <meta content="Wed, 26 Feb 1997 08:21:57GMT" http-equiv="expires">
    <meta name="format-detection" content="telephone=no"/>
    <title>我的荣誉</title>
    <%@ include file="/WEB-INF/view/include/head.jsp" %>
    <link rel="stylesheet" href="${ctx}/css/style.css" />
    <!--移动端版本兼容 -->
    <script type="text/javascript">
        var phoneWidth =  parseInt(window.screen.width);
        var phoneScale = phoneWidth/640;
        var ua = navigator.userAgent;
        if (/Android (\d+\.\d+)/.test(ua)){
            var version = parseFloat(RegExp.$1);
            if(version>2.3){
                document.write('<meta name="viewport" content="width=640, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
            }else{
                document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
            }
        } else {
            document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
        }
    </script>
    <!--移动端版本兼容 end -->
</head>
<body>
<div class="clickXing">
        <div class="bg">
            <img src="${ctx}/images/background.png"/>
        </div>
        <c:if test="${not empty lessonUserDetailVo}">
        <c:forEach items="${lessonUserDetailVo.lessons}" var="lesson">
            <c:if test="${lesson.title eq '孵化营'}">
                <div class="fuhua">
                    <img src="${ctx}/images/fuhua.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '团训营'}">
                <div class="tuanxun">
                    <img src="${ctx}/images/tuanxun.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '演说营1'}">
                <div class="yanshuo">
                    <img src="${ctx}/images/yanshuo.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '演说营2'}">
                <div class="yanshuo2">
                    <img src="${ctx}/images/yanshuo2.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '演说营3'}">
                <div class="yanshuo3">
                    <img src="${ctx}/images/yanshuo3.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '领袖营'}">
                <div class="lingxiu">
                    <img src="${ctx}/images/lingxiu.png"  />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '教练营1'}">
                <div class="jiaolian">
                    <img src="${ctx}/images/jiaolian.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '教练营2'}">
                <div class="jiaolian2">
                    <img src="${ctx}/images/jiaolian2.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '教练营3'}">
                <div class="jiaolian3">
                    <img src="${ctx}/images/jiaolian3.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '一代宗师'}">
                <div class="zongshi">
                    <img src="${ctx}/images/zongshi.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '大师营'}">
                <div class="dashi">
                    <img src="${ctx}/images/dashi.png" />
                </div>
            </c:if>
            <c:if test="${lesson.title eq '一代天骄'}">
                <div class="son">
                    <img src="${ctx}/images/son.png" />
                </div>
            </c:if>
            </c:forEach>
        </c:if>
</div>
</body>
</html>
