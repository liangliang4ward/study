package cn.pwntcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test1 {
	public static void main(String[] args) throws IOException {
		BufferedImage img = ImageIO.read(new File("img3\\" + 13 + ".jpg"));
		int width = img.getWidth();
		int height = img.getHeight();
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				Color c = new Color(img.getRGB(i, j));
				System.out.print(Color.WHITE==c);
			}
			System.out.println();
			
		}
		
	}
}
