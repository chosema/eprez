package sk.tuke.kpi.core.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.core.model.Presentation;

public interface PresentationDao extends MongoRepository<Presentation, Long> {
}
