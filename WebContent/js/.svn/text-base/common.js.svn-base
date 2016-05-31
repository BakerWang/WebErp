var cookieUtil={
		cartListName: "cartList",
		setCookie:function(name,value){
			var Days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days*24*60*60*1000);
			document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
		},
		getCookie:function(name){
			var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
			if(arr=document.cookie.match(reg))
				return unescape(arr[2]);
			else
				return null;
		},
		delCookie:function(name)
		{
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval=this.getCookie(name);
			if(cval!=null)
				document.cookie= name + "="+cval+";expires="+exp.toGMTString();
		},
		clear:function(){
			var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
			if (keys) {
				for (var i =  keys.length; i--;)
					document.cookie=keys[i]+'=0;expires=' + new Date( 0).toUTCString()
			}    
		},
		clearCart:function(){
			this.delCookie(this.cartListName);
		},
		addCart:function(strJSON){
			var existedItem = this.getCookie(this.cartListName);
			if(existedItem ==null){
				this.setCookie(this.cartListName, strJSON)
			}else{
				var setItem = existedItem + "--" + strJSON;
				this.setCookie(this.cartListName, setItem)
			}
		},
		getCartList:function(){
			//need to parse return array to json object
			var cartList = this.getCookie(this.cartListName);
			if(cartList == null){
				return null;
			}
			return  cartList.split("--");
		},
		checkCartList:function(newJson){
			//need to parse return array to json object
			var cartList = this.getCookie(this.cartListName);
			if(cartList == null){
				return false;
			}
			return  cartList.indexOf(newJson)>=0?true:false;
		}
		
};
var messageUtil = {
	 clearMsg:function(){
		$("#messageBox ul").empty();
	 },
	 alertMsg:function(msgContent){
		this.clearMsg();
		$("#messageBox").css("bottom","0px");
		if($("#messageBox ul li").size() == 0){
				$("#messageBox ul").append("<li>"+msgContent+"</li>");
		}
		var isExist = false;
		//check if exist already
		$.each($("#messageBox ul li"),function(k,li){
			var msg = $(li).text();
			if(msg.indexOf( msgContent ) != -1){	
				isExist = true;
			}
		});
		if(!isExist){
			$("#messageBox ul").append("<li>"+msgContent+"</li>");
		}
		setTimeout("$('#messageBox').css('bottom','-200px');",3000);
	}
};
var commonUtil = {
		convertValue:function(textVal){
			if(textVal==0 || textVal==null || textVal==undefined){
				textVal="";
			}
			return textVal;
		}
	};


var activeTitle = function(){
	var title = $("html title").text();
	switch(title)
	{
	case "报价单页面":
		$("#navicon ul li").eq(0).css("background-color","#3BA5A5");
	  break;
	case "报价查询":
		$("#navicon ul li").eq(1).css("background-color","#3BA5A5");
	  break;
	}
};