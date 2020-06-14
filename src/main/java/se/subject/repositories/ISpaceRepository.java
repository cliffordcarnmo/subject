package se.subject.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.subject.entities.Space;

@Repository
public interface ISpaceRepository extends CrudRepository<Space, Long> {
}