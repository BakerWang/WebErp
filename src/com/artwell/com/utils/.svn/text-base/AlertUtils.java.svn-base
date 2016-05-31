package com.artwell.com.utils;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.artwell.erp.techquote.dao.LdapDao;
import com.sun.mail.util.MailSSLSocketFactory;

public class AlertUtils {

	private static Session session =null;
	private static void  init(String sender) throws GeneralSecurityException{
		Properties prop = new Properties();
		MailSSLSocketFactory sf = new MailSSLSocketFactory();  
		sf.setTrustAllHosts(true);
		prop.put("mail.imap.ssl.socketFactory", sf);
		prop.put("mail.smtp.ssl.socketFactory", sf);
		prop.put("mail.store.protocol", "imap");  
		prop.put("mail.imap.host", "imap.artwell-hk.com");  
		prop.put("mail.imap.starttls.enable", "true");
		prop.put("mail.imap.port", "143");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.artwell-hk.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.from", sender);
		//prop.setProperty("mail.debug", "true");
		if(session==null){
			session = Session.getInstance(prop, null);
		}
	}


	public static void sendeHtmlMsg(String sender,String pwd,String subject,String htmlContent,String recipient) throws GeneralSecurityException, MessagingException {
		init(sender);
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, recipient);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// attachment
		Multipart mp = new MimeMultipart();
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(htmlContent, "text/html;charset=utf-8");
		mp.addBodyPart(mbp);
		msg.setContent(mp);
		Transport transport = session.getTransport("smtp");
		//get from ldap
		transport.connect(sender, pwd);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
		//System.out.println("send mail done");
	}
	
	public static void main(String[] args) {
		String sender ="test@artwell-hk.com";
		String pwd ="test123";
		String subject ="Test";
		String htmlContent ="kdjwjlwjwljw";
		String recipient ="toby.yao@artwell-hk.com";
		try {
			
			AlertUtils.sendeHtmlMsg(sender, pwd, subject, htmlContent, recipient);
			
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
