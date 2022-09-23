package br.com.erudio.config;

import br.com.erudio.security.jwt.JwtConfigurer;
import br.com.erudio.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ID_FOR_ENCODE = "pbkdf2";

    @Autowired
    private JwtTokenProvider tokenProvider;

    public PasswordEncoder passwordEncoder() {
        var pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(ID_FOR_ENCODE, pbkdf2PasswordEncoder);

        var passwordEncoder = new DelegatingPasswordEncoder(ID_FOR_ENCODE, encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);

        return passwordEncoder;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .authorizeRequests()
                        .antMatchers(
                                "/auth/signin",
                                "/auth/refresh",
                                "/api-docs/**",
                                "/swagger-ui.html**").permitAll() // swagger-ui/index.html
                        .antMatchers("/api/**").authenticated()
                        .antMatchers("/users").denyAll()
                    .and()
                        .cors()
                    .and()
                        .apply(new JwtConfigurer(tokenProvider));
    }
}
