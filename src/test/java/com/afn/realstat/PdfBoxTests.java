package com.afn.realstat;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)

@ActiveProfiles("prod")
public class PdfBoxTests {

	@Test
	public void testPDFTestStripper() {

		File file = new File(AppFiles.getTestDataDir(), "17-03-25_Tour.pdf");

		String text = getText(file);
		System.out.print(text);

	}

	@Test
	public String getText(File file) {

		String text = null;
		try {
			PDDocument doc = PDDocument.load(file);
			PDFTextStripper pdfStrip = new PDFTextStripper();
			pdfStrip.setArticleStart("");
			pdfStrip.setArticleEnd("^");
			pdfStrip.setParagraphStart("");
			pdfStrip.setParagraphEnd("\n");
			pdfStrip.setWordSeparator("|");
			pdfStrip.setLineSeparator("|");
			pdfStrip.setSortByPosition(true);

			text = pdfStrip.getText(doc);

			System.out.println("|" + pdfStrip.getArticleStart() + "|");
			System.out.println("|" + pdfStrip.getArticleEnd() + "|");
			System.out.println("|" + pdfStrip.getParagraphStart() + "|");
			System.out.println("|" + pdfStrip.getParagraphEnd() + "|");
			System.out.println("|" + pdfStrip.getWordSeparator() + "|");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return text;

	}

}
