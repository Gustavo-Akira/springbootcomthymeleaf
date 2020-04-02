package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.springboot.models.Users;
import curso.springboot.repository.UsersRepository;
@Service
public class ImplementUserService implements UserDetailsService {
	
	@Autowired
	private UsersRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = repository.findUserByLogin(username);
		if(user == null) {
			throw new UsernameNotFoundException("Usuario não foi encontrado");
		}
		return new User(user.getLogin(), user.getPassword(), user.isEnabled(), true, true, true, user.getAuthorities());
	}

}
