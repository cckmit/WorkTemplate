<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head lang="en">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="-1">
<meta charset="UTF-8">
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
	//官方平滑树  
	function convert(rows) {
		function exists(rows, parentId) {
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].id == parentId)
					return true;
			}
			return false;
		}

		var nodes = [];
		// get the top level nodes  
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (!exists(rows, row.parentId)) {
				if(row.url){
					nodes.push({
						id : row.id,
						text : row.name,
						memo : row.memo,
						url  : row.url
					});
				}else{
					nodes.push({
						id : row.id,
						text : row.name,
						memo : row.memo
					});
				}
			}
		}

		var toDo = [];
		for (var i = 0; i < nodes.length; i++) {
			toDo.push(nodes[i]);
		}
		while (toDo.length) {
			var node = toDo.shift(); // the parent node  
			// get the children nodes  
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (row.parentId == node.id) {
					var child = {
						id : row.id,
						text : row.name,
						memo : row.memo,
						url : row.url
					};
					if (node.children) {
						node.children.push(child);
					} else {
						node.children = [ child ];
					}
					toDo.push(child);
				}
			}
		}
		return nodes;
	}

	function addNewTab(tabname, url) {
		//创建一个新的窗口，在mainlayout上  
		var text = "<iframe src='"
				+ url
				+ "'style='width:100%;height:100%'  frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='yes' />";

		if (!$("#mainTabs").tabs('getTab', tabname)) {
			$("#mainTabs").tabs('add', {
				title : tabname,
				selected : true,
				closable : true,
				content : text
			});
			//双击选项卡折叠或展开左侧树状菜单
			$("#mainTabs").tabs('bindDblclick', function(index, title){
		        var p = $("#main_layout").layout("panel", "west")[0].clientWidth;  
	            if (p > 0)  
	                $('#main_layout').layout('collapse', 'west');  
	            else  
	                $('#main_layout').layout('expand', 'west'); 
		    });
		} else {
			$('#mainTabs').tabs('select', tabname);
		}
	}

	$(function() {
		$.getJSON('tree_data.json', {}, function(val) {
			$('#mytree').tree({
				data : val,
				loadFilter : function(rows) {
					var tree = convert(rows);
					var role = ${sessionScope.account.role};
					if(role != 0){
						return convert(${sessionScope.perssionJson});
					}
					return tree;
				}
			});
		});

		//点击事件  
		$('#mytree').tree({
			onClick : function(node) {
				if ($('#mytree').tree('isLeaf', node.target)) {
					addNewTab(node.text, node.url);
				}
			}
		});
	});
</script>

<link rel="stylesheet" type="text/css" href="../css/easyui.css" />
<link rel="stylesheet" type="text/css" href="../css/icon.css" />
<title>FC小游戏运营后台</title>
</head>

<body onload="clockon(bgclock)">
	<div class="easyui-layout" id="main_layout" data-options="fit:true">
		<div data-options="region:'north'"
			style="height:30px;background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #7777cc), color-stop(1,#aaaaee)) ;">
			<div style="margin-top: 3px; float: left;">
				<span style="font-family: '微软雅黑';font-size: 20px;">FC小游戏运营后台<span>
			</div>
			<div style="margin-top: 3px;margin-right:10px; float: right;">
				<span style="font-family: '微软雅黑';font-size: 20px;a{color: #00ffff;}">
					<a>hi</a> <span id="account"></span> ,${sessionScope.account.name} <a>|</a> <a
					href="<%=basePath %>logout" >注销</a>
				</span> <span id="bgclock" style="font-size: 15px;font-family: '微软雅黑';"></span>
			</div>
		</div>
		<div data-options="region:'south'"
			style="height: 1px;background-color: #666666;">
			<div style="text-align: right;margin-top: 3px;"></div>
		</div>

		<div data-options="region:'west',split:true" title="菜单"
			style="width: 180px;">
			<div class="easyui-panel" id="menu_panel" data-options="fit:true">
					<ul id="mytree" class="easyui-tree" data-options="animate:true,lines:true"></ul>
			</div>
		</div>

		<div data-options="region:'center'" data-options="fit:true">
			<div id="mainTabs" class="easyui-tabs" data-options="fit:true">			
			</div>				
		</div>
	</div>
</body>
</html>