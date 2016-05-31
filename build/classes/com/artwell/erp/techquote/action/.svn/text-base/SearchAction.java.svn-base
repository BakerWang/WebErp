package com.artwell.erp.techquote.action;

import java.sql.SQLException;
import java.util.List;

import com.artwell.erp.common.BasicAction;
import com.artwell.erp.techquote.dao.SearchDao;
import com.artwell.erp.techquote.entity.QuoteInfo;
import com.artwell.erp.techquote.entity.Search;
import com.artwell.erp.techquote.entity.Status;
import com.opensymphony.xwork2.Action;

public class SearchAction extends BasicAction { 
	private static final long serialVersionUID = 8552506927094213340L;
	//input
	private Search search;
	//output
	private List<QuoteInfo> quoteInfoList;
	private SearchDao searchDao = SearchDao.getSingleTon();
	
	public String search() {
		if(!isLogined()){
			return Action.LOGIN;
		}
		if (!checkPrivilege()) {
			return Action.ERROR;
		}
	  if(search== null){
		  return Action.SUCCESS;
	  }
	  Status status = null;
	  if(search.getStatusInt() != 0 ){
		  status= Status.of(search.getStatusInt());
	  }
    try {
		quoteInfoList = searchDao.retrieveQuoteList(search.getSearchValue(), search.getClient(), status, search.getBegDate(),search.getEndDate());
	} catch (SQLException e) {
		System.out.println(e);
		messageInfo =new MessageInfo(false,  MessageInfo.MSG002);
		return Action.ERROR;
	}
    if(quoteInfoList.size()==0){
    	messageInfo =new MessageInfo(false,  MessageInfo.MSG005);
    }
       return Action.SUCCESS;
    }

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public List<QuoteInfo> getQuoteInfoList() {
		return quoteInfoList;
	}
	public MessageInfo getMessageInfo() {
		return messageInfo;
	}

}