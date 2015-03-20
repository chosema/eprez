package sk.tuke.kpi.eprez.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.criteria.PageRequest;
import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;
import sk.tuke.kpi.eprez.web.data.AbstractSpringLazyDataModel;

@Controller
@Scope("view")
public class OverviewPresentationsController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OverviewPresentationsController.class);

	@Autowired
	transient PresentationDao presentationDao;

	private String userParameter;
	private List<PresentationCategory> categories = new ArrayList<>();

	private final LazyDataModel<Presentation> presentations = new AbstractSpringLazyDataModel<Presentation>() {
		private static final long serialVersionUID = 1L;

		@Override
		protected Page<Presentation> getData(final PageRequest pageRequest) {
			if (userParameter == null) {
				if (getCategories().isEmpty()) {
					return presentationDao.findByPublished(true, convert(pageRequest));
				} else {
					return presentationDao.findByPublishedAndCategories(true, getCategories(), convert(pageRequest));
				}
			} else {
				if (getCategories().isEmpty()) {
					return presentationDao.findByCreatedBy(loggedUser, convert(pageRequest));
				} else {
					return presentationDao.findByCreatedByAndCategories(loggedUser, getCategories(), convert(pageRequest));
				}
			}
		}
	};

	public void onCategorySelect(final SelectEvent event) {
		final PresentationCategory category = getCategory(event.getObject());
		if (getCategories().contains(category)) {
			LOGGER.info("Categories filter already contain category " + category);
		} else {
			LOGGER.info("Adding category " + category + " to categories filter");
			getCategories().add(category);
		}
	}

	public void onCategoryUnselect(final UnselectEvent event) {
		final PresentationCategory category = getCategory(event.getObject());
		LOGGER.info("Removing category " + category + " from categories filter");
		getCategories().remove(category);
	}

	private PresentationCategory getCategory(final Object selected) {
		final PresentationCategory category;
		if (selected instanceof PresentationCategory) {
			category = (PresentationCategory) selected;
		} else if (selected instanceof String) {
			category = PresentationCategory.valueOfByLabel(String.valueOf(selected));
		} else {
			throw new IllegalArgumentException("Unknown selected category: " + selected);
		}
		return category;
	}

	public List<PresentationCategory> completeCategory(final String query) {
		LOGGER.info("Searching categories with query: " + query);
		final Predicate<PresentationCategory> availableCategoriesPredicate = category -> !getCategories().contains(category);
		final Predicate<PresentationCategory> queryPredicate = category -> StringUtils.isEmpty(query) ? true : StringUtils.startsWithIgnoreCase(category.getLabel(), query);
		return Arrays.stream(PresentationCategory.values()).filter(availableCategoriesPredicate).filter(queryPredicate).collect(Collectors.toList());
	}

	public LazyDataModel<Presentation> getPresentations() {
		return presentations;
	}

	public String getUserParameter() {
		return userParameter;
	}

	public void setUserParameter(final String userParameter) {
		this.userParameter = userParameter;
	}

	public List<PresentationCategory> getCategories() {
		return categories == null ? (categories = new ArrayList<PresentationCategory>()) : categories;
	}

	public void setCategories(final List<PresentationCategory> categories) {
		this.categories = categories;
	}
}
