package myPackage;

/**
 * 客户端
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
		logOn.setTitle("登录");
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

		// 设置绝对布局
		panel.setLayout(null);

		// 写登录按钮
		JButton land = new JButton("登录");
		land.setBounds(250, 340, 120, 60);
		land.setContentAreaFilled(false);
		land.setFont(new Font("黑体", Font.BOLD, 22));
		logOn.getContentPane().add(land);
		land.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 网络端
				Socket client = null;
				String name = txt.getText();
				if (!name.equals("")) {
					try {
						client = new Socket(serverIP, clientPort);
					} catch (UnknownHostException e1) {
						JOptionPane.showMessageDialog(logOn, "与服务器连接失败", "错误 ", 0);
						e1.printStackTrace();
						Utils.close(client);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(logOn, "与服务器连接失败", "错误 ", 0);
						e1.printStackTrace();
						Utils.close(client);
					}
					if (client.isConnected()) {
						logOn.setVisible(false);
						ClientFrame frame = new ClientFrame(client, name);
						frame.setVisible(true);
					}
				} else {
					JOptionPane.showMessageDialog(logOn, "请输入用户名", "错误 ", 0);
				}
				//
			}
		});

		// 写制作人名单
		JButton staff = new JButton("staff");
		staff.setBounds(0, 0, 60, 40);
		staff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				String staff = new String("UI设计：23届 路言翔" + '\n' + "网络编程：22届 彭羽轩" + '\n' + "策划与宣传：23届 金馨彤" + '\n'
						+ "后期指导：代正军老师" + '\n' + "特别感谢：21届 张驰学长");
				JOptionPane.showMessageDialog(logOn, staff, "致谢", JOptionPane.PLAIN_MESSAGE);
			}

		});
		logOn.getContentPane().add(staff);

		// 文本域获取用户名
		txt = new JTextField();
		txt.setBounds(300, 160, 250, 80);
		txt.setFont(new Font("黑体", Font.BOLD, 40));
		txt.setForeground(Color.WHITE);
		txt.setOpaque(false);
		logOn.getContentPane().add(txt);

		// 标签
		JLabel label = new JLabel("用户名：");
		label.setBounds(100, 160, 200, 100);
		label.setFont(new Font("黑体", Font.BOLD, 40));
		label.setForeground(Color.WHITE);
		logOn.getContentPane().add(label);
		
		//彩蛋
		JButton bonusScene = new JButton("...");
		bonusScene.setBounds(580, 0, 60, 40);
		bonusScene.setFont(new Font("黑体", Font.BOLD, 20));
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
