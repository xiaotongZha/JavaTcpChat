package myPackage;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

//这个是用作气泡的标签

public class qipaoLabel extends JLabel{
	
	//气泡的两种颜色
	public final static Color MY_BLUE = new Color(0,160,230);
	public final static Color MY_WHITE = new Color(250,250,250);
	
	private Color penColor;
	
	public qipaoLabel(Color color) {
		super();
		this.penColor = color;
	}
	
	protected void paintComponent(Graphics g) {
			//这边是将图片画出来
			int width = this.getWidth();
			int height = this.getHeight();
			g.clearRect(0,0,width,height);//先清空
			
			g.setColor(new Color(210,220,240));//涂上面板的颜色
			g.fillRect(0, 0, width, height);
			
			g.setColor(penColor);//画圆角矩形
			g.fillRoundRect(0, 0, width, height,15,15);
		}
}
