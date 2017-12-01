package com.afn.cryptobase.core;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.afn.realstat.AppFiles;

public class NumberPatternTest {

	@Test
	public void testNumberPattern() {
		
		int size = 1000000;
		ArrayList<Long> sequence = new ArrayList<Long>();
		for (int i=0; i<size; i++) {
			sequence.add(Math.round(Math.random() * 1023.0));
		}
		
		NumberPattern np = new NumberPattern(sequence);
		BufferedImage im = np.drawPattern();

		String fileName = "NumberPattern.png";
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		try {
			ImageIO.write(im, "png", file);
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void testNumberPatternFromFile() {
	
		String fileName = "netCount.txt";
		File file = new File( AppFiles.getTestDataDir() + "\\" + fileName);
		
		NumberPattern np = new NumberPattern(file);
		BufferedImage im = np.drawPattern();

		fileName = "NumberPattern.png";
		file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		try {
			ImageIO.write(im, "png", file);
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void testGetBitStats() {
	
		String fileName = "netCount.txt";
		File file = new File( AppFiles.getTestDataDir() + "\\" + fileName);
		
		NumberPattern np = new NumberPattern(file);

		fileName = "BitStats.txt";
		file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		
		np.printBitStats(np.computeBitStats(),file);
		
		
	}
 
	@Test
	public void testGetBit() {
		Long l = 1L;
		assertTrue( NumberPattern.getBit(0,l));
		assertFalse( NumberPattern.getBit(63, l));
		
		l = 8L;
		assertFalse( NumberPattern.getBit(0, l));
		assertFalse( NumberPattern.getBit(1, l));
		assertFalse( NumberPattern.getBit(2, l));
		assertTrue( NumberPattern.getBit(3, l));
		
		l = 15L;
		assertTrue( NumberPattern.getBit(0, l));
		assertTrue( NumberPattern.getBit(1, l));
		assertTrue( NumberPattern.getBit(2, l));
		assertTrue( NumberPattern.getBit(3, l));
		assertFalse( NumberPattern.getBit(4, l));
	}
  
}
