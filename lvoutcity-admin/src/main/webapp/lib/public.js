/**
 * 封装DataTable
 * @param json
 */
function LvoutDataTable(json){
	if(!json){
		json = {};
	}
	if(json.id){
		console.log('datatable id is not null');
		return;
	}
	if(json.url){
		console.log('datatable url is not null');
		return;
	}
	$("#"+json.id).DataTable({
		"processing" : isNull(json.processing) ? true : json.processing,	//开关，以指定当正在处理惩罚数据的时辰，是否显示“正在处理惩罚”这个提示信息
		"bProcessing": isNull(json.bProcessing) ? true : json.bProcessing,
		"bFilter": isNull(json.bFilter) ? false:jons.bFilter, //过滤功能
		"serverSide" : true,
		"bAutoWidth": isNull(json.bAutoWidth) ? false:jons.bAutoWidth,//自动宽度
		"sLoadingRecords": "正在加载数据...",
		//"order" : [ [ 1, "asc" ] ],
		"bDeferRender":isNull(json.bDeferRender) ? false:jons.bDeferRender,
		"bSort": isNull(json.sort) ? false : json.sort,
		"orderMulti":isNull(json.orderMulti) ? false:jons.orderMulti,
//		"aoColumns": [{ "sName": "platform" }],
		//'iDisplayLength':13, //每页显示10条记录
		"ajax" : {
			"url" : json.url,
		    "type": "POST",
		    "data":function(data){
		    	if(isNotNull(json.method)){
		    		invokeFunction(json.method,[data])
		    	}
		    }
		},
		"columnDefs" :json.column,
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "显示 _MENU_ 项结果",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
			"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
		},
		"sZeroRecords": "没有检索到数据",
		/* "aoColumnDefs": [
		           	  //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
		           	  {"orderable":false,"aTargets":[0,8]}// 制定列不参与排序
		           	], */
	});
}


/**
 * 调用指定方法名的方法
 * @param {Object} 
 * @param {Object} args
 * @memberOf {TypeName} 
 */
function invokeFunction(functionName,arguments){
	var func = functionName;
	if(typeof(functionName) == 'string'){
		try{
			func = eval(functionName);
			if(arguments){
				return func.apply(this,arguments)
			}else{
				return func.apply(this,[])
			}
		}catch(e){
			return false;
		}
	}
	else if(typeof(functionName) == 'function'){
		try{
			if(arguments){
				return func.apply(this,arguments)
			}else{
				return func.apply(this,[])
			}
		}catch(e){
			return false;
		}
	}
}

/**
 * 判断非空
 * @param obj
 * @returns {Boolean}
 */
function isNull(obj){
	return obj == undefined || obj == null || $.trim(obj.toString()) == '';
}

/**
 * 判断非空
 * @param obj
 * @returns {Boolean}
 */
function isNotNull(obj){
	return !isNull(obj);
}


/**
 * 对象转换成json对象
 * @param {Object} obj
 * @return {TypeName} 
 */
function objToJson(obj){
	try{
		return "{'data':" + JSON.stringify(obj) + "}";
	}catch(e){
		
	}
}

/**
 * 对象转换JSON对象
 * @param obj
 * @returns
 */
function objToJsonOb(obj){
	try{
		return JSON.stringify(obj);
	}catch(e){
		
	}
}

/**
 * 转译 
 * @param obj
 * @returns
 */
function objToJsonND(obj){
	try{
		return JSON.stringify(obj);
	}catch(e){
		
	}
}


/**
 * 字符串转换成json对象
 * @param {Object} str
 * @return {TypeName} 
 */
function strToJson(str){
	try{
		return JSON.parse(str);
	}catch(e){
		return null;
	}
}

/**
 * select init 
 * @param json
 */
function selectDictionary(json){
	if(isNull(json.selectId)){
		return;
	}
	$.ajax({
		cache: true,
        type: "POST",
        url:isNull(json.url) ? "common/selectClublist" : json.url,
        async: false,
        success: function(data) {
        	successValue(data,json);
        }
	}); 
}

/**
 * 
 * @param data
 * @param json
 */
function successValue(data,json){
	if(isNull(data)){
		return;
	}
	var cl_id = json.dataVal;
	
	if(json.selectInit){
		$('#'+json.selectId).append("<option value='-1' selected='selected' >请选择</option>");
	}
	var dd = strToJson(data);
	$.each(data,function(i,obj){
		
		if(cl_id == obj.id){
			$('#'+json.selectId).append("<option value='"+obj.id+"' selected='selected' >"+obj.name+"</option>");
		}else{
			$("#"+json.selectId).append("<option value='"+obj.id+"'>"+obj.name+"</option>");
		}
	});
	if(json.changeBool){
		$('#'+json.selectId).on('change',function(){
			invokeFunction(json.method,$(this).val());
		});
	}
}

/**
 * @author Grahor
 * 自定义js数组
 */
function JSList(obj){
	this.eles = new Array();
	if(obj != null && obj != ''){
		this.add(obj);
	}
	return this;
}
/**
 * 添加元素
 * @param obj
 * @return
 */
JSList.prototype.add = function(obj){
	if(obj instanceof Array){
		this.eles = this.eles.concat(obj);
	}
	else if(typeof(obj) == 'string'){
		this.eles.push($.trim(obj));
	}
	else if(typeof(obj) == 'object'){
		this.eles.push(obj);
	}
	else{
		this.eles.push(obj);
	}
}

/**
 * 获取index对应的数据
 * @param {Object} index
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
JSList.prototype.get = function(index){
	return this.eles[index];
}

/**
 * 根据obj
 * @param {Object} obj
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
JSList.prototype.getIndex = function(obj){
	var index = -1;
	for(var i = 0; i < this.eles.length; i++){
		if($.equals(this.eles[i],obj)){
			index = i;
			break;
		}
	}
	return index;
}

/**
 * list的大小
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
JSList.prototype.size = function(){
	return this.eles.length;
}

/**
 * is empty
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
JSList.prototype.isEmpty = function(){
	return this.eles.length == 0;
}

/**
 * 替换指定位置的对象
 * @param {Object} index
 * @param {Object} newObj
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
JSList.prototype.replace = function(index,newObj){
	var oldObj = this.eles[index];
	this.eles[index] = newObj;
	return oldObj;
}

/**
 * 判断list中是否含有指定对象 返回第一个符合对象的位置
 * @param {Object} obj
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
JSList.prototype.contains = function(obj){
	for(var i = 0; i < this.eles.length; i++){
		if($.equals(this.eles[i],obj)){
			return true;
		}
	}
	return false;
}

JSList.prototype.getSubList = function(start,end){
	if(start < 0 || start > this.size() || end < 0 || end > this.size() || start > end){
		return null;
	}
	return new JSList(this.eles.slice(start,end));
}

/**
 * 清空所有
 * @return
 */
JSList.prototype.clear = function(){
	this.eles = new Array();
}

/**
 * 删除某个位置或者某个对象
 * @param obj
 * @return
 */
JSList.prototype.remove = function(obj){
	if(isNaN(obj)){
		var i;
		for(i = 0; i < this.eles.length; i++){
			if($.equals(this.eles[i],obj)){
				break;
			}
		}
		if(i != this.eles.length){
			this.eles.splice(i, 1);
		}
	}
	else{
		if(parseInt(obj) >= 0 && parseInt(obj) < this.eles.length){
			this.eles.splice(obj, 1);
		}
			
	}
}

/**
 * 删除
 * @param {Object} start
 * @param {Object} end
 */
JSList.prototype.removeSub = function(start,end){
	this.eles.splice(start,end-start);
}

/**
 * 转换成数组
 * @return
 */
JSList.prototype.values = function(){
	return this.eles;
}

/**
 * 遍历list
 * @param func
 * @return
 */
JSList.prototype.each = function(func){
	for(var i = 0; i < this.eles.length; i++){
		func.apply(this, [this.eles[i],i]);
	}
}

/**
 * 获取数组字符串
 * @return
 */
JSList.prototype.toString = function(split){
	if(isNull(split)){
		return this.eles.toString();
	}
	else{
		var str = '';
		for(var i = 0; i < this.eles.length; i++){
			str += this.eles[i].toStr() + split;
		}
		return str.substring(0, str.length - 1);
	}
}

/**
 * 转换成json对象
 * @return
 */
JSList.prototype.toJson = function(){
	return objToJson(this.eles);
}