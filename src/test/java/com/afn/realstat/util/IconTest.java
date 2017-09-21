package com.afn.realstat.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.afn.realstat.AppFiles;
import com.google.common.base.Strings;

public class IconTest {

	@Test
	public void testIconBasic() {
		testIcon(Icon.markerButtonBlue, null, null);
	}

	@Test
	public void testIconText() {
		testIcon(Icon.markerButtonGreen, "33",null);
	}

	@Test
	public void testIconScale() {
		testIcon(Icon.markerButtonRed, "44", 0.8);
	}
	

	@Test
	public void testIconDescendingCharactors() {
		testIcon(Icon.markerButtonBlue, "gg", null);
	}
	
	@Test
	public void testIconMixedCharacters() {
		testIcon(Icon.markerButtonBlue, "Gg", 2.0);
	}
	
	@Test
	public void testIconSingeCharacters() {
		testIcon(Icon.markerButtonBlue, "5", 0.8);
	}
	
	private void testIcon(Integer baseIcon, String text, Double scale) {
		Icon icon = new Icon(baseIcon,text,scale);
		
		String iconUrl = icon.getIconUrl();
		String iconStr = Strings.nullToEmpty(Integer.toString(baseIcon));
		String textParm = "?text=";
		if (text == null) {
			textParm = "";
			text = "";
		}
		assertEquals("/icon/" + iconStr + textParm + text, iconUrl);
	
		String iconBaseName = FilenameUtils.getBaseName(icon.getBaseFileName());
		String extStr = FilenameUtils.getExtension(icon.getBaseFileName());
		if (scale == null) {
			scale = 1.0;
		}
		String scaleStr = Integer.toString((int) (100 * scale));
		String iconFileName = iconBaseName + "_" + iconStr + "_" + text + "_" + scaleStr + "." + extStr;
		writeIcon(iconFileName, icon);
		
	}

	/*
	 * Writes the icon to test output directory for verification
	 */
	private void writeIcon(String fileName, Icon icon) {
		
		byte[] iconBytes = icon.getIcon();

		File file = null;
		try {
			file = new File(AppFiles.getTestOutputDir(), fileName);
			// file = File.createTempFile(iconName, fileExtension, new File(AppFiles.getTestOutputDir()));
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(iconBytes);
			fos.close();
		} catch (IOException e) {
			fail();
		}

	}
}
