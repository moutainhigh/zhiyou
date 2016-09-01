/*******************************************************************************
 * Wrapper/Helper Class for datagrid based on jQuery Datatable Plugin
 ******************************************************************************/
var Datatable = function() {

	var tableOptions; // main options
	var dataTable; // datatable object
	var table; // actual table jquery object
	var tableContainer; // actual table container object
	var tableWrapper; // actual table wrapper jquery object
	var tableInitialized = false;
	var ajaxParams = {}; // set filter mode
	var the;

	var countSelectedRecords = function() {
		var selected = $('tbody > tr > td:nth-child(1) input[type="checkbox"]:checked', table).size();
		var text = tableOptions.dataTable.language.metronicGroupActions;
		if (selected > 0) {
			$('.table-row-checked', tableContainer).text(text.replace("_TOTAL_", selected));
		} else {
			$('.table-row-checked', tableContainer).text("");
		}
	};

	return {

		// main function to initiate the module
		init : function(options) {

			if (!$().dataTable) {
				return;
			}

			the = this;

			// default settings
			options = $.extend(true, {
				src : "", // actual table
				filterApplyAction : "filter",
				filterCancelAction : "filter_cancel",
				resetGroupActionInputOnSuccess : true,
				loadingMessage : '努力加载中...',
				dataTable : {
					// "dom": "<'row'<'col-md-8 col-sm-12'pli><'col-md-4
					// col-sm-12'<'table-group-actions
					// pull-right'>>r><'table-scrollable't><'row'<'col-md-8
					// col-sm-12'pli><'col-md-4 col-sm-12'>>", // datatable layout
					"dom" : "<'table-responsive't><'row'<'col-md-6 col-sm-12'<'margin-top-10'li>><'col-md-6 col-sm-12'<'pull-right'p>>r>", // datatable
																																																																	// layout
					"pageLength" : 10, // default records per page
					"language" : { // language settings
						// metronic spesific
						"metronicGroupActions" : "选中_TOTAL_条记录.",
						"metronicAjaxRequestGeneralError" : "没有响应,请检查网络连接.",
						// data tables spesific
						"lengthMenu" : "每页 _MENU_ 条记录",
						"info" : "<span class='seperator'>|</span>显示 _START_ 至 _END_ 条 &nbsp;&nbsp; 共 _TOTAL_ 条记录",
						"infoEmpty" : "无记录.",
						"emptyTable" : "没有数据.",
						"zeroRecords" : "没有符合条件的记录.",
						"paginate" : {
							"previous" : "上一页",
							"next" : "下一页",
							"last" : "尾页",
							"first" : "首页",
							"page" : "第",
							"pageOf" : "页"
						},
					},

					"orderCellsTop" : true,
					"columnDefs" : [ { // define columns sorting options(by
						// default all columns are sortable extept
						// the first checkbox column)
						'orderable' : false,
						'targets' : [ 0 ]
					} ],

					"pagingType" : "bootstrap_full_number", // pagination
					// type(bootstrap,
					// bootstrap_full_number
					// or
					// bootstrap_extended)
					"autoWidth" : false, // disable fixed width and enable fluid
					// table
					"processing" : false, // enable/disable display message box on
					// record load
					"serverSide" : true, // enable/disable server side ajax
					// loading

					"ajax" : { // define ajax settings
						"url" : "", // ajax URL
						"type" : "POST", // request type
						"timeout" : 20000,
						"data" : function(data) { // add request
							// parameters before
							// submit
							
							var pageNumber, startRow, pageSize, orderBy = '', direction = '';
							var colNames = new Array();

							$.each(data, function(key, value) {
								if (key == 'start') {
									startRow = value;
								} else if (key == 'length') {
									pageSize = value;
								} else if (key == 'order') {
									$.each(value, function(n, sortColDir) {
										$.each(sortColDir, function(key2, value2) {
											// alert(n + "/" + key2 + "/" + value2);
											if (key2 == 'column') {
												orderBy = value2;
											} else if (key2 == 'dir') {
												direction = value2;
											}
										});
									});
								} else if (key == 'columns') {
									$.each(value, function(n, columns) {
										$.each(columns, function(key2, value2) {
											if (key2 == 'data') {
												colNames[n] = value2;
											}
										});
									});
								}
							});
							if (colNames.length > 0 && orderBy != '') {
								orderBy = colNames[orderBy];
		                    }
							
							pageNumber = Math.floor(startRow / pageSize);
							$('#_pageNumber').val(pageNumber);
							$('#_pageSize').val(pageSize);
							$('#_orderBy').val(orderBy);
							$('#_direction').val(direction);

							// alert(pageNumber + "/" + pageSize + "/" + sort);

							the.submitFilter();

							$.each(ajaxParams, function(key, value) {
								if (value) {
									data[key] = value;
								}
							});

							Metronic.blockUI({
								message : tableOptions.loadingMessage,
								target : tableContainer,
								overlayColor : 'none',
								cenrerY : true,
								boxed : true
							});
						},
						
						/*
						"data" : function(data) { // add request parameters before
							// submit

							var pageNumber, startRow, pageSize, sort = '';
							var colNames = new Array();
							var sortCols = new Array();
							var sortDirs = new Array();

							$.each(data, function(key, value) {
								if (key == 'start') {
									startRow = value;
								} else if (key == 'length') {
									pageSize = value;
								} else if (key == 'order') {
									$.each(value, function(n, sortColDir) {
										$.each(sortColDir, function(key2, value2) {
											// alert(n + "/" + key2 + "/" + value2);
											if (key2 == 'column') {
												sortCols[n] = value2;
											} else if (key2 == 'dir') {
												sortDirs[n] = value2;
											}
										});
									});
								} else if (key == 'columns') {
									$.each(value, function(n, columns) {
										$.each(columns, function(key2, value2) {
											if (key2 == 'data') {
												colNames[n] = value2;
											}
										});
									});
								}
							});
							if (sortCols.length > 0) {
								for ( var i in sortCols) {
									var colName = colNames[sortCols[i]];
									if (colName == 'function') {
										continue;
									}
									if (typeof (colName) == 'string') {
										if (sort) {
											sort += '|';
										}
										sort += (colName + ':' + sortDirs[i]);
									}
								}
							}

							pageNumber = Math.floor(startRow / pageSize);
							$('#_pageNumber').val(pageNumber);
							$('#_pageSize').val(pageSize);
							$('#_sort').val(sort);

							// alert(pageNumber + "/" + pageSize + "/" + sort);

							the.submitFilter();

							$.each(ajaxParams, function(key, value) {
								if (value) {
									data[key] = value;
								}
							});

							Metronic.blockUI({
								message : tableOptions.loadingMessage,
								target : tableContainer,
								overlayColor : 'none',
								cenrerY : true,
								boxed : true
							});
						},
						*/
						"dataSrc" : function(res) { // Manipulate the data returned
							// from the server
							if (res.customActionMessage) {
								Metronic.alert({
									type : (res.customActionStatus == 'OK' ? 'success' : 'danger'),
									icon : (res.customActionStatus == 'OK' ? 'check' : 'warning'),
									message : res.customActionMessage,
									container : tableWrapper,
									place : 'prepend'
								});
							}

							if (res.customActionStatus) {
								if (tableOptions.resetGroupActionInputOnSuccess) {
									$('.table-group-action-input', tableWrapper).val("");
								}
							}

							if ($('.group-checkable', table).size() === 1) {
								$('.group-checkable', table).attr("checked", false);
								$.uniform.update($('.group-checkable', table));
							}

							$('#_pageNumber').val(res.pageNumber);
							$('#_pageSize').val(res.pageSize);

							if (tableOptions.onSuccess) {
								tableOptions.onSuccess.call(undefined, the);
							}

							Metronic.unblockUI(tableContainer);

							return res.data;
						},
						"error" : function() { // handle general connection errors
							if (tableOptions.onError) {
								tableOptions.onError.call(undefined, the);
							}

							Metronic.alert({
								type : 'danger',
								icon : 'warning',
								message : tableOptions.dataTable.language.metronicAjaxRequestGeneralError,
								container : tableWrapper,
								place : 'prepend'
							});

							Metronic.unblockUI(tableContainer);
						}
					},

					"drawCallback" : function(oSettings) { // run some code on
						// table redraw
						if (tableInitialized === false) { // check if table has been
							// initialized
							tableInitialized = true; // set table initialized
							table.show(); // display table
						}
						Metronic.initUniform($('input[type="checkbox"]', table)); // reinitialize
						// uniform
						// checkboxes
						// on
						// each
						// table
						// reload
						countSelectedRecords(); // reset selected records indicator
					}
				}
			}, options);

			tableOptions = options;

			// create table's jquery object
			table = $(options.src);
			tableContainer = table.parents(".table-container");

			// apply the special class that used to restyle the default datatable
			var tmp = $.fn.dataTableExt.oStdClasses;

			$.fn.dataTableExt.oStdClasses.sWrapper = $.fn.dataTableExt.oStdClasses.sWrapper + " dataTables_extended_wrapper";
			$.fn.dataTableExt.oStdClasses.sFilterInput = "form-control input-small input-sm input-inline";
			$.fn.dataTableExt.oStdClasses.sLengthSelect = "form-control input-xsmall input-sm input-inline";

			// initialize a datatable
			dataTable = table.DataTable(options.dataTable);

			// revert back to default
			$.fn.dataTableExt.oStdClasses.sWrapper = tmp.sWrapper;
			$.fn.dataTableExt.oStdClasses.sFilterInput = tmp.sFilterInput;
			$.fn.dataTableExt.oStdClasses.sLengthSelect = tmp.sLengthSelect;

			// get table wrapper
			tableWrapper = table.parents('.dataTables_wrapper');

			// build table group actions panel
			if ($('.table-actions-wrapper', tableContainer).size() === 1) {
				$('.table-group-actions', tableWrapper).html($('.table-actions-wrapper', tableContainer).html()); // place
				// the
				// panel
				// inside
				// the
				// wrapper
				$('.table-actions-wrapper', tableContainer).remove(); // remove the
				// template
				// container
			}
			// handle group checkboxes check/uncheck
			$('.group-checkable', table).change(function() {
				var set = $('tbody > tr > td:nth-child(1) input[type="checkbox"]', table);
				var checked = $(this).is(":checked");
				$(set).each(function() {
					$(this).attr("checked", checked);
				});
				$.uniform.update(set);
				countSelectedRecords();
			});

			// handle row's checkbox click
			table.on('change', 'tbody > tr > td:nth-child(1) input[type="checkbox"]', function() {
				countSelectedRecords();
			});

			// handle filter submit button click
			tableContainer.on('click', '.filter-submit', function(e) {
				e.preventDefault();
				// the.submitFilter();
				dataTable.ajax.reload();
			});

			// handle filter cancel button click
			tableContainer.on('click', '.filter-cancel', function(e) {
				e.preventDefault();
				the.resetFilter();
			});
		},

		submitFilter : function() {
			// the.setAjaxParam("action", tableOptions.filterApplyAction);

			the.clearAjaxParams();

			// get all typeable inputs
			var tableFilters = $('.filter-form', tableContainer);

			$('textarea, select, input:not([type="radio"],[type="checkbox"])', tableFilters).each(function() {
				the.setAjaxParam($(this).attr("name"), $(this).val());
			});

			// get all checkboxes
			$('input[type="checkbox"]:checked', tableFilters).each(function() {
				the.addAjaxParam($(this).attr("name"), $(this).val());
			});

			// get all radio buttons
			$('input[type="radio"]:checked', tableFilters).each(function() {
				the.setAjaxParam($(this).attr("name"), $(this).val());
			});
		},

		resetFilter : function() {
			$('textarea, select, input', table).each(function() {
				$(this).val("");
			});
			$('input[type="checkbox"]', table).each(function() {
				$(this).attr("checked", false);
			});
			the.clearAjaxParams();
			the.addAjaxParam("action", tableOptions.filterCancelAction);
			dataTable.ajax.reload();
		},

		getSelectedRowsCount : function() {
			return $('tbody > tr > td:nth-child(1) input[type="checkbox"]:checked', table).size();
		},

		getSelectedRows : function() {
			var rows = [];
			$('tbody > tr > td:nth-child(1) input[type="checkbox"]:checked', table).each(function() {
				rows.push($(this).val());
			});

			return rows;
		},

		setAjaxParam : function(name, value) {
			ajaxParams[name] = value;
		},

		addAjaxParam : function(name, value) {
			if (!ajaxParams[name]) {
				ajaxParams[name] = [];
			}

			skip = false;
			for (var i = 0; i < (ajaxParams[name]).length; i++) { // check for
				// duplicates
				if (ajaxParams[name][i] === value) {
					skip = true;
				}
			}

			if (skip === false) {
				ajaxParams[name].push(value);
			}
		},

		clearAjaxParams : function(name, value) {
			ajaxParams = {};
		},

		getDataTable : function() {
			return dataTable;
		},

		getTableWrapper : function() {
			return tableWrapper;
		},

		getTableContainer : function() {
			return tableContainer;
		},

		getTable : function() {
			return table;
		}

	};

};
