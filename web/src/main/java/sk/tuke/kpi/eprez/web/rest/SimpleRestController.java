package sk.tuke.kpi.eprez.web.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.User;
import sk.tuke.kpi.eprez.web.security.LoggedUserProvider;

@RestController
@RequestMapping("/rest")
public class SimpleRestController {

	//private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRestController.class);

	@Autowired
	PresentationDao presentationDao;

	@Autowired
	LoggedUserProvider userProvider;

	@RequestMapping(value = "/test.json", method = RequestMethod.GET)
	public String test() {
		return "OK";
	}

	@RequestMapping(value = "/info/{token}.json", method = RequestMethod.GET)
	public Map<String, Object> info(@PathVariable("token") final String sessionToken) {
		final Presentation presentation = presentationDao.findBySessionTokensId(sessionToken);
		final User user = userProvider.getUser();

		final Map<String, Object> result = new HashMap<>();
		result.put("presentation", presentation);
		result.put("user", user);
		result.put("userIsPresenter", presentation.getSession().getTokens().stream().filter(token -> token.getId().equals(sessionToken)).collect(Collectors.toList()).get(0)
				.isPresenter());
		return result;
	}
}
