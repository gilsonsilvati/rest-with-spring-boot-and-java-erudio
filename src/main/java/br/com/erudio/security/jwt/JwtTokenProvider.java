package br.com.erudio.security.jwt;

import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.exceptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class JwtTokenProvider {

	private static final int THREE_HOURS = 3;

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-length:3600000}")
	private final long validityInMilliseconds = 3600000; // 1h
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}

	public TokenVO createAccessToken(String username, List<String> roles) {
		var now = new Date();
		var validity = new Date(now.getTime() + validityInMilliseconds);

		var accessToken = getAccessToken(username, roles, now, validity);
		var refreshToken = getRefreshToken(username, roles, now);
		
		return new TokenVO(username, true, now, validity, accessToken, refreshToken);
	}

	public TokenVO refreshToken(String refreshToken) {
		if (refreshToken.startsWith("Bearer ")) {
			refreshToken = refreshToken.substring("Bearer ".length());
		}

		var decodedJWT = decodedToken(refreshToken);

		var username = decodedJWT.getSubject();
		var roles = decodedJWT.getClaim("roles").asList(String.class);

		return createAccessToken(username, roles);
	}

	private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
		var issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(algorithm)
				.strip();
	}
	
	private String getRefreshToken(String username, List<String> roles, Date now) {
		var validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds * THREE_HOURS));

		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validityRefreshToken)
				.withSubject(username)
				.sign(algorithm)
				.strip();
	}
	
	public Authentication getAuthentication(String token) {
		var decodedJWT = decodedToken(token);
		var userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		var verifier = JWT.require(algorithm).build();

		return verifier.verify(token);
	}
	
	public String resolveToken(HttpServletRequest req) {
		var bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (Objects.nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}

		return null;
	}
	
	public boolean validateToken(String token) {
		var decodedJWT = decodedToken(token);

		try {
			return !decodedJWT.getExpiresAt().before(new Date());
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
		}
	}
}
