<%@ page language="java" pageEncoding="UTF-8"%>
<script src="${stccdn}/plugin/jquery-validation-1.15.0/jquery.validate.js"></script>
<script src="${stccdn}/extend/jquery-validation/jquery.validate.method.js"></script>
<script>
  $.validator.setDefaults({
    ignore : '.ignore',
    ignoreTitle : true,
    errorContainer : '.form-message',
    errorClass : 'valid-error',
    errorElement : 'span',
    errorPlacement : function(error, element) {
      //messageFlash(error.text());
      if($('.form-message').length > 0){
        $('.form-message').html('<p>输入信息有误，请先更正。</p>').slideDown(300);
      }
      if ($(element).parents('.list-item').find('.form-error').length == 0) {
        var formError = $('<div class="form-error"><i class="fa fa-exclamation-circle"></i></div>');
        $(element).parents('.list-item').addClass('list-item-error').append(formError);
      }
    },
    success : function(error, element) {
      if ($(element).parents('.list-item').find('.valid-error').length == 0) {
        $(element).parents('.list-item').removeClass('list-item-error').find('.form-error').remove();
      }
    },
    submitHandler : function(form) {
      $(form).find(':submit').prop('disabled', true);
      form.submit();
    }
  });
</script>