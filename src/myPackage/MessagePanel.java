package myPackage;

import javax.swing.JPanel;

//�������ʵ��ֻ�Ƕ���һ��messageHeight���ԣ�����������Ϣ��λ�õ�

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
