package com.artwell.erp.techquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.artwell.com.utils.DatabaseUtils;
import com.artwell.erp.techquote.entity.Quote;
import com.artwell.erp.techquote.entity.QuoteId;
import com.artwell.erp.techquote.entity.QuoteInfo;
import com.artwell.erp.techquote.entity.Status;
import com.artwell.erp.techquote.entity.Unit;
@Service
public class QuoteDao {
	private static QuoteDao quoteDao=null;
	public static QuoteDao getSingleTon(){
		if(quoteDao == null){
			quoteDao = new QuoteDao();
		}
		return quoteDao;
	}
	public boolean upsert (List<Quote> quoteList,Status status) throws SQLException{
		Connection con = DatabaseUtils.getConnection();
		PreparedStatement stmt =null;
		ResultSet rs =null;
		String querySQL="SELECT COUNT(id) FROM  WEBERPDB.dbo.quote WHERE sampleOrderNo=? AND version=?";
		String upsertSQL="";
		for (Quote quote : quoteList) {
		  QuoteInfo quoteInfo =	quote.getQuoteInfo();
		  if(quoteInfo.getSampleOrderNo() ==null || quoteInfo.getSampleOrderNo().isEmpty()){
			  continue;
		  }
		  stmt =con.prepareStatement(querySQL);
		  stmt.setString(1, quoteInfo.getSampleOrderNo());
		  stmt.setInt(2, quoteInfo.getVersion());
		  rs  =  stmt.executeQuery();
		  int countNo = 0;
	      //this case only have one record
		  while (rs.next()) {
			  countNo= rs.getInt(1);
	      }
		  if(countNo > 0){
			  //update
			  upsertSQL="UPDATE WEBERPDB.dbo.quote SET  sampleOrderNo=?, version=?,content=?, gauge=?, size=?, status=?,  quoteDate=?, panelWeight=?, garmentWeight=?, knittingCost=?,"
			  		+ " knittingUnit=?, linkingMin=?, trimKnittingMin=?, handHookMin=?, handStitchingMin=?, embroideryrMin=?,printingPrice=?, sewnMin=?, "
			  		+ "buttonMin=?, specialWashingMin=?, postProcessPrice=?, coverStitchMin=?, costPrice=?, trimsMin=? WHERE  sampleOrderNo=? AND version=?";
		  }else{
			  //insert
			  upsertSQL ="INSERT INTO WEBERPDB.dbo.quote (sampleOrderNo, version, content, gauge, size, status, quoteDate, panelWeight, garmentWeight, knittingCost, knittingUnit, "
			  		+ "linkingMin, trimKnittingMin, handHookMin, handStitchingMin, embroideryrMin, printingPrice, sewnMin, buttonMin, specialWashingMin, postProcessPrice, coverStitchMin, costPrice, trimsMin) VALUES (?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		  }
	  		
	      stmt =con.prepareStatement(upsertSQL);
		  stmt.setString(1, quoteInfo.getSampleOrderNo());
		  stmt.setInt(2, quoteInfo.getVersion());
		  stmt.setString(3, quoteInfo.getContent());
		  stmt.setString(4, quoteInfo.getGauge());
		  stmt.setString(5, quoteInfo.getSize());
		  stmt.setInt(6, status.getValue());
		  stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
		  stmt.setFloat(8, quote.getPanelWeight()==null?0:quote.getPanelWeight());
		  stmt.setFloat(9, quote.getGarmentWeight()==null?0:quote.getGarmentWeight());
		  stmt.setFloat(10, quote.getKnittingCost()==null?0:quote.getKnittingCost());
		  stmt.setInt(11, quote.getKnittingUnitInt());
		  stmt.setFloat(12, quote.getLinkingMin()==null?0:quote.getLinkingMin());
		  stmt.setFloat(13, quote.getTrimKnittingMin()==null?0:quote.getTrimKnittingMin());
		  stmt.setFloat(14, quote.getHandHookMin()==null?0:quote.getHandHookMin());
		  stmt.setFloat(15, quote.getHandStitchingMin()==null?0:quote.getHandStitchingMin());
		  stmt.setFloat(16, quote.getEmbroideryrMin()==null?0:quote.getEmbroideryrMin());
		  stmt.setFloat(17, quote.getPrintingPrice()==null?0:quote.getPrintingPrice());
		  stmt.setFloat(18, quote.getSewnMin()==null?0:quote.getSewnMin());
		  stmt.setFloat(19, quote.getButtonMin()==null?0:quote.getButtonMin());
		  stmt.setFloat(20, quote.getSpecialWashingMin()==null?0:quote.getSpecialWashingMin());
		  stmt.setFloat(21, quote.getPostProcessPrice()==null?0:quote.getPostProcessPrice());
		  stmt.setFloat(22, quote.getCoverStitchMin()==null?0:quote.getCoverStitchMin());
		  stmt.setFloat(23, quote.getCostPrice()==null?0:quote.getCostPrice());
		  stmt.setFloat(24, quote.getTrimsMin()==null?0:quote.getTrimsMin());
		  if(countNo > 0){
			  //update
			  stmt.setString(25, quoteInfo.getSampleOrderNo());
			  stmt.setFloat(26, quoteInfo.getVersion());
		  }
		  //return false when occured error
		  stmt.execute();
		
		}
		
		return true;
	}

	public List<Quote> retrieveQuoteList(List<QuoteId> quoteIdList) throws SQLException{
		List<Quote> quoteList = new ArrayList<Quote>();
		Connection connection = DatabaseUtils.getConnection();
		String quoteInfoSQL= "SELECT sNumber,xStyleNo,xCustStyleNo,xstyle,xYSpec,xsymbol,xUserName,xgauge FROM "
				+ "(SELECT sNumber,xStyleNo,xCustStyleNo,xstyle,xYSpec,rCust_Id,sowner,xgauge FROM ERPDB.dbo.et_style"
				+ "  WHERE sid=? or sNumber=? ) as a "
				+ "INNER JOIN ERPDB.dbo.zt_customer ON zt_customer.sid = a.rCust_Id and zt_customer.xkind=1 "
				+ "INNER JOIN ERPDB.dbo.at_user ON at_user.xUserCode = a.sowner";
		
		String quoteSQL= "SELECT * FROM WEBERPDB.dbo.quote WHERE sampleOrderNo=? and version=?";
		String versionSQL= "SELECT version FROM WEBERPDB.dbo.quote WHERE sampleOrderNo=? ORDER BY version ASC";
		for (QuoteId quoteId : quoteIdList) {
			int barcode = quoteId.getSampleOrderBarcode()==null?0:quoteId.getSampleOrderBarcode();
			String sampleOrderNo = quoteId.getSampleOrderNo();
			PreparedStatement stmt =  connection.prepareStatement(quoteInfoSQL);
			stmt.setInt(1, barcode);
			stmt.setString(2, sampleOrderNo);
			ResultSet rs  =  stmt.executeQuery();
			Quote quote = null;
			//this case only have one record
			while (rs.next()) {
				quote = new Quote();
				//only get erp info not include quote price information
				QuoteInfo quoteInfo = new QuoteInfo();
				quoteInfo.setSampleOrderNo(rs.getString("sNumber"));
				quoteInfo.setStyleNo(rs.getString("xStyleNo"));
				quoteInfo.setClientStyleNo(rs.getString("xCustStyleNo"));
				quoteInfo.setStyle(rs.getString("xstyle"));
				quoteInfo.setContent(rs.getString("xYSpec"));
				quoteInfo.setClient(rs.getString("xsymbol"));
				quoteInfo.setMerchandiser(rs.getString("xUserName"));
				quoteInfo.setGauge(rs.getString("xgauge"));
				quote.setQuoteInfo(quoteInfo);
			}
			//query quote info by sampleOrderNo and version from WEBERP.dbo.quote
			//补全其它信息
			if(quote != null){
				sampleOrderNo = quote.getQuoteInfo().getSampleOrderNo();
				//---------------------version list existed in DB-------------------------
				stmt =  connection.prepareStatement(versionSQL);
				stmt.setString(1, sampleOrderNo);
				rs  =  stmt.executeQuery();
				List<Integer> versionList = new ArrayList<Integer>();
				//get version list by matching sampleorderno 
				while (rs.next()) {
					versionList.add(rs.getInt("version"));
				}
				quote.getQuoteInfo().setVersionList(versionList);
				
				//----------------------------------------------
				int version = quoteId.getVersion()==null?0:quoteId.getVersion();
				stmt =  connection.prepareStatement(quoteSQL);
				stmt.setString(1, sampleOrderNo);
				stmt.setInt(2, version);
				rs  =  stmt.executeQuery();
				//this case only have one record
				while (rs.next()) {
					String content = rs.getString("content");
					if(content !=null)
						quote.getQuoteInfo().setContent(content);
					String gauge = rs.getString("gauge");
					if(gauge !=null)
						quote.getQuoteInfo().setGauge(gauge);
					quote.getQuoteInfo().setVersion(version);
					quote.getQuoteInfo().setSize(rs.getString("size"));
					quote.getQuoteInfo().setStatus(Status.of(rs.getInt("status")));
					quote.getQuoteInfo().setQuoteDate(rs.getTimestamp("quoteDate"));
					quote.setPanelWeight(rs.getFloat("panelWeight"));
					quote.setGarmentWeight(rs.getFloat("garmentWeight"));
					quote.setKnittingCost(rs.getFloat("knittingCost"));
					quote.setKnittingUnit( Unit.of(rs.getInt("knittingUnit")) );
					quote.setLinkingMin(rs.getFloat("linkingMin"));
					quote.setTrimKnittingMin(rs.getFloat("trimKnittingMin") );
					quote.setHandHookMin( rs.getFloat("handHookMin"));
					quote.setHandStitchingMin(rs.getFloat("handStitchingMin") );
					quote.setEmbroideryrMin(rs.getFloat("embroideryrMin") );
					quote.setPrintingPrice(rs.getFloat("printingPrice") );
					quote.setSewnMin(rs.getFloat("sewnMin") );
					quote.setButtonMin(rs.getFloat("buttonMin") );
					quote.setSpecialWashingMin(rs.getFloat("specialWashingMin") );
					quote.setPostProcessPrice(rs.getFloat("postProcessPrice") );
					quote.setCoverStitchMin(rs.getFloat("coverStitchMin") );
					quote.setCostPrice(rs.getFloat("costPrice") );
					quote.setTrimsMin(rs.getFloat("trimsMin") );
				}
				quoteList.add(quote);
			}
			
		}
		return quoteList;
	}
	public String retrieveClientMapping(String erpClient) throws SQLException{
		Connection connection = DatabaseUtils.getConnection();
		String lookupSQL= "SELECT ldapGroupName FROM  WEBERPDB.dbo.clientMap WHERE erpValue=?";
			PreparedStatement stmt =  connection.prepareStatement(lookupSQL);
			stmt.setString(1, erpClient);
			ResultSet rs  =  stmt.executeQuery();
			Quote quote = null;
			//this case only have one record
			while (rs.next()) {
				return rs.getString("ldapGroupName");
			}
			
		return null;
	}
	
}
