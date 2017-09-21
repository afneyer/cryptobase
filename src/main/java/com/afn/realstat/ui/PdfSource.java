package com.afn.realstat.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public class PdfSource implements StreamSource {

	private File pdfFile;

	public PdfSource(File pdfFile) {
		this.pdfFile = pdfFile;
	}

	// TODO factor out
	@Override
	public InputStream getStream() {
		
		InputStream in = null;
		try {
			in = new FileInputStream(pdfFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}
}
