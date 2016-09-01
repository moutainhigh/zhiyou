<%@ page language="java" pageEncoding="UTF-8"%>
<script src="${stc}/assets/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${stc}/assets/plugins/jquery-validation/jquery.validate.method.js"></script>
<script>
$.validator.setDefaults({
  errorElement: 'span',
  errorClass: 'help-inline help-inline-error',
  focusInvalid: false,
  ignore: '',
  errorContainer: '.alert-danger',
  highlight: function (element) {
    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
  },
  unhighlight: function (element) {
    $(element).closest('.form-group').removeClass('has-error');
  },
  success: function (label, element) {
    $(label).remove();
    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
    var icon = $(element).parent('.input-icon').children('i');
    icon.removeClass('fa-warning').addClass('fa-check');
  },
  errorPlacement : function(error, element) {
    var icon = $(element).parent('.input-icon').children('i');
    icon.removeClass('fa-check').addClass('fa-warning');
    icon.attr('data-original-title', error.text()).tooltip({
      'container' : 'body'
    });
    /* error message */
    if (element.attr('data-error-container')) {
      error.appendTo(element);
    } else if (element.parents('.input-group').size() > 0) {
      error.insertAfter(element.parents('.input-group'));
    } else if (element.parent('.input-icon').size() > 0) {
      error.insertAfter(element.parent('.input-icon'));
    } else if (element.parents('.radio-list').size() > 0) {
      error.insertAfter(element.parents('.radio-list'));
    } else if (element.parents('.radio-inline').size() > 0) {
      error.insertAfter(element.parents('.radio-inline'));
    } else if (element.parents('.checkbox-list').size() > 0) {
      error.insertAfter(element.parents('.checkbox-list'));
    } else if (element.parents('.checkbox-inline').size() > 0) {
      error.insertAfter(element.parents('.checkbox-inline'));
    } else {
      error.insertAfter(element); // for other inputs, just perform default behavior
    }
  }
});
</script>
