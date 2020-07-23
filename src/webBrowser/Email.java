package webBrowser;



import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Email extends JFrame {


	private static final long serialVersionUID = 1L;
	private JButton Send, Exit;
	private JLabel Username, Password, Server, ToAddr, Title, Content;
	private JTextField Username_Field, Pwd_Field, Server_Field, ToAddr_Field, Title_Field;
	private JTextArea Content_Field;
	JFrame frame = new JFrame();

	public Email() {
		frame.setTitle("发送邮件");
		init();
		Server_Field.setEditable(false);
		frame.setLayout(null);
		frame.add(Username);
		frame.add(Username_Field);
		frame.add(Password);
		frame.add(Pwd_Field);
		frame.add(Server);
		frame.add(Server_Field);
		frame.add(ToAddr);
		frame.add(ToAddr_Field);
		frame.add(Title);
		frame.add(Title_Field);
		frame.add(Content);
		frame.add(Content_Field);
		frame.add(Send);
		frame.add(Exit);
		frame.setSize(420, 460);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		sendMail();

	}

	private void init() {
		Username = new JLabel("发件人邮箱：");
		Password = new JLabel("邮箱密码：");
		Server = new JLabel("服务器：");
		ToAddr = new JLabel("收件人邮箱：");
		Title = new JLabel("邮件标题：");
		Content = new JLabel("邮件内容：");
		Send = new JButton("发送");
		Exit = new JButton("退出");
		Username_Field = new JTextField();
		Pwd_Field = new JTextField();
		Server_Field = new JTextField();
		ToAddr_Field = new JTextField();
		Title_Field = new JTextField();
		Content_Field = new JTextArea();
		Username_Field.setText("1085615809@qq.com");
		Pwd_Field.setText("xugekrkbudmkhfcb");

		Username.setBounds(10, 20, 100, 30);
		Username_Field.setBounds(90, 20, 300, 30);

		Password.setBounds(10, 60, 100, 30);
		Pwd_Field.setBounds(90, 60, 300, 30);

		Server.setBounds(10, 100, 100, 30);
		Server_Field.setBounds(90, 100, 300, 30);

		ToAddr.setBounds(10, 140, 100, 30);
		ToAddr_Field.setBounds(90, 140, 300, 30);

		Title.setBounds(10, 180, 100, 30);
		Title_Field.setBounds(90, 180, 300, 30);

		Content.setBounds(10, 220, 100, 130);
		Content_Field.setBounds(90, 220, 300, 130);

		Send.setBounds(120, 380, 80, 30);
		Exit.setBounds(250, 380, 80, 30);
	}

	private void sendMail() {
		Username_Field.addFocusListener(new FocusListener() {
			String regex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!Username_Field.getText().trim().matches(regex)) {
					JOptionPane.showMessageDialog(Username_Field, "请正确填写发件人邮箱地址！", "邮箱格式错误", JOptionPane.NO_OPTION);
					Server_Field.setText("");
					Server_Field.setEditable(false);
				} else {
					String s = Username_Field.getText().trim().split("@")[1];
					Server_Field.setText("smtp." + s.substring(0, s.lastIndexOf(".")) + ".com");
					Server_Field.setEditable(false);
				}
			}

		});
		Send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String Username = Username_Field.getText().trim();
				String Password = Pwd_Field.getText().trim();
				String Server = Server_Field.getText().trim();
				String ToAddr = ToAddr_Field.getText().trim();
				String Subject = Title_Field.getText().trim();
				String Content = Content_Field.getText().trim();

				String resilt = "";
				String[] arr = { Username, Password, ToAddr, Subject, Content };
				String[] arr2 = { "发件人邮箱", "邮箱密码", "收件人邮箱", "邮件标题", "邮件内容" };
				for (int i = 0; i < arr.length; i++) {
					if (arr[i] == null || arr[i] == "" || arr[i].length() == 0) {
						resilt += arr2[i] + "\n";
					}
				}

				if (resilt.length() == 0) {
					Mail mail = new Mail();
					mail.setHost(Server);
					mail.setPort(25);
					mail.setUsername(Username);
					mail.setPassword(Password);
					mail.setFromAddress(Username);
					mail.setToAddress(ToAddr);
					mail.setSubject(Subject);
					mail.setContent(Content);
					mail.setContentType("text/html;charset=utf-8");

					boolean IsSent = demo.Mail_Sender(mail);
					if (IsSent) {
						JOptionPane.showMessageDialog(null, "邮件发送成功\n" + ToAddr_Field.getText() + " 请注意查收");
					} else {
						JOptionPane.showMessageDialog(null, "邮件发送失败");
					}
				} else {
					JOptionPane.showMessageDialog(null, "以下字段不能为空：\n" + resilt);
					return;
				}
			}
		});
		
		Exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

	}
	
	public static void main(String[] args) {
		 
		new Email();
	}


}

class Mail {
	String host;
	// 邮箱主机
	int port;
	// 主机端口
	String username;
	// 发送者的账号
	String password;
	// 发送者的密码
	String fromAddress;
	// 发送者的邮箱地址
	String toAddress;
	// 接受者的邮箱地址
	String subject;
	// 设置邮件主题
	String content;
	// 设置邮件内容
	String contentType;

	// 设置邮件类型

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}

class demo {
	public static boolean Mail_Sender(Mail mail) {

		try {
			/** 配置文件 */
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", mail.getHost());
			props.put("mail.smtp.port", 25);
			props.put("mail.smtp.starttls.enable", "true");
			/** 使用STARTTLS安全连接 */

			/** 创建会话 */
			Verify verify = new Verify(mail.getUsername(), mail.getPassword());
			Session mailSession = Session.getInstance(props, verify);
			mailSession.setDebug(true);
			/** 创建信息对象 */
			Message message = new MimeMessage(mailSession);
			InternetAddress from = new InternetAddress(mail.getFromAddress());
			InternetAddress to = new InternetAddress(mail.getToAddress());
			message.setFrom(from); /** 设置邮件信息的来源 */
			message.setRecipient(MimeMessage.RecipientType.TO, to); /** 设置邮件的接收者 */
			message.setSubject(mail.getSubject());
			message.setSentDate(new Date()); /** 设置邮件发送日期 */
			message.setContent(mail.getContent(), mail.getContentType()); /** 设置邮件内容 */
			message.saveChanges();

			/** 发送邮件 */
			Transport trans = mailSession.getTransport("smtp");
			trans.connect(mail.getHost(), mail.getUsername(), mail.getPassword());
			trans.sendMessage(message, message.getAllRecipients());

			return true;

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

	}
}

class Verify extends Authenticator {
	String username = "";
	String password = "";

	public Verify(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
		return new PasswordAuthentication(username, password);
	}
}