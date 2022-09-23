package br.com.erudio.services;

import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.model.User;
import br.com.erudio.repositories.UserRepository;
import br.com.erudio.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenVO> singIn(AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            var authentication = new UsernamePasswordAuthenticationToken(username, password);

            authenticationManager.authenticate(authentication);

            User user = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

            var tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Invalid username/password supplied!", e);
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }
}
