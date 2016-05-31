$(document).ready(function(){
	
	var userName="toby.yao";
	//$.getJSON(url,function(data){ });
	$("#remember").click( function () {
		var isChecked = $(this).get(0).checked;
		if(isChecked){
			//no need to set password into cookie
			cookieUtil.setCookie(userName, "");
		}else{
			cookieUtil.delCookie(userName);
		}
	});
	var pwd = cookieUtil.getCookie(userName);
	if(pwd !=null){
		$("#userName").val(userName);
		//$("#pwd").val(pwd);
		$("#remember").get(0).checked=true;
	}
	
	
});