package curso.springboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.models.Users;
@Repository
@Transactional
public interface UsersRepository extends CrudRepository<Users,Long>{
	@Query("select u from Users u where u.login = ?1")
	Users findUserByLogin(String login);
}
