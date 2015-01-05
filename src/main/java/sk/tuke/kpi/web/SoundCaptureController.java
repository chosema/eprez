package sk.tuke.kpi.web;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SoundCaptureController {

	int index = 0;

	@RequestMapping(value = "/soundCapture")
	public String index() {
		return "soundCapture";
	}

	@RequestMapping(value = "/capture", method = RequestMethod.POST)
	public String capture(final HttpServletRequest req, @RequestParam("audio") final MultipartFile audio) {
		System.out.println();
		try {
			final byte[] bytes = audio.getBytes();
			final String contentType = audio.getContentType();

			System.out.println("Resulting bytes: " + bytes);
			System.out.println("Resulting bytes length: " + bytes.length);
			System.out.println("Resulting content type: " + contentType);

			try {
				// The temporary file that contains our captured audio stream
				final String pathname = "C:\\Users\\pchov_000\\Desktop\\out" + index++ + ".wav";
				System.out.println("Starting new recording.");
				final FileOutputStream fOut = new FileOutputStream(pathname, true);
				fOut.write(bytes);
				fOut.close();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return "soundCapture";
	}

}
