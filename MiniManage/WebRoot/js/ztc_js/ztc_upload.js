 //拼接html
   function html(id,title)
   {
   	var browseid = id + "browse";
   	var uploadid = id + "upload";
   	var d = $("#"+browseid+"-file-list").parent();
   	if(d.parent().length)
   	{
   		d.dialog('close');
   		d.parent().remove();
   		
   	}
   	var html = '\t<div style="margin:20px 0;"></div>\n';
   	html += '\t<div id="upload_dlg" class="easyui-dialog" title="'+title+'" style="width:25%;max-width:100%;min-height:300px;max-height:800px;height: auto;padding:10% 20%;text-align: center;">\n';
   	html += '\t\t<div style="margin-bottom: 20px; text-align: center;"><p>'+title+'</p></div>\n';
   	html += '\t\t<div style="margin-bottom:20px;text-align:center;position: relative;">\n';
   	html += '\t\t\t<a href="javascript:void(0)" class="easyui-linkbutton" id="'+browseid+'">选择文件</a>&nbsp;&nbsp\n';
   	html += '\t\t\t<a href="javascript:void(0)" class="easyui-linkbutton" id="'+uploadid+'">开始上传</a>\n';
   	html += '\t\t</div>\n';
   	html += '\t\t<div id="'+browseid+'-file-list" style="margin-bottom:20px;text-align: center;position: relative;"></div>\n';
   	html += '\t</div>\n';
   	$(html).appendTo('#'+id);
   	$.parser.parse('#'+id);
   	
   }
 
 //上传文件
 (function($){
	var id;
	$.fn.uploadfile=function(callback){
		this.each(function(){
			id = this.id;
		});
 	var url = '';
 	var multi_selection = 'true';
 	var title = 'upload file';
 	var maxfilesize = '500M';
 	var filetype = "jpg,png,gif,zip,apk";
 	var maxfile = $("#"+id).attr('maxfileSize');
 	var dataurl = $("#"+id).attr('data-url');
 	var multi = $("#"+id).attr('data-multiSelect');
 	var datatitle = $("#"+id).attr('data-title');
 	var type = $("#"+id).attr('filetype');
 	var call = $("#"+id).attr('callbackurl');
 	var tagid = $("#"+id).attr('tag_id');
 	var cdn = $("#"+id).attr('cdn');
 	if(!tagid)
 	{
 		tagid = "3";
 	}
 	if(!cdn)
 	{
 		cdn = "true";
 	}
 	if(dataurl)
 	{
 		url = dataurl;
 	}
 	if(multi)
 	{
 		if(multi == "true")
 		{
 			multi_selection = true;
 		}else{
 			multi_selection = false;
 		}
 	}
 	if(datatitle){
 		title = datatitle;
 	}
 	if(maxfile)
 	{
 		maxfilesize = maxfile;
 	}
 	if(type)
 	{
 		filetype = type;
 	}
 	//拼接html
   	html(id,title);
   	var browseid = id + "browse";
   	var uploadid = id + "upload";
    var uploader = new plupload.Uploader({
        browse_button : browseid, //触发文件选择对话框的按钮，为那个元素id
        url : url, //服务器端的上传页面地址
        flash_swf_url : '../../js/Moxie.swf', //swf文件，当需要使用swf方式进行上传时需要配置该参数
        silverlight_xap_url : '../../js/Moxie.xap', //silverlight文件，当需要使用silverlight方式进行上传时需要配置该参数
   		filters: {
 					mime_types : [ //只允许上传图片和zip文件
    								{ title : "Image files", extensions : filetype } 
//  								{ title : "Zip files", extensions : "zip" },
//  								{ title : "apk", extensions : "apk" }
  								],
  					max_file_size : maxfilesize, //设置最大上传文件大小
  					prevent_duplicates : true //不允许选取重复文件
				},
   		resize: {
 					width: 50,
  					height: 50,
  					crop: true,
  					quality: 60,
  					preserve_headers: true
				},
		chunk_size:"50M",
		multipart:true,
		multipart_params:{callbackurl:call,tag_id:tagid,cdn:cdn},
   		multi_selection:true,
   		unique_names:false,
   		runtimes:"html5,flash,silverlight,html4",
   		file_data_name:"file",
    });
    //在实例对象上调用init()方法进行初始化
    uploader.init();
    //绑定各种事件，并在事件监听函数中做你想做的事
    uploader.bind('FilesAdded',function(uploader,files){
        //每个事件监听函数都会传入一些很有用的参数，
        //我们可以利用这些参数提供的信息来做比如更新UI，提示上传进度等操作
        var rule = /.*[\u4e00-\u9fa5]+.*$/;
        for(var i = 0, len = files.length; i<len; i++){
			var file_name = files[i].name; //文件名
			//限制中文名字
			if(rule.test(file_name))
			{
				$.messager.alert('警告','<div style="text-align:center;margin-top:15px">上传文件名不能包含中文名！</div>');
				deletequeue(files[i],files[i].id);
				return false;
			}
			if(!multi_selection)
        	{
        		if(uploader.files.length > 1)
        		{
        			$.messager.alert('警告','<div style="text-align:center;margin-top:15px">上传文件数量超过限制!</div>');
        			deletequeue(files[i],files[i].id);
					return false;
        		}
        	}
			//构造html来更新UI
			var imgsrc = "../icons/uploadify-cancel.png";
			var html = '<div id="file-' + files[i].id +'" style="margin-bottom:20px;">'
			if((files[i].size/1024/1024).toFixed(2) < 1 )
			{
				html += '<span class="file-name">' + file_name + '('+(files[i].size/1024).toFixed(2)+'Kb)</span>'
			}else{
				html += '<span class="file-name">' + file_name + '('+(files[i].size/1024/1024).toFixed(2)+'M)</span>'
			}
			html += '<img src="'+imgsrc+'" class="delete" data-val="'+files[i].id+'"/>'
			html += '</div>';
			$(html).appendTo('#'+browseid+'-file-list');
			$.parser.parse('#'+browseid+'-file-list');
			if(!files[i] || !/image\//.test(files[i].type)) return;
			!function(i){
				previewImage(files[i],function(imgsrc){
					$.messager.alert({
						title:"图片预览",
						msg:'<div style="text-align:center;"><img src="'+ imgsrc +'" style="margin-top:10px;width:100px;height:100px;text-align:center;"/></div>'
					});
				})
		    }(i);
		}
    });
    //上传完成后删除队列
    uploader.bind('FileUploaded',function(uploader,file,responseObject){
    	var response = responseObject.response;
//    	var datas = response.split("#");
//    	var str = strdecode(datas[1]);
//    	$.messager.alert('提示',str);
    	var obj = JSON.parse(response);
//  	var status = obj.status;
//  	$(".messager-body").window('close');
		$.messager.progress('close');
    	var id = "file-"+file.id;
    	$('#'+id).remove();
    	deletequeue(file,id);
    	var filesize;
    	if((file.size/1024/1024).toFixed(2) < 1 )
		{
			filesize = (file.size/1024).toFixed(2);
		}else{
			filesize = (file.size/1024/1024).toFixed(2);
		}
    	if(obj.apk)
    	{
			var apk = obj.apk;
			$.messager.alert({
				title:"上传完成",
				msg:'文件名称：<br>'+obj.file_name+'<br>应用名称：<br>'+apk.name+'<br>版本号：<br>'+apk.apk_version+'<br>文件大小：<br>'+filesize+'<br>MD5:<br>'+apk.apk_md5+'<br>包名：<br>'+apk.package_name+'<br>CDN地址：<br>'+apk.apk_url
			});
    	}else{
			$.messager.alert({
				title:"上传完成",
				msg:'文件名称：<br>'+obj.file_name+'<br>文件大小：<br>'+filesize+'<br>MD5:<br>'+obj.md5+'<br>CDN地址：<br>'+obj.url
			});
    	}
    	callback(obj);
    	var p = $("#"+browseid+"p").parent();
    	var pp = p.parent();
    	pp.dialog('close');
    	if(uploader.files.length > 0)
    	{
			$.messager.alert({
				title:'上传进度',
				msg:'<div id="'+browseid+'p" class="easyui-progressbar" data-options="value:0" style="width:200px;margin:51px auto";></div>'
			});
    	}else{
    		$("#"+browseid+"-file-list").parent().dialog('close');
    		$("#"+browseid+"-file-list").parent().parent().remove();
    	}
    });
    uploader.bind('UploadProgress',function(uploader,file){
        //每个事件监听函数都会传入一些很有用的参数，
        //我们可以利用这些参数提供的信息来做比如更新UI，提示上传进度等操作
//      var progress = $.messager.progress('bar');
//      progress.progressbar('setValue',file.percent);
		$("#"+browseid+"p").progressbar('setValue',file.percent);
        if(file.percent == 100)
        {
    		$.messager.progress({
    			title:'上传CDN',
    			msg:'上传中...',
    			text:''
    		});
        	return false;
        }
    });

    //最后给"开始上传"按钮注册事件
    document.getElementById(uploadid).onclick = function(){
    	if(uploader.files.length == 0)
    	{
    		$.messager.alert('警告','<div style="margin-top:15px;text-align:center;">请选择文件！</div>')
    		return false;
    	}
//  	$.messager.progress({
//      	title: '上传文件',
//			msg: '上传中...'
//      });
		$.messager.alert({
			title:'上传进度',
			msg:'<div id="'+browseid+'p" class="easyui-progressbar" data-options="value:0" style="width:200px;margin:51px auto";></div>'
		});
        uploader.start(); //调用实例对象的start()方法开始上传文件，当然你也可以在其他地方调用该方法
    }
    
    $(document).on('click', '.delete', function () {
                $(this).parent().remove();
                var toremove = '';
                var id = $(this).attr("data-val");
                for (var i in uploader.files) {
                    if (uploader.files[i].id === id) {
                        toremove = i;
                    }
                }
                uploader.files.splice(toremove, 1);
            });
            
    //将文件删除队列
    function deletequeue(file,id)
	{
		var toremove;
		for (var i in uploader.files) {
            if (uploader.files[i].id === id) {
                toremove = i;
            }
        }
        uploader.files.splice(toremove, 1);
	}

	}
 })(jQuery); 
function previewImage(file,callback){//file为plupload事件监听函数参数中的file对象,callback为预览图片准备完成的回调函数
	if(!file || !/image\//.test(file.type)) return; //确保文件是图片
	if(file.type=='image/gif'){//gif使用FileReader进行预览,因为mOxie.Image只支持jpg和png
		var fr = new mOxie.FileReader();
		fr.onload = function(){
			callback(fr.result);
			fr.destroy();
			fr = null;
		}
		fr.readAsDataURL(file.getSource());
	}else
	{
		var preloader = new mOxie.Image();
		preloader.onload = function() {
			preloader.downsize( 100, 100 );//先压缩一下要预览的图片,宽300，高300
			var imgsrc = preloader.type=='image/jpeg' ? preloader.getAsDataURL('image/jpeg',80) : preloader.getAsDataURL(); //得到图片src,实质为一个base64编码的数据
			callback && callback(imgsrc); //callback传入的参数为预览图片的url
			preloader.destroy();
			preloader = null;
		};
		preloader.load( file.getSource() );
	}	
}

function clearFileBox(fileImport){
	$('#'+fileImport).filebox('reset');
	$('#'+fileImport).filebox('setValue','');
}
