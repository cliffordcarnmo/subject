/*
    Subject
    Copyright (C) 2020 Clifford Carnmo

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package se.subject.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.subject.entities.Page;
import se.subject.entities.Space;
import se.subject.entities.User;

@Repository
public interface IPageRepository extends CrudRepository<Page, Integer> {
	List<Page> findTop5BySpaceOrderByUpdatedDesc(Space space);
	List<Page> findTop10BySpaceOrderByUpdatedDesc(Space space);
	List<Page> findAllBySpaceOrderByUpdatedDesc(Space space);
	List<Page> findAllByUserOrderByUpdatedDesc(User user);
	List<Page> findAllByUser(User user);
	Optional<Page> findByUrl(String url);
}