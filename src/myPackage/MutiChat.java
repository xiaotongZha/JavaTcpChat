package myPackage;

/**
 * 服务器
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class MutiChat {
	private static CopyOnWriteArrayList<Channel> all = new CopyOnWriteArrayList<Channel>();
	private static int serverPort;

	// 静态块读取外部配置文件
	static {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader("work.properties"));
			serverPort = Integer.parseInt(prop.getProperty("serverPort"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(serverPort);
		System.out.println("---服务端---");
		while (true) {
			Socket client = server.accept();
			Channel c = new Channel(client);
			all.add(c);
			new Thread(c).start();
		}
	}

	// 使用内部类来封装
	static class Channel implements Runnable {
		private DataInputStream in;
		private DataOutputStream out;
		private Socket client;
		private boolean isRunning;
		private String name;

		public Channel(Socket client) {
			this.client = client;
			try {
				isRunning = true;
				in = new DataInputStream(client.getInputStream());
				out = new DataOutputStream(client.getOutputStream());
				this.name = receive();
				System.out.println(this.name + "已连接");
				send(this.name + "已加入群聊");
				sendOthers(this.name + "已加入群聊");
			} catch (IOException e) {
				release();
			}
		}

		// 接收消息
		public String receive() {
			String msg = "";
			try {
				msg = in.readUTF();
			} catch (IOException e) {
				System.out.println(this.name + "已断开连接");
				sendOthers(this.name + "已退出群聊");
				release();
			}
			return msg;
		}

		// 发送消息
		public void send(String msg) {
			try {
				if (!msg.equals("")) {
					out.writeUTF(msg);
					out.flush();
				}
			} catch (IOException e) {
				release();
			}
		}

		public void sendOthers(String msg) {
			for (Channel c : all) {
				if (c == this)
					continue;
				if (!msg.equals(""))
					c.send(this.name + ":" + msg);
			}
		}

		// 释放资源
		public void release() {
			for (int i = all.size() - 1; i >= 0; i--) {
				if (all.get(i).client.isClosed()) {
					all.remove(i);
				}
			}
			isRunning = false;
			Utils.close(in, out, client);
		}

		@Override
		public void run() {
			while (isRunning) {
				String msg = receive();
				if (!msg.equals(""))
					System.out.println(this.name + ":" + msg);
				sendOthers(msg);
			}
		}
	}
}
