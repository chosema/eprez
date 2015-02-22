package sk.tuke.kpi.eprez.core.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.eprez.core.model.User;

public interface UserDao extends MongoRepository<User, String> {

	User findByLogin(String login);

}
