package sk.tuke.kpi.eprez.core.docs;

import java.awt.image.BufferedImage;

public interface PageProcessedCallback {

	void pageProcessed(int index, byte[] imageData, BufferedImage image);

}
