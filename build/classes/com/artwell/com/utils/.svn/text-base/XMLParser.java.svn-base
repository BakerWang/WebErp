package com.artwell.com.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class XMLParser {
public static void main(String[] args) {
	XMLParser xMLParser = XMLParser.getInstance();
	try {
		xMLParser.parserAccessControlXml("ccc");
	} catch (DocumentException e) {
		e.printStackTrace();
	}
}
	public  WebUrl parserAccessControlXml(String url) throws DocumentException {
		//String fileName = this.getClass().getClassLoader().getResource("/accessControl.xml").getFile();
		String fileName = ServletActionContext.getServletContext().getRealPath("/WEB-INF/classes/accessControl.xml");
		File inputXml = new File(fileName);
		WebUrl webUrl = new WebUrl();
		List<String> viewerList = new ArrayList<String>() ;
		List<String> editorList = new ArrayList<String>();
		List<String> approverList = new ArrayList<String>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(inputXml); 
		Element root = document.getRootElement();
		List<Element> urlList = root.element("urls").elements("url");  
		for (Element element : urlList) {
			if(url.equals(element.attributeValue("value"))){
				//then get all groups under this url
				List<Element> actorList = element.elements();
				for (Element actor : actorList) {
					String groupName = actor.getText();
					switch (actor.getName()) {
					case "approver":
						approverList.add(groupName);
						break;
					case "editor":
						editorList.add(groupName);
						break;
					case "viewer":
						viewerList.add(groupName);
						break;
					}
				}

				webUrl.setViewerList(viewerList);
				webUrl.setEditorList(editorList);
				webUrl.setApproverList(approverList);

			}
		}
		return webUrl;
	}

	private static XMLParser xmlParser =null;
	public static XMLParser getInstance(){
		if(xmlParser == null){
			xmlParser = new XMLParser();
		}
		return xmlParser;
	}
	public class WebUrl{
		private String name;
		private List<String> viewerList;
		private List<String> approverList;
		private List<String> editorList;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<String> getViewerList() {
			return viewerList;
		}
		public void setViewerList(List<String> viewerList) {
			this.viewerList = viewerList;
		}
		public List<String> getApproverList() {
			return approverList;
		}
		public void setApproverList(List<String> approverList) {
			this.approverList = approverList;
		}
		public List<String> getEditorList() {
			return editorList;
		}
		public void setEditorList(List<String> editorList) {
			this.editorList = editorList;
		}

	}
}
