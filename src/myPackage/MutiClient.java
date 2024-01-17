package myPackage;

/**
 * �ͻ���
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MutiClient {
	private JFrame logOn;
	private JPanel panel;
	private ImageIcon image;
	private JLabel bg;
	private JTextField txt;

	private String name;

	private static String serverIP;
	private static int clientPort;

	static {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader("work.properties"));
			clientPort = Integer.parseInt(prop.getProperty("clientPort"));
			serverIP = prop.getProperty("serverIP");
			System.out.println(serverIP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MutiClient() throws UnknownHostException, IOException {
		
		logOn = new JFrame();
		try {
			logOn.setIconImage(ImageIO.read(ClientFrame.class.getResource("/myImage/icon.jpg")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		logOn.setTitle("��¼");
		logOn.setBounds(700, 300, 646, 458);
		logOn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logOn.setVisible(true);
		logOn.setResizable(false);

		URL url = MutiClient.class.getResource("/myImage/nsfz.jpg");
		ImageIcon image = new ImageIcon(url);
		JLabel bg = new JLabel(image);
		bg.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		logOn.getLayeredPane().add(bg, new Integer(Integer.MIN_VALUE));
		panel = (JPanel) logOn.getContentPane();
		panel.setOpaque(false);

		// ���þ��Բ���
		panel.setLayout(null);

		// д��¼��ť
		JButton land = new JButton("��¼");
		land.setBounds(250, 340, 120, 60);
		land.setContentAreaFilled(false);
		land.setFont(new Font("����", Font.BOLD, 22));
		logOn.getContentPane().add(land);
		land.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// �����
				Socket client = null;
				String name = txt.getText();
				if (!name.equals("")) {
					try {
						client = new Socket(serverIP, clientPort);
					} catch (UnknownHostException e1) {
						JOptionPane.showMessageDialog(logOn, "�����������ʧ��", "���� ", 0);
						e1.printStackTrace();
						Utils.close(client);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(logOn, "�����������ʧ��", "���� ", 0);
						e1.printStackTrace();
						Utils.close(client);
					}
					if (client.isConnected()) {
						logOn.setVisible(false);
						ClientFrame frame = new ClientFrame(client, name);
						frame.setVisible(true);
					}
				} else {
					JOptionPane.showMessageDialog(logOn, "�������û���", "���� ", 0);
				}
				//
			}
		});

		// д����������
		JButton staff = new JButton("staff");
		staff.setBounds(0, 0, 60, 40);
		staff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				String staff = new String("UI��ƣ�23�� ·����" + '\n' + "�����̣�22�� ������" + '\n' + "�߻���������23�� ��ܰͮ" + '\n'
						+ "����ָ������������ʦ" + '\n' + "�ر��л��21�� �ų�ѧ��");
				JOptionPane.showMessageDialog(logOn, staff, "��л", JOptionPane.PLAIN_MESSAGE);
			}

		});
		logOn.getContentPane().add(staff);

		// �ı����ȡ�û���
		txt = new JTextField();
		txt.setBounds(300, 160, 250, 80);
		txt.setFont(new Font("����", Font.BOLD, 40));
		txt.setForeground(Color.WHITE);
		txt.setOpaque(false);
		logOn.getContentPane().add(txt);

		// ��ǩ
		JLabel label = new JLabel("�û�����");
		label.setBounds(100, 160, 200, 100);
		label.setFont(new Font("����", Font.BOLD, 40));
		label.setForeground(Color.WHITE);
		logOn.getContentPane().add(label);
		
		//�ʵ�
		JButton bonusScene = new JButton("...");
		bonusScene.setBounds(580, 0, 60, 40);
		bonusScene.setFont(new Font("����", Font.BOLD, 20));
		bonusScene.setContentAreaFilled(false);
		bonusScene.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Cardioid();
			}
		});
		logOn.getContentPane().add(bonusScene);
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new MutiClient();
	}
}
