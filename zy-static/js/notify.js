~(function($) {

  function getNotify() {
    $.getJSON(Config.ctx + "/getNotify?" + Date.now(), function(result) {
      if (result.code == 0) {
        if (result.data) {
          if (result.data.achievement) {
            console.log(result.data.achievement);
            showAchievement(result.data.achievement);
          }
          if (!result.data.isSighedIn) {
            console.log(result.data.isSighedIn);
            showSignButton();
          }
        }
      }
    });
  }

  getNotify();

  // 成就奖励 提示 弹出层
  function showAchievement(achievement) {
    var html = '<aside class="achievement-popup hide">';
    html += '<div class="achievement-popup-mask"></div>';
    html += '<div class="achievement-popup-wrapper">';
    html += '<i class="fa fa-close fa-2x"></i>';
    html += '<img alt="" src="' + Config.stccdn + '/image/achievement/' + achievement.id + '.png">';
    html += '<p>' + achievement.desc + '</p>';
    html += '<h2>恭喜你获得【 ' + achievement.name + ' 】成就！</h2>';
    html += '<a href="javascript:;" data-href="' + Config.ctx + '/u/achievement/' + achievement.id + '">查看奖励</a>';
    html += '</aside>';
    $(html).appendTo('body').fadeIn(500);

    $('.fa-close').click(function() {
      $('.achievement-popup').remove();
      $.post(Config.ctx + '/u/achievement/view/' + achievement.id, {}, function(data) {
        console.log(data)
      });
    })
  }

  $('body').on('click', '.achievement-popup a', function() {
    $('.achievement-popup').remove();
    location.href = $(this).attr('data-href');
  });

  function showSignButton() {
    if ($('#btnSignIn').length > 0) {
      return;
    }
    var html = '<aside class="aside-sign">';
    html += '<a href="javascript:;" id="btnSignIn"><img src="' + Config.stccdn + '/image/sign_in.png"></a>';
    html += '</aside>';
    $('body').append(html);
  }

  $('body').on('click', '#btnSignIn', function() {
    if ($(this).hasClass('disabled')) {
      return false;
    }
    $.ajax({
      url : Config.ctx + '/u/signIn',
      dataType : 'JSON',
      type : 'POST',
      success : function(result) {
        if (result.code == 0) {
          if (result.data.signIn) {
            var point = result.data.signIn.point;
            $.message('签到成功，获得积分' + point, 'success');
            if ($('.point-count').length > 0) {
              $('.point-count').text(Number($('.point-count').text()) + point);
            }
            var $pointFly = $('<div class="abs-rt fs-20 font-orange" style="top: 35%; right: 12%">积分 +' + point + '</div>');
            $pointFly.appendTo('body').animate({
              'top' : '20%',
              'opacity' : '0'
            }, 1500, function() {
              $pointFly.remove();
            });
          } else {
            $.message('签到成功');
          }
          $('.aside-sign').remove();
          setTimeout(getNotify, 2000);
        }
      }
    });
  });

})($ || jQuery);