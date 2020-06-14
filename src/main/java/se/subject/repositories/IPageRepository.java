package se.subject.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.subject.entities.Page;

@Repository
public interface IPageRepository extends CrudRepository<Page, Long> {
}