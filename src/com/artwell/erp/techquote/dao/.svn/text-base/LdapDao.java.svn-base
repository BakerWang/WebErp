package com.artwell.erp.techquote.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

import com.artwell.com.entity.User;
import com.artwell.com.utils.SHAConverter;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

public class LdapDao {
	
	public List<String> getMemberOfHK(final String groupName) {
		if(groupName==null){
			return new ArrayList<String>();
		}
        LdapQuery query = LdapQueryBuilder .query().base("ou=Users,domainName=artwell-hk.com").attributes("memberofgroup","mail","cn").where("objectclass").is("mailUser");
        List<String> rs = ldapTemplate.search(query,new AttributesMapper<String>() {
		         	@Override
		 			public String  mapFromAttributes(Attributes attrs) throws NamingException {
		         			String userMail = (String)attrs.get("mail").get();
		         			String displayName = (String)attrs.get("cn").get();
		         			if(attrs.get("memberofgroup") != null ){
		         				NamingEnumeration<String> groups = (NamingEnumeration<String>)attrs.get("memberofgroup").getAll();
		         				//memberOfGroup under one user
		         				while(groups.hasMore()){
		         					String memberOfGroup =groups.next();
//		         						memberOfGroup = memberOfGroup.replaceAll(mailDomainName, "");
//        								memberOfGroup = memberOfGroup.replaceAll(PREFIX, "");
//        								memberOfGroup = memberOfGroup.replaceAll("_", " ");
        								if( memberOfGroup.indexOf(groupName) >=0 && (displayName.indexOf("(HK)") != -1) ){
        									//get all HK members
        									return userMail;
        								}
		         				}
		         			}
		         			return null;
		               }
		         	
		            });
        //remove all null object
        for (int i = rs.size()-1; i >= 0; i--) {
        	 rs.remove(null);
		}
		return rs;
	}
	
	  public static void main(String[] args) {
	    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
	    	LdapDao test = (LdapDao) context.getBean("ldapDao");
	    	//test.getGroupsOfUser("mario.lee");
	    	test.getGroupNames();
	    	//test.getMemberOfHK("anne.fountain");
		}
	  
	public List<String> getGroupNames() {
		LdapQuery query = LdapQueryBuilder.query()
				.base("ou=Groups,domainName=artwell-hk.com")
				.attributes("mail")
				.where("objectclass").is("mailList");

		List<String> rs = ldapTemplate.search(query,new AttributesMapper<String>() {
			@Override
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				String mail = ((String) attrs.get("mail").get());
				mail = mail.replaceAll("client_", "");
				mail = mail.replaceAll(mailDomainName, "");
				return mail;
			}
		});
		return rs;
	}
	
	public User getGroupsOfUser(String username) {
		username = username+mailDomainName;
        LdapQuery query = LdapQueryBuilder .query().base("ou=Users,domainName=artwell-hk.com").attributes("memberofgroup").where("objectclass").is("mailUser").and("mail").is(username);
        List<User> rs = ldapTemplate.search(query,new AttributesMapper<User>() {
		         	@Override
		 			public User  mapFromAttributes(Attributes attrs)
		                  throws NamingException {
		         			StringBuffer groupName = new StringBuffer();
		         			User user = new User();
		         			if(attrs.get("memberofgroup") != null ){
		         				List<String> groupList = new ArrayList<String>();
		         				NamingEnumeration<String> groups = (NamingEnumeration<String>)attrs.get("memberofgroup").getAll();
		         				while(groups.hasMore()){
		         					groupList.add(groups.next().replaceAll(mailDomainName, ""));
		         					//groupName.append((String)groups.next()).append(";");
		         				}
		         				user.setGroupList(groupList);
		         				//groupName.substring(0, groupName.length()-1);
		         			}
		         			return user;
		               }
		            });
        if(rs !=null && rs.size() == 1){
        	return rs.get(0);
        }
		return null;
	}
    //used for checking userName and pwd in loginAction
	public boolean authentication(String userName,String pwd) {
		boolean correct_flg = false;
		userName = userName+mailDomainName;
        LdapQuery query = LdapQueryBuilder .query()
           .base("ou=Users,domainName=artwell-hk.com")
           .attributes("mail","userpassword").where("objectclass").is("mailUser").and("mail").is(userName);
        /**
         * @return ldap password
         */
        String ldapPwd = null ;
         List<String> rs = ldapTemplate.search(query,new AttributesMapper<String>() {
         	@Override
 			public String mapFromAttributes(Attributes attrs)
                  throws NamingException {
         			Object tempPwd = attrs.get("userpassword").get();
         			byte[] pwd = (byte[]) tempPwd;
         			return new String(pwd);
               }
            });
           if(rs != null && rs.size() > 0){
        	    ldapPwd = rs.get(0);
           }else{
        	   return false;
           }
         
     	try {
     		SHAConverter converter = new SHAConverter();
     		correct_flg = converter.verifyByPwd(ldapPwd,pwd);
		} catch (Base64DecodingException e) {
			e.printStackTrace();
		}
     	return correct_flg;
     }
	
  
    private static LdapDao ldapDao=null;
    public static LdapDao getInstance(){
    	if(ldapDao == null){
    		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
    		ldapDao = (LdapDao) context.getBean("ldapDao");
    	}
    	return ldapDao;
    }
	private LdapTemplate ldapTemplate;
	private final static String mailDomainName = "@artwell-hk.com";

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	public String getMailDomainName() {
		return mailDomainName;
	}



}
