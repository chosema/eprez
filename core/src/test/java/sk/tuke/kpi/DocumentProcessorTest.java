package sk.tuke.kpi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import sk.tuke.kpi.eprez.core.docs.DocumentProcessor;
import sk.tuke.kpi.eprez.core.docs.PageProcessedCallback;

public class DocumentProcessorTest {

	public static void main(final String[] args) throws Exception {
		final InputStream pdfFile = new FileInputStream("C:\\Users\\pchov_000\\pdfToImage\\Diplomovy_projekt_vydanie.pdf");

		new DocumentProcessor().process(pdfFile, new PageProcessedCallback() {
			int pageIndex = 1;

			@Override
			public void pageProcessed(final int index, final byte[] imageData, final BufferedImage image) {
				System.out.println("Processing page: " + pageIndex);
				try {
					FileUtils.writeByteArrayToFile(new File("C:\\Users\\pchov_000\\pdfToImage\\output" + pageIndex++ + ".png"), imageData);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		});

		IOUtils.closeQuietly(pdfFile);
	}

}
