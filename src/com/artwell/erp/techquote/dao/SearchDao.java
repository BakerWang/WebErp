package com.artwell.erp.techquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.artwell.com.utils.DatabaseUtils;
import com.artwell.erp.techquote.entity.QuoteInfo;
import com.artwell.erp.techquote.entity.Status;

public class SearchDao {

	public List<QuoteInfo> retrieveQuoteList(String searchValue,String client,Status status,String bDate,String eDate) throws SQLException{
		List<QuoteInfo> quoteInfoList = new ArrayList<QuoteInfo>();
		Connection con = DatabaseUtils.getConnection();
		ResultSet rs= null;
		PreparedStatement stmt =null;
//		String lookupSQL ="SELECT xsymbol,xStyleNo,sNumber,xSampleType,version,quoteDate,status FROM"
//				+ " ( SELECT sNumber,xStyleNo,rCust_Id,xSampleType FROM ERPDB.dbo.et_style  WHERE snumber=? or xStyleNo=? ) as a "
//				+ " INNER JOIN ERPDB.dbo.zt_customer ON zt_customer.sid = a.rCust_Id and zt_customer.xkind=1 and zt_customer.xsymbol like '"+ client +"' "
//				+ "INNER JOIN WEBERPDB.dbo.quote ON a.sNumber=sampleOrderNo";
		String lookupSQL ="SELECT xsymbol,xStyleNo,sNumber,xSampleType,version,quoteDate,status FROM"
		+ " ( SELECT sNumber,xStyleNo,rCust_Id,xSampleType FROM ERPDB.dbo.et_style  WHERE sid=? or snumber=? or xStyleNo=? ) as a "
		+ " INNER JOIN ERPDB.dbo.zt_customer ON zt_customer.sid = a.rCust_Id and zt_customer.xkind=1 and zt_customer.xsymbol like '";
		String endSQL ="' INNER JOIN WEBERPDB.dbo.quote ON a.sNumber=sampleOrderNo";
		if(client == null || client.isEmpty()){
			client="%";
		}else{
			client=client+"%";
		}
		lookupSQL = lookupSQL +client + endSQL;
		if(status != null){
			lookupSQL = lookupSQL +" and  status ="+ status.getValue();
		}

		if(bDate != null && (!bDate.isEmpty())){
			lookupSQL = lookupSQL +" and quoteDate >='"+bDate +"'";
		}
		
		if(eDate != null && (!eDate.isEmpty())){
			lookupSQL = lookupSQL +" and quoteDate <='"+eDate +"'";
		}
		//end sql 
		stmt =  con.prepareStatement(lookupSQL);
		if(Pattern.matches("\\d+",searchValue)){
			stmt.setString(1, searchValue);
		}else{
			stmt.setString(1, "0");
		}
		stmt.setString(2, searchValue);
		stmt.setString(3, searchValue);
		 rs  =  stmt.executeQuery();
		//this case only have one record
		while (rs.next()) {
			//only get erp info not include quote price information
			QuoteInfo quoteInfo = new QuoteInfo();
			quoteInfo.setClient(rs.getString("xsymbol"));
			quoteInfo.setStyleNo(rs.getString("xStyleNo"));
			quoteInfo.setSampleOrderNo(rs.getString("sNumber"));
			quoteInfo.setSampleType(rs.getString("xSampleType"));
			quoteInfo.setVersion(rs.getInt("version"));
			quoteInfo.setQuoteDate(rs.getTimestamp("quoteDate"));
			quoteInfo.setStatus(Status.of(rs.getInt("status")));
			quoteInfoList.add(quoteInfo);
		}
		
		return quoteInfoList;
	}
	
	private static SearchDao searchDao=null;
	public static SearchDao getSingleTon(){
		if(searchDao == null){
			searchDao = new SearchDao();
		}
		return searchDao;
	}
}
