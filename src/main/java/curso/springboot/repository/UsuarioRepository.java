package curso.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.models.Usuarios;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {
	@Query("select u from Usuarios u where u.nome like %?1%")
	List<Usuarios> findUsuarioByName(String name);

	@Query("select u from Usuarios u where u.nome like %?1% and u.sexo = ?2")
	List<Usuarios> findUsuarioBySexoAndName(String nome, String sexo);

	@Query("select u from Usuarios u where u.sexo = ?1")
	List<Usuarios> findbySex(String sexo);

	default Page<Usuarios> findPessoaByNamePage(String nome , Pageable pageable) {
		Usuarios usuario = new Usuarios();
		usuario.setNome(nome);
		ExampleMatcher example = ExampleMatcher.matchingAny().withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Usuarios> examle = Example.of(usuario,example);
		Page<Usuarios> usuarios = findAll(examle,pageable);
		return usuarios;
	}
	default Page<Usuarios> findPessoaByNameandSexPage(String nome , String sexo,Pageable pageable) {
		Usuarios usuario = new Usuarios();
		usuario.setNome(nome);
		usuario.setSexo(sexo);
		ExampleMatcher example = ExampleMatcher.matchingAny().withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()).withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Usuarios> examle = Example.of(usuario,example);
		Page<Usuarios> usuarios = findAll(examle,pageable);
		return usuarios;
	}
	default Page<Usuarios> findPessoaBySexoPage(String sexo , Pageable pageable) {
		Usuarios usuario = new Usuarios();
		usuario.setNome(sexo);
		ExampleMatcher example = ExampleMatcher.matchingAny().withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Usuarios> examle = Example.of(usuario,example);
		Page<Usuarios> usuarios = findAll(examle,pageable);
		return usuarios;
	}
}
