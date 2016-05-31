package com.artwell.com.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.artwell.erp.techquote.entity.Quote;
import com.artwell.erp.techquote.entity.QuoteInfo;
import com.artwell.erp.techquote.entity.Unit;

public class QuoteTag {
	
	public static String formatDisplayVal(String inputWord){
		Pattern p=Pattern.compile("\\.0*[^0]+0*"); 
		Matcher m=p.matcher(inputWord); 
		if(!m.find()){
			//mean this number is .000 format
			//remove 0 partition
			Pattern zeroFraction=Pattern.compile("\\.0*"); 
			m=zeroFraction.matcher(inputWord); 
			if(m.find()){
				inputWord =inputWord.replaceAll(m.group(), "");
			}
		}
		return inputWord;
	}
	public static String quoteHtml(List<Quote> quoteList,String propertyName,boolean isEdible,Unit unit) {
		String html="";
		if(quoteList==null)
			return html;
		for (Quote quote : quoteList) {
			try {
				if("panelWeight".equals(propertyName)){
					System.out.println("");
				}
				QuoteInfo quoteInfo = quote.getQuoteInfo();
				//exclude empty case
				if(quoteInfo.getSampleOrderNo() == null || quoteInfo.getSampleOrderNo().isEmpty()){
					continue;
				}
				Object propertyValue =getValue(quote,propertyName);
				if(propertyValue == null)
					propertyValue ="";
				//obj   return type
				if(isEdible){
					if(propertyName.indexOf("sampleOrderNo" )!= -1){
						html =html +"<td><input value='"+ propertyValue +"' type='text' placeholder='扫描样板单号' name='quoteList[xx]."+ propertyName +"' onblur='quoteUtil.query(this,false,true)' /></td>";
						continue;
					}
					if(propertyName.indexOf("knittingCost" )!= -1){
						Unit knittingUnit =(Unit)getValue(quote,"knittingUnit");
						if(knittingUnit == null){
							//init this 
							knittingUnit = Unit.MIN;
						}
						html =html +"<td ><input value='"+ formatDisplayVal(propertyValue.toString()) +"'  type='text' name='quoteList[xx]."+ propertyName +"' /><span><select name='quoteList[xx].knittingUnitInt'>";
						switch (knittingUnit) {
						case MIN:
							html =html +"<option value='1' selected='selected'>分钟</option><option value='2'>元</option></select></span></td>";
							break;
						case CNY:
							html =html +"<option value='1'>分钟</option><option value='2'  selected='selected'>元</option></select></span></td>";
							break;							
						default:
							html =html +"<option value='1' selected='selected'>分钟</option><option value='2'>元</option></select></span></td>";
							break;
						}
						continue;
					}
					
					if(unit!=null){
						html =html +"<td><input value='"+ formatDisplayVal(propertyValue.toString()) +"' type='text' name='quoteList[xx]."+propertyName +"'/><label>"+unit.toString() +"</label></td>";
						continue;
					}
					//do not display 0 value
					html =html +"<td><input value='"+ formatDisplayVal(propertyValue.toString()) +"' type='text' name='quoteList[xx]."+propertyName+"' /></td>";
				}else{
					html =html +"<td class='unedible'>"+ propertyValue +"</td>";
				}
				
			}  catch (SecurityException e) {
				e.printStackTrace();
			} catch (IntrospectionException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return html;
	}
		
	public static Object getValue(Object quote,String propertyName) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object  result = null;
		String[]  properties = propertyName.split("\\.");
		for (int i = 0; i < properties.length; i++) {
			if(i == 0){
				 result =getter(quote,properties[i]);
			}else{
				 result =getter(result,properties[i]);
			}
	}
		return result;
	}
	public static Object getter(Object obj,String propertyName) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class objClass = obj.getClass();
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, objClass);
        Method getter = pd.getReadMethod();
        Object value = getter.invoke(obj, new Object[]{});
        return value;
	}

}