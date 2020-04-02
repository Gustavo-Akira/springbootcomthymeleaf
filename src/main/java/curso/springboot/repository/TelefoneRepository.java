package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.models.Telefones;

@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefones, Long>{
	
	@Query("select t from Telefones t where t.usuario.id = ?1")
	public List<Telefones> findByUsuario(Long usuarioId);
}
