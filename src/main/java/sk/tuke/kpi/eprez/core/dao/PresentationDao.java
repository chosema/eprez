package sk.tuke.kpi.eprez.core.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.eprez.core.model.Presentation;

public interface PresentationDao extends MongoRepository<Presentation, String> {
}
