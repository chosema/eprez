package sk.tuke.kpi.core.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.core.model.User;

public interface UserDao extends MongoRepository<User, String> {

	List<User> findByFirstName(String firstName);

}
