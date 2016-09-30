<%@ page language="java" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${stccdn}/plugin/dropload-0.9.0/dropload.css" />
<script src="${stccdn}/plugin/dropload-0.9.0/dropload.js"></script>
<style>
.page-wrap {
  position: absolute; left: 0; top: 0;
  width: 100%; height: 100%;
  display: -webkit-box; display: -webkit-flex; display: -ms-flexbox; display: flex;
  -ms-flex-direction: column; -webkit-flex-direction: column; flex-direction: column;
  -webkit-box-orient: vertical; box-orient: vertical;
}
.page-inner {
  -webkit-box-flex: 1; -webkit-flex: 1; -ms-flex: 1; flex: 1;
  overflow-y: scroll; overflow-x: hidden;
  -webkit-overflow-scrolling: touch;
}
</style>
<script>
  var dropload;

  $(function() {
    dropload = $('.page-inner').dropload({
      autoLoad : false,
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
        domNoData : '<div class="dropload-noData">没有更多数据了</div>'
      },
      loadUpFn : function(dropload) {
        loadData(dropload);
      },
      loadDownFn : function(dropload) {
        loadMore(dropload)
      }
    });
    loadData(dropload);
    
    <c:if test="${page.total <= page.pageSize}">
    dropload.lock('down');
    dropload.$domDown.remove();
    </c:if>
  });

  function loadData(dropload) {
    $.ajax({
      url : getUrl(),
      data : {
        pageNumber : 0
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if (result.code != 0) {
          messageAlert('[' + result.code + ']Ajax Page Load error!');
          dropload.resetload();
          return;
        }
        dropload.$element.find('.page-list').html('');
        dropload.$element.find('.page-empty').remove();
        var page = result.data.page;
        if(page.data.length == 0) {
          dropload.$element.append('<div class="page-empty"><i class="fa fa-file-o"></i><span>暂无记录</span></div>');
          dropload.lock('down');
          dropload.resetload();
          dropload.$domDown.remove();
          return;
        }
        if (page.data.length > 0) {
          timeLT = result.data.timeLT;
          pageNumber = page.pageNumber;
          var pageData = page.data;
          for ( var i in pageData ) {
            var row = pageData[i];
            var html = buildRow(row);
            dropload.$element.find('.page-list').append(html);
          }
        }
        
        if (page.data.length < page.pageSize) {
          dropload.hasMore(false);
          dropload.lock('down');
        } else {
          dropload.hasMore(true);
          dropload.unlock('down');
        }

        dropload.resetload();
      },
      error : function(xhr, type) {
        messageAlert('[' + xhr.status + ']Ajax Page Load error!');
        dropload.resetload();
      }
    });
  }

  var timeLT = '${timeLT}';
  var pageNumber = 0;
  function loadMore(dropload) {
    if(dropload.$element.find('.page-more').hasClass('.disabled')){
      return;
    }
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
          messageAlert('[' + result.code + ']Ajax Page Load error!');
          dropload.resetload();
          return;
        }
        var page = result.data.page;
        if (page.data.length > 0) {
          timeLT = result.data.timeLT;
          pageNumber = page.pageNumber;
          var pageData = page.data;
          for ( var i in pageData ) {
            var row = pageData[i];
            var html = buildRow(row);
            dropload.$element.find('.page-list').append(html);
          }
        }
        
        if (page.data.length == 0 || page.data.length < page.pageSize) {
          dropload.hasMore(false);
          dropload.lock('down');
        } else {
          dropload.hasMore(true);
          dropload.unlock('down');
        }
        
        dropload.resetload();
      },
      error : function(xhr, type) {
        messageAlert('[' + xhr.status + ']Ajax Page Load error!');
        dropload.resetload();
      }
    });
  }
</script>