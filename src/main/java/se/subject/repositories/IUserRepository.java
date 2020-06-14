package se.subject.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.subject.entities.User;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
	User findByEmailAndActiveTrue(String email);
}