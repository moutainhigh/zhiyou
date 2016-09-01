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
      $('.form-message').html('<p>输入信息有误，请先更正。</p>').slideDown();
      if (element.parents('.form-item').find('.input-error').length == 0) {
        var formError = $('<div class="input-error"><i class="fa fa-exclamation-circle"></i></div>');
        element.parents('.form-item').addClass('form-item-error').append(formError);
        if (element.parent().is('.form-select')) {
          formError.addClass('select-error');
        }
      }
    },
    success : function(error, element) {
      if ($(element).parents('.form-item').find('.valid-error').length == 0) {
        $(element).parents('.form-item').removeClass('form-item-error').find('.input-error').remove();
      }
    },
    submitHandler : function(form) {
      $(form).find(':submit').prop('disabled', true);
      form.submit();
    }
  });
</script>