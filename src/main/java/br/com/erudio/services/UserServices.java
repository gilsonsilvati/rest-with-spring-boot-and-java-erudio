package br.com.erudio.services;

import br.com.erudio.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServices implements UserDetailsService {
	
	private final Logger logger = Logger.getLogger(UserServices.class.getName());
	
	private final UserRepository repository;

	public UserServices(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		logger.log(Level.INFO, "Finding one user by name {}", username);

		return repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
	}
}
