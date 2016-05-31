package com.artwell.erp.tna.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artwell.com.utils.DatabaseUtils;
import com.artwell.erp.tna.entity.PurchaseOrder;
import com.artwell.erp.tna.entity.SampleRecord;
import com.artwell.erp.tna.entity.StyleInfo;
import com.artwell.erp.tna.entity.StyleWorkflowRecord;

public class StyleWorkflowRecordDao {
	
	final static private Integer[] protoSampleTypeIds = {90001,90026,90066,90098,90105,90114,90129,90168,90182,90205,90218,90219,90220};
	final static private Integer[] fitSampleTypeIds = {90044,90052,90065,90074,90076,90079,90080,90083,90094,90096,90116,90119,90120,90128,90132,90140,90158,90159,90163,90195,90202,90203,90207,90210};
	final static private Integer[] salesmanSampleTypeIds = {90008,90060,90061,90078,90100,90121,90122,90123,90125,90148,90160,90161,90179};
	final static private Integer[] preproductionSampleTypeIds = {90067,90071,90089,90091,90099,90103,90106,90107,90110,90112,90117,90124,90135,90162,90164,90169,90174,90183,90186,90197,90208,90209};
public static void main(String[] args) {
	StyleWorkflowRecordDao test = new StyleWorkflowRecordDao();
	try {
		test.retrieveStyleWorkflowRecordList();
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
	
	public List<StyleWorkflowRecord> retrieveStyleWorkflowRecordList() throws SQLException {
		Map<String, StyleWorkflowRecord>   styleWorkflowRecordMap  =new HashMap<String, StyleWorkflowRecord>();
		Connection connection = DatabaseUtils.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs  = null;
		
		//-----------------------------------------------------------------------------
		//A)sample orders
		//select  et_style.sNumber,et_style.sCreateDate,et_style.xDate2,et_style.xDate1,et_style.sLastUpdate,xsymbol,et_style.xseason,xstyleno,xcuststyleno,st_systemdata.sid from ERPDB.dbo.et_style  join  ERPDB.dbo.st_systemdata on xsampletype =xValue and et_style.sid=531394785 join  ERPDB.dbo.zt_customer on zt_customer.sid=et_style.rCust_Id
		String sampleOrderSQL="SELECT  et_style.sNumber,et_style.sCreateDate,et_style.xDate2,et_style.xDate1,et_style.sLastUpdate,xsymbol,et_style.xseason,xstyleno,xcuststyleno,st_systemdata.sid"
				+ " FROM ERPDB.dbo.et_style  JOIN  ERPDB.dbo.st_systemdata ON xsampletype =xValue and sCreateDate>= '2015-09-01' "
				+ " JOIN  ERPDB.dbo.zt_customer"
				+ " ON zt_customer.sid=et_style.rCust_Id";
		
			stmt =  connection.prepareStatement(sampleOrderSQL);
			rs  =  stmt.executeQuery();
			while (rs.next()) {
				//styleInfo
				StyleInfo styleInfo = new StyleInfo();
				styleInfo.setClientName(rs.getString("xsymbol"));
				styleInfo.setSeasonName(rs.getString("xseason"));
				styleInfo.setFactoryStyleNumber(rs.getString("xstyleno"));
				styleInfo.setClientStyleNumber(rs.getString("xcuststyleno"));
				
				//sampleRecord
				SampleRecord sampleRecord = new SampleRecord();
				sampleRecord.setSampleOrderNumber(rs.getString("sNumber"));
				sampleRecord.setOrderDate(rs.getDate("sCreateDate"));
				sampleRecord.setRequiredDate(rs.getDate("xDate2"));
				sampleRecord.setDeliveryDate(rs.getDate("xDate1"));
				sampleRecord.setApprovalDate(rs.getDate("sLastUpdate"));
				
				int sampleTypeId =rs.getInt("sid");
				//used for map key
				String identifer = styleInfo.getIdentifer();
				StyleWorkflowRecord styleWorkflowRecord = styleWorkflowRecordMap.get(identifer);
				if(styleWorkflowRecord== null){
					//need new list 
					styleWorkflowRecord = setSampleRecord(styleWorkflowRecord, sampleTypeId,sampleRecord, true);
					
				}else{
					//add to list
					styleWorkflowRecord = setSampleRecord(styleWorkflowRecord, sampleTypeId,sampleRecord, false);
				}
				styleWorkflowRecordMap.put(identifer, styleWorkflowRecord);
				
			}
			
			//-----------------------------------------------------------------------------
			//B) purchase orders
			//select xsymbol,et_style.xseason,xstyleno,xcuststyleno,xso,et_shipment.xdate,(xqty1+xqty2+xqty3+xqty4+xqty5+xqty6+xqty7+xqty8+xqty9+xqty10+xqty11+xqty12) as qtys,et_order.sid from ERPDB.dbo.et_order join ERPDB.dbo.et_style on et_style.mid=et_order.sid  join ERPDB.dbo.et_shipment on et_shipment.mid= et_style.sid  join ERPDB.dbo.et_shipment_qty on et_shipment_qty.mid=et_shipment.sid  and et_shipment.mid=531395316 join  ERPDB.dbo.zt_customer on zt_customer.sid=et_style.rCust_Id 
			String purchaseOrderSQL="SELECT xsymbol,et_style.xseason,xstyleno,xcuststyleno,xso,et_shipment.xdate,(xqty1+xqty2+xqty3+xqty4+xqty5+xqty6+xqty7+xqty8+xqty9+xqty10+xqty11+xqty12) as qtys,et_order.sid"
					+ " FROM ERPDB.dbo.et_order"
					+ " join ERPDB.dbo.et_style ON et_style.mid=et_order.sid and  et_style.sCreateDate>= '2015-09-01' "
					+ " join ERPDB.dbo.et_shipment ON et_shipment.mid= et_style.sid"
					+ " join ERPDB.dbo.et_shipment_qty ON et_shipment_qty.mid=et_shipment.sid "
					+ " join  ERPDB.dbo.zt_customer ON zt_customer.sid=et_style.rCust_Id";
			
			stmt =  connection.prepareStatement(purchaseOrderSQL);
			rs  =  stmt.executeQuery();
			while (rs.next()) {
				//styleInfo
				StyleInfo styleInfo = new StyleInfo();
				styleInfo.setClientName(rs.getString("xsymbol"));
				styleInfo.setSeasonName(rs.getString("xseason"));
				styleInfo.setFactoryStyleNumber(rs.getString("xstyleno"));
				styleInfo.setClientStyleNumber(rs.getString("xcuststyleno"));
				
				//sampleRecord
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				purchaseOrder.setPurchaseOrderNumber(rs.getString("sid"));
				purchaseOrder.setNumberOfPieces(rs.getInt("qtys"));
				purchaseOrder.setShipmentDate(rs.getDate("xdate"));
				
				//used for map key
				String identifer = styleInfo.getIdentifer();
				StyleWorkflowRecord styleWorkflowRecord = styleWorkflowRecordMap.get(identifer);
				if(styleWorkflowRecord== null){
					//need new list 
					styleWorkflowRecord = setPurchaseOrder(styleWorkflowRecord, purchaseOrder, true);
					
				}else{
					//add to list
					styleWorkflowRecord = setPurchaseOrder(styleWorkflowRecord, purchaseOrder, false);
				}
				styleWorkflowRecordMap.put(identifer, styleWorkflowRecord);
				
			}
		//-----------------------------------------------------------------------------
		//convert Map into List
			List<StyleWorkflowRecord>  styleWorkflowRecordList = new ArrayList<StyleWorkflowRecord>();
			for (String key : styleWorkflowRecordMap.keySet()) {
				styleWorkflowRecordList.add(styleWorkflowRecordMap.get(key));
			}
		
		return styleWorkflowRecordList;
	}
	private StyleWorkflowRecord setPurchaseOrder(StyleWorkflowRecord styleWorkflowRecord,PurchaseOrder purchaseOrder,boolean isNew){
		if(isNew){
			//instance
			styleWorkflowRecord= new StyleWorkflowRecord();
		}
		
		List<PurchaseOrder> purchaseOrderList = styleWorkflowRecord.getPurchaseOrderList();
		if(purchaseOrderList==null){
			purchaseOrderList = new ArrayList<PurchaseOrder> ();
		}
		purchaseOrderList.add(purchaseOrder);
		styleWorkflowRecord.setPurchaseOrderList(purchaseOrderList);
		
		return styleWorkflowRecord;
		
	}
	
	private StyleWorkflowRecord setSampleRecord(StyleWorkflowRecord styleWorkflowRecord,int sampleTypeId,SampleRecord sampleRecord,boolean isNew){
		if(isNew){
			//instance
			styleWorkflowRecord= new StyleWorkflowRecord();
			List<SampleRecord> protoSampleRecordList = new ArrayList<SampleRecord>();
			List<SampleRecord> salesmanSampleRecordList = new ArrayList<SampleRecord>();
			List<SampleRecord> fitSampleRecordList = new ArrayList<SampleRecord>();
			List<SampleRecord> preproductionSampleRecordList = new ArrayList<SampleRecord>();
			styleWorkflowRecord.setProtoSampleRecordList(protoSampleRecordList);
			styleWorkflowRecord.setSalesmanSampleRecordList(salesmanSampleRecordList);
			styleWorkflowRecord.setFitSampleRecordList(fitSampleRecordList);
			styleWorkflowRecord.setPreproductionSampleRecordList(preproductionSampleRecordList);
		}
		
		List<SampleRecord> protoSampleRecordList = styleWorkflowRecord.getProtoSampleRecordList();
		List<SampleRecord> fitSampleRecordList = styleWorkflowRecord.getFitSampleRecordList();
		List<SampleRecord> salesmanSampleRecordList = styleWorkflowRecord.getSalesmanSampleRecordList();
		List<SampleRecord> preproductionSampleRecordList = styleWorkflowRecord.getPreproductionSampleRecordList();
		
		//classfy into approriate list
		List<Integer> protoSampleTypeIdList = Arrays.asList(protoSampleTypeIds);
		List<Integer> fitSampleTypeIdsList = Arrays.asList(fitSampleTypeIds);
		List<Integer> salesmanSampleTypeIdsList = Arrays.asList(salesmanSampleTypeIds);
		List<Integer> preproductionSampleTypeIdsList = Arrays.asList(preproductionSampleTypeIds);

		if(protoSampleTypeIdList.contains(sampleTypeId)){
			protoSampleRecordList.add(sampleRecord);
		}else if(fitSampleTypeIdsList.contains(sampleTypeId)){
			fitSampleRecordList.add(sampleRecord);
		}else if(salesmanSampleTypeIdsList.contains(sampleTypeId)){
			salesmanSampleRecordList.add(sampleRecord);
		}else if(preproductionSampleTypeIdsList.contains(sampleTypeId)){
			preproductionSampleRecordList.add(sampleRecord);
		}
		
		
		return styleWorkflowRecord;
	}
}
