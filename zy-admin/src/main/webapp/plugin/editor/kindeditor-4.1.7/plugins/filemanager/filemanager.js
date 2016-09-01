/*******************************************************************************
* KindEditor - WYSIWYG HTML Editor for Internet
* Copyright (C) 2006-2011 kindsoft.net
*
* @author Roddy <luolonghao@gmail.com>
* @site http://www.kindsoft.net/
* @licence http://www.kindsoft.net/license.php
*******************************************************************************/

KindEditor.plugin('filemanager', function(K) {
	var self = this, name = 'filemanager',
	fileManagerUrl = K.undef(self.fileManagerUrl, self.basePath + '/mvc/editor/browse'),
		imgPath = self.pluginsPath + name + '/images/',
		lang = self.lang(name + '.');
	function makeFileTitle(filename, filesize, datetime) {
		return filename + ' (' + Math.ceil(filesize / 1024) + 'KB, ' + datetime + ')';
	}
	function bindTitle(el, data) {
		if (data.is_dir) {
			el.attr('title', data.filename);
		} else {
			el.attr('title', makeFileTitle(data.filename, data.filesize, data.datetime));
		}
	}
	self.plugin.filemanagerDialog = function(options) {
		var width = K.undef(options.width, 750),
			height = K.undef(options.height, 510),
			dirName = K.undef(options.dirName, ''),
			viewType = K.undef(options.viewType, 'list').toLowerCase(), // "grid" or "list"
			clickFn = options.clickFn;
		var url = fileManagerUrl + '?dirName=' + dirName + '&viewType=' + viewType;
		var html = '<div id="popWindow"><iframe id="winFrame" name="winFrame" src="' + url + '" frameborder="no" border="0" style="width:100%;height:100%;border:none;"></iframe></div>';
		var GetSelectedFiles = function(){
		  var iframe = K('#winFrame');
		  //var doc = K.iframeDoc(iframe);
		  var win = iframe[0].contentWindow;
		  //alert("win:" + win.checkedIds);
		  return win.getSelectedFiles() || [];
		};
		var dialog = self.createDialog({
      name : name,
      width : width,
      height : height,
      title : self.lang(name),
      yesBtn: {
        name : self.lang('yes'),
        click : function(e) {
          //swfupload.removeFiles();
          var files = GetSelectedFiles();
          if (files.length == 0) {
            alert(self.lang('pleaseSelectFile'));
          } else if (files.length > 1) {
            alert(self.lang('multiimage.queueLimitExceeded'));
          } else {
            if(files[0].isDirectory) {
              alert(self.lang('pleaseSelectFile'));
            } else {
              clickFn.call(this, files[0].url, files[0].filename);
            }
          }
        }
      },
      body : html
    });
		return dialog;
	};

});
