package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {

	private static final String[] TEST_EMAILS = { "qwertyuiop@xyz.com", "mnbqwe@troll.net", "qazxswedc@poiuy.org",
	"asd123@asdf.com.bd"};
	
	private static final String TEST_NAME = "Name";
	private static final String TEST_HEADER_VALUE = "Value";
	
	private static final String TEST_HOST_NAME = "127.0.0.1";
	
	//Concrete email instance for testing
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		
		email = new EmailConcrete();
		
		
	}
	
	@After
	public void tearDownEmailTest() throws Exception {
		
		
	}
	
	@Test
	public void testAddBcc() throws Exception {
		
		email.addBcc(TEST_EMAILS);
		
		assertEquals(4, email.getBccAddresses().size());
	}
	
	@Test
	public void test2AddBcc() throws Exception {
		
		email.addBcc(TEST_EMAILS[0]);
		
		assertEquals(1, email.getBccAddresses().size());
	}
	
	@Test(expected = EmailException.class)
	public void test3AddBcc() throws Exception {
		
		String[] empty = {};
		
		email.addBcc(empty);
	}
	
	@Test
	public void testAddCc() throws Exception {
		
		email.addCc(TEST_EMAILS);
		
		assertEquals(4, email.getCcAddresses().size());
	}
	
	@Test
	public void test2AddCc() throws Exception {
		
		email.addCc(TEST_EMAILS[0]);
		
		assertEquals(1, email.getCcAddresses().size());
	}
	
	@Test(expected = EmailException.class)
	public void test3AddCc() throws Exception {
		
		String[] empty = {};
		
		email.addCc(empty);
	}
	
	@Test
	public void testAddHeader() throws Exception {
		
		email.addHeader(TEST_NAME, TEST_HEADER_VALUE);
		
		assertEquals(TEST_HEADER_VALUE, email.headers.get(TEST_NAME));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test2AddHeader() throws Exception {
		
		email.addHeader("", TEST_HEADER_VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test3AddHeader() throws Exception {
		
		email.addHeader(TEST_NAME, "");
	}
	
	@Test
	public void testAddReplyTo() throws Exception {
		
		email.addReplyTo(TEST_EMAILS[0]);
		
		assertEquals(1, email.replyList.size());
	}
	
	@Test
	public void test2AddReplyTo() throws Exception {
		
		email.addReplyTo(TEST_EMAILS[0], TEST_NAME);
		
		assertEquals(1, email.replyList.size());
	}
	
	@Test
	public void testAddTo() throws Exception {
		
		email.addTo(TEST_EMAILS);
		
		assertEquals(4, email.toList.size());
	}
	
	@Test(expected = EmailException.class)
	public void test2AddTo() throws Exception {
		
		String[] empty = {};
		
		email.addTo(empty);
	}
	
	@Test
	public void test3AddTo() throws Exception {
		
		email.addTo(TEST_EMAILS[0]);
		
		assertEquals(1, email.toList.size());
	}
	
	@Test
	public void testBuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		Address[] strform = msg.getFrom();
		
		assertEquals(strform[0].toString(), "a@b.com");
	}
	
	@Test
	public void test2BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		email.addReplyTo(TEST_EMAILS[2]);
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		Address[] strform = msg.getReplyTo();
		
		assertEquals(strform[0].toString(), TEST_EMAILS[2]);
	}
	
	@Test
	public void test3BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		email.addBcc(TEST_EMAILS[0]);
		email.addCc(TEST_EMAILS[1]);
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		Address[] strform = msg.getRecipients(RecipientType.BCC);
		
		assertEquals(strform[0].toString(), TEST_EMAILS[0]);
	}
	
	@Test
	public void test4BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		email.addCc(TEST_EMAILS[1]);
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		Address[] strform = msg.getRecipients(RecipientType.CC);
		
		assertEquals(strform[0].toString(), TEST_EMAILS[1]);
	}
	
	@Test
	public void test5BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		MimeMultipart content = new MimeMultipart();
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setText("Brought to you by JDK 11.0.9!");
		content.addBodyPart(bodyPart);
		
		email.setContent(content);
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		MimeMultipart returnedContent = (MimeMultipart) msg.getContent();
		String text = (String) returnedContent.getBodyPart(0).getContent();
		
		assertEquals(text, "Brought to you by JDK 11.0.9!");
	}
	
	@Test
	public void test6BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		MimeBodyPart body = new MimeBodyPart();
		body.setText("Brought to you by JDK 11.0.9!");
		
		email.setContent(body, "text");
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		MimeBodyPart returnedContent = (MimeBodyPart) msg.getContent();
		String text = (String) returnedContent.getContent();
		
		System.out.println(text);
		
		assertEquals(text, "Brought to you by JDK 11.0.9!");
	}
	
	@Test(expected = IllegalStateException.class)
	public void test7BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		email.buildMimeMessage();
		email.buildMimeMessage();
	}
	
	@Test(expected = EmailException.class)
	public void test8BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.addTo("c@d.com");
		email.setSubject("test mail");
		
		email.buildMimeMessage();
	}
	
	@Test(expected = EmailException.class)
	public void test9BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.setSubject("test mail");
		
		email.buildMimeMessage();
	}
	
	@Test
	public void test10BuildMimeMessage() throws Exception {
		
		email.setHostName("localhost");
		email.setSmtpPort(8080);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		email.addHeader(TEST_NAME, TEST_HEADER_VALUE);
		
		email.buildMimeMessage();
		
		MimeMessage msg = email.getMimeMessage();
		String[] header = msg.getHeader(TEST_NAME);
		
		assertEquals(TEST_HEADER_VALUE, header[0]);
	}
	
	@Test
	public void testGetHostName() throws Exception {
		
		assertNull(email.getHostName());
	}
	
	@Test
	public void test2GetHostName() throws Exception {
		
		email.setHostName(TEST_HOST_NAME);
		
		assertEquals(TEST_HOST_NAME, email.getHostName());
	}
	
	@Test
	public void testGetMailSession() throws Exception {
		
		email.setHostName("localhost");
		
		Session session = email.getMailSession();
		
		assertNotNull(session);
	}
	
	@Test(expected = EmailException.class)
	public void test2GetMailSession() throws Exception {
		
		email.getMailSession();
	}
	
	@Test
	public void testGetSentDate() throws Exception {
		
		email.getSentDate();
		
		assertNull(email.sentDate);
	}
	
	@Test
	public void test2GetSentDate() throws Exception {
		
		Date date = Date.from(Instant.now());
		
		email.setSentDate(date);
		
		assertEquals(date, email.getSentDate());
	}
	
	@Test
	public void testGetSocketConnectionTimeout() throws Exception {
		
		assertEquals(EmailConstants.SOCKET_TIMEOUT_MS, email.getSocketConnectionTimeout());
	}
}
