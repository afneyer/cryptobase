package com.afn.realstat.sandbox;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.afn.realstat.AppFiles;

public class ImagingTest {

	@Test
    public void testWriteTextOverImage() {
		String iconDirName = AppFiles.getIconDir();
		String imageFile = iconDirName + "\\BaseCircle.png";
		
		int i = 22;
        String text = Integer.toString(i);
        
        int x = 5;
        int y = 32 - 9;
        
        byte[] bytes = null;
		try {
			bytes = textOverImage(imageFile, text, new Point(x,y));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		String testOutputDir = AppFiles.getTestOutputDir();
		String testOutputFile = testOutputDir += "\\testWriteTextOverImage.png";
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(testOutputFile);
			 fos.write(bytes);
		        fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
       
    }

    public static byte[] textOverImage(String imageFilePath,
            String text, Point textPosition) throws IOException {
    	File imageFile = new File(imageFilePath);
        BufferedImage im = ImageIO.read(imageFile);
        Graphics2D g2 = im.createGraphics();
        g2.scale(0.6666, 0);
        g2.setColor(Color.white);
        Font font = new Font(null,Font.BOLD,20);
        g2.setFont(font);
        g2.drawString(text, textPosition.x, textPosition.y);
        
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(im, "png", baos);
        return baos.toByteArray();
    }
}