package sk.tuke.kpi.eprez.web.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageServlet.class);

	private byte[] image1Placeholder;
	private byte[] image2Placeholder;

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);

		try (final InputStream placeholder1InputStream = getClass().getResourceAsStream("/templates/placeholder.png");
				final InputStream placeholder2InputStream = getClass().getResourceAsStream("/templates/placeholder2.jpg")) {
			image1Placeholder = IOUtils.toByteArray(placeholder1InputStream);
			image2Placeholder = IOUtils.toByteArray(placeholder2InputStream);
		} catch (final Exception e) {
			LOGGER.error("Cannot retrieve image placeholder", e);
		}
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final String id = req.getParameter("id");
		final PresentationDao presentationDao = WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(PresentationDao.class);
		final Presentation presentation = presentationDao.findOne(id);

		final byte[] imageData;
		if (presentation == null || presentation.getImage() == null) {
			imageData = "1".equals(req.getParameter("placeholder")) ? image1Placeholder : image2Placeholder;
		} else {
			imageData = presentation.getImage().getContent();
		}
		resp.setContentLength(imageData.length);
		IOUtils.write(imageData, resp.getOutputStream());
	}
}
