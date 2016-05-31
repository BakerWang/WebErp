package com.artwell.erp.techquote.action;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.artwell.com.utils.AlertUtils;
import com.artwell.com.utils.QuoteTag;
import com.artwell.erp.common.BasicAction;
import com.artwell.erp.techquote.dao.QuoteDao;
import com.artwell.erp.techquote.entity.Quote;
import com.artwell.erp.techquote.entity.QuoteId;
import com.artwell.erp.techquote.entity.QuoteInfo;
import com.artwell.erp.techquote.entity.Status;
import com.artwell.erp.techquote.entity.Unit;
import com.opensymphony.xwork2.Action;

public class QuoteAction extends BasicAction{

	private static final long serialVersionUID = 1L;
	private QuoteDao quoteDao =QuoteDao.getSingleTon();
	//input/output
	private List<Quote> quoteList;
	private List<QuoteId> quoteIdList;

	//action(A) 
	public String save(){
		if(!isLogined()){
			return Action.LOGIN;
		}
		if (!checkPrivilege()) {
			return Action.ERROR;
		}

		try {
			quoteDao.upsert (quoteList,Status.未报价);
			//send mail to TECHNIICAN
		} catch(SQLException e) {
			//record
			messageInfo =new MessageInfo(false,  MessageInfo.MSG002);
			return Action.ERROR;
		}
		quoteIdList = convertQuoteList2QuoteIdList(quoteList);
		query();
		try {
			sendMail(SENDER_MAIL,SENDER_PWD,RECIPIENT_1,Status.未报价,false);
		}  catch (Exception e) {
			messageInfo =new MessageInfo(true,  MessageInfo.MSG006);
			return  Action.SUCCESS;
		}
		messageInfo =new MessageInfo(true,  MessageInfo.MSG004);
		return Action.SUCCESS;
	}

	//action(B)
	public String commit() {
		if(!isLogined()){
			return Action.LOGIN;
		}
		if (!checkPrivilege()) {
			return Action.ERROR;
		}
		if (privilege.isEditor()) {

			try {
				quoteDao.upsert(quoteList,Status.已报价);
			} catch (SQLException e) {
				messageInfo =new MessageInfo(false,  MessageInfo.MSG002);
				return Action.ERROR;
			}
			quoteIdList = convertQuoteList2QuoteIdList(quoteList);
			query();
			//send mail to HK 
			try {
				sendMail(SENDER_MAIL,SENDER_PWD,AUTO_SENDER_RECIPIENT,Status.已报价,true);
				//sendMail(SENDER_MAIL,SENDER_PWD,RECIPIENT_2,Status.已报价,false);
			} catch (Exception e) {
				messageInfo =new MessageInfo(true,  MessageInfo.MSG006);
				return  Action.SUCCESS;
			}
		} 
		messageInfo =new MessageInfo(true,  MessageInfo.MSG004);
		return  Action.SUCCESS;
	}
	//action(C) 
	public String query(){
		if(!isLogined()){
			return Action.LOGIN;
		}
		if (!checkPrivilege()) {
			return Action.ERROR;
		}
		if(quoteIdList== null){
			return Action.SUCCESS;
		}

		try {
			quoteList =quoteDao.retrieveQuoteList(quoteIdList);
			//send mail to TECHNIICAN
		} catch(SQLException e) {
			//record
			messageInfo =new MessageInfo(false,  MessageInfo.MSG002);
			return Action.ERROR;
		}
		messageInfo =new MessageInfo(true,  MessageInfo.MSG004);
		return Action.SUCCESS;
	}
	//action(D)
	/*
	public String approval() {
		if(!isLogined()){
			return Action.LOGIN;
		}
		if (!checkPrivilege()) {
			return Action.ERROR;
		}
		if (privilege.isApprover()) {

			try {
				quoteDao.upsert(quoteList,Status.已审核);
			} catch (SQLException e) {
				messageInfo =new MessageInfo(false,  MessageInfo.MSG002);
				return Action.ERROR;
			}
			quoteIdList = convertQuoteList2QuoteIdList(quoteList);
			query();
			//send mail to hk colleage
			try {
				sendMail(SENDER_MAIL,SENDER_PWD,AUTO_SENDER_RECIPIENT,Status.已审核,true);
			} catch (Exception e) {
				messageInfo =new MessageInfo(true,  MessageInfo.MSG006);
				return  Action.SUCCESS;
			}
		} 
		messageInfo =new MessageInfo(true,  MessageInfo.MSG004);
		return  Action.SUCCESS;
	}
	 * 
	 */
	private void generateExcel(boolean hasUnit) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException, IOException {
		FileOutputStream workbookOS = null;
		String[] titles={"样板单号","版本","本廠款号","客户","跟单员","客款号","款式","毛料成份","针型","码数","織片克重","毛衣克重","电脑摇片工时","套口工时","織下欄附件","手钩","手撞","繡花","印花","車布","布包/手工鈕(每粒)","特殊洗水","後加處理","哈梭","工费","辅料","状态","最新报价时间"};
		String[] properties={"quoteInfo.sampleOrderNo","quoteInfo.version","quoteInfo.styleNo","quoteInfo.client","quoteInfo.merchandiser","quoteInfo.clientStyleNo","quoteInfo.style","quoteInfo.content","quoteInfo.gauge","quoteInfo.size","panelWeight","garmentWeight","knittingCost","linkingMin","trimKnittingMin","handHookMin","handStitchingMin","embroideryrMin","printingPrice","sewnMin","buttonMin","specialWashingMin","postProcessPrice","coverStitchMin","costPrice","trimsMin","quoteInfo.status","quoteInfo.quoteDate"};
		String[] units={"","","","","","","","","","码",Unit.WEIGHT.toString(),Unit.WEIGHT.toString(),"",Unit.MIN.toString(),Unit.MIN.toString(),Unit.MIN.toString(),Unit.MIN.toString(),Unit.MIN.toString(),Unit.CNY.toString(),Unit.MIN.toString(),Unit.MIN.toString(),Unit.MIN.toString(),Unit.CNY.toString(),Unit.MIN.toString(),Unit.CNY.toString(),Unit.WEIGHT.toString(),"",""};
		String fileName = ServletActionContext.getServletContext().getRealPath("/WEB-INF/workbooks/quote.xlsx");
		Workbook  wb = new XSSFWorkbook();

		//remove the file when it has existed already
		File oldWorkbook =new File(fileName);
		if(oldWorkbook.exists()){
			oldWorkbook.delete();
		}
		workbookOS = new FileOutputStream(oldWorkbook);
		Sheet sheet=   wb.createSheet("quoteDetail");
		//------------   initialization to set the title -------------------------
		Row row =  null;
		for (int i = 0; i < titles.length; i++) {
			row =  sheet.createRow(i);
			row.createCell(0).setCellValue(titles[i]);
		}
		//------------   end initialization to set the title -------------------------
		int dataStartColIndex =1;
		for (Quote quote : quoteList) {
			//do next
			if( quote.getQuoteInfo().getSampleOrderNo() == null){
				continue;
			}
			//write data
			for (int jj = 0; jj < properties.length; jj++) {
				String propertyName = properties[jj];
				Object propertyValue =QuoteTag.getValue(quote,propertyName);
				row =  sheet.getRow(jj);
				if(propertyValue==null){
					continue;
				}
				//1 版本
				if(jj == 1){
					int version =(int)propertyValue;
					if(version==0){
						continue;
					}
				}
				String formattedPropertyVal = QuoteTag.formatDisplayVal(propertyValue.toString());
				//12  电脑摇片工时
				if(jj == 12){
					Object knittingUnit=  QuoteTag.getValue(quote,"knittingUnitInt");
					if(!hasUnit){
						row.createCell(dataStartColIndex).setCellValue(formattedPropertyVal );
						continue;
					}
					row.createCell(dataStartColIndex).setCellValue(formattedPropertyVal +" "+ Unit.of((int)knittingUnit).toString() );
				}else{
					if(!hasUnit){
						row.createCell(dataStartColIndex).setCellValue(formattedPropertyVal);
						continue;
					}
					row.createCell(dataStartColIndex).setCellValue(formattedPropertyVal+units[jj]);
				}

			}
			//for quotelist
			dataStartColIndex++;
		}
		wb.write(workbookOS);
		wb.close();	
	}
	//action(E)
	public String download() {
		if(!isLogined()){
			return Action.LOGIN;
		}
		if (!checkPrivilege()) {
			return Action.ERROR;
		}
		quoteIdList = convertQuoteList2QuoteIdList(quoteList);
		try {
			quoteList =quoteDao.retrieveQuoteList(quoteIdList);
			generateExcel(false);
		} catch (Exception e) {
			messageInfo =new MessageInfo(true,  MessageInfo.MSG007);
			return  "failed";
		}
		return  Action.SUCCESS;
	}
	private String currentUrl="http://erp.tx.internal:8888/WebErp/techquote/quote?";
	private String htmlContent ="<table border='1' style='border-collapse: collapse;'><tbody>";

	private void sendMail(String sender,String pwd,String recipient,Status status,boolean isAutoSend) throws Exception{
		generateUrl();
		generateHtml(currentUrl,status,isAutoSend);
		if(isAutoSend){
			recipient=AUTO_SENDER_RECIPIENT;
		}
		if(recipient == null){
			throw new Exception("收件人为空");
		}
		String subject= "";
		switch (status) {
//		case 未审核:
//			subject="样板单报价等待审核";
//			break;
		case 已报价:
			subject="样板单报价";
			break;
		case 未报价:
			subject="样板单报价已修改";
			break;
		}
		try {
			AlertUtils.sendeHtmlMsg(sender, pwd, subject, htmlContent, recipient);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	//generate current operating sampleOrders and versionInfo
	private void generateUrl(){
		String tempUrl="";
		for (int i = 0; i < quoteIdList.size(); i++) {
			QuoteId quoteId =quoteIdList.get(i);
			String sampleOrderNo = quoteId.getSampleOrderNo();
			int version =quoteId.getVersion();
			if(sampleOrderNo!= null  && (!sampleOrderNo.isEmpty())){
				//url
				tempUrl= tempUrl + "quoteIdList["+ i+"].sampleOrderNo="+sampleOrderNo+"&quoteIdList["+ i+"].version="+version+"&";
			}
		}
		currentUrl = currentUrl+tempUrl.substring(0, tempUrl.length()-1);
	}
	//will be displayed by sending mail
	private void generateHtml(String url,Status status,boolean isAutoSend) throws SQLException{
		String client = null;
		String merchandiser = null;
		Set<String> sampleOrderNoSet= new TreeSet<String>();
		Set<String> styleNoSet= new TreeSet<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String currentDate = dateFormat.format(Calendar.getInstance().getTime());

		for (Quote quote : quoteList) {
			QuoteInfo quoteInfo = quote.getQuoteInfo();
			String sampleOrderNo = quoteInfo.getSampleOrderNo();
			if(sampleOrderNo!= null  && (!sampleOrderNo.isEmpty())){
				client = quoteInfo.getClient();
				merchandiser = quoteInfo.getMerchandiser();
				sampleOrderNoSet.add(sampleOrderNo);
				styleNoSet.add(quoteInfo.getStyleNo());

			}
		}
		if(isAutoSend){
			String ldapGroupName = quoteDao.retrieveClientMapping(client);
			List<String> memberHKList = ldapDao.getMemberOfHK(ldapGroupName);
			if(memberHKList.size()>0){
				AUTO_SENDER_RECIPIENT=StringUtils.join(memberHKList.toArray(),",");
			}else{
				AUTO_SENDER_RECIPIENT="";
			}

		}
		htmlContent=htmlContent+"<tr><td>客户</td> <td>"+client+"</td></tr>";
		htmlContent=htmlContent+"<tr><td>跟单员</td> <td>"+merchandiser+"</td></tr>";
		htmlContent=htmlContent+"<tr><td>款号</td> <td>"+StringUtils.join(styleNoSet.toArray(), ",")+"</td></tr>";
		htmlContent=htmlContent+"<tr><td>样板单号</td> <td>"+StringUtils.join(sampleOrderNoSet.toArray(), ",")+"</td></tr>";
		htmlContent=htmlContent+"</tbody></table>";
		String template= "";
		switch (status) {
//		case 未审核:
//			template="时间,毛师傅已经完成以上报价，请审核.确认无误后，再发报给香港同事.";
//			break;
		case 已报价:
			template="时间，以上款号有最新的报价.";
			break;
		case 未报价:
			template="时间，你已对如上版单进行了报价编辑.";
			break;
		}
		htmlContent=htmlContent+"<div>"+currentDate + template +"</div><div>具体详情访问URL <a href='"+ url +"' target='_blank'>点击查看</a></div>";
	}
	private List<QuoteId> convertQuoteList2QuoteIdList(List<Quote> quoteList){
		List<QuoteId> quoteIdList = new ArrayList<QuoteId>();
		for (Quote quote : quoteList) {
			QuoteInfo  quoteInfo= quote.getQuoteInfo();
			QuoteId quoteId = new QuoteId();
			Integer sampleOrderBarcode = quoteInfo.getSampleOrderBarcode();
			String sampleOrderNo = quoteInfo.getSampleOrderNo();
			int version = quoteInfo.getVersion();
			if(version==0 && (sampleOrderNo==null || sampleOrderNo.isEmpty())  && sampleOrderBarcode==null ){
				continue;
			}
			
			quoteId.setSampleOrderBarcode(sampleOrderBarcode);
			quoteId.setSampleOrderNo(sampleOrderNo);
			quoteId.setVersion(version);
			quoteIdList.add(quoteId);
		}
		return quoteIdList;
	}
	public List<Quote> getQuoteList() {
		return quoteList;
	}

	public void setQuoteList(List<Quote> quoteList) {
		this.quoteList = quoteList;
	}
	public Privilege getPrivilege() {
		return privilege;
	}
	public MessageInfo getMessageInfo() {
		return messageInfo;
	}

	public List<QuoteId> getQuoteIdList() {
		return quoteIdList;
	}

	public void setQuoteIdList(List<QuoteId> quoteIdList) {
		this.quoteIdList = quoteIdList;
	}

	public QuoteDao getQuoteDao() {
		return quoteDao;
	}

	public void setQuoteDao(QuoteDao quoteDao) {
		this.quoteDao = quoteDao;
	}

}
