package com.andmail.kernel;

import com.andmail.api.MailReader;
import com.andmail.api.model.MailReaderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class AmMailReader implements MailReader {
	
	static{
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
	}

	protected static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	protected enum Protocol{
		DEFAULT("imap"),IMAP("imap"),POP3("pop3");
		public String value;
		private Protocol(String value) {
			this.value = value;
		}
	}
	
	protected MailReaderModel mMailFolder = null;
	protected Properties mProperties = new Properties();   
	
	public AmMailReader(MailReaderModel folder) {
		mMailFolder = folder;
		setProtocol(Protocol.DEFAULT);
	}
	
	protected void setProtocol(Protocol protocol) {
		mProperties.setProperty("mail.store.protocol",protocol.value);   
	}

	protected void setUseSSLFactory() {
		mProperties.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);        
		mProperties.setProperty("mail.imap.port", "993");
//		mProperties.setProperty("mail.imap.socketFactory.port", "993");     
//		mProperties.setProperty("mail.imap.socketFactory.fallback", "false");        
	}

	protected Store connect() throws Exception{
		Store store = Session.getDefaultInstance(mProperties).getStore();
		store.connect(mMailFolder.getHost(), mMailFolder.getUsername(), mMailFolder.getPassword());
		return store;
	}
	
	protected Folder getFolder(Store store) throws Exception{
		return getFolder(store, mMailFolder.getFolder());
	}
	
	protected Folder getFolder(Store store,String foldername) throws Exception{
		Folder folder = store.getFolder(foldername);
		folder.open(Folder.READ_ONLY);
		return folder;
	}
	

	protected List<Part> getHtmlPart(Message message) throws Exception {
		List<Part> ltParts = new ArrayList<Part>();
		Object o = message.getContent();
		if(o instanceof Multipart) {
			getHtmlPartMultipart(Multipart.class.cast(o),ltParts);
		} else if (o instanceof Part){
			getHtmlPartPart(Part.class.cast(o),ltParts);
		}
		return ltParts;
	}

	protected void getHtmlPartMultipart(Multipart multipart, List<Part> ltParts) throws Exception {
	    for (int j = 0, n = multipart.getCount(); j < n; j++) {
	        Part part = multipart.getBodyPart(j);
	        if (part.getContent() instanceof Multipart) {
	            Multipart mp = (Multipart) part.getContent();// 转成小包裹
	            getHtmlPartMultipart(mp,ltParts); //递归迭代
	        } else {
	        	getHtmlPartPart(part,ltParts);
	        }
	     }
	}

	protected void getHtmlPartPart(Part part, List<Part> ltParts) throws Exception {
		String contenttype = part.getContentType().toLowerCase(Locale.ENGLISH);
		if (contenttype.contains("text/html")) {
			ltParts.add(part);
		}
	}
}
