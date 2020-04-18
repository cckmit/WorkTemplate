function tableToExcel(tableid, name, n,data) {
	var total = tableid.datagrid('getData').total;
	var url = operatorurl;
	var frozenColumns = tableid.datagrid("options").frozenColumns; // 得到frozenColumns对象  
	var columns = tableid.datagrid("options").columns; // 得到columns对象  
	var tableString = '<table id="excelshow_table" cellspacing="0" style="width:100%;background-color:#FFFFFF;">';
	var nameList = new Array();
	//加载th
	if(typeof columns != 'undefined' && columns != '') {
		$(columns).each(function(index) {
			tableString += '\n<thead><tr style="border:0.5px solid #C5C5C5;">';
			if(typeof frozenColumns != 'undefined' && typeof frozenColumns[index] != 'undefined') {
				for(var i = 0; i < frozenColumns[index].length; ++i) {
					//if (!frozenColumns[index][i].hidden) {  
					//tableString += '\n<th width="' + frozenColumns[index][i].width + '"';  
					tableString += '\n<th ';
					if(typeof frozenColumns[index][i].rowspan != 'undefined' && frozenColumns[index][i].rowspan > 1) {
						tableString += ' rowspan="' + frozenColumns[index][i].rowspan + '"';
					}
					if(typeof frozenColumns[index][i].colspan != 'undefined' && frozenColumns[index][i].colspan > 1) {
						tableString += ' colspan="' + frozenColumns[index][i].colspan + '"';
					}
					if(typeof frozenColumns[index][i].field != 'undefined' && frozenColumns[index][i].field != '') {
						nameList.push(frozenColumns[index][i]);
					}
					tableString += ' data-options="field:\'' + frozenColumns[0][i].field + '\'">' + frozenColumns[0][i].title + '</th>';
					//}
				}
			}
			for(var i = 0; i < columns[index].length; ++i) {
				//if (!columns[index][i].hidden) {  
				//tableString += '\n<th width="' + columns[index][i].width + '"'; 
				tableString += '\n<th ';
				if(typeof columns[index][i].rowspan != 'undefined' && columns[index][i].rowspan > 1) {
					tableString += ' rowspan="' + columns[index][i].rowspan + '"';
				}
				if(typeof columns[index][i].colspan != 'undefined' && columns[index][i].colspan > 1) {
					tableString += ' colspan="' + columns[index][i].colspan + '"';
				}
				if(typeof columns[index][i].field != 'undefined' && columns[index][i].field != '') {
					nameList.push(columns[index][i]);
				}
				tableString += ' data-options="field:\'' + columns[index][i].field + '\'">' + columns[index][i].title + '</th>';
				//}  
			}
			tableString += '\n</tr></thead>';
		});
	}
	if(n) {
		var rows = tableid.datagrid("getRows");
		exportExcel(rows, tableString, nameList);
	} else {
		if(data){
			$.getJSON(url, data, function(json) {
				var rows = json.rows;
				exportExcel(rows, tableString, nameList);
			});
		}else{
			$.getJSON(url, {}, function(json) {
				var rows = json.rows;
				exportExcel(rows, tableString, nameList);
			});
		}
	}
}

function exportExcel(rows, tableString, nameList) {
	var uri = 'data:application/vnd.ms-excel;base64,',
	template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body>{table}</body></html>';
	for(var i = 0; i < rows.length; ++i) {
		tableString += '\n<tr>';
		for(var j = 0; j < nameList.length; ++j) {
			var e = nameList[j].field.lastIndexOf('_0');
			tableString += '\n<td';
			if(nameList[j].align != 'undefined' && nameList[j].align != '') {
				tableString += ' style="vnd.ms-excel.numberformat:@;border:0.5px solid #C5C5C5; text-align:' + nameList[j].align + ';"';
			}
			tableString += '>';
			if(e + 2 == nameList[j].field.length) {
				tableString += rows[i][nameList[j].field.substring(0, e)];
			} else if(rows[i][nameList[j].field] == undefined){
				tableString += '';
			} else{
				tableString += rows[i][nameList[j].field];
			}
			tableString += '</td>';
		}
		tableString += '\n</tr>';
	}
	tableString += '\n</table>';
	base64 = function(s) {
		return window.btoa(unescape(encodeURIComponent(s)))
	}, format = function(s, c) {
		return s.replace(/{(\w+)}/g, function(m, p) {
			return c[p];
		})
	}
	var ctx = {
		worksheet: name || 'Worksheet',
		table: tableString
	}
	window.location.href = uri + base64(format(template, ctx));
}
function clearFileBox(fileImport){
$('#'+fileImport).val('');
$('#'+fileImport).filebox('setValue','');
$('#'+fileImport).filebox('reset');
}
$(function() {  
var datagridId = 'dg';  
$('#' + datagridId).resizeDataGrid(0,300);  
$(window).resize(function() {  
$('#' + datagridId).resizeDataGrid(0,300);  
});  
});