$(document).ready(function(){
	/*
	 * init method
	 * if have added cart before,then show the list info directly on the page
	 */
	 var init =function(){
		 var displayBody = $("#cartListDiv table tbody");
		 //first clear table
		 displayBody.empty();
		 var cartList = cookieUtil.getCartList();
		 if(cartList != null){
			 for ( var listIndex in cartList) {
				 var sampleInfo = eval( "(" + cartList[listIndex] + ")" );
				 var tr = "<tr><td>"+ sampleInfo.client +"</td><td>"+ sampleInfo.styleNo +"</td><td>"+ sampleInfo.sampleOrderNo +"</td><td>"+ sampleInfo.sampleClass +"</td><td>"+ sampleInfo.version +"</td><td>"+ sampleInfo.quoteDate +"</td><td>"+ sampleInfo.status +"</td></tr>";
				 displayBody.append(tr);
			}
		 }
	 };
	 $("#selectAll").bind("click", function(){
		 var isChecked = $(this).get(0).checked;
		 var selectedList = $("#detailDiv table tbody tr input");
			 $.each(selectedList, function(i,checkbox){
				 if(isChecked){
					 //select all
					 checkbox.checked = true;
				 }else{
					 checkbox.checked = false;
				 }
			 });
	 });
	 $("#addCart").bind("click", function(){
		 //get all selected items
		    var selectedList = $("#detailDiv table tbody input:checked");
		    $.each(selectedList, function(i,checkbox){
		    	//get sampleInfo,then format this to be json string
		    	var sampleInfo = $(checkbox).parent().prevAll();
		    	var client = sampleInfo.eq(6).text();
		    	var styleNo = sampleInfo.eq(5).text();
		    	var sampleOrderNo = sampleInfo.eq(4).text();
		    	var sampleClass = sampleInfo.eq(3).text();
		    	var version = sampleInfo.eq(2).text();
		    	var quoteDate = sampleInfo.eq(1).text();
		    	var status = sampleInfo.eq(0).text();
		    	
		    	var strJSON = "{client:'"+ client + "',styleNo:'" + styleNo +  "',sampleOrderNo:'" + sampleOrderNo +  "',sampleClass:'" + sampleClass +  "',version:'" + version+  "',quoteDate:'" + quoteDate +  "',status:'" + status +"'}" ;
		    	var existedFlg = cookieUtil.checkCartList(strJSON);
		    	if(!existedFlg){
		    		cookieUtil.addCart(strJSON);
		    	}
		    	//var obj = eval( "(" + strJSON + ")" );
		    	
			 });
		    //reload
		    init();
	 });
	 $("#clear").bind("click", function(){
		 cookieUtil.clearCart();
		 //reload
		 init();
	 });
	 $("#view").bind("click", function(){
		 var viewUrl="http://erp.tx.internal:8888/WebErp/techquote/quote?";
		 var tempUrl="";
		 var cardList = $("#cartListDiv table tbody tr");
		  $.each(cardList, function(i,sampleInfo){
			 var sampleOrderNo = $(sampleInfo).children().eq(2).text();
			 var version = $(sampleInfo).children().eq(4).text();
			 tempUrl= tempUrl + "quoteIdList["+ i+"].sampleOrderNo="+sampleOrderNo+"&quoteIdList["+ i+"].version="+version+"&";
		  });
		  viewUrl = viewUrl+tempUrl.substring(0, tempUrl.length-1);
		  window.open(viewUrl)
	 });
	 activeTitle();
	 init();
	 
});