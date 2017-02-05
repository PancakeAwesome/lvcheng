$(function(){
	//获取俱乐部下拉框
	$.ajax({
		cache: true,
        type: "POST",
        url:"common/selectClublistByUser",
        async: false,
        success: function(data) {
        	if(data.code=='0'){
        		// 旅城账号
        		$("#clubId").html("");
            	$("#clubId").append("<option value=''>--请选择--</option>");
    			$.each(data.data,function(i,obj){
    				$("#clubId").append("<option value='"+obj.id+"'>"+obj.clubShortName+"</option>");
    			});
        	}else if(data.code=='1'){
        		// 俱乐部权限
        		$("#clubId").html("");
    			$.each(data.data,function(i,obj){
    				$("#clubId").append("<option value='"+obj.id+"'selected  >"+obj.clubShortName+"</option>");
    			});
    			$("#is_club_hide").css("display","none");
        	}
        	
                
        }
	});
	
})
