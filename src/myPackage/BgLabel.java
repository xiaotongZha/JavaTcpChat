
package myPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;


//这个是带背景的标签，用做图片

public class BgLabel extends JLabel{
	Image image = null;
	public BgLabel(String txt,String fileName) {
		super(txt);
		//这边是获取图片资源
		try {
			URL imageUrl = ClientFrame.class.getResource(fileName);
			image = ImageIO.read(imageUrl);	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void paintComponent(Graphics g) {
		//这边是将图片画出来
		int width = this.getWidth();
		int height = this.getHeight();
		g.clearRect(0,0,width,height);
		g.drawImage(image, 0,0,width,height,null);
	}
}
