<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/7/5
  Time: 12:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<header class="header">
    <h1>填写保险申请</h1>
    <a href="#" onclick="hideBtn()" class="button-left">
        <i class="fa fa-angle-left"></i>
    </a>
</header>
<form>
    <div id="policy" class="list-group">
        <div id="policyInfo">
            <div class="list-item bd-t-0"><label class="list-label" for="code">产品编码</label>
                <div class="list-text">
                    <input type="text" name="code" id="code" class="form-input" value="" placeholder="填写产品编码" required>
                </div>
            </div>
            <div class="list-item"><label class="list-label" for="idCardNumber">身份证号</label>
                <div class="list-text">
                    <input type="text" name="idCardNumber" id="idCardNumber" class="form-input" value="" placeholder="填写身份证号">
                </div>
            </div>
            <div class="list-item"><label class="list-label">正面照</label>
                <div class="list-text image-upload">
                    <div class="image-item image-single "><input type="hidden" name="image1" id="image1" value="">
                        <div id="preview">
                            <img src="${stccdn}/image/upload_240_150.png" class="previewImage" style="width:100px;height:100px;">
                        </div>
                        <input type="file" onchange="fileChange(this)" style="z-index:999;"/>
                    </div>
                </div>
                <div class="list-unit">
                    <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_1.jpg" data-title="身份证正面">
                        <i class="fa fa-question-circle-o">

                        </i> 示意图
                    </a>
                </div>
            </div>
            <label style="margin-left: 20px; color:red;">(非必填)上传身份证图片可方便核验您的身份证号码的准确性</label>
            <div class="list-item"><label class="list-label">生日</label>
                <div class="list-text">
                    <input type="date" name="birthday" class="form-input" value="" placeholder="填写生日  1900-01-01"></div>
            </div>
        </div>
    </div>
    <div class="form-btn" style="padding-bottom: 50px;">
        <input class="btn orange btn-block round-2" type="submit" value="申 请" style="margin-bottom:10px;" onclick="submitBtn()">
        <div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideBtn()">取 消
        </div>
    </div>
</form>
<script>
    function hideBtn() {
        parent.hideBtn();
    }
    function submitBtn(){
        parent.submitBtn();
    }
</script>
</body>
</html>
