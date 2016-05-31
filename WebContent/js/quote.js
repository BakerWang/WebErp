var quoteMap={
	put:function(key,value){
		$("body") .data(key, value);
	},
	get:function(key){
		return $("body").data(key);
	},
};
//auto  generate the version number,save key-value format datas on body element temporarily
var versionUtil={
		generate:function(sampleOrderNo){
			var versions = quoteMap.get(sampleOrderNo);
			if(versions.length==0){
				versions=[1];
				quoteMap.put(sampleOrderNo,versions);
				return 1;
			}
			for (var j = 0; j < versions.length-2; j++) {
				if(Math.abs( versions[j]-versions[j+1]) != 1){
					return versions[j] + 1;
				}
			}
			var generatedVerNo = versions[versions.length -1] +1;
			versions.push(generatedVerNo);
			quoteMap.put(sampleOrderNo,versions);
			return generatedVerNo;
		}
	};		
var quoteUtil={
		//titles:["样板单号","版本","本廠款号","客户","跟单员","客款号","款式","毛料成份","针型","码数","織片克重","毛衣克重","电脑摇片工时","套口工时","織下欄附件","手钩","手撞","繡花","印花","車布","布包/手工鈕(每粒)","特殊洗水","後加處理","哈梭","工费","辅料","状态","最新报价时间"],
		itemSize:28,
		query: function(currentCell,isVersion,isReloadVersion){
			var version ="";
			//current input is barcode 
			var barcode =  $(currentCell).val();
			var currentIndex = this.getCurrentColIndex(currentCell);
			var table = $("#mainDiv table tr");
			var url= "techquote/quoteJson?quoteIdList[0].sampleOrderBarcode="+ barcode;
			if(isVersion){
				//to get barcode info which located above version column
				//this is sampleOrderNo now
				var sampleOrderNo =table.eq(0).children().eq(currentIndex).children("input").val();
				version = $(currentCell).val();
				var hiddenInput = $(currentCell).parent().find("input,input:hidden");
				if(version == "new"){
					//need make version number auto increment
					var allocateVerNo = versionUtil.generate(sampleOrderNo);
					version=allocateVerNo;
					hiddenInput.val(version);
					//hiddenInput.get(0).value=version;
					//clear
					quoteUtil.emptyInput(currentCell);
					return;
				}
				hiddenInput.val(version);
				url= "techquote/quoteJson?quoteIdList[0].sampleOrderNo="+ sampleOrderNo+"&quoteIdList[0].version="+version;
			}else{
				//means that scan sample order no
				if(barcode ==""){
					this.deleteCol(currentCell);
					return;
				}
			}
			//only one record
			 $.getJSON(url,function(data){
				 if(data.quoteList.length > 0){
					 var quote = data.quoteList[0];
					 var  sampleOrderNo = quote.quoteInfo.sampleOrderNo;
					 var sampleInfoInput =table.eq(0).children().eq(currentIndex).children("input");
					 sampleInfoInput.val(sampleOrderNo);
					 sampleInfoInput.attr("name","quoteList[xx].quoteInfo.sampleOrderNo");
						 //version
				    	 //used for display
						 var versions =quote.quoteInfo.versionList;
						 //hacked by toby  2016-1-20
						 var cacheVersions =quoteMap.get(sampleOrderNo);
						 if(cacheVersions ==undefined){
							 quoteMap.put(sampleOrderNo,versions);
						 }
						
						 //add DOM element for version option button 
						 var verHtml="<select class='versionSelect' onchange='quoteUtil.query(this,true,false)'>";
						 if(versions.length==0){
							 //默认新增
							 version=versionUtil.generate(sampleOrderNo);
						 }
						 
						 for ( var verIndex in versions) {
							 verHtml =verHtml + "<option value='"+versions[verIndex] +"'>"+ versions[verIndex] +"</option>";
						 }
						 //directy to add version number,no need to select version
						 //end close
						 verHtml =verHtml +"<option value='new'>新增</option></select><input type='hidden' name='quoteList[xx].quoteInfo.version' value='"+version +"'>";
						 if(isReloadVersion){
							 //first need to empty in case have added already  
							 table.eq(1).children().eq(currentIndex).empty();
							 table.eq(1).children().eq(currentIndex).append(verHtml);
						 }
						
				     //need  rectify  toby
					quoteUtil.loadData(2, currentIndex, quote.quoteInfo.styleNo);
					quoteUtil.loadData(3, currentIndex, quote.quoteInfo.client);
					quoteUtil.loadData(4, currentIndex, quote.quoteInfo.merchandiser);
					quoteUtil.loadData(5, currentIndex, quote.quoteInfo.clientStyleNo);
					quoteUtil.loadData(6, currentIndex, quote.quoteInfo.style);
					quoteUtil.loadData(7, currentIndex, quote.quoteInfo.content);
					quoteUtil.loadData(8, currentIndex, quote.quoteInfo.gauge);
					quoteUtil.loadData(9, currentIndex, quote.quoteInfo.size);
					quoteUtil.loadData(10, currentIndex, quote.panelWeight);
					quoteUtil.loadData(11, currentIndex, quote.garmentWeight);
					//and unit knittingUnit
					quoteUtil.loadData(12, currentIndex, quote.knittingCost);
					var optionIndex =1;
					switch (quote.knittingUnit){
					  case "MIN":
						  optionIndex=1;
						  break;
					  case "CNY":
						  optionIndex=2;
						  break;
					}
					$("#mainDiv table tr").eq(12).children().eq(currentIndex).find("select option[value='"+ optionIndex +"']").attr("selected",true);
					
					quoteUtil.loadData(13, currentIndex, quote.linkingMin);
					quoteUtil.loadData(14, currentIndex, quote.trimKnittingMin);
					quoteUtil.loadData(15, currentIndex, quote.handHookMin);
					quoteUtil.loadData(16, currentIndex, quote.handStitchingMin);
					quoteUtil.loadData(17, currentIndex, quote.embroideryrMin);
					quoteUtil.loadData(18, currentIndex, quote.printingPrice);
					quoteUtil.loadData(19, currentIndex, quote.sewnMin);
					quoteUtil.loadData(20, currentIndex, quote.buttonMin);
					quoteUtil.loadData(21, currentIndex, quote.specialWashingMin);
					quoteUtil.loadData(22, currentIndex, quote.postProcessPrice);
					quoteUtil.loadData(23, currentIndex, quote.coverStitchMin);
					quoteUtil.loadData(24, currentIndex, quote.costPrice);
					quoteUtil.loadData(25, currentIndex, quote.trimsMin);
					quoteUtil.loadData(26, currentIndex, quote.quoteInfo.status);
					quoteUtil.loadData(27, currentIndex, quote.quoteInfo.quoteDate);
					 //add new col to let user fill up
					//load again to auto load the first version
					if(isReloadVersion){
						if(versions.length>0){
							//sample order no
							quoteUtil.query(table.eq(1).children().eq(currentIndex).find("select").get(0),true,false);
						}
					}
					if(!isVersion){
					  quoteUtil.copyReserveCol();
					}
					
					messageUtil.alertMsg("正常得到数据!");
				 }else{
					 quoteUtil.emptyCol(currentCell);
					 //debug mod
				     messageUtil.alertMsg("查不到数据!");
				 }
			 
			 });
				
		},
		loadData:function(rowIndex,colIndex,value){
			var table = $("#mainDiv table tr");
			var textArray=[2,3,4,5,6,26,27];
			if(value==0 || value==null || value==undefined){
				value="";
				if(rowIndex==22){
					value="6";
				}
			}
			if(textArray.indexOf(rowIndex) != -1){
				table.eq(rowIndex).children().eq(colIndex).text(value);
			}else{
				 table.eq(rowIndex).children().eq(colIndex).children("input").val(value);
			}
			
		},
		submit:function(actionName){
			var form =$("#mainDiv form" );
			quoteUtil.assignIndex();
			//need set correct input name value for passing to action
			form.attr("action","techquote/"+actionName);
			//alert(form.serialize());
			$("form").submit();
		},
		emptyInput:function(currentCol){
			//pass this to this function
			var nodeIndex = $(currentCol).parent().prevAll("td").size();
			for (var index = 0; index < this.itemSize; index++) {
				var selectedTd = $("#mainDiv table tr").eq(index).children().eq(nodeIndex);
					//clear all inputed values
					var inputLen = selectedTd.children("input").size();
					if(index > 8){
						//only the td from 码数  to 最新报价时间
						if(inputLen>0 ){
							selectedTd.children("input").val("");
						}else{
							selectedTd.empty();
						}
						if(index== 22)
							selectedTd.children("input").val("6");
					}
			}
		},
		emptyCol:function(currentCol){
			//pass this to this function
			var nodeIndex = $(currentCol).parent().prevAll("td").size();
			for (var index = 0; index < this.itemSize; index++) {
				var selectedTd = $("#mainDiv table tr").eq(index).children().eq(nodeIndex);
					//clear all inputed values
					var inputLen = selectedTd.children("input").size();
					//version
					//toby
					if(inputLen>0 ){
							selectedTd.children("input").val("");
					}else{
						selectedTd.empty();
					}
					if(index==1)
						selectedTd.empty();
					if(index== 22)
						selectedTd.children("input").val("6");
			}
		},
		deleteCol:function(currentCol){
			//pass this to this function
			var emptyFlg = false;
			if($(currentCol).parent().attr("class") == "reserve"){
				emptyFlg =true;
			}
			var nodeIndex = $(currentCol).parent().prevAll("td").size();
			for (var index = 0; index < this.itemSize; index++) {
				var selectedTd = $("#mainDiv table tr").eq(index).children().eq(nodeIndex);
				if(emptyFlg){
					//clear all inputed values
					selectedTd.children("input").val("");
				}else{
					selectedTd.remove();
				}
			}
		},
		copyReserveCol:function(){
			var nodeIndex = $("#mainDiv table tr").first().children(".reserve").prevAll("td").size();
			for (var index = 0; index < this.itemSize; index++) {
				var copySource = $("#mainDiv table tr").eq(index).children().eq(nodeIndex).clone();
				var destination = $("#mainDiv table tr").eq(index);
				destination.append(copySource);
			}
			//clean all copyed datas
			var newest = $("#mainDiv table tr").eq(0).children().eq(nodeIndex+1).find("input");
			this.emptyCol(newest);
			
			//remove the first td's class attribution (.reserve)
			var oldTd = $("#mainDiv table tr").first().children(".reserve").eq(0);
			oldTd.removeAttr("class");
			
		},
		countCols:function(){
			return $("#mainDiv table tr").first().children("td").size();
		},
		getCurrentColIndex:function(currentCol){
			return $(currentCol).parent().prevAll("td").size();
		},
		assignIndex:function(){
			//do this method before saving datas
			//将input对象一次按序号排列
			//<input name="quoteList[xx].x.x"> -->   <input name="quoteList[0].x.x">,<input name="quoteList[1].x.x">
			var lastColIndex =this.countCols()-1;
			for (var colIndex = 1; colIndex <= lastColIndex; colIndex++) {
				//last col need remove
				
				var  newColIndex = colIndex;
				for (var rowIndex = 0; rowIndex < this.itemSize; rowIndex++) {
					var input = $("#mainDiv table tr").eq(rowIndex).children().eq(colIndex).children("input,select");
					//special process for version selection
					if(rowIndex == 1){
						input = $("#mainDiv table tr").eq(rowIndex).children().eq(colIndex).children("input,input:hidden");
					}
					//knittingUnitInt
					if(rowIndex == 12){
					    var select = $("#mainDiv table tr").eq(rowIndex).children().eq(colIndex).find("select");
						var selectOldName = select.attr("name");
						if(selectOldName != undefined){
							var selectNewName = selectOldName.replace('xx',newColIndex-1);
							select.attr("name",selectNewName);
						}
					}
					var oldName = input.attr("name");
					if(oldName != undefined){
						var newName = oldName.replace('xx',newColIndex-1);
						input.attr("name",newName);
					}
					
				}
				
			}
			
			return $("#mainDiv table tr").first().children("td").size();
		}
		
};

$(document).ready(function(){
	//$.getJSON(url,function(data){ });
	$("#buttonDiv [name='save']").click( function () {
		 quoteUtil.submit("save");
	});
	$("#buttonDiv [name='commit']").click( function () {
		 quoteUtil.submit("commit");
	});
	/*
	$("#buttonDiv [name='approval']").click( function () {
		 quoteUtil.submit("approval");
	});
	*/
	$("#buttonDiv [name='download']").click( function () {
		 quoteUtil.submit("download");
	});
	activeTitle();
});