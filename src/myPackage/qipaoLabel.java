package myPackage;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

//������������ݵı�ǩ

public class qipaoLabel extends JLabel{
	
	//���ݵ�������ɫ
	public final static Color MY_BLUE = new Color(0,160,230);
	public final static Color MY_WHITE = new Color(250,250,250);
	
	private Color penColor;
	
	public qipaoLabel(Color color) {
		super();
		this.penColor = color;
	}
	
	protected void paintComponent(Graphics g) {
			//����ǽ�ͼƬ������
			int width = this.getWidth();
			int height = this.getHeight();
			g.clearRect(0,0,width,height);//�����
			
			g.setColor(new Color(210,220,240));//Ϳ��������ɫ
			g.fillRect(0, 0, width, height);
			
			g.setColor(penColor);//��Բ�Ǿ���
			g.fillRoundRect(0, 0, width, height,15,15);
		}
}
