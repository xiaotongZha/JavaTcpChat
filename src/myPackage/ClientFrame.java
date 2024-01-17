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

//sendBtn�Ǳ�������ͼƬ��·������Ҫ�޸ģ���124�и���
//������������sendMessage(�ڷ��Ͱ�ť�ļ����¼������) �� receiveMessage(Ӧ������Ҫ���õ�)
//�������֮�󴰿ڲ�ˢ�£������� scrollPane.validate();  ����  messagePanel.repaint();
//�ߴ���㲻��ȷ����Ҫ��̫������Ϣ����Ϣ����ScrollPanel�Ҿ�͵������ʡ����
//��ʵ�һ�����д��User�࣬�������û���ͷ���ǳƣ������е��鷳�������˰�

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

		// ���ô�������
		setTitle("���촰��");
		// ��ȡͼƬ��Դ�����Ǵ��ڵ�ͼ��
		try {
			setIconImage(ImageIO.read(ClientFrame.class.getResource("/myImage/icon.jpg")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setSize(500, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ��Ͳ�����
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());

		// ���������壬����ͷ����ǳ�
		panel1 = new JPanel();
		panel1.setBackground(new Color(20, 155, 245));// ��ɫ
		panel1.setPreferredSize(new Dimension(0, 40));
		panel1.setLayout(null);

		// �м����壬������Ϣ��棨ScrollPane,messagePanel����һ���ָ��ߣ�panel23),�ָ�������һ�����Ͱ�ť
		panel2 = new JPanel();
		panel2.setBackground(new Color(210, 220, 240));// ��ɫ
		panel2.setLayout(new BorderLayout());

		// ����������Ϣ��֮��ķָ���
		panel23 = new JPanel();
		panel23.setBackground(Color.LIGHT_GRAY);
		panel23.setPreferredSize(new Dimension(0, 30));
		panel23.setLayout(new BorderLayout());
		panel2.add(panel23, BorderLayout.SOUTH);

		// �������壬����������(txtArea)
		panel3 = new JPanel();
		panel3.setBackground(Color.WHITE);
		panel3.setPreferredSize(new Dimension(0, 80));
		panel3.setLayout(null);

		root.add(panel1, BorderLayout.NORTH);
		root.add(panel2, BorderLayout.CENTER);
		root.add(panel3, BorderLayout.SOUTH);

		add(root);

		// ͷ��
		touXiang = new BgLabel(null, "/myImage/touxiang.jpg");
		touXiang.setBounds(10, 0, 40, 40);
		panel1.add(touXiang);

		// �ǳ�
		niCheng = new JLabel(name);
		niCheng.setBounds(55, 5, 40, 30);
		niCheng.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		panel1.add(niCheng);

		// ��Ϣ���
		messagePanel = new MessagePanel();
		messagePanel.setLayout(null);
		messagePanel.setBackground(new Color(210, 220, 240));
		messagePanel.setPreferredSize(new Dimension(500, 300));

		// �������������,����װmessagePanel
		scrollPane = new JScrollPane(messagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// ��ֱ����Ĺ�����ʼ����ʾ��ˮƽ��ʼ�ղ���ʾ
		panel2.add(scrollPane, BorderLayout.CENTER);
		vertical = scrollPane.getVerticalScrollBar();// ��ȡ��ֱ�����ϵĶ�����������ƹ�������λ��

		// ���Ͱ�ť
		sendBtn = new JButton();
		sendBtn.setPreferredSize(new Dimension(60, 30));
		sendBtn.setBorderPainted(false);// ���ر߿�
		
		URL url1=ClientFrame.class.getResource("/myImage/buttonbg.jpg");
		URL url2=ClientFrame.class.getResource("/myImage/pressedbuttonbg.jpg");
		
		sendBtn.setIcon(new ImageIcon(url1));// ����ǰ�ť��ͼ�꣬·��������Ҫ�޸�
		sendBtn.setPressedIcon(new ImageIcon(url2));// ����ǰ�ť�����ʱ��ͼ�꣬ҲҪ�޸�
		panel23.add(sendBtn, BorderLayout.EAST);

		// ������
		txtArea = new JTextArea();
		txtArea.setBounds(0, 0, 490, 100);
		txtArea.setFont(new Font("΢���ź�", Font.PLAIN, 17));
		txtArea.setLineWrap(true);// �Զ�����
		panel3.add(txtArea);

		// ��ť�ļ����¼���������Ϣ���ͳ�ȥ
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = txtArea.getText();
				txtArea.setText(null);// ����֮�󷢽����������
				if (txt.isEmpty() == true) {
					// ���û���룬�Ͳ����ͳ�ȥ
				} else {
					// ����sendMessage����
					send(txt);
					sendMessage(txt, messagePanel);
				}
			}
		});

		
		//lambdaʵ�����߳�
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
		// ����ȴ�����һ��JLbabel,������ȡ�ַ�������ʾ����
		JLabel label = new JLabel(s);
		label.setFont(new Font("΢���ź�", Font.PLAIN, 17));
		Dimension size = label.getPreferredSize();
		// Ȼ������Ҫ��ʾ�����У��ߴ�Ϊ����
		int hangshu = size.width / 400 + 1;
		int height = hangshu * 30;
		int width;
		if (hangshu == 1) {
			width = size.width;
		} else {
			width = 400;
		}

		// ÿ����Ϣ���Ƿ���һ��JPanel�ϵģ���ͼ��2
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, messagePanel.getMessageHeight(), 500, height);
		panel.setBackground(null);
		messagePanel.setMessageHeight(messagePanel.getMessageHeight() + height);// messageHeight������ȷ��ÿ��JPanel��λ�õģ�ÿ��һ����Ϣ������

		// ��JTextArea��ʾ��Ϣ������
		JTextArea txt = new JTextArea(s);
		txt.setFont(new Font("΢���ź�", Font.PLAIN, 17));
		txt.setLineWrap(true);// �Զ�����
		txt.setEditable(false);// ���ɱ༭
		txt.setBackground(qipaoLabel.MY_BLUE);// ʹ�ı���ı��������ݷ���

		// ����
		JLabel bg = new qipaoLabel(qipaoLabel.MY_BLUE);

		// ����λ��
		txt.setBounds(450 - width, 5, width + 3, hangshu * 22);
		bg.setBounds(445 - width, 5, width + 13, height - 8);
		// ��ӵ����
		panel.add(txt);
		panel.add(bg);
		messagePanel.add(panel);

		messagePanel.setPreferredSize(new Dimension(500, messagePanel.getMessageHeight()));// �޸�messagePanel�Ĵ�С����Ȼ��ScrollPane��ʾ������
		scrollPane.validate();// ˢ��scrollPane
		vertical.setValue(vertical.getMaximum());// ���������������ֵ��Ҳ���Ƿ�����Ϣ�Ƶ����·�
		messagePanel.repaint();// ˢ��messagePanel

	}

	// ��������漸����һģһ���ģ�ֻ�Ǹ���λ�ú���ɫ
	public static void receiveMessage(String s, MessagePanel messagePanel) {
		JLabel label = new JLabel(s);
		label.setFont(new Font("΢���ź�", Font.PLAIN, 17));
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
		txt.setFont(new Font("΢���ź�", Font.PLAIN, 17));
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
