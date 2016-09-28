<%@ page language="java" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${stccdn}/plugin/dropload-0.9.0/dropload.css" />
<script src="${stccdn}/plugin/dropload-0.9.0/dropload.js"></script>
<style>
.drop-wrap {
  position: absolute; left: 0; top: 0;
  width: 100%; height: 100%;
  display: -webkit-box; display: -webkit-flex; display: -ms-flexbox; display: flex;
  -ms-flex-direction: column; -webkit-flex-direction: column; flex-direction: column;
  -webkit-box-orient: vertical; box-orient: vertical;
}
.drop-inner {
  -webkit-box-flex: 1; -webkit-flex: 1; -ms-flex: 1; flex: 1;
  background-color: #fff; overflow-y: scroll; overflow-x: hidden;
  -webkit-overflow-scrolling: touch;
}
</style>
<script>
  var dropload;

  $(function() {
    dropload = $('.drop-inner').dropload({
      domUp : {
        domClass : 'dropload-up',
        domRefresh : '<div class="dropload-refresh">↓ 下拉刷新</div>',
        domUpdate : '<div class="dropload-update">↑ 释放更新</div>',
        domLoad : '<div class="dropload-load"><span class="i-loading"></span>加载中...</div>'
      },
      domDown : {
        domClass : 'dropload-down',
        domRefresh : '<div class="dropload-refresh">↑ 上拉加载更多</div>',
        domLoad : '<div class="dropload-load"><span class="i-loading"></span>加载中...</div>',
        domNoData : '<div class="dropload-noData">暂无数据</div>'
      },
      loadUpFn : function(dropload) {
        reload(dropload);
      },
      loadDownFn : function(dropload) {
        loadMore(dropload)
      }
    });
  });

  function reload(dropload) {
    $.ajax({
      url : getUrl(),
      data : {
        pageNumber : 0
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if (result.code != 0) {
          return;
        }
        dropload.$element.find('.list-group').html('');
        dropload.$element.find('.empty-tip').remove();
        var page = result.data.page;
        if (page.data.length) {
          timeLT = result.data.timeLT;
          pageNumber = page.pageNumber;
          var pageData = page.data;
          for ( var i in pageData ) {
            var row = pageData[i];
            var html = buildRow(row);
            dropload.$element.find('.list-group').append(html);
          }
          if (page.data.length < page.pageSize) {
            dropload.$element.append('<a class="list-item list-more disabled" href="javascript:;"><span>没有更多数据了</span></a>')
          }
        } else {
          dropload.$element.append('<div class="empty-tip"><i class="fa fa-file-o"></i><span>暂无记录</span></div>');
        }

        dropload.resetload();
      },
      error : function(xhr, type) {
        messageAlert('Ajax Load error!');
        dropload.resetload();
      }
    });
  }

  var timeLT = '${timeLT}';
  var pageNumber = 0;
  function loadMore(dropload) {
    $.ajax({
      url : getUrl(),
      data : {
        pageNumber : pageNumber + 1,
        timeLT : timeLT
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if (result.code != 0) {
          return;
        }
        var page = result.data.page;
        if (page.data.length) {
          timeLT = result.data.timeLT;
          pageNumber = page.pageNumber;
          var pageData = page.data;
          for ( var i in pageData ) {
            var row = pageData[i];
            var html = buildRow(row);
            dropload.$element.find('.list-group').append(html);
          }
          if (page.data.length < page.pageSize) {
            dropload.$domDown.remove();
            dropload.$element.append('<a class="list-item list-more disabled" href="javascript:;"><span>没有更多数据了</span></a>')
          }
        }
        
        dropload.resetload();
      },
      error : function(xhr, type) {
        messageAlert('Ajax Load error!');
        dropload.resetload();
      }
    });
  }
</script>