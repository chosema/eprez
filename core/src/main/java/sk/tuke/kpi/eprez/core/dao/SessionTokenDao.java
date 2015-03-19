package sk.tuke.kpi.eprez.core.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.eprez.core.model.SessionToken;

public interface SessionTokenDao extends MongoRepository<SessionToken, String> {
}
