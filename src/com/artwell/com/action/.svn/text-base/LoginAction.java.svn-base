package com.artwell.com.action;

import com.artwell.com.entity.User;
import com.artwell.erp.common.BasicAction;
import com.artwell.erp.common.BasicAction.MessageInfo;
import com.artwell.erp.techquote.dao.LdapDao;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class LoginAction extends BasicAction {
	private static final long serialVersionUID = 1L;
	private User user;
	LdapDao ldapDao  =LdapDao.getInstance();;
	//(A)
	public String login(){
		if(user== null){
			messageInfo =new MessageInfo(false,  MessageInfo.MSG003);
			return Action.ERROR;
		}
		String userName =user.getUserName();
		// lookup from ldap database,check if it exsits,then set attribute into session
		boolean  isAllowed = ldapDao.authentication(userName, user.getPwd());
		if(!isAllowed){
			messageInfo =new MessageInfo(false,  MessageInfo.MSG003);
			return Action.ERROR;
		}
		ActionContext.getContext().getSession().put(USER_SESSION_KEY,userName);
		return Action.SUCCESS;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public MessageInfo getMessageInfo() {
		return messageInfo;
	}

}
