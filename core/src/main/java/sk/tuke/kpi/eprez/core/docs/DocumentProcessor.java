package sk.tuke.kpi.eprez.core.docs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentProcessor {

	static final Logger LOGGER = LoggerFactory.getLogger(DocumentProcessor.class);

	public static final int DEFAULT_RESOLUTION = 160;
	public static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;
	public static final String DEFAULT_IMAGE_FORMAT = "png";

	private final int resolution;
	private final int imageType;
	private final String imageFormat;

	private final boolean useNonSeqParser = false;

	private final int startPage = 1;
	private final int endPage = Integer.MAX_VALUE;

	protected final float cropBoxLowerLeftX = 0;
	protected final float cropBoxLowerLeftY = 0;
	protected final float cropBoxUpperRightX = 0;
	protected final float cropBoxUpperRightY = 0;

	public DocumentProcessor() {
		this(DEFAULT_RESOLUTION, DEFAULT_IMAGE_FORMAT, DEFAULT_IMAGE_TYPE);
	}

	public DocumentProcessor(final int resolution, final String imageFormat) {
		this(resolution, imageFormat, DEFAULT_IMAGE_TYPE);
	}

	public DocumentProcessor(final int resolution, final String imageFormat, final int imageType) {
		this.resolution = resolution;
		this.imageFormat = imageFormat;
		this.imageType = imageType;
	}

	public DocumentProcessor(final int resolution, final String imageFormat, final String color) {
		this.resolution = resolution;
		this.imageFormat = imageFormat;
		this.imageType = parseImageType(color);
	}

	private int parseImageType(final String color) {
		int imageType = 24;
		if ("bilevel".equalsIgnoreCase(color)) {
			imageType = BufferedImage.TYPE_BYTE_BINARY;
		} else if ("indexed".equalsIgnoreCase(color)) {
			imageType = BufferedImage.TYPE_BYTE_INDEXED;
		} else if ("gray".equalsIgnoreCase(color)) {
			imageType = BufferedImage.TYPE_BYTE_GRAY;
		} else if ("rgb".equalsIgnoreCase(color)) {
			imageType = BufferedImage.TYPE_INT_RGB;
		} else if ("rgba".equalsIgnoreCase(color)) {
			imageType = BufferedImage.TYPE_INT_ARGB;
		}
		return imageType;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public String getImageContentType() {
		return DEFAULT_IMAGE_FORMAT.equals(imageFormat) ? "image/png" : null;
	}

	public void process(final InputStream pdfFile, final PageProcessedCallback callback) throws Exception {
		process(pdfFile, callback, null);
	}

	public void process(final InputStream pdfFile, final PageProcessedCallback callback, final String password) throws Exception {
		LOGGER.info("PDF document processing started");
		PDDocument document = null;
		try {
			if (useNonSeqParser) {
				document = PDDocument.loadNonSeq(pdfFile, null, password);
			} else {
				document = PDDocument.load(pdfFile);
				if (document.isEncrypted()) {
					document.decrypt(password);
				}
			}
			LOGGER.info("Document loaded sucessfully with " + document.getNumberOfPages() + " pages");

			// if a CropBox has been specified, update the CropBox:
			// changeCropBoxes(PDDocument document,float a, float b, float c,float d)
//			if (cropBoxLowerLeftX != 0 || cropBoxLowerLeftY != 0 || cropBoxUpperRightX != 0 || cropBoxUpperRightY != 0) {
//				changeCropBoxes(document, cropBoxLowerLeftX, cropBoxLowerLeftY, cropBoxUpperRightX, cropBoxUpperRightY);
//			}

			// Make the call
			convert(document, callback);
		} finally {
			IOUtils.closeQuietly(document);
		}
	}

	protected void convert(final PDDocument document, final PageProcessedCallback callback) throws IOException {
		@SuppressWarnings("unchecked")
		final List<PDPage> pages = document.getDocumentCatalog().getAllPages();
		final int pagesSize = pages.size();

		int index = 0;
		for (int i = startPage - 1; i < endPage && i < pagesSize; i++) {
			LOGGER.info("Converting page " + (i + 1) + " of " + pagesSize);

			final PDPage page = pages.get(i);
			final BufferedImage image = page.convertToImage(imageType, resolution);

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final boolean successfull = ImageIOUtil.writeImage(image, imageFormat, outputStream, resolution);
			if (successfull) {
				callback.pageProcessed(index++, outputStream.toByteArray(), image);
			} else {
				throw new IllegalArgumentException("Error: no writer found for image format '" + imageFormat + "'");
			}
		}

		LOGGER.info("Conversion successfull");
	}

	protected void changeCropBoxes(final PDDocument document, final float a, final float b, final float c, final float d) {
		final List<?> pages = document.getDocumentCatalog().getAllPages();
		for (int i = 0; i < pages.size(); i++) {
			System.out.println("resizing page");
			final PDPage page = (PDPage) pages.get(i);
			final PDRectangle rectangle = new PDRectangle();
			rectangle.setLowerLeftX(a);
			rectangle.setLowerLeftY(b);
			rectangle.setUpperRightX(c);
			rectangle.setUpperRightY(d);
			page.setMediaBox(rectangle);
			page.setCropBox(rectangle);
		}
	}
}
