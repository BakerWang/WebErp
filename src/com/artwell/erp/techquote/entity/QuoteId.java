package com.artwell.erp.techquote.entity;

public class QuoteId {
	private String sampleOrderNo;
	private Integer sampleOrderBarcode;
	private Integer version;
	
	public String getSampleOrderNo() {
		return sampleOrderNo;
	}
	public void setSampleOrderNo(String sampleOrderNo) {
		this.sampleOrderNo = sampleOrderNo;
	}

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getSampleOrderBarcode() {
		return sampleOrderBarcode;
	}
	public void setSampleOrderBarcode(Integer sampleOrderBarcode) {
		this.sampleOrderBarcode = sampleOrderBarcode;
	}
	
}
