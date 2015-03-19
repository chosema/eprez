package sk.tuke.kpi.eprez.core.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.User;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;

public interface PresentationDao extends MongoRepository<Presentation, String> {

	Page<Presentation> findByPublished(boolean published, Pageable pageable);

	Page<Presentation> findByPublishedAndCategories(boolean published, Collection<PresentationCategory> categories, Pageable pageable);

	Page<Presentation> findByCreatedBy(User user, Pageable pageable);

	Page<Presentation> findByCreatedByAndCategories(User user, List<PresentationCategory> categories, Pageable pageable);

	long countBySessionTokens();
}
