package se.subject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.subject.entities.Space;

@Repository
public interface ISpaceRepository extends CrudRepository<Space, Long> {
	List<Space> findTop10ByActiveTrueOrderByUpdatedDesc();
	List<Space> findAllByActiveTrueOrderByUpdatedDesc();
	List<Space> findAllByActiveFalseOrderByUpdatedDesc();
}