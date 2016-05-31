package com.artwell.erp.common;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dom4j.DocumentException;

import com.artwell.com.entity.User;
import com.artwell.com.utils.XMLParser;
import com.artwell.erp.techquote.dao.LdapDao;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public abstract  class BasicAction extends ActionSupport {
	protected static final String USER_SESSION_KEY ="userInfo";
	protected static final String SENDER_MAIL ="erp_alert@artwell-hk.com";
	protected static final String SENDER_PWD ="tBv9FD0w";
	protected static  String AUTO_SENDER_RECIPIENT ="";
	protected static  String RECIPIENT_1 ="xf.mao@artwell-hk.com";
	protected static  String RECIPIENT_2 ="daniel.zhang@artwell-hk.com";
	protected Privilege privilege;
	protected LdapDao ldapDao = LdapDao.getInstance();
	protected MessageInfo messageInfo;
	private static final long serialVersionUID = 1L;
	//method
	protected boolean  isLogined(){
		String userName = (String)ActionContext.getContext().getSession().get(USER_SESSION_KEY);
		if(userName== null){
			return false;
		}
		return true;
	}
	protected boolean  checkPrivilege(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session =request.getSession();
		//check if have logined
		String userName = (String)session.getAttribute(USER_SESSION_KEY);
		String currtenUrl = request.getServletPath();
		// A)得到当前用户所在的组(ldap)
		User user = ldapDao.getGroupsOfUser(userName);
		// B)根据url 找到access
		//accessControlControl.xml 所分配的组
		XMLParser accessControlParser = XMLParser.getInstance();
		try {
			XMLParser.WebUrl  webUrl = accessControlParser.parserAccessControlXml(currtenUrl);
			setPrivilege(user, webUrl);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if(!privilege.isApprover() && !privilege.isEditor() && !privilege.isViewer()){
			messageInfo =new MessageInfo(false,  MessageInfo.MSG001);
			return false;
		}
		return true;
	};
	private void setPrivilege(User user ,XMLParser.WebUrl  webUrl) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(webUrl.getApproverList()==null && webUrl.getEditorList()==null && webUrl.getViewerList()==null){
			privilege = new Privilege();
			return;
		}
		Class webUrlClass = webUrl.getClass();
		String[] propertyNames={"approverList","editorList","viewerList"};
		privilege = new Privilege();
		for (String propertyName : propertyNames) {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, webUrlClass);
			Method getter = pd.getReadMethod();
			//get url configuration
			List<String> actorList = (ArrayList<String>)getter.invoke(webUrl, new Object[]{});
			for (String actor : actorList) {
				List<String> groupList = user.getGroupList();
				if(groupList != null && groupList.contains(actor)){
					switch (propertyName) {
					case "approverList":
						privilege.setApprover(true);
						break;
					case "editorList":
						privilege.setEditor(true);
						break;
					case "viewerList":
						privilege.setViewer(true);
						break;
					}
					break;
				}
			}
		}
		
	}
	public class MessageInfo{
		boolean isSuccess;
		String msg;
		String msgLevel;
		public final static String MSG001 ="没有权限访问当前网页!";
		public final static String MSG002 ="数据库事物处理异常!";
		public final static String MSG003 ="用户名或密码不正确!";
		public final static String MSG004 ="成功!";
		public final static String MSG005 ="找不到数据!";
		public final static String MSG006 ="邮件发送失败!";
		public final static String MSG007 ="报价单生成excel出错!";
		public MessageInfo(boolean isSuccess, String msg) {
			this.isSuccess = isSuccess;
			this.msg = msg;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public boolean isSuccess() {
			return isSuccess;
		}
	}
	public class Privilege{
		private boolean isApprover;
		private boolean  isEditor;
		private boolean  isViewer;
		public boolean isApprover() {
			return isApprover;
		}
		public void setApprover(boolean isApprover) {
			this.isApprover = isApprover;
		}
		public boolean isEditor() {
			return isEditor;
		}
		public void setEditor(boolean isEditor) {
			this.isEditor = isEditor;
		}
		public boolean isViewer() {
			return isViewer;
		}
		public void setViewer(boolean isViewer) {
			this.isViewer = isViewer;
		}

	}
	public LdapDao getLdapDao() {
		return ldapDao;
	}
	public void setLdapDao(LdapDao ldapDao) {
		this.ldapDao = ldapDao;
	}

}
