package myPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//sendBtn那边有两个图片的路径可能要修改，在124行附近
//有两个方法，sendMessage(在发送按钮的监听事件里调用) ， receiveMessage(应该是你要调用的)
//如果发送之后窗口不刷新，就试试 scrollPane.validate();  或者  messagePanel.repaint();
//尺寸计算不精确，不要发太长的消息。消息栏的ScrollPanel我就偷工减料省掉了
//其实我还打算写个User类，里面有用户的头像、昵称，不过有点麻烦，就算了吧

public class ClientFrame extends JFrame {

	private static JPanel panel1;
	private static JPanel panel2;
	private static JPanel panel23;
	private static JPanel panel3;

	private static JLabel touXiang;
	private static JLabel niCheng;

	private static MessagePanel messagePanel;
	private static JScrollPane scrollPane;
	private static JScrollBar vertical;

	private static JButton sendBtn;
	private static JTextArea txtArea;

	//
	private Socket client;
	private DataOutputStream out;
	private boolean isRunning;
	private String name;

	public MessagePanel getMessagePanel() {
		return this.messagePanel;
	}

	public ClientFrame(Socket client, String name) {

		this.name = name;
		this.client = client;
		try {
			isRunning = true;
			out = new DataOutputStream(client.getOutputStream());
			send(name);
		} catch (IOException e2) {
			Utils.close(out, client);
		}

		// 设置窗口属性
		setTitle("聊天窗口");
		// 获取图片资源，这是窗口的图标
		try {
			setIconImage(ImageIO.read(ClientFrame.class.getResource("/myImage/icon.jpg")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setSize(500, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 最低层的面板
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());

		// 最上面的面板，包括头像和昵称
		panel1 = new JPanel();
		panel1.setBackground(new Color(20, 155, 245));// 蓝色
		panel1.setPreferredSize(new Dimension(0, 40));
		panel1.setLayout(null);

		// 中间的面板，包括消息面版（ScrollPane,messagePanel）和一条分割线（panel23),分割线里有一个发送按钮
		panel2 = new JPanel();
		panel2.setBackground(new Color(210, 220, 240));// 灰色
		panel2.setLayout(new BorderLayout());

		// 输入栏与消息栏之间的分割线
		panel23 = new JPanel();
		panel23.setBackground(Color.LIGHT_GRAY);
		panel23.setPreferredSize(new Dimension(0, 30));
		panel23.setLayout(new BorderLayout());
		panel2.add(panel23, BorderLayout.SOUTH);

		// 下面的面板，包括输入栏(txtArea)
		panel3 = new JPanel();
		panel3.setBackground(Color.WHITE);
		panel3.setPreferredSize(new Dimension(0, 80));
		panel3.setLayout(null);

		root.add(panel1, BorderLayout.NORTH);
		root.add(panel2, BorderLayout.CENTER);
		root.add(panel3, BorderLayout.SOUTH);

		add(root);

		// 头像
		touXiang = new BgLabel(null, "/myImage/touxiang.jpg");
		touXiang.setBounds(10, 0, 40, 40);
		panel1.add(touXiang);

		// 昵称
		niCheng = new JLabel(name);
		niCheng.setBounds(55, 5, 40, 30);
		niCheng.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		panel1.add(niCheng);

		// 消息面板
		messagePanel = new MessagePanel();
		messagePanel.setLayout(null);
		messagePanel.setBackground(new Color(210, 220, 240));
		messagePanel.setPreferredSize(new Dimension(500, 300));

		// 带滚动条的面板,用来装messagePanel
		scrollPane = new JScrollPane(messagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 竖直方向的滚动条始终显示，水平的始终不显示
		panel2.add(scrollPane, BorderLayout.CENTER);
		vertical = scrollPane.getVerticalScrollBar();// 获取竖直方向上的额动条，用来控制滚动条的位置

		// 发送按钮
		sendBtn = new JButton();
		sendBtn.setPreferredSize(new Dimension(60, 30));
		sendBtn.setBorderPainted(false);// 隐藏边框
		
		URL url1=ClientFrame.class.getResource("/myImage/buttonbg.jpg");
		URL url2=ClientFrame.class.getResource("/myImage/pressedbuttonbg.jpg");
		
		sendBtn.setIcon(new ImageIcon(url1));// 这个是按钮的图标，路径可能需要修改
		sendBtn.setPressedIcon(new ImageIcon(url2));// 这个是按钮被点击时的图标，也要修改
		panel23.add(sendBtn, BorderLayout.EAST);

		// 输入栏
		txtArea = new JTextArea();
		txtArea.setBounds(0, 0, 490, 100);
		txtArea.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		txtArea.setLineWrap(true);// 自动换行
		panel3.add(txtArea);

		// 按钮的监听事件——将信息发送出去
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = txtArea.getText();
				txtArea.setText(null);// 发送之后发将输入栏清空
				if (txt.isEmpty() == true) {
					// 如果没输入，就不发送出去
				} else {
					// 调用sendMessage方法
					send(txt);
					sendMessage(txt, messagePanel);
				}
			}
		});

		
		//lambda实现收线程
		new Thread(() -> {
			DataInputStream in = null;
			try {
				in = new DataInputStream(client.getInputStream());
				isRunning = true;
				String msg = "";
				while (isRunning) {
					msg = in.readUTF();
					if (!msg.equals("")) {
						receiveMessage(msg, messagePanel);
					}
				}
			} catch (IOException e1) {
				Utils.close(client);
			}
		}).start();

	}

	public void send(String msg) {
		try {
			if (!msg.equals("")) {
				out.writeUTF(msg);
				out.flush();
			}
		} catch (IOException e) {
			Utils.close(out, client);
		}
	}

	public static void sendMessage(String s, MessagePanel messagePanel) {
		// 这边先创建了一个JLbabel,用来获取字符串的显示长度
		JLabel label = new JLabel(s);
		label.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		Dimension size = label.getPreferredSize();
		// 然后计算出要显示多少行，尺寸为多少
		int hangshu = size.width / 400 + 1;
		int height = hangshu * 30;
		int width;
		if (hangshu == 1) {
			width = size.width;
		} else {
			width = 400;
		}

		// 每条消息都是放在一个JPanel上的，见图解2
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, messagePanel.getMessageHeight(), 500, height);
		panel.setBackground(null);
		messagePanel.setMessageHeight(messagePanel.getMessageHeight() + height);// messageHeight是用来确定每个JPanel的位置的，每发一条消息就增加

		// 用JTextArea显示消息的内容
		JTextArea txt = new JTextArea(s);
		txt.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		txt.setLineWrap(true);// 自动换行
		txt.setEditable(false);// 不可编辑
		txt.setBackground(qipaoLabel.MY_BLUE);// 使文本域的背景与气泡符合

		// 气泡
		JLabel bg = new qipaoLabel(qipaoLabel.MY_BLUE);

		// 设置位置
		txt.setBounds(450 - width, 5, width + 3, hangshu * 22);
		bg.setBounds(445 - width, 5, width + 13, height - 8);
		// 添加到面板
		panel.add(txt);
		panel.add(bg);
		messagePanel.add(panel);

		messagePanel.setPreferredSize(new Dimension(500, messagePanel.getMessageHeight()));// 修改messagePanel的大小，不然在ScrollPane显示不出来
		scrollPane.validate();// 刷新scrollPane
		vertical.setValue(vertical.getMaximum());// 将滚动条调到最大值，也就是发完消息移到最下方
		messagePanel.repaint();// 刷新messagePanel

	}

	// 这个和上面几乎是一模一样的，只是改了位置和颜色
	public static void receiveMessage(String s, MessagePanel messagePanel) {
		JLabel label = new JLabel(s);
		label.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		Dimension size = label.getPreferredSize();
		int hangshu = size.width / 400 + 1;
		int height = hangshu * 30;
		int width;
		if (hangshu == 1) {
			width = size.width;
		} else {
			width = 400;
		}

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, messagePanel.getMessageHeight(), 500, height);
		panel.setBackground(null);
		messagePanel.setMessageHeight(messagePanel.getMessageHeight() + height);

		JTextArea txt = new JTextArea(s);
		txt.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		txt.setLineWrap(true);
		txt.setEditable(false);
		txt.setBackground(qipaoLabel.MY_WHITE);

		JLabel bg = new qipaoLabel(qipaoLabel.MY_WHITE);

		txt.setBounds(10, 5, width + 3, hangshu * 22);
		bg.setBounds(5, 5, width + 13, height - 7);

		panel.add(txt);
		panel.add(bg);
		messagePanel.add(panel);

		messagePanel.setPreferredSize(new Dimension(500, messagePanel.getMessageHeight()));
		scrollPane.validate();
		vertical.setValue(vertical.getMaximum());
		messagePanel.repaint();
	}

}
