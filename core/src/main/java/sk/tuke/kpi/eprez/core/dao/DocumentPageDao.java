package sk.tuke.kpi.eprez.core.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.eprez.core.model.DocumentPage;

public interface DocumentPageDao extends MongoRepository<DocumentPage, String> {
}
