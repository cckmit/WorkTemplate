	var cmenu;
	var path = "http://blazefire.f3322.net:10097/st_ui/fo";
	$(function() {  
		var datagridId = 'grid';  
		$('#' + datagridId).resizeDataGrid(0,300);  
		$(window).resize(function() {  
			$('#' + datagridId).resizeDataGrid(0,300);  
		});  
	});
	function createColumnMenu() {
		cmenu = $('<div/>').appendTo('body');
		cmenu.menu({
			onClick: function(item) {
				if(item.iconCls == 'icon-ok') {
					$('#grid').datagrid('hideColumn', item.name);
					cmenu.menu('setIcon', {
						target: item.target,
						iconCls: 'icon-empty'
					});
				} else {
					$('#grid').datagrid('showColumn', item.name);
					cmenu.menu('setIcon', {
						target: item.target,
						iconCls: 'icon-ok'
					});
				}
			}
		});
		var fields = $('#grid').datagrid('getColumnFields');
		for(var i = 0; i < fields.length; i++) {
			var field = fields[i];
			var col = $('#grid').datagrid('getColumnOption', field);
			cmenu.menu('appendItem', {
				text: col.title,
				name: field,
				iconCls: 'icon-ok'
			});
		}
	}
	//导出文件
	function exportClick(id){
		var datagrid;
		$.messager.confirm({title:"导出EXCEL",ok: "本页", cancel: "全部", msg:"导出本页数据还是全部数据？", fn:function(n) {
			if(id){
				datagrid = $('#'+id);
			}else{
				datagrid = $('#grid');
			}
			var title = document.title;
			if(n){
				tableToExcel(datagrid, title, n);
			}else{
				var searchData = form2Json("searchform");
				searchData = searchData.replace("{", "%7B").replace("}", "%7D");
				window.location.href = operatorurl+"?datagrid=export&newPageSize=1&filename="+title+"&search-data="+searchData;
			}
		}});
	}
	//导入文件
	var istransfrom = false;
	function importClick(){
		$("#import-excel-template").dialog("open");
		$("#importFileForm").show();
	}
	function importFileClick() {
		var url = window.location.href;
		var lastindex = url.lastIndexOf("/");
		url = url.replace(url.substr(lastindex + 1), "file_temp.html")+"?id=importFileForm";
		$("#callbackurl").val(url);
		var opts = $("#grid").datagrid('options');
		var objs = opts.columns[0];
		var fieldcolumns = {};
		for(var i = 0; i < objs.length; i++) {
			var title = objs[i].title;
			var field = objs[i].field;
			fieldcolumns[title] = field;
		}
		$("#fieldcolums").val(encodeURI(JSON.stringify(fieldcolumns)));
		$('#importFileForm').attr("action", path).submit();
		$.messager.progress({
			title:'上传中',
			msg:'上传中...',
			text:''
		});
		clearFileBox('execlImport');
	}
	function callback(id, data) {
		$.messager.progress('close');
		if(id == 'importFileForm') {
			var html = strdecode(data);
			dialogClose("import-excel-template");
			istransfrom = false;
			$("#excelshow_tip").empty();
			$("#excelshow_tip").append(html);
			$("#excelshow").dialog("open");
		}
	}
	function dialogClose(id) {
		$('#' + id).dialog('close');
	}
	function excelshowSumbit(){
		if(!istransfrom) {
			$.messager.alert("提示信息", "请您先进行格式化！");
		} else {
			var d = $('#excelshow_table').datagrid('getRows');
			var columns = $('#grid').datagrid('getColumnFields');
			var array = new Array();
			for(var j in d){
				var obj = {};
				var flag = false;
				for(var i=0;i< columns.length;i++){
					var field = columns[i];
					obj[field] = d[j][field];
					if(d[j][field]){
						flag = true;
					}
				}
				if(flag){
					array.push(obj);
				}
			}
			var data = JSON.stringify(array);
			$.post(operatorurl, {
				type: 'excelsave',
				data: data
			}, function(val) {
				$.messager.alert("提示信息", val, "info", function(c) {
					dialogClose('excelshow');
					$("#grid").datagrid("load");
				});
			}, 'text');
		}
	}
	function transfrom() {
		istransfrom = true;
		var columns = $("#grid").datagrid("options").columns;
		$('#excelshow_table').datagrid({
			columns:columns
		});
	}
	function add(){
		$("#dlg").dialog('open').dialog('setTitle', '新增');
		url=url = operatorurl + "?operator=new";
		$("#ff").form('clear');
	}
	
	function edit(){
		var row = $("#grid").datagrid('getSelected');
		
		if(row)
		{
			$("#dlg").dialog("open").dialog('setTitle', '修改');
			url = operatorurl + "?operator=edit";
			$("#ff").form("clear");
			$("#ff").form("load", row);
		}else{
			$.messager.alert('提示','<div style="text-align:center;margin-top:15px">请选中一行数据!</div>');
		}
	}
	
	/**
	 * 批量操作多行数据
	 * @param idName 唯一标识的列名
	 * @param isCharType 是否是字符类型(字符类型会做单引号处理)
	 * @param operate 具体的操作名称(空字符串默认为删除)
	 */
	function multiRows(idName,isCharType,operate,confirmTip){
		var ids = [];
		var rows = $("#grid").datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
			if(isCharType){
				ids.push("'" + rows[i][idName] + "'");
			}else{
				ids.push(rows[i][idName]);
			}
			
		}
		if(rows.length >= 1){
			$.messager.confirm('确认',confirmTip,function(r){
				if(r){
					url = operatorurl + "?operator=operateMulti";
					$.post(url,{ids:ids.join(","),operate:operate},function(result){
						if(result == 1){
							$.messager.alert('提示','<div style="text-align:center;margin-top:15px">操作成功!</div>')
							$("#grid").datagrid('reload');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','<div style="text-align:center;margin-top:15px">请选中一行数据!</div>');
		}
	}
	
	function deleteRow(){
		var row = $("#grid").datagrid('getSelected');
		if(row){
			$.messager.confirm('删除','确认删除本行数据吗？',function(r){
				if(r){
					url = operatorurl + "?operator=delete";
					$.post(url,row,function(result){
						if(result == 1){
							$.messager.alert('提示','<div style="text-align:center;margin-top:15px">删除成功!</div>');
							$("#grid").datagrid('reload');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','<div style="text-align:center;margin-top:15px">请选中一行数据!</div>');
		}
	}
	
	function doSearch() {
		var searchdata = {};
		searchdata['search-data'] = form2Json("searchform");
		$('#grid').datagrid({
			queryParams: searchdata
		});
	}
	
	function form2Json(id) {
		var arr = $("#" + id).serializeArray();
		var jsonStr = "";
		var tempStr = "";
		for(var i = 0; i < arr.length; i++) {
			if(!$.trim(arr[i].value)){
				continue;
			}
			tempStr += '"' + arr[i].name + '":"' + arr[i].value + '",';
		}
		if(tempStr.length > 0){
			jsonStr = '{' + tempStr.substring(0, (tempStr.length - 1)) + '}';
		}
		
//		var json = JSON.parse(jsonStr)
		return jsonStr;
	}
	
	function cancel(){
		$("#ff").form('clear');
		$("#dlg").dialog('close');
	}
	function sort(name,type){
		var row = $("#grid").datagrid('getSelected');
		if(row)
		{
			$("#sortlg #sortlist").empty();
			var arr = new Array();
			var rowName=row[name];
			if(rowName != null && rowName != '') {
				arr = rowName.split(type);
			}
			for(var o in arr)
			{
				var t = arr[o];
				if(t.length <= 0)
					continue;
				var temp='<li class="drag-item" id="'+t+'">'+t+'</li>';
				$("#sortlg #sortlist").append(temp);
			}
			$("#sortlg").dialog("open").dialog('setTitle', '调整优先级');
			var indicator = $('<div class="indicator">>></div>').appendTo('#sortlist');
			$('.drag-item').draggable({
				revert:true,
				deltaX:0,
				deltaY:0
			}).droppable({
				onDragOver:function(e,source){
					/* $.messager.show({
						title: "坐标系",
						msg:"top="+$(this).css('top')+",id="+$(this).attr("id")
					}); */
					indicator.css({
						display:'block',
						left:10,
						top:$(this).offset().top-$("#sortlist").offset().top+2*$(this).outerHeight()-5
					});
				},
				onDragLeave:function(e,source){
					indicator.hide();
				},
				onDrop:function(e,source){
					$(source).insertAfter(this);
					indicator.hide();
				}
			});
		}else{
			$.messager.alert('提示','<div style="text-align:center;margin-top:15px">请选中一行数据!</div>');
		}
	}

		function cancelsort(){
			$("#sortlg").dialog('close');
		}
		function savesort(name,value,type)	{
				var data =$("#grid").datagrid('getSelected');
				var contentCode = data[name];
			    var contentCodes = contentCode.split(type);
				var contents = (data[value]).split(type);
				var sort="";
				$("#sortlg #sortlist li").each(function()
				{
					if(sort != "")
					{	
						sort+=type;
					}
					var li = $(this);
					var temp = li.text();
					for(var i in contentCodes){
						if(contentCodes[i] == temp){
							sort += contents[i];
						}
					}
				});			
				data[value]=sort;
				$.post(operatorurl+'?operator=edit',data,function(result){
					
					if(result == "1") {
						$.messager.alert("提示信息", "操作成功");
						$("#sortlg").dialog("close");
						$("#grid").datagrid("load");
					} else {
						$.messager.alert("提示信息", "操作失败");
					}
				},'json');
		}
		
		function operat_row(tip){
			var row = $("#grid").datagrid('getSelected');
			
			if(row)
			{
				$.messager.confirm('确认',tip,function(r){    
				    if (r){    
				        $("#ff").form("clear");
						$("#ff").form("load", row);
						url = operatorurl + "?operator=edit";
						$("#ff").form("submit", {
							url: url,
							success: function(result) {
								if(result == "1") {
									$.messager.alert("提示信息", "操作成功");
									$("#dlg").dialog("close");
									$("#grid").datagrid("load");
								} else {
									$.messager.alert("提示信息", "操作失败");
								}
							}
						});
				    }    
				});  

			}else{
				$.messager.alert('提示','<div style="text-align:center;margin-top:15px">请选中一行数据!</div>');
			}
		}
		
		function format_money(val,row){
			if(val == undefined){
				return '-';
			}
			return val.toFixed(2);
		}
		
		function format_success(val,row){
			if(val == undefined){
				return '-';
			}
			return val=='1'?'发货成功':'发货失败';
		}
		
		function format_null(val,row){
			if(val == undefined || val == ''){
				return '-';
			}
			return val;
		}
		
		function format_percent(val,row){
			if(val == undefined || val == ''){
				return '-';
			}
			return val+"%";
		}
		
		function format_percents(val,row){
			if(val == undefined || val == ''){
				return '-';
			}
			return val;
		}
		
		
		function format_yesorno(val,row){
			if(val == undefined || val == ''){
				return '-';
			}
			if(val=='1')
			{
				return '是';
			}
			else if(val == '2')
			{
				return '否';
			}
			return "-";
		}
		
		var bussiness_map = {};
		/**
		 * 初始化业务下拉框
		 * @param combobox_id 
		 */
		function init_bus_combobox(combobox_id,isByAccountPermiss){
			$.ajaxSettings.async = false;
			var url = "boysp_cd?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				for(var i=0;i<rows.length;i++){
					bussiness_map[rows[i].channel_id] = rows[i];
				}
				
				if(combobox_id == undefined){
					return;
				}
				
				$("#"+combobox_id).combobox({
					valueField:"channel_id",
					textField:"channel_name",
					data:rows
				});
			});
		}
		
		var in_cp_rows = [];
		/**
		 * 初始化收入模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_in_cp_name_combobox(combobox_id,search){
			$.ajaxSettings.async = false;
			var url = "in_cp?datagrid=data&newPageSize=1";
			if(search){
				url += "&"+search;
			}
			$.getJSON(url,function(data){
				var rows = data.rows;
				in_cp_rows = rows;
				$("#"+combobox_id).combobox({
					valueField:"id",
					textField:"cp_name",
					data:rows
				});
			});
		}
		
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_out_cp_name_combobox(combobox_id){
			var url = "out_cp?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"id",
					textField:"cp_name",
					data:rows
				});
			});
		}
		
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_gamename_combobox(combobox_id,onselect){
			var url = "mini_game?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"game_id",
					textField:"game_name",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_appid_combobox(combobox_id,onselect){
			var url = "mini_fixed_old_community?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"appid",
					textField:"name",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_game_spread_combobox(combobox_id,onselect){
			var url = "mini_game?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"game_spread",
					textField:"game_spread",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		
		/**
		 * 初始化cp产品信息
		 * @param combobox_id 
		 */
		function init_cpgamename_combobox(combobox_id,onselect){
			var url = "cpmini_gameinfo?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"id",
					textField:"cpgame_name",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		
		function init_coursename_combobox(combobox_id,onselect){
			var url = "course?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"course_id",
					textField:"course_name",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		
		
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_miniad_combobox(combobox_id,onselect){
			var url = "mini_ad?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"ad_id",
					textField:"ad_name",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		/**
		 * 初始化支出模块合作方下拉框(特殊)
		 * @param combobox_id 
		 */
		function init_gamename_comboboxID(combobox_id,name,onselect){
			var url = "mini_game?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"game_id",
					textField:name,
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
		}
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_gamename_comboboxTo(combobox_id,onselect){
			var url = "mini_game?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"game_appid",
					textField:"game_name",
					data:rows,
					onSelect:function(rec){    
						onselect(rec);    
			        }
				});
			});
			
		}
		
		
		/**
		 * 初始化输入模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_in_cp_finance_combobox(combobox_id){
			var url = "in_cp_finance?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"id",
					textField:"invoice_title",
					data:rows
				});
			});
		}
		
		/**
		 * 初始化支出模块合作方下拉框
		 * @param combobox_id 
		 */
		function init_out_cp_finance_combobox(combobox_id){
			var url = "out_cp_finance?datagrid=data&newPageSize=1";
			$.getJSON(url,function(data){
				var rows = data.rows;
				$("#"+combobox_id).combobox({
					valueField:"id",
					textField:"cp_company_name",
					data:rows
				});
			});
		}
		
		/**
		 * 初始化所属月份下拉框
		 * @param combobox_id 
		 */
		function init_months_combobox(combobox_id){
			var url = "boysp_data_fin?datagrid=spec_json&spec_type=getAllMonths";
			$.getJSON(url,function(data){
				var rows = [];
				$.each(data,function(index,element){
					var obj = {};
					obj.id = element;
					obj.month = element;
					rows.push(obj);
				})
				rows.unshift({'id':'','month':'全部'});
				$("#"+combobox_id).combobox({
					valueField:"id",
					textField:"month",
					data:rows
				});
			});
		}
		
	    //虚拟表单进行提交
        function form_post(URL, PARAMS) {        
            var temp = document.createElement("form");        
            temp.action = URL;        
            temp.method = "post";        
            temp.style.display = "none";        
            for (var x in PARAMS) {        
                var opt = document.createElement("textarea");        
                opt.name = x;        
                opt.value = PARAMS[x];        
                // alert(opt.name)        
                temp.appendChild(opt);        
            }        
            document.body.appendChild(temp);        
            temp.submit();        
            return temp;        
        }
		
		function displayFor(domId,roleName){
			var role ;
			$.ajaxSettings.async = false;
			$.getJSON("account?datagrid=spec_json",function(data){
				role = data.role;
			});
			
			if(role == 'admin'){
				return;
			}
			if(roleName == role){
				$("#"+domId).show();
			}else{
				$("#"+domId).hide();
			}
		}
		
		//格式化收入开票差额
		function format_bill_diff(val,row){
			if(row.pre_money==undefined || row.bill_money==undefined){
				return '-';
			}
			var pre = Number(row.pre_money);
			var bill = Number(row.bill_money);
			if(pre == 0){
				if(pre == bill){
					return '0%';
				}
				return '-';
			}
			return toPercent((bill - pre) , pre);
		}
		//格式化收入到款差额
		function format_received_diff(val,row){
			if(row.received_money==undefined || row.bill_money==undefined){
				return '-';
			}
			var bill = Number(row.bill_money);
			var rec = Number(row.received_money);
			if(bill == 0){
				if(bill == rec){
					return '0%';
				}
				return '-';
			}
			return toPercent((rec - bill) , bill);
		}
		//格式化支出到款差额
		function format_out_pay_diff(val,row){
			if(row.pay_money==undefined || row.bill_money==undefined){
				return '-';
			}
			var bill = Number(row.bill_money);
			var rec = Number(row.pay_money);
			if(bill == 0){
				if(bill == rec){
					return '0%';
				}
				return '-';
			}
			return toPercent((rec - bill) , bill);
		}
		
		function toPercent(a,b){
		    return ((a/b)*100).toFixed(2)  + '%' ;
		}
		
		//选中行时动态计算开票金额
		var sumPreMoney = 0;
		var sumBillMoney = 0;
		var sumRemitMoney = 0;
		function onGridRowSelectedMuti(gridId,isOut){
			$("#"+gridId).datagrid({
				onSelect:function(index,row){
					if(row.pre_money != undefined){
						sumPreMoney += Number(row.pre_money);
					}
					if(row.bill_money != undefined){
						sumBillMoney += Number(row.bill_money);
					}
					var pay = isOut ? row.pay_money : row.received_money;
					if(pay != undefined){
						sumRemitMoney += Number(pay);
					}
					$("#sum_pre_span").text(sumPreMoney.toFixed(2));
					$("#sum_bill_span").text(sumBillMoney.toFixed(2));
					$("#sum_remit_span").text(sumRemitMoney.toFixed(2));
					$("#parent_sum_span").show();
				},
				onUnselect:function(index,row){
					if(row.pre_money != undefined){
						sumPreMoney -= Number(row.pre_money);
					}
					if(row.bill_money != undefined){
						sumBillMoney -= Number(row.bill_money);
					}
					var pay = isOut ? row.pay_money : row.received_money;
					if(pay != undefined){
						sumRemitMoney -= Number(pay);
					}
					$("#sum_pre_span").text(sumPreMoney.toFixed(2));
					$("#sum_bill_span").text(sumBillMoney.toFixed(2));
					$("#sum_remit_span").text(sumRemitMoney.toFixed(2));
				},
				onLoadSuccess:function(data){
					$("#sum_pre_span").text('');
					$("#sum_bill_span").text('');
					$("#sum_remit_span").text('');
					sumPreMoney = 0;
					sumBillMoney = 0;
					sumRemitMoney = 0;
					$("#parent_sum_span").hide();
				}
			});
		}
		
		//选中行时动态计算开票金额
		var sumMoney = 0;
		function onGridRowSelected(gridId,spanId,isOut){
			$("#"+gridId).datagrid({
				onSelect:function(index,row){
					if(row.bill_money != undefined){
						sumMoney += Number(isOut ? row.pay_money : row.bill_money);
					}
					$("#"+spanId).text(sumMoney.toFixed(2));
					$("#parent_sum_span").show();
				},
				onUnselect:function(index,row){
					if(row.bill_money != undefined){
						sumMoney -= Number(isOut ? row.pay_money : row.bill_money);
					}
					$("#"+spanId).text(sumMoney.toFixed(2));
				},
				onLoadSuccess:function(data){
					$("#"+spanId).text('');
					sumMoney = 0;
					$("#parent_sum_span").hide();
				}
			});
		}
		
		//为开票信息表单加载数据
		function loadFinanceForm(finance_id, formId){
			var searchData = "{'id':"+finance_id+"}";
			var finance_url = "in_cp_finance?datagrid=data&search-data="+searchData;
			$.getJSON(finance_url,{},function(data){
				$("#"+formId).form("load",data.rows[0]);
			});
		}
		
		//为开票信息表单加载数据
		function loadOutFinanceForm(finance_id, formId){
			var searchData = "{'id':"+finance_id+"}";
			var finance_url = "out_cp_finance?datagrid=data&search-data="+searchData;
			$.getJSON(finance_url,{},function(data){
				$("#"+formId).form("load",data.rows[0]);
			});
		}
		
		//编辑实际开票金额后动态计算汇总数据
		function afterEditingCell(){
			var rows = $('#recon_dg').datagrid('getRows');
			var sum_pre=0;
			var sum_actual=0;
			var sum_diff=0;
			$.each(rows,function(index,ele){
				if(ele.pre_income != undefined){
					sum_pre += ele.pre_income;
				}
				if(ele.actual_income != undefined){
					sum_actual += Number(ele.actual_income);
				}
				sum_diff = sum_pre - sum_actual;
			})
			$('#sum_dg').datagrid('updateRow',{
				index: 0,
				row: {sum_pre_income:sum_pre.toFixed(2),
					sum_actual_income:Number(sum_actual).toFixed(2),
					sum_difference:sum_diff.toFixed(2),
					sum_difference_rate:(sum_diff/sum_pre*100).toFixed(2)+"%"}
			});
		}
		
		//编辑实际开票金额后动态计算汇总数据
		function afterOutEditingCell(){
			var rows = $('#recon_dg').datagrid('getRows');
			var sum_pre=0;
			var sum_actual=0;
			var sum_diff=0;
			$.each(rows,function(index,ele){
				if(ele.pre_pay != undefined){
					sum_pre += ele.pre_pay;
				}
				if(ele.actual_pay != undefined){
					sum_actual += Number(ele.actual_pay);
				}
				sum_diff = sum_pre - sum_actual;
			})
			$('#sum_dg').datagrid('updateRow',{
				index: 0,
				row: {sum_pre_income:sum_pre.toFixed(2),
					sum_actual_income:Number(sum_actual).toFixed(2),
					sum_difference:sum_diff.toFixed(2),
					sum_difference_rate:(sum_diff/sum_pre*100).toFixed(2)+"%"}
			});
		}
		
		//获取详细对账产品信息的url
		var product_recon_url = '';
		//查看对账
		function scanRecon(){
			//判断是否选择了多行
			var muti_rows=$("#grid").datagrid('getSelections');
			if(muti_rows.length > 1){
				$.messager.alert('提示','<div style="text-align:center;margin-top:15px">仅能选中一行进行操作!</div>');
				return;
			}
			
			var row = $("#grid").datagrid('getSelected');
			if(row)
			{
				if(row.sericode == -1){
					$.messager.alert('提示','<div style="text-align:center;margin-top:15px">此记录暂未对账!</div>');
					return;
				}
				$("#in_cp_finance_ff").form("clear");
				$("#in_cp_finance_dlg").dialog("open").dialog('setTitle', '查看对账');
				
				init_bus_combobox('finance_bussiness_id');
				init_in_cp_name_combobox('finance_cp_id');
				loadFinanceForm(row.cp_finance_id, 'in_cp_finance_ff');
				
				product_recon_url = "in_bill_ToBill?datagrid=spec_json&spec_type=getReconInfos&bill_id="+row.sericode;
				$('#recon_dg').datagrid({    
					iconCls: 'icon-edit',
				    url:product_recon_url,   
				    method:'get',
				    width:700,
				    idField:'id',
				    fitColumns: true,
				    fit:false,
				    onLoadSuccess:afterEditingCell,
				    singleSelect:true,
				    checkOnSelect: false,
				    selectOnCheck: false,
				    columns:[[    
				        {field:'id',title:'id',hidden:true},    
				        {field:'product_name',title:'产品名称',width:50},    
				        {field:'pre_income',title:'预计开票金额',width:25},    
				        {field:'actual_income',title:'实际开票金额',width:25},   
				        {field:'difference',title:'差异',width:25,
				        	formatter : function(val, row, index) {  
			                    if (val == undefined) {  
			                        return '-';  
			                    } else {  
			                        return val.substr(0,val.indexOf(".")+3);  
			                    }  
			                }  
				        },      
				        {field:'mark',title:'备注',width:40}
				    ]]    
				});   

				$('#recon_dg').datagrid('reload');
			}else{
				$.messager.alert('提示','<div style="text-align:center;margin-top:15px">请选中一行数据!</div>');
			}
		}
		
		//导入excel
		function import_excel(){
			$("#excel_dlg").dialog('open').dialog('setTitle', '导入Excel');
			url=url = operatorurl + "?type=otherPost";
			$("#excel_ff").form('clear');
		}
		
		function excel_save() {
			$("#excel_ff").form("submit", {
				url: url,
				onSubmit: function() {
					return $(this).form("validate");
				},
				success: function(result) {
					if(result == "1") {
						$.messager.alert("提示信息", "操作成功");
						$("#excel_dlg").dialog("close");
						$("#grid").datagrid("load");
					} else {
						$.messager.alert("提示信息", "操作失败");
					}
				}
			});
		}
		
		
		/**
		 * 点击对账编辑后初始化cp对应的开票信息列表
		 * @param cp_id
		 */
		function initInvoiceInfos(cp_id){
			var invoiceInfoUrl = "in_data_ByCp?datagrid=spec_json&spec_type=getInoviceInfos";
			if(cp_id){
				invoiceInfoUrl = invoiceInfoUrl+"&cp_id="+cp_id;
			}
			//$.ajaxSettings.async = false;
			var finance_arr = [];
			$.getJSON(invoiceInfoUrl,{},function(data){
				
				for(var i=0;i<data.length;i++){
					var obj = {};
					obj["id"]=data[i].id;
					//对私
					if(data[i].is_private){
						obj["text"]=bussiness_map[data[i].bussiness_id].bussiness_name 
									+ "-" + data[i].to_private_name + "-" + data[i].invoice_type + "-" + data[i].billing_company_name;
					}else{
						obj["text"]=bussiness_map[data[i].bussiness_id].bussiness_name 
									+ "-" + data[i].invoice_title + "-" + data[i].invoice_type + "-" + data[i].billing_company_name+"-"+data[i].invoice_content;
					}
					finance_arr.push(obj);
				}
				
				$("#cp_finance_id").combobox({
					valueField:"id",
					textField:"text",
					data:finance_arr
				});
			});
		}
		
		function getJson(url,input){
			var json ;
			$.ajaxSettings.async = false;
			$.getJSON(url,input,function(data){
				json = data;
			}); 
			return json;
		}
		function zsgc()
		{
			$.ajax({
				type : "POST", //post提交方式默认是get
				url : "acServlet",
				data :{},
				error : function(request) { // 设置表单提交出错                 
					$.messager.alert("提示信息", "页面错误"); //登录错误提示信息
				},
				async:false,
				success : function(data) {
					if(data !== null && data !== undefined && data !== '')
					{
						/*if(data.indexOf("0") != -1)
						{
							$("#search").css("display","inline-block");
						}*/
						if(data.indexOf("1") != -1)
						{
							$("#add").css("display","inline-block");
						}
						if(data.indexOf("2") != -1)
						{
							$("#edit").css("display","inline-block");
						}
						if(data.indexOf("3") != -1)
						{
							$("#saveout").css("display","inline-block");
						}
					}
				}
			}); 
		}