package se.subject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.subject.entities.Page;
import se.subject.entities.Space;

@Repository
public interface IPageRepository extends CrudRepository<Page, Long> {
	List<Page> findTop5ByActiveTrueAndSpaceOrderByUpdatedDesc(Space space);
	List<Page> findTop10ByActiveTrueAndSpaceOrderByUpdatedDesc(Space space);
	List<Page> findAllByActiveTrueAndSpaceOrderByUpdatedDesc(Space space);
	List<Page> findAllByActiveFalseAndSpaceOrderByUpdatedDesc(Space space);
}