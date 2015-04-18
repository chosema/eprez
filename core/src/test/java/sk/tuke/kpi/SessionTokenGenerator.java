package sk.tuke.kpi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import sk.tuke.kpi.eprez.core.CoreConfig;
import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.dao.SessionTokenDao;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.Presentation.Session;
import sk.tuke.kpi.eprez.core.model.SessionToken;

public class SessionTokenGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionTokenGenerator.class);

	public static void main(final String[] args) {
		final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CoreConfig.class);
		final PresentationDao presentationDao = context.getBean(PresentationDao.class);
		final SessionTokenDao sessionTokenDao = context.getBean(SessionTokenDao.class);

		final Presentation presentation = presentationDao.findOne("54fb5905ddccdd16d857f5be");
		final Session session = presentation.getSession();

//		for (int i = 0; i < 10000; i++) {
//			final SessionToken sessionToken = sessionTokenDao.save(new SessionToken(null));
//			LOGGER.info("Created new session token with id: " + sessionToken.getId());
//			session.getTokens().add(sessionToken);
//		}
//		presentationDao.save(presentation);

		for (final SessionToken token : session.getTokens()) {
			System.out.println(token.getId() + ";");
		}

		context.close();
	}
}
