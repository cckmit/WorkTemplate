//显示饼状图的方法
/**
 * obj 属性:
 * searchData 搜索数据，用于ajax传参
 * text 饼状图标题
 * url 获取饼状图json数据的url
 * 		json格式：
 *  	[
	      {name: 'Firefox',y: 45.0}, 
	      {name: 'IE',y: 26.8}, 
	      {name: 'Safari',y: 8.5}, 
	      {name: 'Opera',y: 6.2}, 
	      {name: 'Others',y: 0.7} 
	    ]
 */
(function($){
	$.fn.pieChart = function(obj){
		var search = obj.searchData ? obj.searchData : {};
		var text = obj.text ? obj.text : "饼状图";
		var url = obj.url ? obj.url : "";
		   var chart = {
		       plotBackgroundColor: null,
		       plotBorderWidth: null,
		       plotShadow: false
		   };
		   var title = {
		      text: obj.text  
		   };      
		   var tooltip = {
		      pointFormat: '值: <b>{point.y:f}</b>'
		   };
		   var plotOptions = {
		      pie: {
		         allowPointSelect: true,
		         cursor: 'pointer',
		         dataLabels: {
		            enabled: true,
		            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		            style: {
		               color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		            }
		         }
		      }
		   };
		   
		   //数据来源
			var result = {};
			$.ajax({
				url:url,
				async:false,
				data:search,
				success:function(data){
					result = data;
				}
			});
			console.log(result);
		   var series= [{
		      type: 'pie',
		      data: JSON.parse(result)
		   }];     
		      
		   var json = {};   
		   json.chart = chart; 
		   json.title = title;     
		   json.tooltip = tooltip;  
		   json.series = series;
		   json.plotOptions = plotOptions;
		   $(this).highcharts(json); 
	};
})(jQuery);

/*
 * 曲线走势图
 */
(function($){
	$.fn.lineChart = function(obj){
		var container = "";
		//标题
		var titlename = (obj.title ? obj.title : "Chart");
		//副标题
		var subtitlename = (obj.subtitle ? obj.subtitle : "");
		//y轴的间隔
		var interval = (obj.interval ? obj.interval : 5);
		//y轴的名称
		var ytitle = (obj.ytitle ? obj.ytitle : "");
		//x轴属性设置
		var xlables = (obj.xlabels ? obj.xlabels : {});
		//y轴属性设置
		var ylables = (obj.ylabels ? obj.ylabels : {});
		var url = (obj.url ? obj.url : "");
		//数据来源
		var result = {};
		$.ajax({
			url:url,
			async:false,
			data:{},
			success:function(data){
				result = data;
			}
		});
		var series = new Array();
		//X轴
		var xArray = new Array();
		var data = JSON.parse(result);
		for(var i in data){
			var obj = {};
			obj["name"] = i;
			obj.data = new Array();
			for(var j in data[i]){
				xArray.push(j);
				obj.data.push(data[i][j]);
			}
			series.push(obj);
		}
		this.each(function(){
			container = this.id;
		});
   		var chart = {
   			renderTo: container,
			defaultSeriesType: 'spline',
   		};
        var title = {
            text: titlename,
        };
        var subtitle = {
            text: subtitlename,
        };
   		var xAxis = {  
      		categories: xArray,
      		labels:xlables,
   		};
   		var yAxis = {
		    title: {
		        text: ytitle,
		    },
		    labels:ylables,
	  		tickInterval:interval,
      		plotLines: [{
		     	value: 0,
		     	width: 1,
		     	color: '#808080',
      		}],
   		};   

   		var tooltip = {
      		valueSuffix: '\xB0C',
   		};

  		var legend = {
      		layout: 'vertical',
      		align: 'right',
      		verticalAlign: 'middle',
      		borderWidth: 0,
   		};
   		var json = {};
   		json.chart = chart;
   		json.title = title;
   		json.subtitle = subtitle;
   		json.xAxis = xAxis;
   		json.yAxis = yAxis;
   		json.tooltip = tooltip;
   		json.legend = legend;
   		json.series = series;
   		new Highcharts.Chart(json);
   	};
})(jQuery);

function clockon(bgclock) {
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth();
	var date = now.getDate();
	var day = now.getDay();
	var hour = now.getHours();
	var minu = now.getMinutes();
	var sec = now.getSeconds();
	var week;
	month = month + 1;
	if (month < 10)
		month = "0" + month;
	if (date < 10)
		date = "0" + date;
	if (hour < 10)
		hour = "0" + hour;
	if (minu < 10)
		minu = "0" + minu;
	if (sec < 10)
		sec = "0" + sec;
	var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	week = arr_week[day];
	var time = "";
	time = year + "年" + month + "月" + date + "日" + week + " " + hour + ":"
			+ minu + ":" + sec;

	bgclock.innerHTML = "[" + time + "]";

	var timer = setTimeout("clockon(bgclock)", 200);
}

$.extend($.fn.tabs.methods, {
    /**
     * 绑定双击事件
     * @param {Object} jq
     * @param {Object} caller 绑定的事件处理程序
     */
    bindDblclick: function(jq, caller){
        return jq.each(function(){
            var that = this;
            $(this).children("div.tabs-header").find("ul.tabs").undelegate('li', 'dblclick.tabs').delegate('li', 'dblclick.tabs', function(e){
                if (caller && typeof(caller) == 'function') {
                    var title = $(this).text();
                    var index = $(that).tabs('getTabIndex', $(that).tabs('getTab', title));
                    caller(index, title);
                }
            });
        });
    },
    /**
     * 解除绑定双击事件
     * @param {Object} jq
     */
    unbindDblclick: function(jq){
        return jq.each(function(){
            $(this).children("div.tabs-header").find("ul.tabs").undelegate('li', 'dblclick.tabs');
        });
    }
});