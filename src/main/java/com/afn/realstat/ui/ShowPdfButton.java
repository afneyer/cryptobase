package com.afn.realstat.ui;

import java.io.File;
import java.io.InputStream;

import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;

import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class ShowPdfButton extends Button  {
	
	private PdfFileGetter pdfFileGetter;

	
	ShowPdfButton() {
		new EnhancedBrowserWindowOpener()
	    .clientSide(true)
	    .withGeneratedContent("myFileName.pdf", this::getStreamSource)
	    .doExtend(this);
	}

	public InputStream getStreamSource() {
		
		File file = pdfFileGetter.getPdfFile();
		PdfSource src = new PdfSource(file);
		return src.getStream();
	}
	
	
	
	public void setPdfFileGetter( PdfFileGetter pdfFileGetter ) {
		this.pdfFileGetter = pdfFileGetter;
	}

	public interface PdfFileGetter {
		public File getPdfFile();
	}
};
