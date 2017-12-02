package com.afn.cryptobase.core;

import static org.junit.Assert.fail;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.primes.Primes;
import org.hsqldb.NumberSequence;

public class NumberPattern {

	private List<Long> sequence = null;
	int width = 1024;
	int height = 1024;

	public NumberPattern(List<Long> numberSequence) {
		this.sequence = numberSequence;
	}

	public NumberPattern(File file) {
		readSequenceFromFile(file);
	}

	public void readSequenceFromFile(File file) {

		sequence = new ArrayList<Long>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot read file " + file.getName());
		}

		try {
			String line = br.readLine();

			int i = 0;
			while (line != null) {
				Long l = new Long(line);
				sequence.add(i, new Long(l));
				line = br.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException("Cannot read line from file " + file.getName());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public BufferedImage drawPattern() {

		double scaleFactor = 0.5;

		List<Long> seq = getScaledSequence(scaleFactor);

		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// initialize image
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				im.setRGB(i, j, new Color(255, 255, 255).getRGB());
			}
		}

		for (int i = 0; i < seq.size() - 1; i += 2) {
			int x = seq.get(i).intValue();
			int y = seq.get(i + 1).intValue();
			addPoint(im, x, y);
		}

		Graphics2D g = im.createGraphics();
		Font font = new Font(null, Font.BOLD, 14);
		g.setFont(font);
		g.setColor(Color.GREEN);
		String info = "Pattern Size " + scaleFactor * width + " x " + scaleFactor * height;
		g.drawString(info, 700, 200);

		BasicStroke line = new BasicStroke(1.0f);
		g.setStroke(line);
		g.drawRect(0, 0, 1023, 1023);
		g.dispose();

		return im;

	}

	public HashMap<Long, Long> computeNormalizedNonceDistribution() {

		Long factor = Math.round(Math.pow(2.0, 10.0));

		// compute distribution (key=nonce, value=count)
		HashMap<Long, Long> dist = new HashMap<Long, Long>();
		Long maxCount = 0L;
		for (Long ele : sequence) {

			Long key = ele;
			while (key > factor) {
				key = key / factor;
			}
			;
			Long count = dist.get(key);
			if (count == null) {
				count = 1L;
			} else {
				count++;
			}
			dist.put(key, count++);
			maxCount = Math.max(maxCount, count);
		}

		return dist;
	}

	private List<Long> getScaledSequence(double factor) {

		ArrayList<Long> seq = new ArrayList<Long>();
		long minValue = Long.MAX_VALUE;
		long maxValue = 0;
		int length = sequence.size();
		for (int i = 0; i < length; i++) {
			minValue = Math.min(minValue, sequence.get(i));
			maxValue = Math.max(maxValue, sequence.get(i));
		}

		// for now assume minimum value of 0
		for (int i = 0; i < length; i++) {
			// seq.add((sequence.get(i) * targetSize-1) / maxValue);
			seq.add(new Double(sequence.get(i) / factor).longValue());
		}
		return seq;

	}

	public Long getMaxValue() {
		long maxValue = 0;
		int length = sequence.size();
		for (int i = 0; i < length; i++) {
			maxValue = Math.max(maxValue, sequence.get(i));
		}
		return new Long(maxValue);
	}

	private void addPoint(BufferedImage im, int x, int y) {

		if (x > width - 1)
			return;
		if (y > height - 1)
			return;
		int col = 0;
		try {
			col = im.getRGB(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" x = " + x + "    y=" + y);
			throw new RuntimeException(e);
		}
		Color currentGray = new Color(col);
		Color newGray = this.addGray(currentGray, -128);
		im.setRGB(x, y, newGray.getRGB());
	}

	private Color addGray(Color c, int i) {
		int r = Math.max(c.getRed() + i, 0);
		int g = Math.max(c.getGreen() + i, 0);
		int b = Math.max(c.getBlue() + i, 0);

		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);

		return new Color(r, g, b);
	}

	public int grayLevel(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return (r + g + b) / 3;
	}

	public Color gray(int level) {
		int r = level;
		int g = level;
		int b = level;
		return new Color(r, g, b);
	}

	public static Boolean getBit(int pos, Long l) {
		return ((l & (1L << pos)) != 0);
	}

	public Long[][] computeBitStats() {

		Long[][] a = new Long[2][64];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 64; j++) {
				a[i][j] = new Long(0);
			}
		}

		for (int i = 0; i < sequence.size(); i++) {
			for (int j = 0; j < 64; j++) {
				if (NumberPattern.getBit(j, sequence.get(i))) {
					a[0][j]++;
				} else {
					a[1][j]++;
				}
			}
		}

		return a;
	}

	public Long[] computeLastEightStats() {

		int numBits = 8;
		int numValues = numBits << 1;

		Long[] count = new Long[numValues];
		for (int i = 0; i < sequence.size(); i++) {
			count[i] = new Long(0L);
		}

		for (int i = 0; i < sequence.size(); i++) {
			Integer index = getNumRightBitsAsInteger(numBits, sequence.get(i));
			count[index]++;
		}

		return count;

	}

	public void printStats(Long[] count, File file) {

		try {
			FileWriter fw = new FileWriter(file);

			for (int i = 0; i < count.length; i++) {
				fw.write(i + "," + count[i]);
				;
			}
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot write file: " + file.getAbsolutePath());
		}

	}

	private Integer getNumRightBitsAsInteger(int numBits, Long value) {
		Long r = 1L << numBits - 1;
		Long result = value & r;
		return result.intValue();
	}

	public void printBitStats(Long[][] a, File file) {
		try {
			FileWriter fw = new FileWriter(file);

			for (int i = 0; i < 64; i++) {
				Double setPercent = new Double(a[0][i] * 100) / new Double((a[0][i] + a[1][i]));
				fw.write("bit " + i + " -> " + String.format("%1$.3f", setPercent) + "\n");
			}
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot write file: " + file.getAbsolutePath());
		}
	}

	public void printFactorStats(File file) {
		try {
			FileWriter fw = new FileWriter(file);

			for (int i = 0; i < sequence.size(); i++) {
				fw.write(sequence.get(i) + ",");
				if (sequence.get(i) < Integer.MAX_VALUE && sequence.get(i) > 1) {
					List<Integer> factors = Primes.primeFactors(sequence.get(i).intValue());
					fw.write(i + "," + factors.size());
					for (int j = 0; j < factors.size(); j++) {
						fw.write("," + j);
						
					}
					fw.write("\n");
				} else {
					fw.write("0\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot write file: " + file.getAbsolutePath());
		}

	}
}