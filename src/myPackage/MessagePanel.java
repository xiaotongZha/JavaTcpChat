package myPackage;

import javax.swing.JPanel;

//这个类其实就只是多了一个messageHeight属性，用来设置消息的位置的

public class MessagePanel extends JPanel{

	private int messageHeight;
	
	public int getMessageHeight() {
		return messageHeight;
	}
	public void setMessageHeight(int messageHeight) {
		this.messageHeight = messageHeight;
	}

	
	public MessagePanel() {
		super();
	}
}
