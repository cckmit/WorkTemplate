//绑定点击事件
function bindClick(){
	//移到右边
	$('#add').click(function(){
		//获取选中的选项，删除并追加给对方
		$('#select1 option:selected').appendTo('#select2');	
	});
	
	//移到左边
	$('#remove').click(function(){
		$('#select2 option:selected').appendTo('#select1');
	});
	
	//全部移到右边
	$('#add_all').click(function(){
		//获取全部的选项,删除并追加给对方
		$('#select1 option').appendTo('#select2');
	});
	
	//全部移到左边
	$('#remove_all').click(function(){
		$('#select2 option').appendTo('#select1');
	});
	
	//双击选项
	$('#select1').dblclick(function(){ //绑定双击事件
		//获取全部的选项,删除并追加给对方
		$("option:selected",this).appendTo('#select2'); //追加给对方
	});
	
	//双击选项
	$('#select2').dblclick(function(){
		$("option:selected",this).appendTo('#select1');
	});
}

		//上移
function up(id){
	var selected = $("#"+id+" option:selected");
	var v = selected[0].value;
	var select2 = document.getElementById(id);
	var ops = select2.options;
	$(ops).each(function(index,option){
		var value = option.value;
		if(value == v){
			if(index-1 >= 0){
				//var temp = ops[index-1];
				var op1=new Option();
				var op2=new Option();
				op1.value = ops[index-1].value;
				op1.text = ops[index-1].text;
				op2.value = ops[index].value;
				op2.text = ops[index].text;
				select2.options[index] = op1;
				select2.options[index-1] = op2;
				select2.options[index-1].selected = true;
			}
		}
	});
}
	
//下移
function down(id){
	var selected = $("#"+id+" option:selected");
	var v = selected[0].value;
	var select2 = document.getElementById(id);
	var ops = select2.options;
	$(ops).each(function(index,option){
		var value = option.value;
		if(value == v){
			if(index+1 <= ops.length){
				//var temp = ops[index-1];
				var op1=new Option();
				var op2=new Option();
				op1.value = ops[index+1].value;
				op1.text = ops[index+1].text;
				op2.value = ops[index].value;
				op2.text = ops[index].text;
				select2.options[index] = op1;
				select2.options[index+1] = op2;
				select2.options[index+1].selected = true;
			}
		}
	});
}



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
			nodes.push({
				id : row.id,
				text : row.name,
				memo : row.memo
			});
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

//树形菜单权限编辑
function inittree(id){
	$.getJSON('tree_data.json', {}, function(val) {
		$('#'+id).combotree({
			data : val,
			loadFilter : function(rows) {
				return convert(rows);
			}
		});
	});
}
