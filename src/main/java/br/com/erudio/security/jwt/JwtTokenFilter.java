package br.com.erudio.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static java.util.Objects.nonNull;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var token = tokenProvider.resolveToken((HttpServletRequest) request);

        if (nonNull(token) && tokenProvider.validateToken(token)) {
            var authentication = tokenProvider.getAuthentication(token);

            if (nonNull(authentication)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
